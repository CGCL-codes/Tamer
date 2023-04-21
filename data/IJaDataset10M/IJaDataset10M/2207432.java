package fr.wbr;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe
 * Date: 27 ao�t 2006
 * Time: 15:58:42
 * To change this template use File | Settings | File Templates.
 */
public class SiteThreadAPWebsiteSubTaxonList extends SiteThread {

    private SiteThreadAPWebsiteSubTaxonList() {
        super("APWebsite sub taxon list", new Options.WikiSite[] { Options.WikiSite.wikiFrance, Options.WikiSite.wikiCommons });
    }

    NonRegressionRequest[] getNonRegressionRequests() {
        return new NonRegressionRequest[0];
    }

    String getDescription() {
        return "Plant family";
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

    private static SiteThread mainInstance_ = new SiteThreadAPWebsiteSubTaxonList();

    public static SiteThread getMainInstance() {
        return mainInstance_;
    }
}
