package org.sucri.floxs.servlet;

import org.apache.commons.collections15.map.MultiKeyMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Jul 31, 2007
 * Time: 7:30:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RequestHandler {

    public boolean register(MultiKeyMap<String, RequestHandler> s);

    public boolean unregister(MultiKeyMap<String, RequestHandler> s);

    public String getPageName();

    public boolean processRequest(EntityManagerFactory emf, User user, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
