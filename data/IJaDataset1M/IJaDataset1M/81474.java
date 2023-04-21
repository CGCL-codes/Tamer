package org.miv.jism.core.executor;

import org.miv.jism.core.JismError;
import java.util.concurrent.ThreadFactory;

/**
 * An implementation of the ThreadFactory interface.
 * Created threaded will be affected to a specified threadGroup.
 *
 * @author Guilhelm Savin
 *
 * @see java.util.concurrent.ThreadFactory
 **/
public class JismThreadFactory implements ThreadFactory {

    /**
	 * Thread group of created threads.
	 **/
    private ThreadGroup group;

    /**
	 * Current thread id.
	 **/
    private int id;

    /**
	 * A forbidden constructor.
	 * JismThreadFactory needs to have a threadGroup.
	 * Using this constructor leads to throw a JismError.
	 *
	 * @see org.miv.jism.core.JismError
	 **/
    private JismThreadFactory() {
        throw new JismError("try to create a forbidden instance");
    }

    /**
	 * Default constructor for this object.
	 * Setting a threadGroup for the created threads.
	 *
	 * @param group
	 * 	threadGroup of created threads
	 * @see java.lang.ThreadGroup
	 **/
    public JismThreadFactory(ThreadGroup group) {
        if (group == null) {
            throw new JismError("security error while creating a ThreadFactory");
        }
        this.group = group;
        this.id = 0;
    }

    /**
	 * Implementation of newThread() specified by the ThreadFactory interface.
	 *
	 * @param r
	 * 	task which will be executed by the created thread
	 * @see java.lang.Runnable
	 **/
    public Thread newThread(Runnable r) {
        return new Thread(group, r, group.getName() + "#" + Integer.toHexString(id++));
    }
}
