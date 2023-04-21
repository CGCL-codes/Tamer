package org.objectstyle.cayenne.unit;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.query.ParameterizedQuery;
import org.objectstyle.cayenne.query.Query;
import org.objectstyle.cayenne.query.QueryChain;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.InputStreamResource;

/**
 * DataSetFactory that loads DataSets from XML using Spring.
 * 
 * @author Andrei Adamchik
 */
public class XMLDataSetFactory implements DataSetFactory {

    private static Logger logObj = Logger.getLogger(XMLDataSetFactory.class);

    protected String location;

    protected Map dataSets;

    public XMLDataSetFactory() {
        this.dataSets = new HashMap();
    }

    /**
     * Returns a Collection of Cayenne queries for a given test.
     */
    public Query getDataSetQuery(Class testCase, String testName, Map parameters) {
        BeanFactory factory = getFactory(testCase);
        if (factory == null) {
            return null;
        }
        Object object = factory.getBean(testName);
        if (object == null) {
            throw new RuntimeException("No query exists for test name:" + testName);
        }
        QueryChain chain = new QueryChain();
        if (object instanceof Collection) {
            Iterator it = ((Collection) object).iterator();
            while (it.hasNext()) {
                chain.addQuery(processQuery((Query) it.next(), parameters));
            }
        } else if (object instanceof Query) {
            chain.addQuery(processQuery((Query) object, parameters));
        } else {
            throw new RuntimeException("Invalid object type for name '" + testName + "': " + object.getClass().getName());
        }
        return chain;
    }

    protected Query processQuery(Query query, Map parameters) {
        if (parameters == null) {
            return query;
        }
        return (query instanceof ParameterizedQuery) ? ((ParameterizedQuery) query).createQuery(parameters) : query;
    }

    protected BeanFactory getFactory(Class testCase) {
        if (testCase == null) {
            throw new CayenneRuntimeException("Null test case");
        }
        BeanFactory factory = null;
        Class aClass = testCase;
        while (factory == null && aClass != null) {
            factory = loadForClass(aClass);
            aClass = aClass.getSuperclass();
        }
        if (factory == null) {
            logObj.error("DataSet resource not found: " + testCase.getName());
            throw new CayenneRuntimeException("DataSet resource not found: " + testCase.getName());
        }
        return factory;
    }

    protected BeanFactory loadForClass(Class testCase) {
        BeanFactory factory = (BeanFactory) dataSets.get(testCase);
        if (factory == null) {
            StringBuffer resourceName = new StringBuffer();
            if (location != null) {
                resourceName.append(location);
                if (!location.endsWith("/")) {
                    resourceName.append("/");
                }
            }
            String name = testCase.getName();
            if (name.startsWith("org.objectstyle.cayenne.")) {
                name = name.substring("org.objectstyle.cayenne.".length());
            }
            resourceName.append(name).append(".xml");
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName.toString());
            if (in == null) {
                return null;
            }
            factory = new XmlBeanFactory(new InputStreamResource(in));
            dataSets.put(testCase, factory);
        }
        return factory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
