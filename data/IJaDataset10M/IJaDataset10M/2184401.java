package jalview.analysis.blast;

import jalview.datamodel.*;
import com.stevesoft.pat.*;
import java.io.*;
import java.util.*;

public class BPLite {

    static Regex reg_query = new Regex("^Query=\\s+(\\S+)");

    static Regex reg_null = new Regex("\\S");

    static Regex reg_replace_space = new Regex("(\\s+)", "");

    static Regex reg_fix_header = new Regex("^(>)", "");

    static Regex reg_parse_header = new Regex("\\((\\d+)\\s+\\S+\\)\\s*$");

    static Regex reg_database = new Regex("^Database:\\s+(.+)");

    static Regex reg_pattern = new Regex("^\\s*pattern\\s+(\\S+).*position\\s+(\\d+)\\D");

    static Regex reg_end_dat = new Regex("^Parameters|^\\s+Database:");

    static Regex reg_end_dat2 = new Regex("^Parameters|^\\s+Database:|^\\s+Posted date:");

    static Regex reg_header = new Regex("^>");

    static Regex reg_word = new Regex("(\\S+)");

    static Regex reg_strand = new Regex("Strand HSP");

    static Regex reg_score = new Regex("^\\s{0,2}Score");

    static Regex reg_parameters = new Regex("^Parameters|^Database:|Posted date:");

    static Regex reg_posted = new Regex("Posted date");

    static Regex reg_length = new Regex("Length = ([\\d,]+)$", "");

    static Regex reg_replace_end_space = new Regex("\\s+$", "");

    static Regex reg_hitid = new Regex("^>(\\S+)");

    String last_line = "";

    String query_line = "";

    String name = "";

    String program = "";

    boolean report_done = false;

    int length;

    Hit current_hit = null;

    BufferedReader reader;

    public BPLite(BufferedReader reader) throws IOException {
        this.reader = reader;
        parseHeader();
    }

    public void parseHeader() throws IOException {
        report_done = false;
        boolean header_flag = false;
        String line = "";
        String query_string = "";
        String database = "";
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            if (!found) {
                if (reg_word.search(line)) {
                    program = reg_word.stringMatched(1);
                }
                found = true;
            }
            if (reg_query.search(line)) {
                header_flag = true;
                query_string = reg_query.stringMatched(1);
                setName(query_string);
                while ((line = reader.readLine()) != null) {
                    if (!reg_null.search(line)) {
                        break;
                    }
                    query_string = query_string + line;
                }
                reg_replace_space.replaceFirst(query_string);
                reg_fix_header.replaceFirst(query_string);
                if (reg_parse_header.search(query_string)) {
                    length = Integer.parseInt(reg_parse_header.stringMatched(1));
                    query_line = query_string;
                }
            } else if (reg_database.search(line)) {
                header_flag = true;
                database = reg_database.stringMatched(1);
            } else if (reg_header.search(line)) {
                last_line = line;
                return;
            } else if (reg_end_dat.search(line)) {
                header_flag = true;
                last_line = line;
                return;
            }
        }
    }

    public HSP next_feature() throws IOException {
        Hit hit = current_hit;
        if (hit == null) {
            current_hit = nextHit();
            hit = current_hit;
            if (hit == null) {
                return null;
            }
        }
        HSP hsp = hit.nextHSP();
        if (hsp == null) {
            current_hit = null;
            return next_feature();
        }
        return hsp;
    }

    public int fast_forward() throws IOException {
        if (report_done) {
            return 0;
        }
        if (reg_end_dat2.search(last_line)) {
            return 0;
        }
        if (reg_header.search(last_line)) {
            return 1;
        }
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (reg_end_dat2.search(line)) {
                last_line = line;
                return 1;
            }
        }
        return 0;
    }

    public Hit nextHit() throws IOException {
        if (fast_forward() == 0) {
            return null;
        }
        String def = last_line;
        String database;
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (!reg_word.search(line)) {
                continue;
            } else if (reg_strand.search(line)) {
                continue;
            } else if (reg_score.search(line)) {
                last_line = line;
                break;
            } else if (reg_parameters.search(def)) {
                last_line = line;
                break;
            } else {
                def = def + line;
            }
        }
        if (reg_header.search(def) == false) {
            return null;
        } else {
            reg_replace_space.replaceAll(def);
            reg_replace_end_space.replaceAll(def);
            reg_length.replaceFirst(def);
            length = Integer.parseInt(reg_length.stringMatched(1));
            String hitid = "";
            if (reg_hitid.search(def)) {
                hitid = reg_hitid.stringMatched(1);
            }
            reg_fix_header.replaceFirst(def);
            Hit hit = new Hit(reader);
            hit.setHitName(hitid);
            hit.setName(getName());
            hit.setLength(length);
            hit.setLastLine(last_line);
            hit.setParent(this);
            return hit;
        }
    }

    public String getLastLine() {
        return last_line;
    }

    public void setLastLine(String line) {
        this.last_line = line;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public BufferedReader getBufferedReader() {
        return reader;
    }

    public void setBufferedReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BPLite bp = new BPLite(reader);
            Hit hit = null;
            while ((hit = bp.nextHit()) != null) {
                HSP hsp = null;
                System.out.println("\nHit " + hit);
                while ((hsp = hit.nextHSP()) != null) {
                    System.out.println("\nHSP " + hsp);
                    if (args.length == 2) {
                        Vector f = hsp.getUngappedFeatures();
                        System.out.println("\nUngapped " + f.size() + "\n");
                        for (int i = 0; i < f.size(); i++) {
                            apollo.datamodel.FeaturePair fp = (apollo.datamodel.FeaturePair) f.elementAt(i);
                            System.out.println("FP " + fp.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException " + e);
        }
    }
}
