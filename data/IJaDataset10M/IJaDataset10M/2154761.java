package icescrum2.service;

import icescrum2.dao.model.IProduct;
import icescrum2.dao.model.IStory;
import icescrum2.dao.model.ITask;
import icescrum2.dao.model.IUser;
import icescrum2.dao.model.impl.ProductBacklogItem;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public interface TaskService {

    public static final int ACTION_VALIDATE = 0;

    public static final int ACTION_ERROR = 1;

    public static final int SAME_NAME = 2;

    public static final int TODO_ZERO = 3;

    public static final int TODO_NOT_NUM = 4;

    public abstract int saveTask(ITask _task, IStory _pbi, IProduct pb, IUser u, String estimation);

    public abstract int updateTask(ITask _task, IUser u, IProduct p, String estimation);

    @SuppressWarnings("unchecked")
    public abstract boolean assignToTasks(Collection tasks, IUser user, IProduct pb);

    public abstract boolean assignToTask(ITask tasks, IUser user);

    public abstract boolean deleteTask(ITask _task, IStory _pbi, IProduct pb, IUser u);

    public abstract boolean estimateTask(ITask _task, IUser user, IProduct p, IStory pbi, Integer estimation);

    @SuppressWarnings("unchecked")
    public abstract boolean deleteTasks(Collection tasks, IStory _pbi, IUser user, IProduct pb);

    @SuppressWarnings("unchecked")
    public abstract boolean deleteTasks(Map pbiTotasks, IUser user, IProduct pb);

    public abstract void moveIncompleteTasksTo(IStory _pbiFrom, IStory _pbiTo);

    public abstract int changeState(ITask t, Integer i, IUser u);

    public List<ITask> filterByAssignedToMe(List<ITask> tasks, IUser user);

    public List<ITask> filterByNobodyAssigned(List<ITask> tasks);

    public ITask getTask(int id);

    public void moveFinishTasks(List pbis, ProductBacklogItem storyThisSprint);
}
