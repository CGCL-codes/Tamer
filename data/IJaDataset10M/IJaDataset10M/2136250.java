package pl.pyrkon.cm.client.base.ui;

import static com.smartgwt.client.types.Alignment.CENTER;
import static pl.pyrkon.cm.client.base.Constants.STYLE_PICKER;
import static pl.pyrkon.cm.client.i18n.I18N.getCaptions;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.layout.Layout;

public class HeaderPanel extends Layout {

    private final DynamicForm form;

    private final ComboBoxItem conventionsComboBox;

    private final CheckboxItem useConventionCheckBox;

    public HeaderPanel() {
        super();
        form = new DynamicForm();
        conventionsComboBox = new ComboBoxItem("name");
        useConventionCheckBox = new CheckboxItem("useConvention");
        createLayout();
    }

    private void createLayout() {
        form.setTitleOrientation(TitleOrientation.TOP);
        form.setColWidths("*", "*");
        form.setFields(useConventionCheckBox, conventionsComboBox);
        conventionsComboBox.setTitle(getCaptions().convention());
        useConventionCheckBox.setTitle(getCaptions().filterByEvent());
        addMember(form);
        setAlign(CENTER);
        setWidth100();
        setHeight(50);
        setStyleName(STYLE_PICKER);
    }

    public ComboBoxItem getConventionsComboBox() {
        return conventionsComboBox;
    }

    public CheckboxItem getUseConventionCheckBox() {
        return useConventionCheckBox;
    }
}
