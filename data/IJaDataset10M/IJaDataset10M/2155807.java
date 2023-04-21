package Types;

import java.util.Vector;

public class ParamList extends Type {

    private Vector p_list;

    private Type aux_argu;

    public ParamList(Type aa) {
        p_list = new Vector();
        aux_argu = aa;
    }

    public Type GetArgu() {
        return aux_argu;
    }

    public void AddType(Type nt) {
        p_list.addElement(nt);
    }

    public Vector GetParamList() {
        return p_list;
    }

    public Type SearchType(String name) {
        return null;
    }

    public boolean CheckType(Type t) {
        return (t instanceof ParamList);
    }

    public String GetType() {
        return ("Param List");
    }
}
