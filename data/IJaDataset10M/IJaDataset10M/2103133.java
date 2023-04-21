package com.leclercb.taskunifier.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.TaskList;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.api.models.beans.TaskListBean.TaskItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TaskListBean implements Cloneable, Serializable, Iterable<TaskItemBean> {

    @XStreamAlias("tasklist")
    private List<TaskItemBean> tasks;

    public TaskListBean() {
        this.tasks = new ArrayList<TaskItemBean>();
    }

    @Override
    protected TaskListBean clone() {
        TaskListBean list = new TaskListBean();
        list.tasks.addAll(this.tasks);
        return list;
    }

    @Override
    public Iterator<TaskItemBean> iterator() {
        return this.tasks.iterator();
    }

    public List<TaskItemBean> getList() {
        return new ArrayList<TaskItemBean>(this.tasks);
    }

    public void add(TaskItemBean item) {
        CheckUtils.isNotNull(item);
        this.tasks.add(item);
    }

    public void addAll(Collection<TaskItemBean> items) {
        if (items == null) return;
        for (TaskItemBean item : items) this.add(item);
    }

    public void remove(TaskItemBean item) {
        CheckUtils.isNotNull(item);
        this.tasks.remove(item);
    }

    public void clear() {
        for (TaskItemBean item : this.getList()) this.remove(item);
    }

    public int size() {
        return this.tasks.size();
    }

    public int getIndexOf(TaskItemBean item) {
        return this.tasks.indexOf(item);
    }

    public TaskItemBean get(int index) {
        return this.tasks.get(index);
    }

    public TaskList toTaskGroup() {
        TaskList list = new TaskList();
        for (TaskItemBean item : this) list.add(item.toTaskItem());
        return list;
    }

    @XStreamAlias("taskitem")
    public static class TaskItemBean {

        @XStreamAlias("task")
        private ModelId task;

        @XStreamAlias("link")
        private String link;

        public TaskItemBean() {
            this(null, null);
        }

        public TaskItemBean(ModelId task, String link) {
            this.setTask(task);
            this.setLink(link);
        }

        public ModelId getTask() {
            return this.task;
        }

        public void setTask(ModelId task) {
            this.task = task;
        }

        public String getLink() {
            return this.link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public TaskItem toTaskItem() {
            if (this.task == null) return new TaskItem(null, this.link);
            Task task = TaskFactory.getInstance().get(this.task);
            if (task == null) task = TaskFactory.getInstance().createShell(this.task);
            return new TaskItem(task, this.link);
        }
    }
}
