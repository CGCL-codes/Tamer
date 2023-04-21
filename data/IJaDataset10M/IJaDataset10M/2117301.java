package com.calipso.xmleditor;

import org.exolab.castor.xml.schema.*;
import java.util.Enumeration;

/**
 *
 * User: soliveri
 * Date: 25-sep-2003
 * Time: 14:01:55
 *
 */
public class SchemaRootSearcher {

    public static String searchRootFrom(Schema schema) {
        String root = null;
        Enumeration enumeration = schema.getElementDecls();
        for (int i = 0; enumeration.hasMoreElements(); i++) {
            ElementDecl elemenentDecl = (ElementDecl) enumeration.nextElement();
            boolean possibleRoot = isRoot(elemenentDecl.getName(), schema.getElementDecls(), i);
            if (possibleRoot) {
                root = elemenentDecl.getName();
                System.out.println(root);
                break;
            }
        }
        return root;
    }

    private static boolean isRoot(String current, Enumeration enumeration, int j) {
        int i;
        int count = 0;
        for (i = 0; enumeration.hasMoreElements(); i++) {
            if (i != j) {
                ElementDecl elementDecl = (ElementDecl) enumeration.nextElement();
                XMLType xmlType = elementDecl.getType();
                if (xmlType != null) {
                    if (xmlType.isComplexType()) {
                        ComplexType type = (ComplexType) elementDecl.getType();
                        Enumeration elements = type.enumerate();
                        if (!isElementIncluded(elements, current)) {
                            count++;
                        }
                    }
                }
            }
        }
        if (count == i - 1) {
            return true;
        }
        return false;
    }

    private static boolean isElementIncluded(Enumeration elements, String current) {
        boolean found = false;
        while (elements.hasMoreElements()) {
            Object object = elements.nextElement();
            if (object instanceof Group) {
                Group group = (Group) object;
                ContentModelGroup modelGroup = group.getContentModelGroup();
                Enumeration enumeration = modelGroup.enumerate();
                if (isElementIncluded(enumeration, current)) {
                    found = true;
                }
            } else if (object instanceof ElementDecl) {
                ElementDecl elementDecl = (ElementDecl) object;
                if (elementDecl.getName().equals(current)) {
                    return true;
                }
            }
        }
        return found;
    }
}
