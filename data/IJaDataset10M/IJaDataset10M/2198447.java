package com.simonstl.moe;

import java.util.ArrayList;
import java.util.Iterator;

/**
* <p>The ComponentList class extends ArrayList with functions for matching objects.  ComponentList is a container for MOE objects which were generated outside of a document parsing environment - from a rules file, for instance.  Developers who need to put a collection of MOE objects into a cluster with semi-transparent matching capability should use ComponentList.</p>
*
* <p>Version 0.01 is a foundation built on ArrayList .</p>
*
* @version 0.01 23 August 2001
* @author Simon St.Laurent
*/
public class ComponentList extends ArrayList implements ComponentListI {

    public String getTextContent() {
        String returnVal = "";
        int size = this.size();
        for (int i = 0; i < size; i++) {
            CoreComponentI node = (CoreComponentI) this.get(i);
            returnVal = returnVal + node.getTextContent();
        }
        return returnVal;
    }

    public String toString() {
        return this.toElements();
    }

    public String toString(ComponentSet namespaceContext) {
        return this.toElements(namespaceContext);
    }

    public String toAttributes(ComponentSet namespaceContext) {
        return "ComponentList.toAttributes(namespaceContext) not yet implemented.";
    }

    public String toAttributes() {
        return "ComponentList.toAttributes() not yet implemented.";
    }

    public String toElements(ComponentSet namespaceContext) {
        String returnVal = "";
        Iterator it = this.iterator();
        while (it.hasNext()) {
            CoreComponentI next = (CoreComponentI) it.next();
            returnVal = returnVal + (next.toString(namespaceContext));
        }
        return returnVal;
    }

    public String toElements() {
        String returnVal = "";
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            returnVal = returnVal + (next.toString());
        }
        return returnVal;
    }

    public void setParent(CoreComponentI _parent) {
        Iterator it = this.iterator();
        while (it.hasNext()) {
            CoreComponentI next = (CoreComponentI) it.next();
            next.setParent(_parent);
        }
    }

    public Object clone() {
        ComponentList returnVal = new ComponentList();
        Iterator it = this.iterator();
        while (it.hasNext()) {
            CoreComponentI next = (CoreComponentI) it.next();
            returnVal.add(next.clone());
        }
        return (Object) returnVal;
    }
}
