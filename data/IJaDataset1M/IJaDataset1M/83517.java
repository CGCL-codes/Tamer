package com.nithinphilips.wifimusicsync.components;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class InputDialog extends PopupScreen {

    BasicEditField nameField;

    int result;

    public InputDialog() {
        super(new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR));
        RichTextField popupTitleField = null;
        popupTitleField = new RichTextField("Add New Playlist", RichTextField.NON_FOCUSABLE);
        net.rim.device.api.ui.Font defaultFont = popupTitleField.getFont();
        popupTitleField.setFont(defaultFont.derive(Font.BOLD, defaultFont.getHeight() + 1));
        RichTextField instructField = null;
        instructField = new RichTextField("Enter the name of an iTunes playlist. ", NON_FOCUSABLE);
        instructField.setFont(defaultFont.derive(Font.ITALIC, defaultFont.getHeight() - 2));
        nameField = new BasicEditField("Name: ", "", 300, BasicEditField.FIELD_RIGHT | BasicEditField.FILTER_FILENAME);
        HorizontalFieldManager buttonFieldMan = new HorizontalFieldManager(HorizontalFieldManager.FIELD_RIGHT);
        ButtonField connectField = new ButtonField("Add", HorizontalFieldManager.FIELD_RIGHT);
        connectField.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                if (nameField.getText().equals("")) {
                    Dialog.alert("The Name cannot be empty.");
                    nameField.setFocus();
                    return;
                }
                result = Dialog.OK;
                close();
            }
        });
        ButtonField quitField = new ButtonField("Cancel", HorizontalFieldManager.FIELD_RIGHT);
        quitField.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                result = Dialog.CANCEL;
                close();
            }
        });
        add(popupTitleField);
        add(new SeparatorField());
        add(instructField);
        add(nameField);
        buttonFieldMan.add(connectField);
        buttonFieldMan.add(quitField);
        add(buttonFieldMan);
    }

    int esc_key_count = 0;

    public boolean keyChar(char c, int status, int time) {
        if (c == Characters.ESCAPE) {
            esc_key_count++;
            if (esc_key_count >= 3) {
                close();
                return true;
            }
        }
        return super.keyChar(c, status, time);
    }

    public String getName() {
        return nameField.getText();
    }

    public int getResult() {
        return result;
    }
}
