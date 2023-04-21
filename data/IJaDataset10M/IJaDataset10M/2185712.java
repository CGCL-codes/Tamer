package org.martho.game.model.figures;

import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.martho.game.client.ChessClient;
import org.martho.game.model.ChessColor;
import org.martho.game.model.Field;
import org.martho.game.model.Figure;

public class Queen extends Figure {

    private ImageIcon tb1;

    public Queen(String name, ChessColor figurColor, Field field) {
        super(name, figurColor, field);
        if (this.getFigurColor() == ChessColor.black) tb1 = new ImageIcon(ChessClient.class.getClassLoader().getResource("org/martho/game/client/res/queenBlack.png")); else tb1 = new ImageIcon(ChessClient.class.getClassLoader().getResource("org/martho/game/client/res/queenWhite.png"));
    }

    /**
	 * tries to reach a field
	 * 
	 * returns an ArrayList with Fields that the queen need to step over
	 * in order to reach the designated field
	 * 
	 */
    public ArrayList<Field> internalAllowedToMove(Field destField) {
        int destRow = destField.getRow();
        int nowRow = this.getField().getRow();
        int destCol = destField.getColumn();
        int nowCol = this.getField().getColumn();
        int deltaRow = destRow - nowRow;
        int deltaCol = destCol - nowCol;
        ArrayList<Field> result = new ArrayList<Field>();
        if (deltaRow != 0 && deltaCol != 0) {
            if (Math.abs(deltaRow) != Math.abs(deltaCol)) return null;
        }
        int plusX;
        int plusY;
        if (deltaRow > 0) plusY = 1; else if (deltaRow < 0) plusY = -1; else plusY = 0;
        if (deltaCol > 0) plusX = 1; else if (deltaCol < 0) plusX = -1; else plusX = 0;
        while (nowRow != destRow || nowCol != destCol) {
            result.add(getField().getBoard().getField(nowRow, nowCol));
            nowRow = nowRow + plusY;
            nowCol = nowCol + plusX;
            if (getField().getBoard().getField(nowRow, nowCol).hasFigure()) {
                if (nowRow != destRow || nowCol != destCol) return null;
                if (nowRow == destRow && nowCol == destCol && getField().getBoard().getField(nowRow, nowCol).getFigure().getFigurColor() == this.getFigurColor()) {
                    return null;
                }
            }
        }
        return result;
    }

    @Override
    public boolean move(Field destField, boolean virtual) {
        if (allowedToMove(destField) != null) {
            this.getField().setFigureNull();
            destField.setFigure(this, false, virtual);
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        if (getFigurColor() == ChessColor.white) return " Qw |"; else return " Qb |";
    }

    public Image getImage() {
        return tb1.getImage();
    }
}
