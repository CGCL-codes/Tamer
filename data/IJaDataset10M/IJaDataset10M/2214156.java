package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_DesktopWorkbench
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_AD_DesktopWorkbench {

    /** TableName=AD_DesktopWorkbench */
    public static final String Table_Name = "AD_DesktopWorkbench";

    /** AD_Table_ID=459 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Column name AD_DesktopWorkbench_ID */
    public static final String COLUMNNAME_AD_DesktopWorkbench_ID = "AD_DesktopWorkbench_ID";

    /** Set Desktop Workbench	  */
    public void setAD_DesktopWorkbench_ID(int AD_DesktopWorkbench_ID);

    /** Get Desktop Workbench	  */
    public int getAD_DesktopWorkbench_ID();

    /** Column name AD_Desktop_ID */
    public static final String COLUMNNAME_AD_Desktop_ID = "AD_Desktop_ID";

    /** Set Desktop.
	  * Collection of Workbenches
	  */
    public void setAD_Desktop_ID(int AD_Desktop_ID);

    /** Get Desktop.
	  * Collection of Workbenches
	  */
    public int getAD_Desktop_ID();

    public I_AD_Desktop getAD_Desktop() throws Exception;

    /** Column name AD_Workbench_ID */
    public static final String COLUMNNAME_AD_Workbench_ID = "AD_Workbench_ID";

    /** Set Workbench.
	  * Collection of windows, reports
	  */
    public void setAD_Workbench_ID(int AD_Workbench_ID);

    /** Get Workbench.
	  * Collection of windows, reports
	  */
    public int getAD_Workbench_ID();

    public I_AD_Workbench getAD_Workbench() throws Exception;

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

    /** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
    public void setSeqNo(int SeqNo);

    /** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
    public int getSeqNo();
}
