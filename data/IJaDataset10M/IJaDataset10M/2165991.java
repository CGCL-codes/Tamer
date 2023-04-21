package org.apache.myfaces.examples.collapsiblepanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;

/**
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 *          <p/>
 *          $Log: $
 */
public class Person implements Serializable {

    /**
     * serial id for serialisation
     */
    private static final long serialVersionUID = 1L;

    private String _surName;

    private String _firstName;

    private boolean _collapsed;

    private static Log log = LogFactory.getLog(Person.class);

    public String getSurName() {
        return _surName;
    }

    public void setSurName(String surName) {
        _surName = surName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public boolean isCollapsed() {
        return _collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        _collapsed = collapsed;
    }

    public String test() {
        log.info("test called for " + Person.class + " with name: " + getFirstName());
        return null;
    }
}
