package net.sf.beezle.ssass.scss.term;

import net.sf.beezle.ssass.scss.Output;

public class Length implements BaseTerm {

    private final String len;

    public Length(String len) {
        this.len = len;
    }

    @Override
    public void toCss(Output output) {
        output.string(len);
    }
}
