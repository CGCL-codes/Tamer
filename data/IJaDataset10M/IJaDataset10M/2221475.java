package org.gbif.ecat;

import org.gbif.common.parsers.utils.ClassificationUtils;
import org.gbif.common.parsers.utils.MappingUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

/**
 * @author markus
 *
 */
public class AddCanonical {

    private static String clean(String x) {
        if (StringUtils.trimToNull(x) == null || x.equalsIgnoreCase("null") || x.equalsIgnoreCase("\\N")) {
            return "";
        }
        return x;
    }

    /**
   * @param args
   * @throws IOException
   */
    public static void main(String[] args) throws IOException {
        File in = new File("/Users/markus/Desktop/occurrence_classification_verbatim.csv");
        Writer out = new FileWriter("/Users/markus/Desktop/occ_classifications.txt");
        final Pattern TAB_DELIMITED = Pattern.compile("\t");
        LineIterator lines = FileUtils.lineIterator(in, "UTF-8");
        int x = 0;
        while (lines.hasNext()) {
            x++;
            if (x % 10000 == 0) {
                System.out.println("Processing " + x + "th record");
                out.flush();
            }
            String line = lines.nextLine();
            String[] cols = TAB_DELIMITED.split(line);
            for (String col : cols) {
                out.write(clean(col) + "\t");
            }
            if (cols.length > 9) {
                String k = cols[1];
                String p = cols[2];
                String c = cols[3];
                String o = cols[4];
                String f = cols[5];
                String sn = cols[9];
                String sn2 = StringUtils.defaultString(ClassificationUtils.canonicalName(sn), "");
                String k2 = StringUtils.defaultString(MappingUtils.mapKingdom(k), "");
                String p2 = StringUtils.defaultString(MappingUtils.mapPhylum(p), "");
                String c2 = StringUtils.defaultString(ClassificationUtils.clean(c), "");
                String o2 = StringUtils.defaultString(ClassificationUtils.clean(o), "");
                String f2 = StringUtils.defaultString(ClassificationUtils.clean(f), "");
                out.write(sn2 + "\t" + k2 + "\t" + p2 + "\t" + c2 + "\t" + o2 + "\t" + f2 + "\n");
            } else {
                out.write("\t\t\t\t\t\n");
            }
        }
        out.flush();
        out.close();
        lines.close();
        System.out.println("DONE processign " + x + " records");
    }
}
