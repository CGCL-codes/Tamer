package wssearch.search;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import wssearch.graph.ServiceGraph;

/**
 * Enthält Suchergebnis-Liste und Zugriffs-Funktionen. Wird für die Ergebnisse
 * sämtlicher Phasen des Suchablaufs verwendet.
 * @author Thorsten Theelen
 *
 */
public class ResultSet {

    private Hashtable<String, Double> searchResults;

    private Hashtable<String, Double> feedbackResults;

    public ResultSet() {
        searchResults = new Hashtable<String, Double>();
        feedbackResults = new Hashtable<String, Double>();
    }

    /**
	 * Suchergebnis ohne Relevanzpunktzahl hinzufügen
	 * @param name Suchergebnis
	 * @param searchscore Suchpunktzahl
	 */
    public void addResult(String name, double searchscore) {
        addResult(name, 0.5, searchscore);
    }

    /**
	 * Suchergebnis mit Relevanzpunktzahl hinzufügen. 
	 * Wenn das Ergebnis schon vorhanden ist, wird die höhere Punktzahl
	 * verwendet.
	 * @param name Suchergebnis
	 * @param feedbackscore Relevanzpunktzahl
	 * @param searchscore Suchpunktzahl
	 */
    public void addResult(String name, double feedbackscore, double searchscore) {
        if (!feedbackResults.containsKey(name) || feedbackResults.get(name) < feedbackscore) {
            feedbackResults.put(name, feedbackscore);
            searchResults.put(name, searchscore);
        } else if (feedbackResults.get(name) == feedbackscore && searchResults.get(name) < searchscore) {
            feedbackResults.put(name, feedbackscore);
            searchResults.put(name, searchscore);
        }
    }

    /**
	 * Liefert sortierte Suchergebnisse ohne Relevanzpunktzahl
	 * @return sortierte Ergebnisliste
	 */
    public Iterator<VertexSearchResult> getVertexResults() {
        Enumeration<String> names = searchResults.keys();
        TreeSet<VertexSearchResult> sortedResults = new TreeSet<VertexSearchResult>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sortedResults.add(new VertexSearchResult(name, searchResults.get(name)));
        }
        return sortedResults.iterator();
    }

    /**
	 * Überprüft ob Suchergebnis vorhanden ist
	 * @param name gesuchtes Ergebnis
	 * @return ist vorhanden?
	 */
    public boolean contains(String name) {
        if (searchResults.containsKey(name)) return true; else return false;
    }

    /**
	 * Liefert sortierte Suchergebnisse mit Relevanzpunktzahl	
	 * @return sortierte Ergebnisliste
	 */
    public Iterator<SearchResult> getSearchResults() {
        TreeSet<SearchResult> sortedResults = new TreeSet<SearchResult>();
        Enumeration<String> names = searchResults.keys();
        while (names.hasMoreElements()) {
            String service = names.nextElement();
            String resultID = service;
            double searchscore = searchResults.get(service);
            double feedbackscore = feedbackResults.get(service);
            int uddiID = Integer.parseInt(service.split(":")[1]);
            String operation = "";
            if (service.startsWith("O")) {
                operation = service;
                service = ServiceGraph.getInstance().getOperationService(operation);
                operation = operation.split(":")[2];
            }
            service = service.split(":")[2];
            sortedResults.add(new SearchResult(service, operation, resultID, uddiID, searchscore, feedbackscore));
        }
        return sortedResults.iterator();
    }
}
