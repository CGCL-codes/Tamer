package org.fao.oaipmh.requests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.fao.geonet.util.ISODate;
import org.fao.oaipmh.exceptions.OaiPmhException;
import org.fao.oaipmh.responses.ListRecordsResponse;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

public class ListRecordsRequest extends ListRequest {

    public static final String VERB = "ListRecords";

    public ISODate getFrom() {
        return from;
    }

    public ISODate getUntil() {
        return until;
    }

    public String getMetadataPrefix() {
        return mdPrefix;
    }

    public String getSet() {
        return set;
    }

    public void setFrom(ISODate date) {
        from = date;
    }

    public void setUntil(ISODate date) {
        until = date;
    }

    public void setMetadataPrefix(String mdPrefix) {
        this.mdPrefix = mdPrefix;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public ListRecordsResponse execute() throws IOException, OaiPmhException, JDOMException, SAXException, Exception {
        Map<String, String> params = new HashMap<String, String>();
        if (resumpToken != null) params.put("resumptionToken", resumpToken); else {
            params.put("metadataPrefix", mdPrefix);
            if (from != null) params.put("from", from.toString() + "Z");
            if (until != null) params.put("until", until.toString() + "Z");
            if (set != null) params.put("set", set);
        }
        return new ListRecordsResponse(this, sendRequest(params));
    }

    public String getVerb() {
        return VERB;
    }

    private ISODate from;

    private ISODate until;

    private String mdPrefix;

    private String set;
}
