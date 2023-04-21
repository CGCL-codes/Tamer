package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_Greeting
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_C_Greeting {

    /** TableName=C_Greeting */
    public static final String Table_Name = "C_Greeting";

    /** AD_Table_ID=346 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Column name C_Greeting_ID */
    public static final String COLUMNNAME_C_Greeting_ID = "C_Greeting_ID";

    /** Set Greeting.
	  * Greeting to print on correspondence
	  */
    public void setC_Greeting_ID(int C_Greeting_ID);

    /** Get Greeting.
	  * Greeting to print on correspondence
	  */
    public int getC_Greeting_ID();

    /** Column name Greeting */
    public static final String COLUMNNAME_Greeting = "Greeting";

    /** Set Greeting.
	  * For letters, e.g. "Dear 
{
0}
" or "Dear Mr. 
{
0}
" - At runtime, "
{
0}
" is replaced by the name
	  */
    public void setGreeting(String Greeting);

    /** Get Greeting.
	  * For letters, e.g. "Dear 
{
0}
" or "Dear Mr. 
{
0}
" - At runtime, "
{
0}
" is replaced by the name
	  */
    public String getGreeting();

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

    /** Column name IsFirstNameOnly */
    public static final String COLUMNNAME_IsFirstNameOnly = "IsFirstNameOnly";

    /** Set First name only.
	  * Print only the first name in greetings
	  */
    public void setIsFirstNameOnly(boolean IsFirstNameOnly);

    /** Get First name only.
	  * Print only the first name in greetings
	  */
    public boolean isFirstNameOnly();

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
