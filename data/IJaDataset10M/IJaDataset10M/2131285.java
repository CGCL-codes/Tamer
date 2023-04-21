package com.cfinkel.reports.util;

import com.cfinkel.reports.exceptions.BadReportSyntaxException;
import com.cfinkel.reports.generatedbeans.GeneratedQueryElement;
import com.cfinkel.reports.generatedbeans.PreparedQueryElement;
import com.cfinkel.reports.generatedbeans.QueryElement;
import com.cfinkel.reports.wrappers.GeneratedQuery;
import com.cfinkel.reports.wrappers.PreparedQuery;
import com.cfinkel.reports.wrappers.Query;
import com.cfinkel.reports.wrappers.Report;

/**
 * Created by IntelliJ IDEA.
 * User: charles
 * Date: Mar 26, 2006
 * Time: 9:28:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryFactory {

    public static Query getQuery(QueryElement queryElement, Report report) throws BadReportSyntaxException {
        Query query;
        if (queryElement instanceof PreparedQueryElement) {
            query = new PreparedQuery((PreparedQueryElement) queryElement, report);
        } else {
            query = new GeneratedQuery((GeneratedQueryElement) queryElement);
        }
        return query;
    }
}
