package de.tuberlin.cs.ni.neubauer.mpcd;

import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import de.tuberlin.cs.ni.neubauer.mpcd.assignments.CommunityAssignment;
import de.tuberlin.cs.ni.neubauer.mpcd.assignments.CommunityAssignments;
import de.tuberlin.cs.ni.neubauer.mpcd.io.DataContainer;
import de.tuberlin.cs.ni.neubauer.mpcd.io.FileImporter;

public class MPCDExperimental {

    public static DataContainer miniData(int index) {
        int[][] x = null;
        switch(index) {
            case 0:
                int[][] x2 = { { 0, 0, 0 }, { 0, 0, 1 }, { 1, 0, 1 } };
                x = x2;
                break;
            case 1:
                int[][] x3 = { { 0, 0, 0 }, { 1, 1, 1 }, { 0, 1, 0 }, { 1, 0, 1 }, { 2, 2, 2 }, { 3, 3, 3 }, { 2, 3, 2 }, { 3, 2, 3 } };
                x = x3;
                break;
            case 2:
                int[][] x4 = { { 1, 1, 1 }, { 10, 10, 10 } };
                x = x4;
                break;
            case 3:
                int[][] x5 = { { 0, 0 }, { 1, 1 } };
                x = x5;
                break;
        }
        ArrayList<int[]> al = new ArrayList<int[]>(x.length);
        for (int[] y : x) al.add(y);
        return new DataContainer(al);
    }

    public static DataContainer sampleFiles(int index) throws Exception {
        String filename = "";
        switch(index) {
            case 0:
                filename = "C:\\Users\\nn\\Desktop\\workspace\\Python\\Tagging\\output\\synthetic\\caveman\\simple\\10_10_1000_0.999\\edges.txt";
                break;
            case 1:
                filename = "C:\\Users\\nn\\Desktop\\workspace\\Python\\Tagging\\output\\synthetic\\caveman\\simple\\2_10_200_1.000\\edges.txt";
                break;
            case 2:
                filename = "C:\\Users\\nn\\Desktop\\workspace\\Python\\Tagging\\output\\synthetic\\caveman\\simple\\4_4_80_1.000\\edges.txt";
                break;
            case 3:
                filename = "C:\\files\\workspace\\python\\Tagging\\output\\synthetic\\caveman\\simple\\2_30_600_0.900\\edges.txt";
                break;
            case 4:
                filename = "C:\\files\\workspace\\python\\Tagging\\output\\bibsonomy_nospam\\user\\177\\expand_200_2000\\c1\\edges.txt";
                break;
            case 5:
                filename = "c:\\files\\workspace\\python\\tagging\\output\\synthetic\\caveman\\simple\\20_10_2000_0.950\\edges.txt";
                break;
            case 6:
                filename = "c:\\files\\workspace\\python\\tagging\\output\\bibsonomy_nospam\\user\\177\\expand_200_2000\\c1\\edges.txt";
                break;
            case 7:
                filename = "F:\\files\\workspace\\Python\\Tagging\\output\\bibsonomy_nospam\\tag\\41385\\randomwalk\\50_73295_c1\\clustering\\edges.txt";
                break;
        }
        return new FileImporter(filename).getEdges(null);
    }

    public static void test() throws Exception {
        DataContainer al = null;
        CommunityAssignments result = null;
        CommunityAssignments result2 = null;
        for (int i = 0; i < 1; i++) {
            al = sampleFiles(3);
            result = MPCD.getResult(al, null);
            result2 = MPCD.getResultOptimized(al, null);
        }
        System.out.println("final modularity value of " + result.getValue());
        System.out.println("final modularity value op " + result2.getValue());
        for (int dimension = 0; dimension < result.assignments.length; dimension++) {
            System.out.println("Dimension " + dimension);
            CommunityAssignment cas = result.assignments[dimension];
            for (Map.Entry<Integer, Integer> e : cas.byNode().entrySet()) {
                int node = e.getKey();
                int comm = e.getValue();
                System.out.println("  " + node + ": " + comm);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String edgefile = "C:\\files\\workspace\\python\\Tagging\\output\\delicious\\usertag\\196015_134\\2\\c1__du3__tu3__ut3__dt3__td3__ud3\\edges.txt";
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(new Options(), args);
        DataContainer data = new FileImporter(edgefile).getEdges(cmd);
        data.dumpNewToOldMap("c:\\files\\map");
    }
}
