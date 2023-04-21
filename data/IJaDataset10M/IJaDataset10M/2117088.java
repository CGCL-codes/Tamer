package database;

public class ClassRecord {

    private String className;

    private long teacherID;

    public void setClassName(String name) {
        className = name;
    }

    public void setTeacherID(long id) {
        teacherID = id;
    }

    public String getClassName() {
        return className;
    }

    public Long getTeacherID() {
        return teacherID;
    }
}
