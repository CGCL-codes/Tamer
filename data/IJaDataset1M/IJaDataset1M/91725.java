package ch.romix.jsens.gui.influence;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

public interface IInfluenceMatrixView {

    /**
	 * This method initializes tabInfluence
	 * 
	 * @return javax.swing.JTabbedPane
	 */
    public JTabbedPane getTabInfluence();

    /**
	 * This method initializes butAdd
	 * 
	 * @return javax.swing.JButton
	 */
    public JButton getButAdd();

    /**
	 * This method initializes butRemove
	 * 
	 * @return javax.swing.JButton
	 */
    public JButton getButRemove();

    /**
	 * Ask the user to type a name for the new influence matrix
	 * 
	 * @return name of the new matrix or <code>null</code> if user pressed
	 *         cancel.
	 */
    public String getNewInfluenceMatrixName();
}
