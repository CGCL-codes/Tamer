package fr.wbr;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe
 * Date: 27 ao�t 2006
 * Time: 15:58:42
 * To change this template use File | Settings | File Templates.
 */
public class SiteThreadITISSubTaxonList extends SiteThread {

    private SiteThreadITISSubTaxonList() {
        super("ITIS sub taxon list", new Options.WikiSite[] { Options.WikiSite.wikiFrance, Options.WikiSite.wikiCommons, Options.WikiSite.wikiSpecies });
    }

    NonRegressionRequest[] getNonRegressionRequests() {
        return new NonRegressionRequest[0];
    }

    String getDescription() {
        return "";
    }

    boolean shouldBeStarted() {
        return false;
    }

    void doRequest(String taxon) {
        assert false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static SiteThread mainInstance_ = new SiteThreadITISSubTaxonList();

    public static SiteThread getMainInstance() {
        return mainInstance_;
    }
}
