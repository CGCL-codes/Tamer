package physicalpoets.model;

import java.io.*;
import java.util.*;

/**
* Class that carries almost all functionalities for creating poetry.
 * The Dictionary class gets a text file containg poetry words,
 * parses it line by line, and from this it generates a Vector of {@link poetryGenerator.Theme Theme}s 
 * a Vector of  {@link poetryGenerator.Type Type}s (word classes), and a two-dimensional array of Vectors
 * containing words that match the combination of theme and type that are indexes in the array.
 * When asked by {@link poetryGenerator.PoetryRunner PoetryRunner} it can also create Sentences with
 * a given structure, finding random matching Words to it.
 *
 * @author Sus & Magnus Lundgren & Olof Torgersson
 * 
 */
public class Dictionary {

    private String delimiters = new String(":,");

    private int state;

    private Random randomizer = new Random();

    private HashMap themeWordsMap = new HashMap();

    private HashMap catWordsMap = new HashMap();

    private HashMap catThemeWordsMap = new HashMap();

    /**
		* Loads a textfile to the Dictionary, starting the whole parsing process.
	 * Takes each line and sends them on to the {@link poetryGenerator.Dictionary#parseLine parseLine}-method.
	 * @param file Path to the textfile.
	 */
    public void load(String file) {
        try {
            BufferedReader reader = new BufferedReader((new InputStreamReader(new FileInputStream(file), "ISO-8859-1")));
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public List getWords(Category c) {
        return (List) catWordsMap.get(c);
    }

    public List getWords(Theme t) {
        return (List) themeWordsMap.get(t);
    }

    public List getWords(Category c, Theme t) {
        DictPair key = new DictPair(c, t);
        return (List) catThemeWordsMap.get(key);
    }

    public Word getRandomWord(Category c, Theme t) {
        DictPair key = new DictPair(c, t);
        List matches = (List) catThemeWordsMap.get(key);
        if (matches != null && matches.size() > 0) {
            return (Word) matches.get(randomizer.nextInt(matches.size()));
        }
        return null;
    }

    /**
		* Checks if a given line contains a definition of a theme, type or word,
	 * sending it on to the correct parser ({@link poetryGenerator.Dictionary#themeParser themeParser}, {@link poetryGenerator.Dictionary#typeParser typeParser} or {@link poetryGenerator.Dictionary#wordParser wordParser}). 
	 * @param line The line sent from load.
	 */
    private void parseLine(String line) {
        if (line.equals("THEMES")) {
            state = 1;
            return;
        } else if (line.equals("TYPES")) {
            state = 2;
            return;
        } else if (line.equals("WORDS")) {
            state = 3;
            return;
        } else if (line.equals("IMAGES")) {
            state = 4;
            return;
        }
        StringTokenizer stringcutter = new StringTokenizer(line, delimiters);
        if (state == 1) themeParser(stringcutter);
        if (state == 2) categoryParser(stringcutter);
        if (state == 3) wordParser(stringcutter);
        if (state == 4) imageParser(stringcutter);
    }

    private void themeParser(StringTokenizer stringcutter) {
        return;
    }

    private void categoryParser(StringTokenizer stringcutter) {
        return;
    }

    public void imageParser(StringTokenizer stringcutter) {
        return;
    }

    /**
		* Analyzes a line containg the description of a Word. The found type is converted 
	 * to a {@link poetryGenerator.Type Type} using the method 
	 * {@link poetryGenerator.Dictionary#stringToType stringToType}. The array of themes is converted 
	 * to a an array of {@link poetryGenerator.Theme Theme}s using the method 
	 * {@link poetryGenerator.Dictionary#stringarrayToThemearray stringarrayToThemearray}.
	 * These are used to create a new {@link poetryGenerator.Word Word}, that is sent to the
	 * {@link poetryGenerator.Dictionary#addWordToList addWordToList} method. 
	 * @param stringcutter A stringTokenizer that delivers tokens (parts
																   * of the line, separated by : and , as defined in the global variable called delimiters.
																   */
    private void wordParser(StringTokenizer stringcutter) {
        int tokens = stringcutter.countTokens();
        if (tokens > 2) {
            String word = stringcutter.nextToken();
            String t = stringcutter.nextToken();
            tokens = tokens - 2;
            String stringarray[] = new String[tokens];
            for (; tokens > 0; tokens--) {
                stringarray[tokens - 1] = stringcutter.nextToken();
            }
            Category cat = stringToCategory(t);
            Theme themearray[] = stringarrayToThemearray(stringarray);
            Word x = new Word(word, cat);
            addWordToMaps(x, themearray);
        }
    }

    private Category stringToCategory(String s) {
        return Category.getCategory(Integer.parseInt(s));
    }

    /**
		* Transforms an array of Strings, into an array of {@link poetryGenerator.Theme Theme}s,
	 * given that the strings in the array contain only the name of a Theme. 
	 * Uses the {@link poetryGenerator.Dictionary#stringToTheme stringToTheme} method.
	 * @param s[] An array of Strings, each String containing only the name of a Theme.
	 */
    private Theme[] stringarrayToThemearray(String s[]) {
        int x = s.length;
        Theme themearray[] = new Theme[x];
        for (int i = 0; i < x; i++) {
            String s_temp = s[i];
            themearray[i] = stringToTheme(s_temp);
        }
        return themearray;
    }

    /**
		* Transforms a String containing only the name of a Theme to a {@link poetryGenerator.Theme Theme}. 
	 * @param s A String containing only the name of a Theme.
	 */
    private Theme stringToTheme(String s) {
        return Theme.getTheme(Integer.parseInt(s));
    }

    private void addWordToMaps(Word w, Theme[] ta) {
        Category c = w.getCategory();
        List l;
        if (!catWordsMap.containsKey(c)) {
            catWordsMap.put(c, new ArrayList());
        }
        l = (List) catWordsMap.get(c);
        if (!l.contains(w)) {
            l.add(w);
        }
        for (int i = 0; i < ta.length; i++) {
            Theme t = ta[i];
            if (!themeWordsMap.containsKey(t)) {
                themeWordsMap.put(t, new ArrayList());
            }
            l = (List) themeWordsMap.get(t);
            if (!l.contains(w)) {
                l.add(w);
            }
        }
        for (int i = 0; i < ta.length; i++) {
            DictPair d = new DictPair(w.getCategory(), ta[i]);
            if (!catThemeWordsMap.containsKey(d)) {
                catThemeWordsMap.put(d, new ArrayList());
            }
            l = (List) catThemeWordsMap.get(d);
            if (!l.contains(w)) {
                l.add(w);
            }
        }
    }

    private class DictPair {

        Category category;

        Theme theme;

        String hashString;

        public DictPair(Category c, Theme t) {
            category = c;
            theme = t;
            hashString = c.getName() + "(" + t.getName() + ")";
        }

        public Category getCategory() {
            return category;
        }

        public Theme getTheme() {
            return theme;
        }

        public int hashCode() {
            return hashString.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj != null && (obj.getClass().equals(this.getClass()))) {
                DictPair d = (DictPair) obj;
                return category.equals(d.category) && theme.equals(d.theme);
            }
            return false;
        }
    }
}
