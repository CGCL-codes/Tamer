package bcry;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.List.*;

class bcModule {

    private final String DEFAULT_DATA = "data/modules/battlecry.bcm";

    private final String DICT_FILE = "data/cmudict.0.6-2-bcry";

    private static final String DEMO_TEMPLATE = "data/bcDemoModule.template";

    private Properties grammar;

    private Properties info;

    private List layout;

    private List wordLists;

    private int sylTolerance = 0;

    private int metTolerance = 1;

    private boolean initialized = false;

    private bcVoice voice = null;

    private bcDictionary dict = null;

    public bcModule(String data, String customLayout, bcVoice v) {
        voice = v;
        if (data.equals("")) {
            data = DEFAULT_DATA;
        }
        if (data.equals("DEMO")) {
            initialized = initializeDemoModule();
        } else {
            dict = new bcDictionary(DICT_FILE, voice);
            initialized = initialize(data, customLayout);
        }
    }

    public bcModule(String data, String customLayout, bcVoice v, bcDictionary d) {
        voice = v;
        if (data.equals("")) {
            data = DEFAULT_DATA;
        }
        if (data.equals("DEMO")) {
            initialized = initializeDemoModule();
        } else {
            dict = d;
            initialized = initialize(data, customLayout);
        }
    }

    private boolean initialize(String data, String customLayout) {
        boolean result = false;
        File test = new File(data);
        if (test.isDirectory()) {
            voice.verbose("Using directory " + data);
            if (openDirectory(data, customLayout)) {
                result = true;
                voice.verbose("Directory opened successfully.");
            }
        } else if (test.isFile()) {
            voice.verbose("Using module " + data);
            if (openZipFile(data, customLayout)) {
                result = true;
                voice.verbose("Module opened successfully.");
            }
        } else {
            voice.sysout("Error: File or directory not found: " + test);
        }
        return result;
    }

    private boolean initializeDemoModule() {
        boolean result = false;
        voice.verbose("Running in Demo Mode");
        bcDemoModule demo = new bcDemoModule();
        grammar = demo.getGrammar();
        info = demo.getInfo();
        layout = demo.getLayout();
        wordLists = demo.getWordLists();
        if ((grammar != null) && (info != null) && (layout != null) && (wordLists != null)) {
            result = true;
        }
        return result;
    }

    private boolean openZipFile(String dataFile, String layoutFile) {
        boolean result = true;
        boolean needLayout = true;
        int complete = 0;
        if (!layoutFile.equals("")) {
            try {
                voice.verbose("Using custom layout " + layoutFile);
                if (!loadLayout(new FileInputStream(layoutFile))) {
                    voice.sysout("Warning: Failed to load custom layout file. Trying to load from data file...");
                } else {
                    needLayout = false;
                }
            } catch (IOException e) {
                voice.sysout("Warning: Failed to open custom layout file. Trying data file...");
            }
        }
        try {
            ZipFile zipFile = new ZipFile(dataFile);
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    voice.sysout("Warning: Directory detected inside data file; this can cause problems.");
                    continue;
                }
                if (entry.getName().equals("info.dat")) {
                    complete++;
                    voice.verbose("Loading file: " + entry.getName());
                    if (!loadInfo(zipFile.getInputStream(entry))) {
                        voice.sysout("Error: Unable to parse info file.");
                        result = false;
                    }
                } else if (entry.getName().equals("layout.dat")) {
                    complete++;
                    voice.verbose("Loading file: " + entry.getName());
                    if (needLayout) {
                        if (!loadLayout(zipFile.getInputStream(entry))) {
                            voice.sysout("Error: Unable to parse layout file.");
                            result = false;
                        }
                    } else {
                        continue;
                    }
                } else if (entry.getName().equals("grammar.dat")) {
                    complete++;
                    voice.verbose("Loading file: " + entry.getName());
                    if (!loadGrammar(zipFile.getInputStream(entry))) {
                        voice.sysout("Error: Unable to parse grammar file.");
                        result = false;
                    }
                } else if (entry.getName().equals("words.dat")) {
                    complete++;
                    voice.verbose("Loading file: " + entry.getName());
                    if (!loadWordLists(zipFile.getInputStream(entry))) {
                        voice.sysout("Error: Unable to parse word list " + entry.getName());
                        result = false;
                    }
                } else {
                    voice.sysout("Warning: Ignoring unknown file type " + entry.getName());
                }
            }
            zipFile.close();
        } catch (IOException e) {
            voice.sysout("Error: Could not open data file " + dataFile);
            result = false;
        }
        if (complete != 4) {
            voice.sysout("Error: Module " + dataFile + " does not contain all necessary files.");
            result = false;
        }
        return result;
    }

    private boolean openDirectory(String dataFile, String layoutFile) {
        boolean result = true;
        boolean needLayout = true;
        if (!layoutFile.equals("")) {
            try {
                voice.verbose("Using custom layout " + layoutFile);
                if (!loadLayout(new FileInputStream(layoutFile))) {
                    voice.sysout("Warning: Failed to load custom layout file. Trying to load from data file...");
                } else {
                    needLayout = false;
                }
            } catch (IOException e) {
                voice.sysout("Warning: Failed to open custom layout file. Trying data file...");
            }
        }
        try {
            int complete = 0;
            File[] fileList = new File(dataFile).listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if ((fileList[i].toString().endsWith("info.dat")) || (fileList[i].toString().endsWith("layout.dat")) || (fileList[i].toString().endsWith("grammar.dat")) || (fileList[i].toString().endsWith("words.dat"))) {
                    complete++;
                }
            }
            if (complete != 4) {
                voice.sysout("Error: Directory " + dataFile + " does not contain all necessary files.");
                result = false;
            } else {
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].getName().equals("info.dat")) {
                        voice.verbose("Loading file: " + fileList[i].toString());
                        if (!loadInfo(new FileInputStream(fileList[i].toString()))) {
                            voice.sysout("Error: Unable to parse info file.");
                            result = false;
                        }
                    } else if (fileList[i].getName().equals("layout.dat")) {
                        voice.verbose("Loading file: " + fileList[i].toString());
                        if (needLayout) {
                            if (!loadLayout(new FileInputStream(fileList[i].toString()))) {
                                voice.sysout("Error: Unable to parse layout file.");
                                result = false;
                            }
                        } else {
                            continue;
                        }
                    } else if (fileList[i].getName().equals("grammar.dat")) {
                        voice.verbose("Loading file: " + fileList[i].toString());
                        if (!loadGrammar(new FileInputStream(fileList[i].toString()))) {
                            voice.sysout("Error: Unable to parse grammar file.");
                            result = false;
                        }
                    } else if (fileList[i].getName().equals("words.dat")) {
                        voice.verbose("Loading file: " + fileList[i].toString());
                        if (!loadWordLists(new FileInputStream(fileList[i].toString()))) {
                            voice.sysout("Error: Unable to parse word list " + fileList[i].toString());
                            result = false;
                        }
                    } else {
                        voice.sysout("Warning: Ignoring unknown file type " + fileList[i].toString());
                    }
                }
            }
        } catch (IOException e) {
            voice.sysout("Error: " + dataFile + " does not contain valid battlecry data.");
            result = false;
        }
        return result;
    }

    private boolean loadWordLists(InputStream wordFile) {
        if (wordLists == null) {
            wordLists = new LinkedList();
        }
        bcWordList tempList = new bcWordList("BC_DUMMY_LIST");
        bcWordList splitList = new bcWordList("BC_SPLIT_STRINGS");
        String line;
        boolean result = true;
        try {
            BufferedReader list = new BufferedReader(new InputStreamReader(wordFile));
            while ((line = list.readLine()) != null) {
                if (!line.equals("")) {
                    if ((line.startsWith("[")) && (line.endsWith("]"))) {
                        if (!tempList.getFileName().equals("BC_DUMMY_LIST")) {
                            wordLists.add(tempList);
                        }
                        voice.verbose("Adding word list: " + line.substring(1, line.length() - 1));
                        tempList = new bcWordList(line.substring(1, line.length() - 1));
                    } else {
                        if (!line.startsWith("#")) {
                            if (line.indexOf("=") == -1) {
                                if (tempList.getItem(line) == null) tempList.addWord(line, dict.getPhonemes(line));
                                if (line.indexOf(" ") != -1) {
                                    String temp[] = line.split(" ");
                                    for (int i = 0; i < temp.length; i++) {
                                        if (splitList.getItem(temp[i]) == null) splitList.addWord(temp[i], dict.getPhonemes(temp[i]));
                                    }
                                }
                            } else {
                                String[] splitLine = line.split("=");
                                bcWord tempWord = new bcWord(splitLine[0], dict.getPhonemes(splitLine[0]));
                                tempWord.setEqualWord(splitLine[1], dict.getPhonemes(splitLine[1]));
                                tempList.addWord(tempWord);
                            }
                        }
                    }
                }
            }
            if (tempList.getFileName().equals("BC_DUMMY_LIST")) {
                voice.sysout("Error: Syntax error in words.dat - see documentation for help.");
                result = false;
            }
            wordLists.add(tempList);
            list.close();
        } catch (Exception e) {
            voice.sysout("Error: Unable to load word lists - " + e.toString());
            result = false;
        }
        tempList = new bcWordList("BC_FROM_GRAMMAR");
        String temp1[];
        String temp2[];
        Enumeration entries = grammar.propertyNames();
        while (entries.hasMoreElements()) {
            temp1 = grammar.getProperty(entries.nextElement().toString()).split("'");
            for (int i = 1; i < temp1.length; i++) {
                if (tempList.getItem(temp1[i]) == null) tempList.addWord(temp1[i], dict.getPhonemes(temp1[i]));
                if (temp1[i].indexOf(" ") != -1) {
                    temp2 = temp1[i].split(" ");
                    for (int j = 0; j < temp2.length; j++) {
                        if (splitList.getItem(temp2[j]) == null) splitList.addWord(temp2[j], dict.getPhonemes(temp2[j]));
                    }
                }
                i++;
            }
        }
        splitList.addWord("", "");
        wordLists.add(tempList);
        if (splitList.getNumberOfWords() != 0) wordLists.add(splitList);
        return result;
    }

    private boolean loadLayout(InputStream layoutFile) {
        int i;
        String line;
        boolean result = true;
        try {
            layout = new LinkedList();
            BufferedReader lo = new BufferedReader(new InputStreamReader(layoutFile));
            while ((line = lo.readLine()) != null) {
                layout.add(line);
            }
            lo.close();
        } catch (Exception e) {
            voice.sysout(e.toString());
            result = false;
        }
        return result;
    }

    private boolean loadGrammar(InputStream grammarFile) {
        boolean result = true;
        try {
            grammar = new Properties();
            grammar.load(grammarFile);
        } catch (Exception e) {
            voice.sysout(e.toString());
            result = false;
        }
        return result;
    }

    private boolean loadInfo(InputStream infoFile) {
        boolean result = true;
        try {
            info = new Properties();
            info.load(infoFile);
            if (!(info.getProperty("OPTION_SYLLABLE_TOLERANCE") == null)) {
                sylTolerance = Integer.parseInt(info.getProperty("OPTION_SYLLABLE_TOLERANCE"));
            } else {
                voice.verbose("OPTION_SYLLABLE_TOLERANCE not set in info.dat, using default.");
            }
            if (!(info.getProperty("OPTION_METRIC_TOLERANCE") == null)) {
                metTolerance = Integer.parseInt(info.getProperty("OPTION_METRIC_TOLERANCE"));
            } else {
                voice.verbose("OPTION_METRIC_TOLERANCE not set in info.dat, using default.");
            }
        } catch (NumberFormatException e) {
            voice.sysout("Warning: Option with an invalid value found, using default.");
        } catch (Exception e) {
            voice.sysout(e.toString());
            result = false;
        }
        return result;
    }

    public void toDemoClass(String outputFile) {
        try {
            int c = 0;
            String line;
            String nextLine;
            String temp;
            Enumeration entries;
            BufferedReader template = new BufferedReader(new FileReader(DEMO_TEMPLATE));
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
            while ((line = template.readLine()) != null) {
                if (line.equals("//DEMOMAKER: INSERT GRAMMAR HERE!")) {
                    entries = grammar.propertyNames();
                    while (entries.hasMoreElements()) {
                        temp = entries.nextElement().toString();
                        nextLine = "      grammar.setProperty(\"" + temp + "\",\"" + grammar.getProperty(temp) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " grammar entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT INFO HERE!")) {
                    entries = info.propertyNames();
                    while (entries.hasMoreElements()) {
                        temp = entries.nextElement().toString();
                        nextLine = "      info.setProperty(\"" + temp + "\",\"" + info.getProperty(temp) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " info entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT LAYOUT HERE!")) {
                    for (int i = 0; i < layout.size(); i++) {
                        nextLine = "      layout.add(\"" + layout.get(i) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " layout entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT WORDLISTS HERE!")) {
                    for (int i = 0; i < wordLists.size(); i++) {
                        bcWordList tempL = (bcWordList) wordLists.get(i);
                        output.write("      tempList = new bcWordList(\"" + tempL.getFileName() + "\");");
                        output.newLine();
                        for (int j = 0; j < tempL.getNumberOfWords(); j++) {
                            nextLine = "      tempList.addWord(\"" + tempL.getItem(j).getWord() + "\",\"" + tempL.getItem(j).getPhonemes() + "\");";
                            output.write(nextLine);
                            output.newLine();
                            c++;
                        }
                        output.write("      wordLists.add(tempList);");
                        output.newLine();
                        output.newLine();
                    }
                    voice.verbose(c + " word list entries added.");
                    c = 0;
                } else {
                    output.write(line);
                    output.newLine();
                }
            }
            template.close();
            output.close();
        } catch (IOException e) {
            voice.sysout("Error: Could not create demo module - " + e.toString());
        }
        voice.sysout("bcDemoModule class created at " + outputFile + ".");
    }

    public Properties getGrammar() {
        return grammar;
    }

    public Properties getInfo() {
        return info;
    }

    public List getLayout() {
        return layout;
    }

    public List getWordLists() {
        return wordLists;
    }

    public bcWordList getWordList(String listName) {
        bcWordList result = null;
        for (int i = 0; i < wordLists.size(); i++) {
            bcWordList temp = (bcWordList) wordLists.get(i);
            if (temp.getFileName().equals(listName)) {
                result = temp;
                break;
            }
        }
        return result;
    }

    public int getSyllableTolerance() {
        return sylTolerance;
    }

    public int getMetricTolerance() {
        return metTolerance;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public bcDictionary getDictionary() {
        return dict;
    }
}
