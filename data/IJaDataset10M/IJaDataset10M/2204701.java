package net.taylor.results.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import junit.framework.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Parse wiki pages.
 * 
 * @author jgilbert01
 * 
 */
public class Parser {

    private static final Log log = LogFactory.getLog(Parser.class);

    private File file = null;

    private FileReader reader = null;

    private BufferedReader buffer = null;

    private String line = null;

    public Parser(String name) throws FileNotFoundException {
        file = new File(name);
        reader = new FileReader(file);
        buffer = new BufferedReader(reader);
    }

    public String getName() {
        String name = file.getName();
        return name.substring(0, name.length() - 5);
    }

    public Suite parse() {
        Configuration.clear();
        Suite suite = new Suite();
        parse(suite);
        return suite;
    }

    private void parse(Suite suite) {
        try {
            while (hasLine()) {
                if (isConfiguration()) {
                    parseConfiguration();
                } else if (isTable()) {
                    parseTable(suite);
                } else {
                    parseForWikiWords(suite);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean hasLine() throws Exception {
        line = buffer.readLine();
        if (log.isDebugEnabled()) {
            log.debug("Line: " + line);
        }
        return line != null;
    }

    private boolean isConfiguration() {
        return line.contains(Configuration.class.getName());
    }

    private void parseConfiguration() throws Exception {
        while (hasLine() && isTable()) {
            String[] pair = split(line);
            Configuration.setValue(trim(pair[0]), trim(pair[1]));
        }
    }

    private boolean isTable() {
        return line.trim().startsWith("|");
    }

    private void parseTable(Suite suite) throws Exception {
        String[] cells = split(line);
        if (cells.length < 2) {
            throw new RuntimeException("Table Headers must have 2 cells: " + Arrays.toString(cells));
        }
        Table table = new Table(getTestCaseClass(cells[0]));
        table.setName(trim(cells[1]));
        if (cells.length == 3) {
            table.setSkip(formatSkip(trim(cells[2])));
        }
        suite.getTables().add(table);
        while (hasLine() && isTable()) {
            addRow(table, line);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<Test> getTestCaseClass(String name) throws Exception {
        name = trim(name);
        if (log.isDebugEnabled()) {
            log.debug("TestCase: " + name);
        }
        return (Class<Test>) Class.forName(name);
    }

    private void addRow(Table table, String line) {
        Row row = new Row();
        table.getRows().add(row);
        String[] cells = split(line);
        for (String cell : cells) {
            row.getCells().add(new Cell(trim(cell)));
        }
    }

    private void parseForWikiWords(Suite suite) {
        String[] words = split(line, " ");
        for (String word : words) {
            if (isWikiWord(word)) {
                try {
                    Parser parser = new Parser(file.getParent() + "/" + word + ".wiki");
                    parser.parse(suite);
                } catch (FileNotFoundException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }

    private boolean isWikiWord(String word) {
        char[] characters = word.toCharArray();
        if (Character.isUpperCase(characters[0])) {
            if (word.contains(".")) {
                return false;
            }
            boolean lowerCaseFound = false;
            for (int i = 1; i < characters.length; i++) {
                if (!lowerCaseFound && Character.isLowerCase(characters[i])) {
                    lowerCaseFound = true;
                }
                if (Character.isUpperCase(characters[i]) && lowerCaseFound) {
                    return true;
                }
            }
        }
        return false;
    }

    private String[] split(String line) {
        return split(line, "|");
    }

    private String[] split(String line, String delimiter) {
        StringTokenizer tokenizer = new StringTokenizer(line, delimiter);
        String[] tokens = new String[tokenizer.countTokens()];
        int counter = 0;
        while (tokenizer.hasMoreElements()) {
            tokens[counter++] = (String) tokenizer.nextElement();
        }
        return tokens;
    }

    private String trim(String value) {
        value = split(value.trim(), "*")[0];
        if ("null".equals(value)) {
            return null;
        } else if ("blank".equals(value)) {
            return "";
        } else {
            return value;
        }
    }

    private boolean formatSkip(String value) {
        return "skip".equalsIgnoreCase(value);
    }
}
