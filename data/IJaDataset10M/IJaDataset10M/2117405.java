package src.projects.VariationDatabase.Util;

import java.util.HashMap;
import java.util.Iterator;
import src.lib.CommandLine;
import src.lib.CurrentVersion;
import src.lib.IterableIterator;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.ioInterfaces.Log_Buffer;
import src.projects.VariationDatabase.libs.PSQLroutines;
import src.projects.VariationDatabase.libs.PSQLutils;

/**
 * Delete Library is a utility for purging the database of specific libraries
 * when the need arrises for removing or replacing a subset of data.
 *
 * @author afejes
 * @version $Revision: 3403 $
 */
public class DeleteLibrary {

    private DeleteLibrary() {
    }

    private static String psql_file;

    private static String db_name;

    private static String[] library;

    private static Log_Buffer LB = null;

    private static final String INS_PARAM = "ins";

    private static final String DELS_PARAM = "del";

    private static final String SNPS_PARAM = "snp";

    private static boolean ins = false;

    private static boolean dels = false;

    private static boolean snps = false;

    private static void parse_input(HashMap<String, String> Variables) {
        if (Variables == null) {
            usage();
        }
        assert (Variables != null);
        if (Variables.containsKey("psql")) {
            CommandLine.test_parameter_count(LB, "psql", Variables.get("psql"), 1);
            psql_file = Variables.get("psql");
            LB.notice(" * PSQL conf file    : " + psql_file);
        } else {
            LB.error("Must specify psql connection configuration file with the -psql flag");
            usage();
        }
        if (Variables.containsKey("db_name")) {
            CommandLine.test_parameter_count(LB, "db_name", Variables.get("db_name"), 1);
            db_name = Variables.get("db_name");
            LB.notice(" * PSQL Database name: " + db_name);
        } else {
            LB.error("Must specify psql database name with the -db_name flag");
            usage();
        }
        if (Variables.containsKey("library")) {
            library = Variables.get("library").split(",");
            if (library.length == 0) {
                LB.error("-library flag must be followed by a list of libraries from which snps are investigated");
                LB.die();
            }
        } else {
            LB.error("Must specify library name with the -library flag");
            usage();
        }
        if (Variables.containsKey(INS_PARAM)) {
            CommandLine.test_parameter_count(LB, INS_PARAM, Variables.get(INS_PARAM), 0);
            ins = true;
            LB.notice(" * Deleting Insertions  : On");
        } else {
            ins = false;
            LB.notice(" * Deleting Insertions  : Off");
        }
        if (Variables.containsKey(DELS_PARAM)) {
            CommandLine.test_parameter_count(LB, DELS_PARAM, Variables.get(DELS_PARAM), 0);
            dels = true;
            LB.notice(" * Deleting Deletions   : On");
        } else {
            dels = false;
            LB.notice(" * Deleting Deletions   : Off");
        }
        if (Variables.containsKey(SNPS_PARAM)) {
            CommandLine.test_parameter_count(LB, SNPS_PARAM, Variables.get(SNPS_PARAM), 0);
            snps = true;
            LB.notice(" * Deleting SNPs        : On");
        } else {
            snps = false;
            LB.notice(" * Deleting SNPs        : Off");
        }
        if (!snps && !ins && !dels) {
            LB.notice(" * CLEANING LIBRARIES   :  ON");
        }
        Variables.remove("psql");
        Variables.remove("library");
        Variables.remove(INS_PARAM);
        Variables.remove(DELS_PARAM);
        Variables.remove(SNPS_PARAM);
        Variables.remove("db_name");
        Iterator<String> keys = Variables.keySet().iterator();
        if (keys.hasNext()) {
            LB.error("Could not process the following flags:");
            for (String k : new IterableIterator<String>(keys)) {
                LB.error("  * " + k);
            }
            LB.die();
        }
    }

    private static void usage() {
        LB.notice("This program requires the following parameters:");
        LB.notice("-psql");
        LB.notice("-library");
        LB.notice("-db_name");
        LB.notice("This program has the following optional parameters:");
        LB.notice("-" + SNPS_PARAM);
        LB.notice("-" + INS_PARAM);
        LB.notice("-" + DELS_PARAM);
        LB.die();
    }

    public static void delete_snps(PSQLutils psql, String libName, int libraryID, int alignerID) {
        String D1 = "delete from observations where library_id = " + libraryID + " and aligner_id = " + alignerID;
        LB.notice("Deleting all observations for library " + libName + " (lib id = " + libraryID + ", aligner id = " + alignerID + " )");
        int rows = psql.run_SQL(D1);
        LB.notice(rows + " rows deleted");
    }

    public static void delete_inserts(PSQLutils psql, String libName, int libraryID, int alignerID) {
        String D1 = "delete from obs_ins where library_id = " + libraryID + " and aligner_id = " + alignerID;
        LB.notice("Deleting all observations (insertions) for library " + libName + " (lib id = " + libraryID + ", aligner id = " + alignerID + " )");
        int rows = psql.run_SQL(D1);
        LB.notice(rows + " rows deleted");
    }

    public static void delete_deletions(PSQLutils psql, String libName, int libraryID, int alignerID) {
        String D1 = "delete from obs_del where library_id = " + libraryID + " and aligner_id = " + alignerID;
        LB.notice("Deleting all observations (deletions) for library " + libName + " (lib id = " + libraryID + ", aligner id = " + alignerID + " )");
        int rows = psql.run_SQL(D1);
        LB.notice(rows + " rows deleted");
    }

    public static void delete_library(PSQLutils psql, String libName, int libraryID) {
        String is_obs = "select count(*) from observations where library_id = " + libraryID + " limit 1";
        int count = psql.get_count(is_obs);
        if (count > 0) {
            LB.notice("Library still in use (observations) - no library records deleted");
            return;
        }
        is_obs = "select count(*) from obs_ins where library_id = " + libraryID + " limit 1";
        count = psql.get_count(is_obs);
        if (count > 0) {
            LB.notice("Library still in use (obs_ins) - no library records deleted");
            return;
        }
        is_obs = "select count(*) from obs_del where library_id = " + libraryID + " limit 1";
        count = psql.get_count(is_obs);
        if (count > 0) {
            LB.notice("Library still in use (obs_del) - no library records deleted");
            return;
        }
        String D2 = "delete from library where library_id = " + libraryID;
        LB.notice("Deleting library record for library " + libName);
        int rows = psql.run_SQL(D2);
        LB.notice(rows + " rows deleted");
        return;
    }

    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("Delete Library", "$Revision: 3403 $");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        assert (Variables != null);
        LB.notice(" * Deleting library(s) : " + Variables.containsKey("library"));
        PSQLutils psql = new PSQLutils(LB, psql_file, db_name);
        PSQLroutines psqlr = new PSQLroutines(LB, psql);
        for (String l : library) {
            int lib_id = psqlr.get_library_id_with_query(l);
            int[] aligners = psqlr.test_for_multiple_aligners_string(lib_id);
            int aligner_id = -1;
            if (aligners.length > 1) {
                aligner_id = psqlr.query_aligner_id(aligners);
            } else {
                aligner_id = aligners[0];
            }
            if (snps) {
                delete_snps(psql, l, lib_id, aligner_id);
            }
            if (ins) {
                delete_inserts(psql, l, lib_id, aligner_id);
            }
            if (dels) {
                delete_deletions(psql, l, lib_id, aligner_id);
            }
            delete_library(psql, l, lib_id);
        }
        psqlr.close();
        psql.close();
        LB.close();
    }
}
