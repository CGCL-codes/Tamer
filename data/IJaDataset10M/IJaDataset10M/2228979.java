package org.jens.cis.gui;

import java.awt.Cursor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import log4j.Log4jOutput;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jens.Shorthand.RsyncWrapper;
import org.jens.Shorthand.RsyncWrapper.RsyncOutputListener;
import org.jens.cis.controller.Connection;
import org.jens.cis.controller.Container;
import org.jens.cis.controller.DiskCachedImporter;
import org.jens.cis.controller.MemoyCachedImporter;
import org.jens.cis.exceptions.CISException;
import org.jens.cis.model.Chapter;
import org.jens.cis.model.CharAndMiss;
import org.jens.cis.model.CharAndTitle;
import org.jens.cis.model.CharAndTruhe;
import org.jens.cis.model.Character;
import org.jens.cis.model.FoundCharAndMiss;
import org.jens.cis.model.Hero;
import org.jens.cis.model.Title;
import org.jens.cis.model.Truhe;

/**
 * Hier kommt das eigentliche Programm . . . 
 * @author Jens Ritter
 *
 */
public class EclipseView extends netbeansView implements Log4jOutput, RsyncOutputListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Log logger = LogFactory.getLog(EclipseView.class);

    protected Connection connection;

    protected Container container;

    protected Helper hlp = new Helper();

    protected DiskCachedImporter hansi;

    /**
	 * Constructor
	 */
    public EclipseView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
            SwingUtilities.updateComponentTreeUI(popupMissiontable);
            SwingUtilities.updateComponentTreeUI(dlgAbout);
            SwingUtilities.updateComponentTreeUI(dlgAddHero);
            SwingUtilities.updateComponentTreeUI(dlgFindCharByMission);
            SwingUtilities.updateComponentTreeUI(dlgAddTitle);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        connection = new Connection();
        hansi = new MemoyCachedImporter(connection);
        container = new Container();
        modeUnauth();
        URL url = getClass().getResource("/changelog.html");
        try {
            txtChangelog.setPage(url);
        } catch (IOException e) {
            logger.fatal("Das Sollte nicht passieren.");
            e.printStackTrace();
        }
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                EclipseView a = EclipseView.getInstance();
                a.setVisible(true);
            }
        });
    }

    private static EclipseView singleton;

    /**
	 * Singleton
	 * @return
	 */
    public static EclipseView getInstance() {
        if (singleton == null) {
            singleton = new EclipseView();
        }
        return singleton;
    }

    @Override
    public void login(String username, String passwd) {
        try {
            logger.info("Anmelden am Server");
            connection.login(username, passwd);
            loadBaseData();
            modeAuthed();
            userConfig.put("username", username);
            userConfig.put("password", passwd);
        } catch (IOException e) {
            fehler("Fehler beim Verbinden -- ist das Internet OK?");
            e.printStackTrace();
        } catch (CISException e) {
            fehler("Benutzername / Password falsch");
            e.printStackTrace();
        }
    }

    /**
	 * Das Frame mit allen Daten füllen ( Alle Kontrols, Texte, Images, alle Tabellen, etc ) 
	 */
    public void loadBaseData() {
        try {
            hansi.clearJPGCache();
            container = new Container();
            logger.info("Hole alle Missionen vom Server");
            hansi.loadAllMissions(container);
            logger.info("Hole alle Title vom Server");
            hansi.loadAllTitle(container);
            logger.info("Hole alle Character vom Server");
            hansi.loadAllCharacter(container);
            logger.info("Hole alle Helden vom Server");
            hansi.loadAllHeroes(container);
            logger.info("Hole alle Truhen vom Server");
            hansi.loadAllTruhen(container);
            logger.info("Hole alle Title-Bezeichnungen");
            hansi.loadAllTitleBez(container);
            logger.info("Hole alle Skill daten");
            hansi.loadAllSkills(container);
            logger.debug("Fill charBox");
            DefaultComboBoxModel modelc = new DefaultComboBoxModel(container.getAllChars().toArray());
            comboCharSelect.setModel(modelc);
            logger.debug("Fill KlassArtCombo");
            modelc = new DefaultComboBoxModel(Character.getAllKlassenBez());
            comboSkillsArt.setModel(modelc);
            comboSkillsArt.setSelectedIndex(0);
            this.setCurrentChar(container.getAllChars().get(0));
            this.setCurrentChapter(container.getAllChapter().get(0));
            radioMissionChap1.setSelected(true);
            List<Object> w2 = new ArrayList<Object>();
            DefaultProgressTableModel m = new MissionProgressTableModel(w2, container.getAllMissionsByChap(container.getCurrentChapter()));
            tableMissProgress.setModel(m);
            m.setValueListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    CharAndMiss m = (CharAndMiss) e.getSource();
                    try {
                        hansi.updateMissionProgress(container.getCurrentChar(), m, container);
                        updateMissionProgressPanel();
                    } catch (IOException ef) {
                        fehler(ef.getMessage());
                        ef.printStackTrace();
                    } catch (CISException ef) {
                        fehler(ef.getMessage());
                        ef.printStackTrace();
                    }
                }
            });
            tableMissProgress.getColumnModel().getColumn(0).setWidth(300);
            List<Truhe> tmpData = container.getAllTruhen();
            Collections.sort(tmpData);
            TruheTableModel model = new TruheTableModel(container.getTruhenFromChar(), tmpData, container);
            tableTruhen.setModel(model);
            tableTruhen.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor());
            model.setValueListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    CharAndTruhe c = (CharAndTruhe) e.getSource();
                    try {
                        hansi.updateTruhenDate(container.getCurrentChar(), c, container);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        fehler(e1.getMessage());
                    } catch (CISException e1) {
                        e1.printStackTrace();
                        fehler(e1.getMessage());
                    }
                }
            });
            TitleTableModel mod = new TitleTableModel(container.getTitleFromChar());
            tableTitle.setModel(mod);
            tableTitle.getColumnModel().getColumn(1).setCellRenderer(new TitleTableRenderer());
            tableTitle.getColumnModel().getColumn(1).setCellEditor(new TitleTableEditor());
            mod.setValueListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    CharAndTitle t = (CharAndTitle) e.getSource();
                    try {
                        hansi.updateTitleLevel(container.getCurrentChar(), t, container);
                    } catch (IOException e1) {
                        fehler(e1.getMessage());
                        e1.printStackTrace();
                    } catch (CISException e1) {
                        fehler(e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            });
            SkillTableModel mods = new SkillTableModel(container.getSkills(Character.getAllKlassenID()[0]));
            tableSkills.setModel(mods);
            ImageIcon TestIcon = container.getSkills(Character.NECRO_ID).get(0).getImageAsIcon();
            tableSkills.setRowHeight(TestIcon.getIconHeight());
            tableSkills.getColumnModel().getColumn(1).setCellRenderer(new SkillTableIconRenderer());
            tableSkills.getColumnModel().getColumn(2).setCellRenderer(new SkillTableWikiRenderer());
            tableSkills.getColumnModel().getColumn(2).setCellEditor(new SkillTableWikiCellEditor());
            mod.setValueListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    logger.error("TODO: SkillTable - data has changed");
                }
            });
            onCharInfoTabChange();
        } catch (CISException e) {
            fehler(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fehler(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
	 * Wechselt in den Modus, wo man sich noch nicht angemeldet hat
	 */
    public void modeUnauth() {
        txtUsername.requestFocus();
        tabMain.setEnabledAt(1, false);
    }

    /**
	 * Welchselt in den Modus, nachdem man sich erfolgreich angemeldet hat
	 */
    public void modeAuthed() {
        tabMain.setEnabledAt(1, true);
        tabMain.setEnabledAt(0, false);
        tabMain.setSelectedComponent(panelMain);
        panelMain.requestFocus();
    }

    /**
	 * MINI-Fehlermeldungen . . .
	 * @param msg
	 */
    public void fehler(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * MINI-Statusmeldungen . . . 
	 * @param msg
	 */
    public void status(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    public void reloadData() {
        if (connection.isLoggedIn()) {
            hansi.removeAllCache();
            loadBaseData();
            modeAuthed();
        }
    }

    @Override
    public void onChangeChapter(int value) {
        logger.info("Change to chapter " + value);
        Chapter c = container.getChapterById(value);
        this.setCurrentChapter(c);
    }

    /**
	 * Das gewünschte Chapter wird angezeigt
	 * @param value
	 */
    public void setCurrentChapter(Chapter value) {
        container.setCurrentChapter(value);
        if (!tableMissProgress.getModel().getClass().getName().equals("javax.swing.table.DefaultTableModel")) {
            updateMissionProgressPanel();
        } else {
            popupMissiontable.setEnabled(false);
        }
    }

    /**
	 * Die Daten für den Missions-Fortschritt werden aktualisiert
	 */
    protected void updateMissionProgressPanel() {
        MissionProgressTableModel mdl = (MissionProgressTableModel) tableMissProgress.getModel();
        List<CharAndMiss> a = container.getAllMissionsByCharByChapter(container.getCurrentChar(), container.getCurrentChapter());
        List<Object> x = new ArrayList<Object>();
        Collections.sort(a);
        x.addAll(a);
        mdl.setProgress(x, container.getAllMissionsByChap(container.getCurrentChapter()));
        if (x.size() > 0) {
            popupMissiontable.setEnabled(true);
        } else {
            popupMissiontable.setEnabled(false);
        }
    }

    /**
	 * Die Daten für das Hero-Panel werden aktualisiert
	 */
    protected void updateHeroPanel() {
        DefaultListModel m = new DefaultListModel();
        List<Hero> list = container.getHeroesFromChar();
        Collections.sort(list);
        for (Hero hero : list) {
            m.addElement(hero);
        }
        this.lstHeros.setModel(m);
        newHeroSelected();
    }

    /**
	 * Die Daten für das Truhen-Panel werden aktualisiert
	 */
    protected void updateTruhenPanel() {
        TruheTableModel model = (TruheTableModel) tableTruhen.getModel();
        List<CharAndTruhe> t = container.getTruhenFromChar();
        Collections.sort(t);
        model.setData(t);
    }

    /**
	 * Die Daten für das Title-Panel werden aktualisiert
	 */
    protected void updateTitlePanel() {
        TitleTableModel model = (TitleTableModel) tableTitle.getModel();
        List<CharAndTitle> t = container.getTitleFromChar();
        Collections.sort(t);
        model.setData(t);
    }

    protected void updateSkillPanel() {
        try {
            SkillTableModel model = (SkillTableModel) tableSkills.getModel();
            String[] tmp = Character.getAllKlassenID();
            int id = comboSkillsArt.getSelectedIndex();
            String art = tmp[id];
            model.setSkills(container.getSkills(art));
        } catch (CISException e) {
            fehler(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onChangeCharacter() {
        logger.info("onChangeCharacter");
        Character c = (Character) this.comboCharSelect.getSelectedItem();
        int w = c.getId();
        c = container.getCharacterById(w);
        try {
            this.setCurrentChar(c);
            onCharInfoTabChange();
        } catch (CISException e) {
            fehler(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fehler(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
	 * Daten des gewünschten Chars (chars) werden angezeigt
	 * @param chars
	 * @throws CISException
	 * @throws IOException
	 */
    public void setCurrentChar(Character chars) throws CISException, IOException {
        container.setCurrentChar(chars);
        txtErstellt_am.setText(chars.getErstellt_am());
        if (chars.getPet_name() != null) {
            labelPetname.setText(chars.getPet_name());
        } else {
            labelPetname.setText("-");
        }
        labelClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jens/cis/images/ClassIcons/" + chars.getPrim_class() + ".png")));
        labelClass.setText(chars.getPrim_classAsName());
        labelPicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jens/cis/images/CharImageLoading.png")));
        String url = connection.buildUrl(chars.getPicture());
        hlp.bgLoad(hansi, new URL(url), labelPicture);
        if (chars.getPet_picture() != null) {
            url = connection.buildPetUrl(chars.getPet_picture());
            hlp.bgLoad(hansi, new URL(url), labelPetPicture);
        } else {
            labelPetPicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jens/cis/images/PetImageLoading.png")));
        }
        try {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date born = format.parse(chars.getErstellt_am());
            if (now.before(born)) {
                txtGeburtstag.setText("Du hast aber ein komisches Geburtsdatum.");
                txtAlter.setText("");
            } else {
                String tmp = DurationFormatUtils.formatPeriod(born.getTime(), now.getTime(), "y M d");
                StringTokenizer tok = new StringTokenizer(tmp, " ");
                String jahre = tok.nextToken();
                String monate = tok.nextToken();
                String tage = tok.nextToken();
                String allTage = DurationFormatUtils.formatPeriod(born.getTime(), now.getTime(), "d");
                int tageAnzahl = Integer.parseInt(allTage);
                tageAnzahl = tageAnzahl % 365;
                tageAnzahl = 365 - tageAnzahl;
                txtAlter.setText(jahre + " Jahre, " + monate + " Monate ," + tage + " Tage. ( oder " + allTage + " Tage )");
                txtGeburtstag.setText("Noch " + tageAnzahl + " Tage bis zum nächsten Geburtstag");
            }
        } catch (ParseException e) {
            logger.fatal("Konnte Datum " + chars.getErstellt_am() + " nicht Parsen");
            e.printStackTrace();
        }
    }

    @Override
    public void toggleMissionTableEditable() {
        DefaultProgressTableModel model = (DefaultProgressTableModel) tableMissProgress.getModel();
        model.setEditable(btnMissionTableEditable.isSelected());
    }

    @Override
    public void findCharsForThisMission() {
        DefaultProgressTableModel m = (DefaultProgressTableModel) tableMissProgress.getModel();
        CharAndMiss w = (CharAndMiss) m.getProgressForRow(tableMissProgress.getSelectedRow());
        try {
            List<FoundCharAndMiss> c = hansi.findCharByMission(container, w.getMission());
            List<Object> XX = new ArrayList<Object>();
            XX.addAll(c);
            DefaultProgressTableModel m2 = new MissionProgressTableModel(XX, container.getAllMissionsByChap(container.getCurrentChapter()));
            this.tableResultFindCharByMission.setModel(m2);
            txtResultFindCharMission.setText(w.getMission().getName());
            dlgFindCharByMission.pack();
            dlgFindCharByMission.setVisible(true);
            if (c != null) {
                for (FoundCharAndMiss cc : c) {
                    logger.debug("Chars for this Mission : " + cc.getCharacter().getName());
                }
            }
            ;
        } catch (CISException e) {
            this.fehler(e.getMessage());
        } catch (IOException e) {
            this.fehler(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void displayHeroDlg() {
        DefaultListModel model = new DefaultListModel();
        List<Hero> heroes = container.getAllHeroes();
        Collections.sort(heroes);
        for (Hero hero : heroes) {
            if (!container.getHeroesFromChar().contains(hero)) {
                model.addElement(hero);
            }
        }
        if (model.getSize() == 0) {
            btnHeroAddOK.setEnabled(false);
        } else {
            btnHeroAddOK.setEnabled(true);
        }
        this.listHeroAdd.setModel(model);
        dlgAddHero.pack();
        dlgAddHero.setVisible(true);
    }

    @Override
    public void newHeroSelected() {
        Hero h = (Hero) lstHeros.getSelectedValue();
        if (h != null) {
            labelHeroImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jens/cis/images/Heroes/" + h.getId() + ".png")));
            labelHeroImage.setToolTipText(h.getName());
            labelHeroClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jens/cis/images/ClassIcons/" + h.getPrim_class() + ".png")));
            labelHeroClass.setToolTipText(h.getPrim_classAsName());
            labelHeroImage.setText("                 ");
        } else {
            labelHeroImage.setIcon(null);
            labelHeroImage.setText("Bitte wählen Sie einen Helden aus");
            labelHeroClass.setIcon(null);
            labelHeroClass.setToolTipText("");
        }
    }

    @Override
    public void addHeroToCharFromList() {
        dlgAddHero.setVisible(false);
        Object[] sel = listHeroAdd.getSelectedValues();
        if (sel.length == 0) {
            logger.info("Benutzer hat KEINEN(!) Heros ausgewählt");
            return;
        }
        setLoadingDataStart();
        try {
            for (int i = 0; i < sel.length; i++) {
                hansi.addHero(container.getCurrentChar(), (Hero) sel[i], container);
                Hero hero = (Hero) sel[i];
                System.out.println("HeroName: " + hero.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fehler("Konnte den Hero nicht hinzufuegen ( SQL Error)");
        } catch (CISException e) {
            fehler("Konnte den Hero nicht hinzufuegen ( CISError )");
            e.printStackTrace();
        }
        this.updateHeroPanel();
        setLoadingDataFinish();
    }

    @Override
    public void onCharInfoTabChange() {
        if (connection == null || !connection.isLoggedIn()) {
            return;
        }
        Object panel = tabCharInfo.getSelectedComponent();
        if (panel == null) {
            logger.fatal("Öhm. tabCharInfo.getSelectedComponent() liefert null ?!?! -> sollte nicht sein ");
        }
        if (panel == this.panelMissionProgress) {
            logger.info("User selected MissionProgress-Panel - updating it");
            LoaderThread loader = new LoaderThread(this, LoaderThread.UPDATE_MISSION);
            Thread a = new Thread(loader);
            a.start();
            return;
        }
        if (panel == this.panelHeros) {
            logger.info("User selected Hero-Panel - updating it");
            LoaderThread loader = new LoaderThread(this, LoaderThread.UPDATE_HEROES);
            Thread a = new Thread(loader);
            a.start();
            return;
        }
        if (panel == this.panelTitle) {
            logger.info("User selected Title-Panel - updating it");
            LoaderThread loader = new LoaderThread(this, LoaderThread.UPDATE_TITLE);
            Thread a = new Thread(loader);
            a.start();
            return;
        }
        if (panel == this.panelTruhen) {
            logger.info("User selected Truhen-Panel - updating it");
            LoaderThread loader = new LoaderThread(this, LoaderThread.UPDATE_TRUHEN);
            Thread a = new Thread(loader);
            a.start();
            return;
        }
        if (panel == this.panelSkills) {
            logger.info("User selected Skill-Panel - updating it");
            LoaderThread loader = new LoaderThread(this, LoaderThread.UPDATE_SKILLS);
            Thread a = new Thread(loader);
            a.start();
            return;
        }
        logger.fatal("hier sollte ich nicht hinkommen");
    }

    /**
	 * Zeigt den Status "Ich lade gerade was" an
	 */
    public void setLoadingDataStart() {
        tabMain.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        comboCharSelect.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        comboCharSelect.setEnabled(false);
        btnSaveErstelltAm.setEnabled(false);
        txtStatus.setText("Bitte Warten");
    }

    /**
	 * Beendet den Status "Ich lade gerade was"
	 */
    public void setLoadingDataFinish() {
        tabMain.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        comboCharSelect.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        comboCharSelect.setEnabled(true);
        btnSaveErstelltAm.setEnabled(true);
        txtStatus.setText("");
    }

    @Override
    public void displayCharImage() {
        displayImage(container.getCurrentChar().getName(), connection.buildUrl(container.getCurrentChar().getPicture()));
    }

    @Override
    public void displayPetImage() {
        displayImage(container.getCurrentChar().getPet_name(), connection.buildPetUrl(container.getCurrentChar().getPet_picture()));
    }

    /**
	 * Helper: Öffnet ein Fenster und zeigt ein Bild darin an
	 * @param name
	 * @param url
	 */
    protected void displayImage(String name, String url) {
        JDialog dlg = new JDialog();
        dlg.setModal(true);
        dlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JLabel img = new JLabel();
        dlg.setTitle(name);
        try {
            img.setIcon(hansi.getImage(new URL(url)));
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        dlg.add(img);
        dlg.pack();
        dlg.setVisible(true);
    }

    @Override
    public void displayChangelog() {
        frmChangelog.pack();
        frmChangelog.setVisible(true);
    }

    @Override
    public JTextArea getLogArea() {
        return this.txtLogoutput;
    }

    /**
	 * Der Benutzer hat hoffentlich ein paar title ausgewählt, die jetzt dem Char hinzugefügt werden müssen.
	 */
    @Override
    public void addTitleFromList() {
        dlgAddTitle.setVisible(false);
        Object[] sel = listTitle.getSelectedValues();
        if (sel.length == 0) {
            logger.info("Benutzer hat KEINEN(!) Title ausgewählt");
            return;
        }
        setLoadingDataStart();
        try {
            for (int i = 0; i < sel.length; i++) {
                hansi.addTitle(container.getCurrentChar(), (Title) sel[i], container);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fehler("Konnte den Hero nicht hinzufuegen ( SQL Error)");
        } catch (CISException e) {
            fehler("Konnte den Hero nicht hinzufuegen ( CISError )");
            e.printStackTrace();
        }
        updateTitlePanel();
        setLoadingDataFinish();
    }

    /**
	 * Zeigt den Dialog zum hinzufügen von Title an
	 */
    @Override
    public void displayTitleDlg() {
        DefaultListModel model = new DefaultListModel();
        List<Title> titles = container.getAllTitle();
        Collections.sort(titles);
        for (Title title : titles) {
            boolean found = false;
            for (CharAndTitle charTitle : container.getTitleFromChar()) {
                if (charTitle.getTitle().equals(title)) {
                    found = true;
                }
            }
            if (!found) {
                model.addElement(title);
            }
        }
        if (model.getSize() == 0) {
            btnTitleAddOK.setEnabled(false);
        } else {
            btnTitleAddOK.setEnabled(true);
        }
        listTitle.setModel(model);
        dlgAddTitle.pack();
        dlgAddTitle.setVisible(true);
    }

    @Override
    public void saveErstelltAm() {
        logger.info("SaveErstlltAm");
        if (btnSaveErstelltAm.isSelected() == true) {
            txtErstellt_am.setEditable(true);
            txtErstellt_am.requestFocus();
            txtErstellt_am.setSelectionStart(0);
            txtErstellt_am.setSelectionEnd(txtErstellt_am.getText().length());
            btnSaveErstelltAm.setText("Speichern");
        } else {
            try {
                String datum = txtErstellt_am.getText();
                Pattern p = Pattern.compile("^\\d\\d\\.\\d\\d.\\d\\d\\d\\d$");
                Matcher m = p.matcher(datum);
                if (!m.matches()) {
                    throw new ParseException(datum + " - not dd.dd.dddd", 0);
                }
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                Date born = format.parse(datum);
                if (born != null) {
                    datum = format.format(born);
                    hansi.updateCharBorn(container.getCurrentChar(), datum, container);
                    txtErstellt_am.setEditable(false);
                    btnSaveErstelltAm.setText("Bearbeiten");
                    hansi.removeCache(DiskCachedImporter.FILE_CHARS);
                    hansi.loadAllCharacter(container);
                    onChangeCharacter();
                }
            } catch (ParseException e) {
                fehler("Das Datum bitte als dd.MM.yyyy eingeben. -- DANKE !");
                txtErstellt_am.requestFocus();
                btnSaveErstelltAm.setSelected(true);
            } catch (CISException e) {
                fehler(e.getMessage());
                txtErstellt_am.requestFocus();
                btnSaveErstelltAm.setSelected(true);
                e.printStackTrace();
            } catch (IOException e) {
                fehler("Fehler beim ändern der Datenbank. :" + e.getMessage());
                txtErstellt_am.requestFocus();
                btnSaveErstelltAm.setSelected(true);
                e.printStackTrace();
            }
        }
    }

    /** Command Buttons : **/
    private void saveUserRunData() {
        userConfig.put(PFAD_RSYNC, txtRsyncPath.getText());
        userConfig.put(PFAD_GWDATA, txtProfilePath.getText());
        userConfig.put(PFAD_BACKUP, txtBackupPath.getText());
        userConfig.put(PFAD_GW, txtGWPath.getText());
        userConfig.put(GWPASS, txtGWPassword.getText());
    }

    public void switchRunButtons(boolean value) {
        btnRunGW.setEnabled(value);
        btnRunPull.setEnabled(value);
        btnRunPush.setEnabled(value);
        btnRunTestPull.setEnabled(value);
        btnRunUpdate.setEnabled(value);
    }

    private class Rsync extends SwingWorker<Integer, Void> {

        private String source;

        private String target;

        private boolean dryRun;

        RsyncOutputListener listener;

        Rsync(String source, String target, boolean dryRun, RsyncOutputListener listener) {
            this.source = source;
            this.target = target;
            this.dryRun = dryRun;
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground() throws Exception {
            logger.info("Starte RSync Thread");
            switchRunButtons(false);
            int exitVals = 0;
            RsyncWrapper rsync = new RsyncWrapper(txtRsyncPath.getText());
            rsync.setDryrun(dryRun);
            rsync.addOutputListener(listener);
            rsync.setSource(source + "/Templates/");
            rsync.setTarget(target + "/Templates");
            exitVals = rsync.rsync();
            rsync.setSource(source + "/Screens/");
            rsync.setTarget(target + "/Screens");
            exitVals += rsync.rsync();
            return exitVals;
        }

        @Override
        public void done() {
            try {
                listener.onNewOutputLine("ExitCode : " + get());
                switchRunButtons(true);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void runTestPullData() {
        saveUserRunData();
        Rsync r = new Rsync(txtBackupPath.getText(), txtProfilePath.getText(), true, this);
        r.execute();
    }

    @Override
    public Object runPullData() {
        saveUserRunData();
        Rsync r = new Rsync(txtBackupPath.getText(), txtProfilePath.getText(), false, this);
        r.execute();
        return r;
    }

    @Override
    public Object runPushData() {
        saveUserRunData();
        Rsync r = new Rsync(txtProfilePath.getText(), txtBackupPath.getText(), false, this);
        r.execute();
        return r;
    }

    @Override
    public void runUpdate() {
        saveUserRunData();
        List<String> cmdLine = new ArrayList<String>();
        cmdLine.add(txtGWPath.getText() + "\\GW.exe");
        cmdLine.add("-image");
        ProcessBuilder p = new ProcessBuilder(cmdLine);
        onNewOutputLine("Starting GW.exe -image");
        try {
            p.start();
        } catch (IOException e) {
            onNewOutputLine(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void runStarGW() {
        saveUserRunData();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                txtRsyncOutput.setText("");
                logger.info("Starte GW.exe ... ( vorher rsync) ");
                Rsync r = (Rsync) runPullData();
                while (!r.isDone()) {
                    Thread.sleep(100);
                }
                switchRunButtons(false);
                List<String> cmdLine = new ArrayList<String>();
                cmdLine.add(txtGWPath.getText() + "\\GW.exe");
                cmdLine.add("-password");
                cmdLine.add(txtGWPassword.getText());
                cmdLine.add("-bmp");
                ProcessBuilder p = new ProcessBuilder(cmdLine);
                onNewOutputLine("Starting GW.exe -password XXXXX -bmp");
                try {
                    Process running = p.start();
                    running.waitFor();
                } catch (IOException e) {
                    onNewOutputLine(e.getMessage());
                    logger.error(e.getMessage());
                }
                onNewOutputLine("Back from Game -- updating Progress");
                r = (Rsync) runPushData();
                while (!r.isDone()) {
                    Thread.sleep(100);
                }
                return null;
            }
        };
        worker.execute();
    }

    @Override
    public void durchsuchen(JTextField field, boolean dir) {
        JFileChooser fs = new JFileChooser();
        if (dir == true) {
            fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        int returnVal = fs.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            field.setText(fs.getSelectedFile().toString());
        }
    }

    @Override
    public synchronized void onNewOutputLine(String line) {
        txtRsyncOutput.append(line + "\n");
    }

    @Override
    public void changeSkillArt() {
        if (tableSkills.getModel() instanceof SkillTableModel) {
            updateSkillPanel();
        } else {
        }
    }
}
