package ch.HaagWeirich.Agenda;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import ch.rgw.swingtools.ButtonFactory;
import ch.rgw.swingtools.DateSelector;
import ch.rgw.swingtools.SwingHelper;
import ch.rgw.tools.ExHandler;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.TimeTool;
import ch.rgw.tools.JdbcLink.Stm;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *	Kontrolle aller ab einem bestimmten Datum gesetzten Tage eines oder
 *  mehrerer Mandanten bez�glich Terminkollisionen und Anzeige derselben in
 *  einer druckbaren Liste.
 */
@SuppressWarnings("serial")
public class Reshape extends JDialog {

    static final String Version = "1.0.3";

    private Reshape self;

    private javax.swing.JPanel jContentPane = null;

    private JLabel jLabel = null;

    private JPanel jPanel = null;

    private JButton jButton = null;

    private JButton jButton1 = null;

    private JPanel jPanel1 = null;

    private DateSelector dateSelector = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel2 = null;

    private Vector vMand = null;

    private Vector vMandlabels = null;

    private JScrollPane jScrollPane = null;

    private JList jList = null;

    ArrayList collides = null;

    /**
	 * This is the default constructor
	 */
    public Reshape(JDialog owner, Vector vMandanten, Vector vLabels) {
        super(owner);
        self = this;
        vMand = vMandanten;
        vMandlabels = vLabels;
        initialize();
    }

    private void initialize() {
        this.setTitle("Tagesvorgaben anpassen");
        this.setSize(420, 466);
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jLabel.setText("<html>Diese Funktion �ndert die reservierten Zeitr�ume aller Tage ab dem Startdatum " + "auf die neuen Werte wie unter Optionen/Grundeinstellungen angegeben.<br>Bereits bestehende " + "Termine, welche mit diesen �nderungen in Konflikt sind, werden<br>anschliessend on einer Liste " + "angezeigt.<br>" + "Der Vorgang kann mehrere Minuten dauern. Es sollte sichergestellt sein, dass in dieser Zeit keine " + "anderen Zugriffe auf die Datenbank stattfinden.<br>" + "<em>Achtung:</em> Diese Funktion �ndert die Tageseinteilung und l�sst sich nicht r�ckg�ngig machen!</html>");
            jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
            jContentPane.add(jLabel, java.awt.BorderLayout.NORTH);
            jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.add(getJButton(), null);
            jPanel.add(getJButton1(), null);
        }
        return jPanel;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Ausf�hren!");
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (doReshape() == true) {
                        if ((collides == null) || (collides.size() == 0)) {
                            SwingHelper.alert(self, "Alles erledigt", "Es traten keine Terminkollisionen auf");
                        } else {
                            ShowCollide dlg = new ShowCollide();
                            SwingHelper.centerComponent(dlg, self);
                            dlg.setModal(true);
                            dlg.setVisible(true);
                        }
                    } else {
                        SwingHelper.alert(self, "Fehler", "Beim Konvertieren traten Fehler auf.");
                    }
                    setVisible(false);
                    dispose();
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Abbrechen");
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
        }
        return jButton1;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jLabel2 = new JLabel();
            jLabel1 = new JLabel();
            jPanel1 = new JPanel();
            jPanel1.setLayout(null);
            jLabel1.setBounds(9, 26, 141, 18);
            jLabel1.setText("Startdatum");
            jLabel2.setBounds(215, 23, 141, 20);
            jLabel2.setText("Mandanten");
            jPanel1.add(getDateSelector(), null);
            jPanel1.add(jLabel1, null);
            jPanel1.add(jLabel2, null);
            jPanel1.add(getJScrollPane(), null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes dateSelector	
	 * 	
	 * @return ch.rgw.tools.DateSelector	
	 */
    private DateSelector getDateSelector() {
        if (dateSelector == null) {
            dateSelector = new DateSelector();
            dateSelector.setBounds(9, 49, 184, 220);
        }
        return dateSelector;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(213, 51, 168, 213);
            jScrollPane.setViewportView(getJList());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
    private JList getJList() {
        if (jList == null) {
            jList = new JList(vMandlabels);
        }
        return jList;
    }

    Agenda.agMandant getMandant(String n) {
        for (int i = 0; i < vMand.size(); i++) {
            Agenda.agMandant res = (Agenda.agMandant) vMand.get(i);
            if (res.getLabel().equals(n)) {
                return res;
            }
        }
        return null;
    }

    private boolean doReshape() {
        TimeTool start = dateSelector.getValue();
        Object[] mands = jList.getSelectedValues();
        collides = new ArrayList();
        ArrayList days = new ArrayList();
        Stm stm = Agenda.j.getStatement();
        try {
            ResultSet res = stm.query("SELECT day from agnDays where day>" + JdbcLink.wrap(start.toString(TimeTool.DATE_COMPACT)));
            while ((res != null) && (res.next() == true)) {
                days.add(res.getString("day"));
            }
            res.close();
        } catch (Exception ex) {
            ExHandler.handle(ex);
            return false;
        } finally {
            Agenda.j.releaseStatement(stm);
        }
        Iterator itDays = days.iterator();
        while (itDays.hasNext()) {
            String sDay = (String) itDays.next();
            for (int i = 0; i < mands.length; i++) {
                TimeTool tt = new TimeTool(sDay);
                Agenda.agMandant agm = getMandant((String) mands[i]);
                if (agm == null) {
                    continue;
                }
                Agenda.makeMandDefaultDay(tt, agm);
                if (checkCollide(sDay, agm) == true) {
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkCollide(String sDay, Agenda.agMandant agm) {
        ArrayList day = new ArrayList();
        Stm stm = Agenda.j.getStatement();
        try {
            ResultSet res = stm.query("SELECT * from agnTermine where Tag=" + JdbcLink.wrap(sDay) + " AND BeiWem=" + JdbcLink.wrap(agm.getLabel()) + " AND Deleted=0 ORDER BY Beginn");
            while ((res != null) && (res.next() == true)) {
                AgendaEntry ae = Agenda.termin.getInstance();
                ae.fetch(res);
                day.add(ae);
            }
            res.close();
        } catch (Exception ex) {
            ExHandler.handle(ex);
            collides.add(sDay);
            return true;
        } finally {
            Agenda.j.releaseStatement(stm);
        }
        Iterator it = day.iterator();
        while (it.hasNext()) {
            AgendaEntry ae = (AgendaEntry) it.next();
            if (ae.collides(day)) {
                collides.add(sDay);
                return true;
            }
        }
        return false;
    }

    class ShowCollide extends JDialog {

        JList list;

        String[] vals;

        java.awt.Container cp;

        ButtonListener blis;

        String exp = "<html>Untenstehend sehen Sie eine Liste aller Tage, an welchen es<br>" + "zu Terminkollisionen kam.<br>�berpr�fen Sie bitte diese Daten manuell und korrigieren<br>" + "bzw. verschieben Sie die Termine, die in Konflikt sind.</html>";

        ShowCollide() {
            super(self);
            vals = new String[collides.size()];
            blis = new ButtonListener();
            TimeTool tt = new TimeTool();
            for (int i = 0; i < vals.length; i++) {
                tt.set((String) collides.get(i));
                vals[i] = tt.toString(TimeTool.WEEKDAY) + ", " + tt.toString(TimeTool.DATE_GER);
            }
            list = new JList(vals);
            JScrollPane scr = new JScrollPane(list);
            cp = getContentPane();
            cp.setLayout(new BorderLayout());
            cp.add(scr, BorderLayout.CENTER);
            JPanel buttons = new JPanel();
            JButton bOK = ButtonFactory.getButton("Schliessen", "close", blis, null);
            JButton bPrint = ButtonFactory.getButton("Drucken", "print", blis, null);
            buttons.add(bOK);
            buttons.add(bPrint);
            cp.add(buttons, BorderLayout.SOUTH);
            cp.add(new JLabel(exp), BorderLayout.NORTH);
            setTitle("Liste der Daten mit Terminkonflikten");
            pack();
        }

        class ButtonListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("close")) {
                    setVisible(false);
                    dispose();
                } else {
                    try {
                        PrinterJob pj = PrinterJob.getPrinterJob();
                        pj.setJobName("Kollosionsliste drucken");
                        pj.setPrintable(new CollidePrinter());
                        HashPrintRequestAttributeSet pa = new HashPrintRequestAttributeSet();
                        if (pj.printDialog(pa) == true) {
                            pj.print(pa);
                        }
                        return;
                    } catch (Exception ex) {
                        ExHandler.handle(ex);
                    }
                }
            }
        }

        class CollidePrinter implements Printable {

            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                int lineHeight = g.getFontMetrics().getHeight() + 3;
                g.translate((int) pf.getImageableX(), (int) pf.getImageableY());
                int numLines = (int) Math.round(pf.getImageableHeight() / lineHeight) - 1;
                int startline = pageIndex * numLines;
                if (startline > vals.length) {
                    return Printable.NO_SUCH_PAGE;
                }
                int y = 10;
                for (int i = 0; i < numLines; i++) {
                    if (startline + i >= vals.length) {
                        break;
                    }
                    g.drawString(vals[startline + i], 10, y);
                    y += lineHeight;
                }
                return Printable.PAGE_EXISTS;
            }
        }
    }
}
