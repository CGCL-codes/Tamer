package org.imogene.web.gwt.client.ui.field;

import java.util.Date;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.util.DateUtil;
import org.zenika.widget.client.datePicker.DatePicker;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ImogDateTimeField extends ImogFieldAbstract<Date> {

    private String thisLabel;

    private Date thisValue;

    private boolean edited = false;

    private HorizontalPanel layout;

    private TextBox dateTimeDisplay;

    private DatePicker dateTimeEdit;

    /**
	 */
    public ImogDateTimeField() {
        layout();
    }

    /**
	 * @param label the field label
	 */
    public ImogDateTimeField(String label) {
        this();
        thisLabel = label;
    }

    /**
	 */
    private void layout() {
        layout = new HorizontalPanel();
        dateTimeDisplay = new TextBox();
        dateTimeEdit = new DatePicker();
        layout.add(dateTimeDisplay);
        initWidget(layout);
        properties();
    }

    /**
	 */
    private void properties() {
        dateTimeEdit.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                notifyValueChange();
            }
        });
        dateTimeDisplay.setStylePrimaryName("imogene-FormText");
        dateTimeDisplay.setEnabled(false);
        dateTimeEdit.setStylePrimaryName("imogene-FormText");
    }

    @Override
    public boolean validate() {
        if (isMandatory() && dateTimeEdit.getValue() == null) {
            displayError(BaseNLS.constants().field_mandatory());
            return false;
        }
        hideError();
        return true;
    }

    @Override
    public String getLabel() {
        if (thisLabel != null) return thisLabel;
        return "";
    }

    @Override
    public Date getValue() {
        return thisValue;
    }

    @Override
    public void setLabel(String label) {
        thisLabel = label;
    }

    @Override
    public void setValue(Date value) {
        thisValue = value;
        if (value != null) {
            dateTimeEdit.setSelectedDate(value);
            dateTimeDisplay.setText(DateUtil.getFormatedDateTime(value));
        }
    }

    @Override
    public void setValue(Date value, boolean notifyChange) {
        setValue(value);
        if (notifyChange) notifyValueChange();
    }

    @Override
    public void setEnabled(boolean editable) {
        if (!edited && editable) {
            layout.remove(dateTimeDisplay);
            layout.add(dateTimeEdit);
        }
        if (edited && !editable) {
            layout.remove(dateTimeEdit);
            layout.add(dateTimeDisplay);
        }
        edited = editable;
    }

    @Override
    public boolean isEdited() {
        return edited;
    }
}
