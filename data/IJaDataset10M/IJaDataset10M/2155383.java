package org.maverickdbms.test.java.string;

import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.mvProgram;
import org.maverickdbms.basic.mvString;

public class FIELD extends mvProgram {

    /**
    * Test the alpha function in the mvString class
    */
    public mvConstantString run(mvString[] arg) {
        String[] test = { "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890", "01234567890" };
        String[] delim = { "M", "z", "A", "5", "4", "3", "2", "1", "5", "4", "3", "2", "1" };
        int[] index = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        String[] result = { "ABCDEFGHIJKL", "abcdefghijklmnopqrstuvwxy", "", "01234", "0123", "012", "01", "0", "67890", "567890", "4567890", "34567890", "234567890" };
        mvString A = (mvString) arg[1];
        mvString B = (mvString) arg[2];
        mvString C = (mvString) arg[3];
        mvString D = (mvString) arg[4];
        mvString E = (mvString) arg[5];
        mvString F = (mvString) arg[6];
        for (int i = 0; i < test.length; i++) {
            A.set(test[i]);
            B.set(delim[i]);
            C.set(index[2 * i]);
            D.set(index[2 * i + 1]);
            F.set(result[i]);
            A.FIELD(E, getString(), getString(), B, C, D);
            if (!E.equals(F)) {
                ((mvString) arg[0]).set("Error " + A + ".FIELD(" + B + "," + C + "," + D + ") = " + E + " not " + F + "\n");
                return null;
            }
        }
        return null;
    }

    public void initVariables() {
    }
}

;
