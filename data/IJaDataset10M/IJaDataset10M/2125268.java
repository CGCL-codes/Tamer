package org.scilab.forge.jlatexmath;

/**
 * An atom representing whitespace. The dimension values can be set using different
 * unit types.
 */
public class LaTeXAtom extends Atom {

    public LaTeXAtom() {
    }

    public Box createBox(TeXEnvironment env) {
        env = env.copy(env.getTeXFont().copy());
        env.getTeXFont().setRoman(true);
        RowAtom rat = (RowAtom) ((RomanAtom) new TeXFormula("\\mathrm{XETL}").root).base;
        HorizontalBox hb = new HorizontalBox(rat.getLastAtom().createBox(env));
        hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.7f, 0, 0).createBox(env));
        float f = new SpaceAtom(TeXConstants.UNIT_EX, 0.45f, 0, 0).createBox(env).getWidth();
        float f1 = new SpaceAtom(TeXConstants.UNIT_EX, 0.45f, 0, 0).createBox(env).getWidth();
        CharBox A = new CharBox(env.getTeXFont().getChar('A', "mathnormal", env.supStyle().getStyle()));
        A.setShift(-f);
        hb.add(A);
        hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.2f, 0, 0).createBox(env));
        hb.add(rat.getLastAtom().createBox(env));
        hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.5f, 0, 0).createBox(env));
        Box E = rat.getLastAtom().createBox(env);
        E.setShift(f1);
        hb.add(E);
        hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.25f, 0, 0).createBox(env));
        hb.add(rat.getLastAtom().createBox(env));
        return hb;
    }
}
