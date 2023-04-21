package tms.client.admin;

import java.util.ArrayList;
import tms.client.entities.Field;
import tms.client.entities.InputField;
import tms.client.entities.InputModelField;
import tms.client.entities.InputModelSubfield;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class FieldPanel extends FocusPanel {

    public int tabIndex;

    private ArrayList<FieldPanel> childPanels = new ArrayList<FieldPanel>();

    private InputField field;

    private boolean focussed = false;

    private String fieldPanelLabel = "";

    private Widget widget;

    public FieldPanel(Field theField) {
        if (theField.isIndexField() || theField.isRecordAttribute()) this.field = new InputModelField(theField); else if (theField.isSynonymField()) {
            this.field = new InputModelField(theField);
            ((InputModelField) this.field).setSynonym(true);
        } else this.field = new InputModelSubfield(theField);
        this.setStyleName("fieldPanel");
        this.addStyleName("cursorMove");
        if (field.isFieldAttribute()) this.addStyleName("fieldAttribute");
        this.fieldPanelLabel = constructFieldPanelLabel();
        this.setWidget();
    }

    private String constructFieldPanelLabel() {
        return field.getFieldname() + " : " + field.getFielddatatypename() + " (" + field.getMaxlength() + ")";
    }

    public void updateFieldPanelLabel() {
        this.fieldPanelLabel = constructFieldPanelLabel();
        if (widget == null) {
            this.clear();
            this.setWidget();
        } else {
            if (widget instanceof TextBoxBase) ((TextBoxBase) widget).setText(fieldPanelLabel); else if (widget instanceof ListBox) {
                ((ListBox) widget).clear();
                ((ListBox) widget).addItem(fieldPanelLabel);
            }
        }
    }

    private void setWidget() {
        widget = null;
        if (field.isFreeform()) {
            if (field.getMaxlength() <= 50) {
                widget = new TextBox();
                ((TextBox) widget).setMaxLength(field.getMaxlength());
                ((TextBox) widget).setText(fieldPanelLabel);
            } else {
                widget = new TextArea();
                ((TextArea) widget).setText(fieldPanelLabel);
            }
        } else if (field.isPreset()) {
            widget = new ListBox();
            ((ListBox) widget).addItem(fieldPanelLabel);
        }
        widget.addStyleName("fieldPanelControlFont");
        widget.setWidth("200px");
        if (field.isIndexField()) {
            this.addStyleName("indexFieldPanel");
            widget.addStyleName("bold");
        }
        this.add(widget);
    }

    public void setField(InputField field) {
        this.field = field;
    }

    public InputField getField() {
        return this.field;
    }

    public void addChildPanel(FieldPanel panel) {
        this.childPanels.add(panel);
    }

    public void setChildPanels(ArrayList<FieldPanel> childPanels) {
        this.childPanels = childPanels;
    }

    public ArrayList<FieldPanel> getChildPanels() {
        return childPanels;
    }

    @Override
    public void setFocus(boolean focussed) {
        this.focussed = focussed;
        if (focussed) this.addStyleName("focussed"); else this.removeStyleName("focussed");
    }

    public boolean hasFocus() {
        return this.focussed;
    }

    public boolean isTopLevelPanel() {
        return this.getField().isIndexField() || this.getField().isSynonymField() || this.getField().isRecordAttribute();
    }
}
