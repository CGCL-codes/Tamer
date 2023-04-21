package net.sf.unruly.listener;

import java.io.Serializable;

/**
 * Interface to recieve events when object is created by a session. 
 * 
 * @author Lance Arlaus
 *
 */
public interface SessionEventListener extends Serializable {

    public <T> void onCreateObject(Class<? extends T> mappedClass, T instance);
}
