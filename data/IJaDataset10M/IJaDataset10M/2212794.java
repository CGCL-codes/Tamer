package org.yajul.jndi;

import javax.naming.InitialContext;

/**
 * Provides an initial context.
 * <br>
 * User: josh
 * Date: Sep 11, 2009
 * Time: 9:59:32 AM
 */
public interface InitialContextProvider {

    InitialContext getInitialContext();
}
