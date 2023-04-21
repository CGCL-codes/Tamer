package com.core.util;

import java.util.Date;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.core.util.BaseDAO;
import com.be.vo.ConnectionPropertiesVO;

public final class MySessionListener implements HttpSessionListener {

    @SuppressWarnings("unchecked")
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println(new Date() + ": HttpSession object " + session.getId() + " has been created.");
        ServletContext sc = event.getSession().getServletContext();
        Hashtable ht = (Hashtable) sc.getAttribute("sessionHT");
        if (ht == null) {
            Hashtable<String, ConnectionPropertiesVO> cpHT = new Hashtable();
            sc.setAttribute("sessionHT", cpHT);
        }
    }

    @SuppressWarnings("unchecked")
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println(new Date() + ": HttpSession object " + session.getId() + " has been removed.");
        ServletContext sc = session.getServletContext();
        Hashtable<String, ConnectionPropertiesVO> ht = (Hashtable) sc.getAttribute("sessionHT");
        if (ht != null) {
            ConnectionPropertiesVO cpVO = ht.get(session.getId());
            if (cpVO != null) {
                System.out.println(new Date() + ": Removing DB Connection " + cpVO.getConnectionString());
                BaseDAO.closeConnections(cpVO);
            }
            ht.remove(session.getId());
        }
    }
}
