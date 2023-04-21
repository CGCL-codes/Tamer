package org.maveryx.finder;

import static org.maveryx.finder.SearchingSettingsModel.PLUGIN_CONFIGURATION_FILE;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.jdom.Document;
import org.jdom.Element;
import org.maveryx.finder.exception.DuplicatePluginException;
import org.maveryx.finder.exception.MultipleObjectFoundException;
import org.maveryx.finder.exception.NoSuchPluginException;
import org.maveryx.finder.exception.ObjectNotFoundException;
import org.maveryx.finder.exception.QueryResultContextExceptionSetter;
import services.W3CSerivcesJDOM;

public class Finder {

    private Document document = null;

    private ArrayList<SearchingStrategyPlugin> searchingStrategies;

    public Finder() throws DuplicatePluginException {
        searchingStrategies = new ArrayList<SearchingStrategyPlugin>();
        try {
            loadSearchingStrategyPlugins();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocument(Document doc) {
        document = doc;
    }

    public Document getDocument() {
        return document;
    }

    public void disposeOfDocument() {
        document = null;
    }

    public QueryResultContext find(SearchingKeys keys) throws NoSuchPluginException, ObjectNotFoundException, MultipleObjectFoundException {
        Element returnedElement = null;
        SearchingStrategyPlugin searchingStrategyPlugin = null;
        String recipientPluginName = keys.getRecipientPluginName();
        boolean found = false;
        Iterator<SearchingStrategyPlugin> iteratorTmp = searchingStrategies.iterator();
        while (iteratorTmp.hasNext() && !found) {
            SearchingStrategyPlugin strategy = (SearchingStrategyPlugin) iteratorTmp.next();
            String name = strategy.getSearchingStrategyPluginName();
            if (name.equals(recipientPluginName)) searchingStrategyPlugin = strategy;
        }
        if (searchingStrategyPlugin == null) {
            throw new NoSuchPluginException(recipientPluginName);
        }
        Iterator<AbstractSearchingCriterion> searchingCriteria = searchingStrategyPlugin.getSearchingCriteria();
        ResultsTable resultsTable = new ResultsTable();
        while (searchingCriteria.hasNext()) {
            AbstractSearchingCriterion criterion = (AbstractSearchingCriterion) searchingCriteria.next();
            ResultsTable partialResult = criterion.search(keys, document);
            resultsTable.addResultTable(partialResult);
        }
        if (resultsTable.isEmpty()) {
            ObjectNotFoundException ex = new ObjectNotFoundException(keys);
            QueryResultContextExceptionSetter.setSnapshot(ex, document);
            QueryResultContextExceptionSetter.setFoundObject(ex, null);
            QueryResultContextExceptionSetter.setScore(ex, Float.NEGATIVE_INFINITY);
            QueryResultContextExceptionSetter.setResultsTable(ex, resultsTable);
            QueryResultContextExceptionSetter.setKeys(ex, keys);
            throw ex;
        }
        try {
            returnedElement = searchingStrategyPlugin.evaluate(resultsTable, keys);
        } catch (MultipleObjectFoundException ex) {
            QueryResultContextExceptionSetter.setSnapshot(ex, document);
            QueryResultContextExceptionSetter.setFoundObject(ex, null);
            QueryResultContextExceptionSetter.setScore(ex, Float.NEGATIVE_INFINITY);
            QueryResultContextExceptionSetter.setResultsTable(ex, resultsTable);
            QueryResultContextExceptionSetter.setKeys(ex, keys);
            throw ex;
        } catch (ObjectNotFoundException ex) {
            QueryResultContextExceptionSetter.setSnapshot(ex, document);
            QueryResultContextExceptionSetter.setFoundObject(ex, null);
            QueryResultContextExceptionSetter.setScore(ex, Float.NEGATIVE_INFINITY);
            QueryResultContextExceptionSetter.setResultsTable(ex, resultsTable);
            QueryResultContextExceptionSetter.setKeys(ex, keys);
            throw ex;
        }
        System.out.println();
        System.out.println("-------------------------------------");
        System.out.println("GUI OBJECT FOUND: '" + returnedElement.getAttributeValue("id") + "'");
        System.out.println("- ID   = '" + returnedElement.getAttributeValue("accessibleName") + "'");
        System.out.println("- ROLE = '" + returnedElement.getAttributeValue("accessibleRole") + "'");
        System.out.println("-------------------------------------\n\n");
        QueryResultContext queryResultContext = new QueryResultContext();
        queryResultContext.setSnapshot(document);
        queryResultContext.setFoundObject(returnedElement);
        resultsTable.sortByScore();
        queryResultContext.setScore(resultsTable.getResultsIterator().next().getScore());
        queryResultContext.setResultsTable(resultsTable);
        queryResultContext.setKeys(keys);
        return queryResultContext;
    }

    public void addSearchingStrategyPlugin(SearchingStrategyPlugin strategy) throws DuplicatePluginException {
        int searchPluginNumber = searchingStrategies.size();
        for (int i = 0; i < searchPluginNumber; i++) {
            String name = searchingStrategies.get(i).getSearchingStrategyPluginName();
            if (strategy.getSearchingStrategyPluginName().equals(name)) throw new DuplicatePluginException(name);
        }
        strategy.register(this);
        searchingStrategies.add(strategy);
    }

    public void updateSearchingStrategyPlugin(SearchingStrategyPlugin strategy) throws NoSuchPluginException {
        boolean flag = false;
        int searchPluginNumber = searchingStrategies.size();
        for (int i = 0; i < searchPluginNumber; i++) {
            String name = searchingStrategies.get(i).getSearchingStrategyPluginName();
            if (strategy.getSearchingStrategyPluginName().equals(name)) {
                flag = true;
                searchingStrategies.remove(i);
                strategy.register(this);
                searchingStrategies.add(i, strategy);
            }
        }
        if (!flag) throw new NoSuchPluginException(strategy.getSearchingStrategyPluginName());
    }

    private void loadSearchingStrategyPlugins() throws IOException, DuplicatePluginException {
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration<URL> urls = cl.getResources(PLUGIN_CONFIGURATION_FILE);
        while (urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            String stringUrl = url.getPath();
            stringUrl = deEscape(stringUrl);
            System.out.println("info: Plugin configuration file path: " + stringUrl);
            int indexColon = stringUrl.lastIndexOf(":");
            int indexExclamationMark = stringUrl.lastIndexOf("!");
            boolean isJarFound = (indexExclamationMark >= 0);
            if (isJarFound) {
                String jarName = stringUrl.substring(indexColon - 1, indexExclamationMark);
                JarFile jarFile = new JarFile(new File(jarName));
                ZipEntry entry = jarFile.getEntry(PLUGIN_CONFIGURATION_FILE);
                if (entry != null) {
                    InputStream in = jarFile.getInputStream(entry);
                    Document doc = W3CSerivcesJDOM.parseXML(in);
                    SearchingSettingsModel model = new SearchingSettingsModel(doc);
                    addSearchingStrategyPlugin(new SearchingStrategyPlugin(model));
                }
            } else {
                Document doc = W3CSerivcesJDOM.parseXML(stringUrl);
                SearchingSettingsModel model = new SearchingSettingsModel(doc);
                addSearchingStrategyPlugin(new SearchingStrategyPlugin(model));
            }
        }
    }

    private String deEscape(String str) {
        str = str.replace("%20", " ");
        return str;
    }
}
