package com.illengineer.jcc;

import java.io.*;
import java.util.*;
import jargs.gnu.CmdLineParser;

public class CLI {

    private static boolean debug = false;

    private static final int MAXPARTS = 8;

    public static void usage() {
        System.err.println("Usage: CLI  {-f ctags_file | -j jar_file} [[-r split_regex] ... ]\n" + "            [-c] [-i] [-m minparts] [-l filter_regex]\n" + "		[-S serialized_data_file | -L serialied_data_file]\n" + "   -c = use CamelCase parsing\n" + "   -i = ignore case when expanding abbreviations\n" + "\nThe program will then read abbreviations on standard input, and\n" + "print lists of suggestions to standard output.\n" + "Enter a period on a line by itself to quit.");
    }

    private static void debugPrint(String s) {
        if (debug) System.err.println(s);
    }

    public static void main(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option fileNameOpt = parser.addStringOption('f', "ctags_file");
        CmdLineParser.Option jarNameOpt = parser.addStringOption('j', "jar_file");
        CmdLineParser.Option regexOpt = parser.addStringOption('r', "split_regex");
        CmdLineParser.Option filterOpt = parser.addStringOption('l', "filter_regex");
        CmdLineParser.Option camelOpt = parser.addBooleanOption('c', "camel_case");
        CmdLineParser.Option ignoreCaseOpt = parser.addBooleanOption('i', "ignore_case");
        CmdLineParser.Option minPartsOpt = parser.addIntegerOption('m', "minparts");
        CmdLineParser.Option debugOpt = parser.addBooleanOption('d', "debug");
        CmdLineParser.Option saveSerializedFileOpt = parser.addStringOption('S', "save_serial");
        CmdLineParser.Option loadSerializedFileOpt = parser.addStringOption('L', "load_serial");
        boolean ignoreCase;
        int minparts;
        File ctagsFile = null, jarFile = null, serialFile = null;
        String fileName = null, jarFileName = null;
        String loadSerialName = null, saveSerialName = null;
        String filterRegex;
        boolean parseError = false;
        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            parseError = true;
        }
        if (!parseError) {
            fileName = (String) parser.getOptionValue(fileNameOpt);
            jarFileName = (String) parser.getOptionValue(jarNameOpt);
            loadSerialName = (String) parser.getOptionValue(loadSerializedFileOpt);
            if (fileName == null && jarFileName == null && loadSerialName == null) parseError = true;
        }
        if (parseError) {
            usage();
            System.exit(2);
        }
        debug = ((Boolean) parser.getOptionValue(debugOpt, Boolean.FALSE)).booleanValue();
        ArrayList<Tokenizer> tokenizers = new ArrayList<Tokenizer>();
        ignoreCase = ((Boolean) parser.getOptionValue(ignoreCaseOpt, Boolean.FALSE)).booleanValue();
        minparts = ((Integer) parser.getOptionValue(minPartsOpt, new Integer(2))).intValue();
        filterRegex = (String) parser.getOptionValue(filterOpt);
        if (filterRegex != null) debugPrint("Filtering regexp is: " + filterRegex);
        if (fileName != null) ctagsFile = new File(fileName);
        if (jarFileName != null) jarFile = new File(jarFileName);
        if (loadSerialName != null) serialFile = new File(loadSerialName);
        if (((Boolean) parser.getOptionValue(camelOpt, Boolean.FALSE)).booleanValue()) {
            debugPrint("Adding CamelCaseTokenizer");
            tokenizers.add(new CamelCaseTokenizer());
        }
        for (Object o : parser.getOptionValues(regexOpt)) {
            debugPrint("Adding RegexpTokenizer: " + o);
            RegexpTokenizer t = new RegexpTokenizer((String) o, ignoreCase);
            tokenizers.add(t);
            debugPrint("   " + t.toString());
        }
        CompletionEngine engine = new CompletionEngine();
        if (serialFile != null) {
            debugPrint("Loading serialized data...");
            try {
                FileInputStream in = new FileInputStream(serialFile);
                engine.deserializeData(in);
                in.close();
            } catch (IOException ex) {
                debugPrint("There was an error: " + ex.getMessage());
            }
        }
        debugPrint("Loading identifiers...");
        IdentifierProvider provider;
        if (ctagsFile != null) {
            provider = new CTagsFileProvider(ctagsFile);
            provider.process();
            engine.loadIdentifiers(provider, tokenizers, minparts, MAXPARTS, ignoreCase, filterRegex);
        }
        if (jarFile != null) {
            provider = new JarFileProvider(jarFile);
            provider.process();
            engine.loadIdentifiers(provider, tokenizers, minparts, MAXPARTS, ignoreCase, filterRegex);
        }
        debugPrint(engine.numIdentifiers() + " identifiers loaded.");
        saveSerialName = (String) parser.getOptionValue(saveSerializedFileOpt);
        if (saveSerialName != null) {
            serialFile = new File(saveSerialName);
            debugPrint("Writing serialized data...");
            try {
                FileOutputStream out = new FileOutputStream(serialFile);
                engine.serializeData(out);
                out.close();
            } catch (IOException ex) {
                debugPrint("There was an error: " + ex.getMessage());
            }
        }
        provider = null;
        System.gc();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String abbrev;
        List<String> completions;
        debugPrint("Ready!\n");
        while (true) {
            abbrev = reader.readLine();
            if (abbrev == null || abbrev.equals(".")) break;
            completions = engine.complete(abbrev, true);
            if (completions != null) for (String s : completions) System.out.println(s);
        }
        System.exit(0);
    }
}
