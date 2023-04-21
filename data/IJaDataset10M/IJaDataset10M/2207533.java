package gr.konstant.transonto.app;

import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.kb.powder.PowderOWL;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.PropertyConfigurator;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;

public class POWDERProc {

    public static boolean debug;

    private static final int EXIT_TODO = 1;

    private static final int EXIT_OK = 0;

    private static final int EXIT_CMDLINE_ERROR = -1;

    private static final int EXIT_FILE_ERROR = -2;

    private static final int EXIT_INTERNAL_ERROR = -3;

    private static Options options = null;

    private static final String OPTION_DEBUG = "d";

    private static final String OPTION_INPUT = "f";

    private static final String OPTION_OUTPUT = "o";

    private static final String OPTION_ATTR = "a";

    private static final String OPTION_ABBREV = "b";

    private static final String OPTION_HELP = "h";

    private static final String OPTION_VERSION = "V";

    static {
        options = new Options();
        options.addOption(OPTION_DEBUG, false, "Help");
        options.addOption(OPTION_INPUT, true, "Help");
        options.addOption(OPTION_OUTPUT, true, "Help");
        options.addOption(OPTION_ATTR, false, "Help");
        options.addOption(OPTION_ABBREV, false, "Help");
        options.addOption(OPTION_HELP, false, "Help");
        options.addOption(OPTION_VERSION, false, "Help");
    }

    private static void printVersion() {
        System.out.println("This is SemPP, the Semantic POWDER Processor,");
        System.out.println("released as part of TransOnto v0.5");
        System.out.println("Release date: 18/5/2009");
        System.out.println("");
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("sempp <POWDER files or URLs> [-f <infile>] [-o <outfile>] [-a]");
        System.out.println("  Describe the resources in <infile> and write the descriptions in <outfile>");
        System.out.println("  At least one POWDER file or URL must be provided.");
        System.out.println("  Local files and remote locations can be freely mixed.");
        System.out.println("  -f <infile> file with newline-separated list of URIs to be described");
        System.out.println("     if this parameter is missing or is -, stdin is used");
        System.out.println("  -o <outfile> file where the description will be written");
        System.out.println("     if this parameter is missing or is -, stdout is used");
        System.out.println("     if file already exists, it will be overwritten");
        System.out.println("  -a include attribution information in the output (non-standard behaviour)");
        System.out.println("  -b serialize response as abbreviated XML (default is verbose)");
        System.out.println("powderproc -V");
        System.out.println("  Print release infromation and exit");
        System.out.println("  Can only be combined with -h");
        System.out.println("powderproc -h");
        System.out.println("  Print this screen and exit");
        System.out.println("  Can only be combined with -V");
        System.out.println("");
        System.out.println("Exit values:");
        System.out.println("   0: normal termination");
        System.out.println("  -1: inconsistent or missing command-line parameters");
        System.out.println("  -2: missing or ill-formed input");
        System.out.println("  -3: internal engine error (most probably a bug)");
        System.out.println("");
    }

    public static void main(String[] args) {
        System.out.println("Transonto knowledge transformation and migration system");
        System.out.println("Transonto Copyright (C) 2006-2009 Stasinos Konstantopoulos <stasinos@users.sourceforge.net>");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
        System.out.println("Transonto is free software; you can redistribute it and/or modify");
        System.out.println("it under the terms of the GNU General Public License as published by");
        System.out.println("the Free Software Foundation; either version 3 of the License, or");
        System.out.println("(at your option) any later version.");
        System.out.println("");
        int retv = EXIT_TODO;
        CommandLineParser parser;
        parser = new PosixParser();
        CommandLine cmdline;
        try {
            cmdline = parser.parse(options, args, false);
        } catch (ParseException ex) {
            printUsage();
            cmdline = null;
            retv = EXIT_CMDLINE_ERROR;
        }
        if (retv == EXIT_TODO) {
            if (cmdline.hasOption(OPTION_HELP) && cmdline.hasOption(OPTION_VERSION)) {
                printVersion();
                printUsage();
                retv = EXIT_OK;
            } else if (cmdline.hasOption(OPTION_HELP)) {
                printUsage();
                retv = EXIT_OK;
            } else if (cmdline.hasOption(OPTION_VERSION)) {
                printVersion();
                retv = EXIT_OK;
            }
        }
        if (retv == EXIT_TODO) {
            if (cmdline.hasOption(OPTION_DEBUG)) {
                debug = true;
            } else {
                debug = false;
            }
        } else {
            debug = false;
        }
        boolean includeAttribution;
        if (retv == EXIT_TODO) {
            if (cmdline.hasOption(OPTION_ATTR)) {
                includeAttribution = true;
            } else {
                includeAttribution = false;
            }
        } else {
            includeAttribution = false;
        }
        InputStream is;
        if (retv == EXIT_TODO) {
            if (cmdline.hasOption(OPTION_INPUT)) {
                String str = cmdline.getOptionValue(OPTION_INPUT);
                if (str.compareTo("-") == 0) {
                    is = System.in;
                } else {
                    try {
                        is = new FileInputStream(str);
                    } catch (FileNotFoundException ex) {
                        System.err.println("File not found: " + str);
                        is = null;
                        retv = EXIT_FILE_ERROR;
                    }
                }
            } else {
                is = System.in;
            }
        } else {
            is = null;
        }
        OutputStream os;
        if (retv == EXIT_TODO) {
            if (cmdline.hasOption(OPTION_OUTPUT)) {
                String str = cmdline.getOptionValue(OPTION_OUTPUT);
                if (str.compareTo("-") == 0) {
                    os = System.out;
                } else {
                    try {
                        os = new FileOutputStream(str);
                    } catch (FileNotFoundException ex) {
                        System.err.println("File not found: " + str);
                        os = null;
                        retv = EXIT_FILE_ERROR;
                    }
                }
            } else {
                os = System.out;
            }
        } else {
            os = null;
        }
        PowderOWL powderDoc;
        if (retv == EXIT_TODO) {
            String[] leftovers = cmdline.getArgs();
            if (leftovers.length == 0) {
                System.err.println("No POWDER document specified.");
                System.err.println("");
                printUsage();
                powderDoc = null;
                retv = EXIT_CMDLINE_ERROR;
            } else {
                URL u;
                try {
                    u = new URL(leftovers[0]);
                } catch (MalformedURLException ex) {
                    u = null;
                }
                if (u == null) {
                    try {
                        powderDoc = new PowderOWL(leftovers[0]);
                    } catch (BackendException ex) {
                        System.err.println("File error: " + ex.getMessage());
                        powderDoc = null;
                        retv = EXIT_FILE_ERROR;
                    } catch (UnsupportedFeatureException ex) {
                        System.err.println("Error: " + ex.getMessage());
                        powderDoc = null;
                        retv = EXIT_FILE_ERROR;
                    }
                } else {
                    try {
                        powderDoc = new PowderOWL(u);
                    } catch (BackendException ex) {
                        System.err.println("File error: " + ex.getMessage());
                        powderDoc = null;
                        retv = EXIT_FILE_ERROR;
                    } catch (UnsupportedFeatureException ex) {
                        System.err.println("Error: " + ex.getMessage());
                        powderDoc = null;
                        retv = EXIT_FILE_ERROR;
                    }
                }
                for (int i = 1; (i < leftovers.length) && (retv == EXIT_TODO); ++i) {
                    try {
                        u = new URL(leftovers[i]);
                    } catch (MalformedURLException ex) {
                        u = null;
                    }
                    PowderOWL morePowder;
                    if (u == null) {
                        try {
                            morePowder = new PowderOWL(leftovers[i]);
                        } catch (BackendException ex) {
                            System.err.println("File error: " + ex.getMessage());
                            morePowder = null;
                            retv = EXIT_FILE_ERROR;
                        } catch (UnsupportedFeatureException ex) {
                            System.err.println("Error: " + ex.getMessage());
                            morePowder = null;
                            retv = EXIT_FILE_ERROR;
                        }
                    } else {
                        try {
                            morePowder = new PowderOWL(u);
                        } catch (BackendException ex) {
                            System.err.println("File error: " + ex.getMessage());
                            morePowder = null;
                            retv = EXIT_FILE_ERROR;
                        } catch (UnsupportedFeatureException ex) {
                            System.err.println("File error: " + ex.getMessage());
                            morePowder = null;
                            retv = EXIT_FILE_ERROR;
                        }
                    }
                    if (morePowder != null) {
                        powderDoc.add(morePowder);
                    }
                }
            }
        } else {
            powderDoc = null;
        }
        if (retv == EXIT_TODO) {
            Scanner inp = new Scanner(is);
            String u;
            Set<String> uu = new HashSet<String>();
            try {
                u = inp.nextLine();
            } catch (NoSuchElementException ex) {
                u = null;
            }
            while (u != null) {
                uu.add(u);
                try {
                    u = inp.nextLine();
                    if (u.length() == 0) {
                        u = null;
                    }
                } catch (NoSuchElementException ex) {
                    u = null;
                }
            }
            try {
                Model ans = powderDoc.describe(uu, includeAttribution);
                if (cmdline.hasOption(OPTION_ABBREV)) {
                    RDFWriter rdfWriter = ans.getWriter("RDF/XML-ABBREV");
                    Resource[] pretty = new Resource[2];
                    pretty[0] = ans.createResource("http://www.w3.org/2007/05/powder-s#Processor");
                    pretty[1] = ans.createResource("http://www.w3.org/2007/05/powder-s#Document");
                    rdfWriter.setProperty("prettyTypes", pretty);
                    rdfWriter.write(ans, os, null);
                } else {
                    ans.write(os);
                }
                retv = EXIT_OK;
            } catch (Exception ex) {
                System.err.print(ex.getMessage());
                retv = EXIT_INTERNAL_ERROR;
            }
        }
        System.exit(retv);
    }
}
