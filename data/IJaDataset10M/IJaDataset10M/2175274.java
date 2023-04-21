package org.mcisb.bioinformatics.util;

import java.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class Translator {

    /**
	 * 
	 */
    private static final String STOP_CODON = "TAA";

    /**
	 * 
	 */
    private static final Map<String, String> aminoAcidToCodon = new TreeMap<String, String>();

    static {
        aminoAcidToCodon.put("A", "GCT");
        aminoAcidToCodon.put("C", "TGT");
        aminoAcidToCodon.put("D", "GAT");
        aminoAcidToCodon.put("E", "GAA");
        aminoAcidToCodon.put("F", "TTT");
        aminoAcidToCodon.put("G", "GGT");
        aminoAcidToCodon.put("H", "CAT");
        aminoAcidToCodon.put("I", "ATT");
        aminoAcidToCodon.put("K", "AAA");
        aminoAcidToCodon.put("L", "CTT");
        aminoAcidToCodon.put("M", "ATG");
        aminoAcidToCodon.put("N", "AAT");
        aminoAcidToCodon.put("P", "CCT");
        aminoAcidToCodon.put("Q", "CAA");
        aminoAcidToCodon.put("R", "CGT");
        aminoAcidToCodon.put("S", "TCT");
        aminoAcidToCodon.put("T", "ACT");
        aminoAcidToCodon.put("V", "GTT");
        aminoAcidToCodon.put("W", "TGG");
        aminoAcidToCodon.put("Y", "TAT");
    }

    /**
	 * 
	 * @param aminoAcidSequence
	 * @return String
	 */
    public static String translate(final String aminoAcidSequence) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < aminoAcidSequence.length(); i++) {
            buffer.append(aminoAcidToCodon.get(Character.valueOf(aminoAcidSequence.charAt(i)).toString()));
        }
        buffer.append(STOP_CODON);
        return buffer.toString();
    }
}
