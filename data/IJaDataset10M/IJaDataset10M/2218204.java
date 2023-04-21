package pl.taab.scrachi.common;

import java.util.ArrayList;
import java.util.Collection;

public class ScrachiBasicLogic {

    public boolean isReductionPossible(ScrachiTable st, int x, int y) {
        if (st.isBorder(x, y)) {
            throw new RuntimeException("The border position passed unproperly");
        }
        if (st.getAt(x, y) == 0) {
            throw new RuntimeException("Reduction on empty ball impossible");
        }
        char c = st.getAt(x, y);
        if (c == st.getAt(x - 1, y) || c == st.getAt(x + 1, y) || c == st.getAt(x, y - 1) || c == st.getAt(x, y + 1)) {
            return true;
        } else {
            return false;
        }
    }

    public int nrOfReduction(ScrachiTable st, int x, int y) {
        return reduce(st, x, y, false).size();
    }

    public Collection reduce(ScrachiTable st, int x, int y) {
        return reduce(st, x, y, true);
    }

    private Collection reduce(ScrachiTable st, int x, int y, boolean reduceBall) {
        ScrachiTable stReduce;
        if (reduceBall) {
            stReduce = st;
        } else {
            stReduce = st.copy();
        }
        ArrayList reduced = new ArrayList();
        reduceRecursive(stReduce, x, y, st.getAt(x, y), reduced);
        if (reduceBall) {
            fallBalls(st);
        }
        return reduced;
    }

    private void fallBalls(ScrachiTable st) {
        int newX = 1;
        for (int x = 1; x <= st.getSizeX(); x++) {
            int newY = st.getSizeY();
            for (int y = st.getSizeY(); y >= 1; y--) {
                if (!st.isEmptyAt(x, y)) {
                    if (newY != y || newX != x) {
                        st.moveBall(x, y, newX, newY);
                    }
                    newY--;
                }
            }
            if (newY != st.getSizeY()) {
                newX++;
            }
        }
    }

    private void reduceRecursive(ScrachiTable st, int x, int y, char c, Collection reduced) {
        if (st.getAt(x, y) == c) {
            st.setEmptyAt(x, y);
            reduced.add(new XY(x, y));
            reduceRecursive(st, x - 1, y, c, reduced);
            reduceRecursive(st, x + 1, y, c, reduced);
            reduceRecursive(st, x, y - 1, c, reduced);
            reduceRecursive(st, x, y + 1, c, reduced);
        }
    }

    public ScrachiTable randomTable(int sizeX, int sizeY, BallSet p) {
        ScrachiTable st = new ScrachiTable(sizeX, sizeY);
        int amount = p.getSet().size();
        RandomGen rand = new RandomGen();
        for (int y = 1; y <= st.getSizeY(); y++) {
            for (int x = 1; x <= st.getSizeX(); x++) {
                Character ball = (Character) p.getSet().get(rand.nextInt(amount));
                st.setAt(x, y, ball.charValue());
            }
        }
        return st;
    }
}
