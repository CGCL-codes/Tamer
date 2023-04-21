package be.lassi.ui.preferences;

import be.lassi.lanbox.ConnectionPreferences;
import be.lassi.ui.util.ValidatingPresentationModel;
import be.lassi.util.NLS;

/**
 * Presentation model for connection preferences.
 */
public class ConnectionPreferencesModel extends ValidatingPresentationModel {

    public ConnectionPreferencesModel(final ConnectionPreferences preferences) {
        super(preferences, new ConnectionPreferencesValidator(preferences));
    }

    public String getName() {
        return NLS.get("preferences.connection.title");
    }
}
