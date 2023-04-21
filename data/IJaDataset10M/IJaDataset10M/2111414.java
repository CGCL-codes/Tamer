package saadadb.vo.request;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.util.Messenger;
import saadadb.vo.request.VORequest;
import saadadb.vo.request.formator.Formator;
import saadadb.vo.request.formator.fits.SaadaqlFitsFormator;
import saadadb.vo.request.formator.votable.SaadaqlVotableFormator;
import saadadb.vo.request.query.SaadaqlQuery;
import saadadb.vo.request.query.VOQuery;

/** * @version $Id: SaadaqlRequest.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * @author laurent
 * @version 07/2011
 */
public class SaadaqlRequest extends VORequest {

    public SaadaqlRequest(String sessionID, String reportDir) {
        super(new SaadaqlQuery(), "Saada Native", "", sessionID, reportDir);
    }

    /**
	 * Constructor mainly used by sub-classes
	 * @param voQuery
	 * @param protocolName
	 * @param protocolVersion
	 * @param sessionID
	 * @param reportDir
	 */
    protected SaadaqlRequest(VOQuery voQuery, String protocolName, String protocolVersion, String sessionID, String reportDir) {
        super(voQuery, protocolName, protocolVersion, sessionID, reportDir);
    }

    @Override
    public void init(Map<String, String> params) throws Exception {
        this.voQuery.setParameters(params);
        this.voQuery.buildQuery();
    }

    protected void addVotableFormator() throws QueryException {
        this.formators.put("votable", new SaadaqlVotableFormator());
    }

    protected void addFitsFormator() throws QueryException {
        this.formators.put("fits", new SaadaqlFitsFormator());
    }

    public void addFormator(String format) throws QueryException {
        if (this.formators.size() == 0) {
            super.addFormator(format);
        } else {
            QueryException.throwNewException(SaadaException.UNSUPPORTED_MODE, "SaadaQL request support only one formator");
        }
    }

    @Override
    public void runQuery() throws Exception {
        this.voQuery.runQuery();
        Messenger.printMsg(Messenger.TRACE, "Query done (no size returned in current mode)");
    }

    /**
	 * Invoke each formator and put response file names in a map with the format as key
	 * @return
	 * @throws QueryException
	 */
    public Map<String, String> buildResponses() throws Exception {
        if (formators.size() == 0) {
            QueryException.throwNewException(SaadaException.MISSING_RESOURCE, "VO request has no Response formator");
        }
        Map<String, String> retour = new LinkedHashMap<String, String>();
        for (Entry<String, Formator> esf : formators.entrySet()) {
            Formator fmter = esf.getValue();
            fmter.setProtocolParams(voQuery.getProtocolParams());
            fmter.setResultSet(voQuery.getSaadaInstanceResultSet());
            if (oids != null && oids.size() > fmter.getLimit()) {
                Messenger.printMsg(Messenger.WARNING, "Result (" + oids.size() + "elements) trucanted to formator limit (" + fmter.getLimit() + " )");
            }
            Messenger.printMsg(Messenger.TRACE, "Build Response file " + fmter.getResponseFilePath());
            fmter.buildDataResponse();
            retour.put(esf.getKey(), fmter.getResponseFilePath());
        }
        return retour;
    }
}
