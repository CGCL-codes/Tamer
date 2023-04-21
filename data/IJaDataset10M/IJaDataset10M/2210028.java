package org.perfectjpattern.jee.api.integration.dao;

import java.lang.reflect.*;
import java.util.*;

/**
 * Abstract definition for executing any defined finder method
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $ $Date: Nov 5, 2008 4:23:29 PM $
 */
public interface IFinderExecutor<Element> {

    public List<Element> execute(Method aMethod, Object... anArguments);
}
