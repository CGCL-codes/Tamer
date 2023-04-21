package de.grogra.lsystem;

import de.grogra.pf.io.*;
import de.grogra.grammar.*;
import de.grogra.xl.parser.*;
import de.grogra.rgg.model.*;

public class LSYFilter extends XLFilter {

    public LSYFilter(FilterItem item, ReaderSource source) {
        super(item, source);
    }

    @Override
    protected Tokenizer createTokenizer() {
        return new LSYTokenizer();
    }

    @Override
    protected Parser createParser(Tokenizer t) {
        LSYParser p = new LSYParser(t);
        p.className = getClassName();
        return p;
    }

    @Override
    protected String[] getPackageImports() {
        return new String[] { "java.lang", "de.grogra.lsystem" };
    }

    @Override
    protected Class[] getMemberTypeImports() {
        return new Class[0];
    }

    @Override
    protected Class[] getSingleTypeImports() {
        return new Class[] { de.grogra.rgg.Axiom.class };
    }

    @Override
    protected Class[] getStaticTypeImports() {
        return new Class[0];
    }

    @Override
    protected boolean isD2FWidening() {
        return true;
    }
}
