package com.gever.goa.dailyoffice.bbs.action;

import com.gever.exception.DefaultException;
import com.gever.goa.dailyoffice.bbs.dao.BBSDAO;
import com.gever.goa.dailyoffice.bbs.dao.BBSFactory;
import com.gever.goa.dailyoffice.bbs.vo.UserVO;
import com.gever.jdbc.BaseDAO;
import com.gever.struts.action.BaseAction;
import com.gever.struts.action.GoaActionConfig;
import com.gever.struts.form.BaseForm;
import com.gever.util.DateTimeUtils;

public class BBSMainAction extends BaseAction {

    protected void initData(GoaActionConfig cfg) throws DefaultException, Exception {
        BBSDAO DAO = BBSFactory.getInstance().createDAO(super.dbData);
        cfg.setBaseDao((BaseDAO) DAO);
        BaseForm myForm = cfg.getBaseForm();
        myForm.setVo(new UserVO());
    }

    public String toMain(GoaActionConfig cfg) throws DefaultException, Exception {
        BBSDAO DAO = BBSFactory.getInstance().createDAO(super.dbData);
        BaseForm myForm = cfg.getBaseForm();
        UserVO userinfo = new UserVO();
        userinfo.setUser_code(myForm.getUserId());
        userinfo = DAO.getBBSUserByUserCode(userinfo);
        if (userinfo == null) return "reg";
        String bbs_user_code = userinfo.getBbs_user_code();
        if (bbs_user_code != null && !"".equals(bbs_user_code)) {
            userinfo.setLast_log_time(DateTimeUtils.getCurrentDateTime());
            cfg.getSession().setAttribute("bbsuser", userinfo);
            ((BaseDAO) DAO).update(userinfo);
            return "main";
        } else {
            return "reg";
        }
    }

    public String doTopTreeData(GoaActionConfig cfg) throws DefaultException, Exception {
        BBSDAO DAO = BBSFactory.getInstance().createDAO(super.dbData);
        cfg.getRequest().setAttribute("treeData", DAO.getTreeData(DAO.TOPBOARD_TREE_SQL));
        return this.TREE_PAGE;
    }

    public String doSTreeData(GoaActionConfig cfg) throws DefaultException, Exception {
        BBSDAO DAO = BBSFactory.getInstance().createDAO(super.dbData);
        cfg.getRequest().setAttribute("treeData", DAO.getTreeData(DAO.SBOARD_TREE_SQL + " and tboard_serial='" + cfg.getRequest().getParameter("nodeid") + "'"));
        return this.TREE_PAGE;
    }
}
