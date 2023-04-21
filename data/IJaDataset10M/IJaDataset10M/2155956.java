package net.sf.beezle.ssass.scss.term;

import net.sf.beezle.ssass.scss.Output;

public class Strng implements BaseTerm {

    private final String str;

    public Strng(String str) {
        this.str = str;
    }

    @Override
    public void toCss(Output output) {
        output.string(str);
    }
}
