package it.jb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Martino Pizol
 */
public class ProblemclasslangBeans implements Serializable {

    private int idProblemclasslang;

    private int idProblemclass;

    private int idLangprog;

    private Vector pclasslang;

    public ProblemclasslangBeans() {
    }

    public void popola(java.sql.ResultSet rs) {
        try {
            pclasslang = new Vector();
            while (rs.next()) {
                PclasslangRecord pcl = new PclasslangRecord();
                pcl.idproblemclasslang = rs.getInt("ID_PROBLEMCLASSLANG");
                pcl.idproblemclass = rs.getInt("ID_PROBLEMCLASS");
                pcl.idlangprog = rs.getInt("ID_LANGPROG");
                pclasslang.addElement(pcl);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void append(int idProblemClassLang, int idProblemClass, int idLangprog) {
        if (pclasslang == null) pclasslang = new Vector();
        PclasslangRecord pcl = new PclasslangRecord();
        pcl.idproblemclasslang = idProblemClassLang;
        pcl.idproblemclass = idProblemClass;
        pcl.idlangprog = idLangprog;
        pclasslang.addElement(pcl);
    }

    public int getIdproblemclasslang(int idx) {
        if (idx < 0) return idProblemclasslang; else return ((PclasslangRecord) pclasslang.elementAt(idx)).idproblemclasslang;
    }

    public void setIdproblemclasslang(int idProblemclasslang) {
        this.idProblemclasslang = idProblemclasslang;
    }

    public int getidProblemclass(int idx) {
        if (idx < 0) return idProblemclass; else return ((PclasslangRecord) pclasslang.elementAt(idx)).idproblemclass;
    }

    public void setIdProblemclass(int idProblemclass) {
        this.idProblemclass = idProblemclass;
    }

    public int getidLangprog(int idx) {
        if (idx < 0) return idLangprog; else return ((PclasslangRecord) pclasslang.elementAt(idx)).idlangprog;
    }

    public void setidLangprog(int idLangprog) {
        this.idLangprog = idLangprog;
    }

    class PclasslangRecord implements Serializable {

        public int idproblemclasslang;

        public int idproblemclass;

        public int idlangprog;
    }
}
