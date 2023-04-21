package com.appenspot.cocokoko.spot;

import java.io.IOException;
import java.util.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.appenspot.cocokoko.data.SpotData;
import com.appenspot.cocokoko.data.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class InsertSpot extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            SpotData spotdata = new SpotData();
            spotdata.setCategoryID(Long.valueOf(req.getParameter("categoryID")));
            spotdata.setSpotNm(req.getParameter("spotNm"));
            spotdata.setLAT(Double.valueOf(req.getParameter("lat")));
            spotdata.setLNG(Double.valueOf(req.getParameter("lng")));
            spotdata.setMemo(req.getParameter("memo"));
            spotdata.setUrl(req.getParameter("url"));
            if (user != null) {
                spotdata.setAddUserNm(user.getNickname());
            } else {
                spotdata.setAddUserNm(null);
            }
            spotdata.setAddDate(new Date());
            pm.makePersistent(spotdata);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
