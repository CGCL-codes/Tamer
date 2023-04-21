package com.google.gwt.i18n.client.impl.cldr;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Localized names for the "es_CL" locale.
 */
public class LocalizedNamesImpl_es_CL extends LocalizedNamesImpl_es {

    @Override
    public String[] loadSortedRegionCodes() {
        return new String[] { "AF", "AL", "DE", "AD", "AO", "AI", "AQ", "AG", "AN", "SA", "DZ", "AR", "AM", "AW", "AU", "AT", "AZ", "BS", "BH", "BD", "BB", "BE", "BZ", "BJ", "BM", "BY", "BO", "BA", "BW", "BQ", "BR", "BN", "BG", "BF", "BI", "BT", "CV", "KH", "CM", "CA", "EA", "TD", "CL", "CN", "CY", "VA", "CO", "KM", "CG", "KP", "KR", "CI", "CR", "HR", "CU", "CW", "DG", "DK", "DM", "EC", "EG", "SV", "AE", "ER", "SK", "SI", "ES", "US", "EE", "ET", "PH", "FI", "FJ", "FR", "FX", "GA", "GM", "GE", "GH", "GI", "GD", "GR", "GL", "GP", "GU", "GT", "GF", "GG", "GN", "GW", "GQ", "GY", "HT", "HN", "HK", "HU", "IN", "ID", "IR", "IQ", "IE", "BV", "CX", "CP", "AC", "IM", "IS", "NU", "NF", "AX", "KY", "IC", "CC", "CK", "FO", "GS", "HM", "FK", "MP", "MH", "UM", "PN", "SB", "TC", "VG", "VI", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KG", "KI", "KW", "LA", "LS", "LV", "LB", "LR", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MY", "MW", "MV", "ML", "MT", "MA", "MQ", "MU", "MR", "YT", "MX", "FM", "MD", "MC", "MN", "ME", "MS", "MZ", "MM", "NA", "NR", "NP", "NI", "NE", "NG", "NO", "NC", "NZ", "OM", "NL", "PK", "PW", "PA", "PG", "PY", "PE", "PF", "PL", "PT", "PR", "QA", "GB", "CF", "CZ", "CD", "DO", "RE", "RW", "RO", "RU", "EH", "WS", "AS", "BL", "KN", "SM", "MF", "PM", "SH", "LC", "ST", "VC", "SN", "RS", "CS", "SC", "SL", "SG", "SY", "SO", "LK", "SS", "SZ", "ZA", "SD", "SE", "CH", "SR", "SJ", "SX", "TH", "TW", "TZ", "TJ", "IO", "PS", "QO", "TF", "TL", "TG", "TK", "TO", "TT", "TA", "TN", "TM", "TR", "TV", "UA", "UG", "EU", "UY", "UZ", "VU", "VE", "VN", "WF", "YE", "DJ", "ZM", "ZW" };
    }

    @Override
    protected void loadNameMapJava() {
        super.loadNameMapJava();
        namesMap.put("005", "Sudamérica");
        namesMap.put("AN", "Antillas Holandesas");
        namesMap.put("AZ", "Azerbayán");
        namesMap.put("EH", "Sahara Occidental");
        namesMap.put("PS", "Territorio Palestino");
        namesMap.put("RO", "Rumania");
        namesMap.put("SA", "Arabia Saudita");
        namesMap.put("TZ", "Tanzanía");
    }

    @Override
    protected JavaScriptObject loadNameMapNative() {
        return overrideMap(super.loadNameMapNative(), loadMyNameMap());
    }

    private native JavaScriptObject loadMyNameMap();
}
