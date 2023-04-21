package com.webstarsltd.common.pim.common;

import com.webstarsltd.common.pim.model.VComponent;

/**
 *
 * @version $Id: WriterInterface.java,v 1.4 2007/06/18 12:34:53 luigiafassina Exp $
 */
public interface WriterInterface {

    public Object write(VComponent vc, Object obj) throws WriterException;
}
