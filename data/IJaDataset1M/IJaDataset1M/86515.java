package com.matrixbi.adans.client.report.widget;

import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.ListViewSelectionModel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.matrixbi.adans.client.messages.MessageFactory;
import com.matrixbi.adans.client.report.mvc.ReportController;
import com.matrixbi.adans.client.report.mvc.ReportEvents;
import com.matrixbi.adans.client.report.mvc.ReportView;
import com.matrixbi.adans.client.service.ServiceFactory;
import com.matrixbi.adans.ocore.client.olap.Member;

public class MeasureSelector extends ReportView {

    private Window selector;

    private FormPanel form;

    private ListView<Member> measures;

    private ListStore<Member> measureStore;

    private Button okButton;

    private String var1;

    @SuppressWarnings("unused")
    private String var2;

    private String varToSelect;

    public Window getSelector() {
        if (selector == null) {
            selector = FormControls.getBasicWindowSelector(MessageFactory.getInstance().metricselector());
            selector.addListener(Events.Resize, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    int h = getSelector().getHeight() - 120;
                    getMeasuresList().setHeight(h);
                }
            });
            selector.add(getForm(), new FitData(FormControls.getFormMargins()));
            selector.hide();
        }
        return selector;
    }

    public FormPanel getForm() {
        if (form == null) {
            form = FormControls.getBasicFormPanel();
            AdapterField adapter = new AdapterField(getMeasuresList());
            adapter.setFieldLabel(MessageFactory.getInstance().selectmetric());
            form.add(adapter, FormControls.getBasicFormData());
            form.addButton(getOkButton());
        }
        return form;
    }

    private Button getOkButton() {
        if (okButton == null) {
            okButton = FormControls.getOkButton();
            okButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {

                @Override
                public void handleEvent(ButtonEvent be) {
                    getSelector().hide();
                    if (isVar1()) {
                        getReport().setVariable1(getMeasuresList().getSelectionModel().getSelectedItem().getUniqueName());
                    } else {
                        getReport().setVariable2(getMeasuresList().getSelectionModel().getSelectedItem().getUniqueName());
                    }
                    getController().getDispatcher().forwardEvent(ReportEvents.MeasureChange);
                }
            });
        }
        return okButton;
    }

    public ListStore<Member> getMeasureStore() {
        if (measureStore == null) {
            measureStore = new ListStore<Member>();
        }
        return measureStore;
    }

    public ListView<Member> getMeasuresList() {
        if (measures == null) {
            measures = new ListView<Member>(getMeasureStore());
            measures.setDisplayProperty("name");
            ListViewSelectionModel<Member> simpleSelectionModel = new ListViewSelectionModel<Member>();
            simpleSelectionModel.setSelectionMode(SelectionMode.SINGLE);
            measures.setSelectionModel(simpleSelectionModel);
        }
        return measures;
    }

    public MeasureSelector() {
        super();
    }

    public MeasureSelector(ReportController controller) {
        super(controller);
    }

    private void loadMeasures() {
        getMeasuresList().mask();
        ServiceFactory.getOlapService().listMeasures(getReport().getConex(), getReport().getCube(), new AsyncCallback<List<Member>>() {

            @Override
            public void onSuccess(List<Member> result) {
                getMeasureStore().add(result);
                getController().getDispatcher().forwardEvent(ReportEvents.MeasuresLoaded);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    private void selectMeasure() {
        for (Member m : getMeasureStore().getModels()) {
            if (m.getUniqueName().toString().equals(varToSelect)) {
                List<Member> selection = new ArrayList<Member>();
                selection.add(m);
                getMeasuresList().getSelectionModel().setSelection(selection);
            }
        }
    }

    @Override
    public Widget asWidget() {
        return getSelector();
    }

    @Override
    protected void handleEvent(AppEvent event) {
        EventType type = event.getType();
        if (type == ReportEvents.Init) {
        }
        if (type == ReportEvents.MeasuresLoaded) {
            getMeasuresList().unmask();
        }
        if (event.getType() == ReportEvents.ReportLoaded) {
            loadMeasures();
        }
        if (event.getType() == ReportEvents.ShowMeasureSelector) {
            var1 = (String) event.getData("var1");
            var2 = (String) event.getData("var2");
            varToSelect = (String) event.getData("varToSelect");
            selectMeasure();
            getSelector().show();
        }
    }

    private Boolean isVar1() {
        return var1.equalsIgnoreCase(varToSelect);
    }
}
