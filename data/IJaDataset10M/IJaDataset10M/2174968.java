package org.gcreator.pineapple.pinedl;

import java.util.Collections;

/**
 * Represents a function.
 *
 * @author Luís Reis
 */
public class Function extends FunctionConstructorBase {

    public Type returnType = null;

    public boolean isStatic = false;

    public boolean isFinal;

    public String name = "";

    public Function() {
    }

    public Function(AccessControlKeyword access, boolean isStatic, Type returnType, String name, boolean isFinal, Argument... arguments) {
        this.access = access;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.name = name;
        this.isFinal = isFinal;
        Collections.addAll(this.arguments, arguments);
    }

    @Override
    public String toString() {
        String s = access.toString() + (isFinal ? " final " : " ") + (isStatic ? " static " : " ") + returnType;
        s += " " + name + "(";
        boolean first = true;
        for (Argument a : arguments) {
            if (!first) {
                s += ", ";
            }
            s += a.toString();
            first = false;
        }
        s += ")";
        s += content.toString();
        return s;
    }

    public Function optimize() {
        content.optimize();
        return this;
    }

    public String getName() {
        return name;
    }
}
