package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_Demand
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_M_Demand {

    /** TableName=M_Demand */
    public static final String Table_Name = "M_Demand";

    /** AD_Table_ID=723 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 2 - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(2);

    /** Column name C_Calendar_ID */
    public static final String COLUMNNAME_C_Calendar_ID = "C_Calendar_ID";

    /** Set Calendar.
	  * Accounting Calendar Name
	  */
    public void setC_Calendar_ID(int C_Calendar_ID);

    /** Get Calendar.
	  * Accounting Calendar Name
	  */
    public int getC_Calendar_ID();

    public I_C_Calendar getC_Calendar() throws Exception;

    /** Column name C_Year_ID */
    public static final String COLUMNNAME_C_Year_ID = "C_Year_ID";

    /** Set Year.
	  * Calendar Year
	  */
    public void setC_Year_ID(int C_Year_ID);

    /** Get Year.
	  * Calendar Year
	  */
    public int getC_Year_ID();

    public I_C_Year getC_Year() throws Exception;

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

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

    /** Set Comment/Help.
	  * Comment or Hint
	  */
    public void setHelp(String Help);

    /** Get Comment/Help.
	  * Comment or Hint
	  */
    public String getHelp();

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

    /** Column name M_Demand_ID */
    public static final String COLUMNNAME_M_Demand_ID = "M_Demand_ID";

    /** Set Demand.
	  * Material Demand
	  */
    public void setM_Demand_ID(int M_Demand_ID);

    /** Get Demand.
	  * Material Demand
	  */
    public int getM_Demand_ID();

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

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

    /** Set Process Now	  */
    public void setProcessing(boolean Processing);

    /** Get Process Now	  */
    public boolean isProcessing();
}
