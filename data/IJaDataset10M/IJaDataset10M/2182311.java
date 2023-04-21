package uk.co.q3c.deplan.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import uk.co.q3c.deplan.domain.ObjectIdGenerator;
import uk.co.q3c.deplan.domain.resource.IndividualResource;
import uk.co.q3c.deplan.domain.resource.Resource;
import uk.co.q3c.deplan.domain.task.Project;
import uk.co.q3c.deplan.domain.task.ResourcedTask;
import uk.co.q3c.deplan.domain.task.SummaryTask;
import uk.co.q3c.deplan.domain.task.Task;
import uk.co.q3c.deplan.domain.task.TaskResourceProfile;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * @see ResourceDao_db4o
 * @author DSowerby 8 Jun 2008 08:07:51
 * 
 */
public class TaskDao_db4o extends GeneralDao_db4o implements TaskDao {

    protected final transient Logger logger = Logger.getLogger(getClass().getName());

    public TaskDao_db4o(ObjectContainer oodb) {
        super(oodb);
    }

    /**
	 * Finds the first task with the given id (there should only be one of
	 * course, but if there were more than one this method would ignore the
	 * others, but log a warning.
	 * 
	 * Returns null if the task not found
	 */
    @SuppressWarnings("unchecked")
    public Task findById(final int criteria) {
        List list = oodb.query(new Predicate() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public boolean match(Task candidate) {
                return candidate.getTaskId() == criteria;
            }
        });
        if (list.size() > 0) {
            if (list.size() > 1) {
                logger.warn("More than one task with id of " + criteria);
            }
            Task task = (Task) list.get(0);
            return task;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Task> findByName(final String criteria) {
        if (criteria == null) {
            return null;
        }
        List<Task> list = oodb.query(new Predicate() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public boolean match(Task candidate) {
                return criteria.equals(candidate.getName());
            }
        });
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Task> orphans() {
        List<Task> list = oodb.query(new Predicate() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public boolean match(Task candidate) {
                return candidate.getParentTask() == null;
            }
        });
        ArrayList<Task> checkList = new ArrayList<Task>(list);
        Task mp = findFirstByName("Master Plan");
        checkList.remove(mp);
        ArrayList<Task> finalList = new ArrayList<Task>();
        for (Task o : checkList) {
            if (o instanceof SummaryTask) {
                SummaryTask t = (SummaryTask) o;
                finalList.addAll(t.getSubTasks());
            }
            finalList.add(o);
        }
        return finalList;
    }

    @Override
    public Task findFirstByName(String name) {
        List<Task> list = findByName(name);
        if (list != null) {
            if (list.size() > 0) {
                Task task = list.get(0);
                return task;
            }
        }
        return null;
    }

    public void save(Task task) {
        oodb.store(task);
    }

    public void deleteAll() {
        ObjectSet result = oodb.queryByExample(Task.class);
        while (result.hasNext()) {
            oodb.delete(result.next());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Task> loadAll() {
        List<Task> list = oodb.queryByExample(Task.class);
        return list;
    }

    public void save(Collection<Task> taskList) {
        oodb.store(taskList);
    }

    @SuppressWarnings("unchecked")
    public List<Project> loadAllProjects() {
        List<Project> list = oodb.queryByExample(Project.class);
        return list;
    }

    @Override
    public int nextTaskId() {
        return ObjectIdGenerator.nextTaskId();
    }

    @Override
    public int peekTaskId() {
        return ObjectIdGenerator.peekTaskId();
    }

    @SuppressWarnings({ "unchecked", "serial" })
    @Override
    public List<ResourcedTask> usingResource(final Resource resource) {
        List<ResourcedTask> list = oodb.query(new Predicate() {

            @SuppressWarnings("unused")
            public boolean match(ResourcedTask candidate) {
                if (resource instanceof IndividualResource) {
                    boolean result = candidate.getAssignedResource().uses((IndividualResource) resource);
                    return result;
                } else {
                    boolean result = candidate.getAssignedResource() == resource;
                    return result;
                }
            }
        });
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TaskResourceProfile> loadProfiles() {
        List<TaskResourceProfile> list = oodb.queryByExample(TaskResourceProfile.class);
        return list;
    }

    @Override
    public void save(TaskResourceProfile taskResourceProfile) {
        oodb.store(taskResourceProfile);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ResourcedTask> loadResourcedTasks() {
        List<ResourcedTask> list = oodb.queryByExample(ResourcedTask.class);
        return list;
    }
}
