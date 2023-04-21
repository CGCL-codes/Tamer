package net.sourceforge.olduvai.lrac.genericdataservice.cellviewer;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Most generic possible cell viewer.  Use only if 
 * overriding {@link GenericCellViewer} does not meet your requirements.  
 *   
 * @author peter
 *
 */
public abstract class AbstractCellViewer extends JDialog {

    /**
	 * Constructor for internal use.  
	 * 
	 * @param owner Owning LiveRAC JFrame
	 * @param modal Whether to lock the LiveRAC app while showing this cell viewer. (Should probably always be false.) 
	 */
    protected AbstractCellViewer(JFrame owner, boolean modal) {
        super(owner, modal);
    }
}
