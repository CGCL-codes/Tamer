package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_RfQLine
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_C_RfQLine {

    /** TableName=C_RfQLine */
    public static final String Table_Name = "C_RfQLine";

    /** AD_Table_ID=676 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 1 - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(1);

    /** Column name C_RfQLine_ID */
    public static final String COLUMNNAME_C_RfQLine_ID = "C_RfQLine_ID";

    /** Set RfQ Line.
	  * Request for Quotation Line
	  */
    public void setC_RfQLine_ID(int C_RfQLine_ID);

    /** Get RfQ Line.
	  * Request for Quotation Line
	  */
    public int getC_RfQLine_ID();

    /** Column name C_RfQ_ID */
    public static final String COLUMNNAME_C_RfQ_ID = "C_RfQ_ID";

    /** Set RfQ.
	  * Request for Quotation
	  */
    public void setC_RfQ_ID(int C_RfQ_ID);

    /** Get RfQ.
	  * Request for Quotation
	  */
    public int getC_RfQ_ID();

    public I_C_RfQ getC_RfQ() throws Exception;

    /** Column name DateWorkComplete */
    public static final String COLUMNNAME_DateWorkComplete = "DateWorkComplete";

    /** Set Work Complete.
	  * Date when work is (planned to be) complete
	  */
    public void setDateWorkComplete(Timestamp DateWorkComplete);

    /** Get Work Complete.
	  * Date when work is (planned to be) complete
	  */
    public Timestamp getDateWorkComplete();

    /** Column name DateWorkStart */
    public static final String COLUMNNAME_DateWorkStart = "DateWorkStart";

    /** Set Work Start.
	  * Date when work is (planned to be) started
	  */
    public void setDateWorkStart(Timestamp DateWorkStart);

    /** Get Work Start.
	  * Date when work is (planned to be) started
	  */
    public Timestamp getDateWorkStart();

    /** Column name DeliveryDays */
    public static final String COLUMNNAME_DeliveryDays = "DeliveryDays";

    /** Set Delivery Days.
	  * Number of Days (planned) until Delivery
	  */
    public void setDeliveryDays(int DeliveryDays);

    /** Get Delivery Days.
	  * Number of Days (planned) until Delivery
	  */
    public int getDeliveryDays();

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

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

    /** Set Line No.
	  * Unique line for this document
	  */
    public void setLine(int Line);

    /** Get Line No.
	  * Unique line for this document
	  */
    public int getLine();

    /** Column name M_AttributeSetInstance_ID */
    public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";

    /** Set Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
    public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID);

    /** Get Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
    public int getM_AttributeSetInstance_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

    /** Set Product.
	  * Product, Service, Item
	  */
    public void setM_Product_ID(int M_Product_ID);

    /** Get Product.
	  * Product, Service, Item
	  */
    public int getM_Product_ID();

    public I_M_Product getM_Product() throws Exception;
}
