package net.sf.beezle.ssass.scss.term;

import net.sf.beezle.ssass.scss.Output;

public class Ems implements BaseTerm {

    private String ems;

    public Ems(String ems) {
        this.ems = ems;
    }

    @Override
    public void toCss(Output output) {
        output.string(ems);
    }
}
