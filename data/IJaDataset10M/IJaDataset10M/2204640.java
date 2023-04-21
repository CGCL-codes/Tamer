package com.sun.org.apache.xerces.internal.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;

/**
 * Namespace support for XML document handlers. This class doesn't
 * perform any error checking and assumes that all strings passed
 * as arguments to methods are unique symbols. The SymbolTable class
 * can be used for this purpose.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: NamespaceSupport.java,v 1.3 2005/09/26 13:03:00 sunithareddy Exp $
 */
public class NamespaceSupport implements NamespaceContext {

    /**
     * Namespace binding information. This array is composed of a
     * series of tuples containing the namespace binding information:
     * &lt;prefix, uri&gt;. The default size can be set to anything
     * as long as it is a power of 2 greater than 1.
     *
     * @see #fNamespaceSize
     * @see #fContext
     */
    protected String[] fNamespace = new String[16 * 2];

    /** The top of the namespace information array. */
    protected int fNamespaceSize;

    /**
     * Context indexes. This array contains indexes into the namespace
     * information array. The index at the current context is the start
     * index of declared namespace bindings and runs to the size of the
     * namespace information array.
     *
     * @see #fNamespaceSize
     */
    protected int[] fContext = new int[8];

    /** The current context. */
    protected int fCurrentContext;

    protected String[] fPrefixes = new String[16];

    /** Default constructor. */
    public NamespaceSupport() {
    }

    /**
     * Constructs a namespace context object and initializes it with
     * the prefixes declared in the specified context.
     */
    public NamespaceSupport(NamespaceContext context) {
        pushContext();
        Enumeration prefixes = context.getAllPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement();
            String uri = context.getURI(prefix);
            declarePrefix(prefix, uri);
        }
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#reset()
     */
    public void reset() {
        fNamespaceSize = 0;
        fCurrentContext = 0;
        fNamespace[fNamespaceSize++] = XMLSymbols.PREFIX_XML;
        fNamespace[fNamespaceSize++] = NamespaceContext.XML_URI;
        fNamespace[fNamespaceSize++] = XMLSymbols.PREFIX_XMLNS;
        fNamespace[fNamespaceSize++] = NamespaceContext.XMLNS_URI;
        fContext[fCurrentContext] = fNamespaceSize;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#pushContext()
     */
    public void pushContext() {
        if (fCurrentContext + 1 == fContext.length) {
            int[] contextarray = new int[fContext.length * 2];
            System.arraycopy(fContext, 0, contextarray, 0, fContext.length);
            fContext = contextarray;
        }
        fContext[++fCurrentContext] = fNamespaceSize;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#popContext()
     */
    public void popContext() {
        fNamespaceSize = fContext[fCurrentContext--];
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#declarePrefix(String, String)
     */
    public boolean declarePrefix(String prefix, String uri) {
        if (prefix == XMLSymbols.PREFIX_XML || prefix == XMLSymbols.PREFIX_XMLNS) {
            return false;
        }
        for (int i = fNamespaceSize; i > fContext[fCurrentContext]; i -= 2) {
            if (fNamespace[i - 2] == prefix) {
                fNamespace[i - 1] = uri;
                return true;
            }
        }
        if (fNamespaceSize == fNamespace.length) {
            String[] namespacearray = new String[fNamespaceSize * 2];
            System.arraycopy(fNamespace, 0, namespacearray, 0, fNamespaceSize);
            fNamespace = namespacearray;
        }
        fNamespace[fNamespaceSize++] = prefix;
        fNamespace[fNamespaceSize++] = uri;
        return true;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#getURI(String)
     */
    public String getURI(String prefix) {
        for (int i = fNamespaceSize; i > 0; i -= 2) {
            if (fNamespace[i - 2] == prefix) {
                return fNamespace[i - 1];
            }
        }
        return null;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#getPrefix(String)
     */
    public String getPrefix(String uri) {
        for (int i = fNamespaceSize; i > 0; i -= 2) {
            if (fNamespace[i - 1] == uri) {
                if (getURI(fNamespace[i - 2]) == uri) return fNamespace[i - 2];
            }
        }
        return null;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#getDeclaredPrefixCount()
     */
    public int getDeclaredPrefixCount() {
        return (fNamespaceSize - fContext[fCurrentContext]) / 2;
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#getDeclaredPrefixAt(int)
     */
    public String getDeclaredPrefixAt(int index) {
        return fNamespace[fContext[fCurrentContext] + index * 2];
    }

    public Iterator getPrefixes() {
        int count = 0;
        if (fPrefixes.length < (fNamespace.length / 2)) {
            String[] prefixes = new String[fNamespaceSize];
            fPrefixes = prefixes;
        }
        String prefix = null;
        boolean unique = true;
        for (int i = 2; i < (fNamespaceSize - 2); i += 2) {
            prefix = fNamespace[i + 2];
            for (int k = 0; k < count; k++) {
                if (fPrefixes[k] == prefix) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                fPrefixes[count++] = prefix;
            }
            unique = true;
        }
        return new IteratorPrefixes(fPrefixes, count);
    }

    /**
     * @see com.sun.org.apache.xerces.internal.xni.NamespaceContext#getAllPrefixes()
     */
    public Enumeration getAllPrefixes() {
        int count = 0;
        if (fPrefixes.length < (fNamespace.length / 2)) {
            String[] prefixes = new String[fNamespaceSize];
            fPrefixes = prefixes;
        }
        String prefix = null;
        boolean unique = true;
        for (int i = 2; i < (fNamespaceSize - 2); i += 2) {
            prefix = fNamespace[i + 2];
            for (int k = 0; k < count; k++) {
                if (fPrefixes[k] == prefix) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                fPrefixes[count++] = prefix;
            }
            unique = true;
        }
        return new Prefixes(fPrefixes, count);
    }

    public Vector getPrefixes(String uri) {
        int count = 0;
        String prefix = null;
        boolean unique = true;
        Vector prefixList = new Vector();
        for (int i = fNamespaceSize; i > 0; i -= 2) {
            if (fNamespace[i - 1] == uri) {
                if (!prefixList.contains(fNamespace[i - 2])) prefixList.add(fNamespace[i - 2]);
            }
        }
        return prefixList;
    }

    /** 
     * Checks whether a binding or unbinding for
     * the given prefix exists in the context.
     * 
     * @param prefix The prefix to look up. 
     * 
     * @return true if the given prefix exists in the context
     */
    public boolean containsPrefix(String prefix) {
        for (int i = fNamespaceSize; i > 0; i -= 2) {
            if (fNamespace[i - 2] == prefix) {
                return true;
            }
        }
        return false;
    }

    /** 
     * Checks whether a binding or unbinding for
     * the given prefix exists in the current context.
     * 
     * @param prefix The prefix to look up. 
     * 
     * @return true if the given prefix exists in the current context
     */
    public boolean containsPrefixInCurrentContext(String prefix) {
        for (int i = fContext[fCurrentContext]; i < fNamespaceSize; i += 2) {
            if (fNamespace[i] == prefix) {
                return true;
            }
        }
        return false;
    }

    protected final class IteratorPrefixes implements Iterator {

        private String[] prefixes;

        private int counter = 0;

        private int size = 0;

        /**
         * Constructor for Prefixes.
         */
        public IteratorPrefixes(String[] prefixes, int size) {
            this.prefixes = prefixes;
            this.size = size;
        }

        /**
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasNext() {
            return (counter < size);
        }

        /**
         * @see java.util.Enumeration#nextElement()
         */
        public Object next() {
            if (counter < size) {
                return fPrefixes[counter++];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < size; i++) {
                buf.append(prefixes[i]);
                buf.append(" ");
            }
            return buf.toString();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    protected final class Prefixes implements Enumeration {

        private String[] prefixes;

        private int counter = 0;

        private int size = 0;

        /**
         * Constructor for Prefixes.
         */
        public Prefixes(String[] prefixes, int size) {
            this.prefixes = prefixes;
            this.size = size;
        }

        /**
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasMoreElements() {
            return (counter < size);
        }

        /**
         * @see java.util.Enumeration#nextElement()
         */
        public Object nextElement() {
            if (counter < size) {
                return fPrefixes[counter++];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < size; i++) {
                buf.append(prefixes[i]);
                buf.append(" ");
            }
            return buf.toString();
        }
    }
}
