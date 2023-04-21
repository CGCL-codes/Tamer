package org.hl7.CTSMAPI;

/**
 *  RIM Coded Attribute
 *	RIMAttribute_id		- unique attribute identifier (model, class, attribute)
 *	dataType_code		- the specific data type of the attribute (CD, CE, CS, ...)
 *	codingStrength_code 	- coded value that represents the strength (CWE, etc) of the attribute
 *	vocabularyDomain_name 	- vocabulary domain (if any) of the attribute
 */
public final class RIMCodedAttribute implements org.omg.CORBA.portable.IDLEntity {

    public org.hl7.CTSMAPI.RIMAttributeId RIMAttribute_id = null;

    public org.hl7.types.ST dataType_code = null;

    public org.hl7.types.ST codingStrength_code = null;

    public org.hl7.types.ST vocabularyDomain_name = null;

    public RIMCodedAttribute() {
    }

    public RIMCodedAttribute(org.hl7.CTSMAPI.RIMAttributeId _RIMAttribute_id, org.hl7.types.ST _dataType_code, org.hl7.types.ST _codingStrength_code, org.hl7.types.ST _vocabularyDomain_name) {
        RIMAttribute_id = _RIMAttribute_id;
        dataType_code = _dataType_code;
        codingStrength_code = _codingStrength_code;
        vocabularyDomain_name = _vocabularyDomain_name;
    }
}
