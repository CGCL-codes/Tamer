package ru.aslanov.schedule.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.core.Function;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import ru.aslanov.schedule.client.ds.SchedulesDS;
import ru.aslanov.schedule.client.i18n.MyConstants;
import ru.aslanov.schedule.client.util.GUIUtil;
import ru.aslanov.schedule.client.util.JavaScriptMethodCallback;
import ru.aslanov.schedule.client.util.JavaScriptMethodHelper;
import ru.aslanov.schedule.client.util.MyAsyncCallback;
import static ru.aslanov.schedule.client.i18n.I18nUtil.cnt;

/**
 * Created by IntelliJ IDEA.
 * Created: Feb 11, 2010 5:09:37 PM
 *
 * @author Sergey Aslanov
 */
public class SchedulesListPanel extends VLayout {

    public SchedulesListPanel(final boolean isAdmin, final ProcessRecordHandler processRecordHandler) {
        super(5);
        VLayout schedulesPane = this;
        schedulesPane.setWidth100();
        schedulesPane.setHeight100();
        final ListGrid listGrid = new ListGrid() {

            @Override
            protected boolean canEditCell(int row, int col) {
                boolean res = super.canEditCell(row, col);
                if (isAdmin) return res; else {
                    final ListGridRecord record = this.getRecord(row);
                    if (record == null) {
                        return res;
                    } else if (record.getAttributeAsBoolean("isScheduleAdmin")) {
                        return res;
                    }
                }
                return false;
            }
        };
        listGrid.setDataSource(SchedulesDS.getInstance());
        listGrid.setAutoFetchData(true);
        listGrid.setShowAllRecords(true);
        listGrid.setCanEdit(false);
        listGrid.setUseAllDataSourceFields(true);
        listGrid.setSelectionType(SelectionStyle.SINGLE);
        HStack toolStrip = new HStack(5);
        toolStrip.setHeight(10);
        final MyConstants cnt = cnt();
        Button addButton = new Button(cnt.newSchedule());
        addButton.setAutoFit(true);
        addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent clickEvent) {
                SC.askforValue(cnt.newSchedule(), cnt().nameField(), new ValueCallback() {

                    @Override
                    public void execute(String value) {
                        if (value != null && value.trim().length() > 0) {
                            Record record = new Record();
                            record.setAttribute("name", value);
                            listGrid.addData(record, new DSCallback() {

                                @Override
                                public void execute(DSResponse response, Object rawData, DSRequest request) {
                                    final Record res = response.getData()[0];
                                    processRecordHandler.process(res);
                                }
                            });
                        }
                    }
                });
            }
        });
        toolStrip.addMember(addButton);
        final Button deleteButton;
        deleteButton = new Button(cnt.removeButton());
        deleteButton.setAutoFit(true);
        GUIUtil.addRemoveHandler(listGrid, deleteButton, null);
        toolStrip.addMember(deleteButton);
        deleteButton.setVisible(false);
        final Button exportButton = new Button(cnt.exportToXml());
        exportButton.setAutoFit(true);
        exportButton.setVisible(false);
        exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent clickEvent) {
                final ListGridRecord selectedRecord = listGrid.getSelectedRecord();
                if (selectedRecord == null) {
                    SC.say(cnt.selectSchedule());
                } else {
                    DOM.setElementAttribute(RootPanel.get("__download").getElement(), "src", "/download-schedule?s=" + selectedRecord.getAttribute("encodedKey"));
                }
            }
        });
        toolStrip.addMember(exportButton);
        final Button getXmlButton = new Button(cnt.getXmlButton());
        getXmlButton.setAutoFit(true);
        getXmlButton.setVisible(false);
        getXmlButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final ListGridRecord record = listGrid.getSelectedRecord();
                final String key = record.getAttribute("encodedKey");
                getXmlAction(key, record.getAttribute("defaultInputLanguage"), getXmlButton);
            }
        });
        toolStrip.addMember(getXmlButton);
        if (isAdmin) {
            Button importButton = new Button(cnt.importButton());
            importButton.setAutoFit(true);
            importButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent clickEvent) {
                    uploadSchedule(listGrid);
                }
            });
            toolStrip.addMember(importButton);
        }
        schedulesPane.addMember(toolStrip);
        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                final boolean selected = selectionEvent.getState();
                final Record record = selectionEvent.getRecord();
                boolean isScheduleAdmin = record.getAttributeAsBoolean("isScheduleAdmin");
                if (deleteButton != null) {
                    deleteButton.setVisible(selected && isScheduleAdmin);
                }
                exportButton.setVisible(selected && isScheduleAdmin);
                getXmlButton.setVisible(selected && isScheduleAdmin);
            }
        });
        listGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

            @Override
            public void onRecordDoubleClick(RecordDoubleClickEvent recordDoubleClickEvent) {
                processRecordHandler.process(recordDoubleClickEvent.getRecord());
            }
        });
        schedulesPane.addMember(listGrid);
    }

    public static void getXmlAction(String scheduleKey, String inputLang, final Button button) {
        ScheduleServices.App.getInstance().getXml(scheduleKey, inputLang, new MyAsyncCallback<String>(button) {

            @Override
            public void onSuccess(final String result) {
                button.enable();
                final com.smartgwt.client.widgets.Window window = new com.smartgwt.client.widgets.Window();
                window.setTitle(cnt().getXmlButton());
                window.setIsModal(true);
                window.setWidth(640);
                window.setHeight(480);
                window.setAlign(Alignment.CENTER);
                window.setAlign(VerticalAlignment.CENTER);
                DynamicForm dynamicForm = new DynamicForm();
                dynamicForm.setWidth100();
                dynamicForm.setHeight100();
                dynamicForm.setNumCols(1);
                final TextAreaItem content = new TextAreaItem();
                dynamicForm.setFields(content);
                content.setShowTitle(false);
                content.setWidth("*");
                content.setHeight("*");
                window.addItem(dynamicForm);
                window.centerInPage();
                window.shouldDismissOnEscape();
                window.doOnRender(new Function() {

                    @Override
                    public void execute() {
                        content.setValue(result);
                        content.selectValue();
                        content.setSelectOnFocus(Boolean.TRUE);
                        content.setAttribute("readonly", "readonly");
                    }
                });
                window.draw();
            }
        });
    }

    private void uploadSchedule(final ListGrid listGrid) {
        MyConstants cnt = cnt();
        final com.smartgwt.client.widgets.Window window = new com.smartgwt.client.widgets.Window();
        window.setTitle(cnt.importSchedule());
        window.setIsModal(true);
        window.setWidth(400);
        window.setHeight(100);
        window.setAlign(Alignment.CENTER);
        window.setAlign(VerticalAlignment.CENTER);
        final DynamicForm uploadForm = new DynamicForm();
        uploadForm.setWidth100();
        uploadForm.setHeight100();
        uploadForm.setTarget("__download");
        uploadForm.setEncoding(Encoding.MULTIPART);
        UploadItem uploadItem = new UploadItem("file", cnt.xmlFile());
        uploadItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(ChangeEvent changeEvent) {
                String callbackName = JavaScriptMethodHelper.registerCallbackFunction(new JavaScriptMethodCallback() {

                    @Override
                    public void execute(JavaScriptObject obj) {
                        boolean success = JSOHelper.getAttributeAsBoolean(obj, "success");
                        String dsc = JSOHelper.getAttribute(obj, "dsc");
                        String scheduleKey = JSOHelper.getAttribute(obj, "scheduleKey");
                        if (success) {
                            window.hide();
                            final Record record = new Record();
                            record.setAttribute("encodedKey", scheduleKey);
                            listGrid.updateData(record);
                        } else {
                            SC.warn(cnt().uploadError() + "\n" + dsc);
                        }
                    }
                });
                uploadForm.setAction("/upload-schedule?callback=" + callbackName);
                uploadForm.submitForm();
            }
        });
        uploadForm.setItems(uploadItem);
        window.addItem(uploadForm);
        window.centerInPage();
        window.show();
    }

    public static interface ProcessRecordHandler {

        void process(Record record);
    }
}
