package edu.unc.med.lccc.tcgasra.datalogic;

import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.unc.med.lccc.tcgasra.hibernateobj.HibernateSessionFactory;
import edu.unc.med.lccc.tcgasra.hibernateobj.LibrarySelection;

public class LibrarySelectionHibDAo {

    /**
	 * get LibrarySelection by primary key
	 * 
	 * @param LibrarySelectionId
	 * @return a LibrarySelection or null
	 */
    public LibrarySelection getLibrarySelectionByLibrarySelectionId(Integer librarySelectionID) {
        LibrarySelection librarySelection = null;
        Session session = null;
        Transaction tx = null;
        session = HibernateSessionFactory.getSession();
        tx = session.beginTransaction();
        librarySelection = (LibrarySelection) session.get(LibrarySelection.class, librarySelectionID);
        tx.commit();
        return librarySelection;
    }
}
