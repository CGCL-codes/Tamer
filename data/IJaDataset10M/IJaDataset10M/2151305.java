package org.curjent.test.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.curjent.agent.Agent;
import org.curjent.agent.AgentTasks;
import org.curjent.agent.CustomLoader;
import org.curjent.agent.DynamicTasks;
import org.curjent.agent.FixedTasks;
import org.curjent.agent.Isolated;
import org.curjent.agent.Leading;
import org.curjent.agent.Marker;
import org.curjent.agent.ReusableTask;
import org.junit.Test;

/**
 * Tests agents with multiple tasks.
 */
public class MultiTaskTest extends AgentTest {

    private static final int TASK_COUNT = 3;

    public interface MultiAgent extends TestAgent {

        void countDownAndWait(CountDownLatch latch) throws InterruptedException;

        void record(List<Integer> list, int value);

        void mark(List<Integer> list, int value);

        void leading(List<Integer> list, int value, CountDownLatch latch) throws InterruptedException;

        void isolated(List<Integer> list, int value);
    }

    public static class MultiTask extends TestTask implements MultiAgent {

        @Override
        public void countDownAndWait(CountDownLatch latch) throws InterruptedException {
            latch.countDown();
            latch.await();
        }

        @Override
        public void record(List<Integer> list, int value) {
            list.add(value);
        }

        @Override
        @Marker
        public void mark(List<Integer> list, int value) {
            if (false) try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(value);
        }

        @Override
        @Leading
        public void leading(List<Integer> list, int value, CountDownLatch latch) throws InterruptedException {
            latch.await();
            list.add(value);
        }

        @Override
        @Isolated
        public void isolated(List<Integer> list, int value) {
            if (false) try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(value);
        }
    }

    MultiAgent agent;

    @Override
    protected TestAgent newTestAgent() {
        try {
            return agent = (MultiAgent) Agent.newInstance(new CustomLoader(MultiAgent.class.getClassLoader()), new Class<?>[] { MultiAgent.class }, getTasks(), MultiTask.class);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    static int agentTasks = 0;

    static AgentTasks getTasks() {
        switch(agentTasks++ % 3) {
            case 0:
                return new ReusableTask(new MultiTask(), TASK_COUNT);
            case 1:
                Object[] tasks = new Object[TASK_COUNT];
                for (int i = 0; i < TASK_COUNT; i++) {
                    tasks[i] = new MultiTask();
                }
                return new FixedTasks(tasks);
            case 2:
                return new DynamicTasks(TASK_COUNT, TASK_COUNT - 1, Collections.singleton(new MultiTask())) {

                    @Override
                    protected Object create() {
                        return new MultiTask();
                    }
                };
            default:
                fail();
        }
        return null;
    }

    @Test
    public void testMultipleThreads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(TASK_COUNT + 1);
        for (int i = 0; i < TASK_COUNT; i++) agent.countDownAndWait(latch);
        assertFalse(latch.await(1, TimeUnit.MILLISECONDS));
        latch.countDown();
        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void testMarker() {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        agent.record(list, 1);
        agent.record(list, 2);
        agent.mark(list, 3);
        agent.record(list, 4);
        agent.record(list, 5);
        agent.mark(list, 6);
        Set<Integer> firstSet = new HashSet<Integer>();
        firstSet.add(1);
        firstSet.add(2);
        firstSet.add(4);
        firstSet.add(5);
        Set<Integer> secondSet = new HashSet<Integer>();
        secondSet.addAll(firstSet);
        secondSet.add(3);
        flush(agent);
        assertEquals(6, list.size());
        for (int i = 0; i < 2; i++) {
            int value = list.get(i);
            assertTrue(firstSet.contains(value));
        }
        for (int i = 0; i < 5; i++) {
            int value = list.get(i);
            assertTrue(secondSet.contains(value));
        }
        int value = list.get(5);
        assertEquals(6, value);
    }

    @Test
    public void testLeading() throws InterruptedException {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        CountDownLatch latch = new CountDownLatch(1);
        agent.leading(list, 1, latch);
        agent.record(list, 2);
        if (false) sleep(200);
        latch.countDown();
        flush(agent);
        assertEquals(1, (int) list.get(0));
        assertEquals(2, (int) list.get(1));
    }

    @Test
    public void testIsolated() {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        agent.record(list, 1);
        agent.mark(list, 2);
        agent.isolated(list, 3);
        agent.record(list, 4);
        agent.record(list, 5);
        Set<Integer> firstSet = new HashSet<Integer>();
        firstSet.add(1);
        firstSet.add(2);
        Set<Integer> secondSet = new HashSet<Integer>();
        secondSet.add(4);
        secondSet.add(5);
        flush(agent);
        assertEquals(5, list.size());
        for (int i = 0; i < 2; i++) {
            int value = list.get(i);
            assertTrue(firstSet.contains(value));
        }
        for (int i = 3; i < 5; i++) {
            int value = list.get(i);
            assertTrue(secondSet.contains(value));
        }
        int value = list.get(2);
        assertEquals(3, value);
    }
}
