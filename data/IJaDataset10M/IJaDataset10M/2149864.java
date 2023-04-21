package com.usoog.commons.network.message;

/**
 * A recordable message.
 * 
 * @param <Obj> The object which will be used in {@link #exec(java.lang.Object)
 *	exec()} when executing it.
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 */
public interface ActionMessage<Obj extends Object> extends Message {

    /**
	 * The time associated with the message. For most actions this is
	 * not the same as the tick at which the action becomes active.
	 *
	 * @return The time.
	 */
    public int getTick();

    /**
	 * Sets the tick at which this action is initiated. For most actions this is
	 * not the same as the tick at which the action becomes active.
	 *
	 * @param tick the time of the message
	 */
    public void setTick(int tick);

    /**
	 * An execute method witch will execute the actions associated with
	 *	this message.
	 * This to get a smoother API.
	 *
	 * @param obj The object which will be used in the method when executing it.
	 * @throws Exception If it failed to execute the message.
	 */
    public void exec(Obj obj) throws Exception;
}
