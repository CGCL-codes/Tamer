package org.aigebi.rbac.action;

import org.aigebi.rbac.admin.SeniorityManager;
import org.aigebi.rbac.to.Seniority;
import org.aigebi.rbac.bean.SeniorityBean;
import org.aigebi.rbac.common.SessionProfile;

/**
 * Seniority CRUD.
 * @author Ligong Xu
 * @version $Id: BaseSeniorityAction.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class BaseSeniorityAction extends CrudActionSupport {

    public BaseSeniorityAction() {
    }

    SeniorityBean mSeniorityBean = new SeniorityBean(new Seniority());

    public SeniorityBean getSeniorityBean() {
        return mSeniorityBean;
    }

    public void setSeniorityBean(SeniorityBean pSeniorityBean) {
        mSeniorityBean = pSeniorityBean;
    }

    SeniorityManager mSeniorityManager;

    public SeniorityManager getSeniorityManager() {
        return mSeniorityManager;
    }

    public void setSeniorityManager(SeniorityManager pSeniorityManager) {
        mSeniorityManager = pSeniorityManager;
    }

    /** clear mSeniorityBean */
    protected void clear() {
        setSeniorityBean(new SeniorityBean(new Seniority()));
    }

    /** update page menu info */
    public String input() throws Exception {
        setActiveMenu(SessionProfile.MENU_AccessAdmin);
        return super.input();
    }
}
