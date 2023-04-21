package src.projects.VariationDatabase.Iterators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import src.lib.Constants;
import src.lib.Utilities;
import src.lib.Error_handling.UnexpectedCharacterException;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.SNP;

/**
 * @version $Revision: 3496 $
 * @author 
 */
public class PileupSnpIterator_Flagged implements Iterator<SNP>, SNPIterator {

    private static boolean display_version = true;

    private static Log_Buffer LB;

    private static final int FIELDS_EXPECTED = 10;

    BufferedReader br = null;

    int linecount = 0;

    final String filename;

    Vector<SNP> buffer;

    public PileupSnpIterator_Flagged(Log_Buffer logbuffer, String source_file) {
        LB = logbuffer;
        this.filename = source_file;
        if (display_version) {
            LB.Version("Pileup Snp Iterator", "$Revision: 3496 $");
            display_version = false;
        }
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            LB.error("Can't find file " + filename);
            LB.die();
        }
        buffer = new Vector<SNP>();
    }

    public boolean mark() {
        try {
            br.mark(Constants.MARK_BUFFER_SIZE);
            return true;
        } catch (IOException ioe) {
            LB.error("Could not mark Input file for read buffer.");
            return false;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close(boolean verbose) {
        try {
            this.br.close();
        } catch (IOException ioe) {
            LB.warning("Could not close file");
            LB.warning("Message thrown by Java environment (may be null):" + ioe.getMessage());
        }
        LB.notice(filename + " closed successfully");
    }

    /** 
	 * Read in next line of the SNP file
	 * 
	 * http://samtools.sourceforge.net/pileup.shtml
	 * 
	 * Expected format:
	 * chr	   position ref    obs    cons q   snp q   map q   cover   readbases   base qualities
	 * 1       713663  G       C       29      29      60      1       c       >
	 * 1       713754  G       C       28      36      21      4       A$ccC   %=;B
	 * 1       713924  T       G       22      22      36      1       G       7
	 * 1       714068  C       M       56      65      22      46      .$.$T$T$TT.,,TAA,,aaA,.gTAT,Tat.ttttgt,t,tttt.tttg     
	 * 																			 +@%+%%%BB%@@+?>>AB1%%1%;%%%;%%%%%%%%%%%%%A%%%% 
	 * 1       1142396 *       -A/*    580     580     59      76      -A      *       15      58      3       1       23
	 * 
	 * 
	 * @return SNP object
	 */
    public SNP next() {
        String line = null;
        Vector<SNP> S = new Vector<SNP>();
        if (!buffer.isEmpty()) {
            return buffer.remove(0);
        }
        try {
            if ((line = br.readLine()) != null) {
                this.linecount++;
                String[] elements = line.split("\t");
                if (elements.length < FIELDS_EXPECTED) {
                    LB.error("Lack of fields in SNP file provided");
                }
                char ref = elements[2].toUpperCase().charAt(0);
                char obs = elements[3].toUpperCase().charAt(0);
                if (ref == '*' || ref == obs || ref == 'N' || ref == 'M' || ref == 'R') {
                    return next();
                }
                String chromosome = elements[0];
                if (chromosome.startsWith("chr")) {
                    chromosome = chromosome.substring(3);
                }
                int position = Integer.valueOf(elements[1]);
                int snp_qual = -1;
                int coverage = Integer.valueOf(elements[7]);
                String read_bases = elements[8];
                int count_canonical = 0;
                int[] count_oth = new int[4];
                for (int x = 0; x < read_bases.length(); x++) {
                    if (read_bases.charAt(x) == '.' || read_bases.charAt(x) == ',') {
                        count_canonical += 1;
                    } else if (read_bases.charAt(x) == 'A' || read_bases.charAt(x) == 'a') {
                        count_oth[0]++;
                    } else if (read_bases.charAt(x) == 'C' || read_bases.charAt(x) == 'c') {
                        count_oth[1]++;
                    } else if (read_bases.charAt(x) == 'G' || read_bases.charAt(x) == 'g') {
                        count_oth[2]++;
                    } else if (read_bases.charAt(x) == 'T' || read_bases.charAt(x) == 't') {
                        count_oth[3]++;
                    }
                }
                char[] snps = Utilities.non_canonical_expansion(obs);
                for (char o : snps) {
                    if (o != ref) {
                        int hits = 0;
                        try {
                            hits = Utilities.getIndexForBase(o);
                        } catch (UnexpectedCharacterException e) {
                            LB.notice("Skipping an unexpected character: " + o);
                            continue;
                        }
                        S.add(new SNP(chromosome, position, o, ref, count_oth[hits], count_canonical, coverage, -1, snp_qual, -1, -1, -1, -1, -1));
                    }
                }
            } else {
                throw new NoSuchElementException("Could not get any more reads.");
            }
        } catch (IOException io) {
            LB.error("Error occured on line " + this.linecount);
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        } catch (NumberFormatException nfe) {
            LB.error("Number format exception error occured on line " + this.linecount);
            LB.error(line);
            LB.error("Message thrown by Java environment (may be null):" + nfe.getMessage());
            LB.die();
        }
        if (S.size() > 1) {
            SNP first_snp = S.remove(0);
            buffer.addAll(S);
            return first_snp;
        } else {
            return S.firstElement();
        }
    }

    public boolean hasNext() {
        try {
            return br.ready();
        } catch (IOException io) {
            LB.error("Could not determine status of SNPIterator");
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
        return false;
    }
}
