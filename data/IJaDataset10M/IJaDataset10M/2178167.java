package net.itikko.taglets.paramtype;

import com.sun.tools.doclets.Taglet;
import com.sun.javadoc.*;
import java.util.Map;

/**
 * @author Ansgar Konermann <joshua.falken@gmx.net>
 */
public class TagletImpl implements Taglet {

    private static final String LICENSE_MESSAGE = "Parameter Type Taglet Copyright (C) 2009 Ansgar Konermann.\n" + "Distributed under the terms of the GNU General Public License. See LICENSE.TXT for details.";

    private static final String tagletName = "net.itikko.paramType";

    public static void register(Map<String, Taglet> tagletMap) {
        print(LICENSE_MESSAGE);
        if (tagletMap.containsKey(tagletName)) {
            tagletMap.remove(tagletName);
        }
        tagletMap.put(tagletName, new TagletImpl());
    }

    private static void print(String message) {
        System.out.println(message);
    }

    public String getName() {
        return tagletName;
    }

    public boolean inConstructor() {
        return false;
    }

    public boolean inField() {
        return false;
    }

    public boolean inMethod() {
        return false;
    }

    public boolean inOverview() {
        return false;
    }

    public boolean inPackage() {
        return false;
    }

    public boolean inType() {
        return false;
    }

    public boolean isInlineTag() {
        return true;
    }

    public String toString(Tag tag) {
        Doc holder = tag.holder();
        if (!holder.isMethod()) {
            print("Invalid tag " + getName() + "; only allowed for methods but encountered at " + holder.toString());
        }
        String parameterName = tag.text().trim();
        print("Holder class: " + holder.getClass().getName() + ".\n");
        MethodDoc method = (MethodDoc) holder;
        Parameter referencedParameter = findReferencedParameter(parameterName, method.parameters());
        try {
            return referencedParameter.typeName();
        } catch (NullPointerException npe) {
            print("Invalid parameter name " + parameterName + ". Method " + method.toString() + " does not have a parameter with this name.");
            return "";
        }
    }

    private Parameter findReferencedParameter(String parameterName, Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            if (parameter.name().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
    }

    public String toString(Tag[] tags) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            sb.append(toString(tag));
        }
        return sb.toString();
    }
}
