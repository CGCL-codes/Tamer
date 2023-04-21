package ho.module.lineup2;

import ho.core.constants.player.PlayerSkill;
import ho.core.datatype.CBItem;
import ho.core.gui.Updateable;
import ho.core.gui.comp.entry.SpielerLabelEntry;
import ho.core.gui.comp.panel.ImagePanel;
import ho.core.gui.model.SpielerCBItem;
import ho.core.gui.model.SpielerCBItemRenderer;
import ho.core.gui.theme.HOColorName;
import ho.core.gui.theme.ThemeManager;
import ho.core.model.HOVerwaltung;
import ho.core.model.player.ISpielerPosition;
import ho.core.model.player.Spieler;
import ho.core.model.player.SpielerPosition;
import ho.core.util.Helper;
import ho.module.lineup.Lineup;
import ho.module.lineup.LineupAssistantSelectorOverlay;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * Panel, in dem die Spielerposition dargestellt wird und geändert werden kann
 */
class PlayerPositionPanel extends ImagePanel implements ItemListener {

    private static final long serialVersionUID = 3121389904282953L;

    private static int PLAYER_POSITION_PANEL_WIDTH = Helper.calcCellWidth(160);

    private static int PLAYER_POSITION_PANEL_HEIGHT_FULL = Helper.calcCellWidth(80);

    private static int PLAYER_POSITION_PANEL_HEIGHT_REDUCED = Helper.calcCellWidth(50);

    private static int MINI_PLAYER_POSITION_PANEL_WIDTH = Helper.calcCellWidth(120);

    private static int MINI_PLAYER_POSITION_PANEL_HEIGHT = Helper.calcCellWidth(32);

    private static SpielerCBItem m_clNullSpieler = new SpielerCBItem("", 0f, null, true);

    private final JComboBox playerComboBox = new JComboBox();

    private final JComboBox tacticComboBox = new JComboBox();

    private final JLabel positionLabel = new JLabel();

    private final SpielerCBItem playerCombo = new SpielerCBItem("", 0f, null, true);

    private Updateable updater;

    private int positionID = -1;

    private int playerId = -1;

    private int tacticOrder = -1;

    private final GridBagLayout layout = new GridBagLayout();

    private JLayeredPane layeredPane = new JLayeredPane();

    private Lineup lineup;

    public PlayerPositionPanel(Updateable updateable, Lineup lineup, int positionsID) {
        super(false);
        this.lineup = lineup;
        this.updater = updateable;
        this.positionID = positionsID;
        setOpaque(true);
        initTaktik(null);
        initLabel();
        initComponents(true);
    }

    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            Spieler spieler = getSelectedPlayer();
            if (itemEvent.getSource().equals(this.playerComboBox)) {
                switch(this.positionID) {
                    case ISpielerPosition.setPieces:
                        int id = (spieler != null) ? spieler.getSpielerID() : 0;
                        this.lineup.setKicker(id);
                        break;
                    case ISpielerPosition.captain:
                        id = (spieler != null) ? spieler.getSpielerID() : 0;
                        this.lineup.setKapitaen(id);
                        break;
                    default:
                        id = (spieler != null) ? spieler.getSpielerID() : 0;
                        this.lineup.setSpielerAtPosition(this.positionID, id);
                }
                if (spieler != null) {
                    this.playerComboBox.setForeground(SpielerLabelEntry.getForegroundForSpieler(spieler));
                }
                setTaktik(getTactic(), spieler);
            } else if (itemEvent.getSource().equals(this.tacticComboBox)) {
                this.lineup.getPositionById(this.positionID).setTaktik(getTactic());
            }
            if (spieler != null) {
            }
            this.updater.update();
        }
    }

    /**
	 * Erneuert die Daten in den Komponenten
	 * 
	 * @param spieler
	 */
    public void refresh(Vector<Spieler> spieler) {
        Spieler aktuellerSpieler = null;
        playerId = -1;
        if (this.positionID == ISpielerPosition.setPieces) {
            aktuellerSpieler = HOVerwaltung.instance().getModel().getSpieler(this.lineup.getKicker());
            if (aktuellerSpieler != null) {
                playerId = aktuellerSpieler.getSpielerID();
            }
            tacticOrder = -1;
            Spieler keeper = this.lineup.getPlayerByPositionID(ISpielerPosition.keeper);
            if (keeper != null) {
                Vector<Spieler> tmpSpieler = new Vector<Spieler>(spieler.size() - 1);
                for (int i = 0; i < spieler.size(); i++) {
                    if (keeper.getSpielerID() != spieler.get(i).getSpielerID()) {
                        tmpSpieler.add(spieler.get(i));
                    }
                }
                spieler = tmpSpieler;
            }
        } else if (this.positionID == ISpielerPosition.captain) {
            aktuellerSpieler = HOVerwaltung.instance().getModel().getSpieler(this.lineup.getKapitaen());
            if (aktuellerSpieler != null) {
                playerId = aktuellerSpieler.getSpielerID();
            }
            tacticOrder = -1;
        } else {
            SpielerPosition position = this.lineup.getPositionById(this.positionID);
            if (position != null) {
                aktuellerSpieler = HOVerwaltung.instance().getModel().getSpieler(position.getSpielerId());
                if (aktuellerSpieler != null) {
                    this.playerComboBox.setEnabled(true);
                    playerId = aktuellerSpieler.getSpielerID();
                } else {
                    if ((this.lineup.hasFreePosition() == false) && (this.positionID >= ISpielerPosition.keeper) && (this.positionID < ISpielerPosition.startReserves)) {
                        this.playerComboBox.setEnabled(false);
                    } else {
                        this.playerComboBox.setEnabled(true);
                    }
                }
                tacticOrder = position.getTaktik();
                setTaktik(position.getTaktik(), aktuellerSpieler);
            }
        }
        setSpielerListe(spieler, aktuellerSpieler);
        initLabel();
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTacticOrder() {
        return tacticOrder;
    }

    public LayoutManager getSwapLayout() {
        return layout;
    }

    public void addSwapItem(Component c) {
        this.layeredPane.add(c, 1);
    }

    public void addAssistantOverlay(LineupAssistantSelectorOverlay overlay) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 3;
        this.layeredPane.add(overlay, constraints, 2);
        repaint();
    }

    public void removeAssistantOverlay(LineupAssistantSelectorOverlay overlay) {
        this.layeredPane.remove(overlay);
        repaint();
    }

    /**
	 * Exposes the player combo box to reset the swap button if needed.
	 * 
	 * @return the player {@link JComboBox}.
	 */
    JComboBox getPlayerComboBox() {
        return this.playerComboBox;
    }

    int getPositionsID() {
        return this.positionID;
    }

    /**
	 * Gibt den aktuellen Spieler auf dieser Position zurück, oder null, wenn
	 * keiner gewählt wurde
	 * 
	 * @return
	 */
    private Spieler getSelectedPlayer() {
        Object obj = this.playerComboBox.getSelectedItem();
        if ((obj != null) && obj instanceof SpielerCBItem) {
            return ((SpielerCBItem) obj).getSpieler();
        }
        return null;
    }

    /**
	 * Gibt die Taktik an
	 * 
	 * @return
	 */
    private byte getTactic() {
        if (this.tacticComboBox.getSelectedItem() == null) {
            return -1;
        }
        return (byte) ((CBItem) this.tacticComboBox.getSelectedItem()).getId();
    }

    /**
	 * Erzeugt die Komponenten, Die CB für die Spieler und den Listener nicht
	 * vergessen!
	 * 
	 * @param aenderbar
	 */
    private void initComponents(boolean aenderbar) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 0;
        constraints.insets = new Insets(1, 2, 1, 2);
        this.layeredPane.setLayout(layout);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        this.layeredPane.add(this.positionLabel, constraints, 1);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        this.playerComboBox.setMaximumRowCount(15);
        this.playerComboBox.setRenderer(new SpielerCBItemRenderer());
        this.layeredPane.add(this.playerComboBox, constraints, 1);
        if (!aenderbar) {
            this.playerComboBox.setEnabled(false);
        }
        this.playerComboBox.setBackground(ThemeManager.getColor(HOColorName.TABLEENTRY_BG));
        if (this.tacticComboBox.getItemCount() > 1) {
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.gridwidth = 2;
            if (!aenderbar) {
                this.tacticComboBox.setEnabled(false);
            }
            this.tacticComboBox.setBackground(this.playerComboBox.getBackground());
            this.layeredPane.add(this.tacticComboBox, constraints, 1);
            setPreferredSize(new Dimension(PLAYER_POSITION_PANEL_WIDTH, PLAYER_POSITION_PANEL_HEIGHT_FULL));
        } else {
            setPreferredSize(new Dimension(PLAYER_POSITION_PANEL_WIDTH, PLAYER_POSITION_PANEL_HEIGHT_REDUCED));
        }
        this.layeredPane.setPreferredSize(getPreferredSize());
        add(this.layeredPane);
    }

    /**
	 * Setzt die Liste der möglichen Spieler für diese Position und den aktuell
	 * gewählten Spieler
	 * 
	 * @param spielerListe
	 * @param aktuellerSpieler
	 */
    private void setSpielerListe(Vector<Spieler> spielerListe, Spieler aktuellerSpieler) {
        this.playerComboBox.removeItemListener(this);
        final DefaultComboBoxModel cbmodel = ((DefaultComboBoxModel) this.playerComboBox.getModel());
        cbmodel.removeAllElements();
        if (aktuellerSpieler != null) {
            cbmodel.addElement(createSpielerCBItem(aktuellerSpieler));
        }
        cbmodel.addElement(m_clNullSpieler);
        SpielerCBItem[] cbItems = new SpielerCBItem[spielerListe.size()];
        for (int i = 0; i < spielerListe.size(); i++) {
            cbItems[i] = createSpielerCBItem(((Spieler) spielerListe.get(i)));
        }
        java.util.Arrays.sort(cbItems);
        for (int i = 0; i < cbItems.length; i++) {
            cbmodel.addElement(cbItems[i]);
        }
        if (aktuellerSpieler != null) {
            this.playerComboBox.setForeground(SpielerLabelEntry.getForegroundForSpieler(aktuellerSpieler));
        }
        cbItems = null;
        this.playerComboBox.addItemListener(this);
        if (aktuellerSpieler != null) {
            setTaktik(getTactic(), aktuellerSpieler);
        }
    }

    /**
	 * Setzt die aktuelle Taktik
	 * 
	 * @param taktik
	 * @param aktuellerSpieler
	 */
    private void setTaktik(byte taktik, Spieler aktuellerSpieler) {
        this.tacticComboBox.removeItemListener(this);
        initTaktik(aktuellerSpieler);
        Helper.markierenComboBox(this.tacticComboBox, taktik);
        this.tacticComboBox.addItemListener(this);
    }

    /**
	 * Setzt das Label
	 */
    private void initLabel() {
        if (this.positionID == ISpielerPosition.setPieces) {
            this.positionLabel.setText(getLanguageString("Standards"));
        } else if (this.positionID == ISpielerPosition.captain) {
            this.positionLabel.setText(getLanguageString("Spielfuehrer"));
        } else {
            SpielerPosition position = this.lineup.getPositionById(this.positionID);
            if (position != null) {
                if ((this.tacticComboBox.getItemCount() == 1) && (position.getId() != ISpielerPosition.keeper)) {
                    if (position.getId() == ISpielerPosition.substDefender) {
                        this.positionLabel.setText(getLanguageString("Reserve") + " " + getLanguageString("defender"));
                    } else {
                        this.positionLabel.setText(getLanguageString("Reserve") + " " + position.getPositionName());
                    }
                } else {
                    this.positionLabel.setText(position.getPositionName());
                }
            }
        }
    }

    /**
	 * Setzt die Taktik je nach SpielerPosition
	 * 
	 * @param aktuellerSpieler
	 */
    private void initTaktik(Spieler aktuellerSpieler) {
        this.tacticComboBox.removeAllItems();
        switch(this.positionID) {
            case ISpielerPosition.keeper:
                {
                    this.tacticComboBox.addItem(new CBItem(getLanguageString("Normal"), ISpielerPosition.NORMAL));
                    break;
                }
            case ISpielerPosition.rightBack:
            case ISpielerPosition.leftBack:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.TOWARDS_MIDDLE);
                    break;
                }
            case ISpielerPosition.rightCentralDefender:
            case ISpielerPosition.leftCentralDefender:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.TOWARDS_WING);
                    break;
                }
            case ISpielerPosition.middleCentralDefender:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    break;
                }
            case ISpielerPosition.rightInnerMidfield:
            case ISpielerPosition.leftInnerMidfield:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.TOWARDS_WING);
                    break;
                }
            case ISpielerPosition.centralInnerMidfield:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    break;
                }
            case ISpielerPosition.leftWinger:
            case ISpielerPosition.rightWinger:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.OFFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.TOWARDS_MIDDLE);
                    break;
                }
            case ISpielerPosition.rightForward:
            case ISpielerPosition.leftForward:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    addTactic(aktuellerSpieler, ISpielerPosition.TOWARDS_WING);
                    break;
                }
            case ISpielerPosition.centralForward:
                {
                    addTactic(aktuellerSpieler, ISpielerPosition.NORMAL);
                    addTactic(aktuellerSpieler, ISpielerPosition.DEFENSIVE);
                    break;
                }
            case ISpielerPosition.substDefender:
            case ISpielerPosition.substForward:
            case ISpielerPosition.substInnerMidfield:
            case ISpielerPosition.substKeeper:
            case ISpielerPosition.substWinger:
                {
                    this.tacticComboBox.addItem(new CBItem(getLanguageString("Normal"), ISpielerPosition.NORMAL));
                    break;
                }
            default:
                this.tacticComboBox.addItem(new CBItem(getLanguageString("Normal"), ISpielerPosition.NORMAL));
        }
    }

    private void addTactic(Spieler player, byte playerPosition) {
        String text = null;
        switch(playerPosition) {
            case ISpielerPosition.OFFENSIVE:
                text = getLanguageString("Offensiv");
                break;
            case ISpielerPosition.DEFENSIVE:
                text = getLanguageString("Defensiv");
                break;
            case ISpielerPosition.TOWARDS_MIDDLE:
                text = getLanguageString("zurMitte");
                break;
            default:
                text = getLanguageString("Normal");
        }
        if (player != null) {
            byte position = SpielerPosition.getPosition(this.positionID, playerPosition);
            text += " (" + player.calcPosValue(position, true) + ")";
        }
        this.tacticComboBox.addItem(new CBItem(text, playerPosition));
    }

    private SpielerCBItem createSpielerCBItem(Spieler spieler) {
        SpielerCBItem item = new SpielerCBItem("", 0f, null, true);
        String spielerName = getShortPlayerName(spieler);
        if (this.positionID == ISpielerPosition.setPieces) {
            item.setValues(spielerName, spieler.getStandards() + spieler.getSubskill4SkillWithOffset(PlayerSkill.SET_PIECES), spieler);
        } else if (this.positionID == ISpielerPosition.captain) {
            item.setValues(spielerName, Helper.round(this.lineup.getAverageExperience(spieler.getSpielerID()), ho.core.model.UserParameter.instance().anzahlNachkommastellen), spieler);
        } else {
            SpielerPosition position = this.lineup.getPositionById(this.positionID);
            if (position != null) {
                item.setValues(spielerName, spieler.calcPosValue(position.getPosition(), true), spieler);
                return item;
            }
        }
        return item;
    }

    /**
	 * Returns a string with just initial as first name
	 * 
	 * @param spieler
	 * @return
	 */
    private String getShortPlayerName(Spieler spieler) {
        String fullName = spieler.getName();
        return fullName.substring(0, 1) + "." + fullName.substring(fullName.indexOf(" ") + 1);
    }

    private String getLanguageString(String key) {
        return HOVerwaltung.instance().getLanguageString(key);
    }
}
