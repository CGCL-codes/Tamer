package com.qcs.eduquill.web;

import com.qcs.eduquill.sl.bprocess.DepartmentMaster;
import com.qcs.eduquill.utilities.CustomizationUtility;
import com.qcs.eduquill.utilities.EQButtonConstants;
import com.qcs.eduquill.utilities.EQLogging;
import com.qcs.eduquill.utilities.EQUserSession;
import com.qcs.eduquill.utilities.EduQuillBeanFactory;
import com.qcs.eduquill.utilities.InformationMessages;
import com.qcs.eduquill.utilities.MasterLog;
import com.qcs.eduquill.utilities.SchemaXML;
import com.qcs.eduquill.utilities.Suggestions;
import com.qcs.eduquill.utilities.ZKUtility;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @version      1.0 24 Jun 2009
 * @author       Sreekanthreddy Y
 */
public class NewDepartmentMasterComposer extends Window implements EQButtonConstants {

    private int clickedButton = BUTTON_CANCEL;

    InformationMessages informationMessages;

    Vector vector;

    public NewDepartmentMasterComposer() {
        setId("winx");
        setHeight("50%");
        setContentStyle("overflow:auto");
        informationMessages = new InformationMessages(this);
        onLoadComponents().setParent(this);
        applyWindowProperties();
    }

    public Vbox onLoadComponents() {
        Vbox vbox = new Vbox();
        vbox.setWidth("100%");
        final Grid grid = new Grid();
        Columns columns = new Columns();
        Column type = new Column();
        type.setWidth("50%");
        Column content = new Column();
        content.setWidth("50%");
        type.setParent(columns);
        content.setParent(columns);
        columns.setParent(grid);
        final Rows rows = new Rows();
        rows.setParent(grid);
        com.qcs.eduquill.sl.bprocess.DepartmentMaster departmentMaster = (DepartmentMaster) EduQuillBeanFactory.getInstance().getBean("departmentMaster");
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        Row deptNameRow = new Row();
        Label deptNameLbl = new Label(Labels.getLabel("label.DepartmentName"));
        Textbox deptName = new Textbox();
        deptName.setId("deptName");
        deptNameLbl.setParent(deptNameRow);
        ZKUtility.getInstance().applyMandatory(deptNameLbl);
        deptName.setParent(deptNameRow);
        deptNameRow.setParent(rows);
        Row deptCodeRow = new Row();
        Label deptCodeLbl = new Label(Labels.getLabel("label.DepartmentCode"));
        Textbox deptCode = new Textbox();
        deptCode.setId("deptCode");
        deptCodeLbl.setParent(deptCodeRow);
        deptCode.setParent(deptCodeRow);
        deptCodeRow.setParent(rows);
        Row hodRow = new Row();
        Label hodLbl = new Label(Labels.getLabel("label.HOD"));
        Textbox hod = new Textbox();
        hod.setId("hod");
        hodLbl.setParent(hodRow);
        hod.setParent(hodRow);
        hodRow.setParent(rows);
        Row frmDateRow = new Row();
        Label frmDateLbl = new Label(Labels.getLabel("label.FromDate"));
        Datebox frmDate = new Datebox();
        frmDate.setId("frmDate");
        frmDateLbl.setParent(frmDateRow);
        frmDate.setParent(frmDateRow);
        frmDateRow.setParent(rows);
        Row toDateRow = new Row();
        Label toDateLbl = new Label(Labels.getLabel("label.ToDate"));
        Datebox toDate = new Datebox();
        toDate.setId("toDate");
        toDateLbl.setParent(toDateRow);
        toDate.setParent(toDateRow);
        toDateRow.setParent(rows);
        Row deptTypeRow = new Row();
        Label deptTypeLbl = new Label(Labels.getLabel("label.DepartmentTypeName"));
        Combobox deptTypeName = new Combobox();
        deptTypeName.setReadonly(true);
        deptTypeName.setId("deptTypeName");
        deptTypeLbl.setParent(deptTypeRow);
        deptTypeName.setParent(deptTypeRow);
        deptTypeRow.setParent(rows);
        vector = departmentMaster.bm_getDepartmentTypeNames(organizationId);
        for (int i = 0; i < vector.size(); i++) {
            Hashtable hashtable = (Hashtable) vector.get(i);
            Comboitem comboitem = new Comboitem(hashtable.get("departmentTypeName").toString());
            comboitem.setParent(deptTypeName);
        }
        Div div = new Div();
        div.setAlign("center");
        Button save = new Button(Labels.getLabel("label.Save"));
        save.setWidth("60px");
        save.addForward(Events.ON_CLICK, "", "onSaveClick");
        Button cancel = new Button(Labels.getLabel("label.Cancel"));
        cancel.setWidth("60px");
        cancel.addEventListener(Events.ON_CLICK, new EventListener() {

            @Override
            public void onEvent(Event arg0) throws Exception {
                detach();
            }
        });
        save.setParent(div);
        cancel.setParent(div);
        grid.setParent(vbox);
        new Separator().setParent(vbox);
        schemaGrid().setParent(vbox);
        combinatoinGrid().setParent(vbox);
        div.setParent(vbox);
        Suggestions suggestions = new Suggestions(vbox);
        suggestions.mandatoryFieldsNote();
        return vbox;
    }

    public void onClickingPlus(Event evt) {
        try {
            ForwardEvent fe = (ForwardEvent) evt;
            MouseEvent me = (MouseEvent) fe.getOrigin();
            Component comp = me.getTarget();
            List newList = new ArrayList();
            Integer maxId = (Integer) getAttribute("MaxId");
            setAttribute("MaxId", maxId + 1);
            newList = getGridData();
            newList.add(buildDefaultGridData(maxId + 1));
            ((Grid) getFellow("advSearchGrid")).setModel(new SimpleListModel(newList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickingRemove(Event evt) {
        try {
            ForwardEvent fe = (ForwardEvent) evt;
            MouseEvent me = (MouseEvent) fe.getOrigin();
            Component comp = me.getTarget();
            List tempList = new ArrayList();
            tempList = getGridData();
            Integer selRowIndx = (Integer) comp.getAttribute("Index");
            List newList = new ArrayList();
            for (int i = 1, j = 1; i <= tempList.size(); i++) {
                Hashtable ht = (Hashtable) tempList.get(i - 1);
                if (i == selRowIndx) {
                } else {
                    ht.put("Index", j);
                    j++;
                    newList.add(ht);
                }
            }
            ((Grid) getFellow("advSearchGrid")).setModel(new SimpleListModel(newList));
            setAttribute("MaxId", newList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Hashtable buildDefaultGridData(int index) {
        Hashtable ht = new Hashtable();
        ht.put("Index", index);
        setAttribute("MaxId", index);
        return ht;
    }

    private Grid combinatoinGrid() {
        Grid grid = new Grid();
        grid.setId("advSearchGrid");
        grid.setFixedLayout(true);
        Columns cols = new Columns();
        Column colSelect = new Column();
        colSelect.setAlign("center");
        colSelect.setWidth("22%");
        colSelect.setParent(cols);
        Column colSelect1 = new Column();
        colSelect1.setAlign("center");
        colSelect1.setWidth("22%");
        colSelect1.setParent(cols);
        Column colSelect2 = new Column();
        colSelect2.setAlign("center");
        colSelect2.setWidth("22%");
        colSelect2.setParent(cols);
        Column colSelect3 = new Column();
        colSelect3.setAlign("center");
        colSelect3.setWidth("22%");
        colSelect3.setParent(cols);
        Column colSelect4 = new Column();
        colSelect4.setAlign("center");
        colSelect4.setWidth("6%");
        colSelect4.setParent(cols);
        Column colSelect5 = new Column();
        colSelect5.setAlign("center");
        colSelect5.setWidth("6%");
        colSelect5.setParent(cols);
        cols.setParent(grid);
        grid.setRowRenderer(getRowRender());
        grid.setModel(new SimpleListModel(new Hashtable[] { (buildDefaultGridData(1)) }));
        return grid;
    }

    private Vbox schemaGrid() {
        com.qcs.eduquill.sl.bprocess.DepartmentMaster departmentMaster = (DepartmentMaster) EduQuillBeanFactory.getInstance().getBean("departmentMaster");
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        String schemaXML = departmentMaster.bm_getSchemaXML(organizationId, "department_master");
        Vbox vbox = CustomizationUtility.getInstance().getSchemaComponentsGrid(schemaXML);
        vbox.setId("customVbox");
        return vbox;
    }

    public RowRenderer getRowRender() {
        return new RowRenderer() {

            @Override
            public void render(Row arg0, Object arg1) throws Exception {
                com.qcs.eduquill.sl.bprocess.DepartmentMaster departmentMaster = (DepartmentMaster) EduQuillBeanFactory.getInstance().getBean("departmentMaster");
                EQUserSession userSession = new EQUserSession();
                Long organizationId = userSession.getorganizationId();
                Hashtable ht = (Hashtable) arg1;
                Integer index = (Integer) ht.get("Index");
                String selFieldVal = "";
                if (ht.get("SelFldVal") != null && ht.get("SelFldVal").toString().length() > 0) {
                    selFieldVal = ht.get("SelFldVal").toString();
                }
                String selBooleanVal = "";
                if (ht.get("SelBooleanVal") != null && ht.get("SelBooleanVal").toString().length() > 0) {
                    selBooleanVal = ht.get("SelBooleanVal").toString();
                }
                Label divisionNameLbl = new Label(Labels.getLabel("label.DivisionName"));
                divisionNameLbl.setParent(arg0);
                ZKUtility.getInstance().applyMandatory(divisionNameLbl);
                Combobox fieldCB = new Combobox();
                fieldCB.setId("field" + index);
                Comboitem comboitem1 = new Comboitem();
                comboitem1.setLabel("-----" + Labels.getLabel("label.SelectOne") + "-----");
                comboitem1.setParent(fieldCB);
                fieldCB.setSelectedItem(comboitem1);
                vector = departmentMaster.bm_getDivisionNames(organizationId);
                for (int i = 0; i < vector.size(); i++) {
                    Hashtable hashtable = (Hashtable) vector.get(i);
                    Comboitem comboitem = new Comboitem();
                    comboitem.setLabel(hashtable.get("divisionName").toString());
                    comboitem.setValue(hashtable.get("divisionId").toString());
                    comboitem.setParent(fieldCB);
                    if (selFieldVal.equalsIgnoreCase(hashtable.get("divisionName").toString())) {
                        comboitem.setValue(hashtable.get("divisionId").toString());
                        comboitem.setLabel(hashtable.get("divisionName").toString());
                        fieldCB.setSelectedItem(comboitem);
                    }
                }
                fieldCB.setReadonly(true);
                fieldCB.setWidth("75%");
                fieldCB.addForward(Events.ON_CHANGE, "", "onClickingDivisionComboItem");
                fieldCB.setAttribute("INDEX", index);
                fieldCB.setParent(arg0);
                Label disciplineLbl = new Label(Labels.getLabel("label.DisciplineName"));
                disciplineLbl.setParent(arg0);
                Combobox booleanCB = new Combobox();
                booleanCB.setId("boolean" + index);
                Comboitem comboitem = new Comboitem();
                comboitem.setLabel("-----" + Labels.getLabel("label.SelectOne") + "-----");
                comboitem.setValue("Select");
                comboitem.setParent(booleanCB);
                booleanCB.setSelectedItem(comboitem);
                if (getValue("TermLbl") != null) {
                    Long divisionId = Long.parseLong(getAttribute("TermVal").toString());
                    vector = departmentMaster.bm_getDisciplineNames(organizationId, divisionId);
                    for (int i = 0; i < vector.size(); i++) {
                        Hashtable hashtable = (Hashtable) vector.get(i);
                        Comboitem comboitem2 = new Comboitem();
                        if (selBooleanVal.equalsIgnoreCase(hashtable.get("disciplineName").toString())) {
                            comboitem2.setLabel(hashtable.get("disciplineName").toString());
                            comboitem2.setValue(hashtable.get("disciplineId").toString());
                            comboitem2.setParent(booleanCB);
                            booleanCB.setSelectedItem(comboitem2);
                        }
                    }
                }
                booleanCB.setReadonly(true);
                booleanCB.setWidth("75%");
                booleanCB.setAttribute("INDEX", index);
                booleanCB.addForward(Events.ON_CHANGE, "", "onClickingDisciplineComboItem");
                booleanCB.setParent(arg0);
                Button plusBtn = new Button("", "/images/16/list-add.png");
                plusBtn.setTooltiptext(Labels.getLabel("label.Add"));
                plusBtn.setAttribute("MaxId", index);
                plusBtn.addForward(Events.ON_CLICK, "", "onClickingPlus");
                plusBtn.setParent(arg0);
                Button minusBtn = new Button("", "/images/16/list-remove.png");
                minusBtn.setTooltiptext(Labels.getLabel("label.Remove"));
                minusBtn.setAttribute("Index", index);
                minusBtn.addForward(Events.ON_CLICK, "", "onClickingRemove");
                if (index == 1) {
                    minusBtn.setVisible(false);
                } else {
                    minusBtn.setDisabled(false);
                }
                minusBtn.setParent(arg0);
            }
        };
    }

    private List getGridData() {
        List selectedGridValues = new ArrayList();
        try {
            Grid grid = (Grid) getFellow("advSearchGrid");
            for (int i = 1; i <= grid.getModel().getSize(); i++) {
                Hashtable selRowHt = new Hashtable();
                Combobox fieldCBox = (Combobox) getFellow("field" + i);
                Combobox booleanCBox = (Combobox) getFellow("boolean" + i);
                String selFldLbl = "";
                String selFldVal = "";
                if (fieldCBox.getSelectedItem() != null) {
                    selFldLbl = fieldCBox.getSelectedItem().getLabel();
                    selFldVal = (String) fieldCBox.getSelectedItem().getValue();
                }
                String selBooleanVal = "";
                String selBooleanLbl = "";
                if (booleanCBox.getSelectedItem() != null) {
                    selBooleanVal = booleanCBox.getSelectedItem().getLabel();
                    selBooleanLbl = (String) booleanCBox.getSelectedItem().getValue();
                }
                selRowHt.put("Index", i);
                selRowHt.put("SelFldVal", selFldLbl);
                selRowHt.put("SelBooleanVal", selBooleanVal);
                selRowHt.put("SelFldLbl", selFldVal);
                selRowHt.put("SelBooleanLbl", selBooleanLbl);
                selectedGridValues.add(selRowHt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedGridValues;
    }

    private List customizationGridData() {
        List customizationGridValues = new ArrayList();
        try {
            Grid grid = (Grid) getFellow("initGrid");
        } catch (Exception e) {
        }
        return customizationGridValues;
    }

    public void onClickingDivisionComboItem(Event evt) {
        com.qcs.eduquill.sl.bprocess.DepartmentMaster departmentMaster = (DepartmentMaster) EduQuillBeanFactory.getInstance().getBean("departmentMaster");
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        ForwardEvent fe = (ForwardEvent) evt;
        InputEvent ie = (InputEvent) fe.getOrigin();
        Component comp = ie.getTarget();
        Combobox cb = (Combobox) comp;
        int index = Integer.parseInt(cb.getAttribute("INDEX").toString());
        Combobox disciplineCombo = (Combobox) getFellow("boolean" + index);
        Combobox prevBox = null;
        if (index > 1) {
            prevBox = (Combobox) getFellow("boolean" + (index - 1));
            String lbl = cb.getSelectedItem().getLabel();
            if (prevBox.getSelectedItem().getValue() == "Select" && getAttribute("SelectDivision" + (index - 1)) != null && getAttribute("SelectDivision" + (index - 1)).toString().equals(lbl)) {
                try {
                    Messagebox.show("Duplicate Entry");
                } catch (Exception e) {
                }
            }
        }
        Long divisionId = Long.parseLong((cb.getSelectedItem().getValue()).toString());
        disciplineCombo.getChildren().clear();
        vector = departmentMaster.bm_getDisciplineNames(organizationId, divisionId);
        for (int i = 0; i < vector.size(); i++) {
            Hashtable hashtable = (Hashtable) vector.get(i);
            Comboitem comboitem2 = new Comboitem();
            comboitem2.setLabel(hashtable.get("disciplineName").toString());
            comboitem2.setValue(hashtable.get("disciplineId").toString());
            comboitem2.setParent(disciplineCombo);
        }
        String selItemVal = (cb.getSelectedItem()).getLabel();
        setAttribute("TermVal", selItemVal);
        setAttribute("SelectDivision" + index, cb.getSelectedItem().getLabel());
        setValue(cb.getSelectedItem().getLabel(), cb.getSelectedItem().getValue());
    }

    public void onClickingDisciplineComboItem(Event evt) {
        ForwardEvent fe = (ForwardEvent) evt;
        InputEvent ie = (InputEvent) fe.getOrigin();
        Component comp = ie.getTarget();
        Combobox cb = (Combobox) comp;
        String selItemLbl = (cb.getSelectedItem()).getLabel();
        setAttribute("selItemLbl", selItemLbl);
        int index = Integer.parseInt(cb.getAttribute("INDEX").toString());
        Combobox prevBox = null;
        Combobox divPrevBox = null;
        if (index > 1) {
            prevBox = (Combobox) getFellow("boolean" + (index - 1));
            divPrevBox = (Combobox) getFellow("field" + (index - 1));
            String lbl = cb.getSelectedItem().getLabel();
            if (prevBox.getSelectedItem().getValue() != "Select" && getAttribute("SelectDivision" + (index - 1)).toString().equalsIgnoreCase(getAttribute("SelectDivision" + (index)).toString()) && prevBox.getSelectedItem().getLabel().toString().equalsIgnoreCase(lbl)) {
                try {
                    Messagebox.show("Duplicate Entry");
                } catch (Exception e) {
                }
            }
        }
    }

    public void onSaveClick(Event event) {
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component save = me.getTarget();
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        String deptName = ((Textbox) getFellow("deptName")).getValue().trim();
        String deptCode = ((Textbox) getFellow("deptCode")).getValue().trim();
        String hod = ((Textbox) getFellow("hod")).getValue().trim();
        Date frmDate = ((Datebox) getFellow("frmDate")).getValue();
        String frmDate1 = ((Datebox) getFellow("frmDate")).getInnerAttrs();
        Date toDate = ((Datebox) getFellow("toDate")).getValue();
        String deptTypeName = "";
        if (((Combobox) getFellow("deptTypeName")).getSelectedItem() != null) {
            deptTypeName = ((Combobox) getFellow("deptTypeName")).getSelectedItem().getLabel();
        }
        Long deptTypeId = null;
        com.qcs.eduquill.sl.bprocess.DepartmentMaster departmentMaster = (DepartmentMaster) EduQuillBeanFactory.getInstance().getBean("departmentMaster");
        vector = departmentMaster.bm_getDepartmentTypeNames(organizationId);
        for (int i = 0; i < vector.size(); i++) {
            Hashtable h = (Hashtable) vector.get(i);
            if (deptTypeName.equalsIgnoreCase(h.get("departmentTypeName").toString())) {
                deptTypeId = Long.parseLong(h.get("departmentTypeId").toString());
                break;
            }
        }
        Vbox vbox = (Vbox) save.getParent().getParent().getFellow("customVbox");
        String schemaXML = departmentMaster.bm_getSchemaXML(organizationId, "department_master");
        List customizationGridValues = CustomizationUtility.getInstance().customizationGridValues(vbox, schemaXML);
        EQLogging.getFineLogger().fine("customizationGridValues : " + customizationGridValues);
        List searchableList = (List) customizationGridValues.get(0);
        Hashtable nonSearchable = (Hashtable) customizationGridValues.get(1);
        String schema = "";
        if (nonSearchable.size() > 0 && nonSearchable != null) {
            SchemaXML xMLSchema = new SchemaXML();
            schema = xMLSchema.bm_generateSchemaDataXML(nonSearchable);
        }
        if (customizationGridValues.size() > 0 && customizationGridValues != null) {
            for (int i = 0; i < customizationGridValues.size(); i++) {
            }
        }
        List list = getGridData();
        if (deptName.length() > 1) {
            Object mode = save.getParent().getParent().getParent().getAttribute("MODE");
            MasterLog masterLog = new MasterLog();
            String editLog = masterLog.bm_appendLog("", new Date().getTime(), MasterLog.ACTIVITY_CREATE);
            int retVal = 1;
            if (mode.toString().equalsIgnoreCase("NEW")) {
                retVal = departmentMaster.bm_saveDepartmentMaster(organizationId, deptName, deptCode, hod, frmDate, toDate, deptTypeId, list, searchableList, editLog);
            } else {
            }
            if (retVal == departmentMaster.STATUS_SUCCESS) {
                setClickedButton(BUTTON_SAVE);
                detach();
            }
        } else {
            informationMessages.warning(Labels.getLabel("message.PleaseSelectCourseName"));
        }
    }

    private void applyWindowProperties() {
        setMaximizable(true);
        setClosable(true);
        setSizable(true);
        ZKUtility.getInstance().applyEscEvent(this);
    }

    /**
     * @return the clickedButton
     */
    public int getClickedButton() {
        return clickedButton;
    }

    /**
     * @param clickedButton the clickedButton to set
     */
    public void setClickedButton(int clickedButton) {
        this.clickedButton = clickedButton;
    }

    private void setValue(Object label, Object value) {
        setAttribute("TermLbl", label);
        setAttribute("TermVal", value);
    }

    private Object getValue(Object TermLbl) {
        if (getAttribute("TermLbl") == null) {
        }
        return getAttribute("TermLbl");
    }
}
