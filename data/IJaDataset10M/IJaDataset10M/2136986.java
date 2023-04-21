package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_Channel
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_C_Channel {

    /** TableName=C_Channel */
    public static final String Table_Name = "C_Channel";

    /** AD_Table_ID=275 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Column name AD_PrintColor_ID */
    public static final String COLUMNNAME_AD_PrintColor_ID = "AD_PrintColor_ID";

    /** Set Print Color.
	  * Color used for printing and display
	  */
    public void setAD_PrintColor_ID(int AD_PrintColor_ID);

    /** Get Print Color.
	  * Color used for printing and display
	  */
    public int getAD_PrintColor_ID();

    public I_AD_PrintColor getAD_PrintColor() throws Exception;

    /** Column name C_Channel_ID */
    public static final String COLUMNNAME_C_Channel_ID = "C_Channel_ID";

    /** Set Channel.
	  * Sales Channel
	  */
    public void setC_Channel_ID(int C_Channel_ID);

    /** Get Channel.
	  * Sales Channel
	  */
    public int getC_Channel_ID();

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
