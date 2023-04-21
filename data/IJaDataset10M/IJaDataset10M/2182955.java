package com.jbogers.framework.scheduler;

import java.util.Map;
import java.util.TreeMap;
import java.lang.Thread;
import java.util.List;
import java.util.LinkedList;

/**
 * TODO Fill in this template in you eclipse installation. (See Eclipse > Window > Preferences > Java > Code Style > Code Templates > Comments > Types )
 * TODO Document this class.
 * 
 * @author t51crojbb [YourFullNameHere]
 * @version %PR%
 */
public class SimpleArtifactAgent implements ArtifactAgent {

    private TreeMap m_map;

    private ArtifactQueue m_saqAgent;

    private ArtifactQueue m_saqWorker;

    private LinkedList m_workerThreadPool;

    private LinkedList m_agentThreadPool;

    public void setArtifactAssociation(Class artifactLineItem, int artifactState, Class handler) {
        synchronized (m_map) {
            m_map.put(artifactLineItem.getName() + ":" + artifactState, handler.getName());
        }
    }

    public void setForwardingAgent(ArtifactLineItem artifactLineItem, int artifactState, ArtifactAgent agent) {
        m_map.put(artifactLineItem.getClass().getName() + ":" + artifactState, agent.getClass().getName());
    }

    public SimpleArtifactAgent(int agentQueueSize, int agentThreadCount, int workerQueueSize, int workerThreadCount) {
        m_map = new TreeMap();
        m_saqAgent = new SimpleArtifactQueueStack(agentQueueSize);
        m_saqWorker = new SimpleArtifactQueueStack(workerQueueSize);
        m_workerThreadPool = new LinkedList();
        m_agentThreadPool = new LinkedList();
        for (int i = 0; i < agentThreadCount; i++) {
            GenericWorkerThread gwt = new GenericWorkerThread("Agent:" + i, m_saqAgent, m_agentThreadPool);
            gwt.start();
        }
        for (int i = 0; i < workerThreadCount; i++) {
            GenericWorkerThread gwt = new GenericWorkerThread("Worker:" + i, m_saqWorker, m_workerThreadPool);
            gwt.start();
        }
    }

    public void signal(Object signal) {
    }

    public void run() {
    }

    /**
	 * {@inheritDoc}
	 */
    public int getMaxSize() {
        return m_saqAgent.getMaxSize();
    }

    /**
	 * {@inheritDoc}
	 */
    public Artifact pop() {
        return m_saqAgent.pop();
    }

    /**
	 * {@inheritDoc}
	 */
    public void push(Artifact item) throws InterruptedException {
    }

    /**
	 * {@inheritDoc}
	 * @throws InterruptedException 
	 */
    public void push(Artifact item, long timeOut) throws InterruptedException {
        m_saqAgent.push(item, timeOut);
    }

    /**
	 * {@inheritDoc}
	 */
    public void setMaxSize(int size) {
        m_saqAgent.setMaxSize(size);
    }

    public int getSize() {
        return m_saqAgent.getSize();
    }

    public void start() {
    }
}
