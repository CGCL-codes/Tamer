package edu.ucdavis.genomics.metabolomics.binbase.connector.references.sdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.connector.references.exception.ResolverException;

/**
 * splits one file into several smaller files
 * 
 * @author wohlgemuth
 * 
 */
public class SDFSplitter {

    public static void split(File input, File outputDir) throws IOException, ResolverException {
        Scanner scanner = new Scanner(input);
        Logger logger = Logger.getLogger(SDFSplitter.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String id = "";
        boolean first = true;
        boolean invalidId = false;
        boolean scanId = false;
        boolean hmdb = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("$$$$")) {
                if (hmdb) {
                    out.write("> <HMDB_ID>\n".getBytes());
                    out.write(id.getBytes());
                    out.write("\n".getBytes());
                    out.write("\n".getBytes());
                }
            }
            out.write(line.getBytes());
            out.write("\n".getBytes());
            if (first == true) {
                first = false;
                id = line.trim();
                if (id.length() == 0) {
                    invalidId = true;
                } else {
                    if (id.contains("Beginning of SDF File of HMDB")) {
                        id = id.replace("Beginning of SDF File of ", "");
                        String ids[] = id.split("\t");
                        id = ids[0].trim();
                        logger.info("found id: " + id);
                        hmdb = true;
                    }
                }
            }
            if (invalidId) {
                if (line.indexOf("> <") == 0) {
                    scanId = true;
                } else {
                    if (scanId) {
                        id = line.trim();
                        scanId = false;
                        invalidId = false;
                    }
                }
            }
            if (line.equals("$$$$")) {
                logger.info("writing: " + id);
                FileOutputStream os = new FileOutputStream(new File(outputDir, id + ".txt"));
                os.write(out.toByteArray());
                out = new ByteArrayOutputStream();
                first = true;
                invalidId = false;
            }
        }
    }
}
