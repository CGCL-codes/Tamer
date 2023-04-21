package org.linzhu.Connect4.ui;

import javax.swing.table.AbstractTableModel;
import org.linzhu.Connect4.core.Connect4Exception;
import org.linzhu.Connect4.core.Connect4Game;
import org.linzhu.Connect4.core.Connect4GameImpl;
import org.linzhu.Connect4.core.Player;
import org.linzhu.Connect4.core.Status;

/**
 *
 * @author zhulin
 */
public class Connect4TableModel extends AbstractTableModel {

    private Connect4Game game;

    private int headerColumnIndex;

    private Player currPlayer;

    public Connect4TableModel() {
        newGame();
    }

    public void newGame() {
        this.game = new Connect4GameImpl();
        headerColumnIndex = getColumnCount() / 2;
        currPlayer = Player.Player1;
    }

    public int getRowCount() {
        return game.getRowCount() + 1;
    }

    public int getColumnCount() {
        return game.getColumnCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) {
            if (columnIndex == headerColumnIndex) {
                return currPlayer;
            } else {
                return null;
            }
        } else {
            return game.getPlayerAt(columnIndex, rowIndex - 1);
        }
    }

    public Class getColumnClass(int columnIndex) {
        return Player.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Status dropMarble() throws Connect4Exception {
        int rowIndex = game.dropMarble(currPlayer, headerColumnIndex);
        if (currPlayer == Player.Player1) {
            currPlayer = Player.Player2;
        } else {
            currPlayer = Player.Player1;
        }
        fireTableCellUpdated(0, headerColumnIndex);
        fireTableCellUpdated(rowIndex, headerColumnIndex);
        return game.getStatus();
    }

    public void moveHeaderPieceLeft() {
        if (headerColumnIndex > 0) {
            headerColumnIndex--;
        }
        fireTableCellUpdated(0, headerColumnIndex + 1);
        fireTableCellUpdated(0, headerColumnIndex);
    }

    public void moveHeaderPieceRight() {
        if (headerColumnIndex < game.getColumnCount() - 1) {
            headerColumnIndex++;
        }
        fireTableCellUpdated(0, headerColumnIndex - 1);
        fireTableCellUpdated(0, headerColumnIndex);
    }
}
