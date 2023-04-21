package megamek.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import megamek.common.*;
import megamek.common.util.Distractable;
import megamek.common.util.DistractableAdapter;

public class DeploymentDisplay extends StatusBarPhaseDisplay implements BoardListener, ActionListener, DoneButtoned, KeyListener, GameListener, BoardViewListener, Distractable {

    private DistractableAdapter distracted = new DistractableAdapter();

    public static final String DEPLOY_TURN = "deployTurn";

    public static final String DEPLOY_NEXT = "deployNext";

    public static final String DEPLOY_LOAD = "deployLoad";

    public static final String DEPLOY_UNLOAD = "deployUnload";

    public Client client;

    private Panel panButtons;

    private Button butNext;

    private Button butTurn;

    private Button butLoad;

    private Button butUnload;

    private Button butDone;

    private int cen = Entity.NONE;

    private boolean turnMode = false;

    /**
     * Creates and lays out a new deployment phase display 
     * for the specified client.
     */
    public DeploymentDisplay(Client client) {
        this.client = client;
        client.addGameListener(this);
        client.game.board.addBoardListener(this);
        setupStatusBar("Waiting to begin Deployment phase...");
        butTurn = new Button("Turn");
        butTurn.addActionListener(this);
        butTurn.setActionCommand(DEPLOY_TURN);
        butTurn.setEnabled(false);
        butLoad = new Button("Load");
        butLoad.addActionListener(this);
        butLoad.setActionCommand(DEPLOY_LOAD);
        butLoad.setEnabled(false);
        butUnload = new Button("Unload");
        butUnload.addActionListener(this);
        butUnload.setActionCommand(DEPLOY_UNLOAD);
        butUnload.setEnabled(false);
        butNext = new Button("Next Unit");
        butNext.addActionListener(this);
        butNext.setActionCommand(DEPLOY_NEXT);
        butNext.setEnabled(true);
        butDone = new Button("Deploy");
        butDone.addActionListener(this);
        butDone.setEnabled(false);
        panButtons = new Panel();
        panButtons.setLayout(new GridLayout(0, 8));
        panButtons.add(butNext);
        panButtons.add(butTurn);
        panButtons.add(butLoad);
        panButtons.add(butUnload);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.0;
        c.weighty = 0.0;
        addBag(panButtons, gridbag, c);
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        addBag(panStatus, gridbag, c);
        client.bv.addKeyListener(this);
        addKeyListener(this);
    }

    private void addBag(Component comp, GridBagLayout gridbag, GridBagConstraints c) {
        gridbag.setConstraints(comp, c);
        add(comp);
        comp.addKeyListener(this);
    }

    /**
     * Selects an entity for deployment
     */
    public void selectEntity(int en) {
        if (client.game.getEntity(en) == null) {
            System.err.println("DeploymentDisplay: tried to select non-existant entity: " + en);
            return;
        }
        if (ce() != null) {
            ce().setSelected(false);
        }
        this.cen = en;
        ce().setSelected(true);
        setTurnEnabled(true);
        butDone.setEnabled(false);
        setLoadEnabled(true);
        setUnloadEnabled(true);
        client.game.board.select(null);
        client.game.board.cursor(null);
        if (null != ce()) {
            client.mechD.displayEntity(ce());
            client.mechD.showPanel("movement");
            client.getMenuBar().setEntity(ce());
        }
    }

    /**
     * Enables relevant buttons and sets up for your turn.
     */
    private void beginMyTurn() {
        client.setDisplayVisible(true);
        selectEntity(client.getFirstDeployableEntityNum());
        setNextEnabled(true);
        Player p = client.getLocalPlayer();
        if (p.getStartingPos() != 0) {
            client.bv.markDeploymentHexesFor(p);
            client.bv.repaint(100);
        }
    }

    /**
     * Clears out old deployment data and disables relevant buttons.
     */
    private void endMyTurn() {
        disableButtons();
        Entity next = client.game.getNextEntity(client.game.getTurnIndex());
        if (Game.PHASE_DEPLOYMENT == client.game.getPhase() && null != next && null != ce() && next.getOwnerId() != ce().getOwnerId()) {
            client.setDisplayVisible(false);
        }
        cen = Entity.NONE;
        client.game.board.select(null);
        client.game.board.highlight(null);
        client.game.board.cursor(null);
        client.bv.markDeploymentHexesFor(null);
        client.bv.repaint(100);
    }

    /**
     * Disables all buttons in the interface
     */
    private void disableButtons() {
        setTurnEnabled(false);
        setNextEnabled(false);
        butDone.setEnabled(false);
        setLoadEnabled(false);
        setUnloadEnabled(false);
    }

    /**
     * Sends a deployment to the server
     */
    private void deploy() {
        disableButtons();
        Entity en = ce();
        client.deploy(cen, en.getPosition(), en.getFacing(), en.getLoadedUnits());
        en.setDeployed(true);
    }

    /**
     * Returns the current entity.
     */
    private Entity ce() {
        return client.game.getEntity(cen);
    }

    public void die() {
        if (client.isMyTurn()) {
            endMyTurn();
        }
        client.bv.markDeploymentHexesFor(null);
        client.removeGameListener(this);
        client.game.board.removeBoardListener(this);
        this.removeAll();
    }

    public void boardHexMoused(BoardEvent b) {
        if (this.isIgnoringEvents()) {
            return;
        }
        if (b.getType() != BoardEvent.BOARD_HEX_DRAGGED) {
            return;
        }
        if (!client.isMyTurn() || ce() == null || (b.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
            return;
        }
        if ((b.getModifiers() & InputEvent.CTRL_MASK) != 0 || (b.getModifiers() & InputEvent.ALT_MASK) != 0) {
            return;
        }
        boolean shiftheld = (b.getModifiers() & MouseEvent.SHIFT_MASK) != 0;
        Coords moveto = b.getCoords();
        if (ce().getPosition() != null && (shiftheld || turnMode)) {
            ce().setFacing(ce().getPosition().direction(moveto));
            ce().setSecondaryFacing(ce().getFacing());
            client.bv.redrawEntity(ce());
            turnMode = false;
        } else if (!client.game.board.isLegalDeployment(moveto, ce().getOwner()) || ce().isHexProhibited(client.game.board.getHex(moveto))) {
            StringBuffer buff = new StringBuffer();
            buff.append(ce().getShortName()).append(" can not deploy into ").append(moveto.getBoardNum()).append(".");
            AlertDialog dlg = new AlertDialog(client.frame, "Invalid deployment", buff.toString());
            dlg.show();
            return;
        } else if (client.game.getFirstEntity(moveto) != null) {
            return;
        } else {
            ce().setPosition(moveto);
            client.bv.redrawEntity(ce());
            butDone.setEnabled(true);
        }
        client.game.board.select(moveto);
    }

    public void gameTurnChange(GameEvent ev) {
        if (this.isIgnoringEvents()) {
            return;
        }
        if (client.isMyTurn()) {
            beginMyTurn();
            setStatusBarText("It's your turn to deploy.");
        } else {
            endMyTurn();
            setStatusBarText("It's " + ev.getPlayer().getName() + "'s turn to deploy.");
        }
    }

    public void gamePhaseChange(GameEvent ev) {
        client.bv.markDeploymentHexesFor(null);
        if (this.isIgnoringEvents()) {
            return;
        }
        if (client.game.getPhase() == Game.PHASE_DEPLOYMENT) {
            setStatusBarText("Waiting to begin Deployment phase...");
        }
    }

    public void actionPerformed(ActionEvent ev) {
        if (this.isIgnoringEvents()) {
            return;
        }
        if (statusBarActionPerformed(ev, client)) return;
        if (!client.isMyTurn()) {
            return;
        }
        if (ev.getSource() == butDone) {
            deploy();
        } else if (ev.getActionCommand().equals(DEPLOY_NEXT)) {
            ce().setPosition(null);
            client.bv.redrawEntity(ce());
            Enumeration iter = ce().getLoadedUnits().elements();
            while (iter.hasMoreElements()) {
                Entity other = (Entity) iter.nextElement();
                ce().unload(other);
                other.setTransportId(Entity.NONE);
                other.newRound(client.game.getRoundCount());
            }
            selectEntity(client.getNextDeployableEntityNum(cen));
        } else if (ev.getActionCommand().equals(DEPLOY_TURN)) {
            turnMode = true;
        } else if (ev.getActionCommand().equals(DEPLOY_LOAD)) {
            Vector choices = new Vector();
            int otherId = client.getNextDeployableEntityNum(cen);
            Entity other = client.getEntity(otherId);
            while ((otherId != cen) && (otherId != -1)) {
                if (other.getPosition() == null) {
                    if (ce().canLoad(other)) {
                        choices.addElement(other);
                    }
                }
                otherId = client.getNextDeployableEntityNum(otherId);
                other = client.getEntity(otherId);
            }
            if (choices.size() > 0) {
                String[] names = new String[choices.size()];
                StringBuffer question = new StringBuffer();
                question.append(ce().getShortName());
                question.append(" has the following unused space:\n");
                question.append(ce().getUnusedString());
                question.append("\n\nWhich unit do you want to load?");
                for (int loop = 0; loop < names.length; loop++) {
                    names[loop] = ((Entity) choices.elementAt(loop)).getShortName();
                }
                SingleChoiceDialog choiceDialog = new SingleChoiceDialog(client.frame, "Load Unit", question.toString(), names);
                choiceDialog.show();
                if (choiceDialog.getAnswer() == true) {
                    other = (Entity) choices.elementAt(choiceDialog.getChoice());
                    ce().load(other);
                    other.setTransportId(cen);
                    client.mechD.displayEntity(ce());
                }
            } else {
                AlertDialog alert = new AlertDialog(client.frame, "Load Unit", ce().getShortName() + " can not load any of the remaining units.");
                alert.show();
            }
        } else if (ev.getActionCommand().equals(DEPLOY_UNLOAD)) {
            Vector choices = ce().getLoadedUnits();
            if (choices.size() > 0) {
                Entity other = null;
                String[] names = new String[choices.size()];
                StringBuffer question = new StringBuffer();
                question.append(ce().getShortName());
                question.append(" has the following unused space:\n");
                question.append(ce().getUnusedString());
                question.append("\n\nWhich unit do you want to unload?");
                for (int loop = 0; loop < names.length; loop++) {
                    names[loop] = ((Entity) choices.elementAt(loop)).getShortName();
                }
                SingleChoiceDialog choiceDialog = new SingleChoiceDialog(client.frame, "Unload Unit", question.toString(), names);
                choiceDialog.show();
                if (choiceDialog.getAnswer() == true) {
                    other = (Entity) choices.elementAt(choiceDialog.getChoice());
                    if (ce().unload(other)) {
                        other.setTransportId(Entity.NONE);
                        other.newRound(client.game.getRoundCount());
                        client.mechD.displayEntity(ce());
                    } else {
                        System.out.println("Could not unload " + other.getShortName() + " from " + ce().getShortName());
                    }
                }
            } else {
                AlertDialog alert = new AlertDialog(client.frame, "Unload Unit", ce().getShortName() + " is not transporting any units.");
                alert.show();
            }
        }
    }

    public void keyPressed(KeyEvent ev) {
    }

    public void keyReleased(KeyEvent ev) {
    }

    public void keyTyped(KeyEvent ev) {
    }

    public void finishedMovingUnits(BoardViewEvent b) {
    }

    public void selectUnit(BoardViewEvent b) {
        if (this.isIgnoringEvents()) {
            return;
        }
        Entity e = client.game.getEntity(b.getEntityId());
        if (null == e) {
            return;
        }
        if (client.isMyTurn()) {
            if (client.game.getTurn().isValidEntity(e, client.game)) {
                if (ce() != null) {
                    ce().setPosition(null);
                    client.bv.redrawEntity(ce());
                    Enumeration iter = ce().getLoadedUnits().elements();
                    while (iter.hasMoreElements()) {
                        Entity other = (Entity) iter.nextElement();
                        ce().unload(other);
                        other.setTransportId(Entity.NONE);
                        other.newRound(client.game.getRoundCount());
                    }
                }
                selectEntity(e.getId());
                if (null != e.getPosition()) {
                    client.bv.centerOnHex(e.getPosition());
                }
            }
        } else {
            client.setDisplayVisible(true);
            client.mechD.displayEntity(e);
            if (e.isDeployed()) {
                client.bv.centerOnHex(e.getPosition());
            }
        }
    }

    private void setNextEnabled(boolean enabled) {
        butNext.setEnabled(enabled);
        client.getMenuBar().setDeployNextEnabled(enabled);
    }

    private void setTurnEnabled(boolean enabled) {
        butTurn.setEnabled(enabled);
        client.getMenuBar().setDeployTurnEnabled(enabled);
    }

    private void setLoadEnabled(boolean enabled) {
        butLoad.setEnabled(enabled);
        client.getMenuBar().setDeployLoadEnabled(enabled);
    }

    private void setUnloadEnabled(boolean enabled) {
        butUnload.setEnabled(enabled);
        client.getMenuBar().setDeployUnloadEnabled(enabled);
    }

    /**
     * Determine if the listener is currently distracted.
     *
     * @return  <code>true</code> if the listener is ignoring events.
     */
    public boolean isIgnoringEvents() {
        return this.distracted.isIgnoringEvents();
    }

    /**
     * Specify if the listener should be distracted.
     *
     * @param   distract <code>true</code> if the listener should ignore events
     *          <code>false</code> if the listener should pay attention again.
     *          Events that occured while the listener was distracted NOT
     *          going to be processed.
     */
    public void setIgnoringEvents(boolean distracted) {
        this.distracted.setIgnoringEvents(distracted);
    }

    /**
     * Retrieve the "Done" button of this object.
     *
     * @return  the <code>java.awt.Button</code> that activates this
     *          object's "Done" action.
     */
    public Button getDoneButton() {
        return butDone;
    }

    /**
     * Stop just ignoring events and actually stop listening to them.
     */
    public void removeAllListeners() {
        die();
    }
}
