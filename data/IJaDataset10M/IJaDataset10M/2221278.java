package org.aplikator.client.widgets;

import java.io.Serializable;
import org.aplikator.client.Aplikator.AplikatorResources;
import org.aplikator.client.data.ListItem;
import org.aplikator.client.descriptor.PropertyDTO;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;

public class ComboBoxWidget<T extends Serializable> extends DataFieldBase<T> implements DataField<T> {

    private ListBox box = new ListBox(false);

    public ComboBoxWidget(String caption, PropertyDTO<T> property, boolean labelAbove) {
        super(caption, property, false, labelAbove);
        for (ListItem<T> item : property.getListValues()) {
            box.addItem(item.getName(), item.getValue().toString());
        }
        add(box);
        box.addStyleName(PaneWidgetResources.INSTANCE.css().formData());
        box.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                setDirty(true);
            }
        });
    }

    public void setValue(T value) {
        box.setSelectedIndex(-1);
        for (int i = 0; i < getProperty().getListValues().size(); i++) {
            if (getProperty().getListValues().get(i).getValue().equals(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        if (box.getSelectedIndex() >= 0) {
            box.setItemSelected(box.getSelectedIndex(), false);
        }
    }

    public T getValue() {
        int index = box.getSelectedIndex();
        if (index == -1) {
            return null;
        }
        return (T) getProperty().getListValues().get(index).getValue();
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return null;
    }

    public void setEnabled(boolean enabled) {
        box.setEnabled(enabled);
    }

    public void setDirty(boolean dirty) {
        super.setDirty(dirty);
        if (dirty) {
            box.addStyleName(AplikatorResources.INSTANCE.css().dirty());
        } else {
            box.removeStyleName(AplikatorResources.INSTANCE.css().dirty());
        }
    }
}
