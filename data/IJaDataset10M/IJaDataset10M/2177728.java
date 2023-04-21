package gu.client.view.dialogs;

import gu.client.view.DatabaseEditorView;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ObjectDialogBox extends DialogBox {

    private FlexTable fields = new FlexTable();

    private final DatabaseEditorView view;

    public ObjectDialogBox(String string, DatabaseEditorView view) {
        this.view = view;
        setHTML(string);
        setWidget(fields);
        show();
        center();
    }

    public void addField(String name, String value) {
        TextBox fieldValue = new TextBox();
        fieldValue.setText(value);
        addField(name, fieldValue);
    }

    public void addField(String name, Widget fieldValue) {
        int row = fields.getRowCount();
        Label fieldName = new Label(name);
        fieldName.setStyleName("gwtapps-FieldName");
        fieldValue.setStyleName("gwtapps-FieldValue");
        fields.getCellFormatter().setWidth(row, 1, "100%");
        fields.setWidget(row, 0, fieldName);
        fields.setWidget(row, 1, fieldValue);
    }

    public void addButtons() {
        int row = fields.getRowCount();
        HorizontalPanel buttons = new HorizontalPanel();
        fields.setWidget(row, 1, buttons);
        buttons.add(new Button("Ok", new ClickListener() {

            public void onClick(Widget sender) {
                onSubmit();
                hide();
            }
        }));
        buttons.add(new Button("Cancel", new ClickListener() {

            public void onClick(Widget sender) {
                hide();
            }
        }));
        fields.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public String getField(int row) {
        TextBox field = (TextBox) fields.getWidget(row, 1);
        return field.getText();
    }

    public void onSubmit() {
    }

    public DatabaseEditorView getView() {
        return view;
    }
}
