package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_AlertRule
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_AD_AlertRule {

    /** TableName=AD_AlertRule */
    public static final String Table_Name = "AD_AlertRule";

    /** AD_Table_ID=593 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Column name AD_AlertRule_ID */
    public static final String COLUMNNAME_AD_AlertRule_ID = "AD_AlertRule_ID";

    /** Set Alert Rule.
	  * Definition of the alert element
	  */
    public void setAD_AlertRule_ID(int AD_AlertRule_ID);

    /** Get Alert Rule.
	  * Definition of the alert element
	  */
    public int getAD_AlertRule_ID();

    /** Column name AD_Alert_ID */
    public static final String COLUMNNAME_AD_Alert_ID = "AD_Alert_ID";

    /** Set Alert.
	  * Adempiere Alert
	  */
    public void setAD_Alert_ID(int AD_Alert_ID);

    /** Get Alert.
	  * Adempiere Alert
	  */
    public int getAD_Alert_ID();

    public I_AD_Alert getAD_Alert() throws Exception;

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

    /** Set Table.
	  * Database Table information
	  */
    public void setAD_Table_ID(int AD_Table_ID);

    /** Get Table.
	  * Database Table information
	  */
    public int getAD_Table_ID();

    public I_AD_Table getAD_Table() throws Exception;

    /** Column name ErrorMsg */
    public static final String COLUMNNAME_ErrorMsg = "ErrorMsg";

    /** Set Error Msg	  */
    public void setErrorMsg(String ErrorMsg);

    /** Get Error Msg	  */
    public String getErrorMsg();

    /** Column name FromClause */
    public static final String COLUMNNAME_FromClause = "FromClause";

    /** Set Sql FROM.
	  * SQL FROM clause
	  */
    public void setFromClause(String FromClause);

    /** Get Sql FROM.
	  * SQL FROM clause
	  */
    public String getFromClause();

    /** Column name IsValid */
    public static final String COLUMNNAME_IsValid = "IsValid";

    /** Set Valid.
	  * Element is valid
	  */
    public void setIsValid(boolean IsValid);

    /** Get Valid.
	  * Element is valid
	  */
    public boolean isValid();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

    /** Set Name.
	  * Alphanumeric identifier of the entity
	  */
    public void setName(String Name);

    /** Get Name.
	  * Alphanumeric identifier of the entity
	  */
    public String getName();

    /** Column name OtherClause */
    public static final String COLUMNNAME_OtherClause = "OtherClause";

    /** Set Other SQL Clause.
	  * Other SQL Clause
	  */
    public void setOtherClause(String OtherClause);

    /** Get Other SQL Clause.
	  * Other SQL Clause
	  */
    public String getOtherClause();

    /** Column name PostProcessing */
    public static final String COLUMNNAME_PostProcessing = "PostProcessing";

    /** Set Post Processing.
	  * Process SQL after executing the query
	  */
    public void setPostProcessing(String PostProcessing);

    /** Get Post Processing.
	  * Process SQL after executing the query
	  */
    public String getPostProcessing();

    /** Column name PreProcessing */
    public static final String COLUMNNAME_PreProcessing = "PreProcessing";

    /** Set Pre Processing.
	  * Process SQL before executing the query
	  */
    public void setPreProcessing(String PreProcessing);

    /** Get Pre Processing.
	  * Process SQL before executing the query
	  */
    public String getPreProcessing();

    /** Column name SelectClause */
    public static final String COLUMNNAME_SelectClause = "SelectClause";

    /** Set Sql SELECT.
	  * SQL SELECT clause
	  */
    public void setSelectClause(String SelectClause);

    /** Get Sql SELECT.
	  * SQL SELECT clause
	  */
    public String getSelectClause();

    /** Column name WhereClause */
    public static final String COLUMNNAME_WhereClause = "WhereClause";

    /** Set Sql WHERE.
	  * Fully qualified SQL WHERE clause
	  */
    public void setWhereClause(String WhereClause);

    /** Get Sql WHERE.
	  * Fully qualified SQL WHERE clause
	  */
    public String getWhereClause();
}
