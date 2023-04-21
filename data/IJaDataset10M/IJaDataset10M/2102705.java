package com.turtle3d.user.grammars;

import com.turtle3d.formallanguage.grammar.Grammar;
import com.turtle3d.formallanguage.parser.SimpleParser;
import com.turtle3d.formallanguage.symbol.SymbolFactory;

public class Peano2D extends Grammar {

    @Override
    public void define() {
        setAxiom(SymbolFactory.getSymbolFactory().createSymbolofClass("F"));
        addProduction(SimpleParser.parseProduction("F->F+F-F-F-F+F+F+F-F"));
    }

    public void setRequiredParameters() {
        setRequiredParameter("turnAngle", 90f);
    }
}
