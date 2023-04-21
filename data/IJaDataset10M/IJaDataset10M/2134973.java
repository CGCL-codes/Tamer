package database.exceptions;

public class ClassNameInUseException extends Exception {

    private static final long serialVersionUID = 4491272627273431793L;

    public String className;

    public long teacherID;

    public ClassNameInUseException(long teacher_id, String class_name) {
        className = class_name;
        teacherID = teacher_id;
    }

    public long getTeacherID() {
        return teacherID;
    }

    public String getClassName() {
        return className;
    }
}
