package com.laoer.bbscs.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import com.laoer.bbscs.bean.Board;
import com.laoer.bbscs.bean.BoardMaster;
import com.laoer.bbscs.bean.Role;
import com.laoer.bbscs.bean.UserInfo;
import com.laoer.bbscs.comm.Constant;
import com.laoer.bbscs.exception.BbscsException;
import com.laoer.bbscs.service.BoardService;
import com.laoer.bbscs.service.RoleService;
import com.laoer.bbscs.service.UserService;
import com.laoer.bbscs.web.ajax.AjaxMessagesJson;
import com.laoer.bbscs.web.ui.OptionsInt;
import com.laoer.bbscs.web.ui.RadioInt;

public class AdminBoardMasterSet extends BaseAction {

    /**
	 * Logger for this class
	 */
    private static final Log logger = LogFactory.getLog(AdminBoardMasterSet.class);

    /**
	 *
	 */
    private static final long serialVersionUID = -6581365428530300861L;

    public AdminBoardMasterSet() {
        this.setRadioYesNoListValues();
    }

    private BoardService boardService;

    private RoleService roleService;

    private UserService userService;

    private AjaxMessagesJson ajaxMessagesJson;

    private long boardID;

    private int isHidden;

    private int overChildPurview;

    private String userName;

    private int roleID;

    public AjaxMessagesJson getAjaxMessagesJson() {
        return ajaxMessagesJson;
    }

    public void setAjaxMessagesJson(AjaxMessagesJson ajaxMessagesJson) {
        this.ajaxMessagesJson = ajaxMessagesJson;
    }

    public long getBoardID() {
        return boardID;
    }

    public void setBoardID(long boardID) {
        this.boardID = boardID;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public int getOverChildPurview() {
        return overChildPurview;
    }

    public void setOverChildPurview(int overChildPurview) {
        this.overChildPurview = overChildPurview;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    List<RadioInt> radioYesNoList = new ArrayList<RadioInt>();

    private void setRadioYesNoListValues() {
        radioYesNoList.add(new RadioInt(0, this.getText("bbscs.no")));
        radioYesNoList.add(new RadioInt(1, this.getText("bbscs.yes")));
    }

    public List<RadioInt> getRadioYesNoList() {
        return radioYesNoList;
    }

    public void setRadioYesNoList(List<RadioInt> radioYesNoList) {
        this.radioYesNoList = radioYesNoList;
    }

    private Board board;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private List<OptionsInt> roleValues = new ArrayList<OptionsInt>();

    private void setRoleValues() {
        List roleList = this.getRoleService().findRolesByTypeID(Constant.ROLE_TYPE_BOARD);
        for (int i = 0; i < roleList.size(); i++) {
            Role role = (Role) roleList.get(i);
            roleValues.add(new OptionsInt(role.getId(), role.getRoleName()));
        }
    }

    public List<OptionsInt> getRoleValues() {
        return roleValues;
    }

    public void setRoleValues(List<OptionsInt> roleValues) {
        this.roleValues = roleValues;
    }

    public String execute() {
        try {
            return this.executeMethod(this.getAction());
        } catch (Exception e) {
            logger.error(e);
            return INPUT;
        }
    }

    public String add() {
        this.setAction("addsave");
        Board b = this.getBoardService().getBoardByID(this.getBoardID());
        this.setBoard(b);
        this.setIsHidden(0);
        this.setOverChildPurview(1);
        setRoleValues();
        return INPUT;
    }

    @SuppressWarnings("unchecked")
    public String addsave() {
        Board b = this.getBoardService().getBoardByID(this.getBoardID());
        if (b.getBoardMaster().get(this.getUserName()) != null) {
            this.getAjaxMessagesJson().setMessage("E_BM_001", this.getText("error.admin.boardm.exist", new String[] { this.getUserName() }));
            return RESULT_AJAXJSON;
        }
        UserInfo ui = this.getUserService().findUserInfoByUserName(this.getUserName());
        if (ui == null) {
            this.getAjaxMessagesJson().setMessage("E_USER_NO_EXIST", this.getText("error.user.notexist"));
            return RESULT_AJAXJSON;
        }
        BoardMaster bm = new BoardMaster();
        bm.setBoard(b);
        bm.setIsHidden(this.getIsHidden());
        bm.setOverChildPurview(this.getOverChildPurview());
        bm.setRoleID(this.getRoleID());
        bm.setUserName(this.getUserName());
        b.getBoardMaster().put(this.getUserName(), bm);
        try {
            this.getBoardService().saveBoard(b);
            this.getAjaxMessagesJson().setMessage("0", this.getText("admin.boardm.add.ok"));
        } catch (BbscsException ex) {
            this.getAjaxMessagesJson().setMessage("E_BM_ADDFAILED", this.getText("error.admin.boardm.add"));
        }
        return RESULT_AJAXJSON;
    }

    public String edit() {
        Board b = this.getBoardService().getBoardByID(this.getBoardID());
        BoardMaster bm = (BoardMaster) b.getBoardMaster().get(this.getUserName());
        this.setAction("editsave");
        this.setIsHidden(bm.getIsHidden());
        this.setOverChildPurview(bm.getOverChildPurview());
        this.setRoleID(bm.getRoleID());
        this.setBoard(b);
        setRoleValues();
        return INPUT;
    }

    public String editsave() {
        Board b = this.getBoardService().getBoardByID(this.getBoardID());
        BoardMaster bm = (BoardMaster) b.getBoardMaster().get(this.getUserName());
        bm.setIsHidden(this.getIsHidden());
        bm.setOverChildPurview(this.getOverChildPurview());
        bm.setRoleID(this.getRoleID());
        try {
            this.getBoardService().saveBoard(b);
            this.getAjaxMessagesJson().setMessage("0", this.getText("admin.boardm.edit.ok"));
        } catch (BbscsException e) {
            logger.error(e);
            this.getAjaxMessagesJson().setMessage("E_BM_EDITFAILED", this.getText("error.admin.boardm.edit"));
        }
        return RESULT_AJAXJSON;
    }

    public String del() {
        Board b = this.getBoardService().getBoardByID(this.getBoardID());
        b.getBoardMaster().remove(this.getUserName());
        try {
            this.getBoardService().saveBoard(b);
            this.getAjaxMessagesJson().setMessage("0", this.getText("bbscs.dataupdate.succeed"));
        } catch (BbscsException ex1) {
            this.getAjaxMessagesJson().setMessage("E_BM_DELFAILED", this.getText("error.admin.boardm.del"));
        }
        return RESULT_AJAXJSON;
    }
}
