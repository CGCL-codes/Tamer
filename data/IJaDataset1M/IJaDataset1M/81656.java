package edu.tufts.cs.geometry.proxtools;

import java.io.*;
import javax.vecmath.*;
import edu.tufts.cs.geometry.*;
import edu.tufts.cs.geometry.datadepth.*;

public class ProxTools {

    public static final String VERSION = "1.0a1 Preview Release";

    public static final String NAME = "ProxTools (c) 2007 John Hugg";

    public static final String USAGE = "\n" + NAME + "\n" + " Input:      A multidimesnional data set\n" + " Output:     Varies depending on calculation\n" + "             See -h option\n" + " Parameters: -v print the version number for this software\n" + "             -h if not followed by a calculation name, print this usage\n" + "                if followed by a calculation name, print description\n" + "             -i specifiy an input filename\n" + "                default is to read from stdin\n" + "                example: \"-i input.txt\"\n" + "             -o specify an output filename\n" + "                default is to write to stdout\n" + "                example: \"-o output.txt\"\n" + "             -p specify the type of proximity graph\n" + "                options: delaunay, gabriel, beta\n" + "                default is \"delauany\"\n" + "                beta graphs must specify beta value as in example\n" + "                example 1: \"-p gabriel\"\n" + "                        2: \"-p beta:0.5\"\n" + "             -c specify the name of the calculation\n" + "                options: edges, depths, seeds\n" + "                default is \"edges\"\n";

    public static void printUsage(String errMsg) {
        System.out.println("Error: " + errMsg);
        System.out.println(USAGE);
        System.exit(0);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        InputStream input = System.in;
        String calculationName = "edges";
        String proximityGraphName = "delaunay";
        OutputStream output = System.out;
        double betaValue = 1.0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                System.out.println(NAME);
                System.out.println(VERSION);
                System.exit(0);
            }
            if (args[i].equals("-i")) {
                if (++i >= args.length) printUsage("incorrect input after -i");
                File f = new File(args[i]);
                try {
                    input = new FileInputStream(f);
                } catch (Exception e) {
                    printUsage("couldn't open input file");
                }
            } else if (args[i].equals("-o")) {
                if (++i >= args.length) printUsage("incorrect input after -o");
                File f = new File(args[i]);
                try {
                    output = new FileOutputStream(f);
                } catch (Exception e) {
                    printUsage("couldn't open output file");
                }
            } else if (args[i].equals("-p")) {
                if (++i >= args.length) printUsage("incorrect input after -p");
                proximityGraphName = args[i];
            } else if (args[i].equals("-c")) {
                if (++i >= args.length) printUsage("incorrect input after -c");
                calculationName = args[i];
                if (calculationName.equals("edges")) continue;
                if (calculationName.equals("depths")) continue;
                if (calculationName.equals("seeds")) continue;
                printUsage("unknown calculation");
            } else printUsage("unknown or missing command line argument(s)");
        }
        if (proximityGraphName.startsWith("beta")) {
            String[] parts = proximityGraphName.split(":");
            if (parts.length != 2) printUsage("can't parse specified beta value");
            proximityGraphName = parts[0];
            betaValue = Double.parseDouble(parts[1]);
        }
        String inputData = Utilities.readInput(input);
        double[][] dataTemp = NumericArrayParser.parse(inputData);
        GMatrix data = Utilities.gmatrixFromMultiArray(dataTemp);
        PrintStream printOut = new PrintStream(output);
        if (calculationName.equals("edges")) {
            ProximityGraph pg = null;
            if (proximityGraphName.equals("delaunay")) pg = new PGDelaunay(); else if (proximityGraphName.equals("gabriel")) pg = new PGGabriel(); else if (proximityGraphName.equals("beta")) {
                pg = new PGBeta();
                ((PGBeta) pg).setBetaValue(betaValue);
            } else printUsage("unrecognized proximity graph type");
            pg.setData(data);
            pg.computeGraph();
            for (int i = 0; i < data.getNumRow(); i++) {
                int[] edges = pg.getConnectedVertices(i);
                if (edges.length == 0) System.out.println("Not a complete graph.");
            }
            printOut.println(pg.getEdgeCount());
        } else {
            ProximityDepth pd = new ProximityDepth();
            if (proximityGraphName.equals("delaunay")) pd.setParameter("delaunay");
            if (proximityGraphName.equals("gabriel")) pd.setParameter("gabriel");
            if (proximityGraphName.startsWith("beta")) pd.setParameter("beta:" + String.valueOf(betaValue));
            if (calculationName.equals("depths")) {
                double[] depths = pd.getDepthValues(data);
                for (double depth : depths) printOut.println(depth);
            } else if (calculationName.equals("seeds")) {
                int[][] seeds = pd.getSeeds(data);
                printOut.println(seeds.length);
                for (int[] seedSet : seeds) {
                    for (int seed : seedSet) printOut.printf("%d ", seed);
                    printOut.println();
                }
            }
        }
    }
}
