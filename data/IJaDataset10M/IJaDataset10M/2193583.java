package root.board.pieces;

import root.board.Piece;
import root.board.moviment.Moviment;
import root.board.moviment.Position;

public class Horse extends Piece {

    public static void main(String[] args) {
        new Horse().moviments(7, 3);
    }

    public Horse() {
        super();
    }

    /**
	 * @param team
	 */
    public Horse(int team) {
        super(team);
    }

    @Override
    public Moviment[] moviments(int xPosition, int yPosition) {
        Moviment[] moviments = new Moviment[8];
        int qX = 2;
        int qY = 2;
        int indexCounter = 0;
        do {
            qX--;
            do {
                qY--;
                boolean xOk2 = false, yOk2 = false, xOk1 = false, yOk1 = false;
                if ((qX * 2) + xPosition < 8 && (qX * 2) + xPosition >= 0) xOk2 = xOk1 = true;
                if ((qY * 2) + yPosition < 8 && (qY * 2) + yPosition >= 0) yOk2 = yOk1 = true;
                if ((qX * 1) + xPosition < 8 && (qX * 1) + xPosition >= 0) xOk1 = true;
                if ((qY * 1) + yPosition < 8 && (qY * 1) + yPosition >= 0) yOk1 = true;
                if (xOk2 && yOk2) {
                    try {
                        moviments[indexCounter++] = new Moviment(new Position(xPosition, yPosition), new Position((xPosition + (qX * 2)), (yPosition + (qY * 1))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        moviments[indexCounter++] = new Moviment(new Position(xPosition, yPosition), new Position((xPosition + (qX * 1)), (yPosition + (qY * 2))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (xOk2 && yOk1) {
                    try {
                        moviments[indexCounter++] = new Moviment(new Position(xPosition, yPosition), new Position((xPosition + (qX * 2)), (yPosition + (qY * 1))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (yOk2 && xOk1) {
                    try {
                        moviments[indexCounter++] = new Moviment(new Position(xPosition, yPosition), new Position((xPosition + (qX * 1)), (yPosition + (qY * 2))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while ((qY--) == 1);
            qY = 2;
        } while (qX-- == 1);
        for (Moviment moviment : moviments) {
            if (moviment != null) System.out.println(moviment.toString());
        }
        return moviments;
    }
}
