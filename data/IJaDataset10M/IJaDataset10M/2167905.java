package com.once.server.security;

import java.util.Map;
import com.once.server.data.DataAccessException;
import com.once.server.data.source.sql.ISQLResults;

public interface ISecurityManager {

    public static final String USER_PREFERENCES_MARQUE_EACH = "marqueeeach";

    public static final String USER_PREFERENCES_MARQUE_WHOLE = "marqueewhole";

    public static final String USER_PREFERENCES_PAGE_BORDER = "pageborder";

    public static final String USER_PREFERENCES_AUTO_COMMIT = "autocommit";

    public static final String USER_PREFERENCES_MENU_BAR_X = "menubarx";

    public static final String USER_PREFERENCES_INSERT_RETURN_STYLE = "insertreturnstyle";

    public static final String USER_PREFERENCES_LARGE_SUBBLOCK_ICONS = "largesubblockicons";

    public static final String USER_PREFERENCES_WILDCARD_SEARCH = "wildcardsearch";

    public static final String ACTION_DELETE = "delete";

    public static final String ACTION_READ = "read";

    public static final String ACTION_WRITE = "write";

    public static final String APPLICATION_ADMIN = "admin";

    public static final String APPLICATION_CALENDAR = "editor";

    public static final String APPLICATION_CLIENT = "client";

    public static final String APPLICATION_EDITOR = "editor";

    public static final String OBJECT_TYPE_BLOCK = "block";

    public static final String OBJECT_TYPE_TABLE = "table";

    public boolean canBlockDelete(int userId, String block) throws DataAccessException;

    public boolean canBlockRead(int userId, String block) throws DataAccessException;

    public boolean canBlockWrite(int userId, String block) throws DataAccessException;

    public boolean canTableDelete(int userId, String table) throws DataAccessException;

    public boolean canTableRead(int userId, String table) throws DataAccessException;

    public boolean canTableWrite(int userId, String table) throws DataAccessException;

    public boolean canUseApplication(int userId, String application) throws DataAccessException;

    public void restrictTableDelete(int userId, String table) throws DataAccessException;

    public void restrictTableRead(int userId, String table) throws DataAccessException;

    public void restrictTableWrite(int userId, String table) throws DataAccessException;

    public String applyTableRestriction(String wherePart, String tableName, int userId, String action, String model) throws DataAccessException;

    public String getTableRestriction(int userId, String tableName, String action, String modelName) throws DataAccessException;

    public String getGeneratedSQL();

    public String getTableReadRestriction(int userId, String table, String model) throws DataAccessException;

    public void setUserPreferences(int userId, Map<String, String> userPrefs) throws DataAccessException;

    public Map<String, String> getUserPreferences(int userId) throws DataAccessException;

    public boolean isUsernameUsed(String userName) throws DataAccessException;

    public int createUser(String userName, String passwordHash, String ownerOrganization, String title, String firstName, String lastName, String middleName) throws DataAccessException, AccessDeniedException;

    public void updateUser(int userId, String passwordHash, String title, String firstName, String lastName, String middleName) throws DataAccessException, AccessDeniedException;

    public ISQLResults getUserInfo(int userId) throws DataAccessException, AccessDeniedException;
}
