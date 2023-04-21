package com.aurecon.kwb.data;

import java.util.ArrayList;
import java.util.Iterator;
import com.aurecon.kwb.core.KWBConstants;
import com.aurecon.kwb.interfaces.ObjectProperty;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;

/**
 * Get Elements by Object Operation.
 * 
 * @author morey_surfer
 *
 */
final class GetElementsByObjectOperation extends AbstractGetElementsOperation {

    /**
	 * Operation name prefix.
	 */
    private static final String NAME = "Get Elements By Object Operation";

    /**
	 * New line.
	 */
    private static final String newLine = "\n";

    /**
	 * Start of the FOR line.
	 */
    private static final String forDocPrefix = "for $A in doc('dbxml:/";

    /**
	 * Splitter between container id and document id in the FOR line.
	 */
    private static final String forSplitter = "/";

    /**
	 * End of the FOR line.
	 */
    private static final String forSuffix = "')";

    /**
	 * Start of the LET line.
	 */
    private static final String letPrefix = "let $";

    /**
	 * End of the LET line.
	 */
    private static final String letSuffix = " := $A/KN";

    /**
	 * Start of the return line.
	 */
    private static final String returnPrefix = "return <result><a>{$";

    /**
	 * Splitter between properties on the return line.
	 */
    private static final String returnMiddle = "}</a><a>{$";

    /**
	 * End of the return line.
	 */
    private static final String returnSuffix = "}</a></result>";

    /**
	 * Operation name to return on request.
	 */
    private String operationName;

    /**
	 * Iterator of object ids.
	 */
    private Iterator<String> objItr;

    /**
	 * Current object id.
	 */
    private String tempObjectId;

    /**
	 * String builder for building queries.
	 */
    private StringBuilder queryBuilder;

    /**
	 * The "for" line for creating the query.
	 */
    private String forLine;

    /**
	 * Bottom half of the query.
	 */
    private String bottomOfQuery;

    /**
	 * Constructed query string holder.
	 */
    private String queryString;

    /**
	 * If the full operation is incomplete.
	 */
    private boolean incomplete = true;

    /**
	 * Priority.
	 */
    private int priority = Thread.NORM_PRIORITY;

    /**
	 * Constructor.
	 * 
	 * @param info			{@link XmlOperationInfo} operation info
	 * @param paths			{@link ArrayList} of paths to check
	 */
    public GetElementsByObjectOperation(XmlOperationInfo info, ArrayList<String> paths) {
        super(info, paths);
        if (opInfo.getDocumentIds().size() > 1) {
            operationName = NAME + OPEN_BRACKET + MULTIPLE_DOCS + CLOSE_BRACKET;
        } else {
            operationName = NAME + OPEN_BRACKET + opInfo.getDocumentIds().get(0) + KWBConstants.DOT_XML + CLOSE_BRACKET;
        }
        this.setTotal(opInfo.getDocumentIds().size());
        priority = XmlOperation.getReadPriority(this.getTotal());
    }

    @Override
    String getName() {
        return operationName;
    }

    @Override
    boolean isIncomplete() {
        return incomplete;
    }

    @Override
    String getCurrentQueryString() {
        return queryString;
    }

    @Override
    void prepareNextOperation() {
        if (objItr.hasNext()) {
            tempObjectId = objItr.next();
            queryBuilder.delete(0, queryBuilder.length());
            queryBuilder.append(forLine);
            queryBuilder.append(tempObjectId);
            queryBuilder.append(forSuffix);
            queryBuilder.append(newLine);
            queryBuilder.append(bottomOfQuery);
            queryString = queryBuilder.toString();
        } else {
            queryString = null;
            incomplete = false;
        }
    }

    @Override
    void prepareQueryStrings() {
        forLine = forDocPrefix + containerId + forSplitter;
        String letLines = "";
        objItr = opInfo.getDocumentIds().iterator();
        Iterator<String> propItr = propertyList.iterator();
        Iterator<String> itr = this.getListOfCharacters().iterator();
        String tempChar = itr.next();
        letLines = letLines + letPrefix + tempChar + letSuffix + propItr.next() + newLine;
        String returnLine = returnPrefix + tempChar;
        while (itr.hasNext() && propItr.hasNext()) {
            tempChar = itr.next();
            letLines = letLines + letPrefix + tempChar + letSuffix + propItr.next() + newLine;
            returnLine = returnLine + returnMiddle + tempChar;
        }
        returnLine = returnLine + returnSuffix;
        bottomOfQuery = letLines + returnLine;
        int querySize = 40 + forLine.length() + forSuffix.length() + newLine.length() + bottomOfQuery.length();
        queryBuilder = new StringBuilder(querySize);
    }

    @Override
    void processResults(XmlResults xresults) throws XmlException, Exception {
        XmlValue temp1 = xresults.next();
        temp1 = temp1.getFirstChild();
        ArrayList<ObjectProperty> props = this.readProperties(temp1);
        synchronized (resultsMutex) {
            results.put(tempObjectId, props);
        }
        incrementCompletedCount();
    }

    @Override
    int getPriority() {
        return priority;
    }

    public boolean isComplete() {
        return !incomplete;
    }
}
