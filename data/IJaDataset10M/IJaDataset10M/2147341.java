package org.melati.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Adds {link #skip} to <code>Enumeration</code>.
 */
public interface SkipEnumeration extends Enumeration {

    void skip() throws NoSuchElementException;
}
