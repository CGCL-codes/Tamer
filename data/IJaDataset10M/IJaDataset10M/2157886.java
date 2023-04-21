package hadit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CGEMSProcessor {

    String mCgemsFilename;

    public CGEMSProcessor(String inFilename) {
        mCgemsFilename = inFilename;
    }

    /** Can parse a cgems file and extract allele frequencies. */
    public static ArrayList<HardyWeinbergCalculator.SnpGenotypePackage> getCountsFromFile(String inFilename) {
        String line = null;
        ArrayList<HardyWeinbergCalculator.SnpGenotypePackage> snpList = new ArrayList<HardyWeinbergCalculator.SnpGenotypePackage>(40000);
        int index = 0;
        String charRegExp = "\\w+";
        Pattern charPattern = Pattern.compile(charRegExp);
        String intRegExp = "\\d+";
        Pattern intPattern = Pattern.compile(intRegExp);
        String countRegExp = "\\d+\\(\\d\\D*\\d*\\)\\|*";
        Pattern countPattern = Pattern.compile(countRegExp);
        try {
            BufferedReader in = new BufferedReader(new FileReader(inFilename));
            while ((line = in.readLine()) != null) {
                String[] components = line.split("\\s");
                int position = Integer.parseInt(components[2]);
                String alleleCountStr = components[9];
                String genotypeCountStr = components[11];
                String alleles = components[8];
                index = 0;
                Matcher pAllelesM = charPattern.matcher(alleles);
                char[] allelesArr = new char[2];
                while (pAllelesM.find()) {
                    allelesArr[index++] = pAllelesM.group().charAt(0);
                }
                index = 0;
                AlleleCountPair alleleCounts = new AlleleCountPair(2);
                Matcher pAlleleCountsM = countPattern.matcher(alleleCountStr);
                while (pAlleleCountsM.find()) {
                    Matcher alleleTempM = intPattern.matcher(pAlleleCountsM.group());
                    if (alleleTempM.find()) {
                        int theCount = Integer.parseInt(alleleTempM.group());
                        alleleCounts.setAlleleCount(index++, theCount);
                    }
                }
                index = 0;
                AlleleCountPair genotypeCounts = new AlleleCountPair(3);
                Matcher pGenotypesM = countPattern.matcher(genotypeCountStr);
                while (pGenotypesM.find()) {
                    Matcher genTempM = intPattern.matcher(pGenotypesM.group());
                    if (genTempM.find()) {
                        int theCount = Integer.parseInt(genTempM.group());
                        genotypeCounts.setAlleleCount(index++, theCount);
                    }
                }
                if (5 == 3) {
                    alleleCounts.reverse();
                    genotypeCounts.reverse();
                }
                HardyWeinbergCalculator.SnpGenotypePackage sgp = new HardyWeinbergCalculator.SnpGenotypePackage(position, allelesArr, alleleCounts, genotypeCounts);
                snpList.add(sgp);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Collections.sort(snpList);
        return snpList;
    }

    public void splitFileIntoChromosomes() {
        String line = null;
        try {
            int numChrom = 22;
            BufferedWriter out[] = new BufferedWriter[numChrom];
            for (int i = 0; i < numChrom; i++) {
                out[i] = new BufferedWriter(new FileWriter(mCgemsFilename + ".Chr" + (i + 1) + ".txt"));
            }
            BufferedReader in = new BufferedReader(new FileReader(mCgemsFilename));
            line = in.readLine();
            while ((line = in.readLine()) != null) {
                String[] components = line.split("\\s");
                String chromStr = components[1];
                if (chromStr.equals("X") || chromStr.equals("Y") || chromStr.equals("M") || chromStr.equals("XY")) continue;
                int chrom = Integer.parseInt(components[1]);
                out[chrom - 1].write(line);
                out[chrom - 1].newLine();
            }
            for (int i = 0; i < numChrom; i++) {
                out[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(line);
            System.exit(-1);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CGEMSProcessor cgems = new CGEMSProcessor(args[0]);
        cgems.splitFileIntoChromosomes();
    }

    public static class FrequencyPackage {

        byte mChromNum;

        int mPosition;

        AlleleCountPair mAlleleCounts;
    }
}
