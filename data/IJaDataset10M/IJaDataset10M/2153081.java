package ch.rgw.IO;

import ch.rgw.swingtools.ButtonFactory;
import ch.rgw.swingtools.DateSelector;
import ch.rgw.swingtools.LblButton;
import ch.rgw.swingtools.LblCombo;
import ch.rgw.swingtools.LblComponent;
import ch.rgw.swingtools.LblInput;
import ch.rgw.swingtools.SwingHelper;
import ch.rgw.tools.Colors;
import ch.rgw.tools.StringTool;
import ch.rgw.tools.TimeTool;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

/**
 * SettingsUI kann visuelle Repr�sentationen eines Settings-Objekts erzeugen
 * und zur Benutzerinteraktion darstellen (resp. die entsprechenden Swing-
 * Objekte liefern). Dazu wird ein entsprechendes Settings-Objekt ben�tigt, sowie
 * ein String-Array, das definiert, welche Werte aus dem Settings-Objekt dargestellt
 * werden sollen, und wie diese darzustellen sind.<br>
 * Zur Darstellung wird JGoodies Forms verwendet. 
 * @author Gerry
 * @see Settings
 */
public class SettingsUI {

    public static final String Version() {
        return "0.3.4";
    }

    Settings cfg;

    private ArrayList rows;

    private suiAdapter ad;

    private JPanel thePanel;

    private static final int TYPE = 0;

    private static final int LABEL = 1;

    private static final int PARM = 2;

    private static final int DEFAULT = 3;

    private static final int CHOICES = 4;

    private static final int TYP_UNKNOWN = 0;

    private static final int TYP_NODE = 1;

    private static final int TYP_PLAIN = 2;

    private static final int TYP_CHOICE = 3;

    private static final int TYP_TREECHOICE = 4;

    private static final int TYP_SEPARATOR = 5;

    private static final int TYP_INPUT = 6;

    private static final int TYP_COLOR = 7;

    private static final int TYP_ADAPTER = 8;

    private static final int TYP_DATE = 9;

    private static final int TYP_BOOL = 10;

    /**
     * Ein SettingsUI-Objekt mit den in set definierten Konfigurationszeilen erstellen
     * @param c Das zugeh�rige Settings-Objekt
     * @param d String Array mit einem String pro Parameter der Form:<br>
     * Typ[::Label::Parameter::Vorgabe[::Werteliste]]<br>
     * Typ:=SEPARATOR | NODE | PLAIN | CHOICE | TREECHOICE | COLORCHOOSE 
     *    | ADAPTER | DATE<br>
     * Label:=text
     */
    public SettingsUI(Settings c, String[] d, suiAdapter ad) {
        cfg = c;
        rows = new ArrayList();
        this.ad = ad;
        for (int i = 0; i < d.length; i++) {
            rowspec r = new rowspec(d[i]);
            add_node(r, false);
        }
    }

    private void add_node(rowspec r, boolean recurse) {
        rows.add(r);
        if (r.typ == TYP_NODE) {
            if (recurse == true) {
                String[] k = cfg.nodes(r.param);
                for (int i = 0; i < k.length; i++) {
                    String base = k[i];
                    int li = k[i].lastIndexOf('/');
                    if (li != -1) {
                        base = "-" + k[i].substring(li + 1);
                    }
                    rowspec rs = new rowspec("NODE::" + base + "::" + k[i]);
                    add_node(rs, true);
                }
            }
            String[] k = cfg.keys(r.param);
            for (int i = 0; i < k.length; i++) {
                String base = k[i];
                int li = k[i].lastIndexOf('/');
                if (li != -1) {
                    base = "-" + k[i].substring(li + 1);
                }
                rowspec rs = new rowspec("PLAIN::" + base + "::" + k[i]);
                rows.add(rs);
            }
        }
    }

    public JPanel getPanel() {
        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));
        ret.add(Box.createVerticalGlue());
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            rowspec r = (rowspec) it.next();
            switch(r.typ) {
                case TYP_PLAIN:
                    LblInput li = new LblInput(r.lbl, 10, LblComponent.VERTICAL);
                    r.setComponent(li.getElem());
                    ret.add(li);
                    break;
                case TYP_SEPARATOR:
                case TYP_NODE:
                    ret.add(new JLabel(r.lbl));
                    break;
                case TYP_CHOICE:
                    String[] c = r.choices.split(",");
                    r.setComponent(new JComboBox(c));
                    LblCombo lc = new LblCombo(r.lbl, 15, LblComponent.VERTICAL, (JComboBox) r.cmp);
                    JPanel dummy = new JPanel();
                    dummy.add(lc);
                    ret.add(dummy);
                    break;
                case TYP_COLOR:
                    JButton but = ButtonFactory.getButton("W�hlen...", "col", new blisten(r, null), null);
                    LblButton lb = new LblButton(r.lbl, 15, LblComponent.VERTICAL, but);
                    r.setComponent(but);
                    dummy = new JPanel();
                    dummy.add(lb);
                    ret.add(dummy);
                    break;
                case TYP_ADAPTER:
                    JComponent view = ad.createView(r.param);
                    r.setComponent(view);
                    ret.add(view);
                    break;
                case TYP_DATE:
                    but = ButtonFactory.getButton("Datum...", "date", new dlisten(r), null);
                    lb = new LblButton(r.lbl, 15, LblComponent.VERTICAL, but);
                    r.setComponent(but);
                    dummy = new JPanel();
                    dummy.add(lb);
                    ret.add(dummy);
                    break;
                case TYP_BOOL:
                    JCheckBox cbool = new JCheckBox(r.lbl);
                    r.setComponent(cbool);
                    break;
                case TYP_UNKNOWN:
                    ret.add(new JLabel("Unknown"));
                    break;
            }
            r.read();
        }
        ret.add(Box.createVerticalGlue());
        thePanel = ret;
        return ret;
    }

    /** 
     * Ein JPanel zur Darstellung der deklarierten Settings zur�ckliefern
     * @param cols Spaltenzahl 
     * @param lsize Mindestbreite der labels
     * @param tsize Mindestbreite der Textfelder
     */
    public JPanel getPanel(int cols, int lsize, int tsize) {
        FormLayout layout = new FormLayout("max(p;" + lsize + "),3dlu,max(" + tsize + ";p):g", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            rowspec r = (rowspec) it.next();
            switch(r.typ) {
                case TYP_PLAIN:
                    r.setComponent(new JTextField());
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_BOOL:
                    r.setComponent(new JCheckBox());
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_SEPARATOR:
                case TYP_NODE:
                    builder.appendSeparator(r.lbl);
                    break;
                case TYP_CHOICE:
                    String[] c = r.choices.split(",");
                    r.setComponent(new JComboBox(c));
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_INPUT:
                    r.setComponent(new JTextField());
                    builder.append(new JTextField(), r.cmp);
                    break;
                case TYP_COLOR:
                    JButton but = ButtonFactory.getButton("W�hlen...", "col", new blisten(r, null), null);
                    r.setComponent(but);
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_ADAPTER:
                    JComponent view = ad.createView(r.param);
                    r.setComponent(view);
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_DATE:
                    but = ButtonFactory.getButton("Datum...", "date", new dlisten(r), null);
                    r.setComponent(but);
                    builder.append(r.lbl, r.cmp);
                    break;
                case TYP_UNKNOWN:
                    builder.append("Fehler", new JLabel("Unbekannter Typ"));
                    break;
            }
            r.read();
            builder.nextLine();
        }
        thePanel = builder.getPanel();
        return thePanel;
    }

    /** Die komplette Userinteraktion zur Einstellung der deklarierten Settings
     * abwickeln
     * @return true: user hat mit "OK" beendet  oder false: user hat "Cancel" geklickt.
     */
    public boolean editSettings(Component parent, String title) {
        EditDlg dlg = new EditDlg(getPanel(1, 0, 0));
        dlg.setTitle(title);
        dlg.setModal(true);
        if (parent == null) {
            SwingHelper.centerScreen(dlg);
        } else {
            SwingHelper.centerComponent(dlg, parent);
        }
        dlg.setVisible(true);
        return dlg.answer;
    }

    /** Werte aus Settings ins SettingsUI laden */
    public void reload() {
        java.util.Iterator it = rows.iterator();
        while (it.hasNext()) {
            rowspec r = (rowspec) it.next();
            r.read();
        }
    }

    /** Werte aus SettingsUI in die Settings zur�ckschreiben */
    public void flush() {
        java.util.Iterator it = rows.iterator();
        while (it.hasNext()) {
            rowspec r = (rowspec) it.next();
            r.write(null);
        }
        cfg.flush();
    }

    class dlisten implements ActionListener {

        rowspec row;

        DateSelector.Dialog sel;

        dlisten(rowspec row) {
            this.row = row;
        }

        public void actionPerformed(ActionEvent ac) {
            TimeTool d = new TimeTool(row.val);
            d = DateSelector.Dialog.show(null, d, row.lbl);
            row.val = d.toString(TimeTool.DATE_GER);
            JButton b = (JButton) row.cmp;
            b.setText(row.val);
        }
    }

    class blisten implements ActionListener {

        rowspec row;

        Component parent;

        blisten(rowspec row, Component parent) {
            this.row = row;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent arg0) {
            Color old = new Color(Integer.parseInt(row.val));
            JButton but = (JButton) arg0.getSource();
            Color nc = JColorChooser.showDialog(parent, "W�hlen Sie eine Farbe aus", old);
            if (nc != null) {
                but.setBackground(nc);
                but.setForeground(Colors.contrast(nc));
                row.val = Integer.toString(nc.getRGB());
            }
        }
    }

    class rowspec {

        rowspec(String r) {
            String[] row = r.split("::");
            if (row[TYPE].equals("PLAIN")) typ = TYP_PLAIN; else if (row[TYPE].equals("BOOL")) typ = TYP_BOOL; else if (row[TYPE].equals("SEPARATOR")) typ = TYP_SEPARATOR; else if (row[TYPE].equals("CHOICE")) typ = TYP_CHOICE; else if (row[TYPE].equals("NODE")) {
                typ = TYP_NODE;
            } else if (row[TYPE].equals("INPUT")) {
                typ = TYP_INPUT;
            } else if (row[TYPE].equals("COLORCHOOSE")) {
                typ = TYP_COLOR;
            } else if (row[TYPE].equals("ADAPTER")) {
                typ = TYP_ADAPTER;
            } else if (row[TYPE].equals("DATE")) {
                typ = TYP_DATE;
            } else typ = TYP_UNKNOWN;
            if (row.length > LABEL) lbl = row[LABEL];
            if (row.length > PARM) param = row[PARM]; else param = lbl;
            if (row.length > DEFAULT) def = row[DEFAULT]; else def = "";
            if (row.length > CHOICES) choices = row[CHOICES];
        }

        void setComponent(JComponent c) {
            cmp = c;
        }

        void read() {
            if (typ == TYP_SEPARATOR) return;
            if (StringTool.isNothing(param)) {
                if (StringTool.isNothing(lbl)) {
                    val = def;
                } else {
                    val = cfg.get(lbl, def);
                }
            } else {
                val = cfg.get(param, def);
            }
            if (cmp == null) return;
            if (typ == TYP_ADAPTER) {
                ad.set(param, cmp, val);
            } else if (cmp instanceof JComboBox) {
                ((JComboBox) cmp).setSelectedItem(val);
            } else if (cmp instanceof JTextField) {
                ((JTextField) cmp).setText(val);
            } else if (cmp instanceof JCheckBox) {
                if (val.equals("1") || val.equals("true")) {
                    ((JCheckBox) cmp).setSelected(true);
                } else {
                    ((JCheckBox) cmp).setSelected(false);
                }
            } else if (cmp instanceof JButton) {
                if (typ == TYP_COLOR) {
                    Color nc = new Color(Integer.parseInt(val));
                    ((JButton) cmp).setBackground(nc);
                    cmp.setForeground(Colors.contrast(nc));
                } else if (typ == TYP_DATE) {
                    if (val.equals(" ") || StringTool.isNothing(val)) {
                        val = new TimeTool().toString(TimeTool.DATE_GER);
                    }
                    ((JButton) cmp).setText(val);
                }
            }
        }

        void write(String v) {
            if (typ == TYP_SEPARATOR) return;
            if (v == null) {
                if (cmp == null) return;
                if (typ == TYP_ADAPTER) {
                    val = ad.get(param, cmp);
                } else if (cmp instanceof JComboBox) {
                    val = (String) ((JComboBox) cmp).getSelectedItem();
                } else if (cmp instanceof JTextField) {
                    val = ((JTextField) cmp).getText();
                } else if (cmp instanceof JCheckBox) {
                    val = ((JCheckBox) cmp).isSelected() ? "1" : "0";
                }
            } else {
                val = v;
            }
            if (StringTool.isNothing(param)) {
                if (!StringTool.isNothing(lbl)) {
                    cfg.set(lbl, val);
                }
            } else {
                cfg.set(param, val);
            }
        }

        int typ;

        String lbl;

        String param;

        String def;

        String val;

        String choices;

        JComponent cmp;
    }

    @SuppressWarnings("serial")
    class EditDlg extends JDialog {

        JPanel center;

        boolean answer;

        EditDlg(JPanel p) {
            Container cnt = getContentPane();
            jnL lis = new jnL();
            cnt.setLayout(new BorderLayout());
            cnt.add(p, BorderLayout.CENTER);
            JPanel buttons = ButtonFactory.getOKCancelPanel("OK", "Abbruch", getRootPane(), lis, lis);
            cnt.add(buttons, BorderLayout.SOUTH);
            pack();
            center = p;
            setModal(true);
        }

        class jnL implements ActionListener {

            public void actionPerformed(ActionEvent ev) {
                if (ev.getActionCommand().equals("OK")) {
                    flush();
                    answer = true;
                } else {
                    answer = false;
                }
                dispose();
            }
        }
    }

    public interface suiAdapter {

        JComponent createView(String param);

        void set(String param, JComponent pnl, String val);

        String get(String param, JComponent pnl);
    }
}
