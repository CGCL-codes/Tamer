package org.op.wword;

import java.util.List;
import javax.swing.JFrame;

public interface EditAliasDialogView {

    /**
	 *  Open the dialog and return the results when OK clicked
	 *  
	 * @param frame - top frame to add the dialog
	 * @param aliasList - the original list
	 * 
	 * @return
	 */
    List<String> openAndGet(JFrame frame, List<String> aliasList);

    /**
	 *   Set the current list of aliases to be edited
	 *   
	 * @param aliasList
	 */
    void setAliases(List<String> aliasList);

    /**
	 *  Set the title of the dialog
	 *  
	 * @param title
	 * @return
	 */
    public void setTitle(String title);

    /**
	 *  Set the message to appear in the dialog
	 * 
	 * @param message
	 * @return
	 */
    public void setMessage(String message);
}
