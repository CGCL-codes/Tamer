package com.google.code.p.keytooliui.ktl.swing.table;

import com.google.code.p.keytooliui.shared.lang.*;

public final class TMEntryKprSel extends TMEntryKprAbs {

    private static final int _f_s_intNbRowMore = 1;

    public static final int f_s_intColumnIdElligible = 0;

    public static final int f_s_intColumnIdAlias = TMEntryKprShowAll.f_s_intColumnIdAlias + TMEntryKprSel._f_s_intNbRowMore;

    public static final int f_s_intColumnIdIsKeyEntry = TMEntryKprShowAll.f_s_intColumnIdIsKeyEntry + TMEntryKprSel._f_s_intNbRowMore;

    public static final int f_s_intColumnIdIsCertEntry = TMEntryKprShowAll.f_s_intColumnIdIsCertEntry + TMEntryKprSel._f_s_intNbRowMore;

    public static final int f_s_intColumnIdIsSelfSignedCert = TMEntryKprShowAll.f_s_intColumnIdIsSelfSignedCert + TMEntryKprSel._f_s_intNbRowMore;

    public static final int f_s_intColumnIdIsTrustedCert = TMEntryKprShowAll.f_s_intColumnIdIsTrustedCert + TMEntryKprSel._f_s_intNbRowMore;

    public static int[] s_intsColW = null;

    private static String[] _s_strsColumnNames = null;

    static {
        TMEntryKprSel.s_intsColW = new int[TMEntryKprShowAll.f_s_intsColW.length + 1];
        TMEntryKprSel.s_intsColW[0] = 30;
        for (int i = 0; i < TMEntryKprShowAll.f_s_intsColW.length; i++) TMEntryKprSel.s_intsColW[i + 1] = TMEntryKprShowAll.f_s_intsColW[i];
        TMEntryKprSel._s_strsColumnNames = new String[TMEntryKprSel.s_intsColW.length];
        for (int i = 0; i < TMEntryKprShowAll.f_s_strsColumnNames.length; i++) TMEntryKprSel._s_strsColumnNames[i + 1] = TMEntryKprShowAll.f_s_strsColumnNames[i];
        String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".TMEntryKprSel";
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.table.TMEntryKprSel";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            TMEntryKprSel._s_strsColumnNames[0] = rbeResources.getString("elligible");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        }
    }

    public int getColumnCount() {
        return TMEntryKprSel._s_strsColumnNames.length;
    }

    public String getColumnName(int col) {
        return TMEntryKprSel._s_strsColumnNames[col];
    }

    public TMEntryKprSel() {
        this(new Object[0][0]);
    }

    public TMEntryKprSel(Object[][] objssData) {
        super(objssData);
    }
}
