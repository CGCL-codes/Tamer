package org.dmd.util.parsing;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class NamedStringArray extends ArrayList<String> {

    String name;

    public NamedStringArray(String n) {
        name = n;
    }

    public String getName() {
        return (name);
    }

    public void toOIF(int padding, StringBuffer sb) {
        for (String s : this) {
            addNameWithPadding(name, padding, sb);
            sb.append(" " + s + "\n");
        }
    }

    /**
	 * Adds the attribute name and pads it with the required number of spaces. If the name is
	 * longer than the padding, we do nothing.
	 * @param attrName The name of the attribute.
	 * @param padding  The amount of padding.
	 * @param sb       The buffer where we're building the output.
	 */
    private void addNameWithPadding(String attrName, int padding, StringBuffer sb) {
        sb.append(attrName);
        if (attrName.length() < padding) {
            for (int i = attrName.length(); i < padding; i++) sb.append(" ");
        }
    }
}
