package org.fao.geonet.kernel.oaipmh.services;

import java.util.List;
import org.fao.oaipmh.exceptions.BadResumptionTokenException;
import org.fao.oaipmh.responses.ListResponse;
import org.fao.oaipmh.responses.ResumptionToken;

class SearchResult {

    public SearchResult(String prefix) {
        this.prefix = prefix;
    }

    public int parseToken(String token) throws BadResumptionTokenException {
        if (!org.fao.geonet.lib.Lib.type.isInteger(token)) throw new BadResumptionTokenException("Invalid token : " + token);
        int pos = Integer.parseInt(token);
        if (pos >= ids.size()) throw new BadResumptionTokenException("Token beyond limit : " + token);
        this.token = token;
        return pos;
    }

    public void setupToken(ListResponse res, int pos) {
        if (pos < ids.size()) res.setResumptionToken(new ResumptionToken(Integer.toString(pos))); else {
            if (token != null) res.setResumptionToken(new ResumptionToken(""));
        }
    }

    public String prefix;

    public List<Integer> ids;

    private String token;
}
