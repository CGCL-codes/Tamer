package net.sf.csv2sql.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import net.sf.csv2sql.storage.exceptions.StorageException;
import net.sf.csv2sql.writers.exceptions.InvalidParameterValueException;
import net.sf.csv2sql.writers.exceptions.WriterException;

/**
 * Write a new textfile with generated statements.
 * @see AbstractWriter AbstractWriter
 * @author <a href="mailto:dconsonni@enter.it">Davide Consonni</a>
 */
public class SqlMultipleFileWriter extends AbstractWriter {

    public static String formatSequence(int currentPage, int totalPages) {
        StringBuffer sb = new StringBuffer();
        int zeroToAdd = (String.valueOf(totalPages)).length() - (String.valueOf(currentPage)).length();
        for (int i = 0; i < zeroToAdd; i++) {
            sb.append("0");
        }
        sb.append(String.valueOf(currentPage));
        return sb.toString();
    }

    private int calculateBreakLines() throws InvalidParameterValueException {
        int breaklines = 10;
        String bl = getWriterProperties().getProperty("breaklines");
        breaklines = (bl != null) ? Integer.parseInt(bl) : 10;
        if (breaklines <= 0) {
            throw new InvalidParameterValueException("breaklines", getWriterProperties().getProperty("breaklines"));
        }
        return breaklines;
    }

    /**
     * @see AbstractWriter#write
     */
    public void write() throws WriterException {
        try {
            int breaklines = calculateBreakLines();
            int sequence = 0;
            float pages = getStorage().size() / breaklines;
            if (pages < 1) {
                File out = new File(getWriterProperties().getProperty("filename"));
                BufferedWriter output = new BufferedWriter(new FileWriter(out));
                for (int i = 0; i < getStorage().size(); i++) {
                    output.write(getStorage().get(i) + "\n");
                }
                output.close();
            } else {
                for (int i = 1; i <= pages; i++) {
                    File out = new File(getWriterProperties().getProperty("filename") + "_" + formatSequence(i, (int) pages));
                    BufferedWriter output = new BufferedWriter(new FileWriter(out));
                    int from = breaklines * (i - 1);
                    int to = (breaklines * (i));
                    for (int j = from; j < to; j++) {
                        sequence++;
                        output.write(getStorage().get(j) + "\n");
                    }
                    output.flush();
                    output.close();
                }
                if (pages != 1) {
                    File out = new File(getWriterProperties().getProperty("filename") + "_" + formatSequence((int) pages, (int) pages));
                    BufferedWriter output = new BufferedWriter(new FileWriter(out));
                    for (int i = sequence; i < getStorage().size(); i++) {
                        sequence++;
                        output.write(getStorage().get(i) + "\n");
                    }
                    output.flush();
                    output.close();
                }
            }
        } catch (StorageException e) {
            throw new WriterException("cannot read data from temporary storage", e);
        } catch (IOException e) {
            throw new WriterException("cannot write statement to file", e);
        }
    }

    protected HashMap requiredParameterList() {
        HashMap hm = new HashMap();
        hm.put("filename", "output filename where result will be stored.");
        return hm;
    }

    protected HashMap optionalParameterList() {
        HashMap hm = new HashMap();
        hm.put("breaklines", "number of lines before break the csvfile 10 if not specified.");
        return hm;
    }
}
