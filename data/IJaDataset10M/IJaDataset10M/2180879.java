package src.projects.SNP_Database.SnpIterators;

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
 * @version $Revision: 1637 $
 * @author 
 */
public class dbSNP130Iterator implements Iterator<SNP>, SNPIterator {

    private static boolean display_version = true;

    private static Log_Buffer LB;

    BufferedReader br = null;

    int linecount = 0;

    final String filename;

    Vector<SNP> buffer;

    public dbSNP130Iterator(Log_Buffer logbuffer, String source_file) {
        LB = logbuffer;
        this.filename = source_file;
        if (display_version) {
            LB.Version("MaqSnpIterator", "$Revision: 1637 $");
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
        } catch (IOException io) {
            LB.warning("Could not close file");
            LB.warning("Message thrown by Java environment (may be null):" + io.getMessage());
        }
        LB.notice(filename + " closed successfully");
    }

    /** 
	 * Read in next line of the SNP file
	 * 
	 * File: /home/mapper/lymphoma/ucsc/hg18/snp/snps.parsed.txt
	 * 
	 * 
	 * Expected format:
	 * chr     position ref    		   strand  ref     alt	   misc
	 * 1 30683621 30683622 rs596200 + A/T A unknown
	 * 1 30683802 30683803 rs1085912 + C/T C unknown
	 * 1 30684227 30684228 rs2722119 + C/T C unknown
	 * 1 30684563 30684564 rs61767962 + A/G A unknown
	 * 1 30684630 30684631 rs61767963 + C/T T unknown
	 * 1 30684735 30684736 rs12740336 + G/T T unknown
	 * 1 30684791 30684792 rs62269249 - A/G C unknown
	 * 1 30684792 30684793 rs12751324 + A/G G unknown
	 * 1 30684820 30684821 rs12139746 + A/C/T C unknown
	 * 
	 * 
	 * @return SNP object
	 */
    public SNP next() {
        String line = null;
        Vector<SNP> S = new Vector<SNP>();
        if (buffer.size() > 0) {
            return buffer.remove(0);
        }
        try {
            while ((line = br.readLine()) != null) {
                this.linecount++;
                String[] elements = line.split(" ");
                if (elements.length < 7) {
                    LB.error("Lack of fields in SNP file provided");
                }
                String chromosome = elements[0];
                int position1 = Integer.valueOf(elements[1]);
                int position2 = Integer.valueOf(elements[2]);
                String misc = elements[3];
                boolean strand = (elements[4].charAt(0) == '+') ? true : false;
                String alleles = elements[5];
                String canonical = elements[6];
                char canon = ' ';
                if (position2 - position1 > 1) {
                    continue;
                }
                if (alleles.charAt(0) == '-' || alleles.charAt(0) == '(') {
                    continue;
                }
                if (canonical.length() > 1) {
                    continue;
                } else {
                    canon = canonical.charAt(0);
                }
                if (canon == '-') {
                    continue;
                }
                String[] alts = alleles.split("/");
                if (alts.length == 1) {
                    continue;
                }
                if (!strand) {
                    for (int x = 0; x < alts.length; ++x) {
                        try {
                            alts[x] = Utilities.reverseCompliment(alts[x]);
                        } catch (UnexpectedCharacterException uce) {
                            LB.error(uce.getMessage());
                            LB.debug(line);
                        }
                    }
                }
                for (String alt : alts) {
                    if (alt.length() == 1 && alt.charAt(0) != canon) {
                        S.add(new SNP(chromosome, position1 + 1, alt.charAt(0), canon, -1, -1, -1, -1, -1d, -1, misc));
                    }
                }
                if (S.size() > 1) {
                    SNP first_snp = S.remove(0);
                    buffer.addAll(S);
                    return first_snp;
                } else if (S.size() == 1) {
                    return S.firstElement();
                }
            }
            throw new NoSuchElementException("Could not get any more reads.");
        } catch (IOException io) {
            LB.error("Error occured on line " + this.linecount);
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
        return null;
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
