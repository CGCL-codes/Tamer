package org.fcrepo.server;

import java.util.Date;
import java.util.Iterator;

/**
 * Context object for testing.
 * Currently, only getSubjectValue() and now() are implemented.
 *
 * @author Edwin Shin
 * @version $Id$
 */
public class MockContext implements Context {

    /**
     * {@inheritDoc}
     */
    public Iterator actionAttributes() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator environmentAttributes() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getActionValue(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getActionValues(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public MultiValueMap getEnvironmentAttributes() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getEnvironmentValue(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getEnvironmentValues(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean getNoOp() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getPassword() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getResourceValue(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getResourceValues(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getSubjectValue(String name) {
        return "fedoraAdmin";
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSubjectValues(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int nActionValues(String name) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int nEnvironmentValues(String name) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int nResourceValues(String name) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int nSubjectValues(String name) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public Date now() {
        return new Date();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator resourceAttributes() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setActionAttributes(MultiValueMap actionAttributes) {
    }

    /**
     * {@inheritDoc}
     */
    public void setResourceAttributes(MultiValueMap resourceAttributes) {
    }

    /**
     * {@inheritDoc}
     */
    public Iterator subjectAttributes() {
        return null;
    }
}
