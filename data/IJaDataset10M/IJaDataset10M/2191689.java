package be.lassi.ui.preferences;

import be.lassi.preferences.DisplayPreferences;
import be.lassi.ui.util.ValidatingPresentationModel;
import be.lassi.util.NLS;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Presentation model for display preferences.
 */
public class DisplayPreferencesModel extends ValidatingPresentationModel {

    public DisplayPreferencesModel(final DisplayPreferences preferences) {
        super(preferences, new DisplayPreferencesValidator(preferences));
    }

    public String getName() {
        return NLS.get("preferences.display.title");
    }

    public BoundedRangeAdapter getSliderModel() {
        ValueModel vm = getModel(DisplayPreferences.COVERAGE);
        return new BoundedRangeAdapter(vm, 0, 0, 100);
    }
}
