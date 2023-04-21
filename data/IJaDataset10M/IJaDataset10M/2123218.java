package de.guhsoft.jinto.core.editor;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author mseele
 * 
 * toDo To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ResourceProperties extends Properties {

    private static final long serialVersionUID = 2599470309541701391L;

    public synchronized Enumeration<Object> keys() {
        return new ResourceEnumerator(super.keys());
    }

    private static class ResourceEnumerator implements Enumeration<Object> {

        private List<String> enumList;

        private int index;

        ResourceEnumerator(Enumeration<Object> enumeration) {
            this.enumList = new LinkedList<String>();
            while (enumeration.hasMoreElements()) {
                this.enumList.add((String) enumeration.nextElement());
            }
            Collections.sort(this.enumList);
            this.index = 0;
        }

        /**
		 * @see java.util.Enumeration#hasMoreElements()
		 */
        public boolean hasMoreElements() {
            return (this.enumList.size() > this.index);
        }

        /**
		 * @see java.util.Enumeration#nextElement()
		 */
        public Object nextElement() {
            Object obj = this.enumList.get(this.index);
            this.index++;
            return obj;
        }
    }
}
