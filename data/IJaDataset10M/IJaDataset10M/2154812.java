package net.sourceforge.thinfeeder.command.action;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.sourceforge.thinfeeder.Constants;
import net.sourceforge.thinfeeder.ThinFeeder;
import net.sourceforge.thinfeeder.model.dao.DAOSystem;
import net.sourceforge.thinfeeder.vo.I18NIF;

/**
 * @author fabianofranz@users.sourceforge.net
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InitI18NAction extends Action {

    public InitI18NAction(ThinFeeder main) {
        super(main);
    }

    public void doAction() throws Exception {
        ResourceBundle bundle;
        try {
            I18NIF i18n = DAOSystem.getSystem().getI18NObject();
            bundle = ResourceBundle.getBundle(Constants.I18N_BUNDLE_NAME, i18n.getCountry() == null ? new Locale(i18n.getLanguage()) : new Locale(i18n.getLanguage(), i18n.getCountry()));
        } catch (MissingResourceException e) {
            bundle = ResourceBundle.getBundle(Constants.I18N_BUNDLE_NAME, new Locale("en", "US"));
        }
        main.setResourceBundle(bundle);
    }
}
