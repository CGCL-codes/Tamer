package edu.psu.its.lionshare.gui.search;

import edu.psu.its.lionshare.gui.search.results.*;
import edu.psu.its.lionshare.search.SearchHandler;
import edu.psu.its.lionshare.util.KeyGenerator;
import edu.psu.its.lionshare.util.RunnableProcessor;
import java.awt.Component;
import java.awt.Color;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchMediator {

    private static final Log LOG = LogFactory.getLog("SearchMediator.class");

    private static SearchMediator instance = null;

    private JComponent MAIN_PANEL = null;

    private static Map search_result_handlers;

    private SearchMediator() {
        search_result_handlers = Collections.synchronizedMap(new HashMap());
    }

    public static SearchMediator getInstance() {
        if (instance == null) {
            instance = new SearchMediator();
        }
        return instance;
    }

    public void requestSearchFocus() {
    }

    public SearchResultHandler getSearchResultHandler(long id) {
        return (SearchResultHandler) search_result_handlers.get(new Long(id));
    }

    /**
   * Adds a new search result handler to the hashmap.
   * 
   * @param nId
   *          The uid.
   * @param handler
   *          The new handler to add.
   */
    public void addSearchResultHandler(Long nId, SearchResultHandler handler) {
        search_result_handlers.put(nId, handler);
    }

    public void removeSearchResultHandler(long id) {
        search_result_handlers.remove(new Long(id));
    }

    public static synchronized void search(final String query, RepositorySelectionPanel selector) {
        List lst = selector.getSelectedRepositories();
        List selected = new ArrayList(lst.size());
        for (int i = 0; i < lst.size(); i++) {
            SearchHandler handler = (SearchHandler) lst.get(i);
            selected.add(handler.getDisplayName());
        }
        final long key = KeyGenerator.getUnique();
        final SearchResultHandler handle = new SearchResultHandler(query, key);
        search_result_handlers.put(new Long(key), handle);
        SearchPane.getInstance().registerSearch(query, new Long(key));
        for (int i = 0; i < lst.size(); i++) {
            final SearchHandler handler = (SearchHandler) lst.get(i);
            handler.addSearchResultListener(key, handle);
            try {
                Runnable run = new Runnable() {

                    public void run() {
                        handler.search(query, key);
                    }
                };
                RunnableProcessor.getInstance().execute(run);
            } catch (Exception e) {
            }
        }
    }

    public static synchronized void advancedSearch(final String xml_query, final String reg, RepositorySelectionPanel selec) {
        List lst = selec.getSelectedRepositories();
        List selected = new ArrayList(lst.size());
        for (int i = 0; i < lst.size(); i++) {
            SearchHandler handler = (SearchHandler) lst.get(i);
            selected.add(handler.getDisplayName());
        }
        final long key = KeyGenerator.getUnique();
        final SearchResultHandler handle = new SearchResultHandler(reg, key);
        search_result_handlers.put(new Long(key), handle);
        SearchPane.getInstance().registerSearch(reg, new Long(key));
        for (int i = 0; i < lst.size(); i++) {
            final SearchHandler handler = (SearchHandler) lst.get(i);
            handler.addSearchResultListener(key, handle);
            try {
                Runnable run = new Runnable() {

                    public void run() {
                        if (handler.supportsAdvancedSearch()) {
                            LOG.debug("XML: " + xml_query + "\n" + reg);
                            handler.advancedSearch(xml_query, reg, key);
                        } else {
                            handler.search(reg, key);
                        }
                    }
                };
                RunnableProcessor.getInstance().execute(run);
            } catch (Exception e) {
            }
        }
    }

    public static synchronized void browseHost(final String sTextIP, final int nPort, RepositorySelectionPanel selector) {
        String sBrowseQuery = "Browsing Host " + sTextIP + " port " + nPort;
        List lst = selector.getSelectedRepositories();
        List selected = new ArrayList(lst.size());
        for (int i = 0; i < lst.size(); i++) {
            SearchHandler handler = (SearchHandler) lst.get(i);
            selected.add(handler.getDisplayName());
        }
        final long key = KeyGenerator.getUnique();
        final SearchResultHandler handle = new SearchResultHandler(sBrowseQuery, key);
        search_result_handlers.put(new Long(key), handle);
        SearchPane.getInstance().registerSearch(sBrowseQuery, new Long(key));
        for (int i = 0; i < lst.size(); i++) {
            final SearchHandler handler = (SearchHandler) lst.get(i);
            handler.addSearchResultListener(key, handle);
            try {
                Runnable run = new Runnable() {

                    public void run() {
                        handler.doBrowseNetworkHost(sTextIP, nPort, key, null);
                    }
                };
                RunnableProcessor.getInstance().execute(run);
            } catch (Exception e) {
                LOG.trace("Error browsing host", e);
            }
        }
    }

    public JComponent getComponent() {
        if (MAIN_PANEL == null) {
            MAIN_PANEL = new JPanel(new BorderLayout());
            MAIN_PANEL.setBorder(null);
            MAIN_PANEL.add(SearchPane.getInstance(), BorderLayout.CENTER);
        }
        return MAIN_PANEL;
    }
}
