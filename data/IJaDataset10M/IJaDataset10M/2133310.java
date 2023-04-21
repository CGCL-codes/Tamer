package mipt.crec.lab.gui.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import mipt.common.Utils;
import mipt.crec.lab.AbstractModule;
import mipt.gui.UIUtils;

/**
 * 
 * @author Evdokimov
 */
public class New extends SwingModuleCommand {

    /**
	 * @see mipt.aaf.command.Command#execute(java.lang.Object)
	 */
    public void execute(Object specification) {
        try {
            getLabModel().setVariant(null, false);
        } catch (FileNotFoundException e) {
            UIUtils.showError(getParentComponent(), Utils.getFileNotFoundString(AbstractModule.DEFAULT_VARIANT));
        } catch (IOException e) {
            UIUtils.showError(getParentComponent(), Utils.getErrorInFileString(AbstractModule.DEFAULT_VARIANT));
        }
    }
}
