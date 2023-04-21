package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import TransmitterS.NetworkCenter;
import TransmitterS.Course;
import TransmitterS.CourseCenter;
import TransmitterS.Group;
import TransmitterS.GroupCenter;
import TransmitterS.Job;
import TransmitterS.JobCenter;
import TransmitterS.SolutionCenter;
import TransmitterS.User;
import TransmitterS.UserCenter;

/**
 * @author LK13
 */
public class ServerImp implements TransmitterS.Server {

    /**
	 * der Server. Beim Start des Programms werden die Vektoren "allUsers",
	 * "allGroups", "allCourses" und "allJobs" sowie das Job-, das Solution-,
	 * Group-, Course- und das UserCenter und APIandParser angelegt.
	 */
    private static final long serialVersionUID = 8324592760094718447L;

    public Vector<User> allUsers;

    public Vector<Group> allGroups;

    public Vector<Course> allCourses;

    public Vector<Job> allJobs;

    public transient JobCenterImp myJobCenter;

    public transient SolutionCenterImp mySolutionCenter;

    public transient GroupCenterImp myGroupCenter;

    public transient NetworkCenterImp myNetworkCenter;

    public transient CourseCenterImp myCourseCenter;

    public transient UserCenterImp myUserCenter;

    /**
	 * Start Erstellung von Userliste, Gruppeliste und Kursliste Erstellung der
	 * User "admin" (Lehrer), "test", "kenny" (Sch�ler) mit den Passw�rtern
	 * "admin", "test", "dead" Erstellung des Kurses "Testkurs" mit "test" und
	 * "kenny"
	 */
    public ServerImp() {
        load();
        try {
            allUsers = new Vector<User>();
            allGroups = new Vector<Group>();
            allCourses = new Vector<Course>();
            allJobs = new Vector<Job>();
            myUserCenter.addTeacher("admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        myJobCenter = new JobCenterImp(this);
        mySolutionCenter = new SolutionCenterImp(this);
        myGroupCenter = new GroupCenterImp(this);
        myNetworkCenter = new NetworkCenterImp(this);
        myCourseCenter = new CourseCenterImp(this);
        myUserCenter = new UserCenterImp(this);
        try {
            JobCenter _myJobCenter = (JobCenter) UnicastRemoteObject.exportObject(myJobCenter, 0);
            SolutionCenter _mySolutionCenter = (SolutionCenter) UnicastRemoteObject.exportObject(mySolutionCenter, 0);
            GroupCenter _myGroupCenter = (GroupCenter) UnicastRemoteObject.exportObject(myGroupCenter, 0);
            CourseCenter _myCourseCenter = (CourseCenter) UnicastRemoteObject.exportObject(myCourseCenter, 0);
            UserCenter _myUserCenter = (UserCenter) UnicastRemoteObject.exportObject(myUserCenter, 0);
            NetworkCenter _myNetworkCenter = (NetworkCenter) UnicastRemoteObject.exportObject(myNetworkCenter, 0);
            Registry registry = LocateRegistry.getRegistry(Network.port);
            registry.bind("JobCenter", _myJobCenter);
            registry.bind("SolutionCenter", _mySolutionCenter);
            registry.bind("GroupCenter", _myGroupCenter);
            registry.bind("CourseCenter", _myCourseCenter);
            registry.bind("UserCenter", _myUserCenter);
            registry.bind("NetworkCenter", _myNetworkCenter);
            Network.debugMsg("Server geladen!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void unload() {
        try {
            Registry registry = LocateRegistry.getRegistry(Network.port);
            registry.unbind("JobCenter");
            registry.unbind("SolutionCenter");
            registry.unbind("GroupCenter");
            registry.unbind("CourseCenter");
            registry.unbind("UserCenter");
            registry.unbind("NetworkCenter");
            Network.debugMsg("Server aufger�umt!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
