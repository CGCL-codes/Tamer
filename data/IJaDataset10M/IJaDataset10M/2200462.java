package org.plazmaforge.framework.action;

import org.plazmaforge.framework.core.exception.ApplicationException;

/** 
 * @author Oleh Hapon
 * $Id: IProcess.java,v 1.3 2010/12/05 07:51:27 ohapon Exp $
 */
public interface IProcess {

    public void run() throws ApplicationException;
}
