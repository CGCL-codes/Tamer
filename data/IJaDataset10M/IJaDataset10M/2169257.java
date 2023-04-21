package cn.edu.pku.ss.bugtraking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Session;
import cn.edu.pku.ss.bugtraking.hibernate.Capblity;
import cn.edu.pku.ss.bugtraking.hibernate.HibernateUtil;
import cn.edu.pku.ss.bugtraking.hibernate.Users;
import cn.edu.pku.ss.bugtraking.hibernate.UsersDAO;

public class ModifyAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm srcForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        Users userWithNewValue = null;
        UsersDAO dbuser = new UsersDAO();
        HibernateUtil.beginTransaction();
        userWithNewValue = dbuser.findById(user.getId());
        HibernateUtil.commitTransaction();
        userWithNewValue.setName(user.getName());
        ActionMessages errors = new ActionMessages();
        ModifyForm form = (ModifyForm) srcForm;
        System.out.println("form.getNewPwd()=" + form.getNewPwd());
        System.out.println("form.getPassword()=" + form.getPwd());
        System.out.println("form.getConfirm()=" + form.getConfirm());
        System.out.println("form.getPwd()=" + user.getPwd());
        if (form.getNewPwd().length() != 0 && form.getNewPwd().equals(form.getConfirm()) && form.getPwd() != null && !form.getPwd().equals("") && form.getPwd().equals(user.getPwd())) {
            userWithNewValue.setPwd(form.getNewPwd());
        } else {
            System.out.println("�����벻ƥ��");
            errors.add("usedUsername", new ActionMessage("register.confirm"));
            this.saveErrors(request, errors);
            System.out.println(mapping.getInputForward());
            return mapping.getInputForward();
        }
        if (!(form.getPwd() != null && !form.getPwd().equals("") && form.getPwd().equals(user.getPwd()))) {
            System.out.println("�����벻ƥ��");
            errors.add("usedUsername", new ActionMessage("register.invalidPwd"));
            this.saveErrors(request, errors);
            return mapping.getInputForward();
        }
        System.out.println("userWithNewValue.getPwd()=" + userWithNewValue.getPwd());
        if (form.getEmail() != null && !form.getEmail().equals("")) {
            userWithNewValue.setEmail(form.getEmail());
        }
        Capblity capblity = (Capblity) session.getAttribute("capblity");
        capblity.setAddUser(form.isAddUser());
        capblity.setAddCmp(form.isAddCmp());
        capblity.setFixBug(form.isFixBug());
        capblity.setRegCmp(form.isRegCmp());
        capblity.setRegProj(form.isRegProj());
        capblity.setRegUser(form.isRegUser());
        capblity.setRoot(form.isRoot());
        capblity.setSearch(form.isSearch());
        Session dbsession = HibernateUtil.currentSession();
        try {
            System.out.println("saveOrUpdate userWithNewValue");
            user.setEmail(userWithNewValue.getEmail());
            user.setPwd(userWithNewValue.getPwd());
            HibernateUtil.beginTransaction();
            dbsession.saveOrUpdate(user);
            HibernateUtil.commitTransaction();
            System.out.println("userWithNewValue=" + userWithNewValue.getPwd());
        } catch (Exception e) {
            errors.add("dbError", new ActionMessage("globle.dbError"));
            this.saveErrors(request, errors);
            dbsession.close();
            System.out.println(e);
            return mapping.getInputForward();
        }
        dbsession.close();
        session.setAttribute("user", userWithNewValue);
        session.setAttribute("capblity", capblity);
        System.out.println("go to frontPage");
        return mapping.findForward("frontPage");
    }
}
