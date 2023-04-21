package mil.army.usace.ehlschlaeger.digitalpopulations;

import java.io.IOException;
import java.util.LinkedList;
import mil.army.usace.ehlschlaeger.rgik.core.CSVTable;
import mil.army.usace.ehlschlaeger.rgik.core.DataException;
import mil.army.usace.ehlschlaeger.rgik.core.GISClass;
import mil.army.usace.ehlschlaeger.rgik.core.GISGrid;
import mil.army.usace.ehlschlaeger.rgik.core.GISLattice;
import mil.army.usace.ehlschlaeger.rgik.core.GISPoint;
import mil.army.usace.ehlschlaeger.rgik.core.GISPointQuadTree;
import mil.army.usace.ehlschlaeger.rgik.core.RGISData;
import mil.army.usace.ehlschlaeger.rgik.util.DistanceDecay;
import mil.army.usace.ehlschlaeger.rgik.util.LinearDecay;
import mil.army.usace.ehlschlaeger.rgik.util.TransformDouble;

/**
 *  Copyright Charles R. Ehlschlaeger,
 *  work: 309-298-1841, fax: 309-298-3003,
 *  <http://faculty.wiu.edu/CR-Ehlschlaeger2/>
 *  This software is freely usable for research and educational purposes. Contact C. R. Ehlschlaeger
 *  for permission for other purposes.
 *  Use of this software requires appropriate citation in all published and unpublished documentation.
 */
public class PointClusterDetector {

    private GISPointQuadTree<GISPoint>[] simulatedPopulations;

    private GISPointQuadTree<GISPoint> events;

    private GISLattice eventClusterDensityMean, eventClusterDensitySD;

    private GISLattice eventClusterDistance;

    private GISClass numberSimulatedMoreExtreme;

    public PointClusterDetector() {
        throw new IllegalArgumentException("Zero-arg constructor not implemented.  PumsHouseholds.java.deprecated shows how.");
    }

    public PointClusterDetector(CSVTable eventTable, String populationFilePrefix, int numberPopulationFiles, double maximumClusterDistance, double incrementDistance, GISGrid grid2check, double distanceDecayExponent, boolean detectMaximumClusters) throws IOException {
        assert eventTable != null;
        assert maximumClusterDistance >= 0.0;
        assert incrementDistance > 0.0;
        assert grid2check != null;
        if (numberPopulationFiles < 2) {
            throw new IllegalArgumentException("numberPopulationFiles < 2.  The number of populations should be REALLY LARGE for an accuracy assessment.");
        }
        TransformDouble td = null;
        if (distanceDecayExponent > .99999 && distanceDecayExponent < 1.00001) {
            td = new LinearDecay();
        } else {
            td = new DistanceDecay(distanceDecayExponent);
        }
        grid2check.print();
        System.out.println("Max cluster dist: " + maximumClusterDistance);
        System.out.println("Number of events: " + eventTable.getRowCount());
        eventClusterDensityMean = new GISLattice(grid2check);
        eventClusterDensitySD = new GISLattice(grid2check);
        eventClusterDistance = new GISLattice(grid2check);
        numberSimulatedMoreExtreme = new GISClass(grid2check);
        events = GISPointQuadTree.load(eventTable, "x", "y", null, null, 10);
        int numEvents = events.getNumberPointsIncludingSubNodes();
        eventTable = null;
        simulatedPopulations = new GISPointQuadTree[numberPopulationFiles];
        double[] simPopIntensity = new double[numberPopulationFiles];
        for (int i = numberPopulationFiles - 1; i >= 0; i--) {
            CSVTable test = new CSVTable(populationFilePrefix + i + ".txt");
            if (test.findColumn("x") < 0 || test.findColumn("y") < 0) {
                throw new DataException(populationFilePrefix + i + "txt not formated properly.");
            }
            simulatedPopulations[i] = GISPointQuadTree.load(test, "x", "y", null, null, 10);
            simPopIntensity[i] = ((double) numEvents) / simulatedPopulations[i].getNumberPointsIncludingSubNodes();
            test = null;
        }
        int distances2do = (int) Math.ceil(maximumClusterDistance / incrementDistance);
        double[] eventCount = new double[distances2do];
        double[] eventDensity = new double[numberPopulationFiles];
        for (int r = grid2check.getNumberRows() - 1; r >= 0; r--) {
            double northing = grid2check.getCellCenterNorthing(r, 0);
            for (int c = grid2check.getNumberColumns() - 1; c >= 0; c--) {
                double easting = grid2check.getCellCenterEasting(r, c);
                LinkedList<GISPoint> closePoints = events.getPoints(new GISPoint(easting, northing), maximumClusterDistance);
                int lag = distances2do - 1;
                for (double d = maximumClusterDistance; d > 0; d -= incrementDistance) {
                    eventCount[lag] = 0.0;
                    LinkedList<GISPoint> closerPoints = new LinkedList<GISPoint>();
                    GISPoint p = closePoints.remove();
                    double nextClusterDistance = maximumClusterDistance - incrementDistance;
                    while (p != null) {
                        double distanceP = RGISData.distance(p, easting, northing);
                        double weight = td.getDouble(distanceP / d);
                        eventCount[lag] += weight;
                        if (distanceP <= nextClusterDistance) {
                            closerPoints.add(p);
                        }
                        p = closePoints.remove();
                    }
                    closePoints = closerPoints;
                    lag--;
                }
                int bestNumberMoreExtreme = -1;
                double bestDensityMean = Double.POSITIVE_INFINITY;
                double bestDensitySD = 0.0;
                double bestDistance = -1.0;
                lag = distances2do - 1;
                for (double d = maximumClusterDistance; d > 0; d -= incrementDistance) {
                    int countMoreExtreme = 0;
                    for (int i = numberPopulationFiles - 1; i >= 0; i--) {
                        GISPoint pp = new GISPoint(easting, northing);
                        closePoints = simulatedPopulations[i].getPoints(pp, d);
                        GISPoint p = closePoints.remove();
                        double popCount = 0.0;
                        double simulatedEventCount = 0.0;
                        while (p != null) {
                            double distanceP = RGISData.distance(p, easting, northing);
                            double weight = td.getDouble(distanceP / d);
                            popCount += weight;
                            if (Math.random() < simPopIntensity[i]) simulatedEventCount += weight;
                            p = closePoints.remove();
                        }
                        eventDensity[i] = eventCount[lag] / popCount;
                        if (detectMaximumClusters == true) {
                            if (eventCount[lag] > simulatedEventCount) {
                                countMoreExtreme++;
                            }
                        } else {
                            if (eventCount[lag] < simulatedEventCount) {
                                countMoreExtreme++;
                            }
                        }
                    }
                    if (countMoreExtreme > bestNumberMoreExtreme) {
                        bestNumberMoreExtreme = countMoreExtreme;
                        bestDistance = d;
                        bestDensityMean = 0.0;
                        for (int j = 0; j <= numberPopulationFiles - 1; j++) {
                            bestDensityMean += eventDensity[j];
                        }
                        bestDensityMean /= numberPopulationFiles;
                        bestDensitySD = 0.0;
                        for (int j = 0; j <= numberPopulationFiles - 1; j++) {
                            bestDensitySD += (eventDensity[j] - bestDensityMean) * (eventDensity[j] - bestDensityMean);
                        }
                        bestDensitySD = Math.sqrt(bestDensitySD);
                    }
                    lag--;
                }
                eventClusterDensityMean.setCellValue(r, c, bestDensityMean);
                eventClusterDensitySD.setCellValue(r, c, bestDensitySD);
                eventClusterDistance.setCellValue(r, c, bestDistance);
                numberSimulatedMoreExtreme.setCellValue(r, c, bestNumberMoreExtreme);
            }
        }
        eventClusterDensityMean.writeAsciiEsri("000densityMean");
        eventClusterDensitySD.writeAsciiEsri("000densitySD");
        eventClusterDistance.writeAsciiEsri("000ClusterDistance");
        numberSimulatedMoreExtreme.writeAsciiEsri("000numberMoreExtreme");
    }

    public static void main(String argv[]) throws IOException {
        if (argv.length != 8 && argv.length != 0) {
            System.out.println("PointClusterDetector.main ERROR: There must be six parameters:");
            System.out.println(" java -mx####m PointClusterDetector eventTableFile populationTableFilePrefix");
            System.out.println("       numberPopulationFiles maxDistance distanceIncrement checkGrid detectClusters?");
            System.out.println(" where eventTableFile is the full file name of events (easting column must be named x and");
            System.out.println("            northing column must be named y),");
            System.out.println("       populationTableFilePrefix is the file name prefix of possible populations with format prefix##.txt");
            System.out.println("            (name0..name## with ## = numPopFiles - 1, easting column must be named x and");
            System.out.println("            northing column must be named y),");
            System.out.println("       numberPopulationFiles is the number of possible populations,");
            System.out.println("       maxDistance is the maximum distance to check clustering relationships,");
            System.out.println("       distanceIncrement is the incremental distance to check clustering relationships,");
            System.out.println("       checkGrid is the grid of cells to check across study area, and");
            System.out.println("       detectClusters? should be true or false indicating whether looking for clusters");
            System.out.println("            (true) or void areas (false)");
            System.exit(-1);
        } else if (argv.length == 8) {
            String popPrefix = argv[1];
            int numPop = new Integer(argv[2].trim()).intValue();
            double maxDist = new Double(argv[3].trim()).doubleValue();
            GISGrid grid2check = GISGrid.loadEsriAscii(argv[5]);
            double incDist = new Double(argv[4].trim()).doubleValue();
            double distDecay = new Double(argv[6].trim()).doubleValue();
            boolean maxCluster = true;
            if (argv[7] == "false") {
                maxCluster = false;
            }
            CSVTable t = new CSVTable(argv[0]);
            new PointClusterDetector(t, popPrefix, numPop, maxDist, incDist, grid2check, distDecay, maxCluster);
        } else {
            new PointClusterDetector();
        }
        System.exit(0);
    }
}
