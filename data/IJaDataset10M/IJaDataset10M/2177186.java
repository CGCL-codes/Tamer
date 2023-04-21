package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for R_IssueSystem
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_R_IssueSystem {

    /** TableName=R_IssueSystem */
    public static final String Table_Name = "R_IssueSystem";

    /** AD_Table_ID=843 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

    /** Set Asset.
	  * Asset used internally or by customers
	  */
    public void setA_Asset_ID(int A_Asset_ID);

    /** Get Asset.
	  * Asset used internally or by customers
	  */
    public int getA_Asset_ID();

    public I_A_Asset getA_Asset() throws Exception;

    /** Column name DBAddress */
    public static final String COLUMNNAME_DBAddress = "DBAddress";

    /** Set DB Address.
	  * JDBC URL of the database server
	  */
    public void setDBAddress(String DBAddress);

    /** Get DB Address.
	  * JDBC URL of the database server
	  */
    public String getDBAddress();

    /** Column name ProfileInfo */
    public static final String COLUMNNAME_ProfileInfo = "ProfileInfo";

    /** Set Profile.
	  * Information to help profiling the system for solving support issues
	  */
    public void setProfileInfo(String ProfileInfo);

    /** Get Profile.
	  * Information to help profiling the system for solving support issues
	  */
    public String getProfileInfo();

    /** Column name R_IssueSystem_ID */
    public static final String COLUMNNAME_R_IssueSystem_ID = "R_IssueSystem_ID";

    /** Set Issue System.
	  * System creating the issue
	  */
    public void setR_IssueSystem_ID(int R_IssueSystem_ID);

    /** Get Issue System.
	  * System creating the issue
	  */
    public int getR_IssueSystem_ID();

    /** Column name StatisticsInfo */
    public static final String COLUMNNAME_StatisticsInfo = "StatisticsInfo";

    /** Set Statistics.
	  * Information to help profiling the system for solving support issues
	  */
    public void setStatisticsInfo(String StatisticsInfo);

    /** Get Statistics.
	  * Information to help profiling the system for solving support issues
	  */
    public String getStatisticsInfo();

    /** Column name SystemStatus */
    public static final String COLUMNNAME_SystemStatus = "SystemStatus";

    /** Set System Status.
	  * Status of the system - Support priority depends on system status
	  */
    public void setSystemStatus(String SystemStatus);

    /** Get System Status.
	  * Status of the system - Support priority depends on system status
	  */
    public String getSystemStatus();
}
