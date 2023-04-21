package com.qcs.eduquill.web;

import com.qcs.eduquill.sl.bprocess.CourseBooks;
import com.qcs.eduquill.sl.bprocess.CourseTopics;
import com.qcs.eduquill.utilities.EQButtonConstants;
import com.qcs.eduquill.utilities.EQLogging;
import com.qcs.eduquill.utilities.EQUserSession;
import com.qcs.eduquill.utilities.EduQuillBeanFactory;
import com.qcs.eduquill.utilities.InformationMessages;
import com.qcs.eduquill.utilities.MasterLog;
import com.qcs.eduquill.utilities.Suggestions;
import com.qcs.eduquill.utilities.ZKUtility;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @version      1.0 11 Jun 2009
 * @author       Sreekanthreddy Y
 */
public class NewCourseBooksComposer extends Window implements Composer, EQButtonConstants {

    private int clickedButton = BUTTON_CANCEL;

    InformationMessages informationMessages;

    Vector vector;

    public NewCourseBooksComposer() {
        setId("winx");
        informationMessages = new InformationMessages(this);
        onLoadComponents().setParent(this);
        applyWindowProperties();
    }

    public Vbox onLoadComponents() {
        Vbox vbox = new Vbox();
        vbox.setWidth("100%");
        Grid grid = new Grid();
        Columns columns = new Columns();
        Column type = new Column();
        type.setWidth("50%");
        Column content = new Column();
        content.setWidth("50%");
        type.setParent(columns);
        content.setParent(columns);
        columns.setParent(grid);
        Rows rows = new Rows();
        rows.setParent(grid);
        com.qcs.eduquill.sl.bprocess.CourseTopics courseTopics = (CourseTopics) EduQuillBeanFactory.getInstance().getBean("courseTopics");
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        vector = courseTopics.bm_getCourseNames(organizationId);
        Row courseNameRow = new Row();
        Label courseNameLbl = new Label(Labels.getLabel("label.CourseName"));
        Combobox courseId = new Combobox();
        courseId.setId("courseId");
        courseId.setReadonly(true);
        courseNameLbl.setParent(courseNameRow);
        courseId.setParent(courseNameRow);
        for (int i = 0; i < vector.size(); i++) {
            Hashtable h = (Hashtable) vector.get(i);
            Comboitem comboitem = new Comboitem(h.get("courseName").toString());
            comboitem.setParent(courseId);
        }
        courseNameRow.setParent(rows);
        Row bookNameRow = new Row();
        Label bookNameLbl = new Label(Labels.getLabel("label.BookName"));
        Textbox bookName = new Textbox();
        bookName.setId("bName");
        bookNameLbl.setParent(bookNameRow);
        bookName.setParent(bookNameRow);
        bookNameRow.setParent(rows);
        Row authorNameRow = new Row();
        Label authorNameLbl = new Label(Labels.getLabel("label.AuthorName"));
        Textbox authName = new Textbox();
        authName.setId("aName");
        authorNameLbl.setParent(authorNameRow);
        authName.setParent(authorNameRow);
        authorNameRow.setParent(rows);
        Row publicRow = new Row();
        Label publicLbl = new Label(Labels.getLabel("label.Publications"));
        Textbox publications = new Textbox();
        publications.setId("publications");
        publicLbl.setParent(publicRow);
        publications.setParent(publicRow);
        publicRow.setParent(rows);
        Row seriesRow = new Row();
        Label seriesLbl = new Label(Labels.getLabel("label.Series"));
        Textbox series = new Textbox();
        series.setId("series");
        seriesLbl.setParent(seriesRow);
        series.setParent(seriesRow);
        seriesRow.setParent(rows);
        Div div = new Div();
        div.setAlign("center");
        Button save = new Button(Labels.getLabel("label.Save"));
        save.setWidth("60px");
        save.addForward(Events.ON_CLICK, "", "onSaveClick");
        Button cancel = new Button(Labels.getLabel("label.Cancel"));
        cancel.setWidth("60px");
        cancel.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                detach();
            }
        });
        save.setParent(div);
        cancel.setParent(div);
        grid.setParent(vbox);
        div.setParent(vbox);
        Suggestions suggestions = new Suggestions(vbox);
        suggestions.mandatoryFieldsNote();
        return vbox;
    }

    public void onSaveClick(Event event) {
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component save = me.getTarget();
        EQUserSession userSession = new EQUserSession();
        Long organizationId = userSession.getorganizationId();
        String bookName = ((Textbox) getFellow("bName")).getValue();
        String authorName = ((Textbox) getFellow("aName")).getValue();
        String publications = ((Textbox) getFellow("publications")).getValue();
        String series = ((Textbox) getFellow("series")).getValue();
        String courseName = ((Combobox) getFellow("courseId")).getSelectedItem().getLabel();
        Long courseId = null;
        for (int i = 0; i < vector.size(); i++) {
            Hashtable h = (Hashtable) vector.get(i);
            if (courseName.equalsIgnoreCase(h.get("courseName").toString())) {
                courseId = Long.parseLong(h.get("courseId").toString());
                break;
            }
        }
        EQLogging.getFineLogger().fine("Course ID" + courseId);
        Object mode = save.getParent().getParent().getParent().getAttribute("MODE");
        com.qcs.eduquill.sl.bprocess.CourseBooks courseBooks = (CourseBooks) EduQuillBeanFactory.getInstance().getBean("courseBooks");
        MasterLog masterLog = new MasterLog();
        String editLog = masterLog.bm_appendLog("", new Date().getTime(), MasterLog.ACTIVITY_CREATE);
        int retVal = 1;
        if (mode.toString().equalsIgnoreCase("NEW")) {
            retVal = courseBooks.bm_saveCourseBooks(organizationId, courseId, bookName, authorName, publications, series, editLog);
        } else {
            Long courseBookId = Long.parseLong(save.getParent().getParent().getParent().getAttribute("courseBookId").toString());
            String log = courseBooks.bm_getLog(organizationId, courseBookId);
            String editLog1 = masterLog.bm_appendLog(log, new Date().getTime(), MasterLog.ACTIVITY_MODIFY);
            retVal = courseBooks.bm_saveCourseBooksAtEdit(organizationId, courseBookId, courseId, bookName, authorName, publications, series, editLog1);
        }
        if (retVal == 0) {
            setClickedButton(BUTTON_SAVE);
            detach();
        }
    }

    private void applyWindowProperties() {
        setTitle(Labels.getLabel("label.NewCourseBooks"));
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

    public void doAfterCompose(Component arg0) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
