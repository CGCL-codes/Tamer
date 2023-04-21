package net.sf.joafip.service;

import net.sf.joafip.entity.EnumFilePersistenceCloseAction;

/**
 * data access session interface for file persistence call back
 * 
 * @author luc peuvrier
 * 
 */
public interface IDASFilePersistenceCallBack {

    /**
	 * fire close action done notification<br>
	 * 
	 * @param action
	 *            the action executed
	 */
    void fireActionDone(EnumFilePersistenceCloseAction action);
}
