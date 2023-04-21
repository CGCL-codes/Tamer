package apollo.analysis;

import java.util.*;
import java.io.*;
import apollo.seq.io.*;
import apollo.analysis.*;
import apollo.datamodel.*;

public class BlastHandler extends GenericProgramHandler {

    /**
   * different analysis programs require different ways of
   * running them
   *
   * this method is only called by SeqAnalysisLocal
   **/
    public String createUnixShellCommand(SeqAnalysisLocal seqAnalysis) {
        String path = "blastall";
        String programName = seqAnalysis.getProperty("programName");
        String dbpath = seqAnalysis.getProperty("datasourcePath");
        if (dbpath == null) {
            String dbdir = seqAnalysis.getProperty("datasourceDir");
            String dbname = seqAnalysis.getProperty("datasourceName");
            if (dbdir == null) {
                dbpath = dbname;
            } else {
                dbpath = dbdir + "/" + dbname;
            }
        }
        String cmd = "";
        String seqpath = seqAnalysis.getSeqFile();
        try {
            String args = seqAnalysis.getProperty("programParams");
            if (args == null) {
                args = "";
            }
            cmd = path + " -p " + programName + " -d " + dbpath + " " + " -i " + seqpath + " " + args;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmd;
    }

    /**
   * different programs must be parsed differently
   * implementations may also choose to also filter the results
   * in some configurable way. it is up to the individual
   * implementations to respect these
   **/
    public CurationSet parseAnalysisResults(SeqAnalysisI seqAnalysis) {
        System.out.println("not implementated!");
        return null;
    }
}
