package jwt.web.actions;

import java.util.Iterator;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jwt.data.models.ContactModel;
import jwt.data.models.UserModel;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Login extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            SessionFactory factory = (SessionFactory) ctx.lookup("java:/hibernate/SessionFactory");
            Session hsession = factory.openSession();
            UserModel model = new UserModel();
            System.out.print("1");
            model.setLogin("alicja");
            model.setName("Alicja");
            model.setPassword("alamakota");
            model.setSurname("Truszkowska");
            System.out.print("2");
            hsession.save(model);
            System.out.print("3");
            hsession = factory.openSession();
            System.out.print("4");
            Query query = hsession.createQuery("from UserModel");
            System.out.print("5");
            Iterator it = query.list().iterator();
            while (it.hasNext()) {
                System.out.print("6");
                UserModel model2 = (UserModel) it.next();
                System.out.print("7");
                System.out.print(model2.getLogin() + " " + model2.getName());
            }
        } catch (NamingException e) {
            return mapping.findForward("error");
        }
        return mapping.findForward("ok");
    }
}
