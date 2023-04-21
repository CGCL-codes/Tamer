package com.skruk.elvis.admin.ontology;

import com.hp.hpl.jena.ontology.OntResource;

/**
 * @author     skruk
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @created    20 lipiec 2004
 */
public class ElvisStatement extends ElvisResource {

    /**  Description of the Field */
    protected ElvisInstance subject = null;

    /**  Description of the Field */
    protected ElvisProperty predicate = null;

    /**  Description of the Field */
    protected ElvisInstance object = null;

    /**
	 *  Gets the uRI attribute of the ElvisStatement object
	 *
	 * @return    The uRI value
	 */
    public String getURI() {
        return null;
    }

    /**
	 *  Gets the resource attribute of the ElvisStatement object
	 *
	 * @return    The resource value
	 */
    public OntResource getResource() {
        return null;
    }

    /**  Description of the Method */
    public void remove() {
    }

    /**  Description of the Method */
    public void refresh() {
    }

    /**
	 *  Gets the super attribute of the ElvisStatement object
	 *
	 * @return    The super value
	 */
    public ElvisResource getSuper() {
        return null;
    }

    /**
	 * @return    Returns the object.
	 */
    public ElvisInstance getObject() {
        return object;
    }

    /**
	 * @param  object  The object to set.
	 */
    public void setObject(ElvisInstance object) {
        this.object = object;
    }

    /**
	 * @return    Returns the predicate.
	 */
    public ElvisProperty getPredicate() {
        return predicate;
    }

    /**
	 * @param  predicate  The predicate to set.
	 */
    public void setPredicate(ElvisProperty predicate) {
        this.predicate = predicate;
    }

    /**
	 * @return    Returns the subject.
	 */
    public ElvisInstance getSubject() {
        return subject;
    }

    /**
	 * @param  subject  The subject to set.
	 */
    public void setSubject(ElvisInstance subject) {
        this.subject = subject;
    }
}
