package net.sf.rcpforms.pojosample.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.converter.AbstractModelValidator;
import net.sf.rcpforms.modeladapter.converter.RequiredValidator;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.RCPTableData;
import net.sf.rcpforms.tablesupport.tables.TableUtil;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class PersonFormPart extends RCPFormPart {

    /**
     * Class DateFutureValidator is an example for a custom validator
     * 
     * @author Remo Loetscher
     */
    public static final class DateFutureValidator extends AbstractModelValidator {

        public Object[] getProperties() {
            return new String[] { PersonPojoDataModel.P_Birthdate };
        }

        public IStatus validate(Object value) {
            PersonPojoDataModel model = (PersonPojoDataModel) value;
            IStatus result = ok();
            if (model.getBirthdate() != null && model.getBirthdate().after(new Date())) {
                result = error("Birthdate has to be in the past!");
            }
            return result;
        }
    }

    private RCPComposite section;

    private RCPText name;

    private RCPText firstName;

    private RCPText street;

    private RCPText streetNumber;

    private RCPText city;

    private RCPCombo country;

    private RCPDatePicker datePicker;

    private GridBuilder formPartBuilder;

    private PersonPojoDataModel model;

    private RCPSimpleTable table;

    private TableViewer tableViewer;

    private ValidationManager validationManager;

    @Override
    public void bind(ValidationManager bm, Object modelBean) {
        model = (PersonPojoDataModel) modelBean;
        validationManager = bm;
        bm.bindValue(name, modelBean, PersonPojoDataModel.P_Name);
        bm.bindValue(firstName, modelBean, PersonPojoDataModel.P_FirstName);
        bm.bindValue(street, modelBean, PersonPojoDataModel.P_Street);
        bm.bindValue(streetNumber, modelBean, PersonPojoDataModel.P_StreetNumber);
        bm.bindValue(city, modelBean, PersonPojoDataModel.P_City);
        bm.bindValue(country, modelBean, PersonPojoDataModel.P_Country);
        bm.bindValue(datePicker, modelBean, PersonPojoDataModel.P_Birthdate);
        bm.addValidator(this, new RequiredValidator(PersonPojoDataModel.P_Name, PersonPojoDataModel.P_FirstName));
        bm.addValidator(this, new DateFutureValidator());
    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent) {
        section = new RCPComposite();
        name = new RCPText("Name: ");
        name.setState(EControlState.MANDATORY, true);
        firstName = new RCPText("Firstname: ");
        firstName.setState(EControlState.MANDATORY, true);
        street = new RCPText("Street/Number: ");
        streetNumber = new RCPText(null);
        city = new RCPText("City: ");
        country = new RCPCombo("Country: ");
        datePicker = new RCPDatePicker(PersonPojoDataModel.P_Birthdate);
        table = new RCPSimpleTable();
        formPartBuilder = new GridBuilder(toolkit, parent, 1);
        GridBuilder sectionBuilder = formPartBuilder.addContainer(section, 4);
        sectionBuilder.addLineGrabAndFill(name, 3);
        sectionBuilder.addLine(firstName);
        sectionBuilder.add(street);
        sectionBuilder.add(streetNumber);
        sectionBuilder.fillLine();
        sectionBuilder.addLine(city);
        sectionBuilder.addLine(country);
        sectionBuilder.add(datePicker);
        sectionBuilder.fillLine();
        GridBuilder buttonBar = sectionBuilder.addContainer(new RCPComposite(), 3);
        createButtonBar(buttonBar);
        sectionBuilder.fillLine();
        GridBuilder friendsBuilder = sectionBuilder.addContainerSpan(new RCPSection("Friends"), 1, 4, 1, false);
        buildFriendsPart(friendsBuilder);
    }

    private void createButtonBar(GridBuilder buttonBar) {
        RCPSimpleButton button = new RCPSimpleButton("set PersonData");
        buttonBar.add(button);
        button.getSWTButton().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setName("Sample " + System.currentTimeMillis());
                model.setFirstName("Nick " + System.currentTimeMillis());
            }
        });
        button = new RCPSimpleButton("update model->target");
        buttonBar.add(button);
        button.getSWTButton().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                validationManager.updateModelToForm();
            }
        });
        button = new RCPSimpleButton("update target->model");
        buttonBar.add(button);
        button.getSWTButton().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                validationManager.updateFormToModel();
            }
        });
    }

    private void buildFriendsPart(GridBuilder friendsBuilder) {
        friendsBuilder.add(table);
        RCPTableData tableData = new RCPTableData();
        tableData.columnConfigurations = createColumnConfigurations(true);
        tableViewer = TableUtil.configureTableViewer((TableViewer) table.getViewer(), PersonPojoDataModel.class, tableData);
        tableViewer.setInput(createPersonList());
    }

    private List<PersonPojoDataModel> createPersonList() {
        List<PersonPojoDataModel> list = new ArrayList<PersonPojoDataModel>();
        PersonPojoDataModel model = new PersonPojoDataModel();
        model.setName("Friend 1");
        list.add(model);
        model = new PersonPojoDataModel();
        model.setName("Friend 2");
        model.setCountry(Country.GERMANY);
        list.add(model);
        return list;
    }

    @Override
    public void setState(EControlState state, boolean value) {
        section.setState(state, value);
    }

    public void dispose() {
        section.dispose();
    }

    private static ColumnConfiguration[] createColumnConfigurations(boolean editable) {
        Object[] values = new Object[Country.values().length + 1];
        values[0] = NullValue.getInstance();
        System.arraycopy(Country.values(), 0, values, 1, values.length - 1);
        ColumnConfiguration[] columnConfigurations = { new ColumnConfiguration("Name", PersonPojoDataModel.P_Name, 100, SWT.LEFT, false, editable ? ECellEditorType.TEXT : null).setGrabHorizontal(true), new ColumnConfiguration("Country", PersonPojoDataModel.P_Country, 80, SWT.LEFT, values) };
        return columnConfigurations;
    }
}
