package org.opensourcephysics.tools;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import org.opensourcephysics.controls.*;

/**
 * This is a Remote Tool implementation for osp data transfers via RMI.
 *
 * @author Wolfgang Christian and Doug Brown
 * @version 1.0
 */
public class RemoteTool extends UnicastRemoteObject implements Tool {

    Tool child;

    Map replies = new HashMap();

    Map jobs = new HashMap();

    /**
   * Constructs a RemoteTool.
   *
   * @param tool a Tool to handle forwarded jobs
   * @throws RemoteException if this cannot be constructed
   */
    public RemoteTool(Tool tool) throws RemoteException {
        super();
        OSPLog.finest("Wrapping tool " + tool.getClass().getName());
        child = tool;
    }

    /**
   * Sends a job to this tool.
   *
   * @param job the job
   * @param replyTo the tool interested in the job (may be null)
   * @throws RemoteException
   */
    public void send(Job job, Tool replyTo) throws RemoteException {
        save(job, replyTo);
        job = convert(job);
        if (child.equals(replyTo)) {
            sendReplies(job);
        } else {
            forward(job);
        }
    }

    /**
   * Saves a tool for later replies.
   *
   * @param job the job
   * @param tool the tool interested in the job (may be null)
   */
    private void save(Job job, Tool tool) {
        if (tool == null || child.equals(tool)) {
            return;
        }
        Collection tools = (Collection) replies.get(job);
        if (tools == null) {
            tools = new HashSet();
            replies.put(job, tools);
        }
        tools.add(tool);
    }

    /**
   * Replies to tools interested in the specified job.
   *
   * @param job the job
   */
    private void sendReplies(Job job) throws RemoteException {
        Collection tools = (Collection) replies.get(job);
        if (tools == null) {
            return;
        }
        Iterator it = tools.iterator();
        while (it.hasNext()) {
            Tool tool = (Tool) it.next();
            tool.send(job, this);
        }
    }

    /**
   * Forwards a job to a child.
   *
   * @param job the job
   */
    private void forward(Job job) throws RemoteException {
        child.send(job, this);
    }

    /**
   * Wraps a job for forwarding or unwraps it for replies.
   *
   * @param job the job to be converted
   * @return the converted job
   */
    private Job convert(Job job) throws RemoteException {
        if (job instanceof LocalJob) {
            Job remote = new RemoteJob(job);
            jobs.put(remote, job);
            return remote;
        }
        Object obj = (Job) jobs.get(job);
        if (obj == null) return job;
        return (Job) obj;
    }
}
