package de.hattrickorganizer.gui.arenasizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;
import de.hattrickorganizer.gui.HOMainFrame;
import de.hattrickorganizer.gui.RefreshManager;
import de.hattrickorganizer.gui.templates.ColorLabelEntry;
import de.hattrickorganizer.gui.templates.DoppelLabelEntry;
import de.hattrickorganizer.gui.templates.ImagePanel;
import de.hattrickorganizer.gui.templates.TableEntry;
import de.hattrickorganizer.logik.ArenaSizer;
import de.hattrickorganizer.model.HOVerwaltung;
import de.hattrickorganizer.model.Stadium;
import de.hattrickorganizer.tools.HOLogger;
import de.hattrickorganizer.tools.Helper;

/**
 * Panel mit JTabel für die Arena anzeige und zum Testen
 */
final class ArenaPanel extends ImagePanel implements ActionListener, FocusListener, de.hattrickorganizer.gui.Refreshable {

    private static final long serialVersionUID = -3049319214078126491L;

    /** TODO Missing Parameter Documentation */
    protected static final byte HRFARENA = 0;

    /** TODO Missing Parameter Documentation */
    protected static final byte TESTARENA = 1;

    private ArenaSizer m_clArenaSizer = new ArenaSizer();

    private JButton m_jbUbernehmen = new JButton(HOVerwaltung.instance().getLanguageString("Uebernehmen"));

    private JButton m_jbUbernehmenGesamt = new JButton(HOVerwaltung.instance().getLanguageString("Uebernehmen"));

    private JTable m_jtArena = new JTable();

    private JTextField m_jtfFans = new JTextField();

    private JTextField m_jtfGesamtgroesse = new JTextField(6);

    private JTextField m_jtfLogen = new JTextField();

    private JTextField m_jtfSitzplatze = new JTextField();

    private JTextField m_jtfStehplatze = new JTextField();

    private JTextField m_jtfUeberdachteSitzplaetze = new JTextField();

    private Stadium m_clStadium;

    private String[] UEBERSCHRIFT = { "", "", "", "", "" };

    private Stadium[] m_clStadien;

    private TableEntry[][] tabellenwerte;

    private int m_clTyp;

    /**
     * Creates a new ArenaPanel object.
     *
     * @param typ TODO Missing Constructuor Parameter Documentation
     */
    protected ArenaPanel(byte typ) {
        m_clTyp = typ;
        RefreshManager.instance().registerRefreshable(this);
        initComponents();
        initTabelle();
        initStadium();
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(m_jbUbernehmen)) {
            int fans = 0;
            int steh = 0;
            int sitz = 0;
            int ueber = 0;
            int loge = 0;
            try {
                fans = Integer.parseInt(m_jtfFans.getText());
                steh = Integer.parseInt(m_jtfStehplatze.getText());
                sitz = Integer.parseInt(m_jtfSitzplatze.getText());
                ueber = Integer.parseInt(m_jtfUeberdachteSitzplaetze.getText());
                loge = Integer.parseInt(m_jtfLogen.getText());
            } catch (NumberFormatException e) {
                HOLogger.instance().log(getClass(), "Fehler: keine Zahl");
            }
            final Stadium stadium = new Stadium();
            stadium.setStehplaetze(steh);
            stadium.setSitzplaetze(sitz);
            stadium.setUeberdachteSitzplaetze(ueber);
            stadium.setLogen(loge);
            m_jtfGesamtgroesse.setText(stadium.getGesamtgroesse() + "");
            reinitStadium(stadium, fans);
        } else {
            int groesse = 0;
            try {
                groesse = Integer.parseInt(m_jtfGesamtgroesse.getText());
            } catch (NumberFormatException e) {
                HOLogger.instance().log(getClass(), "Fehler: keine Zahl");
            }
            m_jtfStehplatze.setText(m_clArenaSizer.calcStehVerteilung(groesse) + "");
            m_jtfSitzplatze.setText(m_clArenaSizer.calcSitzVerteilung(groesse) + "");
            m_jtfUeberdachteSitzplaetze.setText(m_clArenaSizer.calcDachVerteilung(groesse) + "");
            m_jtfLogen.setText(m_clArenaSizer.calcLogenVerteilung(groesse) + "");
            m_jbUbernehmen.doClick();
        }
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param focusEvent TODO Missing Method Parameter Documentation
     */
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param focusEvent TODO Missing Method Parameter Documentation
     */
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        Helper.parseInt(HOMainFrame.instance(), ((JTextField) focusEvent.getSource()), false);
    }

    public void reInit() {
        initStadium();
    }

    /**
     * TODO Missing Method Documentation
     */
    public void refresh() {
    }

    /**
     * TODO Missing Method Documentation
     */
    private void initComponents() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.insets = new Insets(4, 4, 4, 4);
        setLayout(layout);
        final JPanel panel2 = new ImagePanel();
        if (m_clTyp == TESTARENA) {
            final GridBagLayout layout2 = new GridBagLayout();
            final GridBagConstraints constraints2 = new GridBagConstraints();
            constraints2.fill = GridBagConstraints.HORIZONTAL;
            constraints2.weightx = 0.0;
            constraints2.weighty = 0.0;
            constraints2.insets = new Insets(4, 4, 4, 4);
            panel2.setLayout(layout2);
            JLabel label;
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Gesamtgroesse"));
            constraints2.gridx = 0;
            constraints2.gridy = 0;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfGesamtgroesse.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfGesamtgroesse.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 0;
            layout2.setConstraints(m_jtfGesamtgroesse, constraints2);
            panel2.add(m_jtfGesamtgroesse);
            constraints2.gridx = 0;
            constraints2.gridy = 1;
            constraints2.gridwidth = 2;
            layout2.setConstraints(m_jbUbernehmenGesamt, constraints2);
            m_jbUbernehmenGesamt.addActionListener(this);
            panel2.add(m_jbUbernehmenGesamt);
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Stehplaetze"));
            constraints2.gridx = 0;
            constraints2.gridy = 2;
            constraints2.gridwidth = 1;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfStehplatze.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfStehplatze.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 2;
            layout2.setConstraints(m_jtfStehplatze, constraints2);
            panel2.add(m_jtfStehplatze);
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Sitzplaetze"));
            constraints2.gridx = 0;
            constraints2.gridy = 3;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfSitzplatze.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfSitzplatze.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 3;
            layout2.setConstraints(m_jtfSitzplatze, constraints2);
            panel2.add(m_jtfSitzplatze);
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Ueberdachteplaetze"));
            constraints2.gridx = 0;
            constraints2.gridy = 4;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfUeberdachteSitzplaetze.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfUeberdachteSitzplaetze.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 4;
            layout2.setConstraints(m_jtfUeberdachteSitzplaetze, constraints2);
            panel2.add(m_jtfUeberdachteSitzplaetze);
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Logen"));
            constraints2.gridx = 0;
            constraints2.gridy = 5;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfLogen.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfLogen.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 5;
            layout2.setConstraints(m_jtfLogen, constraints2);
            panel2.add(m_jtfLogen);
            label = new JLabel(HOVerwaltung.instance().getLanguageString("Fans"));
            constraints2.gridx = 0;
            constraints2.gridy = 6;
            layout2.setConstraints(label, constraints2);
            panel2.add(label);
            m_jtfFans.setHorizontalAlignment(SwingConstants.RIGHT);
            m_jtfFans.addFocusListener(this);
            constraints2.gridx = 1;
            constraints2.gridy = 6;
            layout2.setConstraints(m_jtfFans, constraints2);
            panel2.add(m_jtfFans);
            constraints2.gridx = 0;
            constraints2.gridy = 7;
            constraints2.gridwidth = 2;
            layout2.setConstraints(m_jbUbernehmen, constraints2);
            m_jbUbernehmen.addActionListener(this);
            panel2.add(m_jbUbernehmen);
        }
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        layout.setConstraints(panel2, constraints);
        add(panel2);
        final JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        m_jtArena.setDefaultRenderer(java.lang.Object.class, new de.hattrickorganizer.gui.model.SpielerTableRenderer());
        panel.add(m_jtArena);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        layout.setConstraints(panel, constraints);
        add(panel);
    }

    private void initStadium() {
        m_clStadium = HOVerwaltung.instance().getModel().getStadium();
        m_jtfFans.setText(HOVerwaltung.instance().getModel().getVerein().getFans() + "");
        m_jtfStehplatze.setText(m_clStadium.getStehplaetze() + "");
        m_jtfSitzplatze.setText(m_clStadium.getSitzplaetze() + "");
        m_jtfUeberdachteSitzplaetze.setText(m_clStadium.getUeberdachteSitzplaetze() + "");
        m_jtfLogen.setText(m_clStadium.getLogen() + "");
        m_jtfGesamtgroesse.setText(m_clStadium.getGesamtgroesse() + "");
        m_clStadien = m_clArenaSizer.berechneAusbaustufen(HOVerwaltung.instance().getModel().getStadium(), HOVerwaltung.instance().getModel().getVerein().getFans());
        reinitTabelle();
    }

    /**
     * TODO Missing Method Documentation
     */
    private void initTabelle() {
        tabellenwerte = new TableEntry[10][5];
        tabellenwerte[0][0] = new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.CENTER);
        tabellenwerte[0][1] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Aktuell"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.CENTER);
        tabellenwerte[0][2] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Maximal"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.CENTER);
        tabellenwerte[0][3] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Durchschnitt"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.CENTER);
        tabellenwerte[0][4] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Minimal"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.CENTER);
        tabellenwerte[1][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Stehplaetze"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[2][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Sitzplaetze"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[3][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Ueberdachteplaetze"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[4][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Logen"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[5][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Gesamt"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[6][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Einnahmen"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[7][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Unterhalt"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[8][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Gewinn"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[9][0] = new ColorLabelEntry(HOVerwaltung.instance().getLanguageString("Baukosten"), ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SPIELERPOSITONSWERTE, SwingConstants.LEFT);
        tabellenwerte[1][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[1][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[1][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[1][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[2][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[2][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[2][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[2][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[3][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[3][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[3][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[3][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[4][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[4][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[4][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[4][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERSUBPOSITONSWERTE);
        tabellenwerte[5][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERPOSITONSWERTE);
        tabellenwerte[5][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERPOSITONSWERTE);
        tabellenwerte[5][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERPOSITONSWERTE);
        tabellenwerte[5][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELERPOSITONSWERTE);
        tabellenwerte[6][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[6][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[6][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[6][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[7][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[7][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[7][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[7][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[8][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[8][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[8][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[8][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[9][1] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[9][2] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[9][3] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        tabellenwerte[9][4] = createDoppelLabelEntry(ColorLabelEntry.BG_SPIELEREINZELWERTE);
        m_jtArena.setModel(new de.hattrickorganizer.gui.model.VAPTableModel(UEBERSCHRIFT, tabellenwerte));
        final TableColumnModel columnModel = m_jtArena.getColumnModel();
        columnModel.getColumn(0).setMinWidth(Helper.calcCellWidth(150));
        columnModel.getColumn(1).setMinWidth(Helper.calcCellWidth(160));
        columnModel.getColumn(2).setMinWidth(Helper.calcCellWidth(160));
        columnModel.getColumn(3).setMinWidth(Helper.calcCellWidth(160));
        columnModel.getColumn(4).setMinWidth(Helper.calcCellWidth(160));
    }

    /**
     * create a new DoppelLabelEntry with default values
     * @param background
     * @return
     */
    private DoppelLabelEntry createDoppelLabelEntry(Color background) {
        return new DoppelLabelEntry(new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD, background, SwingConstants.RIGHT), new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD, background, SwingConstants.RIGHT));
    }

    private void reinitStadium(Stadium stadium, int fans) {
        m_clStadium = stadium;
        m_clStadien = m_clArenaSizer.berechneAusbaustufen(stadium, fans);
        reinitTabelle();
    }

    /**
     * TODO Missing Method Documentation
     */
    private void reinitTabelle() {
        final Stadium stadium = HOVerwaltung.instance().getModel().getStadium();
        if (m_clTyp == HRFARENA) {
            ((DoppelLabelEntry) tabellenwerte[1][1]).getLinks().setText(stadium.getStehplaetze() + "");
            ((DoppelLabelEntry) tabellenwerte[2][1]).getLinks().setText(stadium.getSitzplaetze() + "");
            ((DoppelLabelEntry) tabellenwerte[3][1]).getLinks().setText(stadium.getUeberdachteSitzplaetze() + "");
            ((DoppelLabelEntry) tabellenwerte[4][1]).getLinks().setText(stadium.getLogen() + "");
            ((DoppelLabelEntry) tabellenwerte[5][1]).getLinks().setText(stadium.getGesamtgroesse() + "");
            ((DoppelLabelEntry) tabellenwerte[6][1]).getLinks().setSpezialNumber(stadium.calcMaxEinahmen(), true);
            ((DoppelLabelEntry) tabellenwerte[7][1]).getLinks().setSpezialNumber(-m_clArenaSizer.calcUnterhalt(stadium), true);
            ((DoppelLabelEntry) tabellenwerte[8][1]).getLinks().setSpezialNumber((stadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(stadium)), true);
            ((DoppelLabelEntry) tabellenwerte[9][1]).getLinks().setText("");
            for (int i = 2; i < 5; i++) {
                ((DoppelLabelEntry) tabellenwerte[1][i]).getLinks().setText(m_clStadien[i - 2].getStehplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[1][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getStehplaetze() - stadium.getStehplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[2][i]).getLinks().setText(m_clStadien[i - 2].getSitzplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[2][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getSitzplaetze() - stadium.getSitzplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[3][i]).getLinks().setText(m_clStadien[i - 2].getUeberdachteSitzplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[3][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getUeberdachteSitzplaetze() - stadium.getUeberdachteSitzplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[4][i]).getLinks().setText(m_clStadien[i - 2].getLogen() + "");
                ((DoppelLabelEntry) tabellenwerte[4][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getLogen() - stadium.getLogen(), false);
                ((DoppelLabelEntry) tabellenwerte[5][i]).getLinks().setText(m_clStadien[i - 2].getGesamtgroesse() + "");
                ((DoppelLabelEntry) tabellenwerte[5][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getGesamtgroesse() - stadium.getGesamtgroesse(), false);
                ((DoppelLabelEntry) tabellenwerte[6][i]).getLinks().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen(), true);
                ((DoppelLabelEntry) tabellenwerte[6][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen() - stadium.calcMaxEinahmen(), true);
                ((DoppelLabelEntry) tabellenwerte[7][i]).getLinks().setSpezialNumber(-m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]), true);
                ((DoppelLabelEntry) tabellenwerte[7][i]).getRechts().setSpezialNumber(-(m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]) - m_clArenaSizer.calcUnterhalt(stadium)), true);
                ((DoppelLabelEntry) tabellenwerte[8][i]).getLinks().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]), true);
                ((DoppelLabelEntry) tabellenwerte[8][i]).getRechts().setSpezialNumber((m_clStadien[i - 2].calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2])) - (stadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(stadium)), true);
                ((DoppelLabelEntry) tabellenwerte[9][i]).getLinks().setSpezialNumber(-m_clStadien[i - 2].getAusbauKosten(), true);
            }
            m_jtArena.setModel(new de.hattrickorganizer.gui.model.VAPTableModel(UEBERSCHRIFT, tabellenwerte));
            final TableColumnModel columnModel = m_jtArena.getColumnModel();
            columnModel.getColumn(0).setMinWidth(Helper.calcCellWidth(150));
            columnModel.getColumn(1).setMinWidth(Helper.calcCellWidth(160));
            columnModel.getColumn(2).setMinWidth(Helper.calcCellWidth(160));
            columnModel.getColumn(3).setMinWidth(Helper.calcCellWidth(160));
            columnModel.getColumn(4).setMinWidth(Helper.calcCellWidth(160));
        } else {
            if (m_clStadium != null) {
                ((DoppelLabelEntry) tabellenwerte[1][1]).getLinks().setText(m_clStadium.getStehplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[1][1]).getRechts().setSpezialNumber(m_clStadium.getStehplaetze() - stadium.getStehplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[2][1]).getLinks().setText(m_clStadium.getSitzplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[2][1]).getRechts().setSpezialNumber(m_clStadium.getSitzplaetze() - stadium.getSitzplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[3][1]).getLinks().setText(m_clStadium.getUeberdachteSitzplaetze() + "");
                ((DoppelLabelEntry) tabellenwerte[3][1]).getRechts().setSpezialNumber(m_clStadium.getUeberdachteSitzplaetze() - stadium.getUeberdachteSitzplaetze(), false);
                ((DoppelLabelEntry) tabellenwerte[4][1]).getLinks().setText(m_clStadium.getLogen() + "");
                ((DoppelLabelEntry) tabellenwerte[4][1]).getRechts().setSpezialNumber(m_clStadium.getLogen() - stadium.getLogen(), false);
                ((DoppelLabelEntry) tabellenwerte[5][1]).getLinks().setText(m_clStadium.getGesamtgroesse() + "");
                ((DoppelLabelEntry) tabellenwerte[5][1]).getRechts().setSpezialNumber(m_clStadium.getGesamtgroesse() - stadium.getGesamtgroesse(), false);
                ((DoppelLabelEntry) tabellenwerte[6][1]).getLinks().setSpezialNumber(m_clStadium.calcMaxEinahmen(), true);
                ((DoppelLabelEntry) tabellenwerte[6][1]).getRechts().setSpezialNumber(m_clStadium.calcMaxEinahmen() - stadium.calcMaxEinahmen(), true);
                ((DoppelLabelEntry) tabellenwerte[7][1]).getLinks().setSpezialNumber(-m_clArenaSizer.calcUnterhalt(m_clStadium), true);
                ((DoppelLabelEntry) tabellenwerte[7][1]).getRechts().setSpezialNumber(-(m_clArenaSizer.calcUnterhalt(m_clStadium) - m_clArenaSizer.calcUnterhalt(stadium)), true);
                ((DoppelLabelEntry) tabellenwerte[8][1]).getLinks().setSpezialNumber(m_clStadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadium), true);
                ((DoppelLabelEntry) tabellenwerte[8][1]).getRechts().setSpezialNumber((m_clStadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadium)) - (stadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(stadium)), true);
                ((DoppelLabelEntry) tabellenwerte[9][1]).getLinks().setSpezialNumber(-m_clArenaSizer.calcBauKosten(m_clStadium.getStehplaetze() - stadium.getStehplaetze(), m_clStadium.getSitzplaetze() - stadium.getSitzplaetze(), m_clStadium.getUeberdachteSitzplaetze() - stadium.getUeberdachteSitzplaetze(), m_clStadium.getLogen() - stadium.getLogen()), true);
                ((DoppelLabelEntry) tabellenwerte[9][1]).getRechts().setText("");
                for (int i = 2; i < 5; i++) {
                    ((DoppelLabelEntry) tabellenwerte[1][i]).getLinks().setText(m_clStadien[i - 2].getStehplaetze() + "");
                    ((DoppelLabelEntry) tabellenwerte[1][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getStehplaetze() - m_clStadium.getStehplaetze(), false);
                    ((DoppelLabelEntry) tabellenwerte[2][i]).getLinks().setText(m_clStadien[i - 2].getSitzplaetze() + "");
                    ((DoppelLabelEntry) tabellenwerte[2][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getSitzplaetze() - m_clStadium.getSitzplaetze(), false);
                    ((DoppelLabelEntry) tabellenwerte[3][i]).getLinks().setText(m_clStadien[i - 2].getUeberdachteSitzplaetze() + "");
                    ((DoppelLabelEntry) tabellenwerte[3][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getUeberdachteSitzplaetze() - m_clStadium.getUeberdachteSitzplaetze(), false);
                    ((DoppelLabelEntry) tabellenwerte[4][i]).getLinks().setText(m_clStadien[i - 2].getLogen() + "");
                    ((DoppelLabelEntry) tabellenwerte[4][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getLogen() - m_clStadium.getLogen(), false);
                    ((DoppelLabelEntry) tabellenwerte[5][i]).getLinks().setText(m_clStadien[i - 2].getGesamtgroesse() + "");
                    ((DoppelLabelEntry) tabellenwerte[5][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].getGesamtgroesse() - m_clStadium.getGesamtgroesse(), false);
                    ((DoppelLabelEntry) tabellenwerte[6][i]).getLinks().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen(), true);
                    ((DoppelLabelEntry) tabellenwerte[6][i]).getRechts().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen() - m_clStadium.calcMaxEinahmen(), true);
                    ((DoppelLabelEntry) tabellenwerte[7][i]).getLinks().setSpezialNumber(-m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]), true);
                    ((DoppelLabelEntry) tabellenwerte[7][i]).getRechts().setSpezialNumber(-(m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]) - m_clArenaSizer.calcUnterhalt(m_clStadium)), true);
                    ((DoppelLabelEntry) tabellenwerte[8][i]).getLinks().setSpezialNumber(m_clStadien[i - 2].calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2]), true);
                    ((DoppelLabelEntry) tabellenwerte[8][i]).getRechts().setSpezialNumber((m_clStadien[i - 2].calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadien[i - 2])) - (m_clStadium.calcMaxEinahmen() - m_clArenaSizer.calcUnterhalt(m_clStadium)), true);
                    ((DoppelLabelEntry) tabellenwerte[9][i]).getLinks().setSpezialNumber(-m_clStadien[i - 2].getAusbauKosten(), true);
                }
                m_jtArena.setModel(new de.hattrickorganizer.gui.model.VAPTableModel(UEBERSCHRIFT, tabellenwerte));
                m_jtArena.getColumnModel().getColumn(0).setMinWidth(Helper.calcCellWidth(150));
                m_jtArena.getColumnModel().getColumn(1).setMinWidth(Helper.calcCellWidth(160));
                m_jtArena.getColumnModel().getColumn(2).setMinWidth(Helper.calcCellWidth(160));
                m_jtArena.getColumnModel().getColumn(3).setMinWidth(Helper.calcCellWidth(160));
                m_jtArena.getColumnModel().getColumn(4).setMinWidth(Helper.calcCellWidth(160));
            }
        }
    }
}
