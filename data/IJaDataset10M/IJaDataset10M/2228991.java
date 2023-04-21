package pdp.scrabble.ihm.action.impl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;
import pdp.scrabble.Game;
import pdp.scrabble.game.Bag;
import pdp.scrabble.game.Board;
import pdp.scrabble.game.BoardCase;
import pdp.scrabble.game.GameEnvironment;
import pdp.scrabble.game.Letter;
import pdp.scrabble.game.Player;
import pdp.scrabble.ihm.MainFrame;
import pdp.scrabble.ihm.MainFrame_old;
import pdp.scrabble.ihm.BoardPanel;
import pdp.scrabble.ihm.action.BoardAction;
import pdp.scrabble.ihm.PlayerPanel;
import pdp.scrabble.utility.Debug;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static pdp.scrabble.Language.getMessagesLang;
import static pdp.scrabble.game.Board.HORI_DIM;
import static pdp.scrabble.game.Board.VERT_DIM;
import static pdp.scrabble.game.BoardCase.CASE_SIZE;
import static pdp.scrabble.game.BoardCaseState.FREE;
import static pdp.scrabble.game.BoardCaseState.NEW;
import static pdp.scrabble.game.BoardCaseState.OLD;

/**
 */
public class BoardActionImpl implements BoardAction, MouseListener, MouseMotionListener {

    /** Main frame reference. */
    private MainFrame mainFrame = null;

    /** Panel reference. */
    private BoardPanel panel = null;

    /** Game reference. */
    private GameEnvironment game = null;

    /** Board reference. */
    private Board board = null;

    /** Mouse cursor. */
    private int mx = 0, my = 0;

    /** Letters selection for switch. */
    private int selectV = 0, selectH = 0;

    /** Letters selection for drag. */
    private int dragV = 0, dragH = 0, dragOffsetX = 0, dragOffsetY = 0;

    /** Drag state. */
    private boolean isDragging = false;

    /** Create a new board action.
     * @param mainFrame main frame reference.
     * @param panel parent.
     * @param game game reference.
     */
    public BoardActionImpl(MainFrame mainFrame, BoardPanel panel, GameEnvironment game) {
        this.mainFrame = mainFrame;
        this.panel = panel;
        this.game = game;
        this.board = game.board();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.update(e);
        int vertCase = (this.my / CASE_SIZE);
        int horiCase = (this.mx / CASE_SIZE);
        if (this.board != null) {
            if (this.game != null) {
                Player player = this.game.getPlayer(this.game.engine().getPlayerTurn());
                if (player != null) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        this.cancelLetterOnBoard(player, vertCase, horiCase);
                    } else {
                        if (!this.dropLetterOnBoard(player, vertCase, horiCase)) {
                        }
                    }
                }
            }
        }
        this.panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.isDragging()) {
            this.selectV = (this.getMouseY() / CASE_SIZE);
            this.selectH = (this.getMouseX() / CASE_SIZE);
            if (this.board.getCase(this.dragV, this.dragH).getLetter() != null) {
                if (this.selectV >= 0 && this.selectH >= 0 && this.selectV < VERT_DIM && this.selectH < HORI_DIM) {
                    this.switchLetterOnBoard(this.dragV, this.dragH);
                }
            }
            this.isDragging = false;
            this.panel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.update(e);
        if (!this.isDragging()) {
            this.dragV = (this.getMouseY() / CASE_SIZE);
            this.dragH = (this.getMouseX() / CASE_SIZE);
            this.dragOffsetX = this.getDragH() * CASE_SIZE - this.getMouseX();
            this.dragOffsetY = this.getDragV() * CASE_SIZE - this.getMouseY();
            this.isDragging = true;
        }
        this.selectV = -1;
        this.selectH = -1;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.update(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /** Cancel a letter on board, and send it back to the player rack.
     * @param verticalCase board vertical case.
     * @param horizontalCase board horizontal case.
     */
    private void cancelLetterOnBoard(Player player, int vertCase, int horiCase) {
        if (player != null) {
            BoardCase boardCase = this.board.getCase(vertCase, horiCase);
            if (boardCase.getState() == NEW) {
                Debug.console("cancelLetterOnBoard", "remove", Debug.formatLetter(boardCase.getLetter()) + " at " + Debug.formatCoord(vertCase, horiCase));
                boardCase.getLetter().setJokerChar(Bag.NON_JOKER);
                player.getRack().addLetter(boardCase.getLetter());
                this.board.resetCase(vertCase, horiCase);
                this.board.removeCaseNew(boardCase);
                if (this.board.numberOfNewCases() == 0) {
                    PlayerPanel pp = this.mainFrame.getPlayerPanel();
                    pp.getButton("Validate").setEnabled(false);
                    pp.getButton("Cancel").setEnabled(false);
                }
                this.mainFrame.repaint();
            }
        }
    }

    /** Assign selected letter on board, remove letter from player rack.
     * @param verticalCase board vertical case.
     * @param horizontalCase board horizontal case.
     * @return true if has dropped letter, false else.
     */
    private boolean dropLetterOnBoard(Player player, int vertCase, int horiCase) {
        if (player != null && mainFrame.getMultiplayerPanel().getAction().isMyTurn()) {
            Letter letter = player.getSelectedLetter();
            if (letter != null && this.board.getCase(vertCase, horiCase).getState() == FREE) {
                if (letter.getName() == Bag.JOKER) {
                    Object result = JOptionPane.showInputDialog(this.panel, getMessagesLang("Which letter ?"), "Joker", OK_CANCEL_OPTION, null, Bag.AVAILABLE_LETTERS, 0);
                    Debug.console("dropLetterOnBoard", "choose joker", String.valueOf(result));
                    if (result != null) {
                        letter.setJokerChar((Character) result);
                    } else {
                        letter.setJokerChar(Bag.JOKER);
                    }
                }
                if (letter.getJokerChar() != Bag.JOKER) {
                    this.board.setCaseLetter(vertCase, horiCase, letter, true);
                    player.getRack().removeLetter(letter);
                    this.setPlayerPanelButtonState("Validate", true);
                    this.setPlayerPanelButtonState("Cancel", true);
                    Debug.console("dropLetterOnBoard", "drop", Debug.formatLetter(letter) + " at " + Debug.formatCoord(vertCase, horiCase));
                }
                player.setSelection(0);
                player.setSelectedLetter(null);
                this.game.repaint();
                this.selectV = -1;
                this.selectH = -1;
                return true;
            }
        }
        return false;
    }

    /** Set player panel button state.
     * @param name button name.
     * @param state button state.
     */
    public void setPlayerPanelButtonState(String name, boolean state) {
        this.mainFrame.getPlayerPanel().getButton(name).setEnabled(state);
    }

    /** Switch selected letter on board.
     * @param verticalCase board vertical case.
     * @param horizontalCase board horizontal case.
     */
    private void switchLetterOnBoard(int vertCase, int horiCase) {
        BoardCase case1 = this.board.getCase(vertCase, horiCase);
        BoardCase case2 = this.board.getCase(this.selectV, this.selectH);
        if (this.selectV == -1 && this.selectH == -1) {
            if (case1.getState() == NEW) {
                this.selectV = vertCase;
                this.selectH = horiCase;
            }
        } else {
            if ((case1.getState() != OLD && case2.getState() != OLD)) {
                Debug.console("switchLetterOnBoard", "switch", " from " + Debug.formatCoord(this.selectV, this.selectH) + " to " + Debug.formatCoord(vertCase, horiCase));
                this.board.switchCasesLetter(this.selectV, this.selectH, vertCase, horiCase);
                this.selectV = -1;
                this.selectH = -1;
            }
        }
    }

    /** Update on mouse changes.
     * @param e mouse event.
     */
    private void update(MouseEvent e) {
        this.mx = e.getX();
        this.my = e.getY();
        this.panel.repaint();
    }

    @Override
    public int getMouseX() {
        return this.mx;
    }

    @Override
    public int getMouseY() {
        return this.my;
    }

    @Override
    public int getDragOffsetX() {
        return this.dragOffsetX;
    }

    @Override
    public int getDragOffsetY() {
        return this.dragOffsetY;
    }

    @Override
    public int getDragV() {
        return this.dragV;
    }

    @Override
    public int getDragH() {
        return this.dragH;
    }

    @Override
    public boolean isDragging() {
        return this.isDragging;
    }
}
