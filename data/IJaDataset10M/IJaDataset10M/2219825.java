package org.systemsbiology.dbsearch;

import java.lang.*;
import java.io.*;
import java.util.*;
import org.apache.regexp.RE;

public class Parameter {

    public String dbPath;

    public double precursorPepErr;

    public double fragPepErr;

    public int precursorMassType;

    public int fragMassType;

    public int maxMissCleavage;

    public int numOutputLines;

    public int enzymeNum;

    public int actualEnzNum;

    public int numEnds;

    public int[] ionType;

    public double[] modLight;

    public String dynamicMod;

    public double[] dynamicModValue;

    public Parameter() {
        dbPath = null;
        precursorPepErr = 0;
        fragPepErr = 0;
        precursorMassType = -1;
        fragMassType = -1;
        maxMissCleavage = 0;
        numOutputLines = 0;
        enzymeNum = -1;
        actualEnzNum = -1;
        numEnds = -1;
        ionType = new int[128];
        modLight = new double[128];
        dynamicMod = "";
        dynamicModValue = new double[128];
    }

    public void readParamFile() {
        String[] temp = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("probid.param"));
            String thisLine = null;
            RE r1 = new RE("=");
            RE r2 = new RE("\\s");
            while ((thisLine = br.readLine()) != null) {
                thisLine = thisLine.trim();
                if (!thisLine.startsWith("#")) {
                    if ((thisLine.indexOf("database_name")) != -1) {
                        dbPath = (r1.split(thisLine))[1];
                    }
                    if ((thisLine.indexOf("precursor_mass_tolerance")) != -1) {
                        precursorPepErr = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("fragment_mass_tolerance")) != -1) {
                        fragPepErr = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("precursor_mass_type")) != -1) {
                        int pos = thisLine.indexOf('#');
                        String tmpMassType = thisLine.substring(0, pos).trim();
                        precursorMassType = Integer.parseInt((r1.split(tmpMassType))[1]);
                    }
                    if ((thisLine.indexOf("fragment_mass_type")) != -1) {
                        fragMassType = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("search_enzyme_number")) != -1) {
                        System.out.println(thisLine);
                        enzymeNum = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("search_enzymatic_termini")) != -1) {
                        numEnds = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("num_output_lines")) != -1) {
                        numOutputLines = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("max_miss_cleavage")) != -1) {
                        maxMissCleavage = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("actual_enzyme_number")) != -1) actualEnzNum = Integer.parseInt((r1.split(thisLine))[1]);
                    if ((thisLine.indexOf("aIon")) != -1) {
                        ionType['a'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("bIon")) != -1) {
                        ionType['b'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("cIon")) != -1) {
                        ionType['c'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("xIon")) != -1) {
                        ionType['x'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("yIon")) != -1) {
                        ionType['y'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("zIon")) != -1) {
                        ionType['z'] = Integer.parseInt((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_nTerm")) != -1) {
                        modLight['<'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_cTerm")) != -1) {
                        modLight['>'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_G")) != -1) {
                        modLight['g'] = modLight['G'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_A")) != -1) {
                        modLight['a'] = modLight['A'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_S")) != -1) {
                        modLight['s'] = modLight['S'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_P")) != -1) {
                        modLight['p'] = modLight['P'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_V")) != -1) {
                        modLight['v'] = modLight['V'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_T")) != -1) {
                        modLight['t'] = modLight['T'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_C")) != -1) {
                        modLight['c'] = modLight['C'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_L")) != -1) {
                        modLight['l'] = modLight['L'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_I")) != -1) {
                        modLight['i'] = modLight['I'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_X")) != -1) {
                        modLight['x'] = modLight['X'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_N")) != -1) {
                        modLight['n'] = modLight['N'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_O")) != -1) {
                        modLight['o'] = modLight['O'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_B")) != -1) {
                        modLight['b'] = modLight['B'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_D")) != -1) {
                        modLight['d'] = modLight['D'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_Q")) != -1) {
                        modLight['q'] = modLight['Q'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_K")) != -1) {
                        modLight['k'] = modLight['K'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_Z")) != -1) {
                        modLight['z'] = modLight['Z'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_E")) != -1) {
                        modLight['e'] = modLight['E'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_M")) != -1) {
                        modLight['m'] = modLight['M'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_H")) != -1) {
                        modLight['h'] = modLight['H'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_F")) != -1) {
                        modLight['f'] = modLight['F'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_R")) != -1) {
                        modLight['r'] = modLight['R'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_Y")) != -1) {
                        modLight['y'] = modLight['Y'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("add_W")) != -1) {
                        modLight['w'] = modLight['W'] = Double.parseDouble((r1.split(thisLine))[1]);
                    }
                    if ((thisLine.indexOf("variable_modification")) != -1) {
                        if ((r1.split(thisLine)).length > 1) {
                            temp = r2.split((r1.split(thisLine))[1]);
                            for (int i = 0; i < temp.length; i += 2) fill_dynamic(temp[i], Double.parseDouble(temp[i + 1]));
                        }
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error happened in reading in parameter file!");
            e.printStackTrace();
        }
    }

    public void fill_dynamic(String modString, double modValue) {
        dynamicMod += modString.toLowerCase();
        char[] modChars = modString.toCharArray();
        for (int i = 0; i < modChars.length; i++) {
            char low_case = Character.toLowerCase(modChars[i]);
            dynamicModValue[low_case] = modValue;
        }
    }

    public boolean isDynamic() {
        for (int i = 0; i < 128; i++) {
            if (dynamicModValue[i] != 0) return true;
        }
        return false;
    }

    public String getDBpath() {
        return dbPath;
    }

    public double getPrecursorPepErr() {
        return precursorPepErr;
    }

    public double getFragPepErr() {
        return fragPepErr;
    }

    public int getPrecursorMassType() {
        return precursorMassType;
    }

    public int getFragMassType() {
        return fragMassType;
    }

    public int getMaxMissCleavage() {
        return maxMissCleavage;
    }

    public int getNumOutputLines() {
        return numOutputLines;
    }

    public int getEnzymeNum() {
        return enzymeNum;
    }

    public int getNumEnzymaticEnds() {
        return numEnds;
    }

    public int[] getIonType() {
        return ionType;
    }

    public double[] getModLight() {
        return modLight;
    }

    public String getDynamicMod() {
        return dynamicMod;
    }

    public double[] getDynamicModValue() {
        return dynamicModValue;
    }
}
