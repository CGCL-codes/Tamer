package org.gjt.sp.jedit;

/**
 * An interface to listen some events about registers.
 * @author Matthieu Casanova
 * @version $Id: FoldHandler.java 5568 2006-07-10 20:52:23Z kpouer $
 */
public interface RegistersListener {

    void registerChanged(char name);
}
