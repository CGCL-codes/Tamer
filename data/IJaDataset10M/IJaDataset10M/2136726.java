package org.gridtrust.trs.dao;

/**
 * Interface for the DaoFactory
 * 
 * @author Silvano Riz
 */
public interface DaoFactory {

    /**
	 * Method to retrieve the UserDao
	 * @return the User Dao 
	 */
    public UserDao getUserDao();

    /**
	 * Method to retrieve the ServiceDao
	 * @return the Service Dao
	 */
    public ServiceDao getServiceDao();

    /**
	 * Method to retrieve the VoDao
	 * @return the Vo Dao
	 */
    public VoDao getVoDao();

    /**
	 * Method to retrieve the VbeDao
	 * @return the Vbe Dao
	 */
    public VbeDao getVbeDao();
}
