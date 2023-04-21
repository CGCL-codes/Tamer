package hermes.browser.transferable;

import hermes.HermesRuntimeException;
import hermes.browser.components.BrowserTree;
import hermes.browser.components.ContextTree;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.apache.log4j.Logger;

/**
 * @author colincrist@hermesjms.com
 */
public class HermesAdministeredObjectTransferHandler extends TransferHandler {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3250317070022202875L;

    private static final Logger log = Logger.getLogger(HermesAdministeredObjectTransferHandler.class);

    public boolean canImport(JComponent component, DataFlavor[] flavors) {
        return component instanceof ContextTree;
    }

    protected Transferable createTransferable(JComponent component) {
        if (component instanceof BrowserTree) {
            BrowserTree tree = (BrowserTree) component;
            return new HermesAdministeredObjectTransferable(tree.getSelectedAdministeredObjectNodes());
        } else {
            throw new HermesRuntimeException("cannot create transferable, JComponent " + component.getClass().getName() + " is not a BrowserTree");
        }
    }

    public int getSourceActions(JComponent component) {
        if (component instanceof BrowserTree) {
            return COPY;
        } else {
            return NONE;
        }
    }

    public boolean importData(JComponent component, Transferable t) {
        if (component instanceof ContextTree && t instanceof HermesAdministeredObjectTransferable) {
            ContextTree contextTree = (ContextTree) component;
            return contextTree.doImport((HermesAdministeredObjectTransferable) t);
        } else {
            return false;
        }
    }
}
