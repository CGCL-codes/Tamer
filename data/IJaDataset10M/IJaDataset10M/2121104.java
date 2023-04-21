package org.jpos.ee.action.user;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.jpublish.JPublishContext;
import org.jpublish.action.Action;
import com.anthonyeden.lib.config.Configuration;
import org.jpos.ee.action.ActionSupport;
import org.jpos.ee.User;
import org.jpos.ee.Permission;
import org.jpos.ee.DB;
import org.jpos.ee.BLException;
import org.jpos.util.V;
import org.jpos.util.BeanDiff;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

public class Update extends ActionSupport {

    public void execute(JPublishContext context, Configuration cfg) {
        Transaction tx = null;
        try {
            User me = (User) context.getSession().getAttribute(USER);
            if (me == null) {
                error(context, "Access denied.", true);
                return;
            }
            HttpServletRequest request = context.getRequest();
            String id = request.getParameter("id");
            if (id == null) {
                error(context, "The user id was not specified.", true);
                return;
            }
            DB db = getDB(context);
            User u = (User) db.session().load(User.class, new Long(id));
            if (u.isDeleted()) {
                error(context, "The user does not longer exist.", true);
                return;
            }
            User original = (User) u.clone();
            context.put("u", u);
            context.put("nick", u.getNick());
            context.put("name", u.getName());
            boolean changePasswd = request.getParameter("passwd") != null;
            if (!"POST".equals(request.getMethod())) {
                if (changePasswd) {
                    context.put("passwd", Boolean.TRUE);
                }
                return;
            }
            int errors = 0;
            String name = request.getParameter("name");
            if (!V.isName(name)) {
                context.put("name", name);
                context.put("errorName", "Invalid name");
                errors++;
            }
            String pass = request.getParameter("pass");
            String pass2 = request.getParameter("pass2");
            if (pass != null) {
                if (pass.length() != 32) {
                    context.put("errorPass", "Invalid password.");
                    errors++;
                }
                if (pass2 == null || pass2.length() != 32) {
                    context.put("errorPass2", "Invalid password.");
                    errors++;
                }
                if (pass != null && !pass.equals(pass2)) {
                    context.put("errorPass", "Passwords differ.");
                    context.put("errorPass2", "Please verify.");
                    errors++;
                }
            }
            if (errors > 0) return;
            tx = db.beginTransaction();
            StringBuffer rh = new StringBuffer();
            setPermissions(request, u, me);
            recordChange("name", u.getName(), name, rh);
            u.setName(name);
            if (pass != null) {
                rh.append("password changed");
                rh.append(BR);
                u.setPassword(pass);
            }
            rh.append("Permissions: ");
            rh.append(u.getPermissions());
            rh.append(BR);
            u.logRevision(rh.toString(), me);
            db.session().update(u);
            tx.commit();
            context.put("nick", u.getNick());
            context.put("name", u.getName());
            context.put(MESSAGE, "User updated.");
        } catch (ObjectNotFoundException e) {
            error(context, "The user does not exist.", true);
        } catch (HibernateException e) {
            context.getSyslog().error(e);
            error(context, e.getMessage(), true);
        } catch (NumberFormatException e) {
            error(context, "We have received an invalid user id.", true);
        } catch (Exception e) {
            error(context, "Unexpected exception: " + e.getMessage(), true);
        } finally {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception e) {
                    context.getSyslog().error(e);
                }
            }
        }
    }

    private void setPermissions(HttpServletRequest request, User u, User me) throws HibernateException {
        List perms = new ArrayList();
        Enumeration en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String p = (String) en.nextElement();
            if (p.startsWith("_perm_") && p.length() > 6) {
                String permName = p.substring(6);
                if (me.hasPermission(permName) || me.hasPermission(Permission.USERADMIN)) {
                    perms.add(permName);
                }
            }
        }
        u.revokeAll();
        Iterator iter = perms.iterator();
        while (iter.hasNext()) {
            u.grant((String) iter.next());
        }
    }
}
