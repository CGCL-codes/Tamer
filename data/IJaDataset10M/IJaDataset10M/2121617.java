package de.enough.polish.util;

/**
 * <p>A task that can be scheduled asynchronously</p>
 *
 * <p>Copyright Enough Software 2007 - 2009</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public interface Task {

    /**
	 * Executes this task in a background thread.
	 */
    public void execute() throws Exception;
}
