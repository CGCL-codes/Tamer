package msa2snp.msa.codon;

import java.util.HashMap;

public class CodonTable {

    private static HashMap<String, Character> codonTable;

    public CodonTable() {
        super();
        codonTable = new HashMap<String, Character>(100);
        initTable();
    }

    public static char translate(String codon) {
        Character ch = codonTable.get(codon);
        return ch == null ? '-' : ch;
    }

    public static char translate(final Codon codon) {
        return translate(codon.getTriplet());
    }

    private void initTable() {
        codonTable.put("TTT", 'F');
        codonTable.put("TTC", 'F');
        codonTable.put("TTA", 'L');
        codonTable.put("TTG", 'L');
        codonTable.put("CTT", 'L');
        codonTable.put("CTC", 'L');
        codonTable.put("CTA", 'L');
        codonTable.put("CTG", 'L');
        codonTable.put("ATT", 'I');
        codonTable.put("ATC", 'I');
        codonTable.put("ATA", 'I');
        codonTable.put("ATG", 'M');
        codonTable.put("GTT", 'V');
        codonTable.put("GTC", 'V');
        codonTable.put("GTA", 'V');
        codonTable.put("GTG", 'V');
        codonTable.put("TCT", 'S');
        codonTable.put("TCC", 'S');
        codonTable.put("TCA", 'S');
        codonTable.put("TCG", 'S');
        codonTable.put("CCT", 'P');
        codonTable.put("CCC", 'P');
        codonTable.put("CCA", 'P');
        codonTable.put("CCG", 'P');
        codonTable.put("ACT", 'T');
        codonTable.put("ACC", 'T');
        codonTable.put("ACA", 'T');
        codonTable.put("ACG", 'T');
        codonTable.put("GCT", 'A');
        codonTable.put("GCC", 'A');
        codonTable.put("GCA", 'A');
        codonTable.put("GCG", 'A');
        codonTable.put("TAT", 'Y');
        codonTable.put("TAC", 'Y');
        codonTable.put("TAA", '*');
        codonTable.put("TAG", '*');
        codonTable.put("CAT", 'H');
        codonTable.put("CAC", 'H');
        codonTable.put("CAA", 'Q');
        codonTable.put("CAG", 'Q');
        codonTable.put("AAT", 'N');
        codonTable.put("AAC", 'N');
        codonTable.put("AAA", 'K');
        codonTable.put("AAG", 'K');
        codonTable.put("GAT", 'D');
        codonTable.put("GAC", 'D');
        codonTable.put("GAA", 'E');
        codonTable.put("GAG", 'E');
        codonTable.put("TGT", 'C');
        codonTable.put("TGC", 'C');
        codonTable.put("TGA", '*');
        codonTable.put("TGG", 'W');
        codonTable.put("CGT", 'R');
        codonTable.put("CGC", 'R');
        codonTable.put("CGA", 'R');
        codonTable.put("CGG", 'R');
        codonTable.put("AGT", 'S');
        codonTable.put("AGC", 'S');
        codonTable.put("AGA", 'R');
        codonTable.put("AGG", 'R');
        codonTable.put("GGT", 'G');
        codonTable.put("GGC", 'G');
        codonTable.put("GGA", 'G');
        codonTable.put("GGG", 'G');
    }
}
