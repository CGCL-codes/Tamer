package tms.client.exporttool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mysuso.client.xcontrols.ExtendedTabLayoutPanel;
import tms.client.BusyDialogManager;
import tms.client.accesscontrol.AccessController;
import tms.client.controls.ExtendedListBox;
import tms.client.controls.dialogs.AlertBox;
import tms.client.controls.dialogs.ErrorBox;
import tms.client.entities.InputField;
import tms.client.entities.InputModel;
import tms.client.entities.InputModelSubfield;
import tms.client.i18n.TMSConstants;
import tms.client.i18n.TMSMessages;
import tms.client.services.ExportTypesService;
import tms.client.services.ExportTypesServiceAsync;
import tms.client.util.ErrorHandler;
import tms.shared.util.DateUtilities;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel on the ExportDialog that changes as the export type is changed.
 * It displays the custom settings for each type of export.
 * 
 * @author Ismail Lavangee
 */
public class ExportPanel extends VerticalPanel {

    private static final TMSConstants constants = GWT.create(TMSConstants.class);

    private static final TMSMessages messages = GWT.create(TMSMessages.class);

    private ExportTypesServiceAsync _exportTypesService = GWT.create(ExportTypesService.class);

    private ExportType _export_type = null;

    private static final int RECORDS_TABLE = 0;

    private static final int INDEXES_TABLE = 1;

    private static final int INDEX_ATTRS_TABLE = 2;

    private static final int INDEX_ATTR_SUBFIELDS_ATTRS = 3;

    private ExportDialog _parent = null;

    public ExportPanel(ExportType export_type, ExportDialog export_dialog) {
        super();
        this._export_type = export_type;
        this._parent = export_dialog;
        this.setWidth("550px");
        this.setStyleName("borderedBlock");
        if (this._export_type.getExportType().equalsIgnoreCase("Field Template")) {
            if (this._export_type.getFields() == null) this.loadFields(); else Layout3();
        } else if (this._export_type.getExportType().equalsIgnoreCase("ODT") || this._export_type.getExportType().equalsIgnoreCase("Import Template")) {
            if (this._export_type.getInputModels() == null) this.loadInputModels(); else Layout2();
        } else if (this._export_type.getExportType().equalsIgnoreCase("TAB")) {
            if (this._export_type.getInputModels() == null) this.loadInputModels(); else Layout1();
        }
    }

    public ExportType getExportType() {
        return this._export_type;
    }

    private void loadFields() {
        BusyDialogManager.showBusyDialog(_parent);
        this._exportTypesService.getAllExportFields(AccessController.getAuthToken(), new AsyncCallback<ArrayList<InputField>>() {

            @Override
            public void onSuccess(ArrayList<InputField> result) {
                BusyDialogManager.hideBusyDialog();
                if (result.size() > 0) {
                    ExportPanel.this._export_type.setFields(result);
                    if (ExportPanel.this._export_type.getExportType().equalsIgnoreCase("Field Template")) Layout3();
                } else {
                    ErrorBox.show(constants.export_fields_no());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                BusyDialogManager.hideBusyDialog();
                ErrorHandler.handle(caught.getMessage());
            }
        });
    }

    private void loadInputModels() {
        BusyDialogManager.showBusyDialog(_parent);
        if (this._export_type.getExportType().equalsIgnoreCase("ODT")) {
            this._exportTypesService.getInputModels(AccessController.getAuthToken(), new AsyncCallback<ArrayList<InputModel>>() {

                @Override
                public void onSuccess(ArrayList<InputModel> result) {
                    BusyDialogManager.hideBusyDialog();
                    if (result.size() > 0) {
                        ExportPanel.this._export_type.setInputModels(result);
                        Layout2();
                    } else {
                        ErrorBox.show(constants.export_im_no());
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    BusyDialogManager.hideBusyDialog();
                    ErrorHandler.handle(caught.getMessage());
                }
            });
        } else if (this._export_type.getExportType().equalsIgnoreCase("Import Template")) {
            this._exportTypesService.getGenericInputModel(AccessController.getAuthToken(), constants.admin_im_name(), new AsyncCallback<ArrayList<InputModel>>() {

                @Override
                public void onFailure(Throwable caught) {
                    BusyDialogManager.hideBusyDialog();
                    ErrorHandler.handle(caught.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<InputModel> result) {
                    BusyDialogManager.hideBusyDialog();
                    if (result.size() > 0) {
                        ExportPanel.this._export_type.setInputModels(result);
                        Layout2();
                    } else {
                        ErrorBox.show(constants.export_im_no());
                    }
                }
            });
        } else if (ExportPanel.this._export_type.getExportType().equalsIgnoreCase("TAB")) {
            this._exportTypesService.getInputModels(AccessController.getAuthToken(), new AsyncCallback<ArrayList<InputModel>>() {

                @Override
                public void onSuccess(ArrayList<InputModel> result) {
                    BusyDialogManager.hideBusyDialog();
                    if (result.size() > 0) {
                        ExportPanel.this._export_type.setInputModels(result);
                        Layout1();
                    } else {
                        ErrorBox.show(constants.export_im_no());
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    BusyDialogManager.hideBusyDialog();
                    ErrorHandler.handle(caught.getMessage());
                }
            });
        }
    }

    private void Layout1() {
        final FlexTable table = new FlexTable();
        table.setWidth("100%");
        Label infoLabel = new Label(this._export_type.getInformation());
        infoLabel.addStyleName("plainLabelText");
        Label inputmodels_label = new Label(constants.export_selectIm());
        inputmodels_label.addStyleName("plainLabelText");
        Label sourceLabel = new Label(constants.export_source());
        sourceLabel.addStyleName("plainLabelText");
        Label targetLabel = new Label(constants.export_target());
        targetLabel.addStyleName("plainLabelText");
        Label commentLabel = new Label(constants.export_comment());
        commentLabel.addStyleName("plainLabelText");
        final ExtendedListBox<InputModel> inputmodel_list = new ExtendedListBox<InputModel>(false);
        inputmodel_list.setWidth("300px");
        final ExtendedListBox<InputField> sourceLB = new ExtendedListBox<InputField>(false);
        sourceLB.setWidth("300px");
        final ExtendedListBox<InputField> targetLB = new ExtendedListBox<InputField>(false);
        targetLB.setWidth("300px");
        sourceLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                InputField source = sourceLB.getSelectedItem();
                ExportPanel.this._export_type.setSourceField(source);
                ExportPanel.this._export_type.setSourceIndex(sourceLB.getSelectedIndex());
            }
        });
        targetLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                InputField target = targetLB.getSelectedItem();
                ExportPanel.this._export_type.setTargetField(target);
                ExportPanel.this._export_type.setTargetIndex(targetLB.getSelectedIndex());
            }
        });
        inputmodel_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ExportPanel.this._export_type.setInputModelIndex(inputmodel_list.getSelectedIndex());
                ExportPanel.this._export_type.setInputModel(inputmodel_list.getSelectedItem());
                ExportPanel.this.clearSelections(true, table);
                sourceLB.clear();
                targetLB.clear();
                ExportPanel.this._export_type.setFields(inputmodel_list.getSelectedItem().getIndexFields());
                LoadFieldListBoxes(sourceLB, targetLB);
            }
        });
        Button switchButton = new Button(constants.export_switch());
        switchButton.addClickHandler(new SwitchHandler(sourceLB, targetLB));
        final TextBox commentBox = new TextBox();
        commentBox.setText("");
        commentBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                String text = commentBox.getText();
                ExportPanel.this._export_type.setCommentForTabDoc(text);
            }
        });
        loadInputModelsListBox(inputmodel_list);
        ExportPanel.this._export_type.setFields(inputmodel_list.getSelectedItem().getIndexFields());
        LoadFieldListBoxes(sourceLB, targetLB);
        setCurrentDate(commentBox);
        this.add(infoLabel);
        table.setWidget(0, 0, inputmodels_label);
        table.getCellFormatter().addStyleName(0, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(0, 1, inputmodel_list);
        table.getCellFormatter().addStyleName(0, 1, "exportPanelFlexTableListBoxCell");
        table.setWidget(1, 0, sourceLabel);
        table.getCellFormatter().addStyleName(1, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(1, 1, sourceLB);
        table.getCellFormatter().addStyleName(1, 1, "exportPanelFlexTableListBoxCell");
        table.setWidget(2, 0, targetLabel);
        table.getCellFormatter().addStyleName(2, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(2, 1, targetLB);
        table.getCellFormatter().addStyleName(2, 1, "exportPanelFlexTableListBoxCell");
        table.setWidget(3, 1, switchButton);
        table.getCellFormatter().addStyleName(3, 1, "exportPanelFlexTableListBoxCell");
        table.setWidget(4, 0, commentLabel);
        table.getCellFormatter().addStyleName(4, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(4, 1, commentBox);
        table.getCellFormatter().addStyleName(4, 1, "exportPanelFlexTableListBoxCell");
        if (sourceLB.getItemCount() == 0 || targetLB.getItemCount() == 0) {
            AlertBox.show(constants.fault_minor(), messages.export_no_rights1(_export_type.getExportType()), false, true);
            sourceLB.setEnabled(false);
            targetLB.setEnabled(false);
            switchButton.setEnabled(false);
            commentBox.setEnabled(false);
        }
        this.add(table);
    }

    private void LoadFieldListBoxes(ExtendedListBox<InputField> sourceLB, ExtendedListBox<InputField> targetLB) {
        ArrayList<InputField> fields = this._export_type.getFields();
        for (InputField f : fields) {
            if (f.isIndexField() && f.isExportable()) {
                sourceLB.addItem(f.getFieldname(), f.getFieldname(), f);
                targetLB.addItem(f.getFieldname(), f.getFieldname(), f);
            }
        }
        if (this._export_type.getSourceIndex() == -1) {
            sourceLB.setSelectedIndex(0);
            this._export_type.setSourceIndex(0);
        } else sourceLB.setSelectedIndex(this._export_type.getSourceIndex());
        ExportPanel.this._export_type.setSourceField(sourceLB.getSelectedItem());
        if (fields.size() > 1) {
            if (this._export_type.getTargetIndex() == -1) {
                targetLB.setSelectedIndex(1);
                this._export_type.setTargetIndex(1);
            } else targetLB.setSelectedIndex(this._export_type.getTargetIndex());
        }
        ExportPanel.this._export_type.setTargetField(targetLB.getSelectedItem());
    }

    private void setCurrentDate(final TextBox commentBox) {
        if (!this._export_type.getCommentForTabDoc().isEmpty()) commentBox.setText(this._export_type.getCommentForTabDoc()); else {
            String comment = constants.export_exportOn() + DateUtilities.getCurrentDateAsString();
            ExportPanel.this._export_type.setCommentForTabDoc(comment);
            commentBox.setText(comment);
        }
    }

    private void Layout2() {
        ScrollPanel container = new ScrollPanel();
        container.setWidth("100%");
        container.setHeight("300px");
        final FlexTable table = new FlexTable();
        table.setWidth("100%");
        Label infoLabel = new Label(this._export_type.getInformation());
        infoLabel.addStyleName("plainLabelText");
        Label inputmodels_label = new Label(constants.export_selectIm());
        inputmodels_label.addStyleName("plainLabelText");
        final ExtendedListBox<InputModel> inputmodel_list = new ExtendedListBox<InputModel>(false);
        inputmodel_list.setWidth("300px");
        Label record_fields_label = new Label(constants.export_selectRa());
        record_fields_label.addStyleName("plainLabelText");
        final ExtendedListBox<InputField> record_field_list = new ExtendedListBox<InputField>(true);
        record_field_list.setWidth("300px");
        record_field_list.setHeight("100px");
        Label index_field_label = new Label(constants.export_selectSource());
        index_field_label.addStyleName("plainLabelText");
        final ExtendedListBox<InputField> index_field_list = new ExtendedListBox<InputField>(false);
        index_field_list.setWidth("300px");
        Label index_subfield_label = new Label(constants.export_selectSourceAtrb());
        index_subfield_label.addStyleName("plainLabelText");
        final ExtendedListBox<InputField> index_subfield_list = new ExtendedListBox<InputField>(true);
        index_subfield_list.setWidth("300px");
        index_subfield_list.setHeight("100px");
        Label target_field_label = new Label(constants.export_selectTargets());
        target_field_label.addStyleName("plainLabelText");
        final ExtendedListBox<InputField> target_field_list = new ExtendedListBox<InputField>(true);
        target_field_list.setWidth("300px");
        target_field_list.setHeight("100px");
        inputmodel_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ExportPanel.this._export_type.setInputModelIndex(inputmodel_list.getSelectedIndex());
                ExportPanel.this._export_type.setInputModel(inputmodel_list.getSelectedItem());
                ExportPanel.this.clearSelections(true, table);
                record_field_list.clear();
                index_field_list.clear();
                index_subfield_list.clear();
                target_field_list.clear();
                if (_export_type.getExportType().equalsIgnoreCase("ODT")) loadRecordFieldsListBox(record_field_list, true); else loadRecordFieldsListBox(record_field_list, false);
                loadIndexFieldListBox(index_field_list);
                loadIndexSubFieldListBox(index_subfield_list);
                loadTargetFieldListBox(target_field_list);
            }
        });
        record_field_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ArrayList<Integer> indexes = ExportPanel.this._export_type.getRecordFieldIndexes();
                ArrayList<InputField> record_fields = ExportPanel.this._export_type.getRecordFields();
                indexes.clear();
                record_fields.clear();
                ArrayList<InputField> listbox_items = record_field_list.getItems();
                for (int i = 0; i < record_field_list.getItemCount(); i++) {
                    if (record_field_list.isItemSelected(i)) {
                        indexes.add(i);
                        record_fields.add(listbox_items.get(i));
                    }
                }
                ExportPanel.this._export_type.setRecordFieldIndexes(indexes);
                ExportPanel.this._export_type.setRecordFields(record_fields);
            }
        });
        index_field_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ExportPanel.this._export_type.setIndexFieldIndex(index_field_list.getSelectedIndex());
                ExportPanel.this._export_type.setIndexField(index_field_list.getSelectedItem());
                ExportPanel.this.clearSelections(false, table);
                index_field_list.clear();
                index_subfield_list.clear();
                target_field_list.clear();
                loadIndexFieldListBox(index_field_list);
                loadIndexSubFieldListBox(index_subfield_list);
                loadTargetFieldListBox(target_field_list);
            }
        });
        index_subfield_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ArrayList<Integer> indexes = ExportPanel.this._export_type.getIndexSubFieldIndexes();
                ArrayList<InputField> index_subfields = ExportPanel.this._export_type.getIndexSubFields();
                indexes.clear();
                index_subfields.clear();
                ArrayList<InputField> listbox_items = index_subfield_list.getItems();
                for (int i = 0; i < index_subfield_list.getItemCount(); i++) {
                    if (index_subfield_list.isItemSelected(i)) {
                        index_subfields.add(listbox_items.get(i));
                        if (!listbox_items.get(i).isSynonymField()) indexes.add(i);
                    }
                }
                ExportPanel.this._export_type.setIndexSubFieldIndexes(indexes);
                ExportPanel.this._export_type.setIndexSubFields(index_subfields);
                int before_row_index = 3;
                if (containsSynonymField(_export_type.getIndexSubFields())) {
                    if (!_export_type.hasSynonymSubFields()) {
                        insertSynonymSubFieldList(table, before_row_index, _export_type.getIndexField(), _export_type.getIndexSubFields());
                        _export_type.setHasSynonymSubFields(true);
                    }
                } else {
                    if (_export_type.hasSynonymSubFields()) {
                        removeSynonymSubFieldList(table, before_row_index);
                        _export_type.setHasSynonymSubFields(false);
                    }
                }
            }
        });
        target_field_list.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                final ArrayList<TargetField> target_fields = ExportPanel.this._export_type.getTargetFields();
                final ArrayList<InputField> selected_targets = ExportPanel.this._export_type.getSelectedTargets();
                selected_targets.clear();
                ArrayList<InputField> listbox_items = target_field_list.getItems();
                for (int i = 0; i < target_field_list.getItemCount(); i++) {
                    if (target_field_list.isItemSelected(i)) {
                        InputField inputfield = listbox_items.get(i);
                        selected_targets.add(inputfield);
                    }
                }
                ExportPanel.this.checkTargetFieldList(selected_targets, target_fields);
                ExportPanel.this._export_type.setTargetFields(target_fields);
                int starting_row = 5;
                int column = 0;
                removeTargetAttribs(table, starting_row);
                for (final InputField selected_target : selected_targets) {
                    final TargetField target_field = ExportPanel.this.findTargetField(selected_target, target_fields);
                    target_field.setHasSynonymSubFields(false);
                    target_field.setIndexOnTable(starting_row);
                    Label target_subfield_label = new Label(messages.export_selectTargetAtrb(selected_target.getFieldname()));
                    target_subfield_label.addStyleName("plainLabelText");
                    final ExtendedListBox<InputField> target_subfield_list = new ExtendedListBox<InputField>(true);
                    target_subfield_list.setWidth("300px");
                    target_subfield_list.setHeight("100px");
                    target_subfield_list.addChangeHandler(new ChangeHandler() {

                        @Override
                        public void onChange(ChangeEvent event) {
                            ArrayList<InputField> target_subfields = new ArrayList<InputField>();
                            ArrayList<Integer> target_subfield_indexes = new ArrayList<Integer>();
                            ArrayList<InputField> listbox_items = target_subfield_list.getItems();
                            for (int i = 0; i < target_subfield_list.getItemCount(); i++) {
                                if (target_subfield_list.isItemSelected(i)) {
                                    target_subfields.add(listbox_items.get(i));
                                    if (!listbox_items.get(i).isSynonymField()) target_subfield_indexes.add(i);
                                }
                            }
                            target_field.setTargetSubFields(target_subfields);
                            target_field.setTargetSubFieldIndexes(target_subfield_indexes);
                            ExportPanel.this._export_type.setTargetFields(target_fields);
                            if (containsSynonymField(target_subfields)) {
                                if (!target_field.hasSynonymSubFields()) {
                                    int index = target_field.getIndexOnTable();
                                    insertSynonymSubFieldList(table, index, target_field.getTargetField(), target_field.getTargetSubFields());
                                    target_field.setHasSynonymSubFields(true);
                                }
                            } else {
                                if (target_field.hasSynonymSubFields()) {
                                    int index = target_field.getIndexOnTable();
                                    removeSynonymSubFieldList(table, index);
                                    target_field.setHasSynonymSubFields(false);
                                }
                            }
                        }
                    });
                    loadTargetSubFieldListBox(target_subfield_list, selected_target);
                    setSelectedTargetFieldIndexes(target_subfield_list, target_field);
                    table.setWidget(starting_row, column, target_subfield_label);
                    table.getCellFormatter().addStyleName(starting_row, column, "exportPanelFlexTableLabelCell");
                    column++;
                    table.setWidget(starting_row, column, target_subfield_list);
                    table.getCellFormatter().addStyleName(starting_row, column, "exportPanelFlexTableListBoxCell");
                    starting_row++;
                    column = 0;
                }
            }
        });
        loadInputModelsListBox(inputmodel_list);
        if (_export_type.getExportType().equalsIgnoreCase("ODT")) loadRecordFieldsListBox(record_field_list, true); else loadRecordFieldsListBox(record_field_list, false);
        loadIndexFieldListBox(index_field_list);
        loadIndexSubFieldListBox(index_subfield_list);
        loadTargetFieldListBox(target_field_list);
        this.add(infoLabel);
        int row_index = 0;
        table.setWidget(row_index, 0, inputmodels_label);
        table.getCellFormatter().addStyleName(row_index, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(row_index, 1, inputmodel_list);
        table.getCellFormatter().addStyleName(row_index, 1, "exportPanelFlexTableListBoxCell");
        row_index++;
        table.setWidget(row_index, 0, record_fields_label);
        table.getCellFormatter().addStyleName(row_index, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(row_index, 1, record_field_list);
        table.getCellFormatter().addStyleName(row_index, 1, "exportPanelFlexTableListBoxCell");
        row_index++;
        table.setWidget(row_index, 0, index_field_label);
        table.getCellFormatter().addStyleName(row_index, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(row_index, 1, index_field_list);
        table.getCellFormatter().addStyleName(row_index, 1, "exportPanelFlexTableListBoxCell");
        row_index++;
        table.setWidget(row_index, 0, index_subfield_label);
        table.getCellFormatter().addStyleName(row_index, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(row_index, 1, index_subfield_list);
        table.getCellFormatter().addStyleName(row_index, 1, "exportPanelFlexTableListBoxCell");
        row_index++;
        table.setWidget(row_index, 0, target_field_label);
        table.getCellFormatter().addStyleName(row_index, 0, "exportPanelFlexTableLabelCell");
        table.setWidget(row_index, 1, target_field_list);
        table.getCellFormatter().addStyleName(row_index, 1, "exportPanelFlexTableListBoxCell");
        container.add(table);
        this.add(container);
        this.setHeight("300px");
        if (index_field_list.getItemCount() == 0) {
            AlertBox.show(constants.fault_minor(), messages.export_no_rights2(_export_type.getExportType()), false, true);
            record_field_list.setEnabled(false);
            index_field_list.setEnabled(false);
            index_subfield_list.setEnabled(false);
            target_field_list.setEnabled(false);
        }
    }

    private void insertSynonymSubFieldList(FlexTable table, int before_row_index, InputField parent, final ArrayList<InputField> export_subfields) {
        final ExtendedListBox<InputField> synonym_subfield_listbox = new ExtendedListBox<InputField>(true);
        synonym_subfield_listbox.setWidth("300px");
        synonym_subfield_listbox.setHeight("100px");
        synonym_subfield_listbox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                InputModelSubfield synonym_field = (InputModelSubfield) getSynonymField(export_subfields);
                ArrayList<InputField> listbox_items = synonym_subfield_listbox.getItems();
                ArrayList<InputField> selected_subfieldsubfields = new ArrayList<InputField>();
                for (int i = 0; i < synonym_subfield_listbox.getItemCount(); i++) {
                    if (synonym_subfield_listbox.isItemSelected(i)) selected_subfieldsubfields.add(listbox_items.get(i));
                }
                synonym_field.setInputModelsubfieldsubfields(selected_subfieldsubfields);
            }
        });
        table.insertCell(before_row_index, 2);
        table.setWidget(before_row_index, 2, synonym_subfield_listbox);
        table.getCellFormatter().addStyleName(before_row_index, 2, "exportPanelFlexTableListBoxCell");
        loadSynonymSubFieldListBox(parent, synonym_subfield_listbox);
    }

    private InputField getSynonymField(ArrayList<InputField> subfields) {
        Iterator<InputField> iter = subfields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isSynonymField()) return field;
        }
        return null;
    }

    private void removeSynonymSubFieldList(FlexTable table, int row_index) {
        table.removeCell(row_index, 2);
    }

    private boolean containsSynonymField(ArrayList<InputField> fields) {
        Iterator<InputField> iter = fields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isSynonymField()) return true;
        }
        return false;
    }

    private void removeTargetAttribs(FlexTable table, int row) {
        int row_count = table.getRowCount() - 1;
        while (row_count >= row) {
            table.removeRow(row_count);
            row_count = table.getRowCount() - 1;
        }
    }

    private void clearSelections(boolean inputmodel_changed, FlexTable table) {
        if (inputmodel_changed) {
            ArrayList<InputField> record_fields = this._export_type.getRecordFields();
            record_fields.clear();
            this._export_type.setRecordFields(record_fields);
            ArrayList<Integer> record_field_indexes = this._export_type.getRecordFieldIndexes();
            record_field_indexes.clear();
            this._export_type.setRecordFieldIndexes(record_field_indexes);
        }
        if (_export_type.hasSynonymSubFields()) {
            removeSynonymSubFieldList(table, 3);
        }
        _export_type.setHasSynonymSubFields(false);
        ArrayList<TargetField> targets = _export_type.getTargetFields();
        Iterator<TargetField> iter = targets.iterator();
        while (iter.hasNext()) {
            TargetField target = iter.next();
            target.setHasSynonymSubFields(false);
            target.setIndexOnTable(-1);
        }
        int source_index = this._export_type.getSourceIndex();
        source_index = -1;
        this._export_type.setSourceIndex(source_index);
        int target_index = this._export_type.getTargetIndex();
        target_index = -1;
        this._export_type.setTargetIndex(target_index);
        ArrayList<InputField> index_subfields = this._export_type.getIndexSubFields();
        index_subfields.clear();
        this._export_type.setIndexSubFields(index_subfields);
        ArrayList<Integer> indexes = this._export_type.getIndexSubFieldIndexes();
        indexes.clear();
        this._export_type.setIndexSubFieldIndexes(indexes);
        ArrayList<TargetField> target_fields = this._export_type.getTargetFields();
        target_fields.clear();
        this._export_type.setTargetFields(target_fields);
        ArrayList<InputField> selected_targets = this._export_type.getSelectedTargets();
        selected_targets.clear();
        this._export_type.setSelectedTargets(selected_targets);
        removeTargetAttribs(table, 5);
    }

    private void loadInputModelsListBox(ExtendedListBox<InputModel> inputmodel_list) {
        ArrayList<InputModel> inputmodels = this._export_type.getInputModels();
        for (InputModel inputmodel : inputmodels) {
            inputmodel_list.addItem(inputmodel.getInputmodelname(), inputmodel.getInputmodelname(), inputmodel);
        }
        if (this._export_type.getInputModelIndex() == -1) {
            inputmodel_list.setSelectedIndex(0);
            this._export_type.setInputModelIndex(0);
        } else inputmodel_list.setSelectedIndex(this._export_type.getInputModelIndex());
        this._export_type.setInputModel(inputmodel_list.getSelectedItem());
    }

    private void loadRecordFieldsListBox(ExtendedListBox<InputField> record_field_list, boolean include_project_field) {
        record_field_list.clear();
        InputModel inputmodel = this._export_type.getInputModel();
        ArrayList<InputField> fields = inputmodel.getInputmodelfields();
        for (InputField inputfield : fields) {
            if (inputfield.isRecordAttribute() && inputfield.isExportable() && !inputfield.getFieldname().equalsIgnoreCase(inputfield.getProjectField())) {
                record_field_list.addItem(inputfield.getFieldname(), inputfield.getFieldname(), inputfield);
            }
        }
        if (include_project_field) {
            InputField project_field = inputmodel.getProjectField();
            record_field_list.addItem(project_field.getFieldname(), project_field.getFieldname(), project_field);
        }
        if (this._export_type.getRecordFieldIndexes().size() > 0) {
            ArrayList<Integer> indexes = this._export_type.getRecordFieldIndexes();
            for (int index : indexes) {
                record_field_list.setItemSelected(index, true);
            }
        }
    }

    private void loadIndexFieldListBox(ExtendedListBox<InputField> index_field_list) {
        index_field_list.clear();
        InputModel inputmodel = this._export_type.getInputModel();
        ArrayList<InputField> inputmodel_fields = inputmodel.getIndexFields();
        for (InputField input_field : inputmodel_fields) {
            if (input_field.isExportable()) index_field_list.addItem(input_field.getFieldname(), input_field.getFieldname(), input_field);
        }
        if (this._export_type.getIndexFieldIndex() == -1) {
            index_field_list.setSelectedIndex(0);
            this._export_type.setIndexFieldIndex(0);
        } else index_field_list.setSelectedIndex(this._export_type.getIndexFieldIndex());
        this._export_type.setIndexField(index_field_list.getSelectedItem());
    }

    private void loadIndexSubFieldListBox(ExtendedListBox<InputField> index_subfield_list) {
        index_subfield_list.clear();
        InputField index_field = this._export_type.getIndexField();
        if (index_field == null) return;
        ArrayList<InputField> index_subfields = index_field.getInputmodelsubfields();
        for (InputField subfield : index_subfields) {
            if (subfield.isExportable()) index_subfield_list.addItem(subfield.getFieldname(), subfield.getFieldname(), subfield);
            if (subfield.isSynonymField()) {
                HashMap<String, ArrayList<InputField>> subfieldsubfield_map = _export_type.getSubFieldSubFieldsMap();
                if (!subfieldsubfield_map.containsKey(_export_type.getInputModel().getInputmodelname() + index_field.getFieldname())) {
                    loadSubFieldSubFields(index_field, subfield);
                    InputModelSubfield synonym_field = (InputModelSubfield) subfield;
                    synonym_field.setInputModelsubfieldsubfields(null);
                }
            }
        }
        if (this._export_type.getIndexSubFieldIndexes().size() > 0) {
            ArrayList<Integer> indexes = this._export_type.getIndexSubFieldIndexes();
            for (int index : indexes) {
                index_subfield_list.setItemSelected(index, true);
            }
        }
    }

    private void loadTargetFieldListBox(ExtendedListBox<InputField> target_field_list) {
        target_field_list.clear();
        InputField index_field = this._export_type.getIndexField();
        if (index_field == null) return;
        ArrayList<InputField> target_fields = this._export_type.getInputModel().getIndexFields();
        for (InputField inputfield : target_fields) {
            if (!inputfield.getFieldname().equalsIgnoreCase(index_field.getFieldname()) && inputfield.isExportable()) {
                target_field_list.addItem(inputfield.getFieldname(), inputfield.getFieldname(), inputfield);
            }
        }
    }

    private void loadTargetSubFieldListBox(ExtendedListBox<InputField> target_subfield_list, InputField target_field) {
        target_subfield_list.clear();
        ArrayList<InputField> target_subfields = target_field.getInputmodelsubfields();
        for (InputField target_subfield : target_subfields) {
            if (target_subfield.isExportable()) target_subfield_list.addItem(target_subfield.getFieldname(), target_subfield.getFieldname(), target_subfield);
            if (target_subfield.isSynonymField()) {
                HashMap<String, ArrayList<InputField>> subfieldsubfields_map = _export_type.getSubFieldSubFieldsMap();
                if (!subfieldsubfields_map.containsKey(_export_type.getInputModel().getInputmodelname() + target_field.getFieldname())) {
                    loadSubFieldSubFields(target_field, target_subfield);
                    InputModelSubfield synonym_field = (InputModelSubfield) target_subfield;
                    synonym_field.setInputModelsubfieldsubfields(null);
                }
            }
        }
    }

    private void loadSynonymSubFieldListBox(InputField parent, ExtendedListBox<InputField> synonym_subfield_listbox) {
        HashMap<String, ArrayList<InputField>> subfieldsubfield_map = _export_type.getSubFieldSubFieldsMap();
        ArrayList<InputField> subfieldsubfields = subfieldsubfield_map.get(_export_type.getInputModel().getInputmodelname() + parent.getFieldname());
        Iterator<InputField> iter = subfieldsubfields.iterator();
        while (iter.hasNext()) {
            InputField subfieldsubfield = iter.next();
            if (subfieldsubfield.isExportable()) synonym_subfield_listbox.addItem(subfieldsubfield.getFieldname(), subfieldsubfield.getFieldname(), subfieldsubfield);
        }
    }

    /**
	 * Set the available TermSubAttributes that are under a specified index field.
	 * @param index
	 * @param subfield
	 */
    private void loadSubFieldSubFields(InputField index, InputField subfield) {
        ArrayList<InputField> subfieldsubfields = subfield.getInputInputModelSubfieldSubFields();
        _export_type.setSubFieldSubFieldsMap(_export_type.getInputModel().getInputmodelname() + index.getFieldname(), subfieldsubfields);
    }

    private void checkTargetFieldList(ArrayList<InputField> selected_targets, ArrayList<TargetField> target_fields) {
        Iterator<TargetField> iter = target_fields.iterator();
        while (iter.hasNext()) {
            TargetField target_field = iter.next();
            if (!findSelectedTargetField(selected_targets, target_field.getTargetField())) iter.remove();
        }
        for (InputField input_field : selected_targets) {
            if (!isTargetFieldSelected(input_field, target_fields)) {
                TargetField target_field = new TargetField();
                target_field.setTargetField(input_field);
                target_fields.add(target_field);
            }
        }
    }

    private boolean findSelectedTargetField(ArrayList<InputField> selected_targets, InputField search_target) {
        for (InputField selected_target : selected_targets) {
            if (selected_target.getFieldname().equalsIgnoreCase(search_target.getFieldname())) return true;
        }
        return false;
    }

    private boolean isTargetFieldSelected(InputField search_target, ArrayList<TargetField> target_fields) {
        for (TargetField target_field : target_fields) {
            if (search_target.getFieldname().equalsIgnoreCase(target_field.getTargetField().getFieldname())) return true;
        }
        return false;
    }

    private TargetField findTargetField(InputField search_target, ArrayList<TargetField> target_fields) {
        for (TargetField target_field : target_fields) {
            InputField target = target_field.getTargetField();
            if (search_target.getFieldname().equalsIgnoreCase(target.getFieldname())) return target_field;
        }
        return null;
    }

    private void setSelectedTargetFieldIndexes(ExtendedListBox<InputField> target_subfield_list, TargetField target_field) {
        ArrayList<Integer> target_subfield_indexes = target_field.getTargetSubFieldIndexes();
        for (int index : target_subfield_indexes) {
            target_subfield_list.setItemSelected(index, true);
        }
    }

    private void Layout3() {
        Label infoLabel = new Label(this._export_type.getInformation());
        infoLabel.addStyleName("plainLabelText");
        this.add(infoLabel);
        ArrayList<InputField> record_fields = getRecordFields();
        ArrayList<InputField> index_fields = getIndexFields();
        ArrayList<InputField> index_attr_fields = getIndexAttributes();
        ArrayList<InputField> index_attr_sub_fields = getAttrSubFields();
        Label lbl_recordField = new Label(constants.export_record_fields(), false);
        lbl_recordField.addStyleName("plainLabelText");
        ScrollPanel record_fields_table = this.contructTable(record_fields, RECORDS_TABLE, index_fields);
        Label lbl_index_fields = new Label(constants.export_index_fields(), false);
        lbl_index_fields.addStyleName("plainLabelText");
        ScrollPanel index_fields_table = this.contructTable(index_fields, INDEXES_TABLE, index_fields);
        Label lbl_index_attr_fields = new Label(constants.export_index_attrib_fields(), false);
        lbl_index_attr_fields.addStyleName("plainLabelText");
        ScrollPanel index_attr_fields_table = this.contructTable(index_attr_fields, INDEX_ATTRS_TABLE, index_fields);
        Label lbl_index_attr_subfields = new Label(constants.export_index_attrib_subfield(), false);
        lbl_index_attr_subfields.addStyleName("plainLabelText");
        ScrollPanel index_attr_subfields_table = this.contructTable(index_attr_sub_fields, INDEX_ATTR_SUBFIELDS_ATTRS, index_fields);
        ExtendedTabLayoutPanel tabPanel = new ExtendedTabLayoutPanel(35d, Unit.PX);
        tabPanel.setWidth("100%");
        tabPanel.setHeight("250px");
        tabPanel.add(index_attr_subfields_table, lbl_index_attr_subfields);
        tabPanel.insert(index_attr_fields_table, lbl_index_attr_fields, 0);
        tabPanel.insert(index_fields_table, lbl_index_fields, 0);
        tabPanel.insert(record_fields_table, lbl_recordField, 0);
        this.add(tabPanel);
        tabPanel.selectTab(3);
        tabPanel.selectTab(2);
        tabPanel.selectTab(1);
        tabPanel.selectTab(0);
        this.setHeight("300px");
    }

    private ArrayList<InputField> getRecordFields() {
        ArrayList<InputField> fields = this._export_type.getFields();
        ArrayList<InputField> record_fields = new ArrayList<InputField>();
        Iterator<InputField> iter = fields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isRecordAttribute()) record_fields.add(field);
        }
        return record_fields;
    }

    private ArrayList<InputField> getIndexFields() {
        ArrayList<InputField> fields = this._export_type.getFields();
        ArrayList<InputField> index_fields = new ArrayList<InputField>();
        Iterator<InputField> iter = fields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isIndexField()) index_fields.add(field);
        }
        return index_fields;
    }

    private ArrayList<InputField> getIndexAttributes() {
        ArrayList<InputField> fields = this._export_type.getFields();
        ArrayList<InputField> index_attr_fields = new ArrayList<InputField>();
        Iterator<InputField> iter = fields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isFieldAttribute()) index_attr_fields.add(field);
        }
        return index_attr_fields;
    }

    private ArrayList<InputField> getAttrSubFields() {
        ArrayList<InputField> fields = this._export_type.getFields();
        ArrayList<InputField> index_subattr_fields = new ArrayList<InputField>();
        Iterator<InputField> iter = fields.iterator();
        while (iter.hasNext()) {
            InputField field = iter.next();
            if (field.isFieldSubAttribute()) index_subattr_fields.add(field);
        }
        return index_subattr_fields;
    }

    private ScrollPanel contructTable(ArrayList<InputField> fields, int code, ArrayList<InputField> index_fields) {
        ScrollPanel scroller = new ScrollPanel();
        scroller.setWidth("98%");
        scroller.setHeight("200px");
        FlexTable table = new FlexTable();
        table.setWidth("98%");
        table.setHeight("200px");
        Iterator<InputField> iter = fields.iterator();
        int row = 0;
        int col = 0;
        while (iter.hasNext()) {
            InputField field = iter.next();
            Label lbl_field = new Label(constants.export_txt_name());
            lbl_field.addStyleName("plainLabelText");
            table.setWidget(row, col, lbl_field);
            col++;
            TextBox txt_field = new TextBox();
            txt_field.addStyleName("textboxSizedDisabled");
            txt_field.setText(field.getFieldname());
            txt_field.setEnabled(false);
            table.setWidget(row, col, txt_field);
            col++;
            Button btn_copy = new Button();
            btn_copy.setText(constants.export_btn_copy());
            btn_copy.addStyleName("buttonSizedEnabled");
            table.setWidget(row, col, btn_copy);
            col++;
            Label lbl_field_value = new Label(constants.export_txt_value());
            lbl_field_value.addStyleName("plainLabelText");
            table.setWidget(row, col, lbl_field_value);
            col++;
            TextBox txt_field_value = new TextBox();
            if (field.isSortIndex() || field.isSynonymField()) {
                txt_field_value.addStyleName("textboxSizedDisabled");
                txt_field_value.setText(field.getFieldname());
                txt_field_value.setEnabled(false);
                btn_copy.addStyleName("buttonSizedDisabled");
                btn_copy.setEnabled(false);
                setEntity(code, row, field.getFieldname(), field.getFieldname(), txt_field_value);
            } else {
                txt_field_value.addStyleName("textboxSizedEnabled");
                txt_field_value.addChangeHandler(new TextBoxChangeHandler(field.getFieldname(), txt_field_value, row, code, index_fields));
                btn_copy.addClickHandler(new ButtonCopyClick(field.getFieldname(), txt_field, txt_field_value, row, code));
            }
            table.setWidget(row, col, txt_field_value);
            RowFormatter formatter = table.getRowFormatter();
            formatter.setStyleName(row, "exportPanelFlexTableRow");
            row++;
            col = 0;
        }
        if (code == RECORDS_TABLE) {
            HashMap<Integer, FieldTemplateEntity> records = _export_type.getRecordFieldsTable();
            if (records.size() > 0) setTextBoxText(records, table);
        } else if (code == INDEXES_TABLE) {
            HashMap<Integer, FieldTemplateEntity> indexes = _export_type.getIndexFieldsTable();
            if (indexes.size() > 0) setTextBoxText(indexes, table);
        } else if (code == INDEX_ATTRS_TABLE) {
            HashMap<Integer, FieldTemplateEntity> index_attribs = _export_type.getIndexAttribsFieldsTable();
            if (index_attribs.size() > 0) setTextBoxText(index_attribs, table);
        } else if (code == INDEX_ATTR_SUBFIELDS_ATTRS) {
            HashMap<Integer, FieldTemplateEntity> index_attribs_sub_fields = _export_type.getIndexAttribsSubFieldsTable();
            if (index_attribs_sub_fields.size() > 0) setTextBoxText(index_attribs_sub_fields, table);
        }
        scroller.add(table);
        return scroller;
    }

    private void setTextBoxText(HashMap<Integer, FieldTemplateEntity> table, FlexTable flex_table) {
        int column = 4;
        for (int table_row : table.keySet()) {
            FieldTemplateEntity field_template_entity = table.get(table_row);
            TextBox txt_value = (TextBox) flex_table.getWidget(table_row, column);
            txt_value.setText(field_template_entity.getFieldValue());
        }
    }

    private class SwitchHandler implements ClickHandler {

        ExtendedListBox<InputField> _source;

        ExtendedListBox<InputField> _target;

        public SwitchHandler(ExtendedListBox<InputField> sourceLB, ExtendedListBox<InputField> targetLB) {
            this._source = sourceLB;
            this._target = targetLB;
        }

        @Override
        public void onClick(ClickEvent event) {
            int indx1 = this._source.getSelectedIndex();
            int indx2 = this._target.getSelectedIndex();
            this._source.setSelectedIndex(indx2);
            this._target.setSelectedIndex(indx1);
            ExportPanel.this._export_type.setSourceIndex(indx2);
            ExportPanel.this._export_type.setSourceField(this._source.getSelectedItem());
            ExportPanel.this._export_type.setTargetIndex(indx1);
            ExportPanel.this._export_type.setTargetField(this._target.getSelectedItem());
        }
    }

    private class TextBoxChangeHandler implements ChangeHandler {

        TextBox _txt_current = null;

        String _field_name = null;

        int _row = -1;

        int _code = -1;

        ArrayList<InputField> _index_fields = null;

        public TextBoxChangeHandler(String field_name, TextBox txt_current, int row, int code, ArrayList<InputField> index_fields) {
            this._field_name = field_name;
            this._txt_current = txt_current;
            this._row = row;
            this._code = code;
            this._index_fields = index_fields;
        }

        @Override
        public void onChange(ChangeEvent event) {
            String current_text = this._txt_current.getText();
            if (!hasIndexField(_index_fields, current_text, _code)) setEntity(_code, _row, _field_name, current_text, _txt_current); else {
                _txt_current.setText("");
                AlertBox.show(constants.export_index_field_found(), constants.export_index_field_found_mes(), false, true);
            }
        }
    }

    private class ButtonCopyClick implements ClickHandler {

        TextBox _txt_from = null;

        TextBox _txt_to = null;

        String _field_name = null;

        int _row = -1;

        int _code = -1;

        public ButtonCopyClick(String field_name, TextBox txt_from, TextBox txt_to, int row, int code) {
            this._field_name = field_name;
            this._txt_from = txt_from;
            this._txt_to = txt_to;
            this._row = row;
            this._code = code;
        }

        @Override
        public void onClick(ClickEvent event) {
            String current_text = this._txt_from.getText();
            this._txt_to.setText(current_text);
            setEntity(_code, _row, _field_name, current_text, _txt_to);
        }
    }

    private void setEntity(int code, int row, String field_name, String field_value, TextBox current) {
        if (code == RECORDS_TABLE) {
            HashMap<Integer, FieldTemplateEntity> records = _export_type.getRecordFieldsTable();
            if (!field_value.isEmpty()) {
                if (!hasDuplicates(records, field_value, row)) {
                    FieldTemplateEntity field_template_entity = contructFieldTemplateEntity(field_name, field_value);
                    records.put(row, field_template_entity);
                } else {
                    current.setText("");
                    AlertBox.show(constants.export_duplicate_field(), constants.export_duplicate_field_mes(), false, true);
                }
            } else records.remove(row);
            _export_type.setRecordFieldsTable(records);
        } else if (code == INDEXES_TABLE) {
            HashMap<Integer, FieldTemplateEntity> indexes = _export_type.getIndexFieldsTable();
            if (!field_value.isEmpty()) {
                if (!hasDuplicates(indexes, field_value, row)) {
                    FieldTemplateEntity field_template_entity = contructFieldTemplateEntity(field_name, field_value);
                    indexes.put(row, field_template_entity);
                } else {
                    current.setText("");
                    AlertBox.show(constants.export_duplicate_field(), constants.export_duplicate_field_mes(), false, true);
                }
            } else indexes.remove(row);
            _export_type.setIndexFieldsTable(indexes);
        } else if (code == INDEX_ATTRS_TABLE) {
            HashMap<Integer, FieldTemplateEntity> index_attrs = _export_type.getIndexAttribsFieldsTable();
            if (!field_value.isEmpty()) {
                if (!hasDuplicates(index_attrs, field_value, row)) {
                    FieldTemplateEntity field_template_entity = contructFieldTemplateEntity(field_name, field_value);
                    index_attrs.put(row, field_template_entity);
                } else {
                    current.setText("");
                    AlertBox.show(constants.export_duplicate_field(), constants.export_duplicate_field_mes(), false, true);
                }
            } else index_attrs.remove(row);
            _export_type.setIndexAttribsFieldsTable(index_attrs);
        } else if (code == INDEX_ATTR_SUBFIELDS_ATTRS) {
            HashMap<Integer, FieldTemplateEntity> index_attrs_subfields = _export_type.getIndexAttribsSubFieldsTable();
            if (!field_value.isEmpty()) {
                if (!hasDuplicates(index_attrs_subfields, field_value, row)) {
                    FieldTemplateEntity field_template_entity = contructFieldTemplateEntity(field_name, field_value);
                    index_attrs_subfields.put(row, field_template_entity);
                } else {
                    current.setText("");
                    AlertBox.show(constants.export_duplicate_field(), constants.export_duplicate_field_mes(), false, true);
                }
            } else index_attrs_subfields.remove(row);
        }
    }

    private FieldTemplateEntity contructFieldTemplateEntity(String field_name, String field_value) {
        FieldTemplateEntity field_template_entity = new FieldTemplateEntity();
        field_template_entity.setFieldName(field_name);
        field_template_entity.setFieldValue(field_value);
        return field_template_entity;
    }

    private boolean hasIndexField(ArrayList<InputField> _index_fields, String current_text, int code) {
        if (current_text.isEmpty()) return false;
        if (code == INDEXES_TABLE) return false;
        Iterator<InputField> iter = _index_fields.iterator();
        while (iter.hasNext()) {
            InputField index = iter.next();
            if (index.getFieldname().equalsIgnoreCase(current_text.trim())) return true;
        }
        return false;
    }

    private boolean hasDuplicates(HashMap<Integer, FieldTemplateEntity> list, String current_text, int selected_row) {
        for (int row : list.keySet()) {
            if (selected_row == row) continue;
            FieldTemplateEntity field_template_entity = list.get(row);
            if (field_template_entity.getFieldValue().equalsIgnoreCase(current_text.trim())) return true;
        }
        return false;
    }
}
