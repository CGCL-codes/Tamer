package org.genxdm.processor.w3c.xs.impl.xmlrep;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

final class XMLAttributeGroupHierarchyComparator implements Comparator<XMLAttributeGroup> {

    public XMLAttributeGroupHierarchyComparator() {
    }

    public int compare(final XMLAttributeGroup g1, final XMLAttributeGroup g2) {
        final HashSet<XMLAttributeGroup> targets = new HashSet<XMLAttributeGroup>();
        final Stack<XMLAttributeGroup> stack = new Stack<XMLAttributeGroup>();
        if (g1.getGroups().size() > 0) {
            for (final XMLAttributeGroup attributeGroup : g1.getGroups()) {
                stack.push(attributeGroup);
            }
            while (!stack.isEmpty()) {
                final XMLAttributeGroup popped = stack.pop();
                targets.add(popped);
                if (popped.getGroups().size() > 0) {
                    for (final XMLAttributeGroup attributeGroup : popped.getGroups()) {
                        stack.push(attributeGroup);
                    }
                }
            }
        }
        if (targets.contains(g2)) {
            return +1;
        } else {
            return -1;
        }
    }
}
