package org.maestroframework.markup.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SelectList extends FormComponent {

    private List<Object> selectedOptions = new ArrayList<Object>();

    public SelectList(String name) {
        super("select");
        this.setName(name);
    }

    public SelectList(String name, Object... selectOptions) {
        this(name);
        this.addOptions(selectOptions);
    }

    public void setName(String name) {
        this.setAttribute("name", name);
    }

    public String getName() {
        return this.getAttribute("name");
    }

    public SelectOption addOption(Object value) {
        return this.addOption(value, value);
    }

    public SelectOption addOption(Object value, Object label) {
        SelectOption option = this.add(new SelectOption(value, label));
        if (selectedOptions.contains(value)) {
            option.setSelected(true);
        }
        return option;
    }

    public <T> void addOptions(T... selectOptions) {
        for (T option : selectOptions) {
            this.addOption(option);
        }
    }

    public void setSelected(Collection<?> options) {
        this.selectedOptions.addAll(options);
    }

    public void setSelected(Object... options) {
        this.selectedOptions.addAll(Arrays.asList(options));
    }

    public void addSeparator() {
        SelectOption option = this.addOption("", "----------------");
        option.setEnabled(false);
    }
}
