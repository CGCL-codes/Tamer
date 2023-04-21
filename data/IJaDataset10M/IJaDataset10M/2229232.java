package com.ynhenc.topis.mobile.domain;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ynhenc.topis.mobile.web.Page;
import junit.framework.TestCase;

public class WebLogDAOTest {

    public void select() throws Exception {
        Page page = new Page();
        WebLogDAO dao = new WebLogDAO();
        List list = dao.findAll();
        int index = 0;
        for (Object obj : list) {
            System.out.println(index + ": " + obj);
            index++;
        }
    }

    @Test
    public void testMerge() throws Exception {
        WebLogId id = new WebLogId();
        id.setLogDate("20110209");
        id.setMenuId(2);
        id.setLogTime(15);
        WebLogDAO dao = new WebLogDAO();
        Session session = dao.getSession();
        Transaction tr = session.beginTransaction();
        WebLog webLog = (WebLog) dao.findById(id);
        if (webLog == null) {
            webLog = new WebLog();
            webLog.setId(id);
            webLog.setCount(0);
        }
        if (true) {
            webLog.setCount(webLog.getCount() + 1);
            webLog.setMenuName("abcdef");
        }
        dao.merge(webLog);
        tr.commit();
        session.close();
        Object obj = dao.findById(webLog.getId());
        int index = 0;
        System.out.println(index + ": " + obj);
    }
}
