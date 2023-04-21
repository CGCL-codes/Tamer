package com.mockturtlesolutions.snifflib.util;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.Subscript;
import com.mockturtlesolutions.snifflib.util.QTreeNode;
import com.mockturtlesolutions.snifflib.util.Qdecomp;
import com.mockturtlesolutions.snifflib.util.Nhood;
import com.mockturtlesolutions.snifflib.util.Binary;
import com.mockturtlesolutions.snifflib.datatypes.DblSort;
import com.mockturtlesolutions.snifflib.datatypes.DblRandom;

public class test_util {

    public static void main(String[] argv) {
        DblMatrix XData[] = new DblMatrix[2];
        XData[0] = new DblMatrix("[1 1 1 1 1; 2 2 2 2 2; 3 3 3 3 3; 4 4 4 4 4; 5 5 5 5 5]");
        XData[1] = new DblMatrix("[1 2 3 4 5; 1 2 3 4 5; 1 2 3 4 5; 1 2 3 4 5; 1 2 3 4 5]");
        DblMatrix YData = new DblMatrix("[1 2 3 4 5; 6 7 8 9 10; 11 12 13 14 15; 16 17 18 19 20; 21 22 23 24 25]");
        long seed = 1;
        DblRandom R = new DblRandom();
        R.setSeed(seed);
        int[] size = new int[2];
        size[0] = 25;
        size[1] = 1;
        XData[0] = R.normRand(size);
        XData[1] = R.normRand(size);
        YData = R.normRand(size);
        Qdecomp Q = new Qdecomp(XData, YData);
        System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        Q.decompose();
        Q.show("Q");
        DblMatrix[] refpt = new DblMatrix[2];
        refpt[0] = new DblMatrix(new Double(0.1));
        refpt[1] = new DblMatrix(new Double(0.1));
        int loc = Q.locate(refpt);
        System.out.println("Location:" + loc);
        NNCalc NN = new NNCalc(XData);
        NN.setK(5);
        DblSort S = NN.getKNearest(refpt);
        S.show();
    }
}
