package net.sourceforge.antme.wtk;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements a set of WirelessToolkitJar objects, implementing
 * some convenience methods for examining the set of objects as a whole.
 * 
 * @author khunter
 *
 */
public class ToolkitJarSet {

    private Set _set = new TreeSet();

    /**
	 * Default constructor.
	 *
	 */
    public ToolkitJarSet() {
    }

    /**
	 * Add a <code>WirelessToolkitJar</code> to the set.
	 * @param jar	<code>WirelessToolkitJar</code> to be added.
	 */
    public void add(ToolkitJar jar) {
        _set.add(jar);
    }

    /**
	 * Remove a <code>WirelessToolkitJar</code> from the set.
	 * @param jar	<code>WirelessToolkitJar</code> to be removed.
	 */
    public void remove(ToolkitJar jar) {
        _set.remove(jar);
    }

    /**
	 * Empty the set.
	 *
	 */
    public void clear() {
        _set.clear();
    }

    /**
	 * Return the internal set.
	 * 
	 * @return	The internal set of <code>WirelessToolkitJar</code>s.
	 */
    public Set getSet() {
        return _set;
    }

    /**
	 * Returns an <code>Iterator</code> on the internal set of <code>WirelessToolkitJar</code>s.
	 * @return	<code>Iterator</code> on the internal set of <code>WirelessToolkitJar</code>s.
	 */
    public Iterator iterator() {
        return _set.iterator();
    }

    /**
	 * return the set of flags implemented by the contained <code>WirelessToolkitJar</code>s.
	 * 
	 * @return	<code>Set</code> of <code>String</code>s, each of which will be one
	 * 			of the <code>FLAG_</code> constants from <code>WirelessToolkitJar</code>
	 * @see WirelessToolkitJar
	 */
    public Set getFlagSet() {
        TreeSet flagSet = new TreeSet();
        Iterator jarIterator = iterator();
        while (jarIterator.hasNext()) {
            ToolkitJar jar = (ToolkitJar) jarIterator.next();
            Iterator flagIterator = jar.iterator();
            while (flagIterator.hasNext()) {
                String flag = (String) flagIterator.next();
                flagSet.add(flag);
            }
        }
        return flagSet;
    }

    /**
	 * return an <code>Iterator</code> on the set of flags implemented by the contained
	 * <code>WirelessToolkitJar</code>s.
	 * 
	 * @return	<code>Set</code> of <code>String</code>s, each of which will be one
	 * 			of the <code>FLAG_</code> constants from <code>WirelessToolkitJar</code>
	 * @see WirelessToolkitJar
	 */
    public Iterator flagIterator() {
        return getFlagSet().iterator();
    }

    /**
	 * Evaluates whether or not the specified flag is set for any of the contained JARs,
	 * and thus, by implication, whether the classpath represented by this
	 * set of JARs supports the corresponding
	 * mobile spec.
	 * 
	 * @param flag	<code>String</code> indicating the flag to be checked.
	 * 				Should be one of the <code>FLAG_*</code> constants.
	 * @return	<code>true</code> if this flag is set, <code>false</code> otherwise.
	 */
    public boolean containsFlag(String flag) {
        Iterator jarIterator = iterator();
        while (jarIterator.hasNext()) {
            ToolkitJar jar = (ToolkitJar) jarIterator.next();
            if (jar.containsFlag(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * This routine checks to see if the set of jars wrapped by
	 * this class contains the specified class.  This can be used to check
	 * for support for mobile specifications not implemented by this class.
	 * 
	 * @param theClass	<code>Class</code> item to be looked for.
	 * @return	<code>true</code> if the class is supported, <code>false</code>
	 * 			otherwise.
	 * @throws IOException if the contained <code>File</code> cannot be read.
	 */
    public boolean containsClass(Class theClass) throws IOException {
        Iterator jarIterator = iterator();
        while (jarIterator.hasNext()) {
            ToolkitJar jar = (ToolkitJar) jarIterator.next();
            if (jar.containsClass(theClass)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * This routine checks to see if the set of jars wrapped by
	 * this class contains the specified class.  This can be used to check
	 * for support for mobile specifications not implemented by this class.
	 * 
	 * @param className	<code>String</code> containing the full class name
	 * 					(including package) to be checked in "dotted" form
	 * 					(i.e. <code>java.lang.String</code>)
	 * @return	<code>true</code> if the class is supported, <code>false</code>
	 * 			otherwise.
	 * @throws IOException if the contained <code>File</code> cannot be read.
	 */
    public boolean containsClass(String className) throws IOException {
        Iterator jarIterator = iterator();
        while (jarIterator.hasNext()) {
            ToolkitJar jar = (ToolkitJar) jarIterator.next();
            if (jar.containsClass(className)) {
                return true;
            }
        }
        return false;
    }
}
