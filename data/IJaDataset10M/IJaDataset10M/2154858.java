package se.chalmers.doit.test.core;

import java.util.Date;
import se.chalmers.doit.core.implementation.*;
import android.test.AndroidTestCase;

/**
 * JUnit testclass for testing task.java.
 * 
 * @author Kaufmann, Boel
 * 
 */
public class TaskTest extends AndroidTestCase {

    public void testTaskStringStringIPriorityDateDateIntBoolean() {
        new Task("Name", "Description", new Priority((byte) 1), new Date(1), new Date(2), 0, false);
    }

    public void testTaskStringStringBoolean() {
        new Task("Name", "Description", false);
    }

    public void testTaskITask() {
        new Task(new Task("Name", "Description", false));
    }

    public void testGetCustomPosition() {
        Task t = new Task("Name", "Description", new Priority((byte) 1), new Date(1), new Date(2), 5, false);
        assertTrue(t.getCustomPosition() == 5);
        t = new Task("Name", "Description", new Priority((byte) 1), new Date(1), new Date(2), 13337, false);
        assertTrue(t.getCustomPosition() == 13337);
    }

    public void testGetDescription() {
        Task t = new Task("Name", "Desc.", false);
        assertTrue(t.getDescription().equals("Desc."));
        t = new Task("Name", "ASCii25", false);
        assertTrue(t.getDescription().equals("ASCii25"));
    }

    public void testGetDueDate() {
        Date date1 = new Date(1);
        Date date2 = new Date(2);
        Task t = new Task("Name", "Description", new Priority((byte) 1), date1, new Date(2), 5, false);
        assertTrue(t.getDueDate().equals(date1));
        t = new Task("Name", "Description", new Priority((byte) 1), date2, new Date(2), 13337, false);
        assertTrue(t.getDueDate().equals(date2));
    }

    public void testGetName() {
        Task t = new Task("Boel", "...", false);
        assertTrue(t.getName().equals("Boel"));
        t = new Task("Nelson", "...", false);
        assertTrue(t.getName().equals("Nelson"));
    }

    public void testGetPriority() {
        Task t = new Task("Name", "Description", new Priority((byte) 1), new Date(1), new Date(2), 5, false);
        assertTrue(t.getPriority().getValue() == 1);
        t = new Task("Name", "Description", new Priority((byte) 2), new Date(1), new Date(2), 5, false);
        assertTrue(t.getPriority().getValue() == 2);
    }

    public void testGetReminderDate() {
        Date date1 = new Date(4);
        Date date2 = new Date(5);
        Task t = new Task("Name", "Description", new Priority((byte) 1), new Date(1), date1, 5, false);
        assertTrue(t.getReminderDate().equals(date1));
        t = new Task("Name", "Description", new Priority((byte) 1), new Date(1), date2, 5, false);
        assertTrue(t.getReminderDate().equals(date2));
    }

    public void testIsCompleted() {
        Task t = new Task("Boel", "...", false);
        assertTrue(!t.isCompleted());
        t = new Task("Boel", "...", true);
        assertTrue(t.isCompleted());
    }

    public void testToString() {
        Task t = new Task("Boel", "...", false);
        assertTrue(t.toString().equals(t.getName()));
        t = new Task("Nelson1337", "...", true);
        assertTrue(t.toString().equals(t.getName()));
    }
}
