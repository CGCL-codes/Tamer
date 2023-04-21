package Plan58.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import Plan58.datatype.customtblmodel;
import Plan58.datatype.daddress;
import Plan58.datatype.doktyp;
import Plan58.engine.connectDB;
import Plan58.engine.pdfmaker;
import Plan58.engine.statebar;
import Plan58.provider.datums;
import Plan58.provider.tablefilter;
import Plan58.provider.tbl2object;
import com.jeta.forms.components.panel.FormPanel;

/**
 * The Class showauftrag.
 * 
 * @author Christian Krämer -660563
 * Diese Klasse  zeigt alle Aufträge nach gewissen wählbaren Kriterien auf.
 */
public class showartikelmove {

    /** The Frame. */
    private JInternalFrame Frame;

    /** The panel. */
    private FormPanel panel = new FormPanel("Plan58/forms/showartikelmove.jfrm");

    /** The view. */
    private JComboBox view = panel.getComboBox("view");

    /** The filter. */
    private JComboBox filter = panel.getComboBox("filter");

    /** The pdf. */
    private AbstractButton pdf = panel.getButton("pdf");

    /** The tbl. */
    private JTable tbl = panel.getTable("tbl");

    /** The refto root frame. */
    private JFrame reftoRootFrame;

    /** The model. */
    private customtblmodel model;

    /** The viewmap. */
    private HashMap<String, String> viewmap = new HashMap<String, String>();

    /** The colhead. */
    private String[][] colhead = { { "Artikelnr", "Beschreibung", "Menge", "Art", "Benutzer", "Datum" }, { "Auftragsnr", "Auftrag", "Artikelnr", "Artikel", "Menge" } };

    /** The editmask. */
    private boolean[] editmask = { false, false, false, false, false, false };

    /** The colmap. */
    private HashMap<String, Integer> colmap = new HashMap<String, Integer>();

    /** The filtmap. */
    private HashMap<String, Integer> filtmap = new HashMap<String, Integer>();

    /** The add. */
    daddress add = new daddress();

    /**
	 * Instantiates a new showauftrag.
	 * 
	 * @param desktop the desktop
	 * @param RootFrame the root frame
	 * @param username the username
	 */
    public showartikelmove(JDesktopPane desktop, final JFrame RootFrame, final String username) {
        reftoRootFrame = RootFrame;
        model = new customtblmodel(colhead[0], editmask);
        tbl.setModel(model);
        Frame = new JInternalFrame();
        colmap.put("Materialbewegungen", 0);
        colmap.put("Verbrauch pro Auftrag", 1);
        filtmap.put("Materialbewegungen", 3);
        filtmap.put("Verbrauch pro Auftrag", 0);
        view.addItem("Materialbewegungen");
        viewmap.put("Materialbewegungen", "SELECT a.artikelnr,CONCAT(kurztext1,' ',kurztext2),v.menge,CASE WHEN v.art=2 then 'EINGANG' ELSE 'AUSGANG' END,v.user,DATE_FORMAT(v.datum,'%d.%m.%Y') FROM verbrauch v JOIN artikel a on (v.artikelnr=a.artikelid)");
        view.addItem("Verbrauch pro Auftrag");
        viewmap.put("Verbrauch pro Auftrag", "SELECT auf.auftragsnummer,auf.firma,a.artikelnr,CONCAT(a.kurztext1,' ',a.kurztext2),sum(v.menge) FROM verbrauch v JOIN auftrag auf ON (auf.auftragid=v.verursachernr) JOIN artikel a ON a.artikelid=v.artikelnr GROUP BY auf.auftragsnummer,v.artikelnr");
        ActionListener AL = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(view)) {
                    model = new customtblmodel(colhead[colmap.get(view.getSelectedItem())], editmask);
                    tbl.setModel(model);
                    new tbl2object().filltable(tbl, viewmap.get(view.getSelectedItem()), RootFrame);
                    fillfilter(filter, tbl, filtmap.get(view.getSelectedItem()));
                    tbl.setVisible(false);
                    tbl.addColumnSelectionInterval(0, 1);
                    tbl.repaint();
                    tbl.setVisible(true);
                    panel.repaint();
                }
                if (e.getSource().equals(pdf)) {
                    add.setSubject(view.getSelectedItem() + " " + new datums().getdatums());
                    if (new pdfmaker().pdfmkr(new doFiledialoge().savefile(view.getSelectedItem() + "_" + new datums().getdatums()), doktyp.Datenblatt, new tbl2object().converttoobj(tbl), tbl.getColumnCount(), add)) new statebar(RootFrame, "Datei wurde erfolgreich gespeichrt!", 1); else new statebar(RootFrame, "Der Vorgang wurde abgebrochen", 3);
                }
                if (e.getSource().equals(filter)) {
                    if ((filter.getSelectedItem() != null) && (!filter.getSelectedItem().toString().equals("ALLE"))) {
                        tbl.setModel(model);
                        tbl.setVisible(false);
                        tbl.addColumnSelectionInterval(0, 1);
                        tbl.repaint();
                        tbl.setVisible(true);
                        new tablefilter().filter(tbl, (String) filter.getSelectedItem(), filtmap.get(view.getSelectedItem()));
                    } else {
                        tbl.setModel(model);
                        tbl.setVisible(false);
                        tbl.addColumnSelectionInterval(0, 1);
                        tbl.repaint();
                        tbl.setVisible(true);
                        new tbl2object().filltable(tbl, viewmap.get(view.getSelectedItem()), RootFrame);
                        tbl.setVisible(false);
                        tbl.addColumnSelectionInterval(0, 1);
                        tbl.repaint();
                        tbl.setVisible(true);
                    }
                }
            }
        };
        view.setSelectedIndex(0);
        new tbl2object().filltable(tbl, viewmap.get(view.getSelectedItem()), RootFrame);
        fillfilter(filter, tbl, 3);
        view.addActionListener(AL);
        filter.addActionListener(AL);
        pdf.addActionListener(AL);
        Frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        Frame.setClosable(true);
        Frame.setMaximizable(true);
        Frame.setSize(920, 400);
        Frame.add(panel);
        Frame.setVisible(true);
        Frame.setTitle("Übersicht der Aufträge");
        desktop.add(Frame);
    }

    /**
		 * Updateform.
		 */
    void updateform() {
        NumberFormat formatfl = NumberFormat.getNumberInstance();
        formatfl.setMaximumFractionDigits(2);
        formatfl.setMinimumFractionDigits(2);
    }

    /**
	 * Fillfilter.
	 * 
	 * @param cbox the cbox
	 * @param tbl the tbl
	 * @param colno the colno
	 */
    public void fillfilter(JComboBox cbox, JTable tbl, int colno) {
        cbox.removeAllItems();
        HashSet<String> flist = new HashSet<String>();
        if (tbl.getRowCount() > 0) for (int i = 0; i < tbl.getRowCount(); i++) {
            flist.add((String) tbl.getValueAt(i, colno));
        }
        cbox.addItem("ALLE");
        for (int i = 0; i < flist.size(); i++) cbox.addItem(flist.toArray()[i]);
    }

    /**
	 * Fillcombobox.
	 * 
	 * @param cbox the cbox
	 * @param stm the stm
	 * @param idmap the idmap
	 */
    public void fillcombobox(JComboBox cbox, String stm, HashMap<String, String> idmap) {
        Vector<String[]> ret = new connectDB().giveallresults(reftoRootFrame, stm);
        cbox.removeAllItems();
        for (int i = 0; i <= ret.indexOf(ret.lastElement()); i++) {
            cbox.addItem(ret.get(i)[2]);
            idmap.put(ret.get(i)[2], ret.get(i)[1]);
        }
        cbox.repaint();
    }
}
