package de.kout.wlFxp.interfaces;

import de.kout.wlFxp.Transfer;
import java.util.Vector;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 245 $
  */
public interface wlQueueList {

    /**
	 * DOCUMENT ME!
	 *
	 * @param transfer DOCUMENT ME!
	 */
    public void addAtBegin(Transfer transfer);

    /**
	 * DOCUMENT ME!
	 *
	 * @param transfer DOCUMENT ME!
	 */
    public void addElement(Transfer transfer);

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public Transfer getElement();

    /**
	 * DOCUMENT ME!
	 */
    public void removeFirst();

    /**
	 * DOCUMENT ME!
	 *
	 * @param i DOCUMENT ME!
	 */
    public void removeElementAt(int i);

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isEmpty();

    /**
	 * DOCUMENT ME!
	 */
    public void updateView();

    /**
	 * DOCUMENT ME!
	 *
	 * @param t DOCUMENT ME!
	 */
    public void setTransfering(boolean t);

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public Vector<Transfer> vfiles();

    /**
	 * DOCUMENT ME!
	 *
	 * @param i DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public Transfer elementAt(int i);
}
