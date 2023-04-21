package com.meschbach.psi.example.dprime.work;

import com.meschbach.psi.example.dprime.work.event.WorkListener;

/**
 * An instance of <code>Work</code> describes a unit of work which may be
 * distributed to a node.
 * 
 * @author "Mark Eschbach" &lt;meschbach@gmail.com&gt;
 */
public interface Work<T> {

    enum State {

        Setup, Available, Calculating, Completed
    }

    public String getDescription();

    public T getResult();

    public State getState();

    public void addStateListener(WorkListener listener);

    public void removeStateListener(WorkListener listener);
}
