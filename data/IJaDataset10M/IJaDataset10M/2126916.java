package org.bissa.weatherMonitor.collector;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class DataGenerator {

    /**
     * generate tempature value tuples form 0 - 38 degrees ramdomly
     *
     * @throws Exception
     */
    public static void randomTemtature(int location) throws Exception {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("station_" + location + "data.txt")));
        for (int j = 0; j < 10000; j++) {
            double rand = Math.random();
            float temp = (float) (38 * rand);
            out.println("" + location + ":" + "" + temp + ":" + "" + j);
        }
        out.flush();
        out.close();
    }

    public static void sinVarieingTemrature(int location) throws Exception {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("station_" + location + "data.txt")));
        for (int j = 0; j < 10000; j++) {
            double factor = Math.sin(j);
            float temp = (float) (38 * factor);
            out.println("" + location + ":" + "" + temp + ":" + "" + j);
        }
        out.flush();
        out.close();
    }

    public static void CosVarieingTemrature(int location) throws Exception {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("station_" + location + "data.txt")));
        for (int j = 0; j < 10000; j++) {
            double factor = Math.cos(j);
            float temp = (float) (38 * factor);
            out.println("" + location + ":" + "" + temp + ":" + "" + j);
        }
        out.flush();
        out.close();
    }
}
