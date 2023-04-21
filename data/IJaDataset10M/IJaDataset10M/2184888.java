package de.achim.turnier;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import de.achim.turnier.endrunde.EndrundenPaarungsPanel;
import de.achim.turnier.endrunde.EndrundenTabelle;
import de.achim.turnier.spielplan.DruckSpielPanel;
import de.achim.turnier.spielplan.SpielPanel;
import de.achim.turnier.spielplan.SpielPlanContainer;
import de.achim.turnier.spielplan.SpielPlanPanel;
import de.achim.turnier.tabellen.TabellenContainer;
import de.achim.turnier.tabellen.TabellenPanel;
import de.achim.turnier.utility.Begegnung;
import de.achim.turnier.utility.DateiSpeichern;
import de.achim.turnier.utility.IPaarungPanels;
import de.achim.turnier.utility.MannschaftErgebnis;

public class Turniervewaltung2 extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 640;

    private static final int HEIGHT = 480;

    private int gewinn = 3;

    private int niederlage = 0;

    private int unentschieden = 1;

    private JTabbedPane tabPane = new JTabbedPane();

    private AnlegenPaarungPanel aap = new AnlegenPaarungPanel();

    private SpielPlanContainer spc = new SpielPlanContainer();

    private TabellenContainer tabc = new TabellenContainer();

    private JPanel buttonPanelSPC = new JPanel();

    private Vector<JComponent> vCompList = new Vector<JComponent>();

    private Vector<IPaarungPanels> vSPP = new Vector<IPaarungPanels>();

    private Vector<TabellenPanel> vTP = new Vector<TabellenPanel>();

    private HashMap<String, JPanel> hmSPP = new HashMap<String, JPanel>();

    private HashMap<String, TabellenPanel> hmTP = new HashMap<String, TabellenPanel>();

    private int anzahlGruppen = 1;

    private boolean initialierungSPC = false;

    private boolean initialisierungTABC = false;

    public Turniervewaltung2() {
        setSize(WIDTH, HEIGHT);
        Container contentPane = getContentPane();
        aap.setMotherFrame(this);
        tabPane.addTab("Paarung anlegen", aap);
        tabPane.addTab("Spielplan", spc);
        tabPane.addTab("Tabelle", tabc);
        spc.setMotherFrame(this);
        tabc.setMotherFrame(this);
        JMenuBar menuBar = new JMenuBar();
        JMenu mnSpielplan = new JMenu("Spielplan");
        JMenu mnTabelle = new JMenu("Tabelle");
        ImageIcon iconSpeicher = createImageIcon("/images/save_16.png", "a pretty but meaningless splat");
        JMenuItem sps = new JMenuItem("Spielplan speichern", iconSpeicher);
        sps.addActionListener(new SpeichereDaten2(this));
        sps.setActionCommand("spielplan");
        mnSpielplan.add(sps);
        JMenuItem ts = new JMenuItem("Tabelle speichern", iconSpeicher);
        ts.addActionListener(new SpeichereDaten2(this));
        ts.setActionCommand("tabelle");
        mnTabelle.add(ts);
        ImageIcon iconLaden = createImageIcon("/images/folder_16.png", "a pretty but meaningless splat");
        JMenuItem lsp = new JMenuItem("Lade Spielplan", iconLaden);
        lsp.addActionListener(new LadeDaten2(this));
        lsp.setActionCommand("spielpanel");
        mnSpielplan.add(lsp);
        JMenuItem ltb = new JMenuItem("Lade Tabelle");
        ltb.addActionListener(new LadeDaten2(this));
        ltb.setActionCommand("tabelle");
        mnTabelle.add(ltb);
        menuBar.add(mnSpielplan);
        menuBar.add(mnTabelle);
        contentPane.add(menuBar, BorderLayout.NORTH);
        contentPane.add(tabPane, BorderLayout.CENTER);
    }

    public void registerComponent(JComponent comp) {
        vCompList.add(comp);
    }

    public void updateData(JComponent comp) {
        String compName = comp.getName();
        System.out.println(compName);
        if (compName.equals("anlegenpaarungspanel")) {
            AnlegenPaarungPanel app = (AnlegenPaarungPanel) comp;
            handleAPPOK(app);
        }
        if (compName.equals("spielpanel")) {
            SpielPanel sp = (SpielPanel) comp;
            handleSpielPanelOK(sp);
        }
    }

    public void printData(JPanel panel) {
        PrintDaten2 pd2 = new PrintDaten2(panel);
        pd2.drucken();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Turniervewaltung2 frame = new Turniervewaltung2();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void handleAPPOK(AnlegenPaarungPanel app) {
        if (!initialierungSPC) initSPC(app);
        if (!initialisierungTABC) initTABC();
        Vector<SpielPlanPanel> vspp = spc.getSpielPlanPanels();
        String mannschaft1 = app.jtMannschaft1.getText().trim();
        String mannschaft2 = app.jtMannschaft2.getText().trim();
        String uhrzeit = app.jtUhrzeit.getText().trim();
        String datum = app.jtDatum.getText().trim();
        String date = datum + " " + uhrzeit;
        if (mannschaft1.length() <= 0 || mannschaft2.length() <= 0) {
            app.lblErrorMsg.setText("Bitte Mannschaften eingeben");
        } else {
            if (anzahlGruppen == 1) {
                SpielPlanPanel spp = vspp.firstElement();
                spp.addSpielPaarung(mannschaft1, mannschaft2, date);
                app.lblErrorMsg.setText(spp.getErrorMsg());
            } else if (anzahlGruppen == 2) {
                if (app.jrGruppe1.isSelected()) {
                    SpielPlanPanel spp = vspp.firstElement();
                    spp.addSpielPaarung(mannschaft1, mannschaft2, date);
                    app.lblErrorMsg.setText(spp.getErrorMsg());
                } else if (app.jrGruppe2.isSelected()) {
                    SpielPlanPanel spp = vspp.get(1);
                    spp.addSpielPaarung(mannschaft1, mannschaft2, date);
                    app.lblErrorMsg.setText(spp.getErrorMsg());
                }
            }
        }
    }

    /**
	 * nachdem im Anlegenpaarungspanel auf den OK Knopf gedrueckt wurde
	 * wissen wir wie das spielplanpanel aussehen soll
	 * @param app
	 */
    private void initSPC(AnlegenPaarungPanel app) {
        String title = app.jtTitle.getText();
        if (app.jrZweiGruppen.isSelected()) {
            initSPC(2, title);
        } else {
            initSPC(1, title);
        }
    }

    private void initSPC(int anzahlgruppen, String title) {
        anzahlGruppen = anzahlgruppen;
        if (anzahlgruppen == 2) {
            spc.setGridDimensions(4, 2);
            SpielPlanPanel sppG1 = new SpielPlanPanel();
            SpielPlanPanel sppG2 = new SpielPlanPanel();
            EndrundenPaarungsPanel epp = new EndrundenPaarungsPanel();
            sppG1.setName("sppgruppe1");
            sppG1.setGruppe(0);
            sppG2.setName("sppgruppe2");
            sppG2.setGruppe(1);
            epp.setName("sppendrunde");
            epp.setGruppe(2);
            sppG1.setTitle("Gruppe 1");
            sppG2.setTitle("Gruppe 2");
            epp.setName("endrundenzuordnung");
            sppG1.setMotherPanel(spc);
            sppG2.setMotherPanel(spc);
            epp.setMotherPanel(spc);
            vSPP.add(sppG1);
            vSPP.add(sppG2);
            vSPP.add(epp);
            hmSPP.put("sppgruppe1", sppG1);
            hmSPP.put("sppgruppe2", sppG2);
            hmSPP.put("sppendrunde", epp);
            spc.addSpielPlanPanel(sppG1, "0,1");
            spc.addSpielPlanPanel(sppG2, "0,2");
            spc.addComponent(epp, "1,2");
            spc.setTitle(title, "0,0,1,0");
            JButton btnEndrunde = new JButton("Endrunde");
            InitEPP alIEPP = new InitEPP(spc);
            btnEndrunde.addActionListener(alIEPP);
            buttonPanelSPC.add(btnEndrunde);
            spc.addComponent(buttonPanelSPC, "0,3,1,3");
            setSize(2 * WIDTH, 2 * HEIGHT);
        } else {
            spc.setGridDimensions(2, 1);
            SpielPlanPanel spp = new SpielPlanPanel();
            spp.setName("sppgruppe0");
            spp.setGruppe(0);
            spp.setMotherPanel(spc);
            spc.setTitle(title, "0,0");
            spc.addSpielPlanPanel(spp, "0,1");
            vSPP.add(spp);
        }
        initialierungSPC = true;
    }

    private void handleSpielPanelOK(SpielPanel sp) {
        SpielPlanPanel spp = (SpielPlanPanel) sp.getMotherPanel();
        spp.getName();
        MannschaftErgebnis me1 = new MannschaftErgebnis(sp.getMannschaft1());
        me1.setManschaftTorePlus(new Integer(sp.getTore1()));
        me1.setMannschaftToreMinus(new Integer(sp.getTore2()));
        me1.setMannschaftSpiele(1);
        if (sp.getBegegnung() != null) {
            Begegnung bg = sp.getBegegnung();
            bg.setToreMannschaft1(sp.getTore1());
            bg.setToreMannschaft2(sp.getTore2());
            bg.setSpielfertig(true);
        }
        if (sp.getTore1() > sp.getTore2()) me1.setMannschaftPunkte(gewinn); else if (sp.getTore1() < sp.getTore2()) me1.setMannschaftPunkte(niederlage); else me1.setMannschaftPunkte(unentschieden);
        MannschaftErgebnis me2 = new MannschaftErgebnis(sp.getMannschaft2());
        me2.setManschaftTorePlus(new Integer(sp.getTore2()));
        me2.setMannschaftToreMinus(new Integer(sp.getTore1()));
        me2.setMannschaftSpiele(1);
        if (sp.getTore1() > sp.getTore2()) me2.setMannschaftPunkte(niederlage); else if (sp.getTore1() < sp.getTore2()) me2.setMannschaftPunkte(gewinn); else me2.setMannschaftPunkte(unentschieden);
        spp.getTabellenPanel().addRow(me1);
        spp.getTabellenPanel().addRow(me2);
    }

    /**
	 * initialieseren des tabellen containers
	 */
    private void initTABC() {
        if (anzahlGruppen == 2) {
            tabc.setGridDimensions(3, 1);
            TabellenPanel tpGruppe1 = new TabellenPanel();
            TabellenPanel tpGruppe2 = new TabellenPanel();
            EndrundenTabelle tpEndrunde = new EndrundenTabelle();
            tpGruppe1.setName("tabgruppe1");
            tpGruppe1.setGruppe(0);
            tpGruppe2.setName("tabgruppe2");
            tpGruppe2.setGruppe(1);
            tpEndrunde.setName("tabendspiel");
            tpGruppe1.setMotherPanel(tabc);
            tpGruppe2.setMotherPanel(tabc);
            tpEndrunde.setMotherPanel(tabc);
            ((SpielPlanPanel) hmSPP.get("sppgruppe1")).setTabellenPanel(tpGruppe1);
            ((SpielPlanPanel) hmSPP.get("sppgruppe2")).setTabellenPanel(tpGruppe2);
            ((EndrundenPaarungsPanel) hmSPP.get("sppendrunde")).setEndTab(tpEndrunde);
            vTP.add(tpGruppe1);
            vTP.add(tpGruppe2);
            tabc.addTabellenPanel(tpGruppe1, "0,0");
            tabc.addTabellenPanel(tpGruppe2, "0,1");
            tabc.addEndrundenTabelle(tpEndrunde, "0,2");
        } else {
            TabellenPanel tabelle = new TabellenPanel();
            tabelle.setName("Tabelle");
            tabelle.setGruppe(0);
            tabelle.setMotherPanel(tabc);
            tabc.addTabellenPanel(tabelle, "0,0");
            vSPP.firstElement().setTabellenPanel(tabelle);
            vTP.add(tabelle);
        }
        initialisierungTABC = true;
    }

    public void initSPCFromFile(int anzahlgruppen, Vector<String> vLine) {
        int gruppe = 0;
        Collections.sort(vLine, new ZeilenComparator());
        Iterator<String> iterS = vLine.iterator();
        initSPC(anzahlgruppen, "");
        initTABC();
        while (iterS.hasNext()) {
            String zeile = iterS.next();
            if (zeile.length() > 0) {
                int i = zeile.lastIndexOf(";");
                String s = zeile.substring(i + 1, zeile.length()).trim();
                if (Character.isDigit(s.charAt(0))) {
                    if (Integer.parseInt(s) == gruppe) {
                        vSPP.get(gruppe).initFromLine(zeile);
                    } else {
                        gruppe++;
                        vSPP.get(gruppe).initFromLine(zeile);
                    }
                }
            }
        }
    }

    public void initTAPCFromFile(Vector<String> vLine) {
        initTABC();
        String gruppe = "0";
        Collections.sort(vLine, new ZeilenComparator());
        Iterator<String> iterS = vLine.iterator();
        while (iterS.hasNext()) {
            String zeile = iterS.next();
            if (zeile.substring(zeile.length() - 1, zeile.length()).equals(gruppe)) {
                vTP.get(Integer.parseInt(gruppe));
            } else {
                int i = Integer.parseInt(gruppe);
                i = i + 1;
                gruppe = String.valueOf(i);
                vTP.get(Integer.parseInt(gruppe));
            }
        }
    }

    /**
	 * 
	 * @return the vSPP
	 */
    public Vector<IPaarungPanels> getvSPP() {
        return this.vSPP;
    }

    /**
	 * 
	 * @return the vTP
	 */
    public Vector<TabellenPanel> getvTP() {
        return this.vTP;
    }

    /**
	 * @return the anzahlGruppen
	 */
    public int getAnzahlGruppen() {
        return this.anzahlGruppen;
    }

    /**
	 * 
	 * @param i
	 */
    public void setAnzahlGruppen(int i) {
        this.anzahlGruppen = i;
    }

    /**
	 * 
	 * @param path
	 * @param description
	 * @return
	 */
    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

/**
 * EndrundenPaarungsPanel mit Daten bestuecken
 * @author achimrumberger
 *
 */
class InitEPP implements ActionListener {

    SpielPlanContainer spc;

    public InitEPP(SpielPlanContainer c) {
        this.spc = c;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Vector<SpielPlanPanel> vSPP = spc.getvSPP();
        HashMap<String, SpielPlanPanel> hmSPP = spc.getHmSPP();
        TabellenPanel tpp1 = hmSPP.get("sppgruppe1").getTabellenPanel();
        TabellenPanel tpp2 = hmSPP.get("sppgruppe2").getTabellenPanel();
        TableModel datModel1 = tpp1.getTable().getModel();
        TableModel datModel2 = tpp2.getTable().getModel();
        Vector<String> vGruppe1 = getMannschaften(datModel1);
        Vector<String> vGruppe2 = getMannschaften(datModel2);
        EndrundenPaarungsPanel epp = spc.getEpp();
        epp.setvGruppe1(vGruppe1);
        epp.setvGruppe2(vGruppe2);
        epp.fillPanel();
    }

    private Vector<String> getMannschaften(TableModel dataModel) {
        Vector<String> vMannschaften = new Vector<String>();
        int rowcount = dataModel.getRowCount();
        int manschaftname = 0;
        for (int row = 0; row < rowcount; row++) {
            String name = ((String) dataModel.getValueAt(row, manschaftname)).trim();
            vMannschaften.add(name);
        }
        return vMannschaften;
    }
}

/**
 * tabellen oder spielpanels ausdrucken
 * @author achimrumberger
 *
 */
class PrintDaten2 {

    TabellenPanel tabelle;

    SpielPlanPanel spp;

    EndrundenTabelle etabp;

    public PrintDaten2(JPanel panel) {
        if (panel instanceof TabellenPanel) tabelle = (TabellenPanel) panel;
        if (panel instanceof SpielPlanPanel) spp = (SpielPlanPanel) panel;
        if (panel instanceof EndrundenTabelle) etabp = (EndrundenTabelle) panel;
    }

    public void drucken() {
        if (tabelle != null) {
            MessageFormat headerFormat = new MessageFormat("Seite {0}");
            MessageFormat footerFormat = new MessageFormat("- {0} -");
            try {
                tabelle.getTable().print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        }
        if (etabp != null) {
            MessageFormat headerFormat = new MessageFormat("Seite {0}");
            MessageFormat footerFormat = new MessageFormat("- {0} -");
            try {
                etabp.getTable().print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        }
        if (spp != null) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setCopies(1);
            String title = spp.getTitle().getText();
            job.setJobName("Spielplan");
            DruckSpielPanel dsp = new DruckSpielPanel();
            dsp.setSps(spp);
            dsp.setTitle(title);
            dsp.setGetPrintInfo(true);
            try {
                job.setPrintable(dsp);
                job.print();
                dsp.setGetPrintInfo(false);
                if (job.printDialog()) {
                    job.print();
                }
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        }
    }
}

/**
 * speichert die inhalte von spielplanpanel und tabelle in textdateien
 * @author achimrumberger
 * 
 */
class SpeichereDaten2 implements ActionListener {

    SpielPlanPanel spp;

    TabellenPanel tabelle;

    JPanel panel;

    StringBuffer sb = new StringBuffer();

    String dateiendung;

    Turniervewaltung2 tv2;

    public SpeichereDaten2(JPanel panel) {
        this.panel = panel;
    }

    public SpeichereDaten2(JFrame frame) {
        if (frame instanceof Turniervewaltung2) this.tv2 = (Turniervewaltung2) frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sb.append("####################").append("\n");
        sb.append("# anzahl gruppen: ").append(tv2.getAnzahlGruppen());
        sb.append("\n");
        sb.append("####################").append("\n");
        ;
        if (e.getActionCommand().equals("spielplan")) {
            Vector<IPaarungPanels> vspp = tv2.getvSPP();
            Iterator<IPaarungPanels> iterSPP = vspp.iterator();
            while (iterSPP.hasNext()) {
                IPaarungPanels spp = iterSPP.next();
                Vector<Begegnung> vbg = spp.getBegegnungsListe();
                Iterator<Begegnung> iter = vbg.iterator();
                while (iter.hasNext()) {
                    sb.append(iter.next().toString());
                }
            }
            dateiendung = ".txt";
        } else if (e.getActionCommand().equals("tabelle")) {
            Vector<TabellenPanel> vtp = tv2.getvTP();
            Iterator<TabellenPanel> iterTP = vtp.iterator();
            while (iterTP.hasNext()) {
                TabellenPanel tabelle = iterTP.next();
                sb.append(tabelle.toString());
                dateiendung = ".csv";
            }
        }
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        int state = fc.showSaveDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String pfad = file.getPath() + dateiendung;
            System.out.println(file.getPath());
            DateiSpeichern.speichern(sb.toString(), pfad);
        } else System.out.println("Auswahl abgebrochen");
        System.out.println(sb.toString());
    }
}

/**
 * 
 * @author achimrumberger
 */
class LadeDaten2 implements ActionListener {

    SpielPlanPanel spp;

    TabellenPanel tabelle;

    JPanel panel;

    Turniervewaltung2 tv2;

    StringBuffer sb = new StringBuffer();

    Vector<String> vLine = new Vector<String>();

    public LadeDaten2(JPanel panel) {
        this.panel = panel;
    }

    public LadeDaten2(JFrame frame) {
        if (frame instanceof Turniervewaltung2) {
            this.tv2 = (Turniervewaltung2) frame;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        int anzahlgruppen = 1;
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt") || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "Turnier Daten";
            }
        });
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        int state = fc.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("####################")) {
                    } else if (line.startsWith("# anzahl gruppen: ")) {
                        int beginIndex = "# anzahl gruppen: ".length();
                        String sAnzGr = line.substring(beginIndex, line.length());
                        anzahlgruppen = Integer.parseInt(sAnzGr);
                    } else vLine.add(line);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (e.getActionCommand().equals("spielpanel")) {
            tv2.initSPCFromFile(anzahlgruppen, vLine);
        }
        if (e.getActionCommand().equals("tabelle")) {
            tv2.initTAPCFromFile(vLine);
        }
    }
}
