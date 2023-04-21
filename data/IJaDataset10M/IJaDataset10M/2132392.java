package Server;

import java.util.Vector;
import TransmitterS.Course;
import TransmitterS.CourseCenter;
import TransmitterS.Pupil;

/**
 * Zugriffsklasse auf Kurse.
 * 
 * @author LK13
 * 
 */
public class CourseCenterImp implements CourseCenter {

    public final ServerImp myServer;

    public CourseCenterImp(ServerImp _myServer) {
        myServer = _myServer;
    }

    public CourseCenterImp() {
        myServer = null;
    }

    public Vector<Course> getAllCourses() {
        return myServer.allCourses;
    }

    public void addCourse(String courseName) {
        myServer.allCourses.add(new CourseImp(courseName, myServer));
    }

    public void delCourse(Integer courseID) {
        try {
            for (Pupil pupil : getCourse(courseID).getPupils()) {
                pupil.getCourses().remove(getCourse(courseID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myServer.allCourses.remove(courseID.intValue());
    }

    public void addUser(Integer courseID, Integer userID) {
        Pupil user = (Pupil) myServer.allUsers.get(userID);
        if (user instanceof Pupil) {
            try {
                getCourse(courseID).getPupils().add(user);
                (user).getCourses().add(getCourse(courseID));
            } catch (Exception e) {
                System.err.println("could not add pupil to course");
                e.printStackTrace();
            }
        }
    }

    public void delUser(Integer courseID, Integer userID) {
        Pupil user = (Pupil) myServer.allUsers.get(userID);
        if (user instanceof Pupil) {
            try {
                getCourse(courseID).getPupils().remove(user);
                user.getCourses().remove(getCourse(courseID));
            } catch (Exception e) {
                System.err.println("could not remove pupil from course");
                e.printStackTrace();
            }
        }
    }

    public Course getCourse(Integer courseID) {
        return myServer.allCourses.get(courseID);
    }
}
