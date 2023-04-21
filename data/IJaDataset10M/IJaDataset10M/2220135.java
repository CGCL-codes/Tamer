package maggames.core.base;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import maggames.core.GameMove;
import maggames.core.Piece;
import maggames.core.GameTurn;
import maggames.core.exceptions.InvalidTurnException;
import maggames.core.visual.VisualPiece;
import maggames.core.visual.VisualSpace;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
public class BaseSwingInterface extends JPanel {

    protected static final long serialVersionUID = 5562601025300592899L;

    protected static final Logger log = Logger.getLogger(BaseSwingInterface.class);

    protected BaseGameEngine gameEngine;

    protected GameTurn currentTurn;

    protected VisualSpace[][] spaces = null;

    protected LinkedList<VisualPiece> pieces = null;

    protected JLabel lblCurrentPlayer = null;

    protected JButton btnMove = null;

    protected JPanel controllerPanel = null;

    private JLabel lblGameState = null;

    private JButton btnUndo = null;

    /**
	 * This is the default constructor
	 */
    public BaseSwingInterface(BaseGameEngine bge) {
        super();
        this.gameEngine = bge;
        initialize();
        log.debug("BaseSwingInterface constructed.");
    }

    public void initializePieces() {
        pieces = new LinkedList<VisualPiece>();
    }

    public void initializeSpaces(int spacing) {
        int xstart = 45;
        int ystart = 10 + this.controllerPanel.getHeight();
        if (spaces == null) {
            spaces = new VisualSpace[10][10];
        }
        for (int x = 0; x < spaces.length; x++) {
            for (int y = 0; y < spaces[x].length; y++) {
                spaces[x][y] = getNewSpace();
                spaces[x][y].setLocation(xstart + x * (spacing + spaces[x][y].getWidth()), ystart + y * (spacing + spaces[x][y].getHeight()));
                spaces[x][y].setBoardLocation(new Point(x, y));
                this.add(spaces[x][y]);
            }
        }
        this.setSize(xstart * 2 + spaces.length * (spacing + spaces[0][0].getWidth()), ystart + spacing * 2 + spaces[0].length * (spacing + spaces[0][0].getHeight()));
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    protected void initialize() {
        spaces = new VisualSpace[5][5];
        lblCurrentPlayer = new JLabel();
        lblGameState = new JLabel();
        lblCurrentPlayer.setText(gameEngine.getCurrentPlayer().toString());
        System.out.println("Base Swing Interface");
        lblGameState.setText(gameEngine.getGameState().toString());
        this.setLayout(null);
        this.add(getControllerPanel(), null);
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent e) {
                controllerPanel.setBounds(0, 0, e.getComponent().getWidth(), controllerPanel.getHeight());
            }
        });
        this.initializeSpaces(10);
        this.initializePieces();
    }

    /**
	 * This method initializes piece	
	 * 	
	 * @return maggames.view.VisualPiece	
	 */
    protected VisualPiece getNewPiece() {
        VisualPiece piece = new VisualPiece();
        piece.setLocation(this.getWidth() / 2, this.getHeight() / 2);
        piece.setMinimumSize(new Dimension(30, 30));
        piece.setSize(new Dimension(30, 30));
        piece.setPreferredSize(new Dimension(30, 30));
        piece.setLocation(new Point(149, 99));
        return piece;
    }

    protected VisualPiece getNewPiece(Piece p) {
        VisualPiece temp = getNewPiece();
        temp.setPiece(p);
        return temp;
    }

    /**
	 * This method initializes space	
	 * 	
	 * @return maggames.view.VisualSpace	
	 */
    protected VisualSpace getNewSpace() {
        VisualSpace space = new VisualSpace();
        space.setPreferredSize(new Dimension(32, 32));
        space.setSize(new Dimension(32, 32));
        return space;
    }

    public VisualSpace getSpaceAt(Point p) {
        for (int x = 0; x < spaces.length; x++) {
            for (int y = 0; y < spaces[x].length; y++) {
                VisualSpace s = spaces[x][y];
                if (s.getCenter().distanceSq(p) <= (s.getWidth() * s.getWidth() / 4)) {
                    return s;
                }
            }
        }
        return null;
    }

    public Piece[][] getBoard() {
        Piece[][] board = new Piece[spaces.length][spaces[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (spaces[i][j].getVisualPiece() == null) {
                    board[i][j] = Piece.NONE;
                } else {
                    board[i][j] = spaces[i][j].getVisualPiece().getPiece();
                }
            }
        }
        return board;
    }

    public GameTurn getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(GameTurn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void makeMove(GameMove m) {
        if (currentTurn == null) {
            currentTurn = new GameTurn(gameEngine.getCurrentPlayer());
        }
        currentTurn.addMove(m);
        log.debug("Making " + m);
    }

    public void takeTurn() {
        try {
            if (!gameEngine.takeTurn(currentTurn)) {
                if (currentTurn == null) {
                    JOptionPane.showMessageDialog(this, "No moves made.");
                } else {
                    StringBuffer errorMessage = new StringBuffer();
                    for (InvalidTurnException problem : currentTurn.getProblems()) {
                        errorMessage.append(problem.getMessage());
                    }
                    JOptionPane.showMessageDialog(this, errorMessage.toString());
                }
                reverseTurn();
            } else {
                currentTurn = null;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid move.");
        }
        lblCurrentPlayer.setText(gameEngine.getCurrentPlayer().toString());
        lblGameState.setText(gameEngine.getGameState().toString());
    }

    public void reverseTurn() {
        while (currentTurn.getNumMoves() > 0) {
            undoMostCurrentMove();
        }
        currentTurn = new GameTurn(currentTurn.getMover());
    }

    private void undoMostCurrentMove() {
        reverseMove(currentTurn.getLastMove());
        currentTurn.removeLastMove();
    }

    private void reverseMove(GameMove currentMove) {
        if (currentMove.getX1() == currentMove.getX2() && currentMove.getY1() == currentMove.getY2()) {
            VisualPiece p = getNewPiece();
            p.setPiece(currentMove.getPiece());
            p.setSpace(spaces[currentMove.getX1()][currentMove.getY1()]);
        } else if (currentMove.getX1() == GameMove.OFFBOARD && currentMove.getY1() == GameMove.OFFBOARD) {
            VisualPiece p = spaces[currentMove.getX2()][currentMove.getY2()].getVisualPiece();
            p.setSpace(null);
            if (p.getInitialLocation() == null) {
                p.setLocation(100, 5);
            } else {
                p.setLocation(p.getInitialLocation());
            }
        } else {
            VisualPiece p = spaces[currentMove.getX2()][currentMove.getY2()].getVisualPiece();
            p.setSpace(spaces[currentMove.getX1()][currentMove.getY1()]);
        }
    }

    /**
	 * This method initializes btnMove	
	 * 	
	 * @return javax.swing.JButton	
	 */
    protected JButton getBtnMove() {
        if (btnMove == null) {
            btnMove = new JButton();
            btnMove.setText("Take Turn");
            btnMove.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    log.debug("Attempting to take turn");
                    takeTurn();
                }
            });
        }
        return btnMove;
    }

    /**
	 * This method initializes controllerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    protected JPanel getControllerPanel() {
        if (controllerPanel == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.gridy = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            controllerPanel = new JPanel();
            controllerPanel.setLayout(new GridBagLayout());
            controllerPanel.setBounds(new Rectangle(0, 0, 298, 55));
            controllerPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            controllerPanel.add(lblCurrentPlayer, gridBagConstraints);
            controllerPanel.add(getBtnMove(), gridBagConstraints1);
            controllerPanel.add(lblGameState, gridBagConstraints11);
            controllerPanel.add(getBtnUndo(), gridBagConstraints12);
        }
        return controllerPanel;
    }

    /**
	 * This method initializes btnUndo	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnUndo() {
        if (btnUndo == null) {
            final BaseSwingInterface bsi = this;
            btnUndo = new JButton();
            btnUndo.setText("Undo Move");
            btnUndo.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    bsi.undoMostCurrentMove();
                }
            });
        }
        return btnUndo;
    }
}
