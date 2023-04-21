package itrafgen.gui.packetdescriptor;

import itrafgen.ItrafgenApp;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import itrafgen.XMLPARSER.champ;
import itrafgen.XMLPARSER.choice;

/**
 *
 * @author sebastienhoerner
 */
public class MultiComponentTable2 extends JPanel {

    DefaultTableModel dm;

    EachRowEditor rowEditor;

    public JTable table;

    EachRowRenderer rowRenderer;

    int taille = 0;

    public JScrollPane scroll;

    public org.jdom.Document document = null;

    public String nf;

    public MultiComponentTable2() {
        dm = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                if (column == 2) {
                    return true;
                }
                if (column == 3) {
                    return true;
                }
                if (column == 4) {
                    return true;
                }
                if (column == 5) {
                    return true;
                }
                if (column == 6) {
                    return true;
                }
                return false;
            }
        };
        dm.setDataVector(new Object[][] {}, new Object[] { "Champ", "Taille", "Valeur", "Base", "Separateur", "Offset", "Select" });
        rowRenderer = new EachRowRenderer();
        table = new JTable(dm);
        this.setSize(400, 300);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("5");
        comboBox.addItem("6");
        JCheckBox checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(JLabel.CENTER);
        DefaultCellEditor comboBoxEditor = new DefaultCellEditor(comboBox);
        DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
        scroll = new JScrollPane(table);
        scroll.setSize(400, 180);
        table.setPreferredScrollableViewportSize(new Dimension(400, 180));
        this.add(scroll);
        setVisible(true);
    }

    public void ajchmp(champ o, int num) {
        taille++;
        String sep = null;
        String base = "16";
        if (o.name.equals("src_mac")) {
            o.valdef = ItrafgenApp.macemission;
            sep = ":";
        }
        if (o.name.equals("dst_mac")) {
            o.valdef = ItrafgenApp.macreception;
            sep = ":";
        }
        if (o.name.equals("Src_IP address")) {
            o.valdef = ItrafgenApp.ipemission;
            sep = ".";
            base = "10";
        }
        if (o.name.equals("Dst_IP address")) {
            o.valdef = ItrafgenApp.ipreception;
            sep = ".";
            base = "10";
        }
        if (o.lch == null) {
            if (Integer.parseInt(o.longueur) != 1) {
                dm.addRow(new Object[] { o.name, o.longueur, o.valdef, base, sep, o.offset });
            } else {
                if (o.valdef.equals("1")) {
                    dm.addRow(new Object[] { o.name, o.longueur, new Boolean(true), base, sep, o.offset });
                } else {
                    dm.addRow(new Object[] { o.name, o.longueur, new Boolean(false), base, sep, o.offset });
                }
                JCheckBox checkBox = new JCheckBox();
                checkBox.setHorizontalAlignment(JLabel.CENTER);
                DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
                rowEditor.setEditorAt(num, checkBoxEditor);
                CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
                rowRenderer.add(num, checkBoxRenderer);
            }
        } else {
            JComboBox comboBox = new JComboBox();
            for (int i = 0; i < o.lch.size(); i++) {
                comboBox.addItem(o.lch.get(i).value + "(" + o.lch.get(i).nom + ")");
            }
            dm.addRow(new Object[] { o.name, o.longueur, o.valdef, base, sep, o.offset });
            DefaultCellEditor comboBoxEditor = new DefaultCellEditor(comboBox);
            rowEditor.setEditorAt(num, comboBoxEditor);
        }
        dm.setValueAt("N", num, 6);
        ;
    }

    /**
   * @param _FilePath
   * Methode pour charger mon fichier xml qui contient un protocol
   */
    public void load(File _FilePath) {
        nf = _FilePath.getName();
        rowEditor = new EachRowEditor(table);
        rowRenderer = new EachRowRenderer();
        table.getColumn("Valeur").setCellRenderer(rowRenderer);
        table.getColumn("Valeur").setCellEditor(rowEditor);
        for (int i = dm.getRowCount(); i > 0; --i) {
            dm.removeRow(i - 1);
        }
        taille = 0;
        document = null;
        try {
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(_FilePath);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier " + e.getMessage());
            e.printStackTrace();
        } catch (JDOMException e) {
            System.out.println("Erreur lors de la construction du fichier JDOM " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String nomprotocole() {
        Element racine = document.getRootElement();
        Attribute a = racine.getAttribute("name");
        return a.getValue();
    }

    public String nomlongprotocole() {
        Element racine = document.getRootElement();
        Attribute a = racine.getAttribute("longname");
        return a.getValue();
    }

    public String descriptionprotocole() {
        Element racine = document.getRootElement();
        Attribute a = racine.getAttribute("description");
        return a.getValue();
    }

    public String commentaireprotocole() {
        Element racine = document.getRootElement();
        Attribute a = racine.getAttribute("comment");
        return a.getValue();
    }

    public void packetfrm() {
        Element racine = document.getRootElement();
        try {
            XPath x = XPath.newInstance("//protocol/name");
            Element e = (Element) x.selectSingleNode(document);
            String nprotoc = e.getValue();
            x = XPath.newInstance("//protocol/layer");
            e = (Element) x.selectSingleNode(document);
            x = XPath.newInstance("//protocol/fields/field");
            List list = x.selectNodes(document);
            int iter = 0;
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Element u = (Element) i.next();
                XPath xx = XPath.newInstance("/protocol[name='" + nprotoc + "']/fields/field[name='" + u.getChild("name").getValue() + "']/choices/choice");
                List list2 = xx.selectNodes(document);
                Vector<choice> lch = null;
                if (!list2.isEmpty()) {
                    lch = new Vector<choice>();
                    for (Iterator ii = list2.iterator(); ii.hasNext(); ) {
                        Element ui = (Element) ii.next();
                        lch.add(new choice(ui.getChild("name").getValue(), ui.getChild("value").getValue()));
                    }
                }
                ajchmp(new champ(u.getChild("name").getValue(), u.getChild("length").getValue(), u.getChild("offset").getValue(), u.getChild("defaultvalue").getValue(), lch), iter);
                iter++;
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public List getnextproto() {
        Element racine = document.getRootElement();
        try {
            XPath x = XPath.newInstance("//protocol/encapsulation/nextproto");
            List list = x.selectNodes(document);
            return list;
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return null;
    }
}
