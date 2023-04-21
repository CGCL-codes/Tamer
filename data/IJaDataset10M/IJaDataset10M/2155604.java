package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_CashBook
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_C_CashBook {

    /** TableName=C_CashBook */
    public static final String Table_Name = "C_CashBook";

    /** AD_Table_ID=408 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Column name C_CashBook_ID */
    public static final String COLUMNNAME_C_CashBook_ID = "C_CashBook_ID";

    /** Set Cash Book.
	  * Cash Book for recording petty cash transactions
	  */
    public void setC_CashBook_ID(int C_CashBook_ID);

    /** Get Cash Book.
	  * Cash Book for recording petty cash transactions
	  */
    public int getC_CashBook_ID();

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

    /** Set Currency.
	  * The Currency for this record
	  */
    public void setC_Currency_ID(int C_Currency_ID);

    /** Get Currency.
	  * The Currency for this record
	  */
    public int getC_Currency_ID();

    public I_C_Currency getC_Currency() throws Exception;

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

    /** Column name IsDefault */
    public static final String COLUMNNAME_IsDefault = "IsDefault";

    /** Set Default.
	  * Default value
	  */
    public void setIsDefault(boolean IsDefault);

    /** Get Default.
	  * Default value
	  */
    public boolean isDefault();

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
}
