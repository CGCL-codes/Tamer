package src.projects.VariationDatabase.libs;

import java.sql.ResultSet;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.Tuple;

/**
 * 
 * @author afejes
 * @version $Revision$
 */
public class DatabaseStats {

    private static Log_Buffer LB = null;

    private PSQLutils psqlu = null;

    public DatabaseStats(Log_Buffer logbuffer, PSQLutils psqlutils) {
        LB = logbuffer;
        LB.Version("SNPs In Region Library", "$Revision: 3086 $");
        psqlu = psqlutils;
    }

    public void close() {
        psqlu.close();
    }

    /**
	 * Gets the counts for the number for cancer and non cancer libraries in the datbase
	 * @return returns the number of cancer and non cancer libraries.
	 */
    public Tuple<Integer, Integer> get_max_cnc_library() {
        String Q_cancer_libraries = "select count (distinct name) from library where cancer = true";
        ResultSet l_cancer = psqlu.select_statement(Q_cancer_libraries);
        PSQLutils.do_next(l_cancer);
        int cancer_lib_count = PSQLutils.get_field_Integer(l_cancer, 1);
        PSQLutils.close_resultset(l_cancer);
        String Q_normal_libraries = "select count (distinct name) from library where cancer = false";
        ResultSet l_normal = psqlu.select_statement(Q_normal_libraries);
        PSQLutils.do_next(l_normal);
        int normal_lib_count = PSQLutils.get_field_Integer(l_normal, 1);
        PSQLutils.close_resultset(l_normal);
        return new Tuple<Integer, Integer>(cancer_lib_count, normal_lib_count);
    }

    /**
	 * Gets the counts for the number for cancer and non cancer libraries in the datbase
	 * @return returns the number of cancer and non cancer libraries.
	 */
    public Tuple<Integer, Integer> get_max_cnc_samples() {
        String Q_cancer_libraries = "select count (distinct name) from library where cancer = true and sample_id is null";
        ResultSet l_cancer = psqlu.select_statement(Q_cancer_libraries);
        PSQLutils.do_next(l_cancer);
        int cancer_lib_count = PSQLutils.get_field_Integer(l_cancer, 1);
        PSQLutils.close_resultset(l_cancer);
        String Q_normal_libraries = "select count (distinct name) from library where cancer = false and sample_id is null";
        ResultSet l_normal = psqlu.select_statement(Q_normal_libraries);
        PSQLutils.do_next(l_normal);
        int normal_lib_count = PSQLutils.get_field_Integer(l_normal, 1);
        PSQLutils.close_resultset(l_normal);
        Q_cancer_libraries = "select count (distinct s.original_source_name) from library as l, " + "sample as s where l.sample_id = s.sample_id and l.cancer = true;";
        ResultSet l_cancer_2 = psqlu.select_statement(Q_cancer_libraries);
        PSQLutils.do_next(l_cancer_2);
        cancer_lib_count += PSQLutils.get_field_Integer(l_cancer_2, 1);
        PSQLutils.close_resultset(l_cancer_2);
        Q_normal_libraries = "select count (distinct s.original_source_name) from library as l, sample as s " + "where l.sample_id = s.sample_id and l.cancer = false";
        ResultSet l_normal_2 = psqlu.select_statement(Q_normal_libraries);
        PSQLutils.do_next(l_normal_2);
        normal_lib_count += PSQLutils.get_field_Integer(l_normal_2, 1);
        PSQLutils.close_resultset(l_normal_2);
        return new Tuple<Integer, Integer>(cancer_lib_count, normal_lib_count);
    }

    /**
	 * Estimate of the size of the snp table
	 * @return
	 */
    public String snp_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'snp'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the observations table
	 * @return
	 */
    public String observation_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'observations'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the observations table
	 * @return
	 */
    public String annotation_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'annotation'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the insertions table
	 * @return
	 */
    public String ins_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'insertions'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the insertion observations table
	 * @return
	 */
    public String ins_obs_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'obs_ins'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the annotation observations table
	 * @return
	 */
    public String ins_ann_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'annotation_insertions'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the insertions table
	 * @return
	 */
    public String del_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'deletions'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the insertion observations table
	 * @return
	 */
    public String del_obs_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'obs_del'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }

    /**
	 * Estimate of the size of the annotation observations table
	 * @return
	 */
    public String del_ann_table_size() {
        String q = "SELECT reltuples FROM pg_class r WHERE relkind = 'r' AND relname = 'annotation_deletions'";
        ResultSet r = psqlu.select_statement(q);
        PSQLutils.do_next(r);
        String n = PSQLutils.get_field_String(r, 1);
        PSQLutils.close_resultset(r);
        return n;
    }
}
