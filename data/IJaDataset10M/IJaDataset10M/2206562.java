package org.javadelic.burrow.query;

import java.util.Vector;
import org.xml.sax.Attributes;

public class GenericQueryItem extends Object {

    private String namespace;

    private String className;

    private Object queryObj;

    /**
	  * GenericQueryItem defines a standard set of routines that we can use on 
	  * any of the <code>query</code> messages from the Jabber server.  The 
	  * class contains placeholder variables for the <code>namespace</code>,
	  * <code>className</code> and the <code>queryObj</code>
	  * <p>
	  * When a new <code>&lt;query&gt; tag is encountered, the parser will 
	  * use reflection to create an object of the correct type matching the 
	  * what was specified in the XML streqam.  The information from this 
	  * parsing process is then stored in the GenericQueryItem and inserted
	  * into a Vector in a {@link JabIq} object.  
	  */
    public GenericQueryItem(String namespace, String className, Object queryObj) {
        this.namespace = namespace;
        this.className = className;
        this.queryObj = queryObj;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setQueryObj(Object queryObj) {
        this.queryObj = queryObj;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getClassName() {
        return className;
    }

    public Object getQueryObj() {
        return queryObj;
    }
}
