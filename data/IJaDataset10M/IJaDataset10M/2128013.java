package de.fmannan.addbook.editor;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.IWorkbenchAdapter;
import de.fmannan.addbook.common.fieldtypes.IBooleanField;
import de.fmannan.addbook.common.fieldtypes.IDateField;
import de.fmannan.addbook.common.fieldtypes.IDoubleField;
import de.fmannan.addbook.common.fieldtypes.IField;
import de.fmannan.addbook.common.fieldtypes.ILongField;
import de.fmannan.addbook.common.fieldtypes.IOnePeerAssociationField;
import de.fmannan.addbook.common.fieldtypes.IReadOnlyField;
import de.fmannan.addbook.common.fieldtypes.ISectionField;
import de.fmannan.addbook.common.fieldtypes.IStringField;
import de.fmannan.addbook.common.fieldtypes.IStringSelectField;
import de.fmannan.addbook.domainmodel.IContact;
import de.fmannan.addbook.editor.util.DateUtil;
import de.fmannan.addbook.editor.util.DoubleUtil;
import de.fmannan.addbook.editor.util.LongUtil;
import de.fmannan.addbook.editor.util.Messages;
import de.fmannan.addbook.editor.util.StringUtil;
import de.fmannan.addbook.views.ContactTableView;

/**
 * @author fmannan
 * 
 * Pane with all field widgets. Responsible for creating and storing the fields
 * of entities once they've been modified.
 */
public class GenericSectionPart extends SectionPart implements ModifyListener, SelectionListener {

    private static final Logger log = Logger.getLogger(GenericSectionPart.class);

    private IGenericEditorInput input;

    private final Composite sectionContent;

    /**
	 * Create and initialize the pane.
	 * 
	 * @param parent
	 *            Parent widget.
	 * @param toolkit
	 *            JFace toolkit used to fill the pane.
	 * @param style
	 *            Style bits used to create the pane.
	 * @param inputPar
	 *            Editor input.
	 */
    public GenericSectionPart(final Composite parent, FormToolkit toolkit, int style, IEditorInput inputTmp) {
        super(parent, toolkit, style);
        this.input = (IGenericEditorInput) inputTmp;
        Section section = super.getSection();
        section.setText("");
        section.setLayout(new GridLayout());
        Color backgroundColor = section.getBackground();
        sectionContent = new Composite(section, SWT.NONE);
        sectionContent.setLayout(new GridLayout(2, false));
        sectionContent.setBackground(backgroundColor);
        for (Iterator fieldsIter = input.getFieldsList().iterator(); fieldsIter.hasNext(); ) {
            IField currentField = (IField) fieldsIter.next();
            Label label;
            label = toolkit.createLabel(sectionContent, StringUtil.toWidget(currentField.getLabel()));
            label.setBackground(backgroundColor);
            GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
            label.setLayoutData(labelGridData);
            if (currentField instanceof IReadOnlyField) {
                IReadOnlyField field = (IReadOnlyField) currentField;
                Label labelWidget;
                labelWidget = toolkit.createLabel(sectionContent, StringUtil.toWidget(field.getValue()));
                prepareControl(toolkit, field, labelWidget);
            } else if (currentField instanceof IStringField) {
                IStringField field = (IStringField) currentField;
                Text textWidget;
                textWidget = toolkit.createText(sectionContent, StringUtil.toWidget(field.getValue()), SWT.BORDER);
                prepareControl(toolkit, field, textWidget);
                textWidget.addModifyListener(this);
            } else if (currentField instanceof IDateField) {
                final IDateField field = (IDateField) currentField;
                Text textWidget;
                textWidget = toolkit.createText(sectionContent, DateUtil.formatDate(field.getValue()), SWT.BORDER);
                prepareControl(toolkit, field, textWidget);
                textWidget.setToolTipText("The Date format is: dd.MM.yyyy");
                textWidget.addModifyListener(this);
            } else if (currentField instanceof ILongField) {
                ILongField field = (ILongField) currentField;
                Text textWidget;
                textWidget = toolkit.createText(sectionContent, LongUtil.formatLong(field.getValue()), SWT.BORDER);
                prepareControl(toolkit, field, textWidget);
                textWidget.addModifyListener(this);
            } else if (currentField instanceof IDoubleField) {
                IDoubleField field = (IDoubleField) currentField;
                Text textWidget;
                textWidget = toolkit.createText(sectionContent, DoubleUtil.formatDouble(field.getValue()), SWT.BORDER);
                prepareControl(toolkit, field, textWidget);
                textWidget.addModifyListener(this);
            } else if (currentField instanceof IBooleanField) {
                IBooleanField field = (IBooleanField) currentField;
                Button checkBox;
                checkBox = new Button(sectionContent, SWT.CHECK);
                checkBox.setBackground(backgroundColor);
                checkBox.setSelection(field.getValue());
                prepareControl(toolkit, field, checkBox);
                checkBox.addSelectionListener(this);
            } else if (currentField instanceof ISectionField) {
                ISectionField field = (ISectionField) currentField;
                Section sectionBar = toolkit.createSection(sectionContent, Section.TITLE_BAR | Section.EXPANDED);
                sectionBar.setText(field.getDescription());
                prepareControl(toolkit, field, sectionBar);
            } else if (currentField instanceof IStringSelectField) {
                IStringSelectField field = (IStringSelectField) currentField;
                Combo listWidget;
                listWidget = new Combo(sectionContent, SWT.READ_ONLY);
                Integer currentValueIndex = null;
                String currentValue = field.getValue();
                String[] selectables = field.getSelectables();
                log.debug("Current value from select-Field is :" + currentValue);
                for (int selectablesIndex = 0; selectablesIndex < selectables.length; selectablesIndex++) {
                    String selectableValue = selectables[selectablesIndex];
                    listWidget.add(selectableValue, selectablesIndex);
                    if (selectableValue.equals(currentValue)) {
                        currentValueIndex = new Integer(selectablesIndex);
                    }
                }
                prepareListWidget(toolkit, field, listWidget, currentValueIndex);
            } else {
                log.error("Field has an unexpted type");
            }
        }
        section.setClient(sectionContent);
    }

    /**
	 * Commits changes made to an entity to the internal model (only called when
	 * saving is confirmed). A newly created entity is marked as such with a
	 * boolean flag and hence its attributes will be saved AND it will be
	 * assigned to a parent item depending on it's entity type. Adding entities
	 * to their parents, takes place at the end of the method to ensure, that
	 * the attributes of the entities are properly updated.
	 * 
	 */
    public void commit(boolean onSave) {
        super.commit(onSave);
        Control[] widgets = sectionContent.getChildren();
        for (int widgetIndex = 0; widgetIndex < widgets.length; widgetIndex++) {
            Control control = widgets[widgetIndex];
            if (control instanceof Text) {
                Text widget = (Text) control;
                Object field = widget.getData();
                if (field instanceof IStringField) {
                    ((IStringField) field).setValue(StringUtil.fromWidget(widget.getText()));
                } else if (field instanceof IDateField) {
                    IDateField dateField = (IDateField) field;
                    try {
                        log.debug("Parsing Date from input: " + widget.getText());
                        Date dateValue = DateUtil.parseDate(widget.getText());
                        dateField.setValue(dateValue);
                    } catch (ParseException e) {
                        MessageDialog.openError(null, Messages.getString("GenericEditor.ValidateTitle"), Messages.getString("GenericEditor.ValidateTextPart1") + widget.getText() + Messages.getString("GenericEditor.ValidateTextPart2") + dateField.getLabel() + Messages.getString("GenericEditor.ValidateTextPart3") + e.getMessage() + Messages.getString("GenericEditor.ValidateTextPart4"));
                    }
                } else if (field instanceof ILongField) {
                    ILongField longField = (ILongField) field;
                    try {
                        long longValue = LongUtil.parseLong(widget.getText());
                        longField.setValue(longValue);
                    } catch (ParseException e) {
                        MessageDialog.openError(null, Messages.getString("GenericEditor.ValidateTitle"), Messages.getString("GenericEditor.ValidateTextPart1") + widget.getText() + Messages.getString("GenericEditor.ValidateTextPart2") + longField.getLabel() + Messages.getString("GenericEditor.ValidateTextPart3") + e.getMessage() + Messages.getString("GenericEditor.ValidateTextPart4"));
                    }
                } else if (field instanceof IDoubleField) {
                    IDoubleField doubleField = (IDoubleField) field;
                    try {
                        double doubleValue = DoubleUtil.parseDouble(widget.getText());
                        doubleField.setValue(doubleValue);
                    } catch (ParseException e) {
                        MessageDialog.openError(null, Messages.getString("GenericEditor.ValidateTitle"), Messages.getString("GenericEditor.ValidateTextPart1") + widget.getText() + Messages.getString("GenericEditor.ValidateTextPart2") + doubleField.getLabel() + Messages.getString("GenericEditor.ValidateTextPart3") + e.getMessage() + Messages.getString("GenericEditor.ValidateTextPart4"));
                    }
                }
            } else if (control instanceof Combo) {
                Combo widget = (Combo) control;
                Object field = widget.getData();
                if (field instanceof IOnePeerAssociationField) {
                    IOnePeerAssociationField associationField = (IOnePeerAssociationField) field;
                    IContact[] selectables = associationField.getSelectables();
                    int selectionIndex = widget.getSelectionIndex();
                    IContact selectedEntity;
                    if (selectionIndex == -1) {
                        selectedEntity = null;
                    } else {
                        selectedEntity = selectables[selectionIndex];
                    }
                    associationField.setValue(selectedEntity);
                } else if (field instanceof IStringSelectField) {
                    IStringSelectField selectField = (IStringSelectField) field;
                    selectField.setValue(widget.getText());
                }
            } else if (control instanceof Button) {
                Button widget = (Button) control;
                Object field = widget.getData();
                if (field instanceof IBooleanField) {
                    IBooleanField booleanField = (IBooleanField) field;
                    booleanField.setValue(widget.getSelection());
                }
            }
        }
        input.commit();
        if (ContactTableView.getInstance() != null) ContactTableView.getInstance().refresh();
    }

    public void modifyText(ModifyEvent e) {
        markDirty();
    }

    public void widgetSelected(SelectionEvent e) {
        markDirty();
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    private void prepareControl(FormToolkit toolkit, Object data, Control control) {
        toolkit.adapt(control, true, true);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gridData.minimumWidth = 200;
        control.setLayoutData(gridData);
        control.setData(data);
    }

    private void prepareListWidget(FormToolkit toolkit, Object data, Combo listWidget, Integer currentValueIndex) {
        if (currentValueIndex != null) {
            listWidget.select(currentValueIndex.intValue());
        }
        prepareControl(toolkit, data, listWidget);
        listWidget.addSelectionListener(this);
    }

    /**
	 * Helper method to adapt an entity to IWorkbenchAdapter.
	 * 
	 * @param contact
	 *            Entity to adpapt.
	 * @return Adapter if entity is adaptable, null otherwise.
	 */
    private IWorkbenchAdapter getWorkbenchAdapter(IContact contact) {
        IWorkbenchAdapter result;
        if (contact instanceof IAdaptable) {
            result = (IWorkbenchAdapter) ((IAdaptable) contact).getAdapter(IWorkbenchAdapter.class);
        } else {
            result = null;
        }
        return result;
    }
}
