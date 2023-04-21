package org.nodevision.portal.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.lob.SerializableBlob;
import org.nodevision.portal.cache.SessionCache;
import org.nodevision.portal.hibernate.om.NvUserportlets;
import org.nodevision.portal.hibernate.om.NvUserportletsId;
import org.nodevision.portal.hibernate.om.NvUsers;
import org.nodevision.portal.utils.Constants;
import org.nodevision.portal.utils.HibernateUtil;
import org.nodevision.portal.utils.UserPortletsHolder;

public final class UserPortletsServlet extends HttpServlet {

    private ServletConfig servletConfig;

    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String userportlets_id = request.getParameter("id");
        final String page = request.getParameter("page");
        if ("add".equalsIgnoreCase(request.getParameter("type"))) {
            try {
                final ArrayList portlets = UserPortletsServlet.getPortletList(page, request.getRemoteUser(), userportlets_id);
                if (null == request.getParameterValues("portlets")) {
                    response.sendRedirect(response.encodeRedirectURL(request.getParameter("returnUrl")));
                    response.flushBuffer();
                    return;
                }
                final String[] portletsParams = request.getParameterValues("portlets");
                for (int i = 0; i < portletsParams.length; i++) {
                    final String value = portletsParams[i];
                    final StringTokenizer tokenizer = new StringTokenizer(value, "###");
                    final String newId = userportlets_id + "up" + portlets.size();
                    final UserPortletsHolder tempPortlet = new UserPortletsHolder();
                    tempPortlet.setPortlet_id(newId);
                    tempPortlet.setWebapplication(tokenizer.nextToken());
                    tempPortlet.setPortlet_name(tokenizer.nextToken());
                    portlets.add(tempPortlet);
                }
                UserPortletsServlet.saveNewState(page, request.getRemoteUser(), userportlets_id, portlets);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e.toString());
            }
            response.sendRedirect(response.encodeRedirectURL(request.getParameter("returnUrl")));
        }
        if ("remove".equalsIgnoreCase(request.getParameter("type"))) {
            try {
                final ArrayList portlets = UserPortletsServlet.getPortletList(page, request.getRemoteUser(), userportlets_id);
                UserPortletsHolder tempPortlet = null;
                for (int i = 0; i < portlets.size(); i++) {
                    tempPortlet = (UserPortletsHolder) portlets.get(i);
                    if (tempPortlet.getPortlet_id().equalsIgnoreCase(request.getParameter("nvpid"))) {
                        portlets.remove(i);
                        break;
                    }
                }
                UserPortletsServlet.saveNewState(page, request.getRemoteUser(), userportlets_id, portlets);
                SessionCache.getInstance(servletConfig.getServletContext(), request).flushCacheEntry(Constants.SAVED_BOX_HTML + ".pre." + request.getParameter("nvpid") + '.' + page);
                SessionCache.getInstance(servletConfig.getServletContext(), request).flushCacheEntry(Constants.SAVED_BOX_HTML + ".post." + request.getParameter("nvpid") + '.' + page);
                SessionCache.getInstance(servletConfig.getServletContext(), request).flushCacheEntry(Constants.PORTLET_CACHE + '.' + page + '.' + request.getParameter("nvpid"));
                SessionCache.getInstance(servletConfig.getServletContext(), request).flushCacheEntry(Constants.PORTLET_CACHE + '.' + page + '.' + request.getParameter("nvpid") + ".params");
                request.getSession().setAttribute(Constants.PREFERENCES_PREFIX + "np_" + request.getParameter("nvpid") + '_' + tempPortlet.getPortlet_name() + '_' + tempPortlet.getWebapplication(), null);
                Session hbsession = HibernateUtil.currentSession();
                Transaction tx = hbsession.beginTransaction();
                String hqlDelete = "delete org.nodevision.portal.hibernate.om.NvPreferences where login = :login and page_id = :pageid and portlet_id = :uid";
                hbsession.createQuery(hqlDelete).setString("login", request.getRemoteUser()).setString("pageid", page).setString("uid", request.getParameter("nvpid")).executeUpdate();
                if (!hbsession.connection().getAutoCommit()) {
                    tx.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e.toString());
            } finally {
                HibernateUtil.closeSession();
            }
            response.sendRedirect(response.encodeRedirectURL(request.getParameter("returnUrl")));
        }
        if ("up".equalsIgnoreCase(request.getParameter("type"))) {
            try {
                final ArrayList portlets = UserPortletsServlet.getPortletList(page, request.getRemoteUser(), userportlets_id);
                for (int i = 0; i < portlets.size(); i++) {
                    final UserPortletsHolder tempPortlet = (UserPortletsHolder) portlets.get(i);
                    if (tempPortlet.getPortlet_id().equalsIgnoreCase(request.getParameter("nvpid"))) {
                        if (-1 < i - 1) {
                            final UserPortletsHolder tempPortletSecond = (UserPortletsHolder) portlets.get(i - 1);
                            portlets.set(i - 1, tempPortlet);
                            portlets.set(i, tempPortletSecond);
                            break;
                        }
                    }
                }
                UserPortletsServlet.saveNewState(page, request.getRemoteUser(), userportlets_id, portlets);
            } catch (Exception e) {
                throw new ServletException(e.toString());
            }
            response.sendRedirect(response.encodeRedirectURL(request.getParameter("returnUrl")));
        }
        if ("down".equalsIgnoreCase(request.getParameter("type"))) {
            try {
                final ArrayList portlets = UserPortletsServlet.getPortletList(page, request.getRemoteUser(), userportlets_id);
                for (int i = 0; i < portlets.size(); i++) {
                    final UserPortletsHolder tempPortlet = (UserPortletsHolder) portlets.get(i);
                    if (tempPortlet.getPortlet_id().equalsIgnoreCase(request.getParameter("nvpid"))) {
                        if (portlets.size() > i + 1) {
                            final UserPortletsHolder tempPortletSecond = (UserPortletsHolder) portlets.get(i + 1);
                            portlets.set(i, tempPortletSecond);
                            portlets.set(i + 1, tempPortlet);
                            break;
                        }
                    }
                }
                UserPortletsServlet.saveNewState(page, request.getRemoteUser(), userportlets_id, portlets);
            } catch (Exception e) {
                throw new ServletException(e.toString());
            }
            response.sendRedirect(response.encodeRedirectURL(request.getParameter("returnUrl")));
        }
    }

    private static void saveNewState(final String page, final String user, final String userportlets_id, final ArrayList portlets) throws Exception {
        try {
            Session hbsession = HibernateUtil.currentSession();
            Transaction tx = hbsession.beginTransaction();
            String hqlDelete = "delete org.nodevision.portal.hibernate.om.NvUserportlets where login = :login and page_id = :pageid and userportlets_id = :uid";
            hbsession.createQuery(hqlDelete).setString("login", user).setString("pageid", page).setString("uid", userportlets_id).executeUpdate();
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final ObjectOutputStream oout = new ObjectOutputStream(bout);
            oout.writeObject(portlets);
            oout.flush();
            bout.close();
            oout.close();
            NvUsers nvuser = (NvUsers) hbsession.load(NvUsers.class, user);
            NvUserportlets newPortlets = new NvUserportlets();
            NvUserportletsId newPortletsId = new NvUserportletsId();
            newPortletsId.setPageId(page);
            newPortletsId.setUserportletsId(userportlets_id);
            newPortletsId.setNvUsers(nvuser);
            newPortlets.setId(newPortletsId);
            newPortlets.setPortletsList(Hibernate.createBlob(bout.toByteArray()));
            hbsession.save(newPortlets);
            if (!hbsession.connection().getAutoCommit()) {
                tx.commit();
            }
        } finally {
            HibernateUtil.closeSession();
        }
    }

    private static ArrayList getPortletList(final String page, final String user, final String userportlets_id) throws Exception {
        ArrayList portlets = new ArrayList();
        Session hbsession = HibernateUtil.currentSession();
        try {
            Query q1 = hbsession.createQuery("select c2.PortletsList from org.nodevision.portal.hibernate.om.NvUserportlets as c2 where login = :login and page_id = :pageid and userportlets_id = :uid");
            q1.setString("login", user);
            q1.setString("pageid", page);
            q1.setString("uid", userportlets_id);
            if (!q1.list().isEmpty()) {
                SerializableBlob blob = (SerializableBlob) q1.list().get(0);
                if (blob.length() > 0) {
                    DataInputStream in = new DataInputStream(blob.getBinaryStream());
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    int c;
                    while ((c = in.read()) != -1) {
                        bout.write(c);
                    }
                    final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                    final ObjectInputStream oin = new ObjectInputStream(bin);
                    portlets = (ArrayList) oin.readObject();
                }
            }
        } finally {
            HibernateUtil.closeSession();
        }
        return portlets;
    }

    public final void init(final ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
    }
}
