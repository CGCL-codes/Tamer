package org.scilab.forge.jlatexmath;

/**
 * An atom representing a laped atom (i.e. with no width). 
 */
public class LapedAtom extends Atom {

    private Atom at;

    private char type;

    public LapedAtom(Atom at, char type) {
        this.at = at;
        this.type = type;
    }

    public Box createBox(TeXEnvironment env) {
        Box b = at.createBox(env);
        VerticalBox vb = new VerticalBox();
        vb.add(b);
        vb.setWidth(0);
        switch(type) {
            case 'l':
                b.setShift(-b.getWidth());
                break;
            case 'r':
                b.setShift(0);
                break;
            default:
                b.setShift(-b.getWidth() / 2);
        }
        return vb;
    }
}
