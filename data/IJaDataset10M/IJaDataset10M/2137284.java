package com.um2.simplexe.client.plugin.console;

/** Used for checking the Karush-Kuhn-Tucker conditions.
 * @see GlpkSolver#checkKkt(int scaled, GlpkSolverKktConditions kkt)
 */
public class GlpkSolverKktConditions {

    double pe_ae_max;

    int pe_ae_row;

    double pe_re_max;

    int pe_re_row;

    int pe_quality;

    double pb_ae_max;

    int pb_ae_ind;

    double pb_re_max;

    int pb_re_ind;

    int pb_quality;

    double de_ae_max;

    int de_ae_col;

    double de_re_max;

    int de_re_col;

    int de_quality;

    double db_ae_max;

    int db_ae_ind;

    double db_re_max;

    int db_re_ind;

    int db_quality;

    double cs_ae_max;

    int cs_ae_ind;

    double cs_re_max;

    int cs_re_ind;

    int cs_quality;
}
