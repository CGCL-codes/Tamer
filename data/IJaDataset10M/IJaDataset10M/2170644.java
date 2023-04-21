package com.doculibre.wicket.panels.tree.selectiontree;

import wicket.Component;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

@SuppressWarnings("serial")
public class SelectionTreeItem extends Panel {

    public SelectionTreeItem(String id, final Object elementObject, final AbstractSelectionTreePanel panel) {
        super(id);
        Model modelCheckBox = new Model() {

            @Override
            public Object getObject(Component component) {
                return new Boolean(panel.isChecked(elementObject));
            }
        };
        final CheckBox checkBox = new CheckBox("checkbox", modelCheckBox) {

            @Override
            public void onSelectionChanged() {
                panel.changeIsChecked(elementObject, true, true);
            }

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        checkBox.setEnabled(panel.isCheckable(elementObject));
        checkBox.add(new SimpleAttributeModifier("id", panel.getCode(elementObject)));
        this.add(checkBox);
        Component label = new Label("description", panel.getDescription(elementObject));
        label.add(new SimpleAttributeModifier("for", panel.getCode(elementObject)));
        this.add(label);
    }
}
