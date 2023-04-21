package com.qcs.eduquill.web;

import com.qcs.eduquill.sl.bprocess.CourseTypeMaster;
import com.qcs.eduquill.utilities.EQButtonConstants;
import com.qcs.eduquill.utilities.EQLogging;
import com.qcs.eduquill.utilities.MasterLog;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Sreekanth
 */
public class NewCourseTypeMasterComposer extends Window implements Composer, EQButtonConstants {

    public Vector vector = null;

    private int clickedButton = BUTTON_CANCEL;

    public NewCourseTypeMasterComposer() {
        Vbox vbox = new Vbox();
        vbox.setWidth("100%");
        Grid grid = new Grid();
        grid.setWidth("100%");
        grid.applyProperties();
        grid.setFixedLayout(true);
        Columns columns = new Columns();
        Column typeColumn = new Column(Labels.getLabel("label.Type"));
        typeColumn.setWidth("50%");
        Column contentColumn = new Column(Labels.getLabel("label.Content"));
        contentColumn.setWidth("50%");
        typeColumn.setParent(columns);
        contentColumn.setParent(columns);
        Rows rows = new Rows();
        Row nameRow = new Row();
        nameRow.setParent(rows);
        Label nameLbl = new Label(Labels.getLabel("label.CourseTypeName"));
        Textbox codeBox = new Textbox();
        codeBox.addForward(Events.ON_CHANGING, "", "onEnteringCourseTypeName");
        codeBox.setId("courseName");
        Label lbl = new Label();
        lbl.setId("enteringName");
        nameLbl.setParent(nameRow);
        Hbox hbox = new Hbox();
        codeBox.setParent(hbox);
        lbl.setParent(hbox);
        hbox.setParent(nameRow);
        Row descRow = new Row();
        descRow.setParent(rows);
        Label descLbl = new Label(Labels.getLabel("label.CourseTypeDescription"));
        Textbox descBox = new Textbox();
        descBox.setId("courseDesc");
        descLbl.setParent(descRow);
        descBox.setParent(descRow);
        rows.setParent(grid);
        grid.setParent(vbox);
        new Separator().setParent(vbox);
        Div div = new Div();
        div.setAlign("center");
        div.setParent(vbox);
        Button save = new Button(Labels.getLabel("label.Save"));
        save.setDisabled(true);
        save.setId("save");
        Button cancel = new Button(Labels.getLabel("label.Cancel"));
        save.setParent(div);
        cancel.setParent(div);
        cancel.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                detach();
            }
        });
        save.addForward(Events.ON_CLICK, "", "onClickingSave");
        vbox.setParent(this);
    }

    public void onEnteringCourseTypeName(Event event) {
        InputEvent ie = (InputEvent) ((ForwardEvent) event).getOrigin();
        Component comp = ie.getTarget();
        String code = ie.getValue();
        com.qcs.eduquill.sl.bprocess.CourseTypeMaster courseTypeMaster = (CourseTypeMaster) com.qcs.eduquill.utilities.EduQuillBeanFactory.getInstance().getBean("courseTypeMaster");
        if (comp.getParent().getParent().getParent().getParent().getParent().getParent().getAttribute("MODE") != null && comp.getParent().getParent().getParent().getParent().getParent().getParent().getAttribute("MODE").toString().equalsIgnoreCase("EDIT")) {
            String courseName = comp.getParent().getParent().getParent().getParent().getParent().getParent().getAttribute("courseTypeName").toString();
            EQLogging.getFineLogger().fine("Getting discCode" + courseName);
            Window win = (Window) comp.getParent().getParent().getParent().getParent().getParent().getParent();
            int retVal = courseTypeMaster.bm_enteringCourseTypeNameAtEdit(courseName, code);
            if (retVal == 0) {
                ((Button) win.getFellow("save")).setDisabled(false);
                ((Label) win.getFellow("enteringName")).setValue(Labels.getLabel("message.DataCanUsed"));
                ((Label) win.getFellow("enteringName")).setStyle("color:green");
            } else {
                ((Button) win.getFellow("save")).setDisabled(true);
                ((Label) win.getFellow("enteringName")).setValue(Labels.getLabel("message.SystemDetectDuplicateEntry"));
                ((Label) win.getFellow("enteringName")).setStyle("color:red");
            }
        } else {
            if (code.length() > 0) {
                EQLogging.getFineLogger().fine("Discipline code  :" + code);
                int retVal = courseTypeMaster.bm_enteringCourseTypeName(code);
                if (retVal == 0) {
                    ((Button) getFellow("save")).setDisabled(false);
                    ((Label) getFellow("enteringName")).setValue(Labels.getLabel("message.DataCanUsed"));
                    ((Label) getFellow("enteringName")).setStyle("color:green");
                } else {
                    ((Button) getFellow("save")).setDisabled(true);
                    ((Label) getFellow("enteringName")).setValue(Labels.getLabel("message.SystemDetectDuplicateEntry"));
                    ((Label) getFellow("enteringName")).setStyle("color:red");
                }
            } else {
                ((Button) getFellow("save")).setDisabled(true);
                ((Label) getFellow("enteringName")).setValue(Labels.getLabel("message.FieldCanNotBeEmpty"));
                ((Label) getFellow("enteringName")).setStyle("color:red");
            }
        }
    }

    public void onClickingSave(Event event) {
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        CourseTypeMaster courseTypeMaster = (CourseTypeMaster) com.qcs.eduquill.utilities.EduQuillBeanFactory.getInstance().getBean("courseTypeMaster");
        if (comp.getParent().getParent().getParent().getAttribute("MODE") != null && comp.getParent().getParent().getParent().getAttribute("MODE").toString().equalsIgnoreCase("EDIT")) {
            EQLogging.getFineLogger().fine("" + comp.getParent().getParent().getParent().getAttribute("MODE").toString());
            Long courseTypeId = Long.parseLong(comp.getParent().getParent().getParent().getAttribute("courseTypeId").toString());
            Window win = (Window) comp.getParent().getParent().getParent();
            String desc = ((Textbox) win.getFellow("courseDesc")).getValue();
            String name = ((Textbox) win.getFellow("courseName")).getValue();
            MasterLog masterLog = new MasterLog();
            String log = courseTypeMaster.bm_getLog(courseTypeId);
            String editLog = masterLog.bm_appendLog(log, new Date().getTime(), MasterLog.ACTIVITY_MODIFY);
            EQLogging.getFineLogger().fine("" + editLog);
            Integer retVal = courseTypeMaster.bm_saveCourseTypeMaster(courseTypeId, name, desc, editLog);
            if (retVal == 0) {
                setClickedButton(BUTTON_SAVE);
                detach();
            } else {
                EQLogging.getSevereLogger().warning("Unsupported exception");
            }
        } else {
            String courseName = ((Textbox) getFellow("courseName")).getValue();
            String courseDesc = ((Textbox) getFellow("courseDesc")).getValue();
            MasterLog masterLog = new MasterLog();
            String log = masterLog.bm_appendLog("", new Date().getTime(), MasterLog.ACTIVITY_CREATE);
            Integer retVal = courseTypeMaster.bm_newCourseTypeMaster(courseName, courseDesc, log);
            if (retVal == 0) {
                setClickedButton(BUTTON_SAVE);
                detach();
            } else {
                EQLogging.getSevereLogger().warning("Unsupported exception");
            }
        }
    }

    private Object getKey(Hashtable ht, Object value) {
        Enumeration enm = ht.keys();
        while (enm.hasMoreElements()) {
            Object key = enm.nextElement();
            if (ht.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    public void doAfterCompose(Component arg0) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
