package net.sf.lunareclipse.ast.declarations;

import net.sf.lunareclipse.core.LuaConstants;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;

public class LuaTableDeclaration extends FieldDeclaration {

    public LuaTableDeclaration(String name, int nameStart, int nameEnd, int declStart, int declEnd) {
        super(name, nameStart, nameEnd, declStart, declEnd);
    }

    public boolean isLocal() {
        return (modifiers & LuaConstants.AccLocal) != 0;
    }

    public String toString() {
        String buf = "";
        if (this.isLocal()) {
            buf += "local ";
        }
        if (this.getName() != null) {
            buf += " " + this.getName();
        }
        return buf;
    }
}
