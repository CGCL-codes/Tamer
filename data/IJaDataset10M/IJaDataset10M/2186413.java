package com.konarkdev.elibrary_manager.client;

import com.konarkdev.elibrary_manager.db.DBConnector;
import com.konarkdev.elibrary_manager.server.dataobjects.*;
import java.util.Calendar;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.http.*;
import org.apache.struts.action.*;

public class BookingAction extends Action {

    DBConnector dbconn;

    public BookingAction() {
        dbconn = DBConnector.getInstance();
    }

    public ActionForward execute(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        PersistenceManager persistencemanager;
        String s;
        Transaction transaction;
        persistencemanager = dbconn.getPM();
        s = httpservletrequest.getParameter("id");
        transaction = persistencemanager.currentTransaction();
        try {
            transaction.begin();
            Object obj = persistencemanager.newObjectIdInstance(Book.class, s);
            Book book = (Book) persistencemanager.getObjectById(obj, true);
            HttpSession httpsession = httpservletrequest.getSession();
            User user = (User) httpsession.getAttribute("uname");
            Booked booked = new Booked();
            booked.setUser(user);
            booked.setBook(book);
            booked.setBookedDate(Calendar.getInstance().getTime());
            persistencemanager.makePersistent(booked);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return actionmapping.findForward("success");
    }
}
