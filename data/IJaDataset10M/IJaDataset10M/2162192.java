package family.pedigree.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import org.apache.commons.lang3.ArrayUtils;
import admixture.parameter.Parameter;
import test.Test;
import util.NewIt;
import family.mdr.arsenal.MDRConstant;
import family.pedigree.genotype.BFamilyStruct;
import family.pedigree.genotype.BPerson;

/**
 * @author Guo-Bo Chen, chenguobo@gmail.com
 */
public class PedigreeFile {

    protected char[][] AlleleSet;

    protected short[][] AlleleFreq;

    protected Hashtable<String, BFamilyStruct> familystructure;

    protected ArrayList<String> FamID;

    protected HashSet<String> SixthCol = NewIt.newHashSet();

    protected boolean IsSixthColBinary = true;

    protected int num_marker;

    protected String titleLine = null;

    protected String pedfile;

    protected boolean header = true;

    public PedigreeFile() {
        this.familystructure = NewIt.newHashtable();
        FamID = NewIt.newArrayList();
    }

    public String[] getFamListSorted() {
        Enumeration<String> famstrList = this.familystructure.keys();
        String[] FID = new String[familystructure.size()];
        int ind = 0;
        while (famstrList.hasMoreElements()) {
            FID[ind++] = famstrList.nextElement();
        }
        return FID;
    }

    public BFamilyStruct getFamilyStruct(String familystrID) {
        return this.familystructure.get(familystrID);
    }

    /**
	 * this method iterates through each family in Hashtable families and adds
	 * up the number of individuals in total across all families
	 * 
	 * @return the total number of individuals in all the family objects in the
	 *         families hashtable
	 */
    public int getNumIndividuals() {
        Enumeration<BFamilyStruct> famEnum = familystructure.elements();
        int total = 0;
        while (famEnum.hasMoreElements()) {
            BFamilyStruct fam = famEnum.nextElement();
            total += fam.getNumPersons();
        }
        return total;
    }

    public Hashtable<String, BFamilyStruct> getFamilyStruct() {
        return familystructure;
    }

    public void initial() throws IOException {
    }

    /**
	 * Taking in a pedigree file in the form of a vector of strings and parses
	 * it. The data parsed is stored in families in the member hashtable
	 * families. Note that the "Linkage" here is the relationship between
	 * relatives in a pedigree, but is not that term of genetics.
	 */
    public void parseLinkage(String infile, int numMarkerInFile, int[] WSNP) throws IOException {
        initial();
        num_marker = WSNP.length;
        AlleleSet = new char[num_marker][2];
        AlleleFreq = new short[num_marker][2];
        for (int i = 0; i < num_marker; i++) {
            AlleleSet[i][0] = AlleleSet[i][1] = Parameter.missing_allele.charAt(0);
        }
        int numMarkers = 0;
        BufferedReader reader = new BufferedReader(new FileReader(new File(infile)));
        pedfile = infile;
        String line;
        BPerson per;
        int k = 0;
        while ((line = reader.readLine()) != null) {
            String[] tokenizer = line.split(MDRConstant.delim);
            int numTokens = tokenizer.length;
            numMarkers = (numTokens - 6) / 2;
            if (numMarkers != numMarkerInFile) {
                System.err.println("Mismatched column in ped file at line " + (k + 1));
                Test.LOG.append("Mismatched column in ped file at line " + (k + 1) + ".\n");
                Test.printLog();
                System.exit(0);
            }
            per = new BPerson(num_marker);
            if (tokenizer.length > 6) {
                per.setFamilyID(tokenizer[0]);
                per.setPersonID(tokenizer[1]);
                per.setDadID(tokenizer[2]);
                per.setMomID(tokenizer[3]);
                int Gender = Integer.parseInt(tokenizer[4]);
                SixthCol.add(tokenizer[5]);
                per.setGender(Gender);
                per.setAffectedStatus(tokenizer[5]);
                int c = 0;
                for (int j = 0; j < (tokenizer.length - 6) / 2; j++) {
                    int idx = ArrayUtils.indexOf(WSNP, j);
                    if (idx < 0) continue;
                    try {
                        String[] allele = { tokenizer[6 + j * 2], tokenizer[6 + j * 2 + 1] };
                        boolean flag = (allele[0].compareTo(Parameter.missing_allele) != 0) && (allele[1].compareTo(Parameter.missing_allele) != 0);
                        if (flag) {
                            int[] code = recode(c, allele);
                            per.addMarker(flag, code[0], code[1], c);
                            AlleleFreq[c][code[0]]++;
                            AlleleFreq[c][code[1]]++;
                        } else {
                            per.addMarker(flag, 0, 0, c);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("invalid genotype in ped file at line " + (k + 1) + " for marker " + (c + 1) + ".");
                        Test.LOG.append("invalid genotype in ped file at line " + (k + 1) + " for marker " + (c + 1) + ".\n");
                        Test.printLog();
                        System.exit(0);
                    }
                    c++;
                }
                BFamilyStruct famstr = familystructure.get(per.getFamilyID());
                if (famstr == null) {
                    famstr = new BFamilyStruct(per.getFamilyID());
                    familystructure.put(per.getFamilyID(), famstr);
                }
                if (famstr.getPersons().containsKey(per.getPersonID())) {
                    throw new IOException("Person " + per.getPersonID() + " in family " + per.getFamilyID() + " appears more than once.");
                }
                famstr.addPerson(per);
            }
            k++;
        }
        Is6ColBinary();
    }

    protected void Is6ColBinary() {
        for (String c : SixthCol) {
            if (Parameter.status_shiftFlag) {
                if (c.compareTo("1") != 0 && c.compareTo("0") != 0 && c.compareTo(Parameter.missing_phenotype) != 0) {
                    IsSixthColBinary = false;
                    break;
                }
            } else {
                if (c.compareTo("2") != 0 && c.compareTo("1") != 0 && c.compareTo("0") != 0 && c.compareTo(Parameter.missing_phenotype) != 0) {
                    IsSixthColBinary = false;
                    break;
                }
            }
        }
    }

    public boolean IsSixthColBinary() {
        return IsSixthColBinary;
    }

    protected int[] recode(int idx, String[] allele) {
        int[] code = { -1, -1 };
        char[] ref = AlleleSet[idx];
        if (ref[1] != Parameter.missing_allele.charAt(0)) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (allele[i].charAt(0) == ref[j]) {
                        code[i] = j;
                        break;
                    }
                }
            }
            if (code[0] == -1 || code[1] == -1) {
                System.err.println("more than 2 alleles in the marker column " + (idx + 1));
            }
        } else {
            if (allele[0].compareTo(allele[1]) == 0) {
                if (ref[0] == Parameter.missing_allele.charAt(0)) {
                    ref[0] = allele[0].charAt(0);
                    code[0] = code[1] = 0;
                } else {
                    if (allele[0].charAt(0) == ref[0]) {
                        code[0] = code[1] = 0;
                    } else {
                        code[0] = code[1] = 1;
                        ref[1] = allele[1].charAt(0);
                    }
                }
            } else {
                if (ref[0] == Parameter.missing_allele.charAt(0)) {
                    ref[0] = allele[0].charAt(0);
                    ref[1] = allele[1].charAt(0);
                    code[0] = 0;
                    code[1] = 1;
                } else {
                    if (ref[0] == allele[0].charAt(0)) {
                        ref[1] = allele[1].charAt(0);
                        code[0] = 0;
                        code[1] = 1;
                    } else if (ref[0] == allele[1].charAt(0)) {
                        ref[1] = allele[0].charAt(0);
                        code[0] = 1;
                        code[1] = 0;
                    } else {
                        System.out.println("more than 3 alleles in marker column " + (idx + 1));
                    }
                }
            }
        }
        return code;
    }

    public char[][] getPolymorphism() {
        return AlleleSet;
    }

    public short[][] getAlleleFrequency() {
        return AlleleFreq;
    }

    public int getNumMarker() {
        return num_marker;
    }

    public void setHeader(boolean flag) {
        header = flag;
    }

    public void cleanup() {
        for (int i = 0; i < AlleleSet.length; i++) {
            AlleleSet[i] = null;
            AlleleFreq[i] = null;
        }
        AlleleSet = null;
        AlleleFreq = null;
    }
}
