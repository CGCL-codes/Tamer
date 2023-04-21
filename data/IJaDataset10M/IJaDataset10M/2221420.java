package org.ais.convert.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.ais.convert.Config;
import org.ais.convert.Constants;
import org.ais.convert.EachFileProcessor;
import org.ais.convert.Main;
import org.ais.convert.Parameters;
import org.ais.convert.Result;
import org.ais.convert.Tokenizer;

public class Ped2Raw extends EachFileProcessor {

    private Record rec;

    private char FIELDS_DELIMITER = '\t';

    private char OUTPUT_DELIMITER = '\t';

    private String EXT = ".txt";

    private String LINE_SEPARATOR_IN_OUTPUT = System.getProperty("line.separator");

    private String SEPARATORS_IN_MAPPING = " \t";

    private int lineNum;

    private String mapfile;

    private int snipsLineNum = 0;

    public void init(Result result) throws Exception {
        if (Parameters.getObject(Constants.INPUT_FILE_KEY) == null) {
            result.addError();
            System.out.println("You should pass at least one input file");
            return;
        }
        mapfile = Parameters.getString(Constants.SNIPS_MAPPING_KEY);
        if (mapfile == null) {
            mapfile = Config.getDefaultInputsDir() + File.separator + "snips_short";
            if (Main.warning) System.out.println("WARN: Snips not passed, default is used: " + mapfile);
        }
    }

    /**
	 * We have to overwrite parent's method since here we pass directory as output instead of one file to append the results to  
	 */
    protected void processDir(String filesDir) throws Exception {
        File mainDir = new File(filesDir);
        int entries = mainDir.list().length;
        String[] level1files = mainDir.list();
        if (Main.progressListener != null) Main.progressListener.start(entries);
        for (int i = 0; i < entries; i++) {
            String filePath = filesDir + File.separator + level1files[i];
            File aFile = new File(filePath);
            if (aFile.isDirectory()) {
                continue;
            } else if (aFile.isFile()) {
                processFile(filePath, null);
                if (Main.progressListener != null) Main.progressListener.updateStatus();
            }
        }
        if (Main.progressListener != null) Main.progressListener.finish();
    }

    public boolean processFile(String filePath, BufferedWriter writer) throws Exception {
        BufferedReader reader = null;
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                System.out.println("Cannot read file " + filePath);
                System.out.println();
                return false;
            }
            reader = new BufferedReader(new FileReader(file));
            while (readline(reader)) {
            }
            if (Main.trace) System.out.println(lineNum + " lines processed");
            return true;
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * Returns false - if EOF or there is no mapping file (so we have to terminate), true - if the line has been processed correctly
	 */
    private boolean readline(BufferedReader reader) throws Exception {
        BufferedWriter writer = null;
        int c;
        try {
            c = reader.read();
            if (c == -1) return false;
            lineNum++;
            while (c == '\r') c = reader.read();
            if (c == '\n') {
                return true;
            }
            int i = 0;
            StringBuffer name = new StringBuffer();
            while (true) {
                name.append((char) c);
                if (i > 255 - EXT.length()) {
                    break;
                }
                if ((c = reader.read()) == -1) return false;
                if (c == '\n') return true;
                while (c == '\r') c = reader.read();
                if (c == FIELDS_DELIMITER) {
                    break;
                }
            }
            for (i = 0; i < 5; i++) {
                while (true) {
                    c = reader.read();
                    while (c == '\r') c = reader.read();
                    if (c == -1) return false;
                    if (c == '\n') return true;
                    if (c == FIELDS_DELIMITER) {
                        break;
                    }
                }
            }
            String outputFilePath;
            String outputDir = Parameters.getString(Constants.OUTPUT_DIR_KEY);
            if (outputDir != null) {
                File file = new File(outputDir);
                if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
                    System.out.println("Directory " + outputDir + " does not exist or you do not have permissions to write into it.");
                    System.out.println();
                    return false;
                }
                outputFilePath = outputDir + File.separator + name + EXT;
            } else {
                outputFilePath = name + EXT;
            }
            if (Main.trace) System.out.println(outputFilePath);
            File outFile = new File(outputFilePath);
            writer = new BufferedWriter(new FileWriter(outFile, false));
            writer.append(getHeader());
            return read_genome(reader, writer);
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private boolean read_genome(BufferedReader reader, BufferedWriter writer) throws Exception {
        char[] biallele = new char[2];
        BufferedReader mapReader = null;
        int allelesNumInPed = 0;
        File file = new File(mapfile);
        if (!file.exists() || !file.canRead()) {
            System.out.println("Cannot read mapping (snips) file " + mapfile);
            System.out.println();
            return false;
        }
        try {
            mapReader = new BufferedReader(new FileReader(file));
            while (true) {
                if (!read_biallele(reader, biallele, allelesNumInPed)) {
                    break;
                }
                allelesNumInPed++;
                rec = new Record();
                if (!read_mapping_line(mapReader, snipsLineNum)) {
                    System.out.println("WARN: Line " + lineNum + ": Number of valid alleles in SNP map (" + snipsLineNum + " is in total) is less than number of alleles in PED file (" + allelesNumInPed + ")");
                    return false;
                }
                writer.write(rec.rsid);
                writer.write(OUTPUT_DELIMITER);
                writer.write(rec.chromo);
                writer.write(OUTPUT_DELIMITER);
                writer.write(rec.position);
                writer.write(OUTPUT_DELIMITER);
                writer.write(biallele);
                writer.write(LINE_SEPARATOR_IN_OUTPUT);
            }
            if (read_mapping_line(mapReader, snipsLineNum)) {
                System.out.println("WARN: Line " + lineNum + ": PED is incomplete, number of valid records in SNP map (" + snipsLineNum + " is in total) is bigger than number of alleles in PED file (" + allelesNumInPed + ")");
                return false;
            }
            return true;
        } finally {
            if (mapReader != null) try {
                mapReader.close();
            } catch (Exception e) {
            }
        }
    }

    private boolean read_biallele(BufferedReader reader, char[] biallele, int allelesNumInPed) throws Exception {
        int c;
        c = reader.read();
        if (c == -1 || c == '\n' || c == '\r') {
            return false;
        }
        while (c == FIELDS_DELIMITER) {
            c = reader.read();
        }
        biallele[0] = (char) c;
        c = reader.read();
        if (c == -1 || c == '\n' || c == '\r') {
            return false;
        }
        if (c != ' ') System.out.println("WARN: Alleles in position " + allelesNumInPed + " in line " + lineNum + " are delimited by " + c + " instead of space");
        c = reader.read();
        if (c == -1 || c == '\n' || c == '\r') {
            return false;
        }
        biallele[1] = (char) c;
        return true;
    }

    /**
	 * Returns: true if everything is fine and record is written or false - if EOF
	 */
    private boolean read_mapping_line(BufferedReader mapReader, int snipsLineNum) throws Exception {
        String line;
        while ((line = mapReader.readLine()) != null) {
            snipsLineNum++;
            if (line.startsWith("#")) continue;
            if (!deserialize(line, snipsLineNum)) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
	 * Returns: true if everything is fine and record is written, false - if the line is incomplete
	*/
    private boolean deserialize(String line, int snipsLineNum) {
        String[] tokens = Tokenizer.tokenize(line, SEPARATORS_IN_MAPPING);
        if (tokens.length < 4) {
            System.out.println("WARN: line " + snipsLineNum + " in mapping is incomplete -- skipped");
            return false;
        }
        rec.chromo = tokens[0];
        rec.rsid = tokens[1];
        rec.position = tokens[3];
        return true;
    }

    private String getHeader() {
        return "# This data file generated by ped2raw tool" + LINE_SEPARATOR_IN_OUTPUT + "#" + LINE_SEPARATOR_IN_OUTPUT + "# This file resembles 23andme format, so the following is description from its header:" + LINE_SEPARATOR_IN_OUTPUT + "#" + LINE_SEPARATOR_IN_OUTPUT + "# Fields are TAB-separated" + "# Each line corresponds to a single SNP.  For each SNP, we provide its identifier" + LINE_SEPARATOR_IN_OUTPUT + "# (an rsid or an internal id), its location on the reference human genome, and the" + LINE_SEPARATOR_IN_OUTPUT + "# genotype call oriented with respect to the plus strand on the human reference" + LINE_SEPARATOR_IN_OUTPUT + "# sequence.     We are using reference human assembly build 36.  Note that it is possible" + LINE_SEPARATOR_IN_OUTPUT + "# that data downloaded at different times may be different due to ongoing improvements" + LINE_SEPARATOR_IN_OUTPUT + "# in our ability to call genotypes. More information about these changes can be found at:" + LINE_SEPARATOR_IN_OUTPUT + "# https://www.23andme.com/you/download/revisions/" + LINE_SEPARATOR_IN_OUTPUT + "#" + LINE_SEPARATOR_IN_OUTPUT + "# More information on reference human assembly build 36:" + LINE_SEPARATOR_IN_OUTPUT + "# http://www.ncbi.nlm.nih.gov/projects/mapview/map_search.cgi?taxid=9606&build=36" + LINE_SEPARATOR_IN_OUTPUT + "#" + LINE_SEPARATOR_IN_OUTPUT + "# rsid    chromosome    position    genotype" + LINE_SEPARATOR_IN_OUTPUT + "";
    }

    class Record {

        public String rsid;

        public String chromo;

        public String position;
    }
}
