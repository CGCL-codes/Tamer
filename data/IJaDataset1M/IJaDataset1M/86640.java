package src.lib.ioInterfaces;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;
import src.lib.Constants;
import src.lib.Error_handling.UnexpectedResultException;
import src.lib.objects.AlignedRead;

public class GffIterator implements AlignedReadsIterator {

    private static boolean display_version = true;

    BufferedReader br = null;

    int linecount = 0;

    private final Log_Buffer LB;

    final int max_PET_len;

    int number_filtered;

    public GffIterator(Log_Buffer log_file, String source_file, int max_PET_len) {
        LB = log_file;
        if (display_version) {
            LB.Version("GFFIterator", "$Revision: 2933 $");
            display_version = false;
        }
        this.max_PET_len = max_PET_len;
        this.number_filtered = 0;
        try {
            if (source_file.endsWith(".gz")) {
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(source_file))));
            } else {
                br = new BufferedReader(new FileReader(source_file));
            }
        } catch (FileNotFoundException e) {
            LB.error("Can't open file " + source_file);
            LB.die();
        } catch (IOException io) {
            LB.error("Error processing gzipped bed file " + source_file);
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
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

    /**
     * 
     */
    public void close(boolean verbose) {
        try {
            this.br.close();
        } catch (IOException io) {
            LB.warning("Could not close Eland Ext. file");
            LB.warning("Message thrown by Java environment (may be null):" + io.getMessage());
        }
        if (verbose) {
            LB.notice("Processed " + this.linecount + " records");
        }
    }

    /** 
	 * Read in next line of the GFF file
	 * @return SNP object
	 */
    public AlignedRead next() {
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                while (line.startsWith("track") || line.startsWith("#")) {
                    line = br.readLine();
                    if (null == line) {
                        throw new NoSuchElementException("Could not get any more reads.");
                    }
                }
                this.linecount++;
                String[] fields = line.split("\t");
                final int fields_count = fields.length;
                if (fields.length < 9) {
                    LB.warning("Not enough fields: " + line);
                }
                int alignStart = Integer.valueOf(fields[3]);
                int alignEnd = Integer.valueOf(fields[4]);
                if (this.max_PET_len > 0 && (alignEnd - alignStart) > this.max_PET_len) {
                    this.number_filtered++;
                    continue;
                }
                int score = (fields[5].equals(".")) ? 0 : Integer.valueOf(fields[5]);
                int frame = (fields[7].equals(".")) ? 0 : Integer.valueOf(fields[7]);
                AlignedRead A = null;
                try {
                    A = new AlignedRead((fields[6].equals("-")) ? '-' : '+', fields[1], null, score, 0, 0, fields[2], fields[0], alignStart, alignEnd, 0, 0, (fields_count > 3) ? fields[3] : null, 0, -alignStart, 0, 0, frame, 0, (fields_count > 4) ? Integer.valueOf(fields[4]) : -1, null, 0, 0, 0, fields[8], null, null, null);
                } catch (UnexpectedResultException URE) {
                    LB.error("Line " + linecount + " has an invalid read:");
                    LB.error(URE.getMessage());
                }
                return A;
            }
            throw new NoSuchElementException("Tried to get next token, received a null");
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
            LB.error("Could not determine status of GFFIterator");
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
        return false;
    }

    /**
     * TODO need to be verified
     * 
	 * Return the number of filtered read while iterating.
	 * TODO change this function if the Iterator start filtering
	 * @return 0 in every case
	 */
    public int get_NumberFilteredRead() {
        return this.number_filtered;
    }

    /**
     * TODO need to be verified
     * @return 
	 */
    public boolean reset() {
        try {
            br.reset();
            return true;
        } catch (IOException ioe) {
            LB.error("Could not reset input file for read buffer.");
            return false;
        }
    }

    /**
	 * This function is not supported for this iterator.
	 */
    public void apply_filters(String f) {
        throw new UnsupportedOperationException();
    }
}
