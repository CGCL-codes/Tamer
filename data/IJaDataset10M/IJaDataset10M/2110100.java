package net.sf.joafip.service;

/**
 * 
 * @author luc peuvrier
 * 
 */
public interface ISaveEventListener {

    /**
	 * 
	 * @return true if save must be done, else will be skipped
	 */
    boolean doSave();

    /**
	 * 
	 * @param closing
	 *            true if save for closing
	 */
    void saveDone(boolean closing);
}
