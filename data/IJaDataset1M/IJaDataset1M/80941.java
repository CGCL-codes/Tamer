package edu.clemson.cs.r2jt.type;

import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.ModuleID;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;
import edu.clemson.cs.r2jt.scope.Binding;
import edu.clemson.cs.r2jt.scope.ScopeID;

public class PrimitiveType extends Type {

    private ModuleID id;

    private PosSymbol name;

    private int pars = 0;

    public PrimitiveType(ModuleID id, PosSymbol name, int pars) {
        this.id = id;
        this.name = name;
        this.pars = pars;
    }

    public ModuleID getModuleID() {
        return id;
    }

    public Location getLocation() {
        return name.getLocation();
    }

    public Symbol getSymbol() {
        return name.getSymbol();
    }

    public PosSymbol getName() {
        return name;
    }

    public Symbol getQualifier() {
        return id.getName();
    }

    public int paramCount() {
        return pars;
    }

    public Type instantiate(ScopeID sid, Binding binding) {
        return this;
    }

    public TypeName getProgramName() {
        return new TypeName(id, null, name);
    }

    public String getRelativeName(Location loc) {
        return null;
    }

    public Type toMath() {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id.toString() + ".");
        sb.append(name.toString());
        if (pars != 0) {
            sb.append("(");
            for (int i = 1; i <= pars; i++) {
                sb.append("_");
                if (i != pars) {
                    sb.append(",");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public String asString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id.toString() + ".");
        sb.append(name.toString());
        if (pars != 0) {
            sb.append("(");
            for (int i = 1; i <= pars; i++) {
                sb.append("_");
                if (i != pars) {
                    sb.append(",");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
