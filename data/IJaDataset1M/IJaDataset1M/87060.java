package net.sf;

import java.io.File;
import javax.swing.JOptionPane;

public class BlastParameter {

    public static final String[] MATRICES = new String[] { "BLOSUM100", "BLOSUM30", "BLOSUM35", "BLOSUM40", "BLOSUM45", "BLOSUM50", "BLOSUM55", "BLOSUM60", "BLOSUM62", "BLOSUM65", "BLOSUM70", "BLOSUM75", "BLOSUM80", "BLOSUM85", "BLOSUM90", "BLOSUMN", "DAYHOFF", "EDNAFULL", "GONNET", "IDENTITY", "MATCH", "PAM10", "PAM100", "PAM110", "PAM120", "PAM130", "PAM140", "PAM150", "PAM160", "PAM170", "PAM180", "PAM190", "PAM20", "PAM200", "PAM210", "PAM220", "PAM230", "PAM240", "PAM250", "PAM260", "PAM270", "PAM280", "PAM290", "PAM30", "PAM300", "PAM310", "PAM320", "PAM330", "PAM340", "PAM350", "PAM360", "PAM370", "PAM380", "PAM390", "PAM40", "PAM400", "PAM410", "PAM420", "PAM430", "PAM440", "PAM450", "PAM460", "PAM470", "PAM480", "PAM490", "PAM50", "PAM500", "PAM60", "PAM70", "PAM80", "PAM90" };

    public static float blat_filter;

    public static int blast_filter_score;

    public static double blast_filter_expect;

    public static double blast_filter_expect_nt;

    public static double blast_filter_expect_nr;

    public static double blast_filter_expect_good;

    public static int blast_filter_overlap;

    public static int blast_filter_identity1;

    public static int blast_filter_identity2;

    public static String blast_run_matrix;

    public static String blast_run_thresholds;

    public static String temp_path;

    public static String temp_output_blast;

    public static String temp_output_sequence_query;

    public static String temp_output_sequence_subject;

    public static String path_program_formatdb;

    public static String path_program_blastall;

    public static String path_previous_dir;

    public BlastParameter() {
        super();
        blat_filter = 0.9f;
        blast_filter_score = 100;
        blast_filter_expect = 0.001;
        blast_filter_expect_nt = 1e-10;
        blast_filter_expect_nr = 1e-5;
        blast_filter_expect_good = 1e-50;
        blast_filter_overlap = 90;
        blast_filter_identity1 = 0;
        blast_filter_identity2 = 0;
        blast_run_matrix = MATRICES[8];
        blast_run_thresholds = "1e-10";
        temp_path = "";
        path_program_formatdb = "";
        path_program_blastall = "";
        temp_output_blast = "out.txt";
        temp_output_sequence_query = "query.fas";
        temp_output_sequence_subject = "subject.fas";
    }
}
