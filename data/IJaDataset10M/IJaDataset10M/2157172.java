package com.siemens.ct.exi.datatype;

import com.siemens.ct.exi.util.ExpandedName;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20080718
 */
public class DatatypeInteger extends AbstractDatatype {

    public DatatypeInteger(ExpandedName datatypeIdentifier) {
        super(BuiltInType.BUILTIN_INTEGER, datatypeIdentifier);
    }
}
