package ho.module.lineup;

import ho.core.datatype.CBItem;
import ho.core.gui.HOMainFrame;
import ho.core.gui.comp.panel.ImagePanel;
import ho.core.gui.model.AufstellungCBItem;
import ho.core.gui.theme.HOColorName;
import ho.core.gui.theme.HOIconName;
import ho.core.gui.theme.ThemeManager;
import ho.core.model.HOModel;
import ho.core.model.HOVerwaltung;
import ho.core.model.UserParameter;
import ho.core.model.player.ISpielerPosition;
import ho.core.model.player.Spieler;
import ho.core.util.HOLogger;
import ho.core.util.Helper;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Die automatische Aufstellung wird hier konfiguriert und gestartet
 */
public class AufstellungsAssistentPanel extends ImagePanel implements ActionListener, ItemListener {

    private static final long serialVersionUID = 5271343329674809429L;

    private JButton m_jbElfmeter = new JButton(ThemeManager.getScaledIcon(HOIconName.BUTTON_BALL, 24, 24));

    private final JButton m_jbLoeschen = new JButton(ThemeManager.getIcon(HOIconName.CLEARASSIST));

    private final JButton m_jbOK = new JButton(ThemeManager.getIcon(HOIconName.STARTASSIST));

    private final JButton m_jbReserveLoeschen = new JButton(ThemeManager.getIcon(HOIconName.CLEARRESERVE));

    private final JCheckBox m_jchForm = new JCheckBox(HOVerwaltung.instance().getLanguageString("Form_beruecksichtigen"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_form);

    private final JCheckBox m_jchGesperrte = new JCheckBox(HOVerwaltung.instance().getLanguageString("Gesperrte_aufstellen"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_gesperrt);

    private final JCheckBox m_jchIdealPosition = new JCheckBox(HOVerwaltung.instance().getLanguageString("Idealposition_zuerst"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_idealPosition);

    private final JCheckBox m_jchLast = new JCheckBox(HOVerwaltung.instance().getLanguageString("NotLast_aufstellen"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_notLast);

    private final JCheckBox m_jchListBoxGruppenFilter = new JCheckBox(HOVerwaltung.instance().getLanguageString("ListBoxGruppenFilter"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_cbfilter);

    private final JCheckBox m_jchNot = new JCheckBox(HOVerwaltung.instance().getLanguageString("Not"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_not);

    private final JCheckBox m_jchVerletzte = new JCheckBox(HOVerwaltung.instance().getLanguageString("Verletze_aufstellen"), ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_verletzt);

    private final JComboBox m_jcbGruppe = new JComboBox(HOIconName.TEAMSMILIES);

    private final JComboBox m_jcbWetter = new JComboBox(Helper.WETTER);

    private final CBItem[] REIHENFOLGE = { new CBItem(HOVerwaltung.instance().getLanguageString("AW-MF-ST"), LineupAssistant.AW_MF_ST), new CBItem(HOVerwaltung.instance().getLanguageString("AW-ST-MF"), LineupAssistant.AW_ST_MF), new CBItem(HOVerwaltung.instance().getLanguageString("MF-AW-ST"), LineupAssistant.MF_AW_ST), new CBItem(HOVerwaltung.instance().getLanguageString("MF-ST-AW"), LineupAssistant.MF_ST_AW), new CBItem(HOVerwaltung.instance().getLanguageString("ST-AW-MF"), LineupAssistant.ST_AW_MF), new CBItem(HOVerwaltung.instance().getLanguageString("ST-MF-AW"), LineupAssistant.ST_MF_AW) };

    private JComboBox m_jcbReihenfolge = new JComboBox(REIHENFOLGE);

    private HashMap<PlayerPositionPanel, LineupAssistantSelectorOverlay> positions = new HashMap<PlayerPositionPanel, LineupAssistantSelectorOverlay>();

    JLabel infoLabel = null;

    JButton overlayOk = null;

    JButton overlayCancel = null;

    /**
	 * Creates a new AufstellungsAssistentPanel object.
	 */
    public AufstellungsAssistentPanel() {
        initComponents();
    }

    public final boolean isExcludeLastMatch() {
        return m_jchLast.isSelected();
    }

    public final boolean isFormBeruecksichtigen() {
        return m_jchForm.isSelected();
    }

    public final boolean isGesperrtIgnorieren() {
        return m_jchGesperrte.isSelected();
    }

    public final String getGruppe() {
        return m_jcbGruppe.getSelectedItem().toString();
    }

    public final boolean isGruppenFilter() {
        return m_jchListBoxGruppenFilter.isSelected();
    }

    public final boolean isIdealPositionZuerst() {
        return m_jchIdealPosition.isSelected();
    }

    public final boolean isNotGruppe() {
        return m_jchNot.isSelected();
    }

    public final int getReihenfolge() {
        return ((CBItem) m_jcbReihenfolge.getSelectedItem()).getId();
    }

    public final boolean isVerletztIgnorieren() {
        return m_jchVerletzte.isSelected();
    }

    public final int getWetter() {
        return ((CBItem) m_jcbWetter.getSelectedItem()).getId();
    }

    public final void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        final HOModel hoModel = HOVerwaltung.instance().getModel();
        final HOMainFrame mainFrame = ho.core.gui.HOMainFrame.instance();
        if (actionEvent.getSource().equals(m_jbLoeschen)) {
            hoModel.getAufstellung().resetAufgestellteSpieler();
            hoModel.getAufstellung().setKicker(0);
            hoModel.getAufstellung().setKapitaen(0);
            HOMainFrame.instance().getInfoPanel().setLangInfoText(HOVerwaltung.instance().getLanguageString("Aufstellung_geloescht"));
            mainFrame.getAufstellungsPanel().update();
        } else if (actionEvent.getSource().equals(m_jbReserveLoeschen)) {
            hoModel.getAufstellung().resetReserveBank();
            mainFrame.getAufstellungsPanel().update();
        } else if (actionEvent.getSource().equals(m_jbOK)) {
            displayGUI();
        } else if (actionEvent.getSource().equals(m_jbElfmeter)) {
            new ElfmeterSchuetzenDialog(mainFrame);
        } else if (actionEvent.getSource().equals(m_jchListBoxGruppenFilter) || actionEvent.getSource().equals(m_jchLast)) {
            mainFrame.getAufstellungsPanel().getAufstellungsPositionsPanel().refresh();
        } else if (actionEvent.getSource().equals(m_jcbGruppe) || actionEvent.getSource().equals(m_jchNot)) {
            if (m_jchListBoxGruppenFilter.isSelected()) {
                mainFrame.getAufstellungsPanel().getAufstellungsPositionsPanel().refresh();
            }
        } else if (actionEvent.getSource().equals(overlayOk)) {
            Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
            int reds = 0;
            while (it.hasNext()) {
                if (!it.next().getValue().isSelected()) {
                    reds++;
                }
            }
            if (reds < 3) {
                javax.swing.JOptionPane.showMessageDialog(HOMainFrame.instance().getAufstellungsPanel(), HOVerwaltung.instance().getLanguageString("lineupassist.Error"), HOVerwaltung.instance().getLanguageString("lineupassist.ErrorHeader"), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            removeGUI();
            updateDefaultSelection();
            startAssistant(hoModel, mainFrame);
        } else if (actionEvent.getSource().equals(overlayCancel)) {
            removeGUI();
        }
    }

    public final void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ho.core.gui.HOMainFrame.instance().getAufstellungsPanel().update();
        }
    }

    public void addToAssistant(PlayerPositionPanel positionPanel) {
        positions.put(positionPanel, null);
    }

    private void startAssistant(HOModel hoModel, HOMainFrame mainFrame) {
        Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay> entry = it.next();
            if (!entry.getValue().isSelected()) {
                HOVerwaltung.instance().getModel().getAufstellung().setSpielerAtPosition(entry.getKey().getPositionsID(), 0);
            }
        }
        final Vector<Spieler> vSpieler = new Vector<Spieler>();
        final Vector<Spieler> alleSpieler = hoModel.getAllSpieler();
        for (int i = 0; i < alleSpieler.size(); i++) {
            final ho.core.model.player.Spieler spieler = (ho.core.model.player.Spieler) alleSpieler.get(i);
            if (spieler.isSpielberechtigt() && (((this.getGruppe().trim().equals("") || spieler.getTeamInfoSmilie().equals(this.getGruppe())) && !m_jchNot.isSelected()) || (!spieler.getTeamInfoSmilie().equals(this.getGruppe()) && m_jchNot.isSelected()))) {
                boolean include = true;
                final AufstellungCBItem lastLineup = AufstellungsVergleichHistoryPanel.getLastLineup();
                if (m_jchLast.isSelected() && (lastLineup != null) && lastLineup.getAufstellung().isSpielerInAnfangsElf(spieler.getSpielerID())) {
                    include = false;
                    HOLogger.instance().log(getClass(), "Exclude: " + spieler.getName());
                }
                if (include) {
                    vSpieler.add(spieler);
                }
            }
        }
        hoModel.getAufstellung().doAufstellung(vSpieler, (byte) ((CBItem) m_jcbReihenfolge.getSelectedItem()).getId(), m_jchForm.isSelected(), m_jchIdealPosition.isSelected(), m_jchVerletzte.isSelected(), m_jchGesperrte.isSelected(), ho.core.model.UserParameter.instance().WetterEffektBonus, getWetter());
        mainFrame.getInfoPanel().setLangInfoText(HOVerwaltung.instance().getLanguageString("Autoaufstellung_fertig"));
        mainFrame.getAufstellungsPanel().update();
    }

    private void displayGUI() {
        Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay> entry = it.next();
            if (entry.getValue() == null) {
                boolean selected = true;
                LineupAssistantSelectorOverlay laso = new LineupAssistantSelectorOverlay();
                HashMap<String, String> upValues = ho.core.model.UserParameter.instance().getValues();
                if (UserParameter.instance().assistantSaved) {
                    selected = ho.core.model.UserParameter.instance().getBooleanValue(upValues, "assistant" + entry.getKey().getPositionsID());
                } else {
                    int posId = entry.getKey().getPositionsID();
                    if ((posId == ISpielerPosition.centralForward) || (posId == ISpielerPosition.centralInnerMidfield) || (posId == ISpielerPosition.middleCentralDefender)) {
                        selected = false;
                    }
                }
                laso.setSelected(selected);
                entry.setValue(laso);
            }
            entry.getKey().addAssistantOverlay(entry.getValue());
        }
        JLayeredPane posPanel = HOMainFrame.instance().getAufstellungsPanel().getAufstellungsPositionsPanel().getCenterPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        if (infoLabel == null) {
            infoLabel = new JLabel();
            infoLabel.setText(HOVerwaltung.instance().getLanguageString("lineupassist.Info"));
            infoLabel.setOpaque(false);
        }
        posPanel.add(infoLabel, constraints, 2);
        constraints.gridx = 3;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        if (overlayOk == null) {
            overlayOk = new JButton(HOVerwaltung.instance().getLanguageString("lineupassist.OK"));
            overlayOk.setFont(new Font("serif", Font.BOLD, 16));
            overlayOk.setBackground(ThemeManager.getColor(HOColorName.BUTTON_ASSIST_BG));
            overlayOk.addActionListener(this);
        }
        posPanel.add(overlayOk, constraints, 2);
        constraints.gridx = 4;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        if (overlayCancel == null) {
            overlayCancel = new JButton(HOVerwaltung.instance().getLanguageString("lineupassist.Cancel"));
            overlayCancel.addActionListener(this);
            overlayCancel.setFont(new Font("serif", Font.BOLD, 16));
            overlayCancel.setBackground(overlayOk.getBackground());
        }
        posPanel.add(overlayCancel, constraints, 2);
        posPanel.revalidate();
    }

    private void removeGUI() {
        Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay> entry = it.next();
            entry.getKey().removeAssistantOverlay(entry.getValue());
        }
        JLayeredPane pane = HOMainFrame.instance().getAufstellungsPanel().getAufstellungsPositionsPanel().getCenterPanel();
        pane.remove(infoLabel);
        pane.remove(overlayCancel);
        pane.remove(overlayOk);
        HOMainFrame.instance().getAufstellungsPanel().repaint();
    }

    /**
	 * Returns a HashMap of statuses from the position selection.
	 * The keys are integers containing roleIDs for the positions.
	 * The values are booleans for whether the role should be included or not.
	 * The map does not contain all positions, only those being sent through filtering.
	 * 
	 * @return The HashMap
	 */
    public HashMap<Integer, Boolean> getPositionStatuses() {
        HashMap<Integer, Boolean> returnMap = new HashMap<Integer, Boolean>();
        Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay> entry = it.next();
            returnMap.put(entry.getKey().getPositionsID(), entry.getValue().isSelected());
        }
        return returnMap;
    }

    private void updateDefaultSelection() {
        UserParameter.instance().assistant101 = getStatusForPosition(101);
        UserParameter.instance().assistant102 = getStatusForPosition(102);
        UserParameter.instance().assistant103 = getStatusForPosition(103);
        UserParameter.instance().assistant104 = getStatusForPosition(104);
        UserParameter.instance().assistant105 = getStatusForPosition(105);
        UserParameter.instance().assistant106 = getStatusForPosition(106);
        UserParameter.instance().assistant107 = getStatusForPosition(107);
        UserParameter.instance().assistant108 = getStatusForPosition(108);
        UserParameter.instance().assistant109 = getStatusForPosition(109);
        UserParameter.instance().assistant110 = getStatusForPosition(110);
        UserParameter.instance().assistant111 = getStatusForPosition(111);
        UserParameter.instance().assistant112 = getStatusForPosition(112);
        UserParameter.instance().assistant113 = getStatusForPosition(113);
        UserParameter.instance().assistantSaved = true;
    }

    private boolean getStatusForPosition(int position) {
        Iterator<Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay>> it = positions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<PlayerPositionPanel, LineupAssistantSelectorOverlay> entry = it.next();
            if (entry.getKey().getPositionsID() == position) {
                return entry.getValue().isSelected();
            }
        }
        return false;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panel = new ImagePanel();
        panel.setLayout(new GridLayout(9, 1));
        panel.setOpaque(false);
        final HOVerwaltung hoVerwaltung = ho.core.model.HOVerwaltung.instance();
        m_jcbWetter.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Wetter"));
        m_jcbWetter.setSelectedIndex(1);
        m_jcbWetter.setPreferredSize(new Dimension(50, 20));
        m_jcbWetter.setBackground(ThemeManager.getColor(HOColorName.TABLEENTRY_BG));
        m_jcbWetter.setRenderer(new ho.core.gui.comp.renderer.WeatherListCellRenderer());
        m_jcbWetter.addItemListener(this);
        panel.add(m_jcbWetter);
        final JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setOpaque(false);
        m_jchNot.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Not"));
        m_jchNot.setOpaque(false);
        m_jchNot.addActionListener(this);
        panel2.add(m_jchNot, BorderLayout.WEST);
        m_jcbGruppe.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Gruppe"));
        m_jcbGruppe.setSelectedItem(ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_gruppe);
        m_jcbGruppe.setBackground(ThemeManager.getColor(HOColorName.TABLEENTRY_BG));
        m_jcbGruppe.setRenderer(new ho.core.gui.comp.renderer.SmilieListCellRenderer());
        m_jcbGruppe.addActionListener(this);
        panel2.add(m_jcbGruppe, BorderLayout.CENTER);
        panel.add(panel2);
        m_jchListBoxGruppenFilter.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_GruppeFilter"));
        m_jchListBoxGruppenFilter.setOpaque(false);
        m_jchListBoxGruppenFilter.addActionListener(this);
        panel.add(m_jchListBoxGruppenFilter);
        m_jcbReihenfolge.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Reihenfolge"));
        ho.core.util.Helper.markierenComboBox(m_jcbReihenfolge, ho.core.model.UserParameter.instance().aufstellungsAssistentPanel_reihenfolge);
        panel.add(m_jcbReihenfolge);
        m_jchIdealPosition.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Idealposition"));
        m_jchIdealPosition.setOpaque(false);
        panel.add(m_jchIdealPosition);
        m_jchForm.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Form"));
        m_jchForm.setOpaque(false);
        panel.add(m_jchForm);
        m_jchVerletzte.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Verletzte"));
        m_jchVerletzte.setOpaque(false);
        panel.add(m_jchVerletzte);
        m_jchGesperrte.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_Gesperrte"));
        m_jchGesperrte.setOpaque(false);
        panel.add(m_jchGesperrte);
        m_jchLast.setToolTipText(hoVerwaltung.getLanguageString("tt_AufstellungsAssistent_NotLast"));
        m_jchLast.setOpaque(false);
        m_jchLast.addActionListener(this);
        panel.add(m_jchLast);
        add(panel, BorderLayout.CENTER);
        panel = new JPanel();
        panel.setOpaque(false);
        m_jbLoeschen.setPreferredSize(new Dimension(28, 28));
        m_jbLoeschen.setToolTipText(hoVerwaltung.getLanguageString("Aufstellung_leeren"));
        m_jbLoeschen.addActionListener(this);
        panel.add(m_jbLoeschen);
        m_jbReserveLoeschen.setPreferredSize(new Dimension(28, 28));
        m_jbReserveLoeschen.setToolTipText(hoVerwaltung.getLanguageString("Reservebank_leeren"));
        m_jbReserveLoeschen.addActionListener(this);
        panel.add(m_jbReserveLoeschen);
        m_jbOK.setPreferredSize(new Dimension(28, 28));
        m_jbOK.setToolTipText(hoVerwaltung.getLanguageString("Assistent_starten"));
        m_jbOK.addActionListener(this);
        panel.add(m_jbOK);
        m_jbElfmeter.setPreferredSize(new Dimension(28, 28));
        m_jbElfmeter.setToolTipText(hoVerwaltung.getLanguageString("Elfmeterschuetzen"));
        m_jbElfmeter.addActionListener(this);
        panel.add(m_jbElfmeter);
        add(panel, BorderLayout.SOUTH);
    }
}
