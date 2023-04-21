package sls.crystalstore.db;

import java.util.Collection;
import sls.crystalstore.util.exception.CreateException;
import sls.crystalstore.util.exception.DatabaseException;
import sls.crystalstore.util.exception.DuplicateKeyException;
import sls.crystalstore.util.exception.ObjectNotFoundException;

public interface LanguageDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "languages";

    public void findByPrimaryKey(int languageID) throws ObjectNotFoundException, DatabaseException;

    public void deleteByPrimaryKey(int languageID) throws DatabaseException;

    public void create(LanguageBean bean) throws CreateException, DatabaseException, DuplicateKeyException;

    public void update(int languageID, LanguageBean bean) throws ObjectNotFoundException, DatabaseException;

    /************************************************
	 * Customized methods come below
	 ************************************************/
    public int getNumberOfLanuages() throws DatabaseException;

    public Collection getAllLanguages() throws DatabaseException;

    public LanguageBean getLanguage(int languageID) throws ObjectNotFoundException, DatabaseException;

    public LanguageBean getLanguage(String languageCode) throws ObjectNotFoundException, DatabaseException;
}
