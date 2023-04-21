package com.trinea.sns.serviceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import weibo4android.Comment;
import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.trinea.sns.activity.OAuthConstant;
import com.trinea.sns.activity.WebViewActivity;
import com.trinea.sns.entity.CommentInfo;
import com.trinea.sns.entity.StatusInfo;
import com.trinea.sns.entity.UpdateStatusInfo;
import com.trinea.sns.entity.User;
import com.trinea.sns.entity.UserInfo;
import com.trinea.sns.service.SnsService;
import com.trinea.sns.util.SnsConstant;
import com.trinea.sns.utilImpl.StringUtils;

public class SinaServiceImpl extends SnsService {

    protected static String websiteType = SnsConstant.SINA;

    /**
     * OAuth���ֲμ�<a href="http://open.weibo.com/wiki/Oauth">http://open.weibo.com/wiki/Oauth</a>
     */
    @Override
    public Intent auth(Context context, String callBackUrl) {
        String oAuthUrl;
        try {
            oAuthUrl = SnsConstant.getOAuthProvider(websiteType).retrieveRequestToken(SnsConstant.getOAuthConsumer(websiteType), callBackUrl);
        } catch (Exception e) {
            return null;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SnsConstant.OAUTH_URL, oAuthUrl);
        bundle.putString(SnsConstant.WEBSITE_TYPE, websiteType);
        bundle.putString(SnsConstant.CALL_BACK_URL, callBackUrl);
        intent.putExtras(bundle);
        intent.setClass(context, WebViewActivity.class);
        return intent;
    }

    @Override
    public UserInfo authBack(Uri uri, String requestTokenSecret) {
        return authBack(uri);
    }

    /**
     * ��webView���غ��oauth_verifier
     */
    @Override
    public UserInfo authBack(Uri uri) {
        if (uri != null) {
            try {
                SnsConstant.getOAuthProvider(websiteType).setOAuth10a(true);
                SnsConstant.getOAuthProvider(websiteType).retrieveAccessToken(SnsConstant.getOAuthConsumer(websiteType), uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER));
            } catch (Exception e) {
                return null;
            }
            SortedSet<String> userInfoSet = SnsConstant.getOAuthProvider(websiteType).getResponseParameters().get("user_id");
            return createUserInfo(websiteType, ((userInfoSet != null && !userInfoSet.isEmpty()) ? userInfoSet.first() : null), SnsConstant.getOAuthConsumer(websiteType).getToken(), SnsConstant.getOAuthConsumer(websiteType).getTokenSecret());
        }
        return null;
    }

    /**
     * �õ��û������ע���ѵ�����΢����Ϣ
     * 
     * @param userInfo �û���Ϣ
     * @param page ҳ��
     * @return
     */
    @Override
    public List<StatusInfo> getStatusesOfAll(UserInfo userInfo, int page, long lastPageTime) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Status> statusList = null;
        try {
            statusList = weibo.getFriendsTimeline(paging);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformStatus(statusList);
        }
        return transformStatus(statusList);
    }

    @Override
    public StatusInfo getStatus(UserInfo userInfo, long statusId) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Status status = null;
        try {
            status = weibo.showStatus(statusId);
        } catch (WeiboException e) {
            e.printStackTrace();
            return null;
        }
        return transformStatus(status);
    }

    /**
     * ת��״̬��
     * 
     * @param statusList ״̬lilst
     * @return
     */
    protected List<StatusInfo> transformStatus(List<Status> statusList) {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
        if (statusList != null) {
            for (Status status : statusList) {
                statusInfoList.add(transformStatus(status));
            }
        }
        return statusInfoList;
    }

    /**
     * ת��״̬
     * 
     * @param status ״̬
     * @return
     */
    protected StatusInfo transformStatus(Status status) {
        if (status == null) {
            return null;
        }
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setUser(transformUser(status.getUser()));
        statusInfo.setStatusId(status.getId());
        statusInfo.setStatusContent(status.getText());
        statusInfo.setTime(status.getCreatedAt().getTime());
        statusInfo.setSourceType(StringUtils.getHrefInnerHtml(status.getSource()));
        if (!StringUtils.isBlank(status.getThumbnail_pic())) {
            statusInfo.setIsContainImage(true);
            statusInfo.setSmallPictureUrl(status.getThumbnail_pic());
            statusInfo.setMiddlePictureUrl(status.getBmiddle_pic());
            statusInfo.setSourcePictureUrl(status.getOriginal_pic());
        } else {
            statusInfo.setIsContainImage(false);
        }
        Status sourceStatus = status.getRetweeted_status();
        if (sourceStatus != null) {
            StatusInfo sourceStatusInfo = transformStatus(sourceStatus);
            statusInfo.setSourceStatus(sourceStatusInfo);
            if (sourceStatusInfo.isContainImage()) {
                statusInfo.setIsContainImage(true);
                statusInfo.setSmallPictureUrl(sourceStatusInfo.getSmallPictureUrl());
                statusInfo.setMiddlePictureUrl(sourceStatusInfo.getMiddlePictureUrl());
                statusInfo.setSourcePictureUrl(sourceStatusInfo.getSourcePictureUrl());
            }
        }
        return statusInfo;
    }

    /**
     * ת��user
     * 
     * @param sinaUser �����û���Ϣ
     * @return
     */
    protected User transformUser(weibo4android.User sinaUser) {
        if (sinaUser == null) {
            return null;
        }
        User user = new User();
        user.setUserId(sinaUser.getId());
        user.setUserName(sinaUser.getName());
        user.setIconUrl(sinaUser.getProfileImageURL().toString());
        user.setFollowersCount(sinaUser.getFollowersCount());
        user.setFriendsCount(sinaUser.getFriendsCount());
        user.setStatusesCount(sinaUser.getStatusesCount());
        user.setUserDescription(sinaUser.getDescription());
        return user;
    }

    @Override
    public int updateStatus(UserInfo userInfo, UpdateStatusInfo status) {
        if (status == null) {
            SnsConstant.SINA_OPERATOR_FAIL_REASON = "statusΪ��";
            return SnsConstant.SINA_UPDATE_STATUS_FAIL_WHAT;
        }
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        try {
            weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
            if (status.isContainImage()) {
                File imageFile = new File(status.getImageFilePath());
                if (imageFile == null || !imageFile.exists()) {
                    SnsConstant.SINA_OPERATOR_FAIL_REASON = processException("ͼƬ������");
                    return SnsConstant.SINA_UPDATE_STATUS_FAIL_WHAT;
                }
                Status updateResult = weibo.uploadStatus(StringUtils.utf8Encode(status.getStatusContent()), imageFile);
                return (updateResult != null ? SnsConstant.SINA_UPDATE_STATUS_SUCC_WHAT : SnsConstant.SINA_UPDATE_STATUS_FAIL_WHAT);
            } else {
                Status updateResult = weibo.updateStatus(StringUtils.utf8Encode(status.getStatusContent()));
                return (updateResult != null ? SnsConstant.SINA_UPDATE_STATUS_SUCC_WHAT : SnsConstant.SINA_UPDATE_STATUS_FAIL_WHAT);
            }
        } catch (Exception e) {
            SnsConstant.SINA_OPERATOR_FAIL_REASON = processException(e.getMessage());
            return SnsConstant.SINA_UPDATE_STATUS_FAIL_WHAT;
        }
    }

    @Override
    public int updateStatus(UserInfo userInfo, String statusContent) {
        UpdateStatusInfo status = new UpdateStatusInfo();
        status.setStatusContent(statusContent);
        return updateStatus(userInfo, status);
    }

    @Override
    public int updateStatus(UserInfo userInfo, String statusContent, String imageFilePath) {
        UpdateStatusInfo status = new UpdateStatusInfo();
        status.setStatusContent(statusContent);
        status.setImageFilePath(imageFilePath);
        return updateStatus(userInfo, status);
    }

    @Override
    public int repost(UserInfo userInfo, long statusId, String comment) {
        return repost(userInfo, statusId, comment, false);
    }

    @Override
    public int repost(UserInfo userInfo, long statusId, String comment, boolean isComment) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        try {
            Status updateResult = weibo.repost(((Long) statusId).toString(), comment, (isComment ? 1 : 0));
            return (updateResult != null ? SnsConstant.SINA_REPOST_STATUS_SUCC_WHAT : SnsConstant.SINA_REPOST_STATUS_FAIL_WHAT);
        } catch (WeiboException e) {
            e.printStackTrace();
            SnsConstant.SINA_OPERATOR_FAIL_REASON = processException(e.getMessage());
            return SnsConstant.SINA_REPOST_STATUS_FAIL_WHAT;
        }
    }

    /**
     * ��ʼ��Property�����ظ���ʼ��
     */
    private void initProperty() {
        if (System.getProperty("weibo4j.oauth.consumerKey") == null) {
            System.setProperty("weibo4j.oauth.consumerKey", SnsConstant.SINA_CONSUMER_KEY);
        }
        if (System.getProperty("weibo4j.oauth.consumerSecret") == null) {
            System.setProperty("weibo4j.oauth.consumerSecret", SnsConstant.SINA_CONSUMER_SECRET);
        }
    }

    @Override
    public String processException(String exceptionMessage) {
        if (!StringUtils.isEmpty(exceptionMessage)) {
            if (exceptionMessage.contains("repeated weibo text!")) {
                return "��ʱ�����벻Ҫ������ͬ��״̬Ŷ^_^";
            }
        }
        return "";
    }

    @Override
    public List<StatusInfo> getCommentsToMe(UserInfo userInfo, int page, long lastPageTime) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Comment> commentList = null;
        try {
            commentList = weibo.getCommentsToMe(paging, true);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformCommentToStatus(commentList);
        }
        return transformCommentToStatus(commentList);
    }

    @Override
    public List<StatusInfo> getCommentsByMe(UserInfo userInfo, int page, long lastPageTime, long lastId) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Comment> commentList = null;
        try {
            commentList = weibo.getCommentsByMe(paging, true);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformCommentToStatus(commentList);
        }
        return transformCommentToStatus(commentList);
    }

    @Override
    public List<StatusInfo> getCommentsAboutMe(UserInfo userInfo, int page, long lastPageTime, long lastId) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Comment> commentList = null;
        try {
            commentList = weibo.getCommentsTimeline(paging, true);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformCommentToStatus(commentList);
        }
        return transformCommentToStatus(commentList);
    }

    @Override
    public List<StatusInfo> getDirectMessages(UserInfo userInfo, int page, long lastPageTime) {
        return null;
    }

    @Override
    public List<StatusInfo> getMentions(UserInfo userInfo, int page, long lastPageTime) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Status> statusList = null;
        try {
            statusList = weibo.getMentions(paging);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformStatus(statusList);
        }
        return transformStatus(statusList);
    }

    @Override
    public List<CommentInfo> getStatusComments(UserInfo userInfo, long statusId, int page) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setCount(SnsConstant.SINA_STATUS_COUNT_PER_PAGE);
        List<Comment> commentList = null;
        try {
            commentList = weibo.getComments(((Long) statusId).toString(), paging);
        } catch (WeiboException e) {
            e.printStackTrace();
            return transformComment(commentList);
        }
        return transformComment(commentList);
    }

    /**
     * ת�����۱�
     * 
     * @param commentList ����list
     * @return
     */
    protected List<CommentInfo> transformComment(List<Comment> commentList) {
        List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                commentInfoList.add(transformComment(comment));
            }
        }
        return commentInfoList;
    }

    /**
     * ת�����۱?״̬��
     * 
     * @param commentList ����list
     * @return
     */
    protected List<StatusInfo> transformCommentToStatus(List<Comment> commentList) {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                statusInfoList.add(transformCommentToStatus(comment));
            }
        }
        return statusInfoList;
    }

    /**
     * ת�����۵�״̬
     * 
     * @param comment
     * @return
     */
    protected StatusInfo transformCommentToStatus(Comment comment) {
        if (comment == null) {
            return null;
        }
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setUser(transformUser(comment.getUser()));
        statusInfo.setSourceStatus(transformStatus(comment));
        statusInfo.setTime(comment.getCreatedAt().getTime());
        statusInfo.setStatusId(comment.getId());
        statusInfo.setStatusContent(comment.getText());
        statusInfo.setSourceType(StringUtils.getHrefInnerHtml(comment.getSource()));
        StatusInfo sourceStatusInfo = statusInfo.getSourceStatus();
        if (sourceStatusInfo != null) {
            if (sourceStatusInfo.isContainImage()) {
                statusInfo.setIsContainImage(true);
                statusInfo.setSmallPictureUrl(sourceStatusInfo.getSmallPictureUrl());
                statusInfo.setMiddlePictureUrl(sourceStatusInfo.getMiddlePictureUrl());
                statusInfo.setSourcePictureUrl(sourceStatusInfo.getSourcePictureUrl());
            }
        }
        return statusInfo;
    }

    /**
     * ת������
     * 
     * @param comment ����
     * @return
     */
    protected CommentInfo transformComment(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCommentId(comment.getId());
        commentInfo.setComment(comment.getText());
        commentInfo.setTime(comment.getCreatedAt().getTime());
        commentInfo.setCommentUser(transformUser(comment.getUser()));
        commentInfo.setStatusInfo(transformStatus(comment));
        commentInfo.setWebsiteType(websiteType);
        return commentInfo;
    }

    /**
     * ��������ȡ״̬��Ϣ
     * 
     * @param status
     * @return
     */
    protected StatusInfo transformStatus(Comment comment) {
        if (comment == null) {
            return null;
        }
        return transformStatus(comment.getStatus());
    }

    @Override
    public int commentStatus(UserInfo userInfo, long statusId, String comment) {
        return commentComment(userInfo, statusId, -1, comment);
    }

    @Override
    public int commentComment(UserInfo userInfo, long statusId, long commentId, String comment) {
        initProperty();
        Weibo weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(userInfo.getAccessToken(), userInfo.getAccessSecret());
        try {
            Comment commentResult = weibo.updateComment(comment, ((Long) statusId).toString(), ((commentId > 0) ? ((Long) commentId).toString() : null));
            return (commentResult != null ? SnsConstant.SINA_COMMENT_STATUS_SUCC_WHAT : SnsConstant.SINA_COMMENT_STATUS_FAIL_WHAT);
        } catch (WeiboException e) {
            e.printStackTrace();
            SnsConstant.SINA_OPERATOR_FAIL_REASON = processException(e.getMessage());
            return SnsConstant.SINA_COMMENT_STATUS_FAIL_WHAT;
        }
    }
}
