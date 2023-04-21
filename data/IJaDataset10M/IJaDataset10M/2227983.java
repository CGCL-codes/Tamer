package org.rubypeople.rdt.internal.core.parser.ast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.rubypeople.rdt.internal.core.parser.Position;

/**
 * @author Chris
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class RubyElement implements IRubyElement {

    protected String access;

    protected String name;

    protected Position start;

    protected Position end;

    protected Set elements = new HashSet();

    public static final String PUBLIC = "public";

    public static final String PRIVATE = "private";

    public static final String READ = "read";

    public static final String WRITE = "write";

    public static final String PROTECTED = "protected";

    public RubyElement(String name, Position start) {
        this.start = start;
        this.name = name;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return
	 */
    public Position getStart() {
        return start;
    }

    /**
	 * @return
	 */
    public Position getEnd() {
        return end;
    }

    /**
	 * @return
	 */
    public String getAccess() {
        return access;
    }

    public void setAccess(String newAccess) {
        access = newAccess;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object arg0) {
        if (arg0 instanceof RubyElement) {
            RubyElement element = (RubyElement) arg0;
            return element.name.equals(this.name);
        }
        return false;
    }

    /**
	 * @return
	 */
    public int getElementCount() {
        return elements.size();
    }

    /**
	 * @param method
	 */
    public void addElement(RubyElement method) {
        elements.add(method);
    }

    /**
	 * @param element
	 * @return
	 */
    public boolean contains(RubyElement element) {
        return elements.contains(element);
    }

    public RubyElement getElement(String name) {
        for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
            RubyElement element = (RubyElement) iter.next();
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    public boolean isOutlineElement() {
        return true;
    }

    public Object[] getElements() {
        Set outlineElements = new HashSet();
        for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
            RubyElement element = (RubyElement) iter.next();
            if (element.isOutlineElement()) outlineElements.add(element); else {
                Object[] elements = element.getElements();
                if (elements.length > 0) {
                    outlineElements.addAll(Arrays.asList(elements));
                } else {
                    continue;
                }
            }
        }
        return outlineElements.toArray();
    }

    public boolean hasElements() {
        return getElements().length > 0;
    }

    public String toString() {
        return getClass().getName() + ": " + getName() + ", [" + getStart() + "," + getEnd() + "]";
    }

    /**
	 * @param lineNum
	 * @param offset
	 */
    public void setEnd(int lineNum, int offset) {
        this.end = new Position(lineNum, offset);
    }
}
