package wizworld.navigate.course;

/** Course event
 * @author (c) Stephen Denham 2003
 * @version 0.1
 */
public class CourseEvent extends wizworld.lang.Event {

    /** Course update */
    public static final int COURSE_UPDATE = 0;

    public CourseEvent(Object source, int id) {
        super(source, id, source);
    }
}
