package odrop.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import odrop.shared.dto.FilteringDTO;
import odrop.shared.dto.IPDateDTO;
import odrop.shared.dto.LoginDTO;

/**
 * The RPCHelper class contains static helper methods, that help accomplish relatively common tasks.
 * The methods are called from several *ServiceImpl-classes.
 * One method has a Unit-test. I have to add more tests for this and other classes, but somehow
 * I'm not motivated enough to do so.
 * 
 * @author divStar
 *
 */
public class RPCHelper {

    public static final int IMAGETYPE_ITEM = 0;

    public static final int IMAGETYPE_SKILL = 1;

    /**
	 * preprocessSQL(sql, argument, array)	helps assigning a variable amount of variables to a {?} placeholder.
	 * @param sql		the SQL-statement, containing placeholders
	 * @param argument	the argument inside the SQL-statement that we want to replace;
	 * 					NOTE: the first argument is supposed to be 1, the second is 2 and so on.
	 * 					I followed the suggestion of bindParam, where the first parameter is 1 as well (instead of 0)
	 * @param array		the array of values. Make sure that they are either String- or Integer-based, as no other values are handled yet.
	 * 					NOTE: the array is imploded using a comma as a delimiter-character.
	 * @return
	 */
    public static String preprocessSQL(String sql, int argument, Object[] array, String quotationMarks) {
        String processedSQL = sql;
        int positionOfNthArgument = getNthOccurance("{?}", processedSQL, argument);
        if (positionOfNthArgument == -1) {
            throw new NullPointerException("There's no (" + argument + ") occurance of {?} in \n" + processedSQL);
        }
        String leftPartOfSQL = processedSQL.substring(0, positionOfNthArgument);
        String exchangeableSQL = processedSQL.substring(positionOfNthArgument, positionOfNthArgument + 3);
        String rightPartOfSQL = processedSQL.substring(positionOfNthArgument + 3, processedSQL.length());
        exchangeableSQL = implode(array, ", ", quotationMarks);
        return leftPartOfSQL + exchangeableSQL + rightPartOfSQL;
    }

    /**
	 * getNthOccurance()	returns the position of the nth occurance of needle inside the haystack.
	 * @param needle		String that is searched for
	 * @param haystack		String that is searched in
	 * @param occurence		specifies the nth occurance of needle
	 * @return				an index at the nth position of needle inside the haystack - or -1 if not found
	 */
    public static int getNthOccurance(String needle, String haystack, int occurence) {
        int found = 0;
        Pattern pattern = Pattern.compile("\\Q" + needle + "\\E");
        Matcher matcher = pattern.matcher(haystack);
        while (matcher.find()) {
            found++;
            if (found == occurence) {
                return matcher.start();
            }
        }
        return -1;
    }

    /**
	 * implode()				create a string out of an array of objects. I recommend having an array of Strings or Integers.
	 * @param array				actual Object-array
	 * @param delimiter			delimiter to be used (", " should be used in most cases)
	 * @param wrapEachValueInto	if each element needs to be wrapped with brackets or something, you specify it in here
	 * @return					returns a String that goes like this: value1, value2, value3, value4
	 */
    public static String implode(Object[] array, String delimiter, String wrapEachValueInto) {
        StringBuilder exchangeable = new StringBuilder();
        for (Object item : array) {
            exchangeable.append(wrapEachValueInto).append(item).append(wrapEachValueInto).append(delimiter);
        }
        return exchangeable.substring(0, exchangeable.length() - 2);
    }

    /**
	 * implode()			 	create a string out of an array of objects. I recommend having an array of Strings or Integers.
	 * @param array				actual Object-array
	 * @param delimiter			delimiter to be used (", " should be used in most cases)
	 * @return					returns a String that goes like this: value1, value2, value3, value4
	 */
    public static String implode(Object[] array, String delimiter) {
        return implode(array, delimiter, "");
    }

    /**
	 * getOrderBy(columnList)	prepares an ORDER BY, which we'll use for sorting.
	 * Due to the fact, that data is loaded on demand, we can't do client-side sorting.
	 * Therefore we resort to server-side sorting, which we define here
	 * @return	a formatted ORDER BY part.
	 */
    public static String getOrderBy(String columnList) {
        return getOrderBy(new String[] { columnList });
    }

    public static String getOrderBy(String[] columnList) {
        return RPCHelper.preprocessSQL(" ORDER BY {?} ", 1, columnList, "");
    }

    /**
	 * getLimit(size)	prepares LIMIT string, which we'll use to limit the amount of results.
	 * 					This method will hardly be used though, because it doesn't let you specify
	 * 					the row to start at.
	 * @param size		amount of rows to return
	 * @return			LIMIT string, that should be attached to the SQL-statement
	 */
    public static String getLimit(int size) {
        return " LIMIT " + size;
    }

    /**
	 * getLimit(startingAtRow, size)	prepares LIMIT string, which we'll use to limit the amount of results.
	 * 									It's most likely that you will want to use this method for pagination of the results.
	 * @param size						amount of rows to return
	 * @param startAtRow				row to start at (e.g. 15 if you want to limit your result starting at row 15)
	 * @return							LIMIT string, that should be attached to the SQL-statement
	 */
    public static String getLimit(int size, int startAtRow) {
        return getLimit(startAtRow) + ", " + size;
    }

    public static String getFilterString(String filterString, boolean exactMatch) {
        if (filterString.length() > 0) {
            String filterStringSQL = " AND name ";
            if (exactMatch) {
                filterStringSQL += "=";
            } else {
                filterStringSQL += "LIKE";
            }
            filterStringSQL += " ?";
            return filterStringSQL;
        } else {
            return filterString;
        }
    }

    public static String getFilterBoxClause(HashMap<String, Boolean> filterBox, String column) {
        String result = " AND ";
        for (String key : filterBox.keySet()) {
            if (!filterBox.get(key)) {
                result += column + " <> '" + key + "' AND ";
            }
        }
        result = result.substring(0, result.length() - 5);
        return result;
    }

    /**
	 * getClass(className)	returns a fully formatted class name out of the
	 * 						class_name field of the class_list table.
	 * @param className		class_list.class_name-value (e.g. baseClassName in the CharacterDTO-object)
	 * @return				a human-readable string, representing the class name of the character
	 */
    public static String getClass(String className) {
        String soleClass = className.split("_")[className.split("_").length - 1];
        Pattern p = Pattern.compile("(?<=[^\\p{Upper}])(?=\\p{Upper})");
        String[] classWords = p.split(soleClass);
        String finalClassName = "";
        for (String word : classWords) {
            finalClassName += word + " ";
        }
        return finalClassName.trim();
    }

    /**
	 * getWhereClause(filterConditions)	returns a fully-qualified WHERE-clause based on the filter conditions HashMap.
	 * filterConditions-HashMap contains a map of conditions, which looks like this:
	 * 
	 * [HashMap] 						filterConditions
	 *  -> [key:String] 				name of the column (e.g. "getAssoc.baseType")
	 *  -> [value:ArrayList<String>]	column-values, which have to be parsed using the appropriate enum
	 *  	-> [eachValue: String]		column-value, which has to be parsed using the appropriate enum (which is of the same type as the field the key is referencing to, e.g. "ItemBaseType" for "getAssoc.baseType")
	 *
	 * @param filterConditions
	 * @return
	 */
    public static String getWhereClause(FilteringDTO filterBy) {
        String whereClause = "";
        boolean isFirstColumn = true;
        if (!filterBy.getFilterString().equals("")) {
            isFirstColumn = false;
            whereClause += "WHERE " + filterBy.getFilterNameColumn() + " ";
            if (filterBy.performExactMatch()) {
                whereClause += "$= \"" + filterBy.getFilterString() + "\"";
            } else {
                whereClause += "$LIKE \"%" + filterBy.getFilterString() + "%\"";
            }
        }
        if (!filterBy.getFilterConditions().isEmpty()) {
            for (String key : filterBy.getFilterConditions().keySet()) {
                String currentColumn = (isFirstColumn ? "WHERE " : " AND ");
                currentColumn += key;
                currentColumn += " NOT IN (";
                for (String value : filterBy.getFilterConditions().get(key)) {
                    currentColumn += "'" + value + "', ";
                }
                currentColumn = currentColumn.substring(0, currentColumn.length() - 2);
                currentColumn += ")";
                whereClause += currentColumn;
                isFirstColumn = false;
            }
        }
        return whereClause;
    }

    public static LoginDTO getLoginInformation(String username) {
        LoginDTO login = null;
        try {
            SQLService usernameExistsInL2JAccountsSQL = new SQLService(SQLConstants.LOGINSERVICE_USERNAME_IN_L2J_ACCOUNTS);
            usernameExistsInL2JAccountsSQL.getPS().setString(1, username);
            usernameExistsInL2JAccountsSQL.setResult(usernameExistsInL2JAccountsSQL.getPS().executeQuery());
            if (usernameExistsInL2JAccountsSQL.getResult().first() && usernameExistsInL2JAccountsSQL.getResult().getInt("count") == 1) {
                SQLService usernameExistsInODropAccountsSQL = new SQLService(SQLConstants.LOGINSERVICE_USERNAME_IN_ODROP_ACCOUNTS);
                usernameExistsInODropAccountsSQL.getPS().setString(1, username);
                usernameExistsInODropAccountsSQL.setResult(usernameExistsInODropAccountsSQL.getPS().executeQuery());
                if (usernameExistsInODropAccountsSQL.getResult().first() && usernameExistsInODropAccountsSQL.getResult().getInt("count") == 1) {
                    SQLService retrieveLoginInformationSQL = new SQLService(SQLConstants.LOGINSERVICE_LOGIN_FROM_ODROP_ACCOUNTS);
                    retrieveLoginInformationSQL.getPS().setString(1, username);
                    retrieveLoginInformationSQL.setResult(retrieveLoginInformationSQL.getPS().executeQuery());
                    if (retrieveLoginInformationSQL.getResult().first()) {
                        login = RPCHelper.fetchLoginInfo(retrieveLoginInformationSQL.getResult(), username);
                    }
                    retrieveLoginInformationSQL.destroy();
                } else if (usernameExistsInODropAccountsSQL.getResult().first() && usernameExistsInODropAccountsSQL.getResult().getInt("count") == 0) {
                    login = new LoginDTO();
                    login.setLastCorrectLogin(new IPDateDTO());
                    login.setLastWrongLogin(new IPDateDTO());
                    SQLService createLoginInformationSQL = new SQLService(SQLConstants.LOGINSERVICE_LOGIN_INTO_ODROP_ACCOUNTS);
                    createLoginInformationSQL.getPS().setString(1, username);
                    createLoginInformationSQL.getPS().executeUpdate();
                    createLoginInformationSQL.destroy();
                }
                usernameExistsInODropAccountsSQL.destroy();
            }
            usernameExistsInL2JAccountsSQL.destroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return login;
    }

    /**
	 * getProjectedDate(failedLogins, lastDate)	analyzes the amount of failed logins and returns
	 * a projected login date based on the lastDate and the failedLogins.
	 * 
	 * @param failedLogins	an integer specifying how many tries the user already had
	 * @param lastDate		date of the first failed login since the last correct login
	 * @return				a date that projects a date and time when
	 * 						the user will be able to login (based on failedLogins and lastDate)
	 */
    public static Date getProjectedDate(int failedLogins, Date lastDate) {
        int secondsToDelay;
        switch(failedLogins) {
            case 0:
            case 1:
            case 2:
            case 3:
            default:
                secondsToDelay = 0;
                break;
            case 4:
                secondsToDelay = 60;
                break;
            case 5:
                secondsToDelay = 120;
                break;
            case 6:
                secondsToDelay = 300;
                break;
            case 7:
                secondsToDelay = 600;
                break;
            case 8:
                secondsToDelay = 1800;
                break;
            case 9:
                secondsToDelay = -1;
                break;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        cal.add(Calendar.SECOND, secondsToDelay);
        return cal.getTime();
    }

    public static long waitForMilliseconds(Timestamp subtractFrom, Timestamp subtractWhat) {
        return (subtractFrom.getTime() - subtractWhat.getTime() - (new Date().getTime() - subtractWhat.getTime())) / 1000;
    }

    public static void increasePenalty(LoginDTO login, String username) {
        try {
            SQLService increasePenaltySQL = new SQLService(SQLConstants.LOGINSERVICE_INCREASE_PENALTY_IN_ODROP_ACCOUNTS);
            increasePenaltySQL.getPS().setInt(1, login.getFailedLogins());
            increasePenaltySQL.getPS().setBoolean(2, login.isPermanentlyBanned());
            increasePenaltySQL.getPS().setString(3, login.getLastWrongLogin().getIp());
            increasePenaltySQL.getPS().setTimestamp(4, new Timestamp(login.getLastWrongLogin().getDate().getTime()));
            increasePenaltySQL.getPS().setString(5, username);
            increasePenaltySQL.getPS().executeUpdate();
            increasePenaltySQL.destroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetPenalty(LoginDTO login, String username) {
        try {
            SQLService resetPenaltySQL = new SQLService(SQLConstants.LOGINSERVICE_CORRECT_LOGIN_IN_ODROP_ACCOUNTS);
            resetPenaltySQL.getPS().setInt(1, login.getFailedLogins());
            resetPenaltySQL.getPS().setString(2, login.getLastCorrectLogin().getIp());
            resetPenaltySQL.getPS().setTimestamp(3, new Timestamp(login.getLastCorrectLogin().getDate().getTime()));
            resetPenaltySQL.getPS().setString(4, username);
            resetPenaltySQL.getPS().executeUpdate();
            resetPenaltySQL.destroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LoginDTO fetchLoginInfo(ResultSet result, String username) throws SQLException {
        LoginDTO login = new LoginDTO();
        login.setFailedLogins(result.getInt("failedLogins"));
        login.setPermanentlyBanned(result.getBoolean("permanentlyBanned"));
        IPDateDTO lastCorrect = new IPDateDTO();
        lastCorrect.setIp(result.getString("lastCorrectIP"));
        lastCorrect.setDate(result.getTimestamp("lastCorrectDate"));
        login.setLastCorrectLogin(lastCorrect);
        IPDateDTO lastWrong = new IPDateDTO();
        lastWrong.setIp(result.getString("lastWrongIP"));
        lastWrong.setDate(result.getTimestamp("lastWrongDate"));
        login.setLastWrongLogin(lastWrong);
        return login;
    }
}
