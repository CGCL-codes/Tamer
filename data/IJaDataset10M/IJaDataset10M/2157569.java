package de.flingelli.scrum.datastructure;

import jancilla.date.DateUtils;
import java.util.Date;
import java.util.Locale;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import de.flingelli.scrum.datastructure.BacklogItem;
import de.flingelli.scrum.datastructure.EnumTaskStatusType;
import de.flingelli.scrum.datastructure.Sprint;
import de.flingelli.scrum.datastructure.Task;
import de.flingelli.scrum.datastructure.WorkingUnit;

public class TestCaseTask {

    private static final Double TIME_TO_DO_WORKING_UNIT_1 = 20.0;

    private Task mTask = null;

    private Date date;

    private WorkingUnit beginWorkingUnit;

    private WorkingUnit endWorkingUnit;

    @Before
    public void initTask() {
        mTask = new Task("Task-1");
        date = new Date();
        beginWorkingUnit = new WorkingUnit(new Date());
        beginWorkingUnit.setToDo(TIME_TO_DO_WORKING_UNIT_1);
        beginWorkingUnit.setDone(8.0);
        mTask.addWorkingUnit(beginWorkingUnit);
        WorkingUnit wu2 = new WorkingUnit(DateUtils.addDays(date, 1));
        wu2.setToDo(12.0);
        wu2.setDone(8.0);
        mTask.addWorkingUnit(wu2);
        WorkingUnit wu3 = new WorkingUnit(DateUtils.addDays(date, 2));
        wu3.setToDo(4.0);
        mTask.addWorkingUnit(wu3);
        WorkingUnit wu4 = new WorkingUnit(DateUtils.addDays(date, 3));
        wu4.setToDo(Double.NaN);
        wu4.setDone(Double.NaN);
        mTask.addWorkingUnit(wu4);
        endWorkingUnit = new WorkingUnit(DateUtils.addDays(date, 4));
        endWorkingUnit.setToDo(Double.NaN);
        mTask.addWorkingUnit(endWorkingUnit);
    }

    @Test
    public void testGetTimeToDo() {
        Assert.assertEquals(4, mTask.getTimeToDo(), 0.0);
    }

    @Test
    public void testGetTimeToDoDate() {
        Assert.assertEquals(4, mTask.getTimeToDo(DateUtils.addDays(date, 4)), 0.0);
    }

    @Test
    public void testGetTimeToDoDateWithNaNWorkingUnits() {
        Task task = new Task("Task");
        task.setEstimation(20.0);
        WorkingUnit wu = new WorkingUnit(new Date(), Double.NaN, 8.0);
        task.addWorkingUnit(wu);
        wu = new WorkingUnit(DateUtils.addDays(wu.getDate(), 1), Double.NaN, 4.0);
        Assert.assertEquals(task.getEstimation(), task.getTimeToDo(wu.getDate()), 0.0);
    }

    @Test
    public void testGetTimeDone() {
        Assert.assertEquals(16, mTask.getTimeDone(), 0.0);
    }

    @Test
    public void testGetWorkingUnitCount() {
        Assert.assertEquals(5, mTask.getWorkingUnitCount());
    }

    @Test
    public void testGetterSetter() {
        Locale.setDefault(Locale.ENGLISH);
        mTask.setTitle("Task-1");
        mTask.setDescription("Description");
        mTask.setStatus(EnumTaskStatusType.DONE);
        mTask.setEstimation(100.0);
        Assert.assertEquals("Task-1", mTask.getTitle());
        Assert.assertEquals("Description", mTask.getDescription());
        Assert.assertEquals(100.0, mTask.getEstimation());
        Assert.assertEquals(true, mTask.toString().equalsIgnoreCase("Task-1 [100.0]"));
    }

    @Test
    public void testCompletion() {
        Assert.assertEquals(0.8, mTask.getCompletion());
    }

    @Test
    public void testEquals() {
        Task task1 = new Task("Task-1");
        Task task2 = new Task("Task-2");
        Assert.assertEquals(true, mTask.equals(task1));
        Assert.assertEquals(false, mTask.equals(task2));
    }

    @Test
    public void testWorkingUnit() {
        WorkingUnit wu1 = new WorkingUnit(new Date());
        WorkingUnit wu2 = new WorkingUnit(DateUtils.addDays(wu1.getDate(), 1));
        WorkingUnit wu3 = new WorkingUnit(wu1.getDate());
        Task task = new Task("Task");
        task.addWorkingUnit(wu1);
        task.addWorkingUnit(wu2);
        task.addWorkingUnit(wu3);
        WorkingUnit wu = task.getWorkingUnit(wu2.getDate());
        Assert.assertEquals(true, wu.equals(wu2));
    }

    @Test
    public void testSprint() {
        Sprint sprint = new Sprint("Sprint");
        sprint.setStart(new Date());
        sprint.setEnd(DateUtils.addDays(sprint.getStart(), 14));
        mTask.setSprint(sprint);
        Assert.assertEquals(true, sprint.getId().equalsIgnoreCase(mTask.getSprintId()));
    }

    @Test
    public void testBacklogItem() {
        BacklogItem item = new BacklogItem("Item");
        item.addTask(mTask, "");
        Assert.assertEquals(true, item.getId().equalsIgnoreCase(mTask.getBacklogItemId()));
    }

    @Test
    public void testClone() {
        Task clone = mTask.clone();
        Assert.assertEquals(mTask.getBacklogItemId(), clone.getBacklogItemId());
        Assert.assertEquals(mTask.getDescription(), clone.getDescription());
        Assert.assertEquals(mTask.getMemberId(), clone.getMemberId());
        Assert.assertEquals(mTask.getSprintId(), clone.getSprintId());
        Assert.assertEquals(mTask.getTitle(), clone.getTitle());
        Assert.assertEquals(mTask.getWorkingUnitCount(), clone.getWorkingUnitCount());
        Assert.assertEquals(mTask.getEstimation(), clone.getEstimation());
        Assert.assertEquals(mTask.getStatus(), clone.getStatus());
    }

    @Test
    public void testGetTimeDoneDate() {
        Task task = new Task("Task");
        Date date = new Date();
        WorkingUnit workingUnit = new WorkingUnit(date, 5.0, 10.0);
        task.addWorkingUnit(workingUnit);
        Assert.assertEquals(10.0, task.getTimeDone(date));
        Assert.assertEquals(0.0, task.getTimeDone(DateUtils.addDays(date, 1)));
    }

    @Test
    public void testRemoveWorkingUnit() {
        Task task = new Task("Task");
        WorkingUnit workingUnit = new WorkingUnit(new Date(), 5.0, 4.0);
        task.addWorkingUnit(workingUnit);
        workingUnit = new WorkingUnit(DateUtils.addDays(workingUnit.getDate(), 1), 10.0, 3.0);
        task.addWorkingUnit(workingUnit);
        Assert.assertEquals(2, task.getWorkingUnitCount());
        task.removeWorkingUnit(workingUnit);
        Assert.assertEquals(1, task.getWorkingUnitCount());
    }

    @Test
    public void testRemoveWorkingUnits() {
        Date date = new Date();
        Task task = new Task("Task");
        final int SIZE = 10;
        for (int i = 0; i < SIZE; i++) {
            task.addWorkingUnit(new WorkingUnit(DateUtils.addDays(date, i), 20.0 - i, 3.0));
        }
        Assert.assertEquals(SIZE, task.getWorkingUnitCount());
        task.removeAllWorkingUnits();
        Assert.assertEquals(0, task.getWorkingUnitCount());
    }

    @Test
    public void testIsSprintChangeable() {
        Sprint sprint = new Sprint("Sprint");
        sprint.setStart(new Date());
        sprint.setEnd(DateUtils.addDays(sprint.getStart(), 21));
        Task task = new Task("Task");
        task.setSprint(sprint);
        WorkingUnit workingUnit = new WorkingUnit(sprint.getStart(), 4.0, 8.0);
        task.addWorkingUnit(workingUnit);
        Assert.assertFalse(task.isSprintChangeable(sprint));
    }

    @Test
    public void testIsSprintChangeableWithoutWorkingUnit() {
        Sprint sprint = new Sprint("Sprint");
        sprint.setStart(new Date());
        sprint.setEnd(DateUtils.addDays(sprint.getStart(), 21));
        Task task = new Task("Task");
        task.setSprint(sprint);
        Assert.assertTrue(task.isSprintChangeable(sprint));
    }

    @Test
    public void testIsSprintChangeableWithoutSprint() {
        Task task = new Task("Task");
        Assert.assertTrue(task.isSprintChangeable(null));
    }

    @Test
    public void testUpdateWorkingUnitAlreadyExisting() {
        Task task = new Task("Task");
        Date date = new Date();
        WorkingUnit workingUnit = new WorkingUnit(date, 2.0, 4.0);
        task.addWorkingUnit(workingUnit);
        WorkingUnit newWorkingUnit = new WorkingUnit(date, 3.0, 9.0);
        task.updateWorkingUnit(newWorkingUnit);
        WorkingUnit updatedWorkingUnit = task.getWorkingUnit(date);
        Assert.assertEquals(3.0, updatedWorkingUnit.getToDo());
        Assert.assertEquals(9.0, updatedWorkingUnit.getDone());
    }

    @Test
    public void testUpdateWorkingUnitNotExisting() {
        Task task = new Task("Task");
        Date date = new Date();
        WorkingUnit workingUnit = new WorkingUnit(date, 2.0, 4.0);
        task.updateWorkingUnit(workingUnit);
        WorkingUnit updatedWorkingUnit = task.getWorkingUnit(date);
        Assert.assertEquals(2.0, updatedWorkingUnit.getToDo());
        Assert.assertEquals(4.0, updatedWorkingUnit.getDone());
    }

    @Test
    public void testUpdate() {
        Task task = new Task("Task");
        Task updateTask = task.clone();
        updateTask.setDescription("descripiton");
        updateTask.setEstimation(10.0);
        updateTask.setStatus(EnumTaskStatusType.NOT_STARTED);
        updateTask.setTitle("New task");
        updateTask.setBacklogItemId("backlog id");
        updateTask.setMemberId("member id");
        updateTask.setSprintId("sprint id");
        task.update(updateTask);
        Assert.assertTrue(task.getBacklogItemId().equals("backlog id"));
        Assert.assertTrue(task.getDescription().equals("descripiton"));
        Assert.assertTrue(task.getMemberId().equals("member id"));
        Assert.assertTrue(task.getSprintId().equals("sprint id"));
        Assert.assertTrue(task.getTitle().equals("New task"));
        Assert.assertTrue(task.getEstimation().equals(10.0));
        Assert.assertTrue(task.getStatus() == EnumTaskStatusType.NOT_STARTED);
    }

    @Test
    public void testGetCompletion() {
        Task task = new Task("Task");
        WorkingUnit workingUnit = new WorkingUnit(new Date(), 0.0, 0.0);
        task.addWorkingUnit(workingUnit);
        Assert.assertEquals(0.0, task.getCompletion());
    }

    @Test
    public void testGetCompletionTaskDone() {
        Task task = new Task("Task");
        task.setEstimation(0.0);
        task.setStatus(EnumTaskStatusType.DONE);
        Assert.assertEquals(1.0, task.getCompletion());
    }

    @Test
    public void testRemoveAllNullWorkingUnits() {
        Task task = new Task("Task");
        WorkingUnit workingUnit = new WorkingUnit(new Date(), 10.0, 20.0);
        task.addWorkingUnit(workingUnit);
        task.removeAllNullWorkingUnit();
        Assert.assertEquals(1, task.getWorkingUnitCount());
    }

    @Test
    public void testRemoveAllNullWorkingUnitsWithDoneAndTodoNull() {
        Task task = new Task("Task");
        WorkingUnit workingUnit = new WorkingUnit(new Date(), null, null);
        task.addWorkingUnit(workingUnit);
        task.removeAllNullWorkingUnit();
        Assert.assertEquals(0, task.getWorkingUnitCount());
    }

    @Test
    public void testRemoveAllNullWorkingUnitsWithDoneAndTodoNotANumber() {
        Task task = new Task("Task");
        WorkingUnit workingUnit = new WorkingUnit(new Date(), Double.NaN, Double.NaN);
        task.addWorkingUnit(workingUnit);
        task.addWorkingUnit(new WorkingUnit(DateUtils.addDays(workingUnit.getDate(), 1), 10.0, null));
        task.removeAllNullWorkingUnit();
        Assert.assertEquals(1, task.getWorkingUnitCount());
    }

    @Test
    public void testGetFirstWorkingDay() {
        Date begin = mTask.getFirstWorkingDay();
        Assert.assertTrue(DateUtils.equalDay(begin, beginWorkingUnit.getDate()));
    }

    @Test
    public void testGetLastWorkingDay() {
        Date end = mTask.getLastWorkingUnit();
        Assert.assertTrue(DateUtils.equalDay(end, endWorkingUnit.getDate()));
    }
}
