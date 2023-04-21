package com.aimluck.eip.blog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.blog.util.BlogUtils;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogComment;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogEntry;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogFile;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogThema;
import com.aimluck.eip.common.ALAbstractSelectData;
import com.aimluck.eip.common.ALBlogManager;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALData;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.common.ALEipGroup;
import com.aimluck.eip.common.ALEipManager;
import com.aimluck.eip.common.ALEipPost;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.fileupload.beans.FileuploadBean;
import com.aimluck.eip.fileupload.util.FileuploadUtils;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.ResultList;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.services.accessctl.ALAccessControlConstants;
import com.aimluck.eip.util.ALCommonUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ブログエントリー検索データを管理するクラスです。 <BR>
 * 
 */
public class BlogEntrySelectData extends ALAbstractSelectData<EipTBlogEntry, EipTBlogEntry> implements ALData {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BlogEntrySelectData.class.getName());

    /** カテゴリ一覧 */
    private List<BlogThemaResultData> themaList;

    /** エントリーの総数 */
    private int entrySum;

    private List<BlogCommentResultData> commentList;

    private List<BlogFootmarkResultData> footmarkList;

    private int uid;

    private String ownerId;

    private int view_uid;

    private ALStringField view_uname;

    private boolean has_photo;

    private String userAccountURI;

    private boolean editable;

    private boolean deletable;

    private boolean comment_deletable;

    private boolean other_comment_deletable;

    /** アクセス権限の機能名 */
    private String aclPortletFeature = null;

    private ALStringField keyword;

    private String themeId;

    private String groupId;

    private List<BlogUserResultData> userList;

    private List<ALEipGroup> myGroupList;

    /**
   * 
   * @param action
   * @param rundata
   * @param context
   */
    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        uid = ALEipUtils.getUserId(rundata);
        ALEipUtils.removeTemp(rundata, context, LIST_FILTER_STR);
        ALEipUtils.removeTemp(rundata, context, LIST_FILTER_TYPE_STR);
        ALEipUtils.removeTemp(rundata, context, ALEipConstants.ENTITY_ID);
        if (rundata.getParameters().containsKey(ALEipConstants.ENTITY_ID)) {
            ALEipUtils.setTemp(rundata, context, ALEipConstants.ENTITY_ID, rundata.getParameters().get(ALEipConstants.ENTITY_ID));
        }
        ownerId = BlogUtils.getOwnerId(rundata, context);
        themeId = BlogUtils.getThemeId(rundata, context);
        keyword = new ALStringField(BlogUtils.getKeyword(rundata, context));
        groupId = BlogUtils.getGroupId(rundata, context);
        userList = BlogUtils.getBlogUserResultDataList(getGroupId());
        myGroupList = ALEipUtils.getMyGroups(rundata);
        userAccountURI = BlogUtils.getPortletURIinPersonalConfigPane(rundata, "AccountPerson");
        super.init(action, rundata, context);
        view_uid = BlogUtils.getViewId(rundata, context, uid);
        if (view_uid == uid) {
            aclPortletFeature = ALAccessControlConstants.POERTLET_FEATURE_BLOG_ENTRY_SELF;
        } else {
            aclPortletFeature = ALAccessControlConstants.POERTLET_FEATURE_BLOG_ENTRY_OTHER;
        }
        editable = BlogUtils.checkPermission(rundata, context, ALAccessControlConstants.VALUE_ACL_UPDATE, aclPortletFeature);
        deletable = BlogUtils.checkPermission(rundata, context, ALAccessControlConstants.VALUE_ACL_DELETE, aclPortletFeature);
        other_comment_deletable = BlogUtils.checkPermission(rundata, context, ALAccessControlConstants.VALUE_ACL_DELETE, ALAccessControlConstants.POERTLET_FEATURE_BLOG_ENTRY_OTHER_REPLY);
        comment_deletable = BlogUtils.checkPermission(rundata, context, ALAccessControlConstants.VALUE_ACL_DELETE, ALAccessControlConstants.POERTLET_FEATURE_BLOG_ENTRY_REPLY);
    }

    /**
   * 
   * @param rundata
   * @param context
   */
    public void loadThemaList(RunData rundata, Context context) {
        themaList = BlogUtils.getThemaList(rundata, context);
    }

    /**
   * 一覧データを取得します。 <BR>
   * 
   * @param rundata
   * @param context
   * @return
   */
    @Override
    public ResultList<EipTBlogEntry> selectList(RunData rundata, Context context) {
        try {
            SelectQuery<EipTBlogEntry> query = getSelectQuery(rundata, context);
            buildSelectQueryForListView(query);
            query.orderDesending(EipTBlogEntry.CREATE_DATE_PROPERTY);
            ResultList<EipTBlogEntry> list = query.getResultList();
            entrySum = list.getTotalCount();
            return list;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * 検索条件を設定した SelectQuery を返します。 <BR>
   * 
   * @param rundata
   * @param context
   * @return
   */
    private SelectQuery<EipTBlogEntry> getSelectQuery(RunData rundata, Context context) {
        SelectQuery<EipTBlogEntry> query = Database.query(EipTBlogEntry.class);
        query = BlogUtils.buildSelectQueryForBlogFilter(query, rundata, context);
        query.orderDesending(EipTBlogEntry.CREATE_DATE_PROPERTY);
        return buildSelectQueryForFilter(query, rundata, context);
    }

    /**
   * ResultData に値を格納して返します。（一覧データ） <BR>
   * 
   * @param obj
   * @return
   */
    @SuppressWarnings("unchecked")
    @Override
    protected Object getResultData(EipTBlogEntry record) {
        try {
            BlogEntryResultData rd = new BlogEntryResultData();
            rd.initField();
            rd.setEntryId(record.getEntryId().longValue());
            rd.setOwnerId(record.getOwnerId().longValue());
            rd.setOwnerName(BlogUtils.getUserFullName(record.getOwnerId().intValue()));
            rd.setTitle(ALCommonUtils.compressString(record.getTitle(), getStrLength()));
            rd.setNote(record.getNote().replaceAll("\\r\\n", " ").replaceAll("\\n", " ").replaceAll("\\r", " "));
            rd.setBlogId(record.getEipTBlog().getBlogId().longValue());
            rd.setThemaId(record.getEipTBlogThema().getThemaId().longValue());
            rd.setThemaName(ALCommonUtils.compressString(record.getEipTBlogThema().getThemaName(), getStrLength()));
            rd.setAllowComments("T".equals(record.getAllowComments()));
            rd.setTitleDate(record.getCreateDate());
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
            rd.setDay(Integer.parseInt((sdf1.format(record.getCreateDate()))));
            SelectQuery<EipTBlogComment> query = Database.query(EipTBlogComment.class);
            Expression exp = ExpressionFactory.matchDbExp(EipTBlogComment.EIP_TBLOG_ENTRY_PROPERTY + "." + EipTBlogEntry.ENTRY_ID_PK_COLUMN, record.getEntryId());
            query.setQualifier(exp);
            List<EipTBlogComment> list = query.fetchList();
            if (list != null && list.size() > 0) {
                rd.setCommentsNum(list.size());
            }
            int userId = record.getOwnerId().intValue();
            rd.setHasPhoto(false);
            ALBlogManager manager = ALBlogManager.getInstance();
            List<BlogUserResultData> userDataList = (List<BlogUserResultData>) manager.getUserDataList();
            if (userDataList == null) {
                userDataList = BlogUtils.getBlogUserResultDataList("LoginUser");
                manager.setUserDataList(userDataList);
            }
            for (BlogUserResultData userData : userDataList) {
                if (userId == userData.getUserId().getValue() && userData.hasPhoto()) {
                    rd.setHasPhoto(true);
                    break;
                }
            }
            return rd;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * 詳細データを取得します。 <BR>
   * 
   * @param rundata
   * @param context
   * @return
   */
    @Override
    public EipTBlogEntry selectDetail(RunData rundata, Context context) {
        try {
            EipTBlogEntry obj = BlogUtils.getEipTBlogEntry(rundata, context);
            return obj;
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * ResultData に値を格納して返します。（詳細データ） <BR>
   * 
   * @param obj
   * @return
   */
    @Override
    protected Object getResultDataDetail(EipTBlogEntry record) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日（EE） HH時mm分");
            BlogEntryResultData rd = new BlogEntryResultData();
            rd.initField();
            rd.setEntryId(record.getEntryId().longValue());
            rd.setOwnerId(record.getOwnerId().longValue());
            rd.setOwnerName(BlogUtils.getUserFullName(record.getOwnerId().intValue()));
            rd.setTitle(record.getTitle());
            rd.setNote(record.getNote());
            rd.setBlogId(record.getEipTBlog().getBlogId().longValue());
            rd.setThemaId(record.getEipTBlogThema().getThemaId().intValue());
            rd.setThemaName(record.getEipTBlogThema().getThemaName());
            rd.setAllowComments("T".equals(record.getAllowComments()));
            rd.setCreateDate(sdf.format(record.getCreateDate()));
            rd.setCreateDateAlternative(record.getCreateDate());
            rd.setUpdateDate(record.getUpdateDate());
            commentList = new ArrayList<BlogCommentResultData>();
            SelectQuery<EipTBlogComment> query = Database.query(EipTBlogComment.class);
            Expression exp = ExpressionFactory.matchDbExp(EipTBlogComment.EIP_TBLOG_ENTRY_PROPERTY + "." + EipTBlogEntry.ENTRY_ID_PK_COLUMN, record.getEntryId());
            query.orderAscending(EipTBlogComment.UPDATE_DATE_PROPERTY);
            query.setQualifier(exp);
            List<EipTBlogComment> comments = query.fetchList();
            if (comments != null && comments.size() > 0) {
                int size = comments.size();
                for (int i = 0; i < size; i++) {
                    EipTBlogComment blogcomment = comments.get(i);
                    BlogCommentResultData comment = new BlogCommentResultData();
                    comment.initField();
                    comment.setCommentId(blogcomment.getCommentId().longValue());
                    comment.setOwnerId(blogcomment.getOwnerId().longValue());
                    comment.setOwnerName(BlogUtils.getUserFullName(blogcomment.getOwnerId().intValue()));
                    comment.setComment(blogcomment.getComment());
                    comment.setEntryId(blogcomment.getEipTBlogEntry().getEntryId().longValue());
                    comment.setUpdateDate(sdf.format(blogcomment.getUpdateDate()));
                    comment.setUpdateDateAlternative(blogcomment.getUpdateDate());
                    commentList.add(comment);
                }
            }
            SelectQuery<EipTBlogFile> filequery = Database.query(EipTBlogFile.class);
            Expression fileexp = ExpressionFactory.matchDbExp(EipTBlogFile.EIP_TBLOG_ENTRY_PROPERTY + "." + EipTBlogEntry.ENTRY_ID_PK_COLUMN, record.getEntryId());
            filequery.setQualifier(fileexp);
            List<EipTBlogFile> files = filequery.fetchList();
            if (files != null && files.size() > 0) {
                List<FileuploadBean> attachmentFileList = new ArrayList<FileuploadBean>();
                FileuploadBean filebean = null;
                int size = files.size();
                for (int i = 0; i < size; i++) {
                    EipTBlogFile file = files.get(i);
                    String realname = file.getTitle();
                    javax.activation.DataHandler hData = new javax.activation.DataHandler(new javax.activation.FileDataSource(realname));
                    filebean = new FileuploadBean();
                    filebean.setFileId(file.getFileId());
                    filebean.setFileName(realname);
                    if (hData != null) {
                        filebean.setContentType(hData.getContentType());
                    }
                    filebean.setIsImage(FileuploadUtils.isImage(realname));
                    attachmentFileList.add(filebean);
                }
                rd.setAttachmentFiles(attachmentFileList);
            }
            if (record.getOwnerId().intValue() == uid) {
                record.setUpdateDate(Calendar.getInstance().getTime());
                Database.commit();
            }
            return rd;
        } catch (Exception ex) {
            Database.rollback();
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * 
   * @return
   */
    public List<BlogThemaResultData> getThemaList() {
        return themaList;
    }

    public int getLoginUid() {
        return uid;
    }

    public int getViewUid() {
        return view_uid;
    }

    public ALStringField getViewUname() {
        return view_uname;
    }

    /**
   * エントリーの総数を返す． <BR>
   * 
   * @return
   */
    public int getEntrySum() {
        return entrySum;
    }

    /**
   * @return
   * 
   */
    @Override
    protected Attributes getColumnMap() {
        Attributes map = new Attributes();
        map.putValue("thema", EipTBlogThema.THEMA_ID_PK_COLUMN);
        return map;
    }

    public List<BlogCommentResultData> getCommentList() {
        return commentList;
    }

    public List<BlogFootmarkResultData> getFootmarkList() {
        return footmarkList;
    }

    /**
   * 
   * @param id
   * @return
   */
    public boolean isMatch(int id1, long id2) {
        return id1 == (int) id2;
    }

    public int getUserId() {
        return uid;
    }

    public boolean hasPhoto() {
        return has_photo;
    }

    public String getUserAccountURI() {
        return userAccountURI;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public ALStringField getKeyword() {
        return keyword;
    }

    public String getThemeId() {
        return themeId;
    }

    public List<BlogUserResultData> getUserList() {
        return userList;
    }

    public Map<Integer, ALEipPost> getPostMap() {
        return ALEipManager.getInstance().getPostMap();
    }

    public List<ALEipGroup> getMyGroupList() {
        return myGroupList;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean getEditable() {
        return editable;
    }

    public boolean getDeletable() {
        return deletable;
    }

    public boolean getOtherCommentDeletable() {
        return other_comment_deletable;
    }

    public boolean getCommentDeletable() {
        return comment_deletable;
    }

    /**
   * アクセス権限チェック用メソッド。<br />
   * アクセス権限の機能名を返します。
   * 
   * @return
   */
    @Override
    public String getAclPortletFeature() {
        return aclPortletFeature;
    }
}
