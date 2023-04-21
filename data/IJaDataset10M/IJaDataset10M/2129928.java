package fr.univartois.cril.xtext2.alloyplugin.core;

import org.eclipse.swt.graphics.Image;
import edu.mit.csail.sdg.alloy4.Pos;
import fr.univartois.cril.xtext2.alloyplugin.api.IALSTreeDecorated;
import fr.univartois.cril.xtext2.alloyplugin.api.Iconable;

public class UnsatCorePos implements IALSTreeDecorated, Iconable {

    private final Pos pos;

    public UnsatCorePos(Pos pos) {
        this.pos = pos;
    }

    public int getBeginLine() {
        return pos.y;
    }

    public int getEndLine() {
        return pos.y2;
    }

    public int getX() {
        return pos.x;
    }

    public int getX2() {
        return pos.x2;
    }

    public boolean isPrivate() {
        return false;
    }

    public Image getIcon() {
        return null;
    }

    @Override
    public String toString() {
        return "Unsat core member: line  " + pos.y + " Columns  " + pos.x + "|" + pos.x2;
    }
}
