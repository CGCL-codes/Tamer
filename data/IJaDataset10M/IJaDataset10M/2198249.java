package it.jb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Martino Pizol
 */
public class ClasslangBeans implements Serializable {

    private int idClasslang;

    private int idClass;

    private int idLangprog;

    private Vector classlang;

    public ClasslangBeans() {
    }

    public void popola(java.sql.ResultSet rs) {
        try {
            classlang = new Vector();
            while (rs.next()) {
                ClasslangRecord cl = new ClasslangRecord();
                cl.idclasslang = rs.getInt("ID_CLASSLANG");
                cl.idclass = rs.getInt("ID_CLASS");
                cl.idlangprog = rs.getInt("ID_LANGPROG");
                classlang.addElement(cl);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getLen() {
        return classlang.size();
    }

    public int getIdclasslang(int idx) {
        return ((ClasslangRecord) classlang.elementAt(idx)).idclasslang;
    }

    public void setIdclasslang(int idClasslang) {
        this.idClasslang = idClasslang;
    }

    public int getidClass(int idx) {
        return ((ClasslangRecord) classlang.elementAt(idx)).idclass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public int getidLangprog(int idx) {
        return ((ClasslangRecord) classlang.elementAt(idx)).idlangprog;
    }

    public void setidLangprog(int idLangprog) {
        this.idLangprog = idLangprog;
    }

    class ClasslangRecord implements Serializable {

        public int idclasslang;

        public int idclass;

        public int idlangprog;
    }
}
