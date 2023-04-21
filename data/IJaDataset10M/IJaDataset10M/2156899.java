package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for B_Seller
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_B_Seller {

    /** TableName=B_Seller */
    public static final String Table_Name = "B_Seller";

    /** AD_Table_ID=681 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

    /** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
    public void setAD_User_ID(int AD_User_ID);

    /** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
    public int getAD_User_ID();

    public I_AD_User getAD_User() throws Exception;

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

    /** Column name IsInternal */
    public static final String COLUMNNAME_IsInternal = "IsInternal";

    /** Set Internal.
	  * Internal Organization
	  */
    public void setIsInternal(boolean IsInternal);

    /** Get Internal.
	  * Internal Organization
	  */
    public boolean isInternal();

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

    /** Column name ValidTo */
    public static final String COLUMNNAME_ValidTo = "ValidTo";

    /** Set Valid to.
	  * Valid to including this date (last day)
	  */
    public void setValidTo(Timestamp ValidTo);

    /** Get Valid to.
	  * Valid to including this date (last day)
	  */
    public Timestamp getValidTo();
}
