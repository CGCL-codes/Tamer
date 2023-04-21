package org.asianclassics.ace.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class OptionRadioGroup extends OptionGroup {

    protected String optionKey;

    protected HorizontalPanel options = new HorizontalPanel();

    public OptionRadioGroup(String key) {
        super();
        optionKey = key;
        main.add(options);
    }

    public void add(String label, boolean checked) {
        RadioButton rb = new RadioButton(optionKey, label);
        rb.setChecked(checked);
        options.add(rb);
    }

    public boolean isChecked(int index) {
        return ((RadioButton) options.getWidget(index)).isChecked();
    }
}
