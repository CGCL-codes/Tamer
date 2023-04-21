package org.fm.addressbook.dao;

/**
 * JContact- online Address Book Management System<a
 * href="http://jcontact.sourceforge.net/">http://jcontact.sourceforge.net/</a>
 * <p>
 * Licensed under the terms of any of the following licenses at your choice: -
 * <br/> GNU General Public License Version 2 or later (the "GPL") <a
 * href="http://www.gnu.org/licenses/gpl.html">http://www.gnu.org/licenses/gpl.html</a> -
 * <br/>GNU Lesser General Public License Version 2.1 or later (the "LGPL") <a
 * href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * <p>
 * DAO Factory class.
 * <p>
 * 
 * @author <a href="mailto:tennyson.varghese@yahoo.co.in">Tennyson Varghese</a>
 *         <br/> <a href="mailto:aneeshadoor2003@yahoo.co.in">Aneesh S</a>
 */
public abstract class DAOFactory {

    public static final int HIBERNATE = 1;

    public abstract AddressBookDAO getAddressBookDAO();

    public abstract ContactDAO getContactDAO();

    public abstract CategoryDAO getCategoryDAO();

    /**
	 * Select the DAOFactory with given param whichFactory
	 * 
	 * @param whichFactory
	 * @return DAOFactory
	 */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch(whichFactory) {
            case HIBERNATE:
                return new HibernateDAOFactory();
            default:
                return null;
        }
    }

    public static DAOFactory getDAOFactory() {
        return new HibernateDAOFactory();
    }
}
