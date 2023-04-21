package net.sfjinyan.client.board;

import free.chess.JChessClock;
import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import nanoxml.XMLElement;
import net.sfjinyan.client.Game;
import net.sfjinyan.client.JinyanClient;
import net.sfjinyan.client.event.GameMovesListener;
import net.sfjinyan.common.Message;

class BoardPanel extends JPanel implements GameMovesListener {

    private JBoard graphicBoard;

    private JLabel gameLabel;

    private JPanel infoBox;

    private JChessClock whiteClock;

    private JChessClock blackClock;

    protected MovesTable moveListTable;

    private Game game;

    BoardPanel(JBoard graphicBoard, Game game, boolean flipped) {
        this.game = game;
        game.addGameMovesListener(this);
        setLayout(null);
        this.graphicBoard = graphicBoard;
        add(graphicBoard);
        createComponents(flipped);
        graphicBoard.setVisible(true);
    }

    @Override
    public void doLayout() {
        int maxH, maxW;
        final int constt = 70;
        maxH = maxW = getSize().height;
        Dimension size = getSize();
        if (size.getSize().width - constt <= size.width) {
            maxW = getParent().getSize().width - constt;
        }
        if (size.getSize().height - constt < size.width) {
            maxH = getParent().getSize().width - constt;
            if (maxH > getParent().getSize().height) {
                maxH = getParent().getSize().height - 7;
            }
        }
        if (maxH < maxW) graphicBoard.setSize(maxH, maxH); else graphicBoard.setSize(maxW, maxW);
        infoBox.setBounds(size.height, 0, size.width - size.height, size.height);
    }

    protected JLabel createGameLabel() {
        JLabel gl = new JLabel(getGameLabelText());
        gl.setFont(new Font("SansSerif", Font.PLAIN, 20));
        return gl;
    }

    static JInternalFrame getInternalFrame(String name, final BoardPanel bp) {
        JInternalFrame iframe = new JInternalFrame();
        iframe.setResizable(true);
        iframe.setClosable(true);
        iframe.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent arg0) {
            }

            public void internalFrameClosing(InternalFrameEvent arg0) {
            }

            public void internalFrameIconified(InternalFrameEvent arg0) {
            }

            public void internalFrameDeiconified(InternalFrameEvent arg0) {
            }

            public void internalFrameActivated(InternalFrameEvent arg0) {
            }

            public void internalFrameDeactivated(InternalFrameEvent arg0) {
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent arg0) {
                XMLElement el = Message.Request.toXMLElement("GameUnobserve");
                el.setAttribute("gamenumber", bp.game.getId());
                JinyanClient.connection.send(el);
            }
        });
        iframe.setTitle(name);
        iframe.setMaximizable(true);
        iframe.add(bp);
        iframe.setSize(580, 400);
        return iframe;
    }

    private void createComponents(boolean flipped) {
        whiteClock = new JChessClock(game.getWhiteClock());
        blackClock = new JChessClock(game.getBlackClock());
        gameLabel = createGameLabel();
        moveListTable = new MovesTable();
        infoBox = new JPanel(null);
        infoBox.setLayout(new BorderLayout());
        String whiteLabl = game.getWhitePlayerHandle() + "(" + game.getWhitePlayerRating() + ")";
        String blackLabl = game.getBlackPlayerHandle() + "(" + game.getBlackPlayerRating() + ")";
        JLabel whiteLabel = new JLabel(flipped ? whiteLabl : blackLabl);
        whiteLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel blackLabel = new JLabel(flipped ? blackLabl : whiteLabl);
        whiteLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        getWhiteClock().setAlignmentY(Component.CENTER_ALIGNMENT);
        blackLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        getBlackClock().setAlignmentY(Component.CENTER_ALIGNMENT);
        blackLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        JComponent whiteLabelBox = new JPanel(new BorderLayout());
        whiteLabelBox.add(whiteLabel, BorderLayout.CENTER);
        JComponent blackLabelBox = new JPanel(new BorderLayout());
        blackLabelBox.add(blackLabel, BorderLayout.CENTER);
        Box gameLabelBox = Box.createHorizontalBox();
        gameLabelBox.add(gameLabel);
        gameLabelBox.add(Box.createHorizontalGlue());
        JPanel upperBox = new JPanel(null);
        upperBox.setLayout(new BoxLayout(upperBox, BoxLayout.Y_AXIS));
        JPanel middleBox = new JPanel(null);
        middleBox.setLayout(new BoxLayout(middleBox, BoxLayout.Y_AXIS));
        JPanel bottomBox = new JPanel(null);
        bottomBox.setLayout(new BoxLayout(bottomBox, BoxLayout.Y_AXIS));
        upperBox.add(whiteLabelBox);
        upperBox.add(Box.createVerticalStrut(10));
        upperBox.add(flipped ? whiteClock : blackClock);
        middleBox.add(gameLabelBox);
        middleBox.add(Box.createVerticalStrut(10));
        middleBox.add(moveListTable.scrollPane);
        if (game.isOwn()) middleBox.add(new ButtonsPane());
        bottomBox.add(blackLabelBox);
        bottomBox.add(Box.createVerticalStrut(10));
        bottomBox.add(flipped ? blackClock : whiteClock);
        infoBox.add(upperBox, BorderLayout.NORTH);
        infoBox.add(middleBox, BorderLayout.CENTER);
        infoBox.add(bottomBox, BorderLayout.SOUTH);
        add(infoBox);
        infoBox.setVisible(true);
    }

    private String getGameLabelText() {
        String type = (game.getTime() < 15 && game.getInc() < 15) ? "Blitz" : "Standard";
        String isRated = game.isRated() ? "rated" : "unrated";
        return type + " " + game.getTime() + " " + game.getInc() + " " + isRated;
    }

    public void updateMoves() {
        moveListTable.updateMoveListTable();
    }

    @Override
    public void movePerformed(Move move) {
        moveListTable.addMoveToListTable(move);
    }

    /**
     * @return the whiteClock
     */
    public JChessClock getWhiteClock() {
        return whiteClock;
    }

    /**
     * @return the blackClock
     */
    public JChessClock getBlackClock() {
        return blackClock;
    }

    private class MovesTable extends JTable {

        private JScrollPane scrollPane;

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        MovesTable() {
            TableModel tm = new DefaultTableModel(new String[] { "Move No.", "White", "Black" }, 0);
            setModel(tm);
            setCellSelectionEnabled(true);
            getTableHeader().setPreferredSize(new Dimension(150, 18));
            getTableHeader().setReorderingAllowed(false);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setRequestFocusEnabled(false);
            scrollPane = new JScrollPane(this);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }

        /**
         * Brings the the move list table up to date with the current move list and
         * displayed position.
         */
        protected void updateMoveListTable() {
            Vector madeMoves = game.getMovesMade();
            DefaultTableModel model = (DefaultTableModel) moveListTable.getModel();
            int movesSinceStart = game.getPlies() / 2;
            int moveCount = madeMoves.size();
            boolean isFirstMoveBlack = ((Move) madeMoves.get(0)).isWhite();
            int numRows = isFirstMoveBlack ? 1 + moveCount / 2 : (moveCount + 1) / 2;
            model.setNumRows(numRows);
            for (int i = 0; i < numRows; i++) {
                model.setValueAt((i + 1 + movesSinceStart) + ".", i, 0);
            }
            int row = 0;
            int column = isFirstMoveBlack ? 2 : 1;
            for (int i = 0; i < moveCount; i++) {
                Object move = madeMoves.elementAt(i);
                model.setValueAt(move, row, column);
                column++;
                if (column == 3) {
                    row++;
                    column = 1;
                }
            }
            if (column == 2) {
                model.setValueAt(null, row, column);
            }
        }

        /**
         * Adds a single move to the move list TableModel.
         */
        protected void addMoveToListTable(Move move) {
            DefaultTableModel model = (DefaultTableModel) moveListTable.getModel();
            int movesSinceStart = game.getPlies() / 2;
            int rowCount = moveListTable.getRowCount();
            if ((rowCount == 0) || move.isWhite()) {
                model.setNumRows(++rowCount);
                model.setValueAt((rowCount + movesSinceStart) + ".", rowCount - 1, 0);
            }
            if (move.isWhite()) {
                model.setValueAt(move.getStrRepr(), rowCount - 1, 1);
            } else {
                model.setValueAt(move.getStrRepr(), rowCount - 1, 2);
            }
        }
    }
}
