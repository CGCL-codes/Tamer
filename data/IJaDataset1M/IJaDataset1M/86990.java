package rallyws;

import rallyws.api.*;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.axis.client.Stub;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import rallywidget.ListWidgetItem;

public class RallyService {

    private User user;

    private RallyService_PortType rallyService;

    static final String RALLY_TASK_URL = "https://rally1.rallydev.com/slm/detail/tk/";

    private static DateTimeFormatter jodaFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Starts a Rally session
     * @param username
     * @param password
     * @throws ServiceException
     * @throws RemoteException
     */
    public void login(String username, String password) throws RemoteException, ServiceException {
        rallyService = new RallyServiceServiceLocator().getRallyService();
        Stub stub = (Stub) rallyService;
        stub.setUsername(username);
        stub.setPassword(password);
        stub.setMaintainSession(true);
        user = (User) rallyService.getCurrentUser();
    }

    /**
     * returns the current logged in user
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the users display name
     * @return
     */
    public String getDisplayName() {
        return user == null ? null : user.getDisplayName();
    }

    /**
     * Some useful filter criteria:  Iteration, State, Blocked, WorkProduct (aka parent story/defect),
     * @param filterCriteria
     * @return
     * @throws RemoteException
     */
    @SuppressWarnings({ "unchecked" })
    public List<Task> getTasks(String filterCriteria, String sortOrder) throws RemoteException {
        if (user == null) {
            return Collections.emptyList();
        }
        String queryString = "(Owner = " + user.getLoginName() + ")";
        if (filterCriteria != null) {
            queryString = "(" + filterCriteria + " and " + queryString + ")";
        }
        QueryResult queryResult = rallyService.query(null, "Task", queryString, sortOrder, true, 0, 100);
        ArrayList<Task> list = new ArrayList<Task>();
        for (DomainObject domainObject : queryResult.getResults()) {
            list.add((Task) domainObject);
        }
        return list;
    }

    /**
     * Get total remaining work hours (To Do)  for the current iteration
     * @param projectName
     * @return
     * @throws RemoteException
     */
    public Double getTotalToDo(String projectName) throws RemoteException {
        Double total = 0.0;
        Iteration iteration = getCurrentIteration(projectName);
        List<Task> taskList = getTasks("((State != Completed) and (Iteration.ObjectID = " + iteration.getObjectID() + "))", null);
        for (Task task : taskList) {
            total += task.getToDo();
        }
        return total;
    }

    /**
     *
     * @param projectName
     * @return time (hours)  remaining in the sprint
     * @throws RemoteException
     */
    public Double getRemainingTime(String projectName) throws RemoteException {
        Iteration iteration = getCurrentIteration(projectName);
        Calendar today = new GregorianCalendar();
        int daysRemaining = (iteration.getEndDate().get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR));
        if (daysRemaining < 0) {
            daysRemaining += today.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return (double) daysRemaining * 8;
    }

    /**
     * Get stories for the specified filter criteria
     * @param queryString
     * @return
     * @throws RemoteException
     */
    public List<HierarchicalRequirement> getStories(String queryString) throws RemoteException {
        QueryResult queryResult = rallyService.query(null, "HierarchicalRequirement", queryString, "Rank", true, 0, 100);
        ArrayList<HierarchicalRequirement> list = new ArrayList<HierarchicalRequirement>();
        for (DomainObject domainObject : queryResult.getResults()) {
            list.add((HierarchicalRequirement) domainObject);
        }
        return list;
    }

    /**
     * Get projects for the specified filter criteria
     * @param queryString
     * @return
     * @throws RemoteException
     */
    public List<Project> getProjects(String queryString) throws RemoteException {
        QueryResult queryResult = rallyService.query(null, "Project", queryString, "Name", true, 0, 100);
        ArrayList<Project> list = new ArrayList<Project>();
        for (DomainObject domainObject : queryResult.getResults()) {
            Project project = (Project) domainObject;
            list.add(project);
        }
        return list;
    }

    /**
     * Get the current iteration for the specified project
     * @param projectName
     * @return
     * @throws RemoteException
     */
    public Iteration getCurrentIteration(String projectName) throws RemoteException {
        String currentDate = jodaFormatter.print(System.currentTimeMillis());
        String queryString = "(((Project.Name = \"" + projectName + "\") and (StartDate <= " + currentDate + ")) and (EndDate >= " + currentDate + "))";
        QueryResult result = rallyService.query(null, "Iteration", queryString, "Name", true, 0, 100);
        if (result.getResults().length > 0) {
            return (Iteration) result.getResults()[0];
        }
        return null;
    }

    /**
     * Update  a Rally object (ie Task, Iteration, HierarchicalRequirement)
     * @param artifact
     * @throws RemoteException
     */
    public String[] updateRally(Artifact artifact) throws RemoteException {
        OperationResult result = rallyService.update(artifact);
        return result.getErrors();
    }

    /**
     * Convert task time to a string, suppresses "null"
     * @param time
     * @return
     */
    public String getTaskTimeHours(Double time) {
        if (time == null) {
            return "";
        } else {
            return String.valueOf(time) + "h";
        }
    }

    public List<ListWidgetItem> getItems(String filterCriteria) throws RemoteException {
        ArrayList<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
        List<Task> tasks = getTasks(filterCriteria, null);
        for (final Task task : tasks) {
            items.add(new ListWidgetItem() {

                public String getName() {
                    return task.getName();
                }

                public URI getUri() {
                    try {
                        return new URI(RALLY_TASK_URL + task.getObjectID());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return items;
    }
}
