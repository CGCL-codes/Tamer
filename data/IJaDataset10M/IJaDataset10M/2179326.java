package misc.input;

import java.util.Vector;
import javax.swing.JTextArea;
import db.ConnectionInfo;
import db.PGConnection;
import idiom.Language;

public class UpdateDBTree {

    public Vector validDB = new Vector();

    public Vector vecConn = new Vector();

    Vector listDB;

    PGConnection conn;

    Language idiom;

    ConnectionInfo user;

    boolean killing = false;

    JTextArea LogWin;

    public UpdateDBTree(JTextArea log, Language lang, PGConnection pgconn, Vector DBs) {
        LogWin = log;
        idiom = lang;
        conn = pgconn;
        user = conn.getConnectionInfo();
        listDB = DBs;
        makeSearch();
    }

    public void makeSearch() {
        Vector tables;
        int numDB = listDB.size();
        if (numDB > 0) {
            for (int i = 0; i < numDB; i++) {
                Object o = listDB.elementAt(i);
                String dbname = o.toString();
                addTextLogMonitor(idiom.getWord("LOOKDB") + ": \"" + dbname + "\"... ");
                ConnectionInfo tmp = new ConnectionInfo(user.getHost(), dbname, user.getUser(), user.getPassword(), user.getPort(), user.requireSSL());
                PGConnection proofConn = new PGConnection(tmp, idiom);
                if (!proofConn.Fail()) {
                    addTextLogMonitor(idiom.getWord("OKACCESS"));
                    if (!dbname.equals("template1") || !dbname.equals("postgres")) {
                        vecConn.addElement(proofConn);
                        validDB.addElement(listDB.elementAt(i));
                    }
                } else addTextLogMonitor(idiom.getWord("NOACCESS"));
            }
        }
    }

    public Vector getDatabases() {
        return validDB;
    }

    public Vector getConn() {
        return vecConn;
    }

    /**
  * Metodo addTextLogMonitor
  * Imprime mensajes en el Monitor de Eventos
  */
    public void addTextLogMonitor(String msg) {
        LogWin.append(msg + "\n");
        int longiT = LogWin.getDocument().getLength();
        if (longiT > 0) LogWin.setCaretPosition(longiT - 1);
    }
}
