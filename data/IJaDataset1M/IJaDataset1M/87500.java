package org.jcvi.vics.web.gwt.common.client.ui.renderers;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.vo.MultiSelectVO;
import java.util.List;

/**
 * I am hijacking this class to show a series of check-boxes for multi-selection
 *
 * @author Michael Press
 */
public class MultiSelectParameterRenderer extends ParameterRenderer {

    public MultiSelectParameterRenderer(MultiSelectVO param, String key, Task task) {
        super(param, key, task);
    }

    protected MultiSelectVO getMultiSelectValueObject() {
        return (MultiSelectVO) getValueObject();
    }

    protected void setSelectedItem(String selectedItem) {
        getMultiSelectValueObject().addActualChoice(selectedItem);
        setValueObject(getMultiSelectValueObject());
    }

    protected void setUnselectedItem(String unselectedItem) {
        getMultiSelectValueObject().removeActualChoice(unselectedItem);
        setValueObject(getMultiSelectValueObject());
    }

    @Override
    protected Widget createPanel() {
        VerticalPanel mainPanel = new VerticalPanel();
        FlexTable table = new FlexTable();
        table.setCellPadding(1);
        table.setCellSpacing(1);
        List<String> actuals = getMultiSelectValueObject().getActualUserChoices();
        int i = 0;
        int j = 0;
        for (String s : getMultiSelectValueObject().getPotentialChoices()) {
            CheckBox tmpCheckBox = new CheckBox(s);
            tmpCheckBox.setValue(actuals.contains(s));
            tmpCheckBox.addValueChangeHandler(new MyValueChangeHandler());
            table.setWidget(i / 2, j % 2, tmpCheckBox);
            i++;
            j++;
        }
        mainPanel.add(table);
        return mainPanel;
    }

    public class MyValueChangeHandler implements ValueChangeHandler {

        @Override
        public void onValueChange(ValueChangeEvent valueChangeEvent) {
            Boolean tmpValue = (Boolean) valueChangeEvent.getValue();
            if (tmpValue) {
                setSelectedItem(((CheckBox) valueChangeEvent.getSource()).getText());
            } else {
                setUnselectedItem(((CheckBox) valueChangeEvent.getSource()).getText());
            }
        }
    }
}
