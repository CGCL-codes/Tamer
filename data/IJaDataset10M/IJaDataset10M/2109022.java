package logicsim;

import java.awt.*;
import javax.swing.*;

public class Dummy extends Gate {

    static final long serialVersionUID = -6564591181872025047L;

    public Dummy() {
        super();
    }

    public Dummy(Wire w1) {
        super(w1);
    }

    public int getNumInput() {
        return 1;
    }

    public int getNumOutput() {
        return 1;
    }

    public boolean getOutput(int n) {
        if (n == 0 && getInput(0) != null) return getInputState(0); else return false;
    }

    public boolean isOutputPositive(int n) {
        return false;
    }
}
