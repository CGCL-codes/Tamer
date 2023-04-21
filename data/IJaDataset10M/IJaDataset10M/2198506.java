package com.jacum.cms.source.jcr;

import net.sf.cglib.beans.BeanGenerator;
import javax.jcr.nodetype.NodeType;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author timur
 */
public class BeanHelperFactory {

    private Map<String, BeanGenerator> beanClassCache = new HashMap<String, BeanGenerator>();

    public BeanHelper createBeanHelper(JcrRepositorySessionImpl jcrSession) {
        return new BeanHelper(jcrSession, beanClassCache);
    }
}
