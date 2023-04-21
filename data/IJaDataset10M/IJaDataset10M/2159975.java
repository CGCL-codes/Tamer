package fr.univartois.cril.xtext.alloyplugin.core;

import org.eclipse.swt.graphics.Image;
import edu.mit.csail.sdg.alloy4.Pair;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import fr.univartois.cril.xtext.alloyplugin.api.ALSImageRegistry;
import fr.univartois.cril.xtext.alloyplugin.api.IALSFact;

public class Fact implements IALSFact {

    public static final Image icon = ALSImageRegistry.getImage(ALSImageRegistry.FACT_ICON_ID);

    private Pair<String, Expr> fact;

    public Fact(Pair<String, Expr> expr) {
        this.fact = expr;
    }

    public String toString() {
        return fact.toString();
    }

    public Image getIcon() {
        return icon;
    }

    public int getBeginLine() {
        return fact.b.pos.y;
    }

    public int getEndLine() {
        return fact.b.pos.y2;
    }

    public boolean isPrivate() {
        return false;
    }
}
