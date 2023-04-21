package chessitem;

import chesslan.ChessCell;
import java.util.ArrayList;

/**
 *
 * @author Ngo Anh Tuan
 */
public abstract class ChessItem {

    public static final int WHITE = 0;

    public static final int BLACK = 1;

    protected String image = null;

    protected String smallImage = null;

    protected int type = WHITE;

    public ChessItem(int type) {
        this.type = type;
    }

    public abstract ArrayList<ChessCell> getAvailableTargets(ChessCell currentCell, ChessCell[][] cellTable);

    public abstract boolean checkTargetAvailable(ChessCell currentCell, ChessCell targetCell, ChessCell[][] cellTable);

    public abstract String getName();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }
}
