package Admintool;

import Controller.Controller;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.ArrayList;
import Module.*;
import Shared.Callback;
import Shared.Server;

/**
 *
 * @author thorerikmidtb.
 * "UnicastRemoteObject is used for exporting a remote object with JRMP and obtaining a stub that communicates to the remote object".
 * From http://download.oracle.com/javase/1.5.0/docs/api/java/rmi/server/UnicastRemoteObject.html
 * Listens to RMI requests and implements the interface which is used by the client to invoke remote methods.
 *
 */
public class ServerImpl extends UnicastRemoteObject implements Server {

    Controller controller = new Controller();

    static final String IP = "127.0.0.1";

    static final String NAMING = "//127.0.0.1/ServerImpl";

    private ArrayList<Callback> userList = new ArrayList<Callback>();

    /**
     * Empty constructor.
     * @throws RemoteException
     */
    public ServerImpl() throws RemoteException {
    }

    public synchronized int registerProject(String name, String description, String goals) throws RemoteException {
        Project project = new Project(0, name, description, goals);
        return controller.getProject().registerProject(project);
    }

    public synchronized void editProject(int id, String name, String description, String goals) throws RemoteException {
        controller.getProject().editProject(new Project(id, name, description, goals));
    }

    public synchronized void deleteProject(int id) throws RemoteException {
        Project project = new Project(id, "", "", "");
        controller.getProject().deleteProject(project);
    }

    public ArrayList<Module.Project> getAllProjects() throws RemoteException {
        return controller.getProject().getProjects();
    }

    public ArrayList<Module.Project> getAllProjectsToUser(String user) throws RemoteException {
        System.out.println("Brukernavn " + user);
        System.out.println("ER EG HER I SERVERIMPL?");
        return controller.getUser().getProjectForUser(controller.getUser().getUserForUsername(user));
    }

    public synchronized void registerActivity(int projectId, String startDate, String endDate, String responsible, String status, String description, String goals, String location, boolean onActionList) throws RemoteException {
        Activity activity = new Activity(0, projectId, startDate, endDate, responsible, status, description, goals, location, onActionList);
        controller.getActivity().registerActivity(activity);
    }

    public synchronized void editActivity(int id, int projectId, String startDate, String endDate, String responsible, String status, String description, String goals, String location, boolean onActionList) throws RemoteException {
        Activity activity = new Activity(id, projectId, startDate, endDate, responsible, status, description, goals, location, onActionList);
        controller.getActivity().editActivity(activity);
    }

    public synchronized void deleteActivity(int id) throws RemoteException {
        Activity activity = new Activity(id);
        controller.getActivity().deleteActivity(activity);
    }

    public ArrayList<Module.Activity> getAllActivities(int projectId) {
        Project p = new Project(projectId, "", "", "");
        ArrayList<Module.Activity> activity = new ArrayList<Module.Activity>();
        activity = controller.getActivity().getActivitysToProject(p);
        return activity;
    }

    public synchronized void registerSession(String name, String date, String place, int projectId, String startTime) throws RemoteException {
        Session session = new Session(0, projectId, name, date, place, startTime, "");
        controller.getSession().registerSession(session);
    }

    public synchronized boolean loginn(String username, String password) throws RemoteException {
        System.out.println("før controller");
        boolean b = controller.getUser().logInn(username, password);
        System.out.println(b);
        return b;
    }

    public synchronized void editSession(int id, int projectId, String name, String date, String place, String startTime) throws RemoteException {
        Session session = new Session(id, projectId, name, date, place, startTime, "");
        controller.getSession().editSession(session);
    }

    public synchronized void deleteSession(int id) throws RemoteException {
        Session s = new Session(id);
    }

    public synchronized Module.User getUserForUsername(String username) throws RemoteException {
        return controller.getUser().getUserForUsername(username);
    }

    public ArrayList<Module.Session> getAllSessionsToProject(int id) throws RemoteException {
        Project project = new Project(id, "", "", "");
        return controller.getSession().getSessionsToProject(project);
    }

    public Module.User getFacilitatorForSession(int sessionId) throws RemoteException {
        return controller.getSession().getFasilitatorToSession(sessionId);
    }

    public Module.User getSecretaryForSession(int sessionId) throws RemoteException {
        return controller.getSession().getSecretariesToSession(sessionId);
    }

    public ArrayList<Module.User> getExpertsForSession(int sessionId) throws RemoteException {
        return controller.getSession().getExpertsToSession(sessionId);
    }

    public Module.User getProjectLeader(int sessionId) throws RemoteException {
        return controller.getSession().getProjectLeaderToSession(sessionId);
    }

    public ArrayList<Module.User> getProjectParticipants(int projectId) throws RemoteException {
        return controller.getProject().getUsersToProject(projectId);
    }

    public ArrayList<Module.User> getAllUsers() throws RemoteException {
        return controller.getUser().getAllUsers();
    }

    public ArrayList<Module.User> getProjectLeader() throws RemoteException {
        return controller.getUser().getProjectLeaders();
    }

    public ArrayList<Module.User> getSecretaries() throws RemoteException {
        return controller.getUser().getSecretaries();
    }

    public ArrayList<Module.User> getFasilitator() throws RemoteException {
        return controller.getUser().getFasilitator();
    }

    public synchronized void registerUser(String username, String password) throws RemoteException {
        User user = new User(0, username, password);
        controller.getUser().registerUser(user);
    }

    public synchronized void registerUserToProject(int projectId, ArrayList<String> username) throws RemoteException {
        ArrayList<User> users = new ArrayList<User>();
        username.remove("null");
        username.remove(null);
        for (int i = 0; i < username.size(); i++) {
            users.add(controller.getUser().getUserForUsername(username.get(i)));
        }
        controller.getProject().setUsersToProject(users, projectId);
    }

    public synchronized void registerUserToSession(int sessionId, ArrayList<String> users, int roleId) throws RemoteException {
        ArrayList<User> u = new ArrayList<User>();
        for (int i = 0; i < users.size(); i++) {
            if (controller.getUser().getUserForUsername(users.get(i)).getId() != 0) {
                u.add(controller.getUser().getUserForUsername(users.get(i)));
            }
        }
        controller.getSession().setUsersToSession(sessionId, u, roleId);
    }

    public synchronized void editUser(int id, String username, String password) throws RemoteException {
        User user = new User(id, username, password);
        controller.getUser().editUser(user);
    }

    public synchronized void broadcast(String message, Callback c) throws RemoteException {
        if (!message.trim().isEmpty()) {
            for (int i = 0; i < userList.size(); i++) {
                Callback user = userList.get(i);
                user.broadcastMessage(message, c.getUserName());
            }
        }
    }

    public synchronized void connect(Callback c) throws RemoteException {
        userList.add(c);
        for (int g = 0; g < userList.size(); g++) {
            Callback user = userList.get(g);
            user.connectMessage(c.getUserName());
        }
        getClientList();
    }

    public synchronized void quit(Callback c) throws RemoteException {
        userList.remove(c);
        for (int i = 0; i < userList.size(); i++) {
            Callback user = userList.get(i);
            user.quitMessage(c.getUserName());
        }
        getClientList();
    }

    public synchronized void getClientList() throws RemoteException {
        ArrayList<String> newList = new ArrayList<String>();
        for (int i = 0; i < userList.size(); i++) {
            newList.add(userList.get(i).getUserName());
        }
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).getClientList(newList);
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.rmi.server.hostname", IP);
        try {
            LocateRegistry.createRegistry(1099);
            ServerImpl server = new ServerImpl();
            Naming.rebind(NAMING, server);
            javax.swing.JOptionPane.showMessageDialog(null, "If you push this button down here, you WILL TERMINATE THIS SERVER!");
            Naming.unbind(NAMING);
            System.exit(0);
        } catch (MalformedURLException mal) {
            System.out.println("UH OH! We got an error here(1): " + mal);
        } catch (RemoteException re) {
            System.out.println("UH OH! We got an error here(2): " + re);
        } catch (NotBoundException bound) {
            System.out.println("UH OH! We got an error here(3): " + bound);
        } catch (Exception e) {
            System.out.println("UH OH! We got an error here(4): " + e);
        }
    }
}
