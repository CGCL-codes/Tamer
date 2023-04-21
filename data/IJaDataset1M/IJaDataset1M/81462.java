package com.google.gdata.data.finance;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Google Finance.
 *
 * 
 */
public class FinanceNamespace {

    private FinanceNamespace() {
    }

    /** Google Finance (GF) namespace */
    public static final String GF = "http://schemas.google.com/finance/2007";

    /** Google Finance (GF) namespace prefix */
    public static final String GF_PREFIX = GF + "#";

    /** Google Finance (GF) namespace alias */
    public static final String GF_ALIAS = "gf";

    /** XML writer namespace for Google Finance (GF) */
    public static final XmlNamespace GF_NS = new XmlNamespace(GF_ALIAS, GF);
}
