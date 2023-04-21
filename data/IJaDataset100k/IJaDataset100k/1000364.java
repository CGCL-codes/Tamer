package com.google.gwt.i18n.client.impl.cldr;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Localized names for the "ku_Latn" locale.
 */
public class LocalizedNamesImpl_ku_Latn extends LocalizedNamesImpl_ku {

    @Override
    public String[] loadLikelyRegionCodes() {
        return new String[] { "TR" };
    }

    @Override
    public String[] loadSortedRegionCodes() {
        return new String[] { "AC", "AD", "AE", "AG", "AI", "AN", "AO", "AQ", "AS", "AW", "AX", "BA", "BB", "BD", "BF", "BG", "BI", "BJ", "BL", "BM", "BN", "BO", "BS", "BT", "BV", "BW", "BY", "BZ", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CM", "CO", "CP", "CR", "CV", "CX", "CZ", "DG", "DJ", "DK", "DM", "DO", "EA", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "EU", "FI", "FJ", "FK", "FM", "FO", "GA", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "IC", "ID", "IE", "IL", "IM", "IO", "IR", "IS", "JE", "JM", "JO", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "QO", "RE", "RO", "RS", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ", "TA", "TC", "TD", "TF", "TG", "TH", "TR", "TJ", "TK", "TL", "TM", "TN", "TO", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW", "AR", "AZ", "AT", "AU", "IT", "AM", "AF", "DZ", "AL", "DE", "BR", "BH", "BE", "GB", "CL", "IQ", "FR", "CY", "US", "CN", "RU", "JP", "CA", "CU", "IN" };
    }

    @Override
    protected void loadNameMapJava() {
        super.loadNameMapJava();
        namesMap.put("001", "Cîhan");
        namesMap.put("TR", "Tirkiye");
    }

    @Override
    protected JavaScriptObject loadNameMapNative() {
        return overrideMap(super.loadNameMapNative(), loadMyNameMap());
    }

    private native JavaScriptObject loadMyNameMap();
}
