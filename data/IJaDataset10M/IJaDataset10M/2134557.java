package com.yosemity.extra.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.yosemity.extra.model.TaskDescription;

public class MockCalendarTaskDaoImpl implements CalendarTaskDao {

    HashMap<Long, TaskDescription> tasks = new HashMap<Long, TaskDescription>();

    public MockCalendarTaskDaoImpl() {
        TaskDescription task = new TaskDescription();
        task.setTaskID(-1);
        task.setOwnerID(0);
        task.setActive(true);
        task.setStartTime(new Date());
        task.setSendSMS(true);
        task.setSendSMSTime(new Date());
        task.setDescription("今天要做效果演示（中文）");
        tasks.put(task.getTaskID(), task);
        TaskDescription task2 = new TaskDescription();
        task2.setTaskID(-2);
        task2.setOwnerID(0);
        task2.setActive(true);
        task2.setStartTime(new Date());
        task2.setSendSMS(true);
        task2.setSendSMSTime(new Date());
        task2.setDescription("搜集研究新应用");
        tasks.put(task2.getTaskID(), task2);
    }

    public void addTask(TaskDescription task) {
        if (task != null) tasks.put(task.getTaskID(), task);
    }

    public void deleteTask(long taskID) {
        tasks.remove(taskID);
    }

    public List<TaskDescription> getTaskOfDate(long ownerID, int shareType, Date start, Date end) {
        List<TaskDescription> result = new ArrayList<TaskDescription>();
        for (TaskDescription task : tasks.values()) {
            if (((task.getStartTime().after(start) || task.getStartTime().equals(start)) && task.getStartTime().before(end)) && task.getShareType() >= shareType) {
                result.add(task);
            }
        }
        return result;
    }

    public void saveTask(TaskDescription task) {
        tasks.put(task.getTaskID(), task);
    }
}
