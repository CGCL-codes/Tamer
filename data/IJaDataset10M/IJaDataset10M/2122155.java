package gov.lanl.xmltape.index.berkeleydbImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import gov.lanl.xmltape.index.TapeIndexer;
import gov.lanl.xmltape.index.sets.SetSpecNamespace;
import gov.lanl.xmltape.index.sets.SetSpecProfile;
import gov.lanl.xmltape.index.sets.SetSpecProfileFactory;
import gov.lanl.xmltape.index.sets.SetSpecXPath;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * $Id: TapeIndexerApp.java 1286 2005-01-21 19:25:16Z liu_x $
 */
public class TapeIndexerApp {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("s", false, "index setSpec");
        options.addOption("p", true, "setSpec properties file");
        CommandLineParser parser = new PosixParser();
        boolean indexSetspec = false;
        String tapefile = null;
        String indexdir = null;
        Properties setSpecProps = null;
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("s") || cmd.hasOption("p")) {
                indexSetspec = true;
                if (cmd.hasOption("p")) {
                    setSpecProps = new Properties();
                    String prop = cmd.getOptionValue("p");
                    setSpecProps.load(new FileInputStream(new File(prop)));
                } else {
                    indexSetspec = false;
                    System.out.println("SetSpec Properties Required to index set information; disabling set indexing");
                }
            }
            String[] str = cmd.getArgs();
            if (str.length != 2) throw new ParseException("");
            tapefile = str[0];
            indexdir = str[1];
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java gov.lanl.xmltape.index.berkeleydbImpl.TapeIndexerApp [-s] [-p] <tapefile> <indexdir>", options);
            System.exit(-1);
        }
        String[] paths = tapefile.split("/");
        String tapename = paths[paths.length - 1];
        if (tapename.indexOf(".") != -1) tapename = tapename.substring(0, tapename.indexOf("."));
        long start = System.currentTimeMillis();
        BDBIndex indexdb = new BDBIndex(indexdir, tapename);
        System.out.println(tapename);
        TapeIndexer indexer = new TapeIndexer(indexdb);
        if (indexSetspec) {
            SetSpecProfile sspp = SetSpecProfileFactory.generateSetSpecProfile(setSpecProps);
            for (Iterator i = sspp.getNamespaces().iterator(); i.hasNext(); ) {
                SetSpecNamespace ssn = (SetSpecNamespace) i.next();
                indexer.addSetNamespaces(ssn.getNamespacePrefix(), ssn.getNamespace());
            }
            for (Iterator j = sspp.getXpaths().iterator(); j.hasNext(); ) {
                SetSpecXPath ssx = (SetSpecXPath) j.next();
                indexer.addSetElementXPath(ssx.getXpath(), ssx.getXpathPrefix());
            }
        }
        indexer.parse(tapefile);
        long duration = System.currentTimeMillis() - start;
        System.out.println("\nTotal Indexing Time: " + duration + " ms");
    }
}
