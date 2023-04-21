package ru.ssau.university.web.beans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import ru.ssau.university.ejb.api.UniversityServiceLocal;
import ru.ssau.university.persistence.entity.Faculty;
import ru.ssau.university.persistence.entity.Group;
import ru.ssau.university.persistence.entity.Student;
import ru.ssau.university.web.helper.ListWrapper;
import ru.ssau.university.web.util.WebUtil;

@ManagedBean(name = "groupsBean")
@SessionScoped
public class GroupsBean {

    @EJB
    private UniversityServiceLocal universityService;

    private ListWrapper<Faculty> faculties = new ListWrapper<Faculty>();

    private ListWrapper<Group> groups = new ListWrapper<Group>();

    private ListWrapper<Student> students = new ListWrapper<Student>();

    private String newbieName;

    private String command;

    private boolean editMode = false;

    public String initAll() {
        updateFaculties();
        updateGroups();
        updateStudents();
        if ("edit".equals(command)) {
            if (!WebUtil.getAuthBean().isLoggedInAsAdmin()) {
                WebUtil.putAccessDeniedError();
                return "pretty:welcome";
            }
            editMode = true;
        }
        return null;
    }

    public String save() {
        for (Student s : students.getItems()) {
            universityService.saveOrUpdateStudent(s);
        }
        if (newbieName != null && newbieName.length() > 0) {
            System.out.println("saving newbie!");
            Student newbie = new Student();
            Group group = universityService.getGroupById(groups.getId());
            newbie.setGroup(group);
            newbie.setFullName(newbieName);
            universityService.saveOrUpdateStudent(newbie);
            newbieName = null;
        }
        initAll();
        return null;
    }

    public String deleteStudent() {
        System.out.println(students.getDataTable());
        Student student = (Student) students.getDataTable().getRowData();
        universityService.deleteStudent(student.getId());
        initAll();
        return null;
    }

    public String finishEdit() {
        this.editMode = false;
        return null;
    }

    private void updateStudents() {
        if (groups.getId() != null) {
            students.setItems(universityService.getStudentsByGroup(groups.getId()));
        }
    }

    private void updateGroups() {
        if (faculties.getId() != null) {
            groups.setItems(universityService.getGroupsByFacultyId(faculties.getId()));
        }
    }

    private void updateFaculties() {
        faculties.setItems(universityService.getFaculties());
    }

    public ListWrapper<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(ListWrapper<Faculty> faculties) {
        this.faculties = faculties;
    }

    public ListWrapper<Group> getGroups() {
        return groups;
    }

    public void setGroups(ListWrapper<Group> groups) {
        this.groups = groups;
    }

    public ListWrapper<Student> getStudents() {
        return students;
    }

    public void setStudents(ListWrapper<Student> students) {
        this.students = students;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getNewbieName() {
        return newbieName;
    }

    public void setNewbieName(String newbieName) {
        this.newbieName = newbieName;
    }
}
