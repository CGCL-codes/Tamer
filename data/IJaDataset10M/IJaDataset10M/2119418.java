package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.CollectionHelper;
import honeycrm.client.misc.View;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldEnum extends FieldString {

    private static final long serialVersionUID = -4542742508636055819L;

    protected String[] options;

    public FieldEnum() {
    }

    public FieldEnum(final String id, final String label, final String... options) {
        super(id, label, options[0]);
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    @Override
    protected void internalSetData(ListBox widget, Object value, View view) {
        if (view == View.CREATE) {
            final String[] options = getOptions();
            for (int i = 0; i < options.length; i++) {
                widget.addItem(options[i]);
            }
        } else if (view == View.EDIT) {
            final Set<String> selectedItems = (null == value || value.toString().isEmpty()) ? new HashSet<String>() : CollectionHelper.toSet(value.toString().split(FieldMultiEnum.SEPARATOR));
            final String[] options = getOptions();
            for (int i = 0; i < options.length; i++) {
                widget.addItem(options[i]);
                if (selectedItems.contains(options[i])) {
                    widget.setItemSelected(i, true);
                }
            }
        }
    }

    @Override
    protected Serializable internalGetData(Widget w) {
        final ListBox box = (ListBox) w;
        if (box.getSelectedIndex() == -1) {
            return "";
        } else {
            return box.getValue(box.getSelectedIndex());
        }
    }

    @Override
    public Column<Dto, String> getColumn(final String fieldName, final View viewMode, final Callback<Void> fieldUpdatedCallback) {
        if (View.isReadOnly(viewMode)) {
            return super.getColumn(fieldName, viewMode, fieldUpdatedCallback);
        } else {
            final ArrayList<String> optionList = new ArrayList<String>();
            for (final String o : options) {
                optionList.add(o);
            }
            final SelectionCell optionsCell = new SelectionCell(optionList);
            return new Column<Dto, String>(optionsCell) {

                @Override
                public String getValue(Dto object) {
                    return String.valueOf(object.get(fieldName));
                }
            };
        }
    }

    @Override
    protected Widget editField() {
        return new ListBox();
    }
}
