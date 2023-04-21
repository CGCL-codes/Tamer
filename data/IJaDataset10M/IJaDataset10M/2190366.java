package com.neurogrid.prime;

import junit.framework.*;
import com.neurogrid.om.*;
import com.neurogrid.database.*;
import com.neurogrid.tristero.*;
import com.neurogrid.middle.NeuroGridTestSuite;
import java.util.*;
import org.apache.torque.Torque;
import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.torque.util.SqlEnum;
import org.apache.torque.util.*;
import org.apache.torque.util.Criteria;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * NeuroGridSearch Test class <br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/March/2001   sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */
public class NeuroGridSearchCommandTest extends TestCase {

    private static final String cvsInfo = "$Id: NeuroGridSearchCommandTest.java,v 1.1 2003/02/12 07:07:09 samjoseph Exp $";

    public static String getCvsInfo() {
        return cvsInfo;
    }

    private static Category o_cat = Category.getInstance(NeuroGridSearchCommandTest.class.getName());

    /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
    public static void init(String p_conf) {
        BasicConfigurator.configure();
        PropertyConfigurator.configure(p_conf);
        o_cat.info("NeuroGridSearchCommandTest logging Initialized");
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new NeuroGridSearchCommandTest("testNeuroGridSearch"));
        return suite;
    }

    /**
   * Subclasses must invoke this from their constructor.
   */
    public NeuroGridSearchCommandTest(String p_name) {
        super(p_name);
    }

    public static final EventType[] x_event_types = new EventType[2];

    static {
        x_event_types[0] = NeuroGridSearch.o_click_through_event;
        x_event_types[1] = NeuroGridSearch.o_suggestion_event;
    }

    public static final NeuroGridFlexibleComparator o_ng_comparator = new NeuroGridFlexibleComparator(x_event_types) {

        public double calculate(NeuroGridRating p_rating, double p_no_ratios) {
            Hashtable x_numerator_table = p_rating.getNumeratorTable();
            Hashtable x_denominator_table = p_rating.getDenominatorTable();
            Enumeration x_enum = x_denominator_table.keys();
            Long x_uri_triple_id = null;
            int x_denom = 0;
            int x_numer = 0;
            Integer x_numerator = null;
            double x_sum = 0.0D;
            if (x_denominator_table == null || x_numerator_table == null) return x_sum;
            while (x_enum.hasMoreElements()) {
                x_uri_triple_id = (Long) (x_enum.nextElement());
                x_denom = ((Integer) (x_denominator_table.get(x_uri_triple_id))).intValue();
                if (x_denom == 0) x_sum += 0.0D; else {
                    x_numerator = (Integer) (x_numerator_table.get(x_uri_triple_id));
                    if (x_numerator != null) {
                        x_numer = x_numerator.intValue();
                        x_sum += (double) (x_numer) / (double) (x_denom);
                    }
                }
            }
            return x_sum / (double) (p_no_ratios);
        }
    };

    public static final EventType[][] oo_event_types = new EventType[2][2];

    private static void initEvents() {
        oo_event_types[0] = new EventType[2];
        oo_event_types[0][0] = NeuroGridSearch.o_suggestion_event;
        oo_event_types[0][1] = NeuroGridSearch.o_added_event;
        oo_event_types[1] = new EventType[2];
        oo_event_types[1][0] = NeuroGridSearch.o_click_through_event;
        oo_event_types[1][1] = NeuroGridSearch.o_added_event;
    }

    /**
   * Test the NeuroGrid search process
   */
    public void testNeuroGridSearch() {
        try {
            char c;
            NeuroGridTestSuite.init();
            initEvents();
            Criteria.Criterion x_token1 = null;
            Criteria.Criterion x_token2 = null;
            String x_subject = "keitai";
            String x_predicate = "";
            String x_object = "";
            long x_time = System.currentTimeMillis();
            System.out.println("Started timing ...");
            String x_login_name = "sam";
            NgUser x_user = NgUser.getNgUserObject(x_login_name);
            String x_search_user = null;
            NgUser x_search_user_object = null;
            NgUser x_desc_user_object = x_user;
            x_token1 = TristeroSearch.search(x_subject, SqlEnum.EQUAL, x_predicate, SqlEnum.EQUAL, x_object, SqlEnum.EQUAL, null);
            x_token2 = TristeroSearch.search("", SqlEnum.EQUAL, "IS", SqlEnum.EQUAL, "NODE", SqlEnum.EQUAL, null);
            long x_new_time = System.currentTimeMillis();
            Criteria.Criterion x_token_union = null;
            x_token_union = x_token2;
            SearchResult x_sr = NeuroGridSearch.fetch(x_user, x_token_union, new SearchCriteria(0, 100), x_search_user_object, x_login_name.equals(x_search_user), null, null, oo_event_types, o_ng_comparator, true, true);
            List x_results = null;
            List x_qf_results = null;
            Criteria.Criterion x_token3 = null;
            NeuroGridRating x_rating = null;
            List x_valid_nodes = new Vector();
            UriTriple x_temp_uri_triple = null;
            UriDesc x_uri_desc = null;
            if (x_sr != null) {
                x_results = x_sr.getResults();
                for (int i = 0; i < x_results.size(); i++) {
                    x_rating = (NeuroGridRating) (x_results.get(i));
                    x_rating.setComparator(o_ng_comparator);
                    x_token3 = TristeroSearch.search(x_rating.getUri().getUri(), SqlEnum.EQUAL, "NG_QUERY_FORMAT", SqlEnum.EQUAL, "", SqlEnum.EQUAL, null);
                    x_qf_results = TristeroSearch.fetch(x_token3, new SearchCriteria(0, 1));
                    if (x_qf_results == null || x_qf_results.size() < 1) continue;
                    x_valid_nodes.add(x_rating);
                    x_temp_uri_triple = (UriTriple) (x_qf_results.get(0));
                    x_rating.setQueryFormat(QueryFormat.checkQueryFormat(x_temp_uri_triple.getKeyword().getKeyword()));
                    if (x_desc_user_object != null) {
                        try {
                            if (x_rating.getTitle() == null && x_rating.getDescription() == null) {
                                x_rating.setUriDesc(UriDesc.getUriDescObject(x_rating.getUri(), x_desc_user_object));
                            }
                        } catch (Exception e) {
                            x_uri_desc = UriDesc.registerNewUriDesc(x_rating.getUri(), x_desc_user_object, "Not Specified", "Not Specified", false);
                            x_rating.setUriDesc(x_uri_desc);
                        }
                    }
                }
            }
            int x_count = x_valid_nodes.size();
            o_cat.debug("total_node_results: " + x_count);
        } catch (Exception x_e) {
            x_e.printStackTrace();
            fail("some error ...");
        }
    }

    public static StringBuffer o_buf = new StringBuffer(256);

    public static void outputStats(String p_message) {
        outputStats(p_message, System.currentTimeMillis(), o_time);
    }

    public static void outputStats(String p_message, long p_new_time, long p_time) {
        o_buf.delete(0, o_buf.length());
        o_buf.append(p_message).append((p_new_time - p_time));
        o_buf.append(", Free: ").append(Runtime.getRuntime().freeMemory());
        o_buf.append(", Max: ").append(Runtime.getRuntime().maxMemory());
        o_buf.append(", Total: ").append(Runtime.getRuntime().totalMemory());
        long x_current_used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        o_buf.append(", Used: ").append(x_current_used);
        o_buf.append(", Change: ").append((x_current_used - o_previous_used - 1760L));
        System.out.println(o_buf.toString());
        o_previous_used = x_current_used;
    }

    public static long o_previous_used = 0L;

    public static long o_time = System.currentTimeMillis();
}
