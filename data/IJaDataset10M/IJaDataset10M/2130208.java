package unclej.javatype;

import unclej.util.BracketParser;

/**
 * @author scottv
 */
public class GenericDeclaration {

    public GenericDeclaration(String text) {
        if (!text.startsWith("<") || !text.endsWith(">")) {
            throw new IllegalArgumentException("should be in angle brackets: " + text);
        }
        text = text.substring(1, text.length() - 1).trim();
        if (text.length() == 0) {
            throw new IllegalArgumentException("empty brackets");
        }
        String[] params = new BracketParser(text).split(',');
        variables = new GenericVariable[params.length];
        for (int i = 0; i < params.length; i++) {
            variables[i] = new GenericVariable(params[i].trim());
        }
    }

    public int getVariableCount() {
        return variables.length;
    }

    public GenericVariable getVariable(int n) {
        return variables[n];
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < variables.length; i++) {
            hash ^= variables[i].hashCode();
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj.getClass() == this.getClass()) {
            return equals((GenericDeclaration) obj);
        } else {
            return false;
        }
    }

    public boolean equals(GenericDeclaration that) {
        if (that.variables.length != this.variables.length) {
            return false;
        } else {
            for (int i = 0; i < variables.length; i++) {
                if (!that.variables[i].equals(this.variables[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public String toString() {
        return toString(true);
    }

    public String toString(boolean withFullTypes) {
        StringBuffer buf = new StringBuffer();
        buf.append('<');
        for (int i = 0; i < variables.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(variables[i].toString(withFullTypes));
        }
        buf.append('>');
        return buf.toString();
    }

    private final GenericVariable[] variables;
}
