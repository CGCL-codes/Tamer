package sudoku.engine;

import sudoku.Dimensions;
import sudoku.exceptions.ValueConflictException;

public class Eightynine implements Dimensions {

    private Nine[] nines;

    public Eightynine(Nine[] nines) {
        this.nines = nines;
    }

    public Eightynine() {
        nines = new Nine[DIM];
        for (int i = 0; i < DIM; i++) nines[i] = new Nine();
    }

    public void setNine(int n, Nine nine) {
        nines[n] = nine;
    }

    public Nine getNine(int n) {
        return nines[n];
    }

    public void setThree(int n, int t, Three three) {
        nines[n].setThree(t, three);
    }

    public Three getThree(int n, int t) {
        return nines[n].getThree(t);
    }

    public void setOne(int n, int o, One one) {
        nines[n].setOne(o, one);
    }

    public One getOne(int n, int o) {
        return nines[n].getOne(o);
    }

    public void setValue(int n, int o, int value) throws ValueConflictException {
        nines[n].setValue(o, value);
    }

    public int getValue(int n, int o) {
        return nines[n].getValue(o);
    }

    public int[] getPossibs(int n, int o) {
        return nines[n].getPossibs(o);
    }

    private boolean singleUpdate() throws ValueConflictException {
        boolean hasChanged = false;
        for (int i = 0; i < DIM; i++) {
            hasChanged = hasChanged | nines[i].update();
        }
        return hasChanged;
    }

    public boolean update() throws ValueConflictException {
        boolean hasChanged = false;
        while (singleUpdate()) {
            hasChanged = true;
        }
        return hasChanged;
    }
}
