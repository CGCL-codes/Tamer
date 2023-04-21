package fr.univartois.cril.alloyplugin.core;

import org.eclipse.swt.graphics.Image;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import fr.univartois.cril.alloyplugin.api.ALSImageRegistry;
import fr.univartois.cril.alloyplugin.api.IALSPredicate;

public class Predicate implements IALSPredicate {

    public static final Image icon = ALSImageRegistry.getImage(ALSImageRegistry.PREDICATE_ICON_ID);

    private Func func;

    public Predicate(Func func) {
        this.func = func;
    }

    public String toString() {
        return func.toString().substring(10);
    }

    public Image getIcon() {
        return icon;
    }

    public int getBeginLine() {
        return func.pos.y;
    }

    public int getEndLine() {
        return func.pos.y2;
    }

    public String getId() {
        return func.label.substring(5);
    }

    public boolean isPrivate() {
        return func.isPrivate != null;
    }
}
