package geisler.projekt.editor.view;

import geisler.projekt.editor.controller.ActionController;
import geisler.projekt.editor.controller.EditorKeyController;
import geisler.projekt.editor.controller.MenuBarAndOpenLoadController;
import geisler.projekt.editor.controller.MouseController;
import geisler.projekt.game.constants.EnumObjectType;
import geisler.projekt.game.model.Map2D;
import geisler.projekt.game.model.NonPlayingCharacter;
import geisler.projekt.game.model.Ressources;
import geisler.projekt.game.model.TileAnimation2D;
import geisler.projekt.game.view.GamePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EditorFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Log LOG = LogFactory.getLog(EditorFrame.class);

    private Ressources ress;

    private GamePanel gamePanel;

    private JPanel tempTileSetPane;

    private JComboBox tileSetAuswahl;

    private TileSetLabel tileSetPanel;

    private JScrollPane scrollP;

    private JScrollPane scrollPGame;

    private JLabel lblInfo;

    private MouseController mouseContr;

    private ActionController actionController;

    private EditorKeyController editorKeyController;

    private MenuBarAndOpenLoadController menuController;

    private EditorButtonPanel buttonPanel;

    private TileAnimation2D aktiveAnimation;

    private NonPlayingCharacter aktiveNPC;

    private boolean edited;

    private boolean attrEditing;

    private boolean insertMode = true;

    public EditorFrame(MenuBarAndOpenLoadController menuContr, String projectName) {
        super("Map-Design-Dialog");
        ress = new Ressources(projectName, 1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagLayout lm = new GridBagLayout();
        this.setLayout(lm);
        this.setLocationByPlatform(true);
        actionController = new ActionController(this);
        mouseContr = new MouseController(this);
        if (menuContr == null) {
            menuController = new MenuBarAndOpenLoadController(this);
        }
        editorKeyController = new EditorKeyController(this);
        EditorMenuBar mBar = new EditorMenuBar(this);
        this.setJMenuBar(mBar);
        Map2D map = ress.getMaps().get(ress.getActiveMap());
        gamePanel = new GamePanel((int) map.getWidth(), (int) map.getHeight(), ress, true);
        gamePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        gamePanel.addMouseListener(mouseContr);
        gamePanel.addKeyListener(editorKeyController);
        scrollPGame = new JScrollPane(gamePanel);
        scrollPGame.setPreferredSize(new Dimension(650, 490));
        scrollPGame.setMaximumSize(new Dimension(650, 490));
        scrollPGame.getVerticalScrollBar().setUnitIncrement(10);
        scrollPGame.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPGame.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPGame.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPGame.setBorder(new LineBorder(Color.darkGray, 5));
        tileSetAuswahl = new JComboBox(new String[] { "TileSet 1", "TileSet 2", "TileSet 3", "TileSet 4", "TileSet 5" });
        tileSetAuswahl.addActionListener(getActionController());
        tileSetPanel = new TileSetLabel(1, ress);
        tileSetPanel.addMouseListener(mouseContr);
        tileSetPanel.addMouseMotionListener(mouseContr);
        tileSetPanel.setDoubleBuffered(true);
        LOG.debug("TileSet Gr��e:" + tileSetPanel.getPreferredSize());
        buttonPanel = new EditorButtonPanel(this);
        buttonPanel.setPreferredSize(new Dimension(650, 220));
        lblInfo = new JLabel("#####   Free Info Place   #####");
        lblInfo.setFont(new Font("INFO", Font.BOLD, 12));
        lblInfo.setForeground(Color.BLUE);
        scrollP = new JScrollPane(tileSetPanel);
        scrollP.setPreferredSize(new Dimension(350, 750));
        scrollP.getVerticalScrollBar().setUnitIncrement(10);
        scrollP.getHorizontalScrollBar().setUnitIncrement(10);
        scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tempTileSetPane = new JPanel();
        tempTileSetPane.setLayout(new BorderLayout());
        tempTileSetPane.add(tileSetAuswahl, BorderLayout.NORTH);
        tempTileSetPane.add(scrollP, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(1110, 900));
        this.setMinimumSize(new Dimension(1110, 900));
        this.add(tempTileSetPane, new GridBagConstraints(0, 0, 1, 3, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 20), 0, 0));
        this.add(scrollPGame, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(buttonPanel, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(20, 10, 10, 10), 0, 0));
        this.add(lblInfo, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(20, 10, 10, 10), 0, 0));
        tileSetPanel.repaint();
        gamePanel.repaint();
        this.pack();
    }

    public Point getNewRect(Point rec) {
        int x = 0, y = 0;
        x = (int) rec.getX();
        y = (int) rec.getY();
        x = (x / 32) * 32;
        y = (y / 32) * 32;
        LOG.debug("X = " + x + " Y = " + y);
        rec.setLocation(x, y);
        return rec;
    }

    public TileSetLabel getTileSetPanel() {
        return tileSetPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public ActionController getActionController() {
        return actionController;
    }

    /**
	 * @return the scrollP
	 */
    public JScrollPane getScrollP() {
        return scrollP;
    }

    /**
	 * @return the ress
	 */
    public Ressources getRess() {
        return ress;
    }

    /**
	 * @return the buttonPanel
	 */
    public EditorButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
	 * @param buttonPanel the buttonPanel to set
	 */
    public void setButtonPanel(EditorButtonPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    /**
	 * @return the menuController
	 */
    public MenuBarAndOpenLoadController getMenuController() {
        return menuController;
    }

    /**
	 * @return the edited
	 */
    public boolean isEdited() {
        return edited;
    }

    /**
	 * @param edited the edited to set
	 */
    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setAttrEditing(boolean attrEditing) {
        this.attrEditing = attrEditing;
    }

    public boolean isAttrEditing() {
        return attrEditing;
    }

    /**
	 * @return the lblInfo
	 */
    public JLabel getLblInfo() {
        return lblInfo;
    }

    /**
	 * @return the tempTileSetPane
	 */
    public JPanel getTempTileSetPane() {
        return tempTileSetPane;
    }

    /**
	 * @param aktiveAnimation the aktiveAnimation to set
	 */
    public void setAktiveAnimation(TileAnimation2D aktiveAnimation) {
        this.aktiveAnimation = aktiveAnimation;
    }

    /**
	 * @return the aktiveAnimation
	 */
    public TileAnimation2D getAktiveAnimation() {
        return aktiveAnimation;
    }

    /**
	 * @param scrollPGame the scrollPGame to set
	 */
    public void setScrollPGame(JScrollPane scrollPGame) {
        this.scrollPGame = scrollPGame;
    }

    /**
	 * @return the scrollPGame
	 */
    public JScrollPane getScrollPGame() {
        return scrollPGame;
    }

    /**
	 * @param aktiveNPC the aktiveNPC to set
	 */
    public void setAktiveNPC(NonPlayingCharacter aktiveNPC) {
        this.aktiveNPC = aktiveNPC;
    }

    /**
	 * @return the aktiveNPC
	 */
    public NonPlayingCharacter getAktiveNPC() {
        return aktiveNPC;
    }

    /**
	 * @param insertMode the insertMode to set
	 */
    public void setInsertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }

    /**
	 * @return the insertMode
	 */
    public boolean isInsertMode() {
        return insertMode;
    }

    /**
	 * @return the mouseContr
	 */
    public MouseController getMouseContr() {
        return mouseContr;
    }
}
