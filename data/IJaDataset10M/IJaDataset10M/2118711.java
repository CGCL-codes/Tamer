package sudoku.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sudoku.commands.*;
import sudoku.core.Cell;
import sudoku.core.PlayedCell;
import jguic.Command;
import jguic.Mediator;
import jguic.MediatorExtension;

/**
 * Impl�mente la zone de jeu
 * 
 * @author Romain HUET
 * @author Nicolas RAYNAUD
 */
public class GUIGameZone extends MediatorExtension {

    /** Donn�es utilisateur */
    private UserData userdata;

    /** Gestionnaire de surlignage */
    private HighlightManager highlight;

    /** Horloge */
    private Timer clock;

    /** Chiffre du dashboard courant */
    private int figure;

    /** Indique les couleurs utilis�es dans le Highlight */
    private Color[] highlightColors;

    /** Zone de contr�le */
    private GUIDashBoard dashboard;

    /** Grille de jeu */
    private GUIGrid grid;

    /** Zone de statut */
    private GUIStatus status;

    /** Avertissements */
    private GUIMessage message;

    /** Multiples couches */
    private JLayeredPane swing_layers;

    /** Zone de jeu */
    private JPanel swing_gameZone;

    /** Zone de message */
    private JPanel swing_messageZone;

    /** Champ de message */
    private JComponent swing_message;

    /** Transparence du fond */
    private static final int ALPHA = 40;

    /** Image de fond */
    private static Image swing_var_backgroundImage = new ImageIcon("background/abstract1.jpg").getImage();

    /** Couleur du fond par d�faut */
    private static Color swing_var_backgroundDefaultColor = new Color(0, 153, 204);

    /**
	 * Construit la zone de jeu
	 * 
	 * @param parent
	 *            M�diateur parent
	 */
    public GUIGameZone(Mediator parent, UserData udata) {
        super(parent);
        grid = new GUIGridP(this);
        status = new GUIStatus(this);
        dashboard = new GUIDashBoard(this);
        message = new GUIMessage(this);
        swing_layers = new JLayeredPane();
        swing_layers.setPreferredSize(new Dimension(600, 600));
        swing_gameZone = new JPanel() {

            public void paintComponent(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (swing_var_backgroundImage.getWidth(null) > 0) g.drawImage(swing_var_backgroundImage, 0, 0, this); else {
                    g.setColor(swing_var_backgroundDefaultColor);
                    g.fillRect(0, 0, 600, 600);
                }
                g.setColor(new Color(0, 0, 0, ALPHA));
                Dimension d = this.getPreferredSize();
                int x = Math.min(status.getJComponent().getX(), Math.min(dashboard.getJComponent().getX(), grid.getJComponent().getX()));
                g.fillRoundRect(x - 10, this.getY() + 10, d.width + 20, d.height, 20, 20);
            }
        };
        BoxLayout layout = new BoxLayout(swing_gameZone, BoxLayout.PAGE_AXIS);
        swing_gameZone.setLayout(layout);
        JComponent swing_dashboard = dashboard.getJComponent();
        swing_dashboard.setMaximumSize(swing_dashboard.getPreferredSize());
        JComponent swing_status = status.getJComponent();
        swing_status.setMaximumSize(swing_status.getPreferredSize());
        JComponent swing_grid = grid.getJComponent();
        swing_grid.setMaximumSize(swing_grid.getPreferredSize());
        swing_gameZone.add(Box.createRigidArea(new Dimension(0, 30)));
        swing_gameZone.add(swing_dashboard);
        swing_gameZone.add(Box.createRigidArea(new Dimension(0, 20)));
        swing_gameZone.add(swing_grid);
        swing_gameZone.add(Box.createRigidArea(new Dimension(0, 10)));
        swing_gameZone.add(swing_status);
        swing_gameZone.addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent event) {
                int way = event.getWheelRotation();
                if (way > 0) {
                    if ((figure == 9) || (figure == 0)) figure = 1; else figure++;
                } else {
                    if ((figure == 1) || (figure == 0)) figure = 9; else figure--;
                }
                handle(new SetActiveFigureCommand(figure));
            }
        });
        swing_layers.add(swing_gameZone, JLayeredPane.DEFAULT_LAYER);
        swing_gameZone.setBounds(0, 0, 600, 600);
        swing_message = message.getJComponent();
        swing_messageZone = new JPanel();
        swing_messageZone.setOpaque(false);
        swing_messageZone.setLayout(null);
        swing_messageZone.add(swing_message);
        swing_layers.add(swing_messageZone, JLayeredPane.POPUP_LAYER);
        swing_messageZone.setBounds(0, 0, 600, 600);
        Dimension dm = swing_message.getPreferredSize();
        swing_message.setBounds((int) ((600 - dm.getWidth()) / 2), 140, (int) dm.getWidth(), (int) dm.getHeight());
        userdata = udata;
        highlight = new HighlightManager();
        clock = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                userdata.getClock().addSecond();
                sendCommand(new TicTacCommand(userdata.getClock()));
            }
        });
        clock.start();
        figure = 0;
        highlightColors = new Color[10];
        for (short i = 0; i < highlightColors.length; i++) highlightColors[i] = null;
        handle(new SetActiveFigureCommand(1));
    }

    /**
	 * Retourne le composant Swing
	 * 
	 * @return Composant Swing
	 */
    public JComponent getJComponent() {
        return swing_layers;
    }

    /** {@inheritDoc} */
    public void handle(Command command) {
        boolean blockCommand = false;
        if (command instanceof SetValueCommand) {
            SetValueCommand c = (SetValueCommand) command;
            Cell cell = userdata.getGrid().getCell(c.getCellX() - 1, c.getCellY() - 1);
            if (cell instanceof PlayedCell) {
                c.setOldValue(cell.getValue());
                if (cell.getValue() == figure) c.setNewValue(0); else c.setNewValue(figure);
            } else blockCommand = true;
        } else if (command instanceof SetPossibilityCommand) {
            SetPossibilityCommand c = (SetPossibilityCommand) command;
            int x = c.getCellX() - 1;
            int y = c.getCellY() - 1;
            if (figure != 0) {
                Cell cell = userdata.getGrid().getCell(x, y);
                if (cell instanceof PlayedCell) {
                    PlayedCell pcell = (PlayedCell) cell;
                    c.setPossibility(figure);
                    c.setActive(!pcell.getPossibility(figure));
                    c.setOldValue(pcell.getValue());
                    c.setNewValue(0);
                } else blockCommand = true;
            } else {
                ClearPossibilitiesCommand cc = new ClearPossibilitiesCommand(c.getCellX(), c.getCellY());
                Cell cell = userdata.getGrid().getCell(x, y);
                if (cell instanceof PlayedCell) {
                    PlayedCell pcell = (PlayedCell) cell;
                    cc.setPossibilities(pcell.getPossibilities());
                    cc.setOldValue(pcell.getValue());
                    cc.setNewValue(0);
                    handle(cc);
                }
                blockCommand = true;
            }
        } else if (command instanceof SetHighlightCommand) {
            SetHighlightCommand c = (SetHighlightCommand) command;
            Color color = highlight.lockColor();
            highlightColors[c.getFigure()] = color;
            c.setColor(color);
        } else if (command instanceof UnsetHighlightCommand) {
            UnsetHighlightCommand c = (UnsetHighlightCommand) command;
            highlight.unlockColor(highlightColors[c.getFigure()]);
        }
        if (!blockCommand) super.handle(command);
    }

    /** {@inheritDoc} */
    public void receiveCommand(Mediator mediator, Command command) {
        if (command instanceof SetUserDataCommand) {
            SetUserDataCommand c = (SetUserDataCommand) command;
            for (int i = 1; i < 10; i++) {
                highlight.unlockColor(highlightColors[i]);
                handle(new UnsetHighlightCommand(i));
            }
            userdata = c.getUserData();
            JComponent swing_grid = grid.getJComponent();
            if (userdata.getMode() == UserData.PLAYER_MODE) {
                clock.stop();
                handle(new TicTacCommand(c.getUserData().getClock()));
                if (c.getUserData().isPaused()) {
                    clock.stop();
                    swing_grid.setVisible(false);
                } else {
                    clock.start();
                    swing_grid.setVisible(true);
                }
            } else {
                clock.stop();
                swing_grid.setVisible(true);
                c.getUserData().getClock().reset();
                handle(new TicTacCommand(c.getUserData().getClock()));
            }
        } else if (command instanceof SetValueCommand) {
            SetValueCommand c = (SetValueCommand) command;
            Cell cell = userdata.getGrid().getCell(c.getCellX() - 1, c.getCellY() - 1);
            cell.setValue(c.getNewValue());
        } else if (command instanceof SetPossibilityCommand) {
            SetPossibilityCommand c = (SetPossibilityCommand) command;
            int x = c.getCellX() - 1;
            int y = c.getCellY() - 1;
            Cell cell = userdata.getGrid().getCell(x, y);
            if (cell instanceof PlayedCell) {
                PlayedCell pcell = (PlayedCell) cell;
                pcell.setPossibility(c.getPossibility(), c.isActive());
                userdata.getGrid().getCell(x, y).setValue(c.getNewValue());
            }
        } else if (command instanceof ClearPossibilitiesCommand) {
            ClearPossibilitiesCommand c = (ClearPossibilitiesCommand) command;
            int x = c.getCellX() - 1;
            int y = c.getCellY() - 1;
            Cell cell = userdata.getGrid().getCell(x, y);
            if (cell instanceof PlayedCell) {
                PlayedCell pcell = (PlayedCell) cell;
                boolean[] possibilities = c.getPossibilities();
                for (int i = 0; i < 9; i++) pcell.setPossibility(i + 1, possibilities[i]);
                userdata.getGrid().getCell(x, y).setValue(c.getNewValue());
            }
        } else if (command instanceof SetActiveFigureCommand) {
            figure = ((SetActiveFigureCommand) command).getFigure();
        } else if (command instanceof PlayPauseCommand) {
            JComponent swing_grid = grid.getJComponent();
            if (((PlayPauseCommand) command).isPaused()) {
                clock.stop();
                swing_grid.setVisible(false);
            } else {
                clock.start();
                swing_grid.setVisible(true);
            }
        } else if (command instanceof SetHighlightCommand) {
            SetHighlightCommand c = (SetHighlightCommand) command;
            highlightColors[c.getFigure()] = c.getColor();
        } else if (command instanceof UnsetHighlightCommand) {
            UnsetHighlightCommand c = (UnsetHighlightCommand) command;
            highlightColors[c.getFigure()] = null;
        } else if (command instanceof SetBackgroundImageCommand) {
            SetBackgroundImageCommand c = (SetBackgroundImageCommand) command;
            if (c.getImageName() != null) swing_var_backgroundImage = (new ImageIcon(c.getImageName()).getImage());
        } else if (command instanceof CheckSolutionCommand) {
            CheckSolutionCommand c = (CheckSolutionCommand) command;
            if (c.isGridFull() && c.isValid()) {
                clock.stop();
                for (short i = 0; i < highlightColors.length; i++) {
                    if (highlightColors[i] != null) handle(new UnsetHighlightCommand(i));
                }
            }
        }
        swing_layers.repaint();
        if (!(command instanceof RepaintCommand)) super.receiveCommand(mediator, command);
        if (command instanceof SetValueCommand) {
            SetValueCommand c = (SetValueCommand) command;
            Color color = highlightColors[c.getNewValue()];
            if (color != null) {
                SetHighlightCommand cm = new SetHighlightCommand(c.getNewValue());
                cm.setColor(color);
                sendCommand(cm);
            }
        } else if (command instanceof SetPossibilityCommand) {
            SetPossibilityCommand c = (SetPossibilityCommand) command;
            Color color = highlightColors[c.getNewValue()];
            if (color != null) {
                SetHighlightCommand cm = new SetHighlightCommand(c.getNewValue());
                cm.setColor(color);
                sendCommand(cm);
            }
        } else if (command instanceof ClearPossibilitiesCommand) {
            ClearPossibilitiesCommand c = (ClearPossibilitiesCommand) command;
            Color color = highlightColors[c.getNewValue()];
            if (color != null) {
                SetHighlightCommand cm = new SetHighlightCommand(c.getNewValue());
                cm.setColor(color);
                sendCommand(cm);
            }
        }
    }
}
