package org.gdbi.db.pgvcgi;

import org.gdbi.api.GdbiUtilDebug;

/**
 * Creates a set of read-only fields indicating the CGI features based on
 * the PGV version.
 */
class CgiFeatures {

    private static final GdbiUtilDebug udebug = PgvcgiDatabase.udebug;

    private static final String SET_GEDCOM_COMMAND_OLD = "listgedcoms";

    private static final String SET_GEDCOM_COMMAND_NEW = "version";

    private static final String SET_GEDCOM_VARIABLE_OLD = "GEDCOM";

    private static final String SET_GEDCOM_VARIABLE_NEW = "ged";

    public final String set_gedcom_command;

    public final String set_gedcom_variable;

    public final boolean has_read_only;

    public final boolean has_anon_list;

    public final boolean has_anon_getvar;

    public CgiFeatures(String version) {
        udebug.dprintln("version = " + version);
        final int mmp[] = { 0, 0, 0 };
        if (version != null) {
            version = version.replaceAll(" .*", "");
            String sParts[] = version.split("\\.");
            final int length = Math.min(mmp.length, sParts.length);
            for (int index = 0; index < length; index++) {
                try {
                    mmp[index] = Integer.parseInt(sParts[index]);
                } catch (NumberFormatException e) {
                    break;
                }
            }
        }
        final int bigver = mmp[0] * 10000 + mmp[1] * 100 + mmp[2];
        udebug.dprintln("bigver = " + bigver);
        boolean my_has_anon_getvar = true;
        boolean my_has_anon_list = false;
        boolean my_has_read_only = false;
        String my_set_gedcom_command = SET_GEDCOM_COMMAND_OLD;
        String my_set_gedcom_variable = SET_GEDCOM_VARIABLE_OLD;
        if (bigver >= 30100) {
            my_set_gedcom_command = SET_GEDCOM_COMMAND_NEW;
            my_set_gedcom_variable = SET_GEDCOM_VARIABLE_NEW;
        }
        if (bigver >= 30200) {
            my_has_read_only = true;
            my_has_anon_list = true;
        }
        if ((bigver >= 30303) && (bigver <= 30305)) {
            my_has_anon_getvar = false;
        }
        has_anon_list = my_has_anon_list;
        has_anon_getvar = my_has_anon_getvar;
        has_read_only = my_has_read_only;
        set_gedcom_variable = my_set_gedcom_variable;
        set_gedcom_command = my_set_gedcom_command;
    }
}
