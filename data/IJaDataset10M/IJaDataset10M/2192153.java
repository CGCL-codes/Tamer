package net.ideapad.struts.form;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.ideapad.hibernate.HibernateSessionFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.Session;

/**
 * Java Bean to used by the View Group form to display a specific group
 * 
 * MyEclipse Struts Creation date: 12-13-2009
 * 
 * XDoclet definition:
 * 
 * @struts.form name="viewGroupForm"
 */
public class ViewGroupForm extends ActionForm {

    private Integer groupID;

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    /**
	 * Validates that the user input group is valid.
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        List<String> errors = new ArrayList<String>();
        Integer userID = (Integer) request.getSession().getAttribute("userID");
        if (userID == null) {
            actionErrors.add("Errors", new ActionMessage("You are not logged in, please do so."));
            errors.add("You are not logged in, please do so.");
        } else {
            Session session = HibernateSessionFactory.getSession();
            session.beginTransaction();
            Object obj = session.createQuery("from Group g left join fetch g.usersInGroup as ug where g.groupId = :groupID AND ug.userId = :id").setInteger("groupID", groupID).setInteger("id", userID).uniqueResult();
            if (obj == null) {
                actionErrors.add("Errors", new ActionMessage("You do not have access to this group"));
                errors.add("You do not have access to this group");
            }
            session.getTransaction().commit();
            HibernateSessionFactory.closeSession();
        }
        if (errors.size() > 0) {
            request.setAttribute("Errors", errors);
        }
        return actionErrors;
    }
}
