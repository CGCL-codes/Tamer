package com.itaskmanager;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import com.itaskmanager.datastore.DatastoreFactory;
import com.itaskmanager.testutils.TMTestUtils;

/**
 * Creates sample entities
 * 
 * @author mannc2
 * 
 */
public class SampleCreater {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        try {
            TaskManager tmInstance = TaskManager.getInstance();
            DatastoreFactory.getSQLExecutor().clean();
            TaskManager.getInstance().init();
            for (int i = 0; i < 2; i++) {
                Entity dummyTask = TMTestUtils.createDummyTask("sample Task" + i, new GregorianCalendar(2010, 8, 30));
                dummyTask.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                tmInstance.addOrUpdateEntity(dummyTask);
            }
            for (int i = 0; i < 2; i++) {
                TaskGroup dummyTaskGr = TMTestUtils.createDummyTaskGroup("sample Task Group" + i, new GregorianCalendar(2010, 8, 30));
                dummyTaskGr.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                Task subTask1 = TMTestUtils.createDummyTask("first Task" + i, new GregorianCalendar(2010, 8, 30));
                subTask1.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                dummyTaskGr.addChildEntity(subTask1);
                Task subTask2 = TMTestUtils.createDummyTask("second Task" + i, new GregorianCalendar(2010, 8, 30));
                subTask2.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                dummyTaskGr.addChildEntity(subTask2);
                tmInstance.addOrUpdateEntity(dummyTaskGr);
            }
            for (int i = 0; i < 2; i++) {
                TaskGroup parentTaskGr = TMTestUtils.createDummyTaskGroup("parent Task Group" + i, new GregorianCalendar(2010, 8, 30));
                parentTaskGr.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                TaskGroup childTaskGr = TMTestUtils.createDummyTaskGroup("child Task Group" + i, new GregorianCalendar(2010, 8, 30));
                childTaskGr.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                Task subTask1 = TMTestUtils.createDummyTask("sub Task" + i, new GregorianCalendar(2010, 8, 30));
                subTask1.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                parentTaskGr.addChildEntity(subTask1);
                Task subTask2 = TMTestUtils.createDummyTask("child sub Task" + i, new GregorianCalendar(2010, 8, 30));
                subTask2.setReminderCalendar(new GregorianCalendar(2009, 11, 30));
                childTaskGr.addChildEntity(subTask2);
                parentTaskGr.addChildEntity(childTaskGr);
                tmInstance.addOrUpdateEntity(parentTaskGr);
            }
            for (int i = 0; i < 2; i++) {
                TaskGroup emptyTaskGr = TMTestUtils.createDummyTaskGroup("empty Task Group" + i, new GregorianCalendar(2010, 8, 30));
                tmInstance.addOrUpdateEntity(emptyTaskGr);
            }
            Entity dummyTask = TMTestUtils.createDummyTask("Go to Gym in evening", null);
            dummyTask.setComments("daily Task");
            dummyTask.setRecurrenceType(RECURRENCE_TYPE.DAILY);
            tmInstance.addOrUpdateEntity(dummyTask);
            dummyTask = TMTestUtils.createDummyTask("play lawn tennis today", null);
            dummyTask.setComments("weekly Task");
            Calendar calendar = new GregorianCalendar();
            EnumSet<WEEKLY_DAYS> repetitionPattern = EnumSet.of(WEEKLY_DAYS.fromIndex(calendar.get(Calendar.DAY_OF_WEEK)));
            dummyTask.setWeeklyDays(repetitionPattern);
            dummyTask.setRecurrenceType(RECURRENCE_TYPE.WEEKLY);
            tmInstance.addOrUpdateEntity(dummyTask);
            calendar = new GregorianCalendar();
            calendar.add(Calendar.SECOND, 45);
            dummyTask = TMTestUtils.createDummyTask("car insurance", null);
            dummyTask.setComments("yeerly Task");
            dummyTask.setDueTime(calendar);
            dummyTask.setRecurrenceType(RECURRENCE_TYPE.YEARLY);
            dummyTask.setRecurrenceCalendar(calendar);
            tmInstance.addOrUpdateEntity(dummyTask);
            dummyTask = TMTestUtils.createDummyTask("Book gas cyclinder", null);
            dummyTask.setComments("monthly Task");
            dummyTask.setRecurrenceType(RECURRENCE_TYPE.MONTHLY);
            dummyTask.setWeeklyDays(EnumSet.of(WEEKLY_DAYS.MONDAY));
            calendar = new GregorianCalendar();
            calendar.add(Calendar.SECOND, 45);
            dummyTask.setDueTime(calendar);
            dummyTask.setRecurrenceCalendar(calendar);
            tmInstance.addOrUpdateEntity(dummyTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
