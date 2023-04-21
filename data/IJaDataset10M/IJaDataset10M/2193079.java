package as.ide.core.dom;

import java.util.ArrayList;

public class ParamList {

    private ArrayList<Param> list;

    public ParamList() {
        list = new ArrayList<Param>();
    }

    public void addParam(Param p) {
        list.add(p);
    }

    public int getParamCount() {
        return list.size();
    }

    public Param[] getParams() {
        return list.toArray(new Param[list.size()]);
    }

    /**
	 * Get the parameters string, with the type declaration and possible default value
	 * of each parameter.
	 */
    public String toString() {
        if (list == null || list.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = list.size(); i < n; i++) {
            Param pm = list.get(i);
            sb.append(pm.toString());
            if (i < n - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
	 * Get the parameters string, without the type declaration or default value
	 * of each parameter.
	 */
    public String getString() {
        if (list == null || list.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = list.size(); i < n; i++) {
            Param pm = list.get(i);
            sb.append(pm.getString());
            if (i < n - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof Param[]) {
            Param[] pms = (Param[]) o;
            if (list.size() != pms.length) return false;
            for (int i = 0; i < pms.length; i++) {
                Param p = list.get(0);
                Param q = pms[i];
                if (!p.equals(q)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
