package com.google.code.whitetask.old;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.code.whitetask.old.AbstractFluentTask;
import com.google.code.whitetask.old.AsyncerDecorator;
import com.google.code.whitetask.old.IfRunDecorator;
import com.google.code.whitetask.old.RetryDecorator;
import com.google.code.whitetask.old.TaskContext;
import com.google.code.whitetask.old.TaskResultType;
import com.google.code.whitetask.old.Times;
import com.google.code.whitetask.old.TryCatchDecorator;

public class AbstractFluentTaskTest {

    SampleTask task = new SampleTask();

    @Test
    public void asycer() throws Exception {
        assertTrue(task.asynchronously() instanceof AsyncerDecorator);
    }

    @Test
    public void caught() throws Exception {
        assertTrue(task.caught() instanceof TryCatchDecorator);
    }

    @Test
    public void IfResultIs() throws Exception {
        assertTrue(task.ifResultIs(TaskResultType.SUCCESS) instanceof IfRunDecorator);
    }

    @Test
    public void retry() throws Exception {
        assertTrue(task.retry() instanceof RetryDecorator);
    }

    @Test
    public void retryWithTimes() throws Exception {
        final int times = 3;
        RetryDecorator retryDecorator = task.retry(new Times(times));
        assertEquals(times, retryDecorator.times.getTimes());
    }

    private class SampleTask extends AbstractFluentTask {

        public TaskResultType execute(TaskContext context) {
            return TaskResultType.SUCCESS;
        }
    }
}
