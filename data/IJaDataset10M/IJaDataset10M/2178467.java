package uk.co.pointofcare.echobase.terminology.rest;

import javax.annotation.Resource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.ws.WebServiceContext;
import uk.co.pointofcare.echobase.terminology.atc.webpage.SearchType;
import uk.co.pointofcare.echobase.terminology.soap.AtcSearchRequestType;
import uk.co.pointofcare.echobase.terminology.soap.AtcSearchService;
import uk.pointofcare.echobase.terminology.atc.model.AtcSearchResult;

@Path("/atcRestSearch")
public class AtcRest {

    public AtcRest() {
        searcher = new AtcSearchService();
        searcher.setup();
    }

    AtcSearchService searcher;

    public enum Actions {

        SEARCH("search"), DETAIL("getDetail");

        String s;

        Actions(String s) {
            this.s = s;
        }

        public boolean equals(String s) {
            return s.equals(this.s);
        }

        public String toString() {
            return this.s;
        }
    }

    @Resource
    protected WebServiceContext wsContext;

    @GET
    @Produces("text/plain")
    public String invoke(@QueryParam("term") String term, @QueryParam("item") @DefaultValue("1") int item, @QueryParam("action") @DefaultValue("10") String action) {
        if (Actions.SEARCH.equals(action)) {
            AtcSearchResult qry = searcher.searchTerm(AtcSearchRequestType.create(term, SearchType.CONTAINING));
            StringBuilder out = new StringBuilder();
            out.append(qry.conceptList.get(item).code + "\t");
            out.append(qry.conceptList.get(item).term + "\t");
            return out.toString();
        } else if (Actions.DETAIL.equals(action)) {
            return "not implemented";
        } else {
            return "invalid input";
        }
    }
}
