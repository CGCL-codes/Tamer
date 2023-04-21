package sls.crystalstore.db;

import java.util.HashMap;
import sls.crystalstore.util.exception.AssertionException;
import sls.crystalstore.util.exception.CreateException;
import sls.crystalstore.util.exception.DatabaseException;
import sls.crystalstore.util.exception.DuplicateKeyException;
import sls.crystalstore.util.exception.ObjectNotFoundException;

public interface ManufacturerDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "manufacturers";

    public static final String DESC_TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "manufacturers_info";

    public void findByPrimaryKey(int mfID) throws ObjectNotFoundException, DatabaseException;

    public void deleteByPrimaryKey(int mfID) throws DatabaseException;

    public void create(ManufacturerBean bean) throws CreateException, DatabaseException, DuplicateKeyException;

    public void update(int mfID, ManufacturerBean bean) throws ObjectNotFoundException, DatabaseException;

    /************************************************
	 * Customized methods come below
	 ************************************************/
    public int getNumberOfManufacturers() throws AssertionException, DatabaseException;

    public HashMap getManufacturerNames() throws DatabaseException;
}
