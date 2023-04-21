package services.agrega;

import modelo.Transaction;
import modelo.Product;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.*;
import utilidades.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.*;
import modelo.Invoice;

public class AgregaInvoiceAction {

    private Invoice nueva = new Invoice();

    private static final Logger logger = LogManager.getLogger(AgregaInvoiceAction.class);

    public AgregaInvoiceAction() {
        init();
    }

    /**
      * Initializeing  
      * objects to be used by the application.
      */
    private synchronized void init() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.getTransaction().commit();
    }

    /**
      * Obtiene la CreditCard de hafner recien creado.
      * @return la CreditCard de hafner recien creado.
      */
    public Invoice getNewInvoice() {
        return this.nueva;
    }

    /**
      * Obtiene la Creditcard de hafner que se esta actualizando.
      * @return CreditCard de hafner que se esta actualizando.
      */
    public void setNewInvoice(Invoice newcreditcard) {
        this.nueva = newcreditcard;
    }

    /**
      * Listener para add creditcard button click action.
      * 
      */
    public boolean addTransaction() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Invoice p = this.getNewInvoice();
        try {
            session.save(p);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
