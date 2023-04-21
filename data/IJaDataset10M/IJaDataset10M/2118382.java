package gpl.scotlandyard.ui;

import gpl.scotlandyard.Main;
import gpl.scotlandyard.beans.adapters.PlayerMovesTM;
import gpl.scotlandyard.beans.adapters.SecuredFugitive;
import gpl.scotlandyard.beans.adapters.TicketsBoardTM;
import gpl.scotlandyard.beans.basics.Board;
import gpl.scotlandyard.beans.basics.Link;
import gpl.scotlandyard.beans.basics.Node;
import gpl.scotlandyard.beans.basics.Player;
import gpl.scotlandyard.exceptions.IllegalMoveException;
import gpl.scotlandyard.services.basics.BoardManager;
import gpl.scotlandyard.ui.dialogs.AboutDialog;
import gpl.scotlandyard.ui.dialogs.SpecialDialog;
import gpl.scotlandyard.ui.dialogs.TicketsBoardDialog;
import gpl.scotlandyard.ui.dialogs.TracerDialog;
import gpl.scotlandyard.ui.tools.JDialogs;
import gpl.scotlandyard.ui.tools.LayeredLayout;
import gpl.scotlandyard.utils.Config;
import gpl.scotlandyard.utils.Constants;
import gpl.scotlandyard.utils.I18n;
import gpl.scotlandyard.utils.ImageProvider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;

/** Main frame, contains the board.
 * 
 * @author Norbert Martin */
public class GameFrame {

    private static final Logger LOG = Logger.getLogger(GameFrame.class);

    private final JFrame jframe = new JFrame();

    private final PlayerMovesTM fugitiveTM;

    private final TracerDialog ftracer;

    private final TicketsBoardDialog detectivesView;

    private final JLayeredPane layeredPane = new JLayeredPane();

    private final HashMap<Player, JLabel> playersIcon = new HashMap<Player, JLabel>();

    private final HashMap<JLabel, List<Link>> offeredMoves = new HashMap<JLabel, List<Link>>();

    private final HashMap<Node, JLabel> nodesLabels = new HashMap<Node, JLabel>();

    private final List<JLabel> moves = new ArrayList<JLabel>();

    private Action quitAction;

    private SpecialDialog specialDialog;

    private boolean isInit;

    private final BoardManager boardManager;

    /** Creates a game frame.
	 * 
	 * @param oldGameBoard */
    public GameFrame(BoardManager boardManager) {
        boardManager.getClass();
        this.boardManager = boardManager;
        fugitiveTM = new PlayerMovesTM(boardManager.getBoard().getFugitive());
        ftracer = new TracerDialog(fugitiveTM);
        detectivesView = new TicketsBoardDialog(new TicketsBoardTM(boardManager.getBoard()));
        boardManager.getBoard().addListener(new Board.Listener() {

            @Override
            public void currentPlayerChanged(Player player) {
                if (specialDialog != null) {
                    specialDialog.setVisible(false);
                    specialDialog = null;
                }
                if (player.isFugitive()) {
                    fugitivePlay(player);
                } else {
                    detectivePlay(player);
                }
                if (player.hasSpecialTickets()) {
                    specialDialog = new SpecialDialog(player);
                    specialDialog.setVisible(true);
                }
            }

            @Override
            public void gameFinished(int code) {
                endGame(code);
            }
        });
        for (Player player : boardManager.getBoard().getPlayersQueue()) {
            player.addListener(new Player.ListenerImpl() {

                @Override
                public void playerMoved(Player player, Link link) {
                    refreshPlayer(player);
                }
            });
        }
    }

    /** Sets visible.
	 * 
	 * @param b */
    public void setVisible(boolean b) {
        if (b && !isInit) {
            init();
        }
        jframe.setVisible(b);
    }

    /** Initializes the content. */
    private void init() {
        jframe.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                LOG.info("GameFrame opened");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                quitAction.actionPerformed(new ActionEvent(jframe, 0, ""));
            }
        });
        jframe.setTitle(String.format(I18n.get("GAME_TITLE"), Main.VERSION));
        jframe.setIconImage(ImageProvider.get(Config.get("LOGO")).getImage());
        jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jframe.setLayout(new BorderLayout());
        initJMenuBar();
        buildcontent();
        jframe.pack();
        jframe.setSize(jframe.getSize());
        jframe.setLocationRelativeTo(null);
        detectivesView.setVisible(true);
        ftracer.setVisible(true);
        isInit = true;
    }

    /**
    * 
    */
    private void initJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu(I18n.get("FILE"));
        menuBar.add(file);
        quitAction = new AbstractAction(I18n.get("QUIT"), ImageProvider.get(Config.get("QUIT_ICO"))) {

            private static final long serialVersionUID = -926393672976888316L;

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jframe, I18n.get("CONFIRM_QUIT"), I18n.get("SY"), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    jframe.dispose();
                    LOG.info("GameFrame closed");
                    System.exit(Constants.STD_EXIT);
                }
            }
        };
        quitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
        file.add(quitAction);
        JMenu view = new JMenu(I18n.get("VIEW"));
        view.setEnabled(false);
        menuBar.add(view);
        JMenu help = new JMenu(I18n.get("HELP"));
        menuBar.add(help);
        Action aboutAction = new AbstractAction(I18n.get("ABOUT_SY")) {

            private static final long serialVersionUID = -926393672976888316L;

            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.show(jframe);
            }
        };
        quitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
        help.add(aboutAction);
        jframe.setJMenuBar(menuBar);
    }

    /** Builds content. */
    private void buildcontent() {
        layeredPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                List<Link> list = offeredMoves.get(layeredPane.getComponentAt(e.getPoint()));
                if (list == null) {
                    return;
                }
                Link l = list.get(0);
                if (list.size() > 1) {
                    l = (Link) JDialogs.showInputChoices(null, I18n.get("CHOOSE_TICKET"), list.toArray(), list.get(0));
                }
                if (l != null) {
                    try {
                        boardManager.moveCurrentPlayer(l);
                    } catch (IllegalMoveException e1) {
                        String msg = "moveCurrentPlayer error shouldn't happen : " + e1.getMessage();
                        LOG.error(msg);
                        JDialogs.showError(jframe, msg);
                    }
                }
            }
        });
        ImageIcon map = ImageProvider.get(Config.get("MAP_IMG"));
        layeredPane.setLayout(new LayeredLayout());
        JLabel mapLabel = new JLabel(map);
        layeredPane.add(mapLabel, nextLayer());
        jframe.getContentPane().add(layeredPane, BorderLayout.NORTH);
        JLabel icon = new JLabel(ImageProvider.get(Config.get("F_PLAYER_ICO")));
        playersIcon.put(boardManager.getBoard().getFugitive(), icon);
        layeredPane.add(icon, nextLayer());
        updateIconLocation(boardManager.getBoard().getFugitive());
        icon.setVisible(false);
        for (Player d : boardManager.getBoard().getDetectives()) {
            icon = new JLabel(ImageProvider.get(Config.get("D_PLAYER_ICO")));
            playersIcon.put(d, icon);
            layeredPane.add(icon, nextLayer());
            updateIconLocation(d);
        }
    }

    /** Gets next layer for layeredPane.
	 * 
	 * @return Integer */
    private Integer nextLayer() {
        return Integer.valueOf(layeredPane.getComponentCount());
    }

    /** Updates player location visualisation.
	 * 
	 * @param player */
    private void updateIconLocation(Player player) {
        final JLabel icon = playersIcon.get(player);
        gpl.scotlandyard.beans.utils.Point p = player.getNode().getLocation();
        showAt(icon, new Point(p.getX(), p.getY()));
    }

    /** Shows icon at given location.
	 * 
	 * @param icon
	 * @param location */
    private void showAt(JLabel icon, Point location) {
        Dimension d = icon.getPreferredSize();
        location.translate(-(d.width / 2), -d.height);
        icon.setLocation(location);
        icon.revalidate();
    }

    /** Shows input dialog and returns fugitive password.
	 * 
	 * @return entered password or null */
    public String getFugitivePwd() {
        return JOptionPane.showInputDialog(I18n.get("F_ENTER_PWD"));
    }

    /** Shows this information message.
	 * 
	 * @param msg */
    public void showMessage(String msg) {
        JDialogs.showInfo(jframe, msg);
    }

    /** Removes available moves, update player location and set player name.
	 * 
	 * @param player */
    public void refreshPlayer(Player player) {
        updateIconLocation(player);
        for (JLabel old : moves) {
            layeredPane.remove(old);
        }
        moves.clear();
        layeredPane.revalidate();
        layeredPane.repaint();
        LOG.info("Del moves and refresh view of : " + player);
    }

    /** Hides fugitive icon and shows avaible moves for player.
	 * 
	 * @param player */
    public void showPlayerMoves(Player player) {
        playersIcon.get(boardManager.getBoard().getFugitive()).setVisible(player.isFugitive());
        jframe.setTitle(String.format(I18n.get("TITLE_N_PLAYER"), player.getName(), Main.VERSION));
        offeredMoves.clear();
        for (Link link : boardManager.getPlayerManager().getMoves(player)) {
            JLabel m = nodesLabels.get(link.getToNode());
            if (m == null) {
                m = new JLabel(ImageProvider.get(Config.get("ARROW_IMG")));
                nodesLabels.put(link.getToNode(), m);
            }
            List<Link> list = offeredMoves.get(m);
            if (list == null) {
                list = new ArrayList<Link>();
                offeredMoves.put(m, list);
            }
            list.add(link);
            moves.add(m);
            layeredPane.add(m, nextLayer());
            gpl.scotlandyard.beans.utils.Point p = link.getToNode().getLocation();
            showAt(m, new Point(p.getX(), p.getY()));
        }
        layeredPane.revalidate();
        layeredPane.repaint();
        LOG.info("Show moves of : " + player);
    }

    /** Show fugitive icon. */
    public void showFugitive() {
        playersIcon.get(boardManager.getBoard().getFugitive()).setVisible(true);
    }

    /** Tests if fugitive is hold or win, shows message and then stop game if it is. If fugitive must play, ask for
	 * password and the show view.
	 * 
	 * @param player */
    private void fugitivePlay(Player player) {
        boolean isLogged = false;
        while (!isLogged) {
            LOG.debug("try fugitive login");
            String pwd = getFugitivePwd();
            SecuredFugitive fugitive = (SecuredFugitive) player;
            isLogged = fugitive.isValidPassword(pwd);
        }
        refreshPlayer(player);
        showPlayerMoves(player);
    }

    private void detectivePlay(Player player) {
        refreshPlayer(player);
        showPlayerMoves(player);
    }

    /** Stops game, shows messages, futigitive location and full tracers for all players.
	 * 
	 * @param code */
    private void endGame(int code) {
        if (specialDialog != null) {
            specialDialog.setVisible(false);
            specialDialog = null;
        }
        showFugitive();
        String msg = "";
        switch(code) {
            case Board.DETECTIVES_WIN:
                msg = I18n.get("D_WIN");
                break;
            case Board.FUGITIVE_HOLD:
                msg = I18n.get("F_HOLD");
                break;
            case Board.FUGITIVE_WIN:
                msg = I18n.get("F_WIN");
                break;
            default:
                LOG.error("unknown finish code : " + code);
                msg = "unknown finish code : " + code;
        }
        showMessage(msg);
        fugitiveTM.showAll(true);
        for (Player detective : boardManager.getBoard().getDetectives()) {
            new TracerDialog(new PlayerMovesTM(detective)).setVisible(true);
        }
        LOG.info("show end game message : " + msg);
    }
}
