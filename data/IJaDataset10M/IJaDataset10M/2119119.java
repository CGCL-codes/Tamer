package com.bluesky.plum.uimodels.render.html.components;

import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.bluesky.javawebbrowser.domain.html.tags.form.select.Option;
import com.bluesky.javawebbrowser.domain.html.tags.form.select.Select;
import com.bluesky.plum.uimodels.standard.components.ListBox;

public class HListBox extends ListBox {

    private Select selectBox;

    public HListBox() {
        super();
        selectBox = new Select();
    }

    @Override
    public void setObjects(List objects) {
        super.setObjects(objects);
        selectBox.getChildren().clear();
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            Option optionTag = new Option();
            optionTag.setValue(String.valueOf(i));
            optionTag.setLabel(o.toString());
            selectBox.addChild(optionTag);
        }
        selectBox.setSelectedIndex(defaultSelectedIndex);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        selectBox.setName(name);
    }

    @Override
    public Object getNativeComponent() {
        selectBox.setId(getId());
        selectBox.setName(getId());
        if (getSelectChangeListener() != null) {
            selectBox.setOnChange(String.format("_oum_post(this,'selectChange')", getId()));
        }
        return selectBox;
    }

    @Override
    public int getSelectedIndex() {
        return selectBox.getSelectedIndex();
    }

    @Override
    public int[] getSelectedIndices() {
        return new int[] { selectBox.getSelectedIndex() };
    }

    @Override
    public void setSelectedIndex(int index) {
        selectBox.setSelectedIndex(index);
    }

    @Override
    public void setSelectedIndices(int[] indices) {
        selectBox.setSelectedIndices(indices);
    }

    @Override
    public void setSelectedValue(Object value) {
        super.setSelectedValue(value);
        selectBox.setHttpValue(String.valueOf(getSelectedIndex()));
    }
}
