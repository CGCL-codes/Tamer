package yati.ai;

import java.awt.Point;
import yati.data.Block;
import yati.data.Spielfeld;
import yati.game.SimpleGame;

public class OnePieceAI extends YatiAI {

    public OnePieceAI(SimpleGame sg) {
        super(sg);
    }

    public void doMove() {
        Spielfeld feld = sg.getSpielfeld();
        Block block = sg.getBlock();
        Spielfeld[][] fields = feld.getAllPossibilities(block);
        int wid = feld.getWidth();
        int bestx = -1, bestrot = -1;
        double bestval = -1e+20, bestpri = -1e+20;
        Block b0 = block.copy();
        for (int rot = 0; rot < 4; rot++) {
            Block b = b0.copy();
            for (int dx = -1; dx != 3; dx += 2) {
                Point p = (Point) b0.getPosition().clone();
                b.setPosition(p);
                int y = p.y;
                while (!feld.checkCollision(b, p)) {
                    if (fields[p.x + 2][rot] != null) {
                        feld.drop(b);
                        double priority = 0.0;
                        priority += 100 * Math.abs(b.getPosition().x - 4);
                        if (b.getPosition().x < 4) {
                            priority += 10;
                        }
                        priority -= b.getRotation();
                        double curval = evaluateField(fields[p.x + 2][rot], b);
                        if (curval > bestval) {
                            bestval = curval;
                            bestpri = priority;
                            bestx = p.x;
                            bestrot = rot;
                        } else if (curval == bestval && priority > bestpri) {
                            bestpri = priority;
                            bestx = p.x;
                            bestrot = rot;
                        }
                    }
                    p.move(p.x + dx, y);
                }
            }
            b0.rotate();
        }
        for (int i = 0; i < bestrot; ++i) {
            sg.block_rotate();
        }
        sg.getBlock().setPosition(new Point(bestx, block.getPosition().y));
        sg.getSpielfeld().drop(sg.getBlock());
    }

    private double evaluateField(Spielfeld feld, Block b) {
        int eroded = 0;
        int rowtrans = 0;
        int coltrans = 0;
        int buried = 0;
        int wells = 0;
        int pileh = 0;
        double landh = 20 - (.5 * (b.getMax().y + b.getMin().y));
        int comprows = feld.getCompletedRows();
        if (comprows > 0) {
            eroded = comprows * feld.getEliminatedCells(b);
            feld.deleteRows();
        }
        pileh = (20 - (feld.getHeight() - feld.getMaxColumnHeight()));
        rowtrans = 2 * (feld.getHeight() - pileh);
        for (int y = 20 - pileh; y < feld.getHeight(); y++) rowtrans += feld.getRowTransitions(y);
        for (int x = 0; x < feld.getWidth(); x++) {
            coltrans += feld.getColTransitions(x);
            buried += feld.getBuried(x);
            wells += feld.getWells(x);
        }
        double rating = -landh + eroded - rowtrans - coltrans - 4 * buried - wells;
        return rating;
    }
}
