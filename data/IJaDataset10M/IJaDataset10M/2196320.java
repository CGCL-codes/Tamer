package org.scilab.forge.jlatexmath;

/**
 * An atom representing a reflected Atom.
 */
public class ReflectAtom extends Atom {

    private Atom base;

    public ReflectAtom(Atom base) {
        this.type = base.type;
        this.base = base;
    }

    public Box createBox(TeXEnvironment env) {
        return new ReflectBox(base.createBox(env));
    }
}
