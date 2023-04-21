package mil.army.usace.ehlschlaeger.digitalpopulations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import mil.army.usace.ehlschlaeger.rgik.core.CSVTable;
import mil.army.usace.ehlschlaeger.rgik.core.ClassCrop;
import mil.army.usace.ehlschlaeger.rgik.core.CumulativeDistributionFunction;
import mil.army.usace.ehlschlaeger.rgik.core.DataException;
import mil.army.usace.ehlschlaeger.rgik.core.GISClass;
import mil.army.usace.ehlschlaeger.rgik.core.GISLattice;
import mil.army.usace.ehlschlaeger.rgik.core.GISPoint;
import mil.army.usace.ehlschlaeger.rgik.core.GISPointQuadTree;
import mil.army.usace.ehlschlaeger.rgik.core.GISPointQuadTreeBreadthFirstIterator;
import mil.army.usace.ehlschlaeger.rgik.core.PointSpatialStatistic;
import mil.army.usace.ehlschlaeger.rgik.core.Proportion;
import mil.army.usace.ehlschlaeger.rgik.core.Reclass;

/**
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 * 
 * @author Chuck Ehlschlaeger
 */
public class ConflatePumsTracts {

    private GISPointQuadTree[] qts;

    public static final int AGE_UNDER5 = 1;

    public static final int AGE_5_17 = 2;

    public static final int AGE_18_21 = 3;

    public static final int AGE_22_29 = 4;

    public static final int AGE_30_39 = 5;

    public static final int AGE_40_49 = 6;

    public static final int AGE_50_64 = 7;

    public static final int AGE_65_UP = 8;

    public static final int MED_AGE_M = 11;

    public static final int MED_AGE_F = 12;

    public static final int AVE_HH_SZ = 14;

    public static final int HSEHLD_1_M = 15;

    public static final int HSEHLD_1_F = 16;

    public static final int MARHH_CHD = 17;

    public static final int MARHH_NO_C = 18;

    public static final int MHH_CHILD = 19;

    public static final int FHH_CHILD = 20;

    public static final int FAMILIES = 21;

    public static final int AVE_FAM_SZ = 22;

    public static final int MED_AGE = 23;

    public static final int MALES = 24;

    public static final int FEMALES = 25;

    public static final int OWNER_OCC = 31;

    public static final int RENTER_OCC = 32;

    public static final int WHITE = 41;

    public static final int BLACK = 42;

    public static final int AMERI_ES = 43;

    public static final int ASIAN = 44;

    public static final int HAWN_PI = 45;

    public static final int OTHER = 46;

    public static final int MULT_RACE = 47;

    public static final int HISPANIC = 48;

    public String[] variableNames = { "", "AGE_UNDER5", "AGE_5_17", "AGE_18_21", "AGE_22_29", "AGE_30_39", "AGE_40_49", "AGE_50_64", "AGE_65_UP", "", "", "MED_AGE_M", "MED_AGE_F", "", "AVE_HH_SZ", "HSEHLD_1_M", "HSEHLD_1_F", "MARHH_CHD", "MARHH_NO_C", "MHH_CHILD", "FHH_CHILD", "FAMILIES", "AVE_FAM_SZ", "MED_AGE", "MALES", "FEMALES", "", "", "", "", "", "OWNER_OCC", "RENTER_OCC", "", "", "", "", "", "", "", "", "WHITE", "BLACK", "AMERI_ES", "ASIAN", "HAWN_PI", "OTHER", "MULT_RACE", "HISPANIC" };

    public GISPointQuadTree[] getQuadTrees() {
        return qts;
    }

    public ConflatePumsTracts(String nlcdMapName, String tractsMapName, String tractsTableName, String tractsKeyColumnName, String pumsHouseholdTableName, String pumsPopulationTableName, double hours2save, int numberMapRealizations) throws IOException {
        Random r = new Random();
        long rs = r.nextLong();
        new ConflatePumsTracts(nlcdMapName, tractsMapName, tractsTableName, tractsKeyColumnName, pumsHouseholdTableName, pumsPopulationTableName, hours2save, rs, 1.0, null, null, numberMapRealizations);
    }

    /** otherStatistics is an array of variable indices that the user would want ConflatePumsTracts to 
	 *  match. weightOtherStatistics is an array of doubles with values between 0-1 to indicate how much 
	 *  importance to give to each of the otherStatistics.
	 * @throws IOException 
	 */
    public ConflatePumsTracts(String nlcdMapName, String tractsMapName, String tractsTableName, String tractsKeyColumnName, String pumsHouseholdTableName, String pumsPopulationTableName, double hours2save, long randomSeedValue, double weightLocations, double[] weightOtherStatistics, int[] otherStatistics, int numberMapRealizations) throws IOException {
        Date startTime = new Date();
        GISClass lc_map = GISClass.loadEsriAscii(nlcdMapName);
        for (int r = lc_map.getNumberRows() - 1; r >= 0; r--) {
            for (int c = lc_map.getNumberColumns() - 1; c >= 0; c--) {
                if (lc_map.isNoData(r, c) == false) {
                    int value = lc_map.getCellValue(r, c);
                    if (value > 90) {
                        lc_map.setCellValue(r, c, 9);
                    } else if (value > 80) {
                        lc_map.setCellValue(r, c, 8);
                    } else if (value > 70) {
                        lc_map.setCellValue(r, c, 4);
                    } else if (value > 60) {
                        lc_map.setCellValue(r, c, 6);
                    } else if (value > 40) {
                        lc_map.setCellValue(r, c, 4);
                    } else if (value > 30) {
                        lc_map.setCellValue(r, c, 3);
                    } else if (value == 11) {
                        lc_map.setNoData(r, c, true);
                    }
                }
            }
        }
        String newColumnName = "row_number";
        GISClass region = GISClass.loadEsriAscii(tractsMapName);
        CSVTable regionTable = new CSVTable(tractsTableName);
        regionTable.addKeyColumn(newColumnName);
        GISClass newRegionMap = Reclass.reclass(region, regionTable, tractsKeyColumnName, newColumnName);
        int minValue = newRegionMap.getMinimumValue();
        int maxValue = newRegionMap.getMaximumValue();
        if (minValue != 0) {
            throw new DataException("minValue != 0");
        }
        int tracts = regionTable.getRowCount();
        int householdsInAggregated = 0;
        int col = regionTable.findColumn("HSE_UNITS");
        for (int i = tracts - 1; i >= 0; i--) {
            String hiar = regionTable.getStringAt(i, col);
            householdsInAggregated += (new Integer(hiar)).intValue();
        }
        int peopleInAggregated = 0;
        col = regionTable.findColumn("POP2000");
        for (int i = tracts - 1; i >= 0; i--) {
            String np = regionTable.getStringAt(i, col);
            peopleInAggregated += (new Integer(np)).intValue();
        }
        System.out.println("Aggregated data has " + householdsInAggregated + " households and " + peopleInAggregated + " people.");
        System.out.println("");
        GISClass[] classMaps = new GISClass[maxValue + 1];
        CumulativeDistributionFunction[] cdfMaps = new CumulativeDistributionFunction[maxValue + 1];
        LandcoverPopulationDensity lpd = new LandcoverPopulationDensity(lc_map, newRegionMap, regionTable, newColumnName, "HSE_UNITS");
        System.out.println("ConflatePumsTracts.main LandcoverPopulationDensity done.");
        GISLattice pdf = lpd.getPDF();
        for (int i = 0; i <= maxValue; i++) {
            classMaps[i] = new GISClass(newRegionMap, newRegionMap);
            ClassCrop cc = new ClassCrop(i, i);
            classMaps[i] = cc.setInMap(classMaps[i]);
            GISClass smallLC = new GISClass(classMaps[i], lc_map);
            GISLattice smallPDF = new GISLattice(smallLC, pdf);
            GISLattice data4cdf = new GISLattice(smallPDF);
            double sumPDF = 0.0;
            for (int r = 0; r < smallPDF.getNumberRows(); r++) {
                for (int c = 0; c < smallPDF.getNumberColumns(); c++) {
                    if (classMaps[i].isNoData(r, c) == false) {
                        sumPDF += smallPDF.getCellValue(r, c);
                    }
                }
            }
            double thisSum = 0.0;
            for (int r = 0; r < smallPDF.getNumberRows(); r++) {
                for (int c = 0; c < smallPDF.getNumberColumns(); c++) {
                    if (classMaps[i].isNoData(r, c) == false) {
                        thisSum += smallPDF.getCellValue(r, c);
                    }
                    data4cdf.setCellValue(r, c, thisSum / sumPDF);
                }
            }
            cdfMaps[i] = new CumulativeDistributionFunction(data4cdf);
        }
        lc_map = null;
        PointSpatialStatistic[] goalArray = new PointSpatialStatistic[2];
        Proportion rp_goal = new Proportion(newRegionMap, regionTable, newColumnName, "VACANT", "HSE_UNITS");
        System.out.println("Goal Proportion:");
        rp_goal.print();
        System.out.println("");
        goalArray[0] = rp_goal;
        rp_goal = new Proportion(newRegionMap, regionTable, newColumnName, "POP2000", "POP2000");
        System.out.println("Goal Proportion:");
        rp_goal.print();
        System.out.println("");
        goalArray[1] = rp_goal;
        PointSpatialStatistic[] pssArray = new PointSpatialStatistic[2];
        GISPointQuadTree qt = new GISPointQuadTree(newRegionMap.getWestEdge(), newRegionMap.getNorthEdge(), newRegionMap.getEastEdge(), newRegionMap.getSouthEdge(), 10);
        qts = new GISPointQuadTree[1];
        qts[0] = qt;
        PumsHouseholdVacancy2double phv2d = new PumsHouseholdVacancy2double();
        Proportion rp = new Proportion(newRegionMap, minValue, maxValue, qts, phv2d, "VACANT", "HSE_UNITS");
        pssArray[0] = rp;
        PumsHouseholdPeople2double php2d = new PumsHouseholdPeople2double();
        rp = new Proportion(newRegionMap, minValue, maxValue, qts, php2d, "POP2000", "POP2000", php2d);
        pssArray[1] = rp;
        CSVTable households = new CSVTable(pumsHouseholdTableName);
        CSVTable populations = new CSVTable(pumsPopulationTableName);
        int pumsRows = households.getRowCount();
        System.out.println("PUMS data had " + pumsRows + " types of households.");
        System.out.println("");
        System.out.println("Each PUMS household type should have " + (((double) householdsInAggregated) / pumsRows) + " members.");
        PointSpatialStatistic[] otherStatsGoal = null;
        PointSpatialStatistic[] otherStatsMaps = null;
        if (otherStatistics != null) {
            if (weightOtherStatistics == null) {
                throw new IllegalArgumentException("weightOtherStatistics == null");
            }
            if (weightOtherStatistics.length != otherStatistics.length) {
                throw new IllegalArgumentException("weightOtherStatistics.length != otherStatistics.length");
            }
            otherStatsGoal = new PointSpatialStatistic[otherStatistics.length];
            otherStatsMaps = new PointSpatialStatistic[otherStatistics.length];
            for (int i = 0; i < otherStatistics.length; i++) {
                switch(otherStatistics[i]) {
                    case 1:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_UNDER5", "POP2000");
                        PumsHouseholdAge2double pha00042d = new PumsHouseholdAge2double(0, 4);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha00042d, "AGE_UNDER5", "POP2000", php2d);
                        break;
                    case 2:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_5_17", "POP2000");
                        PumsHouseholdAge2double pha05172d = new PumsHouseholdAge2double(5, 17);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha05172d, "AGE_5_17", "POP2000", php2d);
                        break;
                    case 3:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_18_21", "POP2000");
                        PumsHouseholdAge2double pha18212d = new PumsHouseholdAge2double(18, 21);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha18212d, "AGE_18_21", "POP2000", php2d);
                        break;
                    case 4:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_22_29", "POP2000");
                        PumsHouseholdAge2double pha22292d = new PumsHouseholdAge2double(22, 29);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha22292d, "AGE_22_29", "POP2000", php2d);
                        break;
                    case 5:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_30_39", "POP2000");
                        PumsHouseholdAge2double pha30392d = new PumsHouseholdAge2double(30, 39);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha30392d, "AGE_30_39", "POP2000", php2d);
                        break;
                    case 6:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_40_49", "POP2000");
                        PumsHouseholdAge2double pha40492d = new PumsHouseholdAge2double(40, 49);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha40492d, "AGE_40_49", "POP2000", php2d);
                        break;
                    case 7:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_50_64", "POP2000");
                        PumsHouseholdAge2double pha50642d = new PumsHouseholdAge2double(50, 64);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha50642d, "AGE_50_64", "POP2000", php2d);
                        break;
                    case 8:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AGE_65_UP", "POP2000");
                        PumsHouseholdAge2double pha65_2d = new PumsHouseholdAge2double(65, Integer.MAX_VALUE);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, pha65_2d, "AGE_65_UP", "POP2000", php2d);
                        break;
                    case 24:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "MALES", "POP2000");
                        PumsHouseholdGender2double phg2d = new PumsHouseholdGender2double(PumsHouseholdGender2double.MALES);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, phg2d, "MALES", "POP2000", php2d);
                        break;
                    case 25:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "FEMALES", "POP2000");
                        phg2d = new PumsHouseholdGender2double(PumsHouseholdGender2double.FEMALES);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, phg2d, "FEMALES", "POP2000", php2d);
                        break;
                    case 41:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "WHITE", "POP2000");
                        PumsHousehold8Race2double ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.WHITE);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "WHITE", "POP2000", php2d);
                        break;
                    case 42:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "BLACK", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.BLACK);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "BLACK", "POP2000", php2d);
                        break;
                    case 43:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "AMERI_ES", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.AMERI_ES);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "AMERI_ES", "POP2000", php2d);
                        break;
                    case 44:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "ASIAN", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.ASIAN);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "ASIAN", "POP2000", php2d);
                        break;
                    case 45:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "HAWN_PI", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.HAWN_PI);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "HAWN_PI", "POP2000", php2d);
                        break;
                    case 46:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "OTHER", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.OTHER);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "OTHER", "POP2000", php2d);
                        break;
                    case 47:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "MULT_RACE", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.MULT_RACE);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "MULT_RACE", "POP2000", php2d);
                        break;
                    case 48:
                        otherStatsGoal[i] = new Proportion(newRegionMap, regionTable, newColumnName, "HISPANIC", "POP2000");
                        ph8r2d = new PumsHousehold8Race2double(PumsHousehold8Race2double.HISPANIC);
                        php2d = new PumsHouseholdPeople2double();
                        otherStatsMaps[i] = new Proportion(newRegionMap, minValue, maxValue, qts, ph8r2d, "HISPANIC", "POP2000", php2d);
                        break;
                    default:
                        throw new IllegalArgumentException("otherStatistics[" + i + "] not ready yet");
                }
                System.out.println("Goal statistic [" + i + "]:");
                otherStatsGoal[i].print();
                System.out.println("");
            }
        }
        Date dataPrep = new Date();
        double dataPrepMinutes = (dataPrep.getTime() - startTime.getTime()) / (1000.0 * 60.0);
        System.out.println("data preparation took " + dataPrepMinutes + " minutes");
        new ConflatePumsTracts(classMaps, cdfMaps, households, populations, qts, goalArray, pssArray, householdsInAggregated, peopleInAggregated, hours2save, randomSeedValue, weightLocations, weightOtherStatistics, otherStatsGoal, otherStatsMaps, numberMapRealizations, otherStatistics);
    }

    public ConflatePumsTracts(GISClass[] regions, CumulativeDistributionFunction[] cdfs, CSVTable households, CSVTable populations, GISPointQuadTree[] emptyQuadTrees, PointSpatialStatistic[] goalStatistics, PointSpatialStatistic[] mapsStatistics, int householdsInAggregated, int peopleInAggregated, double hours2save, long randomSeedValue, double weightLocations, double[] weightOtherStatistics, PointSpatialStatistic[] otherStatsGoal, PointSpatialStatistic[] otherStatisticsMaps, int numberMapRealizations, int[] otherStatistics) throws IOException {
        qts = emptyQuadTrees;
        if (weightLocations < 0.0) {
            throw new IllegalArgumentException("negative value in weightLocations");
        }
        if (weightOtherStatistics != null) {
            if (otherStatsGoal == null) {
                throw new IllegalArgumentException("otherStats == null");
            }
            if (otherStatsGoal.length != weightOtherStatistics.length) {
                throw new IllegalArgumentException("otherStatsGoal.length != weightOtherStatistics.length");
            }
            double[] oStats = new double[otherStatsGoal.length];
            for (int i = 0; i < weightOtherStatistics.length; i++) {
                if (weightOtherStatistics[i] < 0.0) {
                    throw new IllegalArgumentException("weightOtherStatistics[" + i + "] < 0.0");
                }
                oStats[i] = weightOtherStatistics[i] * weightOtherStatistics[i];
            }
            weightOtherStatistics = oStats;
        }
        weightLocations *= weightLocations;
        double nsRes = cdfs[0].getNSResolution();
        double ewRes = cdfs[0].getEWResolution();
        int numHouseholdArchtypes = households.getRowCount();
        for (int mrDone = 0; mrDone < numberMapRealizations; mrDone++) {
            if (mrDone != 0) {
                for (int i = 0; i < emptyQuadTrees.length; i++) {
                    emptyQuadTrees[i].deleteAllPoints();
                }
            }
            Date startTime = new Date();
            System.out.println("");
            System.out.println("ConflatePumsTracts.ConflatePumsTracts realization [" + mrDone + "] start at " + startTime.toString());
            System.out.println("");
            PointSpatialStatistic[] mapsSS = null;
            if (mapsStatistics != null) {
                mapsSS = new PointSpatialStatistic[mapsStatistics.length];
                for (int i = 0; i < mapsStatistics.length; i++) {
                    mapsSS[i] = (PointSpatialStatistic) mapsStatistics[i].createCopy();
                }
            }
            PointSpatialStatistic[] otherStatsMaps = null;
            if (otherStatisticsMaps != null) {
                otherStatsMaps = new PointSpatialStatistic[otherStatisticsMaps.length];
                for (int i = 0; i < otherStatsMaps.length; i++) {
                    otherStatsMaps[i] = (PointSpatialStatistic) otherStatisticsMaps[i].createCopy();
                }
            }
            PumsHousehold[] phs = new PumsHousehold[numHouseholdArchtypes];
            int peopleInArchTypes = 0;
            for (int r = 0; r < numHouseholdArchtypes; r++) {
                int[] householdValues = new int[PumsHousehold.validVariables.length];
                for (int v = 0; v < PumsHousehold.validVariables.length; v++) {
                    String s = households.getStringAt(r, v);
                    int value = Integer.MIN_VALUE;
                    if (s.length() > 0) {
                        try {
                            value = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                        }
                    }
                    if (value == Integer.MIN_VALUE) {
                        householdValues[v] = PumsHousehold.blankValues[v];
                    } else {
                        householdValues[v] = value;
                    }
                }
                phs[r] = new PumsHousehold(householdValues, populations);
                peopleInArchTypes += phs[r].getVariableValue(PumsHousehold.NP);
            }
            System.out.println("");
            System.out.println("Ratio of households Agg / Pums: " + (numHouseholdArchtypes * 1.0 / householdsInAggregated));
            System.out.println("    Ratio of people Agg / Pums: " + (peopleInArchTypes * 1.0 / peopleInAggregated));
            System.out.println("");
            int[] numArchtypes2Make = new int[numHouseholdArchtypes];
            for (int j = 0; j < numHouseholdArchtypes - 1; j++) {
                numArchtypes2Make[j] = 100;
            }
            double bestfit = 0.0;
            double[] otherGoalSums = new double[otherStatsGoal.length];
            double[][] mapHStats = new double[otherStatsGoal.length][numHouseholdArchtypes];
            for (int i = 0; i < otherStatsGoal.length; i++) {
                otherGoalSums[i] = otherStatsGoal[i].getSum();
                double otherMapSums = 0.0;
                for (int j = 0; j < numHouseholdArchtypes; j++) {
                    mapHStats[i][j] = otherStatisticsMaps[i].getCount((GISPoint) new PumsHouseholdRealization(phs[j], 0, 0.0, 0.0));
                    otherMapSums += numArchtypes2Make[j] * mapHStats[i][j];
                }
                bestfit += (otherGoalSums[i] - otherMapSums) * (otherGoalSums[i] - otherMapSums) * weightOtherStatistics[i];
                System.out.println("otherStat [" + i + "]: Goal: " + otherGoalSums[i] + ", Number in Archtypes: " + otherMapSums + ", ratio: " + (otherGoalSums[i] / otherMapSums) + " Variable: " + variableNames[otherStatistics[i]]);
            }
            double[][] mapForcedHStats = new double[goalStatistics.length][numHouseholdArchtypes];
            double[] goalSums = new double[goalStatistics.length];
            for (int i = 0; i < goalStatistics.length; i++) {
                goalSums[i] = goalStatistics[i].getSum();
                double mapSums = 0.0;
                for (int j = 0; j < numHouseholdArchtypes; j++) {
                    mapForcedHStats[i][j] = mapsStatistics[i].getCount((GISPoint) new PumsHouseholdRealization(phs[j], 0, 0.0, 0.0));
                    mapSums += numArchtypes2Make[j] * mapForcedHStats[i][j];
                }
                bestfit += (goalSums[i] - mapSums) * (goalSums[i] - mapSums) * weightLocations;
                System.out.println("stat [" + i + "]: Goal: " + goalSums[i] + ", Number in Archtypes: " + mapSums + ", ratio: " + (goalSums[i] / mapSums));
            }
            System.out.println("fitness: " + bestfit);
            System.out.println("");
            boolean checkedAll = false;
            Date startArchCount = new Date();
            System.out.println("Starting the process of determining number of archtypes at " + startArchCount);
            int changes = 0;
            while (checkedAll == false) {
                Date nowTime = new Date();
                if (nowTime.getTime() - startArchCount.getTime() > 60000) {
                    startArchCount = nowTime;
                    System.out.println("changes: " + changes + ", fitness: " + bestfit);
                }
                int ch = (int) (Math.floor(numHouseholdArchtypes * Math.random()));
                int lastCH = ch;
                do {
                    double thisfit = 0.0;
                    for (int i = 0; i < otherStatsGoal.length; i++) {
                        double otherMapSums = 0.0;
                        for (int j = 0; j < numHouseholdArchtypes; j++) {
                            if (j == ch) {
                                otherMapSums += (numArchtypes2Make[j] + 1) * mapHStats[i][j];
                            } else {
                                otherMapSums += numArchtypes2Make[j] * mapHStats[i][j];
                            }
                        }
                        thisfit += (otherGoalSums[i] - otherMapSums) * (otherGoalSums[i] - otherMapSums) * weightOtherStatistics[i];
                    }
                    for (int i = 0; i < goalStatistics.length; i++) {
                        double mapSums = 0.0;
                        for (int j = 0; j < numHouseholdArchtypes; j++) {
                            if (j == ch) {
                                mapSums += (numArchtypes2Make[j] + 1) * mapForcedHStats[i][j];
                            } else {
                                mapSums += numArchtypes2Make[j] * mapForcedHStats[i][j];
                            }
                        }
                        thisfit += (goalSums[i] - mapSums) * (goalSums[i] - mapSums) * weightLocations;
                    }
                    if (thisfit < bestfit) {
                        numArchtypes2Make[ch]++;
                        bestfit = thisfit;
                        lastCH = ch;
                        changes++;
                    } else if (numArchtypes2Make[ch] > 17) {
                        thisfit = 0.0;
                        for (int i = 0; i < otherStatsGoal.length; i++) {
                            double otherMapSums = 0.0;
                            for (int j = 0; j < numHouseholdArchtypes; j++) {
                                if (j == ch) {
                                    otherMapSums += (numArchtypes2Make[j] - 1) * mapHStats[i][j];
                                } else {
                                    otherMapSums += numArchtypes2Make[j] * mapHStats[i][j];
                                }
                            }
                            thisfit += (otherGoalSums[i] - otherMapSums) * (otherGoalSums[i] - otherMapSums) * weightOtherStatistics[i];
                        }
                        for (int i = 0; i < goalStatistics.length; i++) {
                            double mapSums = 0.0;
                            for (int j = 0; j < numHouseholdArchtypes; j++) {
                                if (j == ch) {
                                    mapSums += (numArchtypes2Make[j] - 1) * mapForcedHStats[i][j];
                                } else {
                                    mapSums += numArchtypes2Make[j] * mapForcedHStats[i][j];
                                }
                            }
                            thisfit += (goalSums[i] - mapSums) * (goalSums[i] - mapSums) * weightLocations;
                        }
                        if (thisfit < bestfit) {
                            numArchtypes2Make[ch]--;
                            bestfit = thisfit;
                            lastCH = ch;
                            changes++;
                        } else {
                            ch--;
                            if (ch < 0) {
                                ch = numHouseholdArchtypes - 1;
                            }
                            if (lastCH == ch) {
                                checkedAll = true;
                            }
                        }
                    } else {
                        ch--;
                        if (ch < 0) {
                            ch = numHouseholdArchtypes - 1;
                        }
                        if (lastCH == ch) {
                            checkedAll = true;
                        }
                    }
                } while (lastCH != ch);
            }
            Date endArchCount = new Date();
            System.out.println("End of process of determining number of archtypes at " + endArchCount);
            System.out.println("");
            for (int j = 0; j < numArchtypes2Make.length; j++) {
                System.out.print(numArchtypes2Make[j] + " ");
            }
            System.out.println("");
            bestfit = 0.0;
            for (int i = 0; i < otherStatsGoal.length; i++) {
                otherGoalSums[i] = otherStatsGoal[i].getSum();
                double otherMapSums = 0.0;
                for (int j = 0; j < numHouseholdArchtypes; j++) {
                    otherMapSums += numArchtypes2Make[j] * mapHStats[i][j];
                }
                bestfit += (otherGoalSums[i] - otherMapSums) * (otherGoalSums[i] - otherMapSums) * weightOtherStatistics[i];
                System.out.println("otherStat [" + i + "]: Goal: " + otherGoalSums[i] + ", Number in Archtypes: " + otherMapSums + ", ratio: " + (otherGoalSums[i] / otherMapSums) + " Variable : " + variableNames[otherStatistics[i]]);
            }
            for (int i = 0; i < goalStatistics.length; i++) {
                goalSums[i] = goalStatistics[i].getSum();
                double mapSums = 0.0;
                for (int j = 0; j < numHouseholdArchtypes; j++) {
                    mapSums += numArchtypes2Make[j] * mapForcedHStats[i][j];
                }
                bestfit += (goalSums[i] - mapSums) * (goalSums[i] - mapSums) * weightLocations;
                System.out.println("stat [" + i + "]: Goal: " + goalSums[i] + ", Number in Archtypes: " + mapSums + ", ratio: " + (goalSums[i] / mapSums));
            }
            mapForcedHStats = null;
            mapHStats = null;
            System.out.println("New best fit: " + bestfit);
            int pDoneLast = 0;
            int householdsDone = 0;
            int households2do = 0;
            for (int j = 0; j < numHouseholdArchtypes; j++) {
                households2do += numArchtypes2Make[j];
            }
            int peopleDone = 0;
            while (householdsDone < households2do) {
                PumsHouseholdRealization bestPHR = null;
                double bestFit = Double.MAX_VALUE;
                PointSpatialStatistic[] bestSS = null;
                PointSpatialStatistic[] bestOSS = null;
                int bestI = -1;
                int bestH = -1;
                int h = -1;
                int realizationNumber = -1;
                double bestRatio = Double.MAX_VALUE;
                for (int j = 0; j < 5; j++) {
                    int thisH = (int) (Math.floor(numHouseholdArchtypes * Math.random()));
                    realizationNumber = phs[thisH].getNumberRealizations();
                    while (realizationNumber > numArchtypes2Make[thisH]) {
                        thisH--;
                        if (thisH < 0) {
                            thisH = numHouseholdArchtypes - 1;
                        }
                        realizationNumber = phs[thisH].getNumberRealizations();
                    }
                    double thisRatio = ((double) realizationNumber) / numArchtypes2Make[thisH];
                    if (thisRatio < bestRatio) {
                        h = thisH;
                        bestRatio = thisRatio;
                    }
                }
                for (int i = cdfs.length - 1; i >= 0; i--) {
                    PointSpatialStatistic[] potSS = new PointSpatialStatistic[mapsSS.length];
                    double randomValue = Math.random();
                    int cdfID = cdfs[i].getGridCellID(randomValue);
                    int row = cdfs[i].getGridCellRow(cdfID);
                    int col = cdfs[i].getGridCellColumn(cdfID, row);
                    double newEasting = ((Math.random() - .5) * ewRes + cdfs[i].getCellCenterEasting(row, col));
                    double newNorthing = ((Math.random() - .5) * nsRes + cdfs[i].getCellCenterNorthing(row, col));
                    PumsHouseholdRealization potPHR = new PumsHouseholdRealization(phs[h], realizationNumber, newEasting, newNorthing);
                    for (int numSS = mapsSS.length - 1; numSS >= 0; numSS--) {
                        potSS[numSS] = (PointSpatialStatistic) mapsSS[numSS].createCopy();
                        potSS[numSS].modifySS4NewPt(potPHR, 0);
                    }
                    double potSpread = 0.0;
                    for (int numSS = mapsSS.length - 1; numSS >= 0; numSS--) {
                        double spread = potSS[numSS].spread(goalStatistics[numSS]);
                        potSpread += spread * spread * weightLocations;
                    }
                    PointSpatialStatistic[] potOtherStatsMaps = new PointSpatialStatistic[otherStatsMaps.length];
                    for (int numSS = otherStatsMaps.length - 1; numSS >= 0; numSS--) {
                        potOtherStatsMaps[numSS] = (PointSpatialStatistic) otherStatsMaps[numSS].createCopy();
                        potOtherStatsMaps[numSS].modifySS4NewPt(potPHR, 0);
                    }
                    for (int numSS = potOtherStatsMaps.length - 1; numSS >= 0; numSS--) {
                        double spread = potOtherStatsMaps[numSS].spread(otherStatsGoal[numSS]);
                        potSpread += spread * spread * weightOtherStatistics[numSS];
                    }
                    if (potSpread < bestFit) {
                        bestFit = potSpread;
                        bestPHR = potPHR;
                        bestSS = potSS;
                        bestOSS = potOtherStatsMaps;
                        bestI = i;
                        bestH = h;
                    }
                }
                phs[bestH].addRealization(bestPHR);
                mapsSS = bestSS;
                otherStatsMaps = bestOSS;
                emptyQuadTrees[0].addPoint(bestPHR);
                householdsDone++;
                peopleDone += bestPHR.getVariableValue(PumsHousehold.NP);
                int pDone = (20 * householdsDone) / households2do;
                pDone *= 5;
                if (pDone > pDoneLast) {
                    pDoneLast = pDone;
                    Date nTime = new Date();
                    double tTime = (nTime.getTime() - startTime.getTime()) / (1000.0);
                    System.out.print(pDone + "%. SD: ");
                    for (int i = 0; i < mapsSS.length; i++) {
                        System.out.print(Math.sqrt(mapsSS[i].spread(goalStatistics[i])) + " ");
                    }
                    System.out.print(": ");
                    for (int i = 0; i < otherStatsGoal.length; i++) {
                        System.out.print(Math.sqrt(otherStatsMaps[i].spread(otherStatsGoal[i])) + " ");
                    }
                    double toDo = (tTime * (100.0 - pDone) / pDone) / (60.0 * 60.0);
                    if (toDo >= 1.0) {
                        System.out.println(Math.sqrt(bestFit) + ". ETA: " + toDo + " hr.");
                    } else {
                        System.out.println(Math.sqrt(bestFit) + ". ETA: " + (toDo * 60.0) + " min.");
                    }
                }
            }
            System.out.println("");
            System.out.println("FINISHED INITIALIZATION OF HOUSEHOLDS");
            System.out.println("");
            double bestFit = 0.0;
            for (int numSS = 0; numSS < mapsSS.length; numSS++) {
                System.out.println("Maps Statistic [" + numSS + "]:");
                mapsSS[numSS].print();
                double spread = mapsSS[numSS].spread(goalStatistics[numSS]);
                bestFit += spread * spread * weightLocations;
            }
            for (int numSS = 0; numSS < otherStatsGoal.length; numSS++) {
                System.out.println("Maps Statistic [" + numSS + "]:");
                otherStatsMaps[numSS].print();
                double spread = otherStatsMaps[numSS].spread(otherStatsGoal[numSS]);
                bestFit += spread * spread * weightOtherStatistics[numSS];
            }
            System.out.println("");
            System.out.println("spread: " + Math.sqrt(bestFit));
            System.out.println("");
            Date nowTime = new Date();
            double totalSeconds = (nowTime.getTime() - startTime.getTime()) / (1000.0);
            System.out.println("Seconds for initial construction: " + totalSeconds);
            System.out.println("");
            System.out.println("ConflatePumsTracts.ConflatePumsTracts saving initial point locations to optInitial.txt");
            PrintWriter out = null;
            String outFileName = "optInitial" + mrDone + ".txt";
            out = new PrintWriter(new BufferedWriter(new FileWriter(outFileName)));
            System.out.println();
            out.println("x,y,uid");
            GISPointQuadTreeBreadthFirstIterator tO = new GISPointQuadTreeBreadthFirstIterator(emptyQuadTrees[0]);
            GISPoint p = tO.next();
            while (p != null) {
                Object[] atts = p.getAttributes();
                int serID = ((PumsHousehold) atts[0]).getVariableValue(PumsHousehold.SERIALNO);
                int realizationNumber = ((Integer) atts[1]).intValue();
                out.println(p.getEasting() + "," + p.getNorthing() + "," + serID + ":" + realizationNumber);
                p = tO.next();
            }
            out.close();
        }
    }

    public static void main(String argv[]) throws IOException {
        Date startMain = new Date();
        int numberRealizations = 1;
        double hours2save = 1.0;
        long seed = 23;
        double weightLocations = 1.0;
        System.out.println("");
        System.out.println("Running only black, female, and 50-64");
        System.out.println("");
        double[] nextWeights2Use = { 1., 1., 1. };
        int[] nextVariables2Use = { 7, 25, 42 };
        new ConflatePumsTracts("ri_nlcd240", "ri_tracts240", "ri_tracts.csv", "CNTY_TRACT_FIPS", "ss01hri.csv", "ss01pri.csv", hours2save, seed, weightLocations, nextWeights2Use, nextVariables2Use, numberRealizations);
        Date endPartOne = new Date();
        double partOneHours = (endPartOne.getTime() - startMain.getTime()) / (1000.0 * 60.0 * 60.0);
        System.out.println("");
        System.out.println("Running all variables");
        System.out.println("");
        double[] weights2Use = { 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. };
        int[] variables2Use = { 1, 2, 3, 4, 5, 6, 7, 8, 24, 25, 41, 42, 43, 44, 45, 46, 47, 48 };
        new ConflatePumsTracts("ri_nlcd240", "ri_tracts240", "ri_tracts.csv", "CNTY_TRACT_FIPS", "ss01hri.csv", "ss01pri.csv", hours2save, seed, weightLocations, weights2Use, variables2Use, numberRealizations);
        Date endPartTwo = new Date();
        double partTwoHours = (endPartTwo.getTime() - endPartOne.getTime()) / (1000.0 * 60.0 * 60.0);
        System.out.println("");
        System.out.println("Time for running black, female, and 50-64:  " + partOneHours + " hours.");
        System.out.println("Time for running all demographic variables: " + partTwoHours + " hours.");
    }
}
