package hoplugins.transfers.ui.component;

import hoplugins.transfers.constants.TransferTypes;
import javax.swing.JComboBox;

/**
 * ComboBox to edit the TrainingType
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class TransferTypeComboBox extends JComboBox {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1931409243964094858L;

    /**
     * Creates a new TrainingComboBox object.
     */
    public TransferTypeComboBox() {
        super();
        for (int i = -1; i < TransferTypes.NUMBER; i++) {
            addItem(TransferTypes.getTransferDesc(i));
        }
    }
}
