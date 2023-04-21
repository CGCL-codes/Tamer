package ch.HaagWeirich.Agenda;

import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.table.*;
import ch.rgw.tools.*;

/**
 * Repr�sentation eines Makros f�r Agenda
 * Ein Makro besteht aus einer Anzahl von MakroCmds.
 * Ein MakroCmd besteht aus einem Mandanten, einem TerminTyp, einem TerminStatus,
 * einer Startzeit und einer Dauer.
 */
public class Makro {

    static final String Version = "2.0.1";

    String FieldSeparator = "#";

    String LineSeparator = "�";

    java.util.Vector lines = new java.util.Vector(10, 10);

    makroCmd newCmd;

    private TimeTool date;

    private static Log log;

    static {
        log = Log.get("Makro");
    }

    public Makro() {
    }

    public Makro(String name) {
        String mak = Agenda.localCfg.get("makros/" + name, null);
        if (mak != null) {
            Log.setAlert(Agenda.client);
            if (read(mak) == false) {
                log.log("Syntaxfehler im Makro " + name, Log.ERRORS);
            }
            Log.setAlert(null);
        }
    }

    /**
   * String lesen, parsen und als Makro aufnehmen
   * R�ckabe false: Syntax fehler
   */
    public boolean read(String m) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(m, LineSeparator);
        while (st.hasMoreTokens()) {
            makroCmd cmd = new makroCmd();
            String act = st.nextToken();
            if (cmd.readString(act) == false) {
                log.log("Makrolesefehler bei " + act, Log.ERRORS);
                return false;
            }
            lines.add(cmd);
        }
        return true;
    }

    /**
   * String-Repr�sentation des aktuellen Makros generieren. Der 
   * so erzeugte String kann mit read(String) wieder in ein Makro
   * umgewandelt werden.
   */
    public String toString() {
        if (lines.isEmpty()) return "";
        StringBuffer ret = new StringBuffer(100);
        for (int i = 0; i < lines.size(); i++) {
            makroCmd cmd = (makroCmd) lines.get(i);
            String cs = cmd.toString();
            if (cs != null) {
                ret.append(cs).append(LineSeparator);
            }
        }
        return ret.toString();
    }

    public void apply(TimeSpan ts) {
        Hashtable done = new Hashtable();
        ArrayList collides = new ArrayList();
        date = new TimeTool(ts.from);
        Agenda.touch(date.toString(TimeTool.DATE_COMPACT));
        date.chop(3);
        for (int i = 0; i < lines.size(); i++) {
            makroCmd cmd = (makroCmd) lines.get(i);
            if (cmd.apply(date, done, collides) == false) {
                log.log("Fehler beim Ausf�hren von " + lines.toString(), Log.ERRORS);
            }
        }
    }

    class makroCmd {

        String Mandant, TerminTyp, TerminStatus;

        TimeSpan vobi;

        boolean killReserved;

        makroCmd() {
            Mandant = null;
            TerminTyp = null;
            TerminStatus = null;
            killReserved = false;
            vobi = null;
        }

        public String toString() {
            StringBuffer ret = new StringBuffer(30);
            if (StringTool.isNothing(Mandant)) Mandant = "";
            if (StringTool.isNothing(TerminTyp)) TerminTyp = "";
            if (StringTool.isNothing(TerminStatus)) TerminStatus = "";
            if (vobi == null) vobi = new TimeSpan(new TimeTool(), 0);
            ret.append(Mandant).append(FieldSeparator).append(TerminTyp).append(FieldSeparator);
            ret.append(TerminStatus).append(FieldSeparator);
            ret.append(vobi.from.toString(TimeTool.TIME_COMPACT));
            ret.append(vobi.until.toString(TimeTool.TIME_COMPACT)).append(FieldSeparator);
            ret.append(killReserved == true ? "1" : "0");
            return ret.toString();
        }

        boolean readString(String src) {
            if (StringTool.isNothing(src)) return false;
            String[] tokens = src.split(FieldSeparator);
            if ((tokens == null) || (tokens.length != 5)) return false;
            Mandant = tokens[0];
            TerminTyp = tokens[1];
            TerminStatus = tokens[2];
            vobi = new TimeSpan();
            if (vobi.set(tokens[3]) == false) return false;
            killReserved = tokens[4].equals("1") ? true : false;
            return true;
        }

        /**
     * Makro anwenden
     * @param when Tag
     * @param done Als Marker, dass dieser Tag schon erledigt ist
     * @return
     */
        boolean apply(TimeTool when, Hashtable done, ArrayList collides) {
            String sql;
            if (StringTool.isNothing(Mandant)) return false;
            if (StringTool.isNothing(TerminTyp)) return false;
            if (StringTool.isNothing(TerminStatus)) return false;
            if (done.get(Mandant) == null) {
                done.put(Mandant, "1");
                if (killReserved == true) {
                    sql = "DELETE FROM agnTermine WHERE BeiWem=" + JdbcLink.wrap(Mandant) + " AND TerminTyp=" + JdbcLink.wrap(AgendaEntry.TerminTypes[AgendaEntry.RESERVIERT]) + " AND Tag=" + JdbcLink.wrap(when.toString(TimeTool.DATE_COMPACT));
                    if (exec(sql) == false) return false;
                }
            }
            sql = "INSERT into agnTermine (Tag, Beginn, Dauer, BeiWem, TerminTyp, TerminStatus) " + "values (" + JdbcLink.wrap(when.toString(TimeTool.DATE_COMPACT)) + "," + (AgendaEntry.TimeInMinutes(vobi.from)) + "," + (vobi.getSeconds() / 60) + "," + JdbcLink.wrap(Mandant) + "," + TerminTyp + "," + TerminStatus + ")";
            return exec(sql);
        }

        boolean exec(String sql) {
            if (Agenda.j == null) return false;
            if (Agenda.j.isAlive() == false) return false;
            Agenda.j.exec(sql);
            return true;
        }
    }

    JTable getTable() {
        String[] MandLabels = Agenda.getMandLabels().split(",");
        JComboBox cbMandanten = new JComboBox(MandLabels);
        JComboBox cbTerminTypen = new JComboBox(AgendaEntry.TerminTypes);
        JComboBox cbTerminStatus = new JComboBox(AgendaEntry.TerminStatus);
        TableCellEditor mandEditor = new DefaultCellEditor(cbMandanten);
        TableCellEditor TTEditor = new DefaultCellEditor(cbTerminTypen);
        TableCellEditor TSEditor = new DefaultCellEditor(cbTerminStatus);
        JTable ret = new JTable(new MakroTableModel(cbMandanten, cbTerminTypen, cbTerminStatus));
        TableColumnModel tcm = ret.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        tc.setCellEditor(mandEditor);
        tc = tcm.getColumn(1);
        tc.setCellEditor(TTEditor);
        tc = tcm.getColumn(2);
        tc.setCellEditor(TSEditor);
        return ret;
    }

    @SuppressWarnings("serial")
    class MakroTableModel extends AbstractTableModel {

        private final String[] colnames = { "Mandant", "Termintyp", "Terminstatus", "von", "bis", "Res. l�schen", "Aktion" };

        JCheckBox killReserved;

        JComboBox cbTerminTypen, cbTerminStatus, cbMandanten;

        MakroTableModel(JComboBox cbMandanten, JComboBox cbTerminTypen, JComboBox cbTerminStatus) {
            this.cbMandanten = cbMandanten;
            this.cbTerminStatus = cbTerminStatus;
            this.cbTerminTypen = cbTerminTypen;
            killReserved = new JCheckBox();
        }

        public int getColumnCount() {
            return 7;
        }

        public int getRowCount() {
            return lines.size() + 1;
        }

        public String getColumnName(int col) {
            return colnames[col];
        }

        public Class getColumnClass(int col) {
            switch(col) {
                case 5:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void setValueAt(Object v, int row, int col) {
            makroCmd mc;
            if (row < lines.size()) {
                mc = (makroCmd) lines.get(row);
            } else {
                mc = new makroCmd();
                mc.vobi = new TimeSpan("00002359");
                lines.add(mc);
            }
            switch(col) {
                case 0:
                    mc.Mandant = (String) v;
                    break;
                case 1:
                    mc.TerminTyp = (String) v;
                    break;
                case 2:
                    mc.TerminStatus = (String) v;
                    break;
                case 3:
                    mc.vobi.from = new TimeTool((String) v);
                    break;
                case 4:
                    mc.vobi.until = new TimeTool((String) v);
                    break;
                case 5:
                    mc.killReserved = ((Boolean) v).booleanValue();
                    break;
                default:
                    mc.Mandant = "-";
            }
        }

        public Object getValueAt(int row, int col) {
            makroCmd mc;
            if (row >= lines.size()) {
                mc = new makroCmd();
                mc.vobi = new TimeSpan("00002359");
            } else {
                mc = (makroCmd) lines.get(row);
            }
            switch(col) {
                case 0:
                    return mc.Mandant;
                case 1:
                    return mc.TerminTyp;
                case 2:
                    return mc.TerminStatus;
                case 3:
                    if (mc.vobi == null) {
                        return "---";
                    } else {
                        return mc.vobi.from.toString(TimeTool.TIME_SMALL);
                    }
                case 4:
                    if (mc.vobi == null) {
                        return "---";
                    } else {
                        return mc.vobi.until.toString(TimeTool.TIME_SMALL);
                    }
                case 5:
                    return new Boolean(mc.killReserved);
                case 6:
                    return "OK";
                default:
                    return "ERROR";
            }
        }
    }
}
