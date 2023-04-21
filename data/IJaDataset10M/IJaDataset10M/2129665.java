package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for K_Category
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_K_Category {

    /** TableName=K_Category */
    public static final String Table_Name = "K_Category";

    /** AD_Table_ID=615 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name K_Category_ID */
    public static final String COLUMNNAME_K_Category_ID = "K_Category_ID";

    /** Set Knowledge Category.
	  * Knowledge Category
	  */
    public void setK_Category_ID(int K_Category_ID);

    /** Get Knowledge Category.
	  * Knowledge Category
	  */
    public int getK_Category_ID();

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
