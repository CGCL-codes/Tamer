package org.vikamine.app;

import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.vikamine.gui.VikamineUI;
import org.vikamine.kernel.KernelResources;
import org.vikamine.kernel.util.LoggerConfigurator;
import de.d3web.ka.gui.LocaleManager;
import de.d3web.ka.ui.ApplicationUI;
import de.d3web.ka.ui.UIManager;

/**
 * @author Atzmueller
 */
public class VIKAMINE {

    public static final ResourceBundle I18N = ResourceBundle.getBundle("resources.messages.vikamineMessages", LocaleManager.getInstance().getLocaleFromPreferences());

    public static final String VERSION = "1.9";

    public VIKAMINE() {
        super();
    }

    public static void main(final String[] args) {
        LoggerConfigurator.init("resources.logging.logging_off_finest");
        KernelResources.createInstance(LocaleManager.getInstance().getLocaleFromPreferences());
        VikamineConfigurator.newInstance().configure();
        new VIKAMINE().show();
    }

    public void show() {
        ApplicationUI appUI = UIManager.getInstance().getUi();
        if (appUI instanceof VikamineUI) {
            VikamineUI vikamineUI = (VikamineUI) appUI;
            vikamineUI.setVisiblyEnabled(true);
            vikamineUI.show();
        } else {
            Logger.getLogger(getClass().getName()).severe("The UI used by VIKAMINE is no VikamineUI!");
        }
    }
}
