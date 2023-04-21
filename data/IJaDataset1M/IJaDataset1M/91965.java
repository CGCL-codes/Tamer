package it.xargon.events;

import java.lang.reflect.Method;

/**
 * Events returned from various "waitForEvent" in EventsTrap class will implement this interface
 * @author Francesco Muccilli
 */
public interface ConcreteEvent {

    /**
    * @return interface method that has been called in order to generate the event 
    */
    public Method getMethod();

    /**
    * @return all the references to the parameters that have been passed to the event
    */
    public Object[] getParams();

    /**
    * @return Java timestamp of the moment when this event has been captured
    */
    public long getTimeStamp();
}
