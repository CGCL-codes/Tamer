package ipmss.dwr;

import java.util.List;
import java.util.Properties;
import ipmss.entity.planning.Project;
import ipmss.entity.planning.Task;
import ipmss.security.Authorities;
import ipmss.services.common.UserContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;

/**
 * The Class ScheduleRemoteBean.
 *
 * @author Michał Czarnik
 */
@Named
@Scope("session")
@Secured({ Authorities.ROLE_SCHEDULE_READ })
public class ScheduleRemoteBean {

    /**
     * Instantiates a new schedule remote bean.
     */
    public ScheduleRemoteBean() {
    }

    /** The user context. */
    @Inject
    private UserContext userContext;

    /**
     * Gets the projects count.
     *
     * @return the projects count
     */
    public int getProjectsCount() {
        return userContext.getProjects().size();
    }

    /**
     * Gets the project.
     *
     * @param index the index
     * @return the project
     */
    public Properties getProject(int index) {
        Project p = userContext.getProjects().get(index);
        Properties prop = new Properties();
        prop.put("name", p.getName());
        prop.put("id", p.getId());
        return prop;
    }

    /**
     * Gets the projects.
     *
     * @return the projects
     */
    public Properties getProjects() {
        Properties prop = new Properties();
        int i = 0;
        for (Project project : userContext.getProjects()) {
            Properties prp = new Properties();
            prp.put("name", project.getName());
            prp.put("id", project.getId());
            prp.put("schedule", taskToProperties(project.getSchedule(), new Properties()));
            prop.put("project" + i, prp);
            i++;
        }
        prop.put("projectsCount", i);
        return prop;
    }

    /**
     * Test.
     *
     * @return the string
     */
    public String test() {
        return "SheduleRemoteBean test message";
    }

    /**
     * Task to properties.
     *
     * @param task the task
     * @param prop the prop
     * @return the properties
     */
    private Properties taskToProperties(Task task, Properties prop) {
        prop.put("id", task.getId());
        prop.put("name", task.getName());
        if (task.getStartDate() != null) prop.put("startDate", task.getStartDate());
        if (task.getEndDate() != null) prop.put("endDate", task.getEndDate());
        prop.put("duration", task.getDuration());
        prop.put("estimatedEffort", task.getEstimatedEffort());
        prop.put("percentageComplete", task.getPercetageComplete());
        prop.put("realizationEffort", task.getRealizationEffort());
        if (task.getTasks() != null) prop.put("childTaskSize", task.getTasks().size()); else prop.put("childTaskSize", 0);
        prop.put("preTaskId", task.getPreTaskId());
        int index = 0;
        if (task.getTasks() != null) for (Task t : task.getTasks()) {
            prop.put("task" + index, taskToProperties(t, new Properties()));
            index++;
        }
        return prop;
    }
}
