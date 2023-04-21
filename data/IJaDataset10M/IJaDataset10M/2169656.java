package org.cobertura4j2me.merge;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cobertura4j2me.runtime.CoverageDataPersistence;
import org.cobertura4j2me.runtime.Factory;

public class Main {

    public Main(String[] args) throws Exception {
        LongOpt[] longOpts = new LongOpt[4];
        longOpts[0] = new LongOpt("instrumentation", LongOpt.REQUIRED_ARGUMENT, null, 'i');
        longOpts[1] = new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o');
        longOpts[2] = new LongOpt("datafile", LongOpt.OPTIONAL_ARGUMENT, null, 'f');
        longOpts[3] = new LongOpt("debug", LongOpt.OPTIONAL_ARGUMENT, null, 'd');
        Getopt g = new Getopt(getClass().getName(), args, ":i:o:f:d:", longOpts);
        int c;
        File destDir = new File(System.getProperty("user.dir"));
        System.setProperty("cobertura4j2me-dontloadoninitialisation", "true");
        CoverageDataPersistence persistence = Factory.getInstance().getCoverageDataPersistence();
        String dataFile = new File(destDir, CoverageDataPersistence.DEFAULT_FILE_NAME).getAbsolutePath();
        Factory.getInstance().stopTimer();
        try {
            while ((c = g.getopt()) != -1) {
                switch(c) {
                    case 'i':
                        System.out.println("cobertura loading: " + g.getOptarg());
                        try {
                            persistence.setDataFile(g.getOptarg());
                            persistence.merge(persistence.load());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 'o':
                        destDir = new File(g.getOptarg());
                        destDir.mkdirs();
                        break;
                    case 'f':
                        dataFile = g.getOptarg();
                        break;
                    case 'd':
                        if (Boolean.parseBoolean(g.getOptarg())) {
                            Factory.setTraceLevel(Factory.TRACE_ALL);
                        }
                        break;
                }
            }
            if (dataFile != null) {
                persistence.setDataFile(dataFile);
            }
            persistence.save();
        } catch (Exception e) {
            Factory.trace(Factory.TRACE_EXTENDED, e);
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Cobertura For J2ME instrumentation merge tool");
        boolean hasCommandsFile = false;
        String commandsFileName = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-commandsfile")) {
                hasCommandsFile = true;
                commandsFileName = args[++i];
            }
        }
        if (hasCommandsFile) {
            List arglist = new ArrayList();
            BufferedReader bufreader = null;
            try {
                bufreader = new BufferedReader(new FileReader(commandsFileName));
                String line;
                while ((line = bufreader.readLine()) != null) {
                    arglist.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufreader != null) {
                    try {
                        bufreader.close();
                    } catch (IOException e) {
                    }
                }
            }
            args = (String[]) arglist.toArray(new String[arglist.size()]);
        }
        new Main(args);
    }
}
