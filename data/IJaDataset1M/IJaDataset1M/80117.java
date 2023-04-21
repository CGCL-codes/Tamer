package com.turtle3d.user.grammars;

import java.util.ArrayList;
import com.turtle3d.formallanguage.grammar.Grammar;
import com.turtle3d.formallanguage.parser.SimpleParser;
import com.turtle3d.formallanguage.symbol.Method;
import com.turtle3d.formallanguage.symbol.Symbol;
import com.turtle3d.formallanguage.symbol.SymbolClass;
import com.turtle3d.formallanguage.symbol.SymbolFactory;

public class Fern extends Grammar {

    @Override
    public void define() {
        setAxiom(SymbolFactory.getSymbolFactory().createSymbolofClass("B"));
        SymbolClass turnAngle2 = new SymbolClass("-", "-(2)");
        turnAngle2.addMethod(new Method() {

            public boolean calculate(ArrayList<Symbol> symbolSequence, int symbolIndex, Symbol calculatedSymbol) {
                calculatedSymbol.setParameter("turnAngle", calculatedSymbol.getParameter("turnAngle2"));
                return true;
            }
        });
        SymbolFactory.getSymbolFactory().registerSymbolClass(turnAngle2);
        SymbolClass mirror = new SymbolClass("S", "S(mirror)");
        mirror.addMethod(new Method() {

            public boolean calculate(ArrayList<Symbol> symbolSequence, int symbolIndex, Symbol calculatedSymbol) {
                calculatedSymbol.setParameter("scaleMultiplierX", -1f);
                calculatedSymbol.setParameter("scaleMultiplierY", 1f);
                calculatedSymbol.setParameter("scaleMultiplierZ", 1f);
                return true;
            }
        });
        SymbolFactory.getSymbolFactory().registerSymbolClass(mirror);
        String fernModule = "[-S(mirror)SSSB]-(2)/SF[+SSSB]-(2)/SF";
        String fernString = "";
        for (int i = 0; i < 5; i++) fernString += fernModule;
        addProduction(SimpleParser.parseProduction("B->F[+SSSB]-(2)/SF" + fernString));
    }

    public void setRequiredParameters() {
        setRequiredParameter("turnAngle", 60f);
        setRequiredParameter("turnAngle2", 3f);
        setRequiredParameter("distance", 2f);
        setRequiredParameter("radius", 0.06f);
        setRequiredParameter("pitchAngle", 5f);
        setRequiredParameter("leafSize", 10f);
        setRequiredParameter("scaleMultiplier", 0.78f);
    }
}
