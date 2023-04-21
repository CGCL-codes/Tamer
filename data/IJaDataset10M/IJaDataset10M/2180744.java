package wssearch.plugin.html;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import wssearch.Properties;
import wssearch.graph.ServiceGraph;
import wssearch.graph.WsGraph;
import wssearch.messages.Messages;
import wssearch.search.SearchResult;

/**
 * Generiert HTML-Code für die Ergebnisliste
 * @author Thorsten Theelen
 *
 */
public class HtmlGenerator {

    private int resultID;

    private final String br = "<br />";

    private final String sp = "&ensp;";

    private Hashtable<Integer, SearchResult> results;

    private WsGraph sGraph;

    public HtmlGenerator() {
        resultID = 0;
        results = new Hashtable<Integer, SearchResult>();
        sGraph = ServiceGraph.getInstance().getGraph();
    }

    private String jsFunctions() {
        String html = "<script type=\"text/javascript\">";
        html += "function details (ID){window.status=\"ws0:\"+ID;}";
        html += "function palette (ID){window.status=\"ws1:\"+ID;}";
        html += "function relevant(ID){window.status=\"ws2:\"+ID;}";
        html += "function notRelevant(ID){window.status=\"ws3:\"+ID;}";
        html += "function page (PAGE) {window.status=\"ws4:\"+PAGE;}";
        html += "</script>";
        return html;
    }

    private String css() {
        String html = "<style type=\"text/css\">";
        html += "p {font-family: verdana, sans-serif; font-size:11}";
        html += "a {font-family: verdana, sans-serif; font-size:11; text-decoration: underline; cursor: hand}";
        return html + "</style>";
    }

    private String header() {
        String html = "<html><head>";
        html += jsFunctions();
        html += css();
        html += "</head><body>";
        return html;
    }

    private String end() {
        return "</body></html>";
    }

    private String service(int id, String service, boolean bold) {
        String b1 = "";
        String b2 = "";
        if (bold) {
            b1 = "<b>";
            b2 = "</b> " + Messages.HtmlGeneratorB2Service;
        }
        return "<a onClick=\"details('" + id + "');\" title=\"Klicken, um Dienst-Details anzuzeigen.\">" + b1 + service + b2 + "</a>";
    }

    private String operation(int id, String operation, boolean bold) {
        String b1 = "";
        String b2 = "";
        if (bold) {
            b1 = "<b>";
            b2 = "</b> " + Messages.HtmlGeneratorB2Operation;
        }
        return "<a onClick=\"palette('" + id + ":" + operation + "');\" title=\"Klicken, um Operation in Palette zu übernehmen.\">" + b1 + operation + b2 + "</a>";
    }

    private String relevance(int id) {
        String html = Messages.HtmlGeneratorHtml + "&#160;";
        html += "<a onClick=\"relevant('" + id + "');\"  title=\"Klicken, wenn das Ergebnis für Sie relevant ist.\"><img src=\"" + Properties.rtPath + Properties.isRelevantPic + "\" height=16 width=16></a>" + sp;
        html += "<a onClick=\"notRelevant('" + id + "');\" title=\"Klicken, wenn das Ergebnis für Sie nicht relevant ist.\"><img src=\"" + Properties.rtPath + Properties.isNotRelevantPic + "\" height=16 width=16></a>";
        return html;
    }

    private String result(SearchResult sr) {
        results.put(++resultID, sr);
        String html = "<p>";
        if (sr.getOperation().isEmpty()) {
            html += serviceResult(sr);
        } else {
            html += operationResult(sr);
        }
        return html + br + score(sr) + br + relevance(resultID) + "</p>";
    }

    private String score(SearchResult sr) {
        double rScore = sr.getFeedbackscore();
        double sScore = sr.getSearchScore();
        int width = 50;
        String rPic = Properties.rtPath + Properties.greenPic;
        String sPic = Properties.rtPath + Properties.greenPic;
        String grey = Properties.rtPath + Properties.greyPic;
        if (rScore < 0.33) rPic = Properties.rtPath + Properties.redPic; else if (rScore < 0.66) rPic = Properties.rtPath + Properties.orangePic;
        if (sScore < 0.33) sPic = Properties.rtPath + Properties.redPic; else if (sScore < 0.66) sPic = Properties.rtPath + Properties.orangePic;
        String scoreString = Messages.HtmlGeneratorscoreString + " " + Math.round(sScore * 10000) / 100 + "%";
        String relevanceString = Messages.HtmlGeneratorrelevanceString + " " + Math.round(rScore * 10000) / 100 + "%";
        String html = Messages.HtmlGeneratorrelevanceString + " <img src=\"" + rPic + "\" width=" + rScore * width + " height=10 title=\"" + relevanceString + "\"><img src=\"" + grey + "\" width=" + (1 - rScore) * width + " height=10 title=\"" + relevanceString + "\">";
        html += ", " + Messages.HtmlGeneratorscoreString + " <img src=\"" + sPic + "\" width=" + sScore * width + " height=10 title=\"" + scoreString + "\"><img src=\"" + grey + "\" width=" + (1 - sScore) * width + " height=10 title=\"" + scoreString + "\">";
        return html;
    }

    private String operationResult(SearchResult sr) {
        String s = sr.getService();
        String o = sr.getOperation();
        String html = operation(resultID, o, true) + br;
        String oNodeID = "O:" + sr.getUddiID() + ":" + o;
        String oDescr = sGraph.getDescription(oNodeID);
        if (!oDescr.isEmpty()) {
            if (oDescr.length() > Properties.maxDescLength) oDescr = oDescr.substring(0, Properties.maxDescLength) + "...";
            html += oDescr + br;
        }
        html += Messages.HtmlGeneratorB2Service + " " + service(resultID, s, false);
        return html;
    }

    private String serviceResult(SearchResult sr) {
        String s = sr.getService();
        String html = service(resultID, s, true) + br;
        String sNodeID = "S:" + sr.getUddiID() + ":" + s;
        String descr = sGraph.getDescription(sNodeID);
        if (!descr.isEmpty()) {
            if (descr.contains("::")) {
                String[] split = descr.split("::");
                if (split.length > 1) descr = split[1]; else descr = "";
            }
            if (descr.length() > Properties.maxDescLength) descr = descr.substring(0, Properties.maxDescLength) + "...";
            if (descr.length() > 0) html += descr + br;
        }
        html += Messages.HtmlGeneratorB2Operation + ":" + sp;
        Enumeration<String> ops = ServiceGraph.getInstance().getServiceOperations(sNodeID).elements();
        Hashtable<String, Boolean> inList = new Hashtable<String, Boolean>();
        while (ops.hasMoreElements()) {
            String nextO = ops.nextElement().split(":")[2];
            if (!inList.containsKey(nextO)) {
                inList.put(nextO, true);
                html += operation(resultID, nextO, false) + sp;
            }
        }
        return html;
    }

    private String pageSelect(int currentPage, int totalPages) {
        String html = br;
        if (currentPage > 1) html += "<a onClick=\"page('" + (currentPage - 1) + "');\"  title=\"vorherige Seite\"><</a>" + sp;
        for (int i = 1; i <= totalPages; i++) {
            String b1 = "";
            String b2 = "";
            if (i == currentPage) {
                b1 = "<b>";
                b2 = "</b>";
            }
            html += "<a onClick=\"page('" + i + "');\"  title=\"Seite " + i + "\">" + b1 + i + b2 + "</a>" + sp;
        }
        if (currentPage < totalPages) html += "<a onClick=\"page('" + (currentPage + 1) + "');\"  title=\"nächste Seite\">></a>";
        return html;
    }

    /**
	 * Generiert aus Suchergebnis-Iterator HTML-Code für eine Seite der Ergebnisliste
	 * @param searchResults Suchergebnisse
	 * @param page anzuzeigende Seite
	 * @return HTML-Code
	 */
    public String generateHtml(Iterator<SearchResult> searchResults, int page) {
        String html = header();
        int rpp = Properties.resultsPerPage;
        int lastResult = page * rpp;
        int i = 0;
        if (searchResults.hasNext()) {
            while (searchResults.hasNext()) {
                if (i >= lastResult - rpp && i < lastResult) {
                    html += result(searchResults.next());
                } else searchResults.next();
                i++;
            }
            html += pageSelect(page, (i / rpp) + 1);
        } else html += "<p>Keine Suchergebnisse!</p>";
        return html + end();
    }

    /**
	 * Platzhalter-Seite vor Auswahl eines Benutzers
	 * @return
	 */
    public String generateNoUser() {
        String html = header();
        html += "<p>Bitte zuerst Benutzer auswählen!</p>";
        return html + end();
    }

    /**
	 * Liefert Suchergebnis zu einer Ergebnis-ID
	 * @param resultID
	 * @return Suchergebnis
	 */
    public SearchResult getSearchResult(int resultID) {
        return results.get(resultID);
    }
}
