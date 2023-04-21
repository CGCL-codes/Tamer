package samplepluginproject.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Team implements IXMLSerializable {

    private final Map<Integer, Student> idManager = new HashMap<Integer, Student>();

    private final List<Student> students = new LinkedList<Student>();

    public Team() {
    }

    public Student getStudent(int id) {
        return idManager.get(id);
    }

    public Student createStudent(int id) {
        Student result;
        if (!idAvailable(id)) throw new RuntimeException(id + " already exist.");
        result = new Student(id);
        addStudent(result);
        return result;
    }

    public boolean idAvailable(int id) {
        return !idManager.containsKey(id);
    }

    private void addStudent(Student student) {
        idManager.put(student.getId(), student);
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }
}
