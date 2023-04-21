package org.kidsd.dspace.remotesearch.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.kidsd.dspace.remotesearch.LoadServersListFromXml;
import org.kidsd.dspace.remotesearch.RemotedSearchAbstract;
import org.kidsd.dspace.remotesearch.SRWUtilAndConstants;
import org.kidsd.dspace.remotesearch.SearchRetrieveResponseBean;
import org.kidsd.dspace.remotesearch.ServersList;

/**
 * <p>Servlet for handling the remote search.
 *
 * It uses SRW to perform search by constructing the valid query string and
 * sending to all requested servers. The number of records found will be extracted
 * from XML and then shown to the user beside its corresponding server name with
 * a link to view its search detail.</p>
 *
 * The search result's detail will be handled by RemotedSearchServlet
 *
 * @author Kopkaj Oupapatig
 */
public class RemotedSearchListServlet extends RemotedSearchAbstract {

    /** logger */
    protected static Logger log = Logger.getLogger(RemotedSearchListServlet.class);

    protected static final long serialVersionUID = 1L;

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        performRemotedScanSearch(context, request, response);
        JSPManager.showJSP(request, response, "/remotesearch/remotescan.jsp");
    }

    private void performRemotedScanSearch(Context context, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("performRemotedScanSearch");
        String queryStringSRW = rebuildQueryForSRW(request, response, 0);
        int[] indexNumber = buildIndexNumber(request);
        ServersList svList = fetchServerURL(request);
        request.setAttribute("query", request.getQueryString());
        String listQueryURL = null;
        int[] hitCount = new int[indexNumber.length];
        StringBuffer responseBuffer = new StringBuffer("");
        HttpClient client = new HttpClient();
        HttpMethod method = null;
        DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler(3, false);
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
        String responseString = null;
        SearchRetrieveResponseBean result;
        for (int i = 0; i < indexNumber.length; i++) {
            int index = indexNumber[i];
            listQueryURL = generateServerNameForListQuery(svList.getDetailAt(index).getValue()) + queryStringSRW;
            log.info(LogManager.getHeader(context, "QueryToSRW at " + i, listQueryURL));
            method = new GetMethod(listQueryURL);
            try {
                int statusCode = client.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    log.error("Method failed: " + method.getStatusLine());
                    hitCount[i] = -1;
                } else {
                    byte[] responseBody = method.getResponseBody();
                    responseString = new String(responseBody, Constants.DEFAULT_ENCODING);
                    log.info(LogManager.getHeader(context, "resultString at " + i, responseString));
                    result = parseXmlToResultBean(responseString, context);
                    hitCount[i] = result.getNumberOfRecords();
                    responseBuffer.append(svList.getDetailAt(index).getName());
                }
                if (hitCount[i] < 0) {
                    responseBuffer.append(svList.getDetailAt(index).getName() + " - " + SRWUtilAndConstants.ERROR_MESSAGE_FROM_HTTPCLIENT + "<br />");
                } else {
                    responseBuffer.append(" ค้นพบ ");
                    responseBuffer.append(hitCount[i]);
                    responseBuffer.append(" รายการ - ");
                    responseBuffer.append("<a href=\"" + request.getContextPath());
                    responseBuffer.append("/remotesearch?");
                    responseBuffer.append(getBaseQuery(request.getQueryString()));
                    responseBuffer.append("&" + SRWUtilAndConstants.SERVER_NAME_PARAMETER + "=");
                    responseBuffer.append(svList.getDetailAt(index).getValue());
                    responseBuffer.append("\" target=\"_blank\">");
                    responseBuffer.append("เรียกดู");
                    responseBuffer.append("</a>");
                    responseBuffer.append("<br />\n");
                }
            } catch (HttpException e) {
                log.error(e);
                responseBuffer.append(svList.getDetailAt(index).getName() + " - " + SRWUtilAndConstants.ERROR_MESSAGE_FROM_HTTPCLIENT + "<br />");
                hitCount[i] = -1;
            } catch (IOException e) {
                log.error(e);
                responseBuffer.append(svList.getDetailAt(index).getName() + " - " + SRWUtilAndConstants.ERROR_MESSAGE_FROM_HTTPCLIENT + "<br />");
                hitCount[i] = -1;
            }
        }
        request.setAttribute("responseText", responseBuffer.toString());
    }

    private ServersList fetchServerURL(HttpServletRequest request) {
        ServersList svList = null;
        svList = LoadServersListFromXml.load();
        return svList;
    }

    private String generateServerNameForListQuery(String serverValue) {
        return serverValue;
    }

    private int[] buildIndexNumber(HttpServletRequest request) {
        String searchIndex = request.getParameter(searchAddressIndex);
        String[] indexString = {};
        if (searchIndex != null) {
            indexString = request.getParameter(searchAddressIndex).split(SRWUtilAndConstants.SERVER_SEPERATOR);
        }
        int[] indexInt = null;
        try {
            indexInt = new int[indexString.length];
            for (int i = 0; i < indexString.length; i++) {
                indexInt[i] = Integer.parseInt(indexString[i]);
            }
        } catch (Exception e) {
            indexInt = new int[0];
        }
        return indexInt;
    }

    private String getBaseQuery(String queryString) {
        queryString = removeParameterFromQuery(queryString, "&operation");
        queryString = removeParameterFromQuery(queryString, "&" + SRWUtilAndConstants.SERVER_LIST_PARAMETER);
        return queryString;
    }
}
