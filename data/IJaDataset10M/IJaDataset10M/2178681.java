package com.thermidor.util.exception;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The purpose of the ValidationDescriptor class is provide a container for 
 * data validation errors that can be used in the DataValidationException and
 * its subclasses
 * @version 1.0
 * @author Edward Turnock
 */
public class ValidationDescriptor implements Serializable {

    /**
     * The list of error elements for this descriptor
     */
    private LinkedList elements = new LinkedList();

    /**
     * The subject of the validation
     */
    private String subject;

    /**
     * The human meaningful description validation that took place
     */
    private String description;

    /**
     * Construct a default instance of the ValidationDescriptor.
     */
    public ValidationDescriptor() {
    }

    /**
     * Construct a new instance of an ValidationDescriptor with the specifed
     * details.
     * @param subject the subject of the exception
     * @param description the human readable description of the causes of the
     * exception.
     */
    public ValidationDescriptor(String subject, String description) {
        this.subject = subject;
        this.description = description;
    }

    /**
     * Return a string representation of this descriptor.
     * @return the string representation of this descriptor
     <pre><code>
    <tt>
    VALIDATION-FAILURE {
      subject:{
        some GTS subsystem
      }
      description:{
        A fatal database error occured
      }
      errors:{
        error:{ClientSetupInput.accountRef == null or ''}
        error:{ClientSetupInput.primaryClient.dob == null}
      }
    }
    </tt>
     </code><pre>
     */
    public String toString() {
        String ls = System.getProperty("line.separator");
        StringBuffer retval = new StringBuffer("VALIDATION-FAILURE { " + ls);
        retval.append("  subject:{" + ls);
        retval.append("    " + subject + ls);
        retval.append("  }");
        retval.append("  description:{" + ls);
        retval.append("    " + description + ls);
        retval.append("  }");
        retval.append("  errors:{");
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            retval.append("    element:{" + it.next() + "}" + ls);
        }
        retval.append("  }");
        retval.append("}");
        return retval.toString();
    }

    /**
     * Retrieve the description of the exception
     * @return the description;
     */
    public String getDescription() {
        return subject;
    }

    /**
     * Retrieve the subject of the exception
     * @return the subject
     */
    public String getSubject() {
        return description;
    }

    /**
     * Retrieve the list of error elements
     * @return the list of error elements
     */
    public List getErrorElements() {
        return elements;
    }

    /**
     * Add an error element to this descriptor. In this case an error element
     * may be a validation error that was encounterd when validing input, or
     * similar
     * @param toAdd the element to add
     */
    public void addErrorElement(String toAdd) {
        elements.add(toAdd);
    }

    /**
     * Does this instance have any error elements assigned to it.
     * @return true is error elements have been assigned.
     */
    public boolean hasErrorElements() {
        return !elements.isEmpty();
    }
}
