package com.ziclix.python.io;

import org.python.core.PyObject;

/**
 *
 * @author brian zimmer
 * @date 07/02/2001
 * @author last modified by $Author: bzimmer $
 * @date last modified on $Date: 2002/01/17 06:18:35 $
 * @version $Revision: 1.3 $
 */
public interface IOCallback {

    /**
	 * Method iowrite
	 *
	 * @param PyObject data
	 *
	 */
    public void iowrite(PyObject data);

    /**
	 * Method iowriteln
	 *
	 * @param PyObject data
	 *
	 */
    public void iowriteln(PyObject data);
}
