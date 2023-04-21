package com.konarkdev.elibrary_manager.client;

import com.konarkdev.elibrary_manager.db.DBConnector;
import com.konarkdev.elibrary_manager.server.dataobjects.Book;
import com.konarkdev.elibrary_manager.server.dataobjects.History;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Collection;
import javax.jdo.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

public class ReturnBookAction extends Action {

    DBConnector dbconn;

    public ReturnBookAction() {
        dbconn = DBConnector.getInstance();
    }

    public ActionForward execute(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        PersistenceManager persistencemanager;
        String s;
        Transaction transaction;
        persistencemanager = dbconn.getPM();
        DynaActionForm dynaactionform = (DynaActionForm) actionform;
        s = (String) dynaactionform.get("bookid");
        transaction = persistencemanager.currentTransaction();
        try {
            transaction.begin();
            Object obj = persistencemanager.newObjectIdInstance(Book.class, s);
            Book book = (Book) persistencemanager.getObjectById(obj, true);
            com.konarkdev.elibrary_manager.server.dataobjects.User user = book.getBorrower();
            Query query = persistencemanager.newQuery(History.class);
            Collection collection = (Collection) query.execute();
            History ahistory[] = (History[]) (History[]) collection.toArray(new History[0]);
            History history = null;
            for (int i = 0; i < ahistory.length; i++) {
                History history1 = ahistory[i];
                if (history1.getUser() == user && history1.getBook() == book) {
                    history = history1;
                }
            }
            System.out.println((new StringBuilder()).append("got the history .. ").append(history).toString());
            if (history != null) {
                history.setReturnedDate(Calendar.getInstance().getTime());
            }
            book.setBorrower(null);
            persistencemanager.makePersistent(book);
            persistencemanager.makePersistent(history);
            httpservletrequest.setAttribute("id", s);
            httpservletrequest.setAttribute("saved", new Boolean(true));
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
