package org.kidsd.dspace.remotesearch.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
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
import org.dspace.search.QueryResults;
import org.kidsd.dspace.remotesearch.RecordBean;
import org.kidsd.dspace.remotesearch.RemotedSearchAbstract;
import org.kidsd.dspace.remotesearch.SRWUtilAndConstants;
import org.kidsd.dspace.remotesearch.SearchRetrieveResponseBean;

/**
 * Servlet for handling the remote search.
 *
 * It uses SRW to perform search by constructing the valid query string and
 * sending to the requested server. Then, it will parse the XML from SRW
 * to a Java Bean, which will be converted to the DSpace Item's bean and
 * QueryResult's bean.
 *
 * After that, the JSP file and the JSPTag bean will handle and format the output.
 *
 * @author Kopkaj Oupapatig
 */
public class RemotedSearchServlet extends RemotedSearchAbstract {

    /** logger */
    protected static Logger log = Logger.getLogger(RemotedSearchServlet.class);

    protected static final long serialVersionUID = 1L;

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        performRemotedSearch(context, request, response);
        request.setAttribute("queryWithoutStartParameter", fetchQueryWithoutStartParameter(request.getQueryString()));
        request.setAttribute("remoteQueryURL", generateRemoteQueryURL(request));
        JSPManager.showJSP(request, response, "/remotesearch/remote.jsp");
    }

    private void performRemotedSearch(Context context, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        QueryResults qResults = new QueryResults();
        SearchRetrieveResponseBean result;
        String queryStringSRW = rebuildQueryForSRW(request, response, maximumRecordsForSRWSearching);
        request.setAttribute("query", request.getQueryString());
        String remoteURLwithQueryString = constructSearchURL(request, queryStringSRW);
        log.info(LogManager.getHeader(context, "searchSRW", "SRWQueryString: " + remoteURLwithQueryString));
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(remoteURLwithQueryString);
        DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler(3, false);
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
        String responseString = null;
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            byte[] responseBody = method.getResponseBody();
            responseString = new String(responseBody, Constants.DEFAULT_ENCODING);
            result = parseXmlToResultBean(responseString, context);
            resultRemotedItems = parseResultBeanToItem(result, context, request);
            request.setAttribute("resultItems", resultRemotedItems);
            qResults = generateQueryResults(result, request);
            int pageTotal = 1 + ((qResults.getHitCount() - 1) / qResults.getPageSize());
            int pageCurrent = 1;
            if (retrieveStartRecord(request) >= 0) {
                pageCurrent = 1 + (retrieveStartRecord(request) / qResults.getPageSize());
            }
            int pageLast = ((pageCurrent + maxPageDisplay) > pageTotal) ? pageTotal : (pageCurrent + maxPageDisplay);
            int pageFirst = ((pageCurrent - maxPageDisplay) > 1) ? (pageCurrent - maxPageDisplay) : 1;
            request.setAttribute("queryresults", qResults);
            request.setAttribute("pagetotal", new Integer(pageTotal));
            request.setAttribute("pagecurrent", new Integer(pageCurrent));
            request.setAttribute("pagelast", new Integer(pageLast));
            request.setAttribute("pagefirst", new Integer(pageFirst));
        } catch (IOException e) {
            request.setAttribute("responseFromHTTPClient", SRWUtilAndConstants.ERROR_MESSAGE_FROM_HTTPCLIENT);
            qResults.setErrorMsg("Error while searching database!");
        } finally {
            method.releaseConnection();
        }
    }

    /**
   * Append SRW's query string with server name or IP.
   *
   */
    private String constructSearchURL(HttpServletRequest request, String queryString) {
        StringBuffer remoteQueryString = new StringBuffer("");
        remoteQueryString.append(request.getParameter(addressQueryName));
        remoteQueryString.append(queryString);
        return remoteQueryString.toString();
    }

    /**
   * To let DSpace search multiple pages by append the start parameter in this query.
   */
    private String fetchQueryWithoutStartParameter(String query) {
        String tempParameterName = "?".concat(startPositionParameter.substring(1, startPositionParameter.length()));
        if (query.contains(startPositionParameter)) {
            return removeParameterFromQuery(query, startPositionParameter);
        } else if (query.contains(tempParameterName)) {
            tempParameterName = tempParameterName.replace("?", "");
            tempParameterName = tempParameterName.concat("&");
            return removeParameterFromQuery(query, tempParameterName);
        }
        return query;
    }

    /**
   * Make a QueryResults bean to mimic the real DSpace process.
   *
   */
    private QueryResults generateQueryResults(SearchRetrieveResponseBean result, HttpServletRequest request) {
        QueryResults qResults = new QueryResults();
        qResults.setPageSize(maximumRecordsForSRWSearching);
        qResults.setHitCount(result.getNumberOfRecords());
        qResults.setErrorMsg(null);
        String startRecord = request.getParameter("startRecord");
        RecordBean[] record = result.getRecords().getRecord();
        if (startRecord == null || startRecord.length() == 0) {
            qResults.setStart(0);
        } else {
            qResults.setStart(Integer.parseInt(startRecord));
        }
        List itemType = new ArrayList();
        List handleList = new ArrayList();
        if (record != null) {
            for (int i = qResults.getStart(); i < qResults.getStart() + qResults.getPageSize(); i++) {
                if (i < record.length) {
                    handleList.add(toRemoteHandleString(record[i].getRecordData().getSrwDcDc().getDcIdentifierURI(), request));
                    itemType.add(new Integer(Constants.ITEM));
                }
            }
        }
        qResults.setHitTypes(itemType);
        qResults.setHitHandles(handleList);
        return qResults;
    }
}
