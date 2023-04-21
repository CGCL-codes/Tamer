package com.laoer.bbscs.web.action;

import java.util.ArrayList;
import java.util.List;
import com.laoer.bbscs.bean.Board;
import com.laoer.bbscs.bean.Forum;
import com.laoer.bbscs.comm.BBSCSUtil;
import com.laoer.bbscs.comm.Constant;
import com.laoer.bbscs.exception.BbscsException;
import com.laoer.bbscs.service.BoardService;
import com.laoer.bbscs.service.ForumService;
import com.laoer.bbscs.web.interceptor.RequestBasePathAware;
import com.laoer.bbscs.web.ui.OptionsString;

public class MoveForum extends BaseBoardAction implements RequestBasePathAware {

    /**
	 *
	 */
    private static final long serialVersionUID = -4688647062365117743L;

    private String basePath;

    private long tobid;

    private String id;

    private ForumService forumService;

    private BoardService boardService;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTobid() {
        return tobid;
    }

    public void setTobid(long tobid) {
        this.tobid = tobid;
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

    private List<OptionsString> boardSelectValues = new ArrayList<OptionsString>();

    public List<OptionsString> getBoardSelectValues() {
        return boardSelectValues;
    }

    public void setBoardSelectValues(List<OptionsString> boardSelectValues) {
        this.boardSelectValues = boardSelectValues;
    }

    private String forwardUrl;

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String movepage() {
        int isHidden = 0;
        if (this.getUserSession().isHaveSpecialPermission(Constant.SPERMISSION_CAN_SEE_HIDDEN_BOARD)) {
            isHidden = -1;
        }
        this.setBoardSelectValues(isHidden);
        this.setAction("move");
        return INPUT;
    }

    public String move() {
        Forum f = this.getForumService().findForumByID(this.getId(), this.getBid());
        if (f == null) {
            this.addActionError(this.getText("error.post.getpost"));
            return ERROR;
        }
        if (f.getIsNew() != 1) {
            this.addActionError(this.getText("error.post.move.isnotmain"));
            return ERROR;
        }
        Board toboard = this.getBoardService().getBoardByID(this.getTobid());
        if (toboard == null) {
            this.addActionError(this.getText("error.board.id"));
            return ERROR;
        }
        if (toboard.getBoardType() != 3) {
            this.addActionError(this.getText("error.post.move.boardtype"));
            return ERROR;
        }
        try {
            this.getForumService().saveMoveForum(this.getBid(), this.getId(), toboard);
            if (Constant.USE_URL_REWRITE) {
                this.setForwardUrl(this.getBasePath() + "forum-index-" + this.getTobid() + "-0-1-0.html");
            } else {
                this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("/forum?action=index&bid=" + this.getTobid() + "&tagId=0"));
            }
            return SUCCESS;
        } catch (BbscsException ex) {
            this.addActionError(this.getText("error.post.move.error"));
            return ERROR;
        }
    }

    private void setBoardSelectValues(int isHidden) {
        List blist = this.getBoardService().findBoardsByParentID(0, 1, isHidden, Constant.FIND_BOARDS_BY_ORDER);
        for (int i = 0; i < blist.size(); i++) {
            Board b = (Board) blist.get(i);
            this.boardSelectValues.add(new OptionsString(String.valueOf(b.getId()), BBSCSUtil.getBoardPrefixLine(b.getLevel(), "-") + b.getBoardName()));
            List bclist = this.getBoardService().findBoardsByParentID(b.getId(), 1, isHidden, Constant.FIND_BOARDS_BY_ORDER);
            for (int j = 0; j < bclist.size(); j++) {
                Board bc = (Board) bclist.get(j);
                this.boardSelectValues.add(new OptionsString(String.valueOf(bc.getId()), BBSCSUtil.getBoardPrefixLine(bc.getLevel(), "-") + bc.getBoardName()));
            }
        }
    }
}
