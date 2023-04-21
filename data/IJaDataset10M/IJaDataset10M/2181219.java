package de.tudarmstadt.ukp.wikipedia.parser.tutorial;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListElement;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

/**
 * Displays all nested lists of a page.
 *
 */
public class T6_NestedLists {

    public static void main(String[] args) throws WikiApiException {
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setDatabase("DATABASE");
        dbConfig.setHost("HOST");
        dbConfig.setUser("USER");
        dbConfig.setPassword("PASSWORD");
        dbConfig.setLanguage(Language.english);
        Wikipedia wiki = new Wikipedia(dbConfig);
        ParsedPage pp = wiki.getPage("House_(disambiguation)").getParsedPage();
        int i = 1;
        for (NestedList nl : pp.getNestedLists()) {
            System.out.println(i + ": \n" + outputNestedList(nl, 0));
            i++;
        }
    }

    /**
	 * Returns String with all elements of a NestedList
	 * @param nl NestedList
	 * @param depth Current depth of the Nestedlist
	 * @return
	 */
    public static String outputNestedList(NestedList nl, int depth) {
        String result = "";
        if (nl == null) return result;
        for (int i = 0; i < depth; i++) result += " ";
        if (nl.getClass() == NestedListElement.class) {
            result += nl.getText();
        } else {
            result += "---";
            for (NestedList nl2 : ((NestedListContainer) nl).getNestedLists()) result += "\n" + outputNestedList(nl2, depth + 1);
        }
        return result;
    }
}
