package org.gruposp2p.aula.gwt.client.content.student;

import java.util.ArrayList;
import java.util.List;
import org.gruposp2p.aula.gwt.client.AulaApplication;
import org.gruposp2p.aula.gwt.client.content.ValidationErrorDialogBox;
import org.gruposp2p.aula.gwt.client.event.AulaEventBus;
import org.gruposp2p.aula.gwt.client.event.CoursesChangeEvent;
import org.gruposp2p.aula.gwt.client.event.CoursesChangeHandler;
import org.gruposp2p.aula.gwt.client.event.StudentChangeEvent;
import org.gruposp2p.aula.gwt.client.event.StudentChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import org.gruposp2p.aula.gwt.client.model.Course;
import org.gruposp2p.aula.gwt.client.model.Student;
import org.gruposp2p.aula.gwt.client.representation.CoursesRepresentation;
import org.gruposp2p.aula.gwt.client.representation.StudentRepresentation;
import org.gruposp2p.aula.gwt.client.representation.StudentsRepresentation;

public class NewStudentDialogBox extends DialogBox implements StudentChangeHandler {

    private AulaApplication aulaApplication;

    private TextBox nameTextBox;

    private TextBox descriptionTextBox;

    public static interface CwConstants extends Constants {

        String NewStudentDialogBoxNameLabel();

        String NewStudentDialogBoxDescriptionLabel();

        String NewStudentDialogBoxCaption();

        String CancelButtonCaption();

        String AcceptButtonCaption();
    }

    /**
	   * An instance of the constants.
	   */
    private CwConstants constants;

    public NewStudentDialogBox(AulaApplication aulaApplication) {
        this.aulaApplication = aulaApplication;
        constants = aulaApplication.getConstants();
        ensureDebugId("NewCourseDialogBox");
        showDialog();
        AulaEventBus.getInstance().registerToStudentChange(this);
    }

    private void showDialog() {
        setText(constants.NewStudentDialogBoxCaption());
        FlexTable layout = new FlexTable();
        layout.clear();
        layout.setCellSpacing(6);
        layout.setWidth("250px");
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
        layout.setHTML(0, 0, constants.NewStudentDialogBoxNameLabel());
        nameTextBox = new TextBox();
        layout.setWidget(0, 1, nameTextBox);
        layout.setHTML(1, 0, constants.NewStudentDialogBoxDescriptionLabel());
        descriptionTextBox = new TextBox();
        layout.setWidget(1, 1, descriptionTextBox);
        cellFormatter.setColSpan(2, 0, 2);
        cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
        Button cancelButton = new Button(constants.CancelButtonCaption(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                hide();
            }
        });
        Button acceptButton = new Button(constants.AcceptButtonCaption(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                sendData();
            }
        });
        Button getButton = new Button("GET", new ClickHandler() {

            public void onClick(ClickEvent event) {
                StudentsRepresentation.getInstance().getStudents();
            }
        });
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setSpacing(5);
        hPanel.add(getButton);
        hPanel.add(cancelButton);
        hPanel.add(acceptButton);
        layout.setWidget(3, 1, hPanel);
        cellFormatter.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        setWidget(layout);
        nameTextBox.setFocus(true);
    }

    private void showRegisteredialog() {
    }

    private void sendData() {
        if ("".equals(nameTextBox.getText())) {
            ValidationErrorDialogBox validationErrorDialogBox = new ValidationErrorDialogBox(aulaApplication);
        } else {
            CoursesRepresentation.getInstance().postCourses(getCourses());
        }
    }

    private List<Course> getCourses() {
        List<Course> courses = new ArrayList<Course>();
        Course course = new Course();
        course.setDescription(descriptionTextBox.getText());
        courses.add(course);
        return courses;
    }

    @Override
    public void processStudent(StudentChangeEvent event) {
        Student student = event.getStudent();
        GWT.log("student: " + student.getName(), null);
    }
}
