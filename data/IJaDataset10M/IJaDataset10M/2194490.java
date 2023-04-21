package org.apache.webdav.lib.search;

import org.apache.webdav.lib.Constants;
import org.apache.webdav.lib.util.QName;

/**
 * @author Stefan Lützkendorf
 */
public final class CompareOperator extends QName {

    public static final CompareOperator EQ = new CompareOperator(Constants.DAV, "eq");

    public static final CompareOperator LT = new CompareOperator(Constants.DAV, "lt");

    public static final CompareOperator GT = new CompareOperator(Constants.DAV, "gt");

    public static final CompareOperator LTE = new CompareOperator(Constants.DAV, "lte");

    public static final CompareOperator GTE = new CompareOperator(Constants.DAV, "gte");

    public static final CompareOperator LIKE = new CompareOperator(Constants.DAV, "like");

    public static final CompareOperator PROPERTY_CONTAINS = new CompareOperator(Constants.SLIDE, "property-contains");

    private CompareOperator(String namespace, String name) {
        super(namespace, name);
    }
}
