package fr.wbr;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe
 * Date: 27 ao�t 2006
 * Time: 15:58:42
 * To change this template use File | Settings | File Templates.
 */
public class SiteThreadAluka extends SiteThread {

    static final String taxonNotFound404_ = "Erreur sur www.aluka.org";

    private SiteThreadAluka() {
        super("Aluka");
    }

    String getDescription() {
        return "African plant species";
    }

    boolean shouldBeStarted() {
        assert (getRequest().getOptions().isWikiSiteActivated(Options.WikiSite.wikiFrance));
        if ((getSearchedRank() != null) && (getSearchedRank() != Rank.Species)) {
            return false;
        }
        if ((getSearchedType() != null) && (getSearchedType() != TYPE.PLANT)) {
            return false;
        }
        return true;
    }

    NonRegressionRequest[] getNonRegressionRequests() {
        return new NonRegressionRequest[] { new NonRegressionRequest("Andropogon fastigiatus", "* {{Aluka|Andropogon|fastigiatus|Sw. }}", "http://www.aluka.org/action/showCompilationPage?doi=10.5555/AL.AP.COMPILATION.PLANT-NAME-SPECIES.ANDROPOGON.FASTIGIATUS&cookieSet=1"), new NonRegressionRequest("Heteropogon contortus", "* {{Aluka|Heteropogon|contortus|(L.) Roem. et Schult. }}", "http://www.aluka.org/action/showCompilationPage?doi=10.5555/AL.AP.COMPILATION.PLANT-NAME-SPECIES.HETEROPOGON.CONTORTUS&cookieSet=1"), new NonRegressionRequest("Christophe christophe", null, null) };
    }

    void doRequest(String taxon) {
        int pos = taxon.indexOf(" ");
        if (pos <= 0) {
            displayFinalError("taxon doesn't look like a specie: " + taxon);
            return;
        }
        String genus = taxon.substring(0, pos).trim();
        String species = taxon.substring(pos + 1, taxon.length()).trim();
        displayTempStatus("genus='" + genus + "' species='" + species + "'");
        String url;
        String response;
        HTTPClient c = null;
        try {
            url = "http://www.aluka.org/action/showCompilationPage?doi=10.5555/AL.AP.COMPILATION.PLANT-NAME-SPECIES." + genus.toUpperCase() + "." + species.toUpperCase() + "&cookieSet=1";
            displayTempStatus(url);
            c = new HTTPClient(url, this);
            Properties properties = new Properties();
            properties.setProperty("Cookie", "I2KBRCK=1; I2KBRCK=1");
            c.connect("GET", null, properties);
            int errorCode = c.getResponseCode();
            if (200 != errorCode) {
                displayFinalError("returned errorCode " + errorCode + " on " + url);
                return;
            }
            StringBuilder responseBuilder = new StringBuilder(50 * 1024);
            c.displayResponse(null, responseBuilder, HTTPClient.utf8);
            response = responseBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            displayFinalError(e.getMessage());
            return;
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        displayTempStatus("Received page");
        if (response.indexOf(taxon) < 0) {
            displayFinalError("returned page without " + taxon);
            return;
        }
        if (response.indexOf(taxonNotFound404_) >= 0) {
            displayFinalError("returned page with " + taxonNotFound404_);
            return;
        }
        displayFinalURL(url);
        String author = extractAuthor(genus, species, response);
        displayFinalResult("* {{Aluka|" + genus + "|" + species + author + " }}", true);
    }

    String extractAuthor(String genus, String species, String response) {
        StringBuilder title = new StringBuilder();
        int pos = extract(response, 0, "<title>", EXTRACT.WITHOUT, "</title>", EXTRACT.WITHOUT, title);
        if (pos <= 0) {
            return "";
        }
        String titleStr = cleanStringFromHTML(title.toString()).trim();
        if (titleStr.startsWith("Aluka -")) {
            titleStr = titleStr.substring("Aluka -".length()).trim();
        }
        pos = titleStr.indexOf("[");
        if (pos > 0) {
            titleStr = titleStr.substring(0, pos).trim();
        }
        titleStr = titleStr.replace(genus, "").replace(species, "").trim().replace("  ", " ");
        if (titleStr.length() > 0) titleStr = "|" + titleStr;
        return titleStr;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static SiteThread mainInstance_ = new SiteThreadAluka();

    public static SiteThread getMainInstance() {
        return mainInstance_;
    }
}
