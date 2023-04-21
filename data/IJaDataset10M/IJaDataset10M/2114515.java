package com.windsor.node.common.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.Enum;

public final class RequestType extends Enum {

    public static final RequestType NONE = new RequestType("None");

    public static final RequestType SOLICIT = new RequestType("Solicit");

    public static final RequestType QUERY = new RequestType("Query");

    public static final RequestType EXECUTE = new RequestType("Execute");

    public static final RequestType SUBMIT = new RequestType("Submit");

    private static final long serialVersionUID = 1;

    private RequestType(String type) {
        super(type);
    }

    public static Map getEnumMap() {
        return getEnumMap(RequestType.class);
    }

    public static List getEnumList() {
        return getEnumList(RequestType.class);
    }

    public static Iterator iterator() {
        return iterator(RequestType.class);
    }
}
