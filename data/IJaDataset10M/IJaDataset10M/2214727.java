package com.tensegrity.palowebviewer.modules.ui.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.util.GuiHelper;

public class SaveViewAsDialog extends DialogBox {

    private final TextBox tbName = new TextBox();

    private final TextArea tbDescrtiption = new TextArea();

    private final ArrayList listeners = new ArrayList();

    private final ClickListener okListener = new ClickListener() {

        public void onClick(Widget sender) {
            fireOnOk();
        }
    };

    private final ClickListener cancelListener = new ClickListener() {

        public void onClick(Widget sender) {
            fireOnCancel();
        }
    };

    private final KeyboardListenerAdapter keyboardListenerAdapter = new KeyboardListenerAdapter() {

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            if ((keyCode == KeyboardListener.KEY_ENTER)) {
                for (Iterator it = listeners.iterator(); it.hasNext(); ) {
                    SaveViewAsDialogListener listener = (SaveViewAsDialogListener) it.next();
                    listener.onOk(tbName.getText(), tbDescrtiption.getText());
                }
            }
        }
    };

    public void showDialog() {
        GuiHelper.centerShowDialog(this);
        tbName.setFocus(true);
    }

    public SaveViewAsDialog() {
        buildWidgets();
    }

    public void addListener(SaveViewAsDialogListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SaveViewAsDialogListener listener) {
        listeners.remove(listener);
    }

    protected void fireOnCancel() {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            SaveViewAsDialogListener listener = (SaveViewAsDialogListener) it.next();
            listener.onCancel();
        }
    }

    protected void fireOnOk() {
        for (Iterator it = listeners.iterator(); it.hasNext(); ) {
            SaveViewAsDialogListener listener = (SaveViewAsDialogListener) it.next();
            listener.onOk(tbName.getText(), tbDescrtiption.getText());
        }
    }

    private void buildWidgets() {
        final Button btOk = new Button("Ok");
        btOk.setStyleName("button");
        btOk.addClickListener(okListener);
        Button btCancel = new Button("Cancel");
        btCancel.setStyleName("button");
        btCancel.addClickListener(cancelListener);
        tbName.setVisibleLength(30);
        tbDescrtiption.setCharacterWidth(30);
        tbDescrtiption.setVisibleLines(10);
        HorizontalPanel paButtons = new HorizontalPanel();
        paButtons.setSpacing(3);
        paButtons.add(btOk);
        paButtons.add(btCancel);
        FlexTable table = new FlexTable();
        table.setStyleName("input_form");
        table.setText(0, 0, "Name");
        table.setText(1, 0, "Description");
        tbDescrtiption.setHeight("70");
        tbDescrtiption.setWidth("200");
        tbName.setWidth("200");
        tbName.addKeyboardListener(keyboardListenerAdapter);
        table.setWidget(0, 1, tbName);
        table.setWidget(1, 1, tbDescrtiption);
        table.setHeight("100");
        table.setWidget(3, 1, paButtons);
        setWidget(table);
    }
}
