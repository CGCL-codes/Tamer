package org.fudaa.dodico.mnt;

import java.util.List;
import org.fudaa.ctulu.CtuluCommandContainer;

/**
 * Classe concrete implementant les methodes par d�faut.
 * @author Fred Deniger
 * @version $Id: MNTListPoint.java,v 1.3 2004-10-08 14:24:23 deniger Exp $
 */
public class MNTListPoint extends MNTListPointAbstract {

    /**
   * 
   */
    public MNTListPoint() {
        super();
    }

    /**
   * @param _i
   */
    public MNTListPoint(int _i) {
        super(_i);
    }

    protected void fireObjectAdded() {
    }

    protected void fireObjectChanged() {
    }

    protected void fireObjectRemoved() {
    }

    protected void fireObjectStructureChanged() {
    }

    protected void internActionCleared(CtuluCommandContainer _c) {
    }

    protected void internActionPointAdded(List _values, CtuluCommandContainer _c) {
    }

    protected void internActionPointInserted(int _i, List _values, CtuluCommandContainer _c) {
    }

    protected void internActionPointRemoved(int _i, CtuluCommandContainer _c) {
    }
}
