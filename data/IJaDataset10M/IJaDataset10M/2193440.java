package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_LabelPrinterFunction
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_AD_LabelPrinterFunction {

    /** TableName=AD_LabelPrinterFunction */
    public static final String Table_Name = "AD_LabelPrinterFunction";

    /** AD_Table_ID=624 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Column name AD_LabelPrinterFunction_ID */
    public static final String COLUMNNAME_AD_LabelPrinterFunction_ID = "AD_LabelPrinterFunction_ID";

    /** Set Label printer Function.
	  * Function of Label Printer
	  */
    public void setAD_LabelPrinterFunction_ID(int AD_LabelPrinterFunction_ID);

    /** Get Label printer Function.
	  * Function of Label Printer
	  */
    public int getAD_LabelPrinterFunction_ID();

    /** Column name AD_LabelPrinter_ID */
    public static final String COLUMNNAME_AD_LabelPrinter_ID = "AD_LabelPrinter_ID";

    /** Set Label printer.
	  * Label Printer Definition
	  */
    public void setAD_LabelPrinter_ID(int AD_LabelPrinter_ID);

    /** Get Label printer.
	  * Label Printer Definition
	  */
    public int getAD_LabelPrinter_ID();

    public I_AD_LabelPrinter getAD_LabelPrinter() throws Exception;

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

    /** Column name FunctionPrefix */
    public static final String COLUMNNAME_FunctionPrefix = "FunctionPrefix";

    /** Set Function Prefix.
	  * Data sent before the function
	  */
    public void setFunctionPrefix(String FunctionPrefix);

    /** Get Function Prefix.
	  * Data sent before the function
	  */
    public String getFunctionPrefix();

    /** Column name FunctionSuffix */
    public static final String COLUMNNAME_FunctionSuffix = "FunctionSuffix";

    /** Set Function Suffix.
	  * Data sent after the function
	  */
    public void setFunctionSuffix(String FunctionSuffix);

    /** Get Function Suffix.
	  * Data sent after the function
	  */
    public String getFunctionSuffix();

    /** Column name IsXYPosition */
    public static final String COLUMNNAME_IsXYPosition = "IsXYPosition";

    /** Set XY Position.
	  * The Function is XY position
	  */
    public void setIsXYPosition(boolean IsXYPosition);

    /** Get XY Position.
	  * The Function is XY position
	  */
    public boolean isXYPosition();

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

    /** Column name XYSeparator */
    public static final String COLUMNNAME_XYSeparator = "XYSeparator";

    /** Set XY Separator.
	  * The separator between the X and Y function.
	  */
    public void setXYSeparator(String XYSeparator);

    /** Get XY Separator.
	  * The separator between the X and Y function.
	  */
    public String getXYSeparator();
}
