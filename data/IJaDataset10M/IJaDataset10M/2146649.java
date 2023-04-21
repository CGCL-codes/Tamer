package gov.usda.gdpc.test;

import gov.usda.gdpc.Allele;
import gov.usda.gdpc.AlleleList;
import gov.usda.gdpc.DBConnection;
import gov.usda.gdpc.DefaultGenotypeExperimentGroup;
import gov.usda.gdpc.DefaultGenotypeTable;
import gov.usda.gdpc.DefaultTaxonGroup;
import gov.usda.gdpc.Export;
import gov.usda.gdpc.FilterSingleValue;
import gov.usda.gdpc.GenotypeExperiment;
import gov.usda.gdpc.GenotypeExperimentGroup;
import gov.usda.gdpc.GenotypeExperimentProperty;
import gov.usda.gdpc.GenotypeTable;
import gov.usda.gdpc.Import;
import gov.usda.gdpc.Taxon;
import gov.usda.gdpc.TaxonFilter;
import gov.usda.gdpc.TaxonGroup;
import gov.usda.gdpc.TaxonProperty;
import gov.usda.gdpc.axis2.Axis2Proxy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author terryc
 */
public class TestFeng {

    private static final int NUM_EXP_HEADERS = 11;

    private static final int NUM_TAXA_HEADERS = 13;

    private BufferedReader myInput = null;

    private BufferedWriter myOutput = null;

    private String[] myTarget = null;

    private int[] myFrequencies = null;

    /** Creates a new instance of TestFeng */
    public TestFeng(String inputFile, String outputFile) {
        try {
            myInput = new BufferedReader(new FileReader(inputFile));
            myOutput = new BufferedWriter(new FileWriter(outputFile));
            copyExpHeaders();
            getTargetAlleles();
            getAlleleFrequencies();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                myOutput.close();
                myInput.close();
            } catch (Exception e) {
            }
        }
    }

    private static void getData() {
        DBConnection connection = new Axis2Proxy("http://mays.maize.cornell.edu:8081/axis2/services/aztec");
        String[] taxa = new String[] { "B73", "B97", "CML103", "CML228", "CML247", "CML277", "CML322", "CML333", "CML52", "CML69", "HP301", "Il14H", "Ki11", "Ki3", "Ky21", "M162W", "M37W", "MS71", "Mo18W", "NC350", "NC358", "Oh43", "Oh7B", "P39", "Tx303", "Tzi8", "TIL01", "TIL02", "TIL03", "TIL04", "TIL05", "TIL06", "TIL07", "TIL08", "TIL09", "TIL10", "TIL11", "TIL12", "TIL14", "TIL15", "TIL16", "TIL17", "TIL18", "TIL23", "TIL25", "TIL27", "TIL29", "Mo17" };
        GenotypeTable[] tables = new GenotypeTable[taxa.length + 1];
        for (int i = 0; i < taxa.length; i++) {
            TaxonFilter taxaFilter = new TaxonFilter();
            taxaFilter.addValue(new FilterSingleValue(TaxonProperty.ACCESSION, taxa[i]));
            TaxonGroup taxaGroup = connection.getTaxonGroup(taxaFilter);
            tables[i] = connection.getGenotypeTable(null, taxaGroup);
        }
        TaxonFilter taxaFilter = new TaxonFilter();
        taxaFilter.addValue(new FilterSingleValue(TaxonProperty.GENUS, "Tripsacum"));
        TaxonGroup taxaGroup = connection.getTaxonGroup(taxaFilter);
        tables[tables.length - 1] = connection.getGenotypeTable(null, taxaGroup);
        GenotypeTable result = getResultGenotypeTable(tables);
        try {
            Export.writeJavaSerialized(result, new File("Sequences_Teosinte_NAM.ser"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GenotypeTable getResultGenotypeTable(GenotypeTable[] tables) {
        List expList = new ArrayList();
        List taxaList = new ArrayList();
        for (int i = 0; i < tables.length; i++) {
            Iterator itr = tables[i].getGenotypeExperimentGroup().iterator();
            while (itr.hasNext()) {
                GenotypeExperiment current = (GenotypeExperiment) itr.next();
                String polyType = (String) current.getProperty(GenotypeExperimentProperty.POLY_TYPE);
                if ((polyType.equals(GenotypeExperimentProperty.POLY_TYPE_SEQUENCE)) && (!expList.contains(current))) {
                    expList.add(current);
                }
            }
            Iterator itr2 = tables[i].getTaxonGroup().iterator();
            while (itr2.hasNext()) {
                Object current = itr2.next();
                if (!taxaList.contains(current)) {
                    taxaList.add(current);
                }
            }
        }
        GenotypeExperimentGroup expGroup = DefaultGenotypeExperimentGroup.getInstance(expList, true);
        TaxonGroup taxaGroup = DefaultTaxonGroup.getInstance(taxaList, true);
        AlleleList[][] data = new AlleleList[expList.size()][taxaList.size()];
        for (int i = 0; i < tables.length; i++) {
            for (int r = 0; r < tables[i].numRows(); r++) {
                for (int c = 0; c < tables[i].numColumns(); c++) {
                    GenotypeExperiment currentExp = tables[i].getGenotypeExperiment(c);
                    int expIndex = expGroup.indexOf(currentExp);
                    if (expIndex != -1) {
                        Taxon currentTaxon = tables[i].getTaxon(r);
                        int taxonIndex = taxaGroup.indexOf(currentTaxon);
                        AlleleList current = (AlleleList) tables[i].get(c, r);
                        if (data[expIndex][taxonIndex] == null) {
                            data[expIndex][taxonIndex] = current;
                        } else {
                            ArrayList list = new ArrayList(current);
                            list.addAll(data[expIndex][taxonIndex]);
                            data[expIndex][taxonIndex] = AlleleList.getInstance(list);
                        }
                    }
                }
            }
        }
        return DefaultGenotypeTable.getInstance(expGroup, taxaGroup, data);
    }

    private void getAlleleFrequencies() {
        myFrequencies = new int[myTarget.length];
        try {
            String input = myInput.readLine();
            while (input != null) {
                String[] current = input.split("\t");
                int count = Math.min(myTarget.length, current.length);
                for (int i = NUM_TAXA_HEADERS + 1; i < count; i++) {
                    if (myFrequencies[i] != -1) {
                        if ((current[i] == null) || (current[i].length() == 0)) {
                            myFrequencies[i] = -1;
                        } else if (current[i].equalsIgnoreCase(myTarget[i])) {
                            myFrequencies[i]++;
                        }
                    }
                }
                input = myInput.readLine();
            }
            for (int k = 0; k < myTarget.length; k++) {
                myOutput.write(String.valueOf(myTarget[k]));
                myOutput.write("\t");
            }
            myOutput.write("\n");
            for (int j = 0; j < myTarget.length; j++) {
                myOutput.write(String.valueOf(myFrequencies[j]));
                myOutput.write("\t");
            }
            myOutput.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTargetAlleles() {
        try {
            String input = myInput.readLine();
            input = myInput.readLine();
            myTarget = input.split("\t");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyExpHeaders() {
        try {
            for (int i = 0; i < NUM_EXP_HEADERS; i++) {
                String current = myInput.readLine();
                myOutput.write(current);
                myOutput.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void countAllScored() {
        String filename = "C:/local/no_backup/frequency/Sequences_B73_NAM_Merged_Accession.ser";
        int result = 0;
        int[] alleleCounts = new int[10];
        int numCounts = 0;
        GenotypeTable table = null;
        try {
            table = (GenotypeTable) Import.readJavaSerialized(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int c = 0; c < table.numColumns(); c++) {
            boolean allScored = true;
            for (int r = 0; r < table.numRows(); r++) {
                AlleleList current = (AlleleList) table.get(c, r);
                if (current == null) {
                    allScored = false;
                }
            }
            if (allScored) {
                result++;
                for (int r = 0; r < table.numRows(); r++) {
                    AlleleList current = (AlleleList) table.get(c, r);
                    int numAlleles = current.size();
                    alleleCounts[numAlleles]++;
                    if (numAlleles + 1 > numCounts) {
                        numCounts = numAlleles + 1;
                    }
                }
            }
        }
        System.out.println("TestFeng: countAllScored: result: " + result);
        for (int i = 0; i < numCounts; i++) {
            System.out.println("TestFeng: countAllScored: " + i + " allele(s): " + alleleCounts[i]);
        }
    }

    private static void getFrequencies() {
        String filename = "C:/local/no_backup/frequency/Sequences_B73_NAM_Merged_Accession.ser";
        GenotypeTable table = null;
        try {
            table = (GenotypeTable) Import.readJavaSerialized(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TaxonFilter filter = new TaxonFilter();
        filter.addValue(new FilterSingleValue(TaxonProperty.ACCESSION, "B73"));
        TaxonGroup b73Group = table.getTaxonGroup().getTaxonGroup(filter);
        if (b73Group.size() != 1) {
            throw new IllegalStateException("TestFeng: getFrequencies: should be exactly one B73.");
        }
        int b73Index = table.getTaxonGroup().indexOf(b73Group.get(0));
        int numTaxa = table.numRows();
        System.out.println("TestFeng: getFrequencies: num of taxa: " + numTaxa);
        int[] frequencies = new int[25];
        int[][] freqByAlign = new int[25][table.numColumns()];
        int[] alignLength = new int[table.numColumns()];
        int[][] namAlignLength = new int[25][table.numColumns()];
        int[] taxaTranslate = new int[25];
        int tempCount = 0;
        for (int r = 0; r < numTaxa; r++) {
            if (r != b73Index) {
                taxaTranslate[tempCount++] = r;
            }
        }
        List charList = new ArrayList();
        List allVariations = new ArrayList();
        for (int c = 0; c < table.numColumns(); c++) {
            boolean allScored = true;
            for (int r = 0; r < numTaxa; r++) {
                Object current = table.get(c, r);
                if (current == null) {
                    allScored = false;
                    break;
                }
            }
            if (allScored) {
                String b73Alignment = null;
                String[] namAlignments = new String[25];
                int count = 0;
                for (int r = 0; r < numTaxa; r++) {
                    AlleleList current = (AlleleList) table.get(c, r);
                    if (r == b73Index) {
                        if (current.size() > 1) {
                            String first = ((Allele) current.get(0)).getValue();
                            String second = ((Allele) current.get(1)).getValue();
                            if (!first.equals(second)) {
                                System.out.println("b73: " + table.getGenotypeExperiment(c).getName() + ": size: " + current.size());
                                int longest = Math.max(first.length(), second.length());
                                System.out.print("sites: ");
                                for (int l = 0; l < longest; l++) {
                                    try {
                                        if (first.charAt(l) != second.charAt(l)) {
                                            System.out.print("\t" + l);
                                        }
                                    } catch (Exception e) {
                                        System.out.print("\t" + l);
                                    }
                                }
                                System.out.println("");
                            }
                        }
                        b73Alignment = ((Allele) current.get(0)).getValue();
                        alignLength[c] = b73Alignment.length();
                    } else {
                        if (current.size() > 1) {
                            String first = ((Allele) current.get(0)).getValue();
                            String second = ((Allele) current.get(1)).getValue();
                            if (!first.equals(second)) {
                                System.out.println(table.getTaxonGroup().get(r).toString() + ": " + table.getGenotypeExperiment(c).getName() + ": size: " + current.size());
                                int longest = Math.max(first.length(), second.length());
                                System.out.print("sites: ");
                                for (int l = 0; l < longest; l++) {
                                    try {
                                        if (first.charAt(l) != second.charAt(l)) {
                                            System.out.print("\t" + l);
                                        }
                                    } catch (Exception e) {
                                        System.out.print("\t" + l);
                                    }
                                }
                                System.out.println("");
                            }
                        }
                        namAlignments[count] = ((Allele) current.get(0)).getValue();
                        namAlignLength[count][c] = namAlignments[count].length();
                        count++;
                    }
                }
                List variation = new ArrayList();
                for (int i = 0, m = b73Alignment.length(); i < m; i++) {
                    char target = b73Alignment.charAt(i);
                    int numMatches = 0;
                    if (!charList.contains(target)) {
                        charList.add(target);
                    }
                    variation.clear();
                    variation.add(target);
                    if ((target == 'N') || (target == '-') || (target == '?')) {
                        numMatches = 25;
                    }
                    for (int j = 0; j < 25; j++) {
                        try {
                            char currentChar = namAlignments[j].charAt(i);
                            if (!charList.contains(currentChar)) {
                                charList.add(currentChar);
                            }
                            if ((currentChar == 'N') || (currentChar == '-') || (currentChar == '?')) {
                                numMatches = 25;
                                break;
                            } else if (currentChar == target) {
                                numMatches++;
                            } else {
                                if (!variation.contains(currentChar)) {
                                    variation.add(currentChar);
                                }
                            }
                        } catch (Exception e) {
                            numMatches = 25;
                        }
                    }
                    if (numMatches < 25) {
                        frequencies[numMatches]++;
                        freqByAlign[numMatches][c]++;
                        char temp[] = new char[variation.size()];
                        for (int a = 0; a < variation.size(); a++) {
                            temp[a] = ((Character) variation.get(a)).charValue();
                        }
                        Arrays.sort(temp);
                        String charStr = null;
                        for (int b = 0; b < temp.length; b++) {
                            if (charStr == null) {
                                charStr = String.valueOf(temp[b]);
                            } else {
                                charStr = charStr + ":" + temp[b];
                            }
                        }
                        if (!allVariations.contains(charStr)) {
                            allVariations.add(charStr);
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 25; x++) {
            System.out.println(x + " of 25\t" + frequencies[x]);
        }
        for (int y = 0; y < charList.size(); y++) {
            System.out.println("char: " + charList.get(y));
        }
        for (int z = 0; z < allVariations.size(); z++) {
            System.out.println("SNPs: " + allVariations.get(z));
        }
        int[] totals = new int[25];
        for (int t = 0; t < table.numColumns(); t++) {
            System.out.print("\t" + table.getGenotypeExperimentGroup().get(t));
        }
        System.out.println();
        System.out.print("B73");
        for (int t = 0; t < table.numColumns(); t++) {
            System.out.print("\t" + alignLength[t]);
        }
        System.out.println();
        for (int s = 0; s < 25; s++) {
            System.out.print(table.getTaxonGroup().get(taxaTranslate[s]).toString());
            for (int t = 0; t < table.numColumns(); t++) {
                System.out.print("\t" + namAlignLength[s][t]);
            }
            System.out.println();
        }
        for (int s = 0; s < 25; s++) {
            System.out.print(s + " of 25");
            for (int t = 0; t < table.numColumns(); t++) {
                System.out.print("\t" + freqByAlign[s][t]);
                totals[s] = totals[s] + freqByAlign[s][t];
            }
            System.out.println();
        }
        for (int x = 0; x < 25; x++) {
            System.out.println(x + " of 25\t" + totals[x]);
        }
    }

    public static void main(String[] args) {
        getData();
    }
}
