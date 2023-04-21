package pcgen.CharacterViewer.fragments;

import pcgen.CharacterViewer.R;
import android.view.View;

public class NumberPickerDialogFragmentPrevious extends NumberPickerDialogFragment {

    @Override
    protected void initialize(View view) {
        _picker = (com.casadelgato.widgets.NumberPicker) view.findViewById(R.id.number_picker);
    }

    @Override
    protected int getValue() {
        return _picker.getValue();
    }

    @Override
    protected void setMaxValue(int value) {
        _picker.setMaxValue(value);
    }

    @Override
    protected void setMinValue(int value) {
        _picker.setMinValue(value);
    }

    @Override
    protected void setValue(int value) {
        _picker.setValue(value);
    }

    @Override
    protected void setVisibility(int value) {
        _picker.setVisibility(value);
    }

    private com.casadelgato.widgets.NumberPicker _picker;
}
