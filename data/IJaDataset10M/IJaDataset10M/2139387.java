package com.abiquo.test.api;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.abiquo.api.Service;
import com.abiquo.api.Workflow;
import com.abiquo.api.resources.CommandResource;
import com.abiquo.api.resources.PingResource;
import com.abiquo.framework.domain.ResourceInstance;
import com.abiquo.framework.domain.types.IBaseType;
import com.abiquo.framework.exception.FrameworkException;
import com.abiquo.framework.task.AbstractTask;
import com.abiquo.framework.task.Job;
import com.abiquo.framework.task.AbstractTask.State;
import com.abiquo.util.test.BaseTestCase;

public class TestWorkflow extends BaseTestCase {

    public TestWorkflow(String configPath) {
        super("TestWorkflow", configPath);
    }

    List<CommandResource> commands;

    Method callback;

    public void setUp() throws Exception {
        super.setUp();
        commands = new LinkedList<CommandResource>();
        for (ResourceInstance ri : grid.forceProvisionResourcesOnAllNodes(CommandResource.ID)) {
            commands.add(CommandResource.cast(ri));
        }
        callback = Service.findJobHandlerByName(this.getClass(), "onEnd");
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * 1	->	2	->	3 
	 * 						->	5 
	 * 				->	4	
	 * */
    public void testChain() throws FrameworkException {
        Workflow wf;
        wf = new Workflow(this);
        Random rnd = new Random(System.currentTimeMillis());
        for (CommandResource cm : commands) {
            Job j1, j2, j3, j4, j5;
            j1 = cm.execute("sleep " + String.valueOf(rnd.nextInt(10)) + " ");
            j2 = cm.execute("sleep " + String.valueOf(rnd.nextInt(10)) + " ");
            j3 = cm.execute("sleep " + String.valueOf(rnd.nextInt(10)) + " ");
            j4 = cm.execute("sleep " + String.valueOf(rnd.nextInt(10)) + " ");
            j5 = cm.execute("sleep " + String.valueOf(rnd.nextInt(10)) + " ");
            wf.chain(j1, j2);
            wf.chain(j2, j3);
            wf.chain(j2, j4);
            wf.chain(j3, j5);
            wf.chain(j4, j5);
            wf.connect(j1, State.FINISHED, callback);
            wf.connect(j2, State.FINISHED, callback);
            wf.connect(j3, State.FINISHED, callback);
            wf.connect(j4, State.FINISHED, callback);
            wf.connect(j5, State.FINISHED, callback);
        }
        wf.launch(true);
        for (Integer i : sequences) {
            System.out.println(i);
            System.out.flush();
        }
    }

    /***
 * 1
 * 2
 * 3
 * 4
 * 5
 * using ResourceAccess synchronization
 * */
    public void testAdd() throws FrameworkException {
    }

    public void testAddMultiInstance() throws FrameworkException {
        Workflow wf;
        wf = new Workflow(this);
        int im = 1;
        for (ResourceInstance ri : grid.forceProvisionResourcesOnAllNodes(PingResource.ID)) {
            PingResource pr = PingResource.cast(ri);
            Job j = pr.doEcho("msg" + String.valueOf(im));
            wf.add(j);
            wf.connect(j, State.FINISHED, callback);
            im++;
        }
        wf.launch(true);
        for (Integer i : sequences) {
            System.out.println(i);
            System.out.flush();
        }
    }

    public void testCallback() throws FrameworkException {
    }

    /** Job sequence in order its finish*/
    public Queue<Integer> sequences = new ConcurrentLinkedQueue<Integer>();

    public synchronized void onEnd(Job j) {
        System.err.println("*** " + j.getSequence());
        System.err.flush();
        for (IBaseType bt : j.getOutputTypes()) {
            System.err.println(bt.toString());
            System.err.flush();
        }
        synchronized (sequences) {
            sequences.add(new Integer(j.getSequence()));
        }
    }

    public static void main(String args[]) throws Exception {
        TestWorkflow tw = new TestWorkflow("test-config.xml");
        tw.setUp();
        tw.testChain();
        tw.tearDown();
    }
}
