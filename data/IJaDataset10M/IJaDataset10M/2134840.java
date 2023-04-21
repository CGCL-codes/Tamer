package client.gui;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.MenuKeyListener;
import common.CampaignData;
import common.House;
import common.Planet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.StringTokenizer;
import client.MWClient;
import client.campaign.CArmy;
import client.gui.dialog.ArmyViewerDialog;
import client.gui.dialog.PlanetNameDialog;
import client.gui.dialog.PlayerNameDialog;

/**
 * Create an "Attack" menu. Used in Map, CMainFramge, etc. to generate
 * menus which show a player's currently available operation options.
 * 
 * Unlike the old Task's dynamic menus, this is an actual class/object,
 * not a static method which returns a contructed menu. Also allows for
 * clearing/updating.
 */
public class AttackMenu extends JMenu implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 7420602115238025725L;

    private MWClient mwclient;

    private int armyID;

    private String planetName;

    private static int OPRANGE = 0;

    private static int OPCOLOR = 1;

    private static int OPFACINFO = 3;

    private static int OPHOMEINFO = 4;

    private static int OPLAUNCHON = 5;

    private static int OPLAUNCHFROM = 6;

    private static int OPMINOWN = 7;

    private static int OPMAXOWN = 8;

    private static int OPLEGALDEFENDERS = 9;

    private static int OPALLOWEDPLANETFLAGS = 10;

    private static int OPDISALLOWEDPLANETFLAGS = 11;

    private static int OPAFR = 12;

    private static int OPACTIVE = 13;

    private static int OPACCESSLEVEL = 14;

    public AttackMenu(MWClient mwclient, int armyID, String planetName) {
        super("Attack");
        this.mwclient = mwclient;
        this.armyID = armyID;
        this.planetName = planetName;
    }

    public void updateMenuItems(boolean fullMenu) {
        this.removeAll();
        TreeMap<String, String[]> allOps = mwclient.getAllOps();
        if (armyID < 0) {
            TreeSet<String> allEligibles = new TreeSet<String>();
            if (planetName == null || planetName.trim().equals("") || planetName.equals("-1")) {
                for (CArmy currA : mwclient.getPlayer().getArmies()) allEligibles.addAll(currA.getLegalOperations());
            } else {
                Planet tp = mwclient.getData().getPlanetByName(planetName);
                int houseID = mwclient.getPlayer().getHouseFightingFor().getId();
                int accessLevel = mwclient.getPlayer().getSubFactionAccess();
                TreeSet<String> tempEligibles = new TreeSet<String>();
                for (CArmy currA : mwclient.getPlayer().getArmies()) tempEligibles.addAll(currA.getLegalOperations());
                for (String currOpName : tempEligibles) {
                    String[] opProps = mwclient.getAllOps().get(currOpName);
                    double range = Double.parseDouble(opProps[AttackMenu.OPRANGE]);
                    String facInfo = opProps[AttackMenu.OPFACINFO];
                    String homeInfo = opProps[AttackMenu.OPHOMEINFO];
                    int launchOn = Integer.parseInt(opProps[AttackMenu.OPLAUNCHON]);
                    int launchFrom = Integer.parseInt(opProps[AttackMenu.OPLAUNCHFROM]);
                    int minOwn = Integer.parseInt(opProps[AttackMenu.OPMINOWN]);
                    int maxOwn = Integer.parseInt(opProps[AttackMenu.OPMAXOWN]);
                    String legalDefenders = opProps[AttackMenu.OPLEGALDEFENDERS];
                    String allowPlanetFlags = opProps[AttackMenu.OPALLOWEDPLANETFLAGS] + "^";
                    String disallowPlanetFlags = opProps[AttackMenu.OPDISALLOWEDPLANETFLAGS] + "^";
                    boolean reserveOnly = Boolean.parseBoolean(opProps[AttackMenu.OPAFR]);
                    int minAccessLevel = Integer.parseInt(opProps[AttackMenu.OPACCESSLEVEL]);
                    if (accessLevel < minAccessLevel) continue;
                    if (!legalDefenders.startsWith("allFactions")) {
                        TreeMap<String, Object> legalDefTree = new TreeMap<String, Object>();
                        StringTokenizer legalDefTokenizer = new StringTokenizer(legalDefenders, "$");
                        while (legalDefTokenizer.hasMoreTokens()) legalDefTree.put(legalDefTokenizer.nextToken(), null);
                        boolean foundDefender = false;
                        for (House currH : tp.getInfluence().getHouses()) {
                            if (legalDefTree.containsKey(currH.getName())) {
                                foundDefender = true;
                                break;
                            }
                        }
                        if (!foundDefender) continue;
                    }
                    if (tp.getFactoryCount() > 0 && facInfo.equals("none")) continue; else if (tp.getFactoryCount() < 1 && facInfo.equals("only")) continue;
                    if (tp.isHomeWorld() && homeInfo.equals("none")) continue; else if (!tp.isHomeWorld() && homeInfo.equals("only")) continue;
                    if (tp.getInfluence().getInfluence(houseID) < minOwn) continue;
                    if (tp.getInfluence().getInfluence(houseID) > maxOwn) continue;
                    if (tp.getInfluence().getInfluence(houseID) >= launchOn) {
                        allEligibles.add(currOpName);
                        continue;
                    }
                    if (allowPlanetFlags.length() > 0) {
                        boolean allowOp = true;
                        StringTokenizer st = new StringTokenizer(allowPlanetFlags, "^");
                        while (st.hasMoreTokens()) {
                            String key = st.nextToken();
                            if (key.trim().length() < 1) continue;
                            if (!tp.getPlanetFlags().containsKey(key)) {
                                CampaignData.mwlog.errLog(tp.getName() + " does not have flag: " + key);
                                allowOp = false;
                                break;
                            }
                        }
                        if (!allowOp) continue;
                    }
                    if (reserveOnly) continue;
                    if (disallowPlanetFlags.length() > 0) {
                        boolean allowOp = true;
                        StringTokenizer st = new StringTokenizer(disallowPlanetFlags, "^");
                        while (st.hasMoreTokens()) {
                            String key = st.nextToken();
                            if (key.trim().length() < 1) continue;
                            if (tp.getPlanetFlags().containsKey(key)) {
                                allowOp = false;
                                CampaignData.mwlog.errLog(tp.getName() + " has flag: " + key);
                                break;
                            }
                        }
                        if (!allowOp) continue;
                    }
                    for (Planet currP : mwclient.getData().getAllPlanets()) {
                        if (currP.getInfluence().getInfluence(houseID) >= launchFrom) {
                            double tdist = currP.getPosition().distanceSq(tp.getPosition());
                            if (tdist <= range) {
                                allEligibles.add(currOpName);
                                break;
                            }
                        }
                    }
                }
            }
            if (allEligibles.size() <= 0) {
                JMenuItem filler = new JMenuItem("None");
                this.add(filler);
            }
            for (String currName : allEligibles) {
                if (!allOps.containsKey(currName)) {
                    mwclient.updateOpData(false);
                    if (!allOps.containsKey(currName)) {
                        CampaignData.mwlog.errLog("Error in updateMenuItems(): no _" + currName + "_ in allOps.");
                        StringBuilder allOpsList = new StringBuilder("allOps contains: ");
                        for (String currO : allOps.keySet()) allOpsList.append(currO + " ");
                        CampaignData.mwlog.errLog(allOpsList.toString());
                        continue;
                    }
                }
                String[] settings = allOps.get(currName);
                String color = settings[AttackMenu.OPCOLOR];
                String menuItemName = "";
                if (!fullMenu) menuItemName = "<html><font color=" + color + ">" + currName + "</font></html>"; else menuItemName = " - " + currName;
                JMenuItem currItem = new JMenuItem(menuItemName);
                currItem.addActionListener(this);
                currItem.setActionCommand(currName);
                this.add(currItem);
            }
        } else {
            CArmy clickArmy = mwclient.getPlayer().getArmy(armyID);
            if (clickArmy != null) {
                if (clickArmy.getLegalOperations().size() <= 0) {
                    JMenuItem filler = new JMenuItem("None");
                    this.add(filler);
                }
                for (String currName : clickArmy.getLegalOperations()) {
                    if (!allOps.containsKey(currName)) {
                        CampaignData.mwlog.errLog("Error in updateMenuItems(): no _" + currName + "_ in allOps.");
                        String allOpsList = "allOps contains: ";
                        for (String currO : allOps.keySet()) allOpsList += currO + " ";
                        CampaignData.mwlog.errLog(allOpsList);
                        continue;
                    }
                    String[] settings = allOps.get(currName);
                    if (Boolean.parseBoolean(settings[AttackMenu.OPAFR])) continue;
                    String color = settings[AttackMenu.OPCOLOR];
                    String menuItemName = "";
                    if (!fullMenu) menuItemName = "<html><font color=" + color + ">" + currName + "</font></html>"; else menuItemName = " - " + currName;
                    JMenuItem currItem = new JMenuItem(menuItemName);
                    currItem.addActionListener(this);
                    currItem.setActionCommand(currName);
                    this.add(currItem);
                }
            }
        }
        if (fullMenu) {
            this.setText("Games");
            this.setMnemonic('G');
            JMenuItem toAdd = new JMenuItem("Attacks:");
            MouseListener[] mouse = toAdd.getMouseListeners();
            FocusListener[] focus = toAdd.getFocusListeners();
            MenuKeyListener[] menukey = toAdd.getMenuKeyListeners();
            PropertyChangeListener[] property = toAdd.getPropertyChangeListeners();
            for (int i = 0; i < mouse.length; i++) toAdd.removeMouseListener(mouse[i]);
            for (int i = 0; i < focus.length; i++) toAdd.removeFocusListener(focus[i]);
            for (int i = 0; i < menukey.length; i++) toAdd.removeFocusListener(focus[i]);
            for (int i = 0; i < property.length; i++) toAdd.removePropertyChangeListener(property[i]);
            this.add(toAdd, 0);
            if (Boolean.parseBoolean(mwclient.getserverConfigs("AllowAttackFromReserve")) && mwclient.getMyStatus() == MWClient.STATUS_RESERVE) {
                this.add(new JSeparator());
                toAdd = new JMenuItem("Attack From Reserve");
                toAdd.addActionListener(this);
                toAdd.setActionCommand("cmdAttackFromReserve");
                this.add(toAdd);
            }
            this.add(new JSeparator());
            toAdd = new JMenuItem("Check Access");
            toAdd.addActionListener(this);
            toAdd.setActionCommand("cmdCheckAccess");
            this.add(toAdd);
            toAdd = new JMenuItem("Games Status");
            toAdd.addActionListener(this);
            toAdd.setActionCommand("cmdGamesStatus");
            toAdd.setMnemonic('G');
            this.add(toAdd);
            if (mwclient.getMyStatus() >= MWClient.STATUS_ACTIVE) {
                toAdd = new JMenuItem("Cancel Game");
                toAdd.addActionListener(this);
                toAdd.setActionCommand("cmdCancelGames");
                this.add(toAdd);
            }
        }
    }

    /**
	 * The actionCommand has to be of the following structure:
	 * 
	 * aid|pid 
	 * *or*
	 * aid|pid|arg0|arg1|arg2...
	 * 
	 * "aid" is the ArmyID to use. If "-1", choice will be given.
	 * "pid" is the PlanetID to attack. If "-1", menu will open.
	 * 
	 * cmd is the command and one of those:
	 * 
	 * if arg0 == "multi"
	 * arg1: maxattackers
	 * arg2: minattackers
	 * arg3: maxdefenders
	 * arg4: mindefenders
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed
	 */
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        if (name.equals("cmdCancelGames")) {
            mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c terminate");
            return;
        }
        if (name.equals("cmdCheckAccess")) {
            if (mwclient.getPlayer().getArmies().size() <= 0) {
                String toUser = "CH|CLIENT: It's impossible to check legality when you have no armies.";
                mwclient.doParseDataInput(toUser);
                return;
            }
            TreeSet<String> names = new TreeSet<String>();
            for (CArmy currArmy : mwclient.getPlayer().getArmies()) names.add("#" + currArmy.getID() + " - BV: " + currArmy.getBV());
            JComboBox armyCombo = new JComboBox(names.toArray());
            armyCombo.setEditable(false);
            JComboBox attackCombo = new JComboBox(mwclient.getAllOps().keySet().toArray());
            attackCombo.setEditable(false);
            JPanel holderPanel = new JPanel();
            holderPanel.setLayout(new BoxLayout(holderPanel, BoxLayout.Y_AXIS));
            holderPanel.add(armyCombo);
            holderPanel.add(attackCombo);
            JOptionPane jop = new JOptionPane(holderPanel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            JDialog dlg = jop.createDialog(mwclient.getMainFrame(), "Select army and attack type.");
            armyCombo.grabFocus();
            armyCombo.getEditor().selectAll();
            dlg.setVisible(true);
            if ((Integer) jop.getValue() == JOptionPane.CANCEL_OPTION) return;
            String attackName = (String) attackCombo.getSelectedItem();
            String armyName = (String) armyCombo.getSelectedItem();
            armyID = Integer.parseInt(armyName.substring(1, armyName.indexOf(" ")).trim());
            mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c checkarmyeligibility#" + armyID + "#" + attackName);
            return;
        }
        if (name.equals("cmdGamesStatus")) {
            mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c games");
            return;
        }
        if (name.equals("cmdAttackFromReserve")) {
            String planet = "";
            String target = "";
            String Op = "";
            TreeSet<String> allEligibles = new TreeSet<String>();
            for (CArmy currA : mwclient.getPlayer().getArmies()) allEligibles.addAll(currA.getLegalOperations());
            Iterator<String> i = allEligibles.iterator();
            while (i.hasNext()) {
                String[] currProperties = mwclient.getAllOps().get(i.next());
                if (Boolean.parseBoolean(currProperties[AttackMenu.OPACTIVE])) i.remove();
            }
            JComboBox attackCombo = new JComboBox(allEligibles.toArray());
            attackCombo.setEditable(false);
            JOptionPane jop = new JOptionPane(attackCombo, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            JDialog dlg = jop.createDialog(mwclient.getMainFrame(), "Select an operation.");
            dlg.setVisible(true);
            if ((Integer) jop.getValue() == JOptionPane.CANCEL_OPTION) return;
            Op = (String) attackCombo.getSelectedItem();
            String[] opProperties = mwclient.getAllOps().get(Op);
            PlanetNameDialog planetDialog = new PlanetNameDialog(mwclient, "Choose a planet", opProperties);
            planetDialog.setVisible(true);
            planet = planetDialog.getPlanetName();
            planetDialog.dispose();
            if (planet == null) return;
            PlayerNameDialog playerDialog = new PlayerNameDialog(mwclient, "Select an opponent", PlayerNameDialog.ANY_PLAYER);
            playerDialog.setVisible(true);
            target = playerDialog.getPlayerName();
            playerDialog.dispose();
            if (target == null) return;
            new ArmyViewerDialog(mwclient, Op, null, ArmyViewerDialog.AVD_ATTACKFROMRESERVE, planet, target, -1, -1);
            return;
        }
        if (mwclient.getMyStatus() < MWClient.STATUS_ACTIVE) {
            String toUser = "CH|CLIENT: You must be active in order to initiate standard attacks.";
            mwclient.doParseDataInput(toUser);
            return;
        } else if (mwclient.getMyStatus() == MWClient.STATUS_FIGHTING) {
            String toUser = "CH|CLIENT: You may not initiate an attack while you are in a game.";
            mwclient.doParseDataInput(toUser);
            return;
        }
        if (planetName.equals("-1")) {
            String[] opProperties = mwclient.getAllOps().get(name);
            PlanetNameDialog planetdialog = new PlanetNameDialog(mwclient, "Select a planet to attack.", opProperties);
            planetdialog.setVisible(true);
            planetName = planetdialog.getPlanetName();
            planetdialog.dispose();
            if (planetName == null || planetName.trim().length() == 0) return;
        }
        if (armyID < 0) {
            new ArmyViewerDialog(mwclient, name, null, ArmyViewerDialog.AVD_ATTACK, planetName, null, -1, -1);
        } else mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c attack#" + name + "#" + armyID + "#" + planetName);
    }
}
