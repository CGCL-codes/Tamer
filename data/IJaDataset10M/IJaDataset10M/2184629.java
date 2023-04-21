package de.tobiasmaasland.voctrain.client.options;

import javax.swing.JOptionPane;
import de.tobiasmaasland.voctrain.client.util.Preferences;
import de.tobiasmaasland.voctrain.client.util.i18n.I18NToolkit;
import fi.mmm.yhteinen.swing.core.YController;

public class OptionsController extends YController {

    private OptionsModel model = new OptionsModel();

    private OptionsView view = new OptionsView();

    /**
	 * Standard constructor.
	 */
    public OptionsController() {
        setUpMVC(model, view);
    }

    /**
	 * Method must be available otherwise there will be warnings of the
	 * framework.
	 * 
	 * @see fi.mmm.yhteinen.swing.core.component.YFrame
	 */
    public void optionsViewOpened() {
    }

    /**
	 * View is closing.
	 * 
	 * @see fi.mmm.yhteinen.swing.core.component.YFrame
	 */
    public void optionsViewClosing() {
        view.removeAll();
        view.dispose();
        this.nullifyView();
        this.nullifyModel();
        this.getParent().removeChild(this);
    }

    /**
	 * Button Ok was pressed.
	 * 
	 * @see fi.mmm.yhteinen.swing.core.component.YButton
	 */
    public void btnOkPressed() {
        Preferences.getInstance().storeNewPreferences(model.getPreferences());
        optionsViewClosing();
    }

    /**
	 * Button Cancel was pressed.
	 * 
	 * @see fi.mmm.yhteinen.swing.core.component.YButton
	 */
    public void btnCancelPressed() {
        optionsViewClosing();
    }

    public void themeChanged() {
        JOptionPane.showMessageDialog(view, I18NToolkit.getLabel("options.view.notify.theme.change"));
    }
}
