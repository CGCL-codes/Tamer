package shu.cms.colorformat.legend;

import java.io.*;
import shu.cms.colorspace.independ.*;
import shu.math.array.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �ΨӱNSpectraWinASCIIFile��Lab�C�X
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class SpectraWinLabLister {

    public static String getString(String dirName) {
        StringBuilder buf = new StringBuilder();
        double[][] array = getArray(dirName);
        for (int x = 0; x < array.length; x++) {
            buf.append(DoubleArray.toString(array[x]) + "\n");
        }
        return buf.toString();
    }

    public static double[][] getArray(String dirName) {
        File dir = new File(dirName);
        String[] fileList = dir.list();
        double[][] array = new double[fileList.length][];
        for (int x = 0; x < fileList.length; x++) {
            String file = fileList[x];
            SpectraWinAsciiParser pr650parser = new SpectraWinAsciiParser(dirName + '/' + file);
            SpectraWinAsciiFile swFile = pr650parser.getSpectraWinAsciiFile();
            array[x] = swFile.getCalculated()._Lab;
        }
        return array;
    }

    public static CIELab[] array2Labs(double[][] array) {
        int size = array.length;
        CIELab[] Labs = new CIELab[size];
        for (int x = 0; x < size; x++) {
            double[] tmp = new double[3];
            tmp[0] = array[x][0];
            tmp[1] = array[x][1];
            tmp[2] = array[x][2];
            Labs[x] = new CIELab(tmp);
        }
        return Labs;
    }

    public static final int STRING = 1;

    public static final int ARRAY = 2;

    public static void main(String[] args) {
        String dirName = "tmp\\MCDM2\\CC24White\\A1";
        switch(STRING) {
            case STRING:
                System.out.println(getString(dirName));
                break;
            case ARRAY:
                break;
        }
    }
}
