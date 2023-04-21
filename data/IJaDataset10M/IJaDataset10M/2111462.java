package com.laoer.bbscs.web.action;

import java.util.List;
import com.laoer.bbscs.bean.Forum;
import com.laoer.bbscs.exception.BbscsException;
import com.laoer.bbscs.service.BoardService;
import com.laoer.bbscs.service.ForumService;
import com.laoer.bbscs.service.VoteItemService;
import com.laoer.bbscs.service.VoteUserService;
import com.laoer.bbscs.service.config.SysConfig;
import com.laoer.bbscs.web.ajax.AjaxMessagesJson;

public class VoteOpt extends BaseBoardAction {

    /**
	 *
	 */
    private static final long serialVersionUID = -1808283476383019882L;

    private long deadline;

    private int isM;

    private String postid;

    private String voteid;

    private List<String> voteitemid;

    private ForumService forumService;

    private BoardService boardService;

    private SysConfig sysConfig;

    private VoteUserService voteUserService;

    private VoteItemService voteItemService;

    private AjaxMessagesJson ajaxMessagesJson;

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public int getIsM() {
        return isM;
    }

    public void setIsM(int isM) {
        this.isM = isM;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getVoteid() {
        return voteid;
    }

    public void setVoteid(String voteid) {
        this.voteid = voteid;
    }

    public List<String> getVoteitemid() {
        return voteitemid;
    }

    public void setVoteitemid(List<String> voteitemid) {
        this.voteitemid = voteitemid;
    }

    public AjaxMessagesJson getAjaxMessagesJson() {
        return ajaxMessagesJson;
    }

    public void setAjaxMessagesJson(AjaxMessagesJson ajaxMessagesJson) {
        this.ajaxMessagesJson = ajaxMessagesJson;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    public ForumService getForumService() {
        return forumService;
    }

    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }

    public SysConfig getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(SysConfig sysConfig) {
        this.sysConfig = sysConfig;
    }

    public VoteItemService getVoteItemService() {
        return voteItemService;
    }

    public void setVoteItemService(VoteItemService voteItemService) {
        this.voteItemService = voteItemService;
    }

    public VoteUserService getVoteUserService() {
        return voteUserService;
    }

    public void setVoteUserService(VoteUserService voteUserService) {
        this.voteUserService = voteUserService;
    }

    public String vote() {
        if (this.getDeadline() < System.currentTimeMillis()) {
            this.getAjaxMessagesJson().setMessage("E_POST_VOTE_DEADLINE", this.getText("error.vote.deadline1"));
            return RESULT_AJAXJSON;
        }
        if (this.getVoteitemid() == null || this.getVoteitemid().isEmpty()) {
            this.getAjaxMessagesJson().setMessage("E_POST_VOTE_SELITEM", this.getText("error.vote.selectitem"));
            return RESULT_AJAXJSON;
        }
        Forum f = this.getForumService().findForumByID(this.getPostid(), this.getBid());
        if (f == null) {
            this.getAjaxMessagesJson().setMessage("E_POST_NOT_EXIST", this.getText("error.post.getpost"));
            return RESULT_AJAXJSON;
        }
        if (this.getVoteUserService().findVoteUserByVoteIDUserID(this.getVoteid(), this.getUserSession().getId()) != null) {
            this.getAjaxMessagesJson().setMessage("E_POST_VOTE_USER_EXIST", this.getText("error.vote.userexist"));
            return RESULT_AJAXJSON;
        }
        try {
            this.getVoteItemService().createVoteItemAdd(this.getUserSession().getId(), this.getVoteid(), this.getVoteitemid());
            if (this.getSysConfig().getVoteUpdatePost() == 1) {
                f.setLastTime(System.currentTimeMillis());
                this.getForumService().saveOrUpdateForum(f);
            }
        } catch (BbscsException ex1) {
            this.getAjaxMessagesJson().setMessage("E_POST_VOTE_ERROR", this.getText("error.post.vote.error"));
            return RESULT_AJAXJSON;
        }
        this.getAjaxMessagesJson().setMessage("0", this.getText("post.vote.ok"));
        return RESULT_AJAXJSON;
    }
}
