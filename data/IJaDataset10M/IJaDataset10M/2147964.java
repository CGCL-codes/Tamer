package cn.shining365.webclips.clipperbuilder.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.query.client.Regexp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

class PropertyTableUpdater {

    void updatePropertyTable(final ClipperBuilder clipperBuilder, final FlexTable propertyTable) {
        final ClipperBuilderActor actor = clipperBuilder.getActor();
        if (actor == null) {
            throw new IllegalStateException("actor is null");
        }
        final List<Property> properties = actor.getProperties();
        for (int i = propertyTable.getRowCount() - 1; i > 0; i--) {
            propertyTable.removeRow(i);
        }
        final Set<String> propertyNames = new HashSet<String>();
        for (Property property : properties) {
            propertyNames.add(property.getName());
        }
        final List<TextBox> nameTextBoxes = new ArrayList<TextBox>();
        for (int i = 1; i <= properties.size(); i++) {
            final int index = i - 1;
            final Property property = properties.get(index);
            final TextBox nameTextBox = new TextBox();
            nameTextBoxes.add(nameTextBox);
            nameTextBox.setWidth("70");
            nameTextBox.setText(property.getName());
            nameTextBox.addBlurHandler(new BlurHandler() {

                @Override
                public void onBlur(BlurEvent event) {
                    String newName = nameTextBox.getText().trim();
                    if ("".equals(newName) || !isLegalXmlName(newName)) {
                        Window.alert("Property names must conform to XML naming " + "rules. (But ':' and '-' should not be used.)");
                        nameTextBox.setText(property.getName());
                        return;
                    }
                    nameTextBox.setText(newName);
                    if (property.getName().equals(newName)) {
                        return;
                    }
                    Set<String> otherPropertyNames = new HashSet<String>(propertyNames);
                    otherPropertyNames.remove(property.getName());
                    if (otherPropertyNames.contains(newName)) {
                        Window.alert("Duplicate property name \"" + newName + "\".");
                        nameTextBox.setText(property.getName());
                        return;
                    }
                    propertyNames.remove(property.getName());
                    propertyNames.add(newName);
                    property.setName(newName);
                    actor.sendProperties();
                }
            });
            nameTextBox.addKeyPressHandler(new KeyPressHandler() {

                @Override
                public void onKeyPress(KeyPressEvent event) {
                    switch(event.getCharCode()) {
                        case KeyCodes.KEY_ENTER:
                            TextBox tb = nameTextBoxes.get((index + 1) % properties.size());
                            tb.setFocus(true);
                            tb.selectAll();
                            break;
                        case KeyCodes.KEY_ESCAPE:
                            nameTextBox.setText(property.getName());
                            break;
                    }
                }
            });
            final CheckBox trimCheckBox = new CheckBox();
            trimCheckBox.setValue(property.isTrim());
            trimCheckBox.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    property.setTrim(trimCheckBox.getValue());
                    actor.sendProperties();
                }
            });
            final CheckBox hideCheckBox = new CheckBox();
            hideCheckBox.setValue(property.isHide());
            hideCheckBox.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    property.setHide(hideCheckBox.getValue());
                    actor.sendProperties();
                }
            });
            Button modifyButton = new Button("M");
            modifyButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (!actor.getSelPaths().isEmpty() && !Window.confirm("This will clear unsaved " + "selections. Continue?")) {
                        return;
                    }
                    actor.clearHighlights();
                    actor.loadProperty(property.getName());
                    clipperBuilder.showModifyDeck(true);
                }
            });
            Button deleteButton = new Button("X");
            deleteButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    properties.remove(property);
                    updatePropertyTable(clipperBuilder, propertyTable);
                    actor.sendProperties();
                }
            });
            propertyTable.setWidget(i, 0, nameTextBox);
            propertyTable.setWidget(i, 1, trimCheckBox);
            propertyTable.setWidget(i, 2, hideCheckBox);
            propertyTable.setWidget(i, 3, modifyButton);
            propertyTable.setWidget(i, 4, deleteButton);
        }
    }

    private boolean isLegalXmlName(String name) {
        return xmlNameRegx.test(name);
    }

    private static final String nameStartChar = "A-Z_a-zÀ-ÖØ-öø-˿Ͱ-ͽͿ-῿ȀC-‍⁰-↏Ⰰ-⿯、-퟿豈-﷏ﷰ-�";

    private static final String nameEndChar = ".0-9·̀-ͯ‿-⁀";

    private static final Regexp xmlNameRegx = new Regexp("^[" + nameStartChar + "][" + nameStartChar + nameEndChar + "]*$");
}
