package com.matrixbi.adans.client.report.widget;

import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.matrixbi.adans.client.messages.MessageFactory;
import com.matrixbi.adans.client.report.mvc.ReportEvents;
import com.matrixbi.adans.client.report.mvc.ReportView;
import com.matrixbi.adans.client.service.ServiceFactory;
import com.matrixbi.adans.ocore.client.olap.Dimension;
import com.matrixbi.adans.ocore.client.olap.Hierarchy;
import com.matrixbi.adans.ocore.client.olap.Level;
import com.matrixbi.adans.ocore.client.olap.Member;

public class DimensionSelector extends ReportView {

    private Window selector;

    private FormPanel form;

    private FieldSet memberSet;

    private CheckBox longname;

    private ComboBox<Dimension> dimension;

    private ListStore<Dimension> dimensionStore;

    private List<Dimension> dimensionCache;

    private ComboBox<Hierarchy> hierarchy;

    private ListStore<Hierarchy> hierarchyStore;

    private ComboBox<Level> level;

    private ListStore<Level> levelStore;

    private ListView<Member> members;

    private ListStore<Member> memberStore;

    private Button okButton;

    private String currentDimension = "";

    private String currentHierarchy = "";

    private String currentLevel = "";

    private List<String> currentMembers = new ArrayList<String>();

    private final int collapseHeight = 185;

    private int expandedHeight = 450;

    public DimensionSelector() {
        super();
    }

    public String getCurrentDimension() {
        return currentDimension;
    }

    public void setCurrentDimension(String currentDimension) {
        this.currentDimension = currentDimension;
    }

    public String getCurrentHierarchy() {
        return currentHierarchy;
    }

    public void setCurrentHierarchy(String currentHierarchy) {
        this.currentHierarchy = currentHierarchy;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<Dimension> getDimensionCache() {
        return dimensionCache;
    }

    public void setDimensionCache(List<Dimension> dimensionCache) {
        this.dimensionCache = dimensionCache;
    }

    public Window getSelector() {
        if (selector == null) {
            selector = FormControls.getBasicWindowSelector(MessageFactory.getInstance().dimensionselector());
            selector.setWidth(300);
            selector.setMinWidth(280);
            selector.addListener(Events.Resize, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    if (getMemberSet().isExpanded()) {
                        getSelector().setMinHeight(expandedHeight - 50);
                        expandedHeight = getSelector().getHeight();
                        int h = getSelector().getHeight() - 170;
                        getMemberSet().setHeight(h);
                        getMembers().setHeight(h - 50);
                    } else {
                        getSelector().setMinHeight(collapseHeight - 2);
                        if (collapseHeight != getSelector().getHeight()) {
                            getSelector().setHeight(collapseHeight);
                        }
                    }
                    getSelector().repaint();
                }
            });
            selector.add(getForm(), new FitData(FormControls.getFormMargins()));
            selector.hide();
        }
        return selector;
    }

    private FieldSet getMemberSet() {
        if (memberSet == null) {
            memberSet = new FieldSet();
            memberSet.setHeading(MessageFactory.getInstance().selectLevelMebers());
            memberSet.setCheckboxToggle(true);
            memberSet.setExpanded(false);
            memberSet.setHeight(200);
            memberSet.add(getMembers(), FormControls.getBasicFormData());
            memberSet.add(getLongname(), FormControls.getBasicFormData());
            memberSet.addListener(Events.Expand, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    getSelector().setMinHeight(expandedHeight - 50);
                    getSelector().setHeight(expandedHeight);
                    int l = getSelector().getAbsoluteLeft();
                    getSelector().center();
                    getSelector().setPosition(l, getSelector().getAbsoluteTop());
                }
            });
            memberSet.addListener(Events.Collapse, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    currentMembers.clear();
                    getMembers().getSelectionModel().deselectAll();
                    getSelector().setMinHeight(collapseHeight - 2);
                    getSelector().setHeight(collapseHeight);
                }
            });
        }
        return memberSet;
    }

    private CheckBox getLongname() {
        if (longname == null) {
            longname = new CheckBox();
            longname.setBoxLabel(MessageFactory.getInstance().longname());
            longname.addListener(Events.OnClick, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    getMembers().refresh();
                }
            });
        }
        return longname;
    }

    public FormPanel getForm() {
        if (form == null) {
            form = FormControls.getBasicFormPanel();
            form.setLabelAlign(LabelAlign.LEFT);
            FormData f = FormControls.getBasicFormData();
            form.add(getDimension(), f);
            form.add(getHierarchy(), f);
            form.add(getLevel(), f);
            form.add(getMemberSet(), f);
            form.addButton(getOkButton());
        }
        return form;
    }

    protected void initCurrentValues() {
        setCurrentDimension(getReport().getDimension().getDimension());
        setCurrentHierarchy(getReport().getDimension().getHierarchy());
        setCurrentLevel(getReport().getDimension().getLevel());
        currentMembers.clear();
        if (getReport().getDimension().getMembers() != null) {
            for (String m : getReport().getDimension().getMembers()) {
                currentMembers.add(m);
            }
        }
    }

    public ComboBox<Dimension> getDimension() {
        if (dimension == null) {
            dimension = new ComboBox<Dimension>();
            dimension.setTriggerAction(TriggerAction.ALL);
            dimension.setWidth(150);
            dimension.setEditable(false);
            dimension.setFieldLabel(MessageFactory.getInstance().dimension());
            dimension.setEmptyText(MessageFactory.getInstance().selectdimen());
            dimension.setDisplayField("name");
            dimension.setStore(getDimensionStore());
            dimension.addSelectionChangedListener(getDimensionSelectionChangedListener());
        }
        return dimension;
    }

    private SelectionChangedListener<Dimension> getDimensionSelectionChangedListener() {
        return new SelectionChangedListener<Dimension>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Dimension> se) {
                if (!se.getSelectedItem().getName().equalsIgnoreCase(currentDimension)) {
                    setCurrentDimension(se.getSelectedItem().getName());
                    setCurrentHierarchy("");
                    setCurrentLevel("");
                    currentMembers.clear();
                    getController().getDispatcher().forwardEvent(ReportEvents.DimensionSelected);
                }
            }
        };
    }

    private ListStore<Dimension> getDimensionStore() {
        if (dimensionStore == null) {
            dimensionStore = new ListStore<Dimension>();
        }
        return dimensionStore;
    }

    private void loadDimensions() {
        if (dimensionCache == null) {
            ServiceFactory.getOlapService().listDimensions(getReport().getConex(), getReport().getCube(), new AsyncCallback<List<Dimension>>() {

                @Override
                public void onSuccess(List<Dimension> result) {
                    setDimensionCache(result);
                    loadDimensionsFromCache();
                    AppEvent dimLoaded = new AppEvent(ReportEvents.DimensionsLoaded, result);
                    getController().getDispatcher().forwardEvent(dimLoaded);
                }

                @Override
                public void onFailure(Throwable caught) {
                }
            });
        } else {
            loadDimensionsFromCache();
        }
    }

    private void loadDimensionsFromCache() {
        getDimensionStore().removeAll();
        for (Dimension d : getDimensionCache()) {
            if (getReport().isAvailableForDimensionFilter(d.getName())) {
                getDimensionStore().add(d);
            }
        }
    }

    private void selectDimension() {
        if (currentDimension.equalsIgnoreCase("")) {
            if (getDimensionStore().getModels().size() > 0) {
                currentDimension = getDimensionStore().getModels().get(0).getName();
            }
        }
        for (Dimension d : getDimensionStore().getModels()) {
            if (d.getName().equalsIgnoreCase(currentDimension)) {
                getDimension().setValue(d);
                getController().getDispatcher().forwardEvent(ReportEvents.DimensionSelected);
            }
        }
    }

    public ComboBox<Hierarchy> getHierarchy() {
        if (hierarchy == null) {
            hierarchy = new ComboBox<Hierarchy>();
            hierarchy.setTriggerAction(TriggerAction.ALL);
            hierarchy.setWidth(150);
            hierarchy.setEditable(false);
            hierarchy.setFieldLabel(MessageFactory.getInstance().hierarchy());
            hierarchy.setEmptyText(MessageFactory.getInstance().selecthierarchy());
            hierarchy.setDisplayField("name");
            hierarchy.setStore(getHierarchyStore());
            hierarchy.addSelectionChangedListener(getHierarchySelectionChangedListener());
        }
        return hierarchy;
    }

    private SelectionChangedListener<Hierarchy> getHierarchySelectionChangedListener() {
        return new SelectionChangedListener<Hierarchy>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Hierarchy> se) {
                if (!se.getSelectedItem().getName().equalsIgnoreCase(currentHierarchy)) {
                    setCurrentHierarchy(se.getSelectedItem().getName());
                    setCurrentLevel("");
                    currentMembers.clear();
                    getController().getDispatcher().forwardEvent(ReportEvents.HierarchySelected);
                }
            }
        };
    }

    private ListStore<Hierarchy> getHierarchyStore() {
        if (hierarchyStore == null) {
            hierarchyStore = new ListStore<Hierarchy>();
        }
        return hierarchyStore;
    }

    private void loadHierarchies() {
        ServiceFactory.getOlapService().listHierarchies(getReport().getConex(), getReport().getCube(), currentDimension, new AsyncCallback<List<Hierarchy>>() {

            @Override
            public void onSuccess(List<Hierarchy> result) {
                getHierarchyStore().removeAll();
                getHierarchyStore().add(result);
                getController().getDispatcher().forwardEvent(ReportEvents.HierarchiesLoaded);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    private void selectHierarchy() {
        if (currentHierarchy.equalsIgnoreCase("")) {
            if (getHierarchyStore().getModels().size() > 0) {
                currentHierarchy = getHierarchyStore().getModels().get(0).getName();
            }
        }
        for (Hierarchy h : getHierarchyStore().getModels()) {
            if (h.getName().equalsIgnoreCase(currentHierarchy)) {
                getHierarchy().setValue(h);
                getController().getDispatcher().forwardEvent(ReportEvents.HierarchySelected);
            }
        }
    }

    public ComboBox<Level> getLevel() {
        if (level == null) {
            level = new ComboBox<Level>();
            level.setTriggerAction(TriggerAction.ALL);
            level.setEditable(false);
            level.setFieldLabel(MessageFactory.getInstance().level());
            level.setEmptyText(MessageFactory.getInstance().selectlevel());
            level.setDisplayField("name");
            level.setStore(getLevelStore());
            level.addSelectionChangedListener(getLevelSelectionChangedListener());
        }
        return level;
    }

    private SelectionChangedListener<Level> getLevelSelectionChangedListener() {
        return new SelectionChangedListener<Level>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Level> se) {
                if (!se.getSelectedItem().getName().equalsIgnoreCase(currentLevel)) {
                    setCurrentLevel(se.getSelectedItem().getName());
                    currentMembers.clear();
                    getController().getDispatcher().forwardEvent(ReportEvents.LevelSelected);
                }
            }
        };
    }

    private ListStore<Level> getLevelStore() {
        if (levelStore == null) {
            levelStore = new ListStore<Level>();
        }
        return levelStore;
    }

    private void loadLevels() {
        ServiceFactory.getOlapService().listLevels(getReport().getConex(), getReport().getCube(), currentDimension, currentHierarchy, new AsyncCallback<List<Level>>() {

            @Override
            public void onSuccess(List<Level> result) {
                getLevelStore().removeAll();
                getLevelStore().add(result);
                getController().getDispatcher().forwardEvent(ReportEvents.LevelsLoaded);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    private void selectLevel() {
        if (currentLevel.equalsIgnoreCase("")) {
            if (getLevelStore().getModels().size() > 0) {
                currentLevel = getLevelStore().getModels().get(0).getName();
            }
        }
        for (Level l : getLevelStore().getModels()) {
            if (l.getName().equalsIgnoreCase(currentLevel)) {
                getLevel().setValue(l);
                getController().getDispatcher().forwardEvent(ReportEvents.LevelSelected);
            }
        }
    }

    private ListView<Member> getMembers() {
        if (members == null) {
            members = new ListView<Member>(getMemberStore()) {

                @Override
                protected Member prepareData(Member model) {
                    if (getLongname().getValue()) {
                        model.set("custom", model.get("unique_name"));
                    } else {
                        model.set("custom", model.get("name"));
                    }
                    return model;
                }
            };
            members.setDisplayProperty("custom");
        }
        return members;
    }

    private ListStore<Member> getMemberStore() {
        if (memberStore == null) {
            memberStore = new ListStore<Member>();
        }
        return memberStore;
    }

    private void loadMembers() {
        getMembers().mask();
        ServiceFactory.getOlapService().listMembers(getReport().getConex(), getReport().getCube(), currentDimension, currentHierarchy, currentLevel, true, new AsyncCallback<List<Member>>() {

            @Override
            public void onSuccess(List<Member> result) {
                getMemberStore().removeAll();
                getMemberStore().add(result);
                getMembers().unmask();
                getController().getDispatcher().forwardEvent(ReportEvents.LevelMembersLoaded);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    private void selectMembers() {
        getMembers().getSelectionModel().deselectAll();
        for (String c : currentMembers) {
            for (Member m : getMemberStore().getModels()) {
                if (c.equalsIgnoreCase(m.getUniqueName())) {
                    getMembers().getSelectionModel().select(m, true);
                    break;
                }
            }
        }
    }

    private Button getOkButton() {
        if (okButton == null) {
            okButton = FormControls.getOkButton();
            okButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {

                @Override
                public void handleEvent(ButtonEvent be) {
                    if (saveReport()) {
                        getController().getDispatcher().forwardEvent(ReportEvents.DimensionChange);
                    }
                    getSelector().hide();
                }
            });
        }
        return okButton;
    }

    private Boolean saveReport() {
        Boolean changed = false;
        if (!getReport().getDimension().getDimension().equalsIgnoreCase(currentDimension)) {
            getReport().getDimension().setDimension(currentDimension);
            changed = true;
        }
        if (changed || !getReport().getDimension().getHierarchy().equalsIgnoreCase(currentHierarchy)) {
            getReport().getDimension().setHierarchy(currentHierarchy);
            changed = true;
        }
        if (changed || !getReport().getDimension().getLevel().equalsIgnoreCase(currentLevel)) {
            getReport().getDimension().setLevel(currentLevel);
            changed = true;
        }
        if (changed || isLevelMembersChanged()) {
            getReport().getDimension().getMembers().clear();
            for (Member m : getMembers().getSelectionModel().getSelectedItems()) {
                getReport().getDimension().getMembers().add(m.getUniqueName());
            }
            changed = true;
        }
        return changed;
    }

    private Boolean isLevelMembersChanged() {
        if (getReport().getDimension().getMembers().size() == getMembers().getSelectionModel().getSelectedItems().size()) {
            for (String s : getReport().getDimension().getMembers()) {
                for (Member m : getMembers().getSelectionModel().getSelectedItems()) {
                    if (s.equalsIgnoreCase(m.getUniqueName())) {
                        break;
                    }
                    return true;
                }
            }
            return false;
        } else {
            return true;
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
        if (type == ReportEvents.ReportLoaded) {
            initCurrentValues();
            loadDimensions();
        }
        if (type == ReportEvents.DimensionsLoaded) {
            selectDimension();
        }
        if (type == ReportEvents.DimensionSelected) {
            loadHierarchies();
        }
        if (type == ReportEvents.HierarchiesLoaded) {
            selectHierarchy();
        }
        if (type == ReportEvents.HierarchySelected) {
            loadLevels();
        }
        if (type == ReportEvents.LevelsLoaded) {
            selectLevel();
        }
        if (type == ReportEvents.LevelSelected) {
            loadMembers();
        }
        if (type == ReportEvents.LevelMembersLoaded) {
            selectMembers();
            if (getMembers().getSelectionModel().getSelectedItems().size() > 0) {
                getMemberSet().expand();
            }
        }
        if (type == ReportEvents.ShowDimensionSelector) {
            initCurrentValues();
            loadDimensionsFromCache();
            selectDimension();
            getSelector().show();
            getSelector().center();
        }
    }
}
