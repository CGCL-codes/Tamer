package chess.ui;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import chess.data.Piece;
import chess.data.Ply;
import chess.data.Square;
import chess.logic.CheckMate;
import chess.logic.GameState;

/**
 * Main class of the chess program which is started as an applet. Initializes
 * all needed classes.
 * <p>
 * Contains some user interface componensts for controlling the playing and
 * showing information to the user.
 * 
 * @author Juho Karppinen
 * @version $Id: CheckGUI.java 1073 2006-06-07 12:18:06Z jkarppin $
 */
public class CheckGUI extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static CheckGUI instance;

    private Board board;

    private CheckMate game;

    private JTextArea information;

    private JTextField turn;

    private JButton go;

    private JButton sim;

    /**
     * Create the
     */
    public CheckGUI() {
        this(null);
    }

    /**
     * Init new board
     * @param state existing state or null for new game
     */
    public CheckGUI(GameState state) {
        super();
        CheckGUI.instance = this;
        if (state == null) this.game = new CheckMate(); else this.game = new CheckMate(state);
        Square.initialize();
    }

    public String getAppletInfo() {
        return "Check Player GUI";
    }

    /**
     * Gets instance of the CheckMate core class.
     * 
     * @return instance of the CheckMate
     */
    public static CheckGUI instance() {
        return instance;
    }

    /**
     * Init user interface
     */
    public void init() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.board = new Board();
        c.gridx = c.gridy = 1;
        c.gridwidth = 2;
        add(this.board, c);
        information = new JTextArea("Hello");
        information.setRows(5);
        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridy++;
        c.weightx = c.weighty = 1.f;
        c.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(information), c);
        c.gridx++;
        c.gridheight = 1;
        c.weightx = c.weighty = 0.f;
        c.fill = GridBagConstraints.NONE;
        go = new JButton("Go");
        go.addActionListener(this);
        add(go, c);
        c.gridy++;
        sim = new JButton("Simulate");
        sim.addActionListener(this);
        add(sim, c);
        c.gridy++;
        turn = new JTextField("WHITE");
        add(turn, c);
    }

    public void start() {
        System.out.println("starting... ");
    }

    public void stop() {
        System.out.println("stopping... ");
    }

    public void destroy() {
        System.out.println("preparing for unloading...");
        game.endGame();
    }

    private void setInformation(String message) {
        information.append("\n" + message);
        Rectangle rec = information.getVisibleRect();
        FontMetrics metrics = information.getFontMetrics(information.getFont());
        rec.y += metrics.getHeight() * 3;
        information.scrollRectToVisible(rec);
    }

    public CheckMate getGame() {
        return game;
    }

    /**
     * Move the piece to new location
     * 
     * @param move
     *            repaing moved pieces
     */
    public void repaintPly(Ply move) {
        Rectangle rec = Board.getDimension(move.getEndPosition());
        board.repaint(rec.x, rec.y, rec.width, rec.height);
        rec = Board.getDimension(move.getStartPosition());
        board.repaint(rec.x, rec.y, rec.width, rec.height);
    }

    public void actionPerformed(ActionEvent ae) {
        setInformation("thinking...");
        Ply bestPly = null;
        if (ae.getSource().equals(go)) bestPly = game.computerPly(false); else if (ae.getSource().equals(sim)) bestPly = game.computerPly(true);
        setInformation("Computer moved " + Square.toString(bestPly.getStartPosition()) + Square.toString(bestPly.getEndPosition()) + " = " + bestPly.getScore() + " (" + bestPly.getCalculations() + " moves)");
        refresh();
    }

    private void refresh() {
        int player = game.getGameState().getTurn();
        if (player == Piece.WHITE) turn.setText("WHITE"); else turn.setText("BLACK");
    }

    public static void playerPly(Ply ply) {
        try {
            Ply comp = instance.getGame().playerPly(ply.getStartPosition(), ply.getEndPosition());
            instance.repaintPly(ply);
            String info = "Player " + Square.toString(ply.getStartPosition()) + Square.toString(ply.getEndPosition());
            if (ply.getOldPiece() != null) info += " " + ply.getOldPiece().toString();
            instance.setInformation(info);
            if (comp != null) {
                instance.repaintPly(comp);
                instance.setInformation("Computer: " + Square.toString(comp.getStartPosition()) + Square.toString(comp.getEndPosition()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            instance().setInformation(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        CheckGUI mate = new CheckGUI();
        mate.init();
        JFrame frame = new JFrame();
        frame.getContentPane().add(mate);
        frame.pack();
        frame.setVisible(true);
    }
}
