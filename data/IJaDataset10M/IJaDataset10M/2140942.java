package auo.cms.test;

import java.io.*;
import java.util.*;
import shu.cms.plot.Plot2D;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class WavefromPicker {

    public static void main(String[] args) {
        processDot(args);
    }

    public static void processDot(String[] args) {
        final double downThreshhold = 6.6;
        ArrayList<Double> list = new ArrayList<Double>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("D:/�n��/nobody zone/exp data/0914/test.csv"));
            boolean overThreshhold = false;
            ArrayList<Double> overThreshholdList = null;
            while (br.ready()) {
                String line = br.readLine();
                double d = Double.parseDouble(line);
                if (false == overThreshhold && d > downThreshhold) {
                    overThreshhold = true;
                    overThreshholdList = new ArrayList<Double>();
                }
                if (true == overThreshhold) {
                    if (d > downThreshhold) {
                        overThreshholdList.add(d);
                    } else {
                        overThreshhold = false;
                        int size = overThreshholdList.size();
                        int size10 = size / 10;
                        double sum = 0;
                        for (int x = size10; x < size10 * 9; x++) {
                            sum += overThreshholdList.get(x);
                        }
                        double average = sum / (size10 * 9 - size10 + 1);
                        list.add(average);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Plot2D plot = Plot2D.getInstance();
        int size = list.size();
        for (int x = 0; x < size; x++) {
            double d = list.get(x).doubleValue();
            plot.addCacheScatterLinePlot("", x, d);
        }
        plot.setVisible();
    }

    public static void shrink(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("12401.csv"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("_12401.csv"));
            int index = 0;
            while (br.ready()) {
                String line = br.readLine();
                index++;
                if (index % 100 == 0) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
