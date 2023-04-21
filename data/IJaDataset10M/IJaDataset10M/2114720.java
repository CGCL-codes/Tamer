package jsattrak.utilities;

import java.io.Serializable;

public class StateVector implements Serializable {

    public double[] state;

    public StateVector() {
        state = new double[7];
    }

    public StateVector(double[] newStateWithTime) {
        state = new double[7];
        for (int i = 0; i < 7; i++) {
            state[i] = newStateWithTime[i];
        }
    }

    public StateVector(double[] newState, double time) {
        state = new double[7];
        state[0] = time;
        for (int i = 1; i < 7; i++) {
            state[i] = newState[i - 1];
        }
    }

    public StateVector(double t, double x, double y, double z, double dx, double dy, double dz) {
        state = new double[7];
        state[0] = t;
        state[1] = x;
        state[2] = y;
        state[3] = z;
        state[4] = dx;
        state[5] = dy;
        state[6] = dz;
    }

    public String toString() {
        return "" + state[0] + "," + state[1] + "," + state[2] + "," + state[3] + "," + state[4] + "," + state[5] + "," + state[6];
    }
}
