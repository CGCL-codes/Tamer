package de.strategytester.datamanager;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.eclipse.emf.teneo.hibernate.HbHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import Trading.TradingPackage;

/**
 * The Class TransactionManager.
 */
public class TransactionManager {

    private static TransactionManager singleton = null;

    private HbDataStore hbds = null;

    private SessionFactory sessionFactory = null;

    private Session session = null;

    private Transaction tx = null;

    private TransactionManager() {
        this.hbds = HbHelper.INSTANCE.createRegisterDataStore("MyDb");
        this.hbds.setEPackages(new EPackage[] { TradingPackage.eINSTANCE });
        this.hbds.initialize();
        this.sessionFactory = this.hbds.getSessionFactory();
    }

    public static TransactionManager getInstance() {
        if (TransactionManager.singleton == null) {
            TransactionManager.singleton = new TransactionManager();
        }
        return TransactionManager.singleton;
    }

    /**
	 * @return false if a new transaction was started, true if there was already
	 *         a transaction in progress!
	 */
    public boolean startTransaction() {
        boolean result = true;
        if (this.session == null) {
            this.session = this.sessionFactory.openSession();
            this.tx = this.session.getTransaction();
            this.tx.begin();
            result = false;
        }
        return result;
    }

    public void persistAndEndTransaction() {
        this.tx.commit();
        this.session.close();
        this.session = null;
    }

    /**
	 * @param executeIt
	 *            the transaction is only ended if executeit is true(intended to
	 *            be used with the return value of startTransaction to be able
	 *            to cascade transaction creation calls)
	 */
    public void persistAndEndTransaction(boolean executeIt) {
        if (executeIt) {
            this.persistAndEndTransaction();
        }
    }

    public void save(Object o) {
        if (this.session != null) {
            this.session.save(o);
        } else {
            this.startTransaction();
            this.session.save(o);
            this.persistAndEndTransaction();
        }
    }

    public Session getSession() {
        return this.session;
    }
}
