package org.amse.grigory.dchess.kernel;

import java.util.*;

/**
 * 
 * @author grigory
 * 
 */
public final class Moves {

    public static final char MOVE_SLIDE = '0';

    public static final char MOVE_JUMP = '1';

    public static final char MOVE_FSLIDE = '2';

    public static final char MOVE_FJUMP = '3';

    public static final char MOVE_SPECIAL = '4';

    public static final char NOT_A_MOVE = 'n';

    private ArrayList myX, myY, myType;

    public Moves() {
        myX = new ArrayList();
        myY = new ArrayList();
        myType = new ArrayList();
    }

    public Moves(int[] x, int[] y, char type[]) {
        myX = new ArrayList();
        myY = new ArrayList();
        myType = new ArrayList();
        for (int i = 0; i < x.length; ++i) {
            myX.add(x[i]);
            myY.add(y[i]);
            myType.add(type[i]);
        }
    }

    public int getMoveX(int index) {
        if (myX.get(index) instanceof Integer) {
            Integer temp = (Integer) myX.get(index);
            return temp.intValue();
        }
        return 0;
    }

    public int getMoveY(int index) {
        if (myY.get(index) instanceof Integer) {
            Integer temp = (Integer) myY.get(index);
            return temp.intValue();
        }
        return 0;
    }

    public char getMoveType(int index) {
        if (myType.get(index) instanceof Character) {
            Character temp = (Character) myType.get(index);
            return temp;
        }
        return '!';
    }

    public int getNumber() {
        return myX.size();
    }

    public void setMoveX(int index, int x) {
        myX.set(index, x);
    }

    public void setMoveY(int index, int y) {
        myY.set(index, y);
    }

    public void setMoveType(int index, char type) {
        myType.set(index, type);
    }

    public void add(int x, int y, char type) {
        myX.add(x);
        myY.add(y);
        myType.add(type);
    }

    public void add(int[] x, int[] y, char[] type) {
        for (int i = 0; i < x.length; ++i) {
            myX.add(x[i]);
            myY.add(y[i]);
            myType.add(type[i]);
        }
    }

    public void remove(int index) {
        myX.remove(index);
        myY.remove(index);
        myType.remove(index);
    }

    /**
     * Removes quantity-moves from index.
     * very slow method!
     * @param index
     * @param quantity
     * 
     */
    public void remove(int index, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            myX.remove(index);
            myY.remove(index);
            myType.remove(index);
        }
    }
}
