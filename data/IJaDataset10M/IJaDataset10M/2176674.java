package org.fudaa.ctulu;

/**
 * @author Fred Deniger
 * @version $Id: CtuluCommandPersitant.java,v 1.2 2005-08-11 09:06:57 deniger Exp $
 */
public interface CtuluCommandPersitant {

    void undo();

    boolean canUndo();
}
