package org.adempierelbr.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LBR_Bank
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_LBR_Bank {

    /** TableName=LBR_Bank */
    public static final String Table_Name = "LBR_Bank";

    /** AD_Table_ID=1000003 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

    /** Get Client.
	  * Client/Tenant for this installation.
	  */
    public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

    /** Set Organization.
	  * Organizational entity within client
	  */
    public void setAD_Org_ID(int AD_Org_ID);

    /** Get Organization.
	  * Organizational entity within client
	  */
    public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

    /** Get Created.
	  * Date this record was created
	  */
    public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

    /** Get Created By.
	  * User who created this records
	  */
    public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

    /** Set Description.
	  * Optional short description of the record
	  */
    public void setDescription(String Description);

    /** Get Description.
	  * Optional short description of the record
	  */
    public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

    /** Set Active.
	  * The record is active in the system
	  */
    public void setIsActive(boolean IsActive);

    /** Get Active.
	  * The record is active in the system
	  */
    public boolean isActive();

    /** Column name LBR_Bank_ID */
    public static final String COLUMNNAME_LBR_Bank_ID = "LBR_Bank_ID";

    /** Set Bank.
	  * Primary Key table LBR_Bank
	  */
    public void setLBR_Bank_ID(int LBR_Bank_ID);

    /** Get Bank.
	  * Primary Key table LBR_Bank
	  */
    public int getLBR_Bank_ID();

    /** Column name lbr_jBoletoNo */
    public static final String COLUMNNAME_lbr_jBoletoNo = "lbr_jBoletoNo";

    /** Set jBoleto Number.
	  * Identifies the bank number at jBoleto
	  */
    public void setlbr_jBoletoNo(String lbr_jBoletoNo);

    /** Get jBoleto Number.
	  * Identifies the bank number at jBoleto
	  */
    public String getlbr_jBoletoNo();

    /** Column name lbr_PaymentLocation1 */
    public static final String COLUMNNAME_lbr_PaymentLocation1 = "lbr_PaymentLocation1";

    /** Set Payment Location 1.
	  * Identifies the Payment Location 1
	  */
    public void setlbr_PaymentLocation1(String lbr_PaymentLocation1);

    /** Get Payment Location 1.
	  * Identifies the Payment Location 1
	  */
    public String getlbr_PaymentLocation1();

    /** Column name lbr_PaymentLocation2 */
    public static final String COLUMNNAME_lbr_PaymentLocation2 = "lbr_PaymentLocation2";

    /** Set Payment Location 2.
	  * Identifies the Payment Location 2
	  */
    public void setlbr_PaymentLocation2(String lbr_PaymentLocation2);

    /** Get Payment Location 2.
	  * Identifies the Payment Location 2
	  */
    public String getlbr_PaymentLocation2();

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

    /** Column name RoutingNo */
    public static final String COLUMNNAME_RoutingNo = "RoutingNo";

    /** Set Routing No.
	  * Bank Routing Number
	  */
    public void setRoutingNo(String RoutingNo);

    /** Get Routing No.
	  * Bank Routing Number
	  */
    public String getRoutingNo();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

    /** Get Updated.
	  * Date this record was updated
	  */
    public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

    /** Get Updated By.
	  * User who updated this records
	  */
    public int getUpdatedBy();
}
