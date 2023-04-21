package org.scilab.forge.jlatexmath;

/**
 * An atom representing a italic atom. 
 */
public class ItAtom extends Atom {

    private Atom base;

    public ItAtom(Atom base) {
        this.base = base;
    }

    public Box createBox(TeXEnvironment env) {
        env = env.copy(env.getTeXFont().copy());
        env.getTeXFont().setIt(true);
        Box box = base.createBox(env);
        return box;
    }
}
