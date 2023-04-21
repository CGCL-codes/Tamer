package Main;

public class Rule {

    public Rule(Pieces p) {
        pieces = p;
    }

    public boolean isWin() {
        int num = 0;
        int _x = pieces.lastX;
        int _y = pieces.lastY;
        int color = pieces.lastColor;
        int[][] chessboard = pieces.getPieces();
        int xx, yy;
        for (int dir = 0; dir < 4; dir++) {
            num = 1;
            changeDir(dir);
            for (int m = 0; m < 2; m++) {
                xx = _x + xi[m];
                yy = _y + yi[m];
                for (int n = 1; n < 5; n++) {
                    int size = Pieces.dimX;
                    if (xx < size && xx >= 0 && yy < size && yy >= 0) {
                        if (chessboard[xx][yy] == color) {
                            num++;
                            xx += xi[m];
                            yy += yi[m];
                        } else {
                            n = 5;
                        }
                    } else {
                        n = 5;
                    }
                }
            }
            if (num >= 5) {
                return true;
            }
        }
        return false;
    }

    private void changeDir(int dir) {
        switch(dir) {
            case 0:
                xi[0] = -1;
                yi[0] = 0;
                xi[1] = 1;
                yi[1] = 0;
                break;
            case 1:
                xi[0] = -1;
                yi[0] = -1;
                xi[1] = 1;
                yi[1] = 1;
                break;
            case 2:
                xi[0] = 0;
                yi[0] = -1;
                xi[1] = 0;
                yi[1] = 1;
                break;
            case 3:
                xi[0] = -1;
                yi[0] = 1;
                xi[1] = 1;
                yi[1] = -1;
        }
    }

    private int[] xi = { 0, 0 };

    private int[] yi = { 0, 0 };

    private Pieces pieces;
}
