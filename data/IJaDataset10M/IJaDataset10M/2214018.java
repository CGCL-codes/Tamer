package jp.ac.jaist.ceqea.io;

import javax.swing.*;
import java.awt.*;
import jp.ac.jaist.ceqea.CEq_enums.*;

public class CEq_feedback {

    public static boolean showError = true;

    private static boolean showWarnings = true;

    public static void debug(String info) {
        System.err.println(info + " @ " + (new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new java.util.Date()));
    }

    public enum ERR {

        CEqUI_editor_error, CEqUI_environment_error, CEqUI_import_error, Unrecognised_symbol, File_or_generic_error, Feature_not_implemented, Combinatorial_explosion, File_does_not_exist, Unreadable_file, Internal_error_in_MIG_object, Syntax_error, Internal_syntax_error, Internal_error_not_desugared_tree, Internal_error_nonBinary_inhibition, Internal_error_reindexing_node, Internal_error_systems_formation, Undeclared_name, Missing_declaration, Repeat_seed, Repeat_sustainer, Incorrect_parity, Illegal_name, Repeat_declaration, Illegal_range_termination, Inheritance_error, Inheritance_shortcoming, Modality_range_mismatch, Index_out_of_bounds, Thread_execution_exception
    }

    public static void exit(ERR err, Exception e) {
        e.printStackTrace(System.err);
        exit(err, "", "");
    }

    public static void exit(ERR err, String info) {
        exit(err, "", info);
    }

    public static void exit(ERR err, int lineno, String info) {
        exit(err, ", l." + lineno, info);
    }

    public static void exit(ERR err, int lineno, int posno, String info) {
        exit(err, ", l." + lineno + ":" + posno, info);
    }

    public static void exit(ERR err, String comment, String info) {
        if (showError) {
            final String msg = err.toString() + comment + ": " + ((info.isEmpty()) ? "<no additional info>" : info);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        JOptionPane.showMessageDialog(null, msg + "\nNB! Any results produced in this run are compromised.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } catch (HeadlessException e) {
                        System.err.println("ERROR: " + msg);
                    }
                }
            });
        }
        Thread.currentThread().stop();
    }

    public static void pp(String str) {
        System.out.println(str);
    }

    public static void repeatMdlt(String attr, int line, int pos) {
        CEq_feedback.exit(CEq_feedback.ERR.Repeat_declaration, line, pos, "attribute " + attr);
    }

    public static void warning(final String info) {
        if (showWarnings) SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    JOptionPane.showMessageDialog(null, info, "WARNING", JOptionPane.WARNING_MESSAGE);
                } catch (HeadlessException e) {
                    System.err.println("WARNING: " + info);
                }
            }
        });
    }
}
