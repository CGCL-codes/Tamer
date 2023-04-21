package com.nitbcn.web.cont;

import com.nitbcn.lib.bd.AccesSQL;
import com.nitbcn.web.cont.*;
import com.nitbcn.lib.dao.DAOClick;
import com.nitbcn.lib.dao.DAOSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SendActionRedirectSource extends Action {

    public SendActionRedirectSource(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        super(request, response, session);
    }

    public int performanceAction() {
        try {
            int id = Integer.parseInt((String) request.getParameter("id"));
            int pos = Integer.parseInt((String) request.getParameter("pos"));
            AccesSQL a = new AccesSQL();
            DAOSource da = new DAOSource(a);
            DAOClick dc = new DAOClick(a);
            dc.insertClick(id, pos, DAOClick.SOURCE);
            String url = da.getUrlFromId(id);
            response.setStatus(301);
            response.setHeader("Location", url);
            response.setHeader("Connection", "close");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(301);
            response.setHeader("Location", "http://www.nitbcn.com");
            response.setHeader("Connection", "close");
        }
        return 1;
    }
}
