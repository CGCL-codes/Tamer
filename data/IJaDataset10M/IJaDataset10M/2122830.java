package com.webstarsltd.common.pim.model;

/**
 *
 * @version $Id: ValueRelated.java,v 1.3 2007/06/18 12:36:58 luigiafassina Exp $
 */
public class ValueRelated implements ValueInterface {

    public boolean checkValue(String v) {
        if ("START".equals(v) || "END".equals(v)) {
            return true;
        }
        return false;
    }
}
