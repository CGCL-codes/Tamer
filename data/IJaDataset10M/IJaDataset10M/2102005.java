package cat.quadriga.parsers.code.symbols;

import cat.quadriga.parsers.code.CodeZone;
import cat.quadriga.parsers.code.CodeZoneClass;
import cat.quadriga.parsers.code.types.BaseType;

public class LocalVariableSymbol extends BaseSymbol {

    public BaseType type;

    public int modifiers;

    public int position;

    public CodeZone cz;

    public LocalVariableSymbol(int modifiers, BaseType type, String name, int position, CodeZone cz) {
        super(name);
        this.type = type;
        this.modifiers = modifiers;
        this.position = position;
        this.cz = cz;
    }

    public void updateCodeZone(CodeZone cz) {
        if (this.cz.beginLine() < cz.beginLine() || (this.cz.beginLine() == cz.beginLine() && this.cz.beginColumn() < cz.beginColumn())) {
            this.cz = new CodeZoneClass(cz, this.cz);
        }
        if (this.cz.endLine() > cz.endLine() || (this.cz.endLine() == cz.endLine() && this.cz.endColumn() > cz.endColumn())) {
            this.cz = new CodeZoneClass(this.cz, cz);
        }
    }

    @Override
    public String createTreeStringRepresentation() {
        return "Symbol Var [ " + name + " ] Type [ " + type.toString() + " ]";
    }

    public String toString() {
        return "Local var \"" + name + "\" {" + type + "} [" + position + "]";
    }
}
