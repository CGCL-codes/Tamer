package com.spoledge.audao.parser.gql.impl.soft.func;

import com.google.appengine.api.datastore.GeoPt;

/**
 * GEOPT_LAT( geopt ) .<br/>
 *
 * Extracts latitude from the geopt.
 */
public class FuncGEOPT_LAT extends GeoPtFunc1 {

    protected Object getFunctionValue(GeoPt arg) {
        return new Double(arg.getLatitude());
    }
}
