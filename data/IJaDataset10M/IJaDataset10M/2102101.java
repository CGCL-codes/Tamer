package net.sf.unruly.mock.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collection;
import java.util.List;

/**
 * @author Jeff Drost
 */
public class Classroom extends TestPropertyChangeSource {

    private static final Log LOG = LogFactory.getLog(Classroom.class);

    private Person teacher;

    private List<Person> students;

    private Collection<String> bookTitles;

    private int roomNumber;

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        changeSupport.firePropertyChange("teacher", this.teacher, teacher);
        this.teacher = teacher;
    }

    public List<Person> getStudents() {
        return students;
    }

    public void setStudents(List<Person> students) {
        changeSupport.firePropertyChange("students", this.students, students);
        this.students = students;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        LOG.debug("setRoomNumber " + roomNumber);
        changeSupport.firePropertyChange("roomNumber", this.roomNumber, roomNumber);
        this.roomNumber = roomNumber;
    }

    public Collection<String> getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(Collection<String> bookTitles) {
        LOG.debug("setBookTitles " + bookTitles);
        changeSupport.firePropertyChange("bookTitles", this.bookTitles, bookTitles);
        this.bookTitles = bookTitles;
    }
}
