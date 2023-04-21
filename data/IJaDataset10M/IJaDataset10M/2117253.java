package com.google.gwt.i18n.client.impl.cldr;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Localized names for the "aa" locale.
 */
public class LocalizedNamesImpl_aa extends LocalizedNamesImpl {

    @Override
    public String[] loadSortedRegionCodes() {
        return new String[] { "AC", "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BQ", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CN", "CI", "CK", "CL", "CM", "CO", "CP", "CR", "CU", "CV", "CW", "CX", "CY", "CZ", "DG", "DK", "DM", "DO", "DZ", "EA", "EC", "EE", "EG", "EH", "ER", "ES", "EU", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GD", "GE", "DE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "IC", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JP", "JE", "JM", "JO", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "ET", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "QO", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SX", "SY", "SZ", "TA", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "GB", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "DJ", "YE", "YT", "ZA", "ZM", "ZW" };
    }

    @Override
    protected void loadNameMapJava() {
        super.loadNameMapJava();
        namesMap.put("DJ", "Yabuuti");
        namesMap.put("ER", "Eretria");
        namesMap.put("ET", "Otobbia");
    }

    @Override
    protected JavaScriptObject loadNameMapNative() {
        return overrideMap(super.loadNameMapNative(), loadMyNameMap());
    }

    private native JavaScriptObject loadMyNameMap();
}
