package gov.sns.apps.rocs;

import java.math.*;
import java.util.*;
import java.io.*;

public class TuneSearch extends TuneSettings {

    public double key_x = 0, key_y = 0;

    public int index = 0, jdex = 0;

    public int foundxi = 0, foundyj = 0;

    public int position[] = new int[2];

    public int[] searchData(double key_X, double key_Y) throws Exception {
        foundxi = 0;
        foundyj = 0;
        index = 0;
        jdex = 0;
        readData();
        key_x = key_X;
        key_y = key_Y;
        if (key_x == gridmax) {
            foundxi = (imax);
            position[0] = foundxi;
        } else {
            for (index = 0; index < (imax); index++) {
                if (key_x >= tune_x[index][jdex] & key_x < tune_x[index + 1][jdex]) {
                    foundxi = index;
                    position[0] = foundxi;
                }
            }
        }
        if (key_y == gridmax) {
            foundyj = (jmax);
            position[1] = foundyj;
        } else {
            for (jdex = 0; jdex < (jmax); jdex++) {
                if (key_y >= tune_y[foundxi][jdex] & key_y < tune_y[foundxi][jdex + 1]) {
                    foundyj = jdex;
                    position[1] = foundyj;
                }
            }
        }
        return position;
    }

    public int getXi() {
        return foundxi;
    }

    public int getYj() {
        return foundyj;
    }
}
