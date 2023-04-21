package ch.bbv.mda.ui.actions;

import java.util.Collection;
import javax.swing.JOptionPane;
import ch.bbv.application.Locator;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.StatusBar;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.explorer.nodeactions.ValidateNodeAction;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.resources.MessagesMda;
import com.jgoodies.validation.ValidationResult;

/**
 * Generate the artifacts for all registered and active catridges. If the
 * validation of the model detects errors a warning message is displayed.
 * @author Marcel Baumann
 * @version $Revision: 1.16 $
 */
public class GenerateAllAction extends ExplorerGlobalActionImpl {

    /**
   * Serial version identifier coded as back compatibility date.
   */
    private static final long serialVersionUID = 20041201;

    /**
   * Default constructor.
   * @param explorer explorer owning the global action
   */
    public GenerateAllAction(Explorer explorer) {
        super(MessagesMda.actionGenerateAll, explorer);
    }

    public boolean execute() {
        switchWaitCursor(true);
        MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
        if (model != null) {
            CartridgeFactory factory = CartridgeFactory.getInstance();
            if (factory.shouldValidate()) {
                Validator validator = (Validator) Locator.getInstance().getComponent(Validator.NAME);
                ValidateNodeAction.validateTree(model, validator, getExplorer());
                if (getExplorer().getValidationPanel().getResults().hasErrors()) {
                    JOptionPane.showMessageDialog(getExplorer().getFrame(), MessagesMda.msgModelhasErrors, Messages.titleError, JOptionPane.ERROR_MESSAGE);
                    switchWaitCursor(false);
                    lastResult = false;
                    return lastResult;
                }
            }
            Collection<Cartridge> cartridges = factory.getCartridges();
            int size = cartridges.size();
            int i = 0;
            StatusBar statusBar = getExplorer().getStatusBar();
            ValidationResult messages = getExplorer().getValidationPanel().getResults();
            for (Cartridge cartridge : cartridges) {
                statusBar.setInfo("Generate" + cartridge.getPrefix());
                statusBar.setProgress(++i / size * 100);
                try {
                    factory.execute(cartridge, model, messages);
                } catch (Throwable t) {
                    messages.addError(cartridge.getName() + " failed");
                }
            }
            statusBar.setProgress(100);
            statusBar.setInfo("Generate All done");
            statusBar.setProgress(0);
        }
        switchWaitCursor(false);
        lastResult = true;
        return lastResult;
    }
}
