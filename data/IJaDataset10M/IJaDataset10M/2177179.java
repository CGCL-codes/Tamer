package Data;

import java.util.ArrayList;

/**
 *
 * @author Phillip
 */
public class c_ExpansionList {

    private ArrayList<Integer> m_list = new ArrayList<Integer>();

    private c_ExpansionDB m_expansionDB;

    public c_ExpansionList(c_ExpansionDB edb) {
        m_expansionDB = edb;
    }

    public void addExpansion(c_Expansion exp) {
        addExpansion(m_expansionDB.getEID(exp.getName()));
    }

    public void addExpansion(Integer eid) {
        m_list.add(eid);
    }

    public c_Expansion[] getList() {
        return m_list.toArray(new c_Expansion[] {});
    }

    public boolean contains(c_Expansion exp) {
        return m_list.contains(m_expansionDB.getEID(exp.getName()));
    }

    @Override
    public String toString() {
        String exps = "";
        for (Integer eid : m_list) {
            exps += m_expansionDB.getExpansion(eid).toString() + ",";
        }
        if (exps.length() > 0) {
            return exps.substring(0, exps.length() - 1);
        }
        return exps;
    }
}
