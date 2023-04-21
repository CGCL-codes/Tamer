package jp.hpl.physics.gui.monster;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import src.backend.exception.kajiya.NoSelectedException;
import src.backend.wad.physics.PhysicsData;
import src.backend.wad.physics.PhysicsFile;
import jp.hpl.physics.MainPhysicsFrame;
import jp.hpl.physics.gui.IPanelListener;
import jp.hpl.physics.gui.monster.action.MPActionPanel;
import jp.hpl.physics.gui.monster.appearance.MPAppearancePanel;
import jp.hpl.physics.gui.monster.immunity.MPFlagsPanel;
import jp.hpl.physics.prefs.PhysicsPreferences;

/**
 * 
 * @author koji
 *
 */
public class MonsterPanel extends JPanel implements IPanelListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String TAB_LABEL_SIMPLE = "Easy";

    private static final String TAB_LABEL_APPEARANCE = "Face&Sound";

    private static final String TAB_LABEL_FLAGS = "Flags";

    private static final String TAB_LABEL_ATTACK = "Attack";

    private static final String TAB_LABEL_PHYSICS = "Physics";

    private static final String TAB_LABEL_ACTION = "Action";

    private JTabbedPane tabPane;

    private MainPhysicsFrame parent;

    /** easy setting */
    public MPSimplePanel simplePanel;

    /** physics panel */
    public MPPhysicsPanel physicsPanel;

    /** face/sounds */
    public MPAppearancePanel appearancePanel;

    public MPActionPanel actionPanel;

    /** action */
    public MPFlagsPanel flagPanel;

    public MPAttackPanel attackPanel;

    /**
	 * constructor
	 * @param p
	 */
    public MonsterPanel(MainPhysicsFrame p) {
        super();
        parent = p;
        PhysicsPreferences pref = PhysicsPreferences.getInstance();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        tabPane = getTabPane();
        this.add(tabPane);
    }

    /**
	 * 
	 * @return
	 */
    private JTabbedPane getTabPane() {
        JTabbedPane pane = new JTabbedPane();
        simplePanel = new MPSimplePanel(parent, this);
        physicsPanel = new MPPhysicsPanel(parent, this);
        appearancePanel = new MPAppearancePanel(parent, this);
        actionPanel = new MPActionPanel(parent, this);
        flagPanel = new MPFlagsPanel(parent, this);
        attackPanel = new MPAttackPanel(parent, this);
        pane.addTab(TAB_LABEL_SIMPLE, simplePanel);
        pane.addTab(TAB_LABEL_PHYSICS, physicsPanel);
        pane.addTab(TAB_LABEL_APPEARANCE, appearancePanel);
        pane.addTab(TAB_LABEL_ACTION, actionPanel);
        pane.addTab(TAB_LABEL_FLAGS, flagPanel);
        pane.addTab(TAB_LABEL_ATTACK, attackPanel);
        return pane;
    }

    public void update(PhysicsData file) {
        update(file, null);
    }

    public void update(PhysicsData file, JPanel exceptPanel) {
        if (exceptPanel != simplePanel) simplePanel.update(file);
        if (exceptPanel != appearancePanel) appearancePanel.update(file);
        if (exceptPanel != actionPanel) actionPanel.update(file);
        if (exceptPanel != flagPanel) flagPanel.update(file);
        if (exceptPanel != attackPanel) attackPanel.update(file);
        if (exceptPanel != physicsPanel) physicsPanel.update(file);
    }

    public int getType() throws NoSelectedException {
        return parent.getSelectedType();
    }
}
