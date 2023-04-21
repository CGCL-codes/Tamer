package org.sqlexp.util.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import org.sqlexp.util.StrUtils;

/**
 * Language objects contains all language specific data.<br>
 * Two languages are available at a same time : default (English) and parameterized one.
 * @author Matthieu Réjou
 */
public final class Language implements Comparable<Language> {

    /** String value replacing any unparameterized entry. */
    public static final String UNRECOGNIZED_STRING = "<Unrecognized token>";

    private static Language defaultLng;

    private static Language instance;

    private static ArrayList<File> unexistingFiles;

    /**
	 * Gets the active language instance.
	 * @return the current <code>Language</code> instance.
	 */
    public static Language getInstance() {
        if (instance == null) {
            System.err.println("Language object is not initialized.");
        }
        synchronized (Language.class) {
            return instance;
        }
    }

    /**
	 * Initializes the default language (English).<br>
	 * Future <code>getInstance()</code> calls will return the newly created language.<br>
	 * <b>Synchronized</b>.
	 */
    public static void initializeDefault() {
        setLanguage("en");
        defaultLng = instance;
    }

    /**
	 * Sets a new language by id.<br>
	 * Future <code>getInstance()</code> calls will return the newly created language.<br>
	 * <b>Synchronized</b>.
	 * @param id (i.e. directory under "language/")
	 */
    public static void setLanguage(final String id) {
        synchronized (Language.class) {
            try {
                instance = new Language(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Computes a sorted list of possible languages.<br>
	 * Note that getName() method will return a value.
	 * @return list of languages
	 */
    public static ArrayList<Language> getAvailableLanguages() {
        ArrayList<Language> languages = new ArrayList<Language>();
        File directory = new File("language");
        if (directory.exists() && directory.isDirectory()) {
            for (File lngDir : directory.listFiles()) {
                if (lngDir.isDirectory()) {
                    String name = getName(lngDir);
                    if (name != null) {
                        languages.add(new Language(lngDir.getName(), name));
                    }
                }
            }
        }
        Collections.sort(languages);
        return languages;
    }

    /**
	 * Gets the language name from the given directory.
	 * @param lngDir directory associated with the language
	 * @return name (as specified in "name.lngdef" file), or null if any error occurred
	 */
    private static String getName(final File lngDir) {
        String name = null;
        File file = new File(lngDir, "name.lngdef");
        if (!file.exists()) {
            return null;
        }
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            char[] cbuff = new char[100];
            int count = fr.read(cbuff);
            name = new String(cbuff, 0, count);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e1) {
                }
            }
        }
        return name;
    }

    private final String id;

    private String name;

    private final ArrayList<Package> packages = new ArrayList<Package>();

    private final HashMap<LanguageEntry, String> strings = new HashMap<LanguageEntry, String>();

    /**
	 * Creates a new language by id.<br>
	 * @param id (i.e. directory under "language/")
	 */
    private Language(final String id) {
        this(id, null);
    }

    /**
	 * Creates a new language by id and name.<br>
	 * @param id (i.e. directory under "language/")
	 * @param name (as specified in "name.lngdef" file)
	 */
    private Language(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
	 * Gets the language unique identifier.
	 * @return id (i.e. directory under "language/")
	 */
    public String getId() {
        return id;
    }

    /**
	 * Gets the language name.
	 * @return name (as specified in "name.lngdef" file)<br>
	 * Returns a value only from language objects created
	 * by <code>getAvailableLanguages()</code> method.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets a value for the given entry, with other language entries as parameters.
	 * @param entry object
	 * @param parameters used to replace {0}, {1}, etc.
	 * @return language specific value
	 */
    public String get(final LanguageEntry entry, final LanguageEntry[] parameters) {
        if (entry == null) {
            return UNRECOGNIZED_STRING;
        }
        if (parameters != null) {
            String[] stringParams = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                stringParams[i] = get(parameters[i]);
            }
        }
        return get(entry.getCls(), entry.getEntry());
    }

    /**
	 * Gets a value for the given entry.
	 * @param entry object
	 * @param parameters used to replace {0}, {1}, etc.
	 * @return language specific value
	 */
    public String get(final LanguageEntry entry, final String... parameters) {
        if (entry == null) {
            return UNRECOGNIZED_STRING;
        }
        return get(entry.getCls(), entry.getEntry(), parameters);
    }

    /**
	 * Get a value for the given entry.
	 * @param object associated with the value
	 * @param entry name
	 * @param parameters used to replace {0}, {1}, etc.
	 * @return language specific value
	 */
    public String get(final Object object, final String entry, final String... parameters) {
        if (object == null) {
            return UNRECOGNIZED_STRING;
        }
        return get(object.getClass(), entry, parameters);
    }

    /**
	 * Gets a value for the given entry.
	 * @param cls class associated with the value
	 * @param entry name
	 * @param parameters used to replace {0}, {1}, etc.
	 * @return language specific value
	 */
    public String get(final Class<?> cls, final String entry, final String... parameters) {
        if (cls == null) {
            return UNRECOGNIZED_STRING;
        }
        loadPackage(cls.getPackage());
        LanguageEntry key = new LanguageEntry(cls, entry);
        String value = strings.get(key);
        if (value == null) {
            if (this != defaultLng) {
                value = defaultLng.get(cls, entry, parameters);
            }
            if (value == null) {
                strings.put(key, UNRECOGNIZED_STRING);
                System.err.println("Could not find entry value for " + key);
                value = UNRECOGNIZED_STRING;
            }
        } else if (parameters != null) {
            String replaced;
            String replacement;
            for (int i = 0; i < parameters.length; i++) {
                replacement = parameters[i];
                if (replacement == null) {
                    replacement = "";
                }
                replaced = "{" + i + "}";
                if (value.contains(replaced)) {
                    value = value.replace(replaced, replacement);
                } else {
                    value += replacement;
                }
            }
        }
        value = value.replaceAll("\\{[0-9]+\\}", "");
        return value;
    }

    /**
	 * Loads the file associated with the given package.<br>
	 * File path is language/[id]/[package].lng.
	 * @param pack package name
	 */
    private void loadPackage(final Package pack) {
        if (packages.contains(pack)) {
            return;
        }
        StringBuilder fileName = new StringBuilder();
        fileName.append("language");
        fileName.append(File.separatorChar);
        fileName.append(id);
        fileName.append(File.separatorChar);
        fileName.append(pack.getName());
        fileName.append(".lng");
        File file = new File(fileName.toString());
        FileReader fr = null;
        BufferedReader br = null;
        try {
            if (!file.exists()) {
                if (unexistingFiles == null) {
                    unexistingFiles = new ArrayList<File>();
                }
                if (!unexistingFiles.contains(file)) {
                    System.err.println("Language file " + file.getPath() + " does not exists");
                    unexistingFiles.add(file);
                }
                return;
            }
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            String fixedClassName = null;
            while (line != null) {
                fixedClassName = parseLine(pack.getName(), fixedClassName, line);
                line = br.readLine();
            }
            packages.add(pack);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
	 * Parses a single line.
	 * @param pack class package
	 * @param fixedClassName if line is in a class { ... } block
	 * @param line to parse
	 * @return fixed class name (when using a class { ... } syntax)
	 * @throws ClassNotFoundException if class name is invalid
	 */
    private String parseLine(final String pack, final String fixedClassName, final String line) throws ClassNotFoundException {
        if (line.length() == 0 || line.charAt(0) == '#') {
            return fixedClassName;
        }
        StringBuilder sb = new StringBuilder();
        String clsName = fixedClassName;
        String entry = null;
        char c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            if ((c == ' ' || c == '\t') && entry == null) {
                continue;
            } else if (c == '.' && clsName == null) {
                clsName = pack + '.' + sb.toString();
                sb = new StringBuilder();
            } else if (c == '{' && entry == null) {
                return pack + '.' + sb.toString();
            } else if (c == '}' && fixedClassName != null && entry == null) {
                return null;
            } else if (c == '=' && entry == null) {
                entry = sb.toString();
                sb = new StringBuilder();
            } else if (c == '\\') {
                i++;
                if (i < line.length()) {
                    c = line.charAt(i);
                    switch(c) {
                        case 'n':
                            sb.append(StrUtils.EOL);
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        default:
                            System.err.println("Unrecognized language token : \\" + c);
                    }
                } else {
                    System.err.println("Language token ends with a \\ character");
                }
            } else {
                sb.append(c);
            }
        }
        if (clsName != null && entry != null) {
            Class<?> cls = Class.forName(clsName);
            strings.put(new LanguageEntry(cls, entry), sb.toString());
        }
        return fixedClassName;
    }

    @Override
    public String toString() {
        ArrayList<String> packages = new ArrayList<String>();
        HashMap<String, ArrayList<String>> classes = new HashMap<String, ArrayList<String>>();
        HashMap<Class<?>, ArrayList<String>> entries = new HashMap<Class<?>, ArrayList<String>>();
        Class<?> cls;
        String pack, entry;
        for (Entry<LanguageEntry, String> string : strings.entrySet()) {
            cls = string.getKey().getCls();
            pack = cls.getPackage().getName();
            entry = string.getKey().getEntry();
            if (!classes.containsKey(pack)) {
                packages.add(pack);
                classes.put(pack, new ArrayList<String>());
            }
            if (!entries.containsKey(cls)) {
                classes.get(pack).add(cls.getName());
                entries.put(cls, new ArrayList<String>());
            }
            entries.get(cls).add(entry);
        }
        Collections.sort(packages);
        for (ArrayList<String> classList : classes.values()) {
            Collections.sort(classList);
        }
        for (ArrayList<String> entryList : entries.values()) {
            Collections.sort(entryList);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" :\n");
        for (String packName : packages) {
            sb.append("  ");
            sb.append(packName);
            sb.append('\n');
            for (String clsName : classes.get(packName)) {
                sb.append("    ");
                sb.append(clsName.substring(clsName.lastIndexOf('.') + 1));
                sb.append('\n');
                try {
                    cls = Class.forName(clsName);
                    for (String entryName : entries.get(cls)) {
                        sb.append("      ");
                        sb.append(entryName);
                        sb.append('=');
                        sb.append(get(Class.forName(clsName), entryName));
                        sb.append('\n');
                    }
                } catch (ClassNotFoundException e1) {
                    sb.append("      Unknown class name : " + clsName);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(final Language o) {
        if (o == null) {
            return -1;
        } else if (this == o) {
            return 0;
        } else if (name != null && o.name == null) {
            return -1;
        } else if (name == null && o.name != null) {
            return 1;
        } else if (name != null) {
            return name.compareTo(o.name);
        } else {
            return id.compareTo(o.id);
        }
    }
}
