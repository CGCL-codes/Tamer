package com.ilog.translator.java2cs.configuration.options;

import com.ilog.translator.java2cs.configuration.TranslatorProjectOptions.UnitTestLibrary;

public class UnitTestLibraryOptionEditor extends ComboBoxEditor<UnitTestLibrary> {

    String[] getListItems() {
        final UnitTestLibrary[] values = UnitTestLibrary.values();
        final String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = values[i].toString();
        }
        return stringValues;
    }

    @Override
    public void setOptionValue(OptionImpl<UnitTestLibrary> option, int index) {
        option.setValue(UnitTestLibrary.values()[index]);
    }
}
