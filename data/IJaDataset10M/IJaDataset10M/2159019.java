package com.velocityme.session;

import com.velocityme.entity.ChangeDelta;
import com.velocityme.entity.Node;
import com.velocityme.entity.Project;
import com.velocityme.entity.State;
import com.velocityme.entity.Activity;
import com.velocityme.entity.Contactable;
import com.velocityme.entity.DependencyLink;
import com.velocityme.entity.Task;
import com.velocityme.entity.Transition;
import com.velocityme.enums.ChangeDeltaItemType;
import com.velocityme.enums.ChangeDeltaType;
import com.velocityme.enums.Permission;
import com.velocityme.utility.InvalidKeyException;
import com.velocityme.utility.PermissionDeniedException;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rcrida
 */
@Stateless
public class TaskBean implements TaskLocal {

    @EJB
    private NodeLocal nodeBean;

    @PersistenceContext
    private EntityManager em;

    State m_startState;

    @PostConstruct
    public void initialize() {
        m_startState = (State) em.createNamedQuery("findStartState").getSingleResult();
    }

    public boolean isTaskActive(KeyLocal key, Activity activity) {
        if (key.isValid()) {
            if (activity.getState() != null) return !activity.getState().equals(m_startState); else return false;
        } else throw new InvalidKeyException();
    }

    public Set<Transition> getLegalTransitions(KeyLocal key, Long activityId) {
        Activity activity = em.find(Activity.class, activityId);
        key.assertHasPermission(activity, Permission.TASK_CHANGE_STATE);
        return new HashSet<Transition>((List<Transition>) em.createNamedQuery("findTransitionBySourceStateAndTaskStateMachine").setParameter("sourceState", activity.getState()).setParameter("taskStateMachine", activity.getTaskStateMachine()).getResultList());
    }

    public Date getTaskLastStateChangeTime(KeyLocal key, Long activityId) {
        Activity activity = em.find(Activity.class, activityId);
        key.assertHasPermission(activity, Permission.TASK_CHANGE_STATE);
        Date lastTime = new Date(0L);
        for (ChangeDelta changeDelta : (List<ChangeDelta>) em.createNamedQuery("findChangeDeltaByNode").setParameter("node", activity).getResultList()) {
            if (changeDelta.getChangeDeltaType().equals(ChangeDeltaType.CHANGE_STATE)) {
                if (changeDelta.getCreationTime().after(lastTime)) {
                    lastTime = changeDelta.getCreationTime();
                }
            }
        }
        return lastTime;
    }

    public void recursiveUpdateTimes(KeyLocal key, Long taskId) {
        Task task = em.find(Task.class, taskId);
        key.assertHasPermission(task, Permission.PROJECT_EDIT);
        if (task instanceof Project) {
            Date now = new Date();
            sortOutGraph(task, now.getTime());
        } else throw new PermissionDeniedException();
    }

    private Project getEncapsulatingProject(Task task) {
        Project project = null;
        Node parentNode = task.getParentNode();
        while (parentNode != null) {
            if (parentNode instanceof Project) {
                project = (Project) parentNode;
                break;
            }
            parentNode = parentNode.getParentNode();
        }
        return project;
    }

    public boolean isValidDependencyPair(KeyLocal key, Task dependencyTask, Task dependantTask) {
        if (dependencyTask == null || dependantTask == null) {
            return false;
        }
        Project dependencyProject = getEncapsulatingProject(dependencyTask);
        Project dependantProject = getEncapsulatingProject(dependantTask);
        if (dependencyProject == null || dependantProject == null || !dependencyProject.equals(dependantProject)) return false;
        return true;
    }

    public void createDependency(KeyLocal key, Long dependencyTaskId, Long dependantTaskId) {
        Task dependencyTask = em.find(Task.class, dependencyTaskId);
        Task dependantTask = em.find(Task.class, dependantTaskId);
        if (isValidDependencyPair(key, dependencyTask, dependantTask)) {
            DependencyLink link = new DependencyLink(dependencyTask, dependantTask);
            em.persist(link);
            ChangeDelta changeDelta = dependencyTask.addChangeDelta(ChangeDeltaType.CREATE_DEPENDENCY, key.getUser());
            changeDelta.addItem(ChangeDeltaItemType.DEPENDANT_TASK, dependantTask.getPathName());
            em.persist(changeDelta);
            changeDelta = dependantTask.addChangeDelta(ChangeDeltaType.CREATE_DEPENDENCY, key.getUser());
            changeDelta.addItem(ChangeDeltaItemType.DEPENDENCY_TASK, dependencyTask.getPathName());
            em.persist(changeDelta);
        } else {
            throw new PermissionDeniedException();
        }
    }

    public void removeDependency(KeyLocal key, Long dependencyTaskId, Long dependantTaskId) {
        Task dependencyTask = em.find(Task.class, dependencyTaskId);
        Task dependantTask = em.find(Task.class, dependantTaskId);
        key.assertHasPermission(dependencyTask, Permission.TASK_EDIT_DEPENDENCY);
        key.assertHasPermission(dependantTask, Permission.TASK_EDIT_DEPENDENCY);
        DependencyLink link = (DependencyLink) em.createNamedQuery("findDependencyLink").setParameter("dependency", dependencyTask).setParameter("dependant", dependantTask).getSingleResult();
        em.remove(link);
        ChangeDelta changeDelta = dependencyTask.addChangeDelta(ChangeDeltaType.DELETE_DEPENDENCY, key.getUser());
        changeDelta.addItem(ChangeDeltaItemType.DEPENDANT_TASK, dependantTask.getPathName());
        em.persist(changeDelta);
        changeDelta = dependantTask.addChangeDelta(ChangeDeltaType.DELETE_DEPENDENCY, key.getUser());
        changeDelta.addItem(ChangeDeltaItemType.DEPENDENCY_TASK, dependencyTask.getPathName());
        em.persist(changeDelta);
    }

    private void sortOutGraph(Task rootTask, long now) {
    }

    public void logCreate(Task task, ChangeDelta changeDelta, boolean interested) {
        nodeBean.logCreate(task, changeDelta);
        task.setCreator(changeDelta.getUser().getPerson());
        if (interested) {
            task.addInterestedContactable(changeDelta.getUser().getPerson());
        }
        changeDelta.addItem(ChangeDeltaItemType.PRIORITY, Integer.toString(task.getPriority()));
        changeDelta.addItem(ChangeDeltaItemType.CREATOR, task.getCreator().toString());
        changeDelta.addItem(ChangeDeltaItemType.OWNER, task.getOwner().toString());
    }

    public void logEdit(Task task, Task current, ChangeDelta changeDelta) {
        nodeBean.logEdit(task, current, changeDelta);
        task.setCreator(current.getCreator());
        task.setStartDate(current.getStartDate());
        task.setStopDate(current.getStopDate());
        if (task.getPriority() != current.getPriority()) changeDelta.addItem(ChangeDeltaItemType.PRIORITY, Integer.toString(task.getPriority()));
        Collection<Contactable> interestedRemoved = new ArrayList<Contactable>(current.getInterestedContactables());
        interestedRemoved.removeAll(task.getInterestedContactables());
        Collection<Contactable> interestedAdded = new ArrayList<Contactable>(task.getInterestedContactables());
        interestedAdded.removeAll(current.getInterestedContactables());
        for (Contactable contactable : interestedRemoved) {
            changeDelta.addItem(ChangeDeltaItemType.REMOVE_INTERESTED_CONTACTABLE, contactable.toString());
        }
        for (Contactable contactable : interestedAdded) {
            changeDelta.addItem(ChangeDeltaItemType.ADD_INTERESTED_CONTACTABLE, contactable.toString());
        }
        if (!task.getOwner().equals(current.getOwner())) changeDelta.addItem(ChangeDeltaItemType.OWNER, task.getOwner().toString());
    }

    public void enableInterestedState(KeyLocal key, Task task) {
        key.validateKey();
        Contactable me = key.getPerson();
        if (!task.getInterestedContactables().contains(me)) {
            task.addInterestedContactable(me);
            ChangeDelta changeDelta = task.addChangeDelta(ChangeDeltaType.INTERESTED, key.getUser());
            changeDelta.addItem(ChangeDeltaItemType.ADD_INTERESTED_CONTACTABLE, me.toString());
            em.merge(task);
        }
    }

    public List<Task> getDependencyTasks(KeyLocal key, Task task) {
        key.validateKey();
        return em.createNamedQuery("findDependencyTasks").setParameter("task", task).getResultList();
    }

    public List<Task> getDependantTasks(KeyLocal key, Task task) {
        key.validateKey();
        return em.createNamedQuery("findDependantTasks").setParameter("task", task).getResultList();
    }

    public void reassignResponsibility(KeyLocal key, Long taskId, Long[] contactableIds, String message, File file, String contentType, InputStream stream) {
        Task task = em.find(Task.class, taskId);
        key.assertHasPermission(task, Permission.TASK_REASSIGN_RESPONSIBILITY);
        Set<Contactable> contactables = new HashSet<Contactable>();
        if (contactableIds != null) {
            for (Long id : contactableIds) {
                contactables.add(em.find(Contactable.class, id));
            }
        }
        Set<Contactable> newResponsibleContactables = new HashSet<Contactable>(contactables);
        newResponsibleContactables.removeAll(task.getResponsibleContactables());
        task.setResponsibleContactables(contactables);
        ChangeDelta changeDelta = task.addChangeDelta(ChangeDeltaType.REASSIGN_RESPONSIBILITY, key.getUser());
        changeDelta.addItem(ChangeDeltaItemType.MESSAGE, message);
        for (Contactable contactableLocal : task.getResponsibleContactables()) {
            changeDelta.addItem(ChangeDeltaItemType.RESPONSIBILITY, contactableLocal.toString());
        }
        em.persist(changeDelta);
        em.merge(task);
    }

    public void toggleInterested(KeyLocal key, Long taskId) {
        key.validateKey();
        Task task = em.find(Task.class, taskId);
        Collection<Contactable> interestedContactables = task.getInterestedContactables();
        Contactable meLocal = key.getPerson();
        ChangeDelta changeDelta;
        if (interestedContactables.contains(meLocal)) {
            interestedContactables.remove(meLocal);
            task.setInterestedContactables(interestedContactables);
            changeDelta = task.addChangeDelta(ChangeDeltaType.INTERESTED, key.getUser());
            changeDelta.addItem(ChangeDeltaItemType.REMOVE_INTERESTED_CONTACTABLE, meLocal.toString());
        } else {
            interestedContactables.add(meLocal);
            task.setInterestedContactables(interestedContactables);
            changeDelta = task.addChangeDelta(ChangeDeltaType.INTERESTED, key.getUser());
            changeDelta.addItem(ChangeDeltaItemType.ADD_INTERESTED_CONTACTABLE, meLocal.toString());
        }
        em.merge(task);
        em.persist(changeDelta);
    }
}
