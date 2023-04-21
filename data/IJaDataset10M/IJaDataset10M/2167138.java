package src.projects.VariationDatabase.libs;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.IndelCoordinates;
import src.lib.objects.Tuple;

/**
 * Library for performing Concordance analysis.
 * @author afejes
 * @version $Revision: 3546 $
 */
public class Concordance {

    private static Log_Buffer LB;

    private Concordance() {
    }

    private static final int WINDOW_SEARCH_CONST = 10000;

    public static void setLB(Log_Buffer logBuffer) {
        LB = logBuffer;
    }

    public static Tuple<Integer, Integer> getConcordanceSNPs(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver) {
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        String Q1 = "select count(*) from observations as a, annotation as b " + "where a.snp_id = b.snp_id " + "and a.library_id = " + library_id + " " + "and aligner_id = " + aligner_id + " " + "and xlat_id = " + xlat_id;
        if (min_obs > 1) {
            Q1 = Q1.concat(" and a.variation_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            Q1 = Q1.concat(" and a.variation_obs/cast(a.coverage as float) >= " + min_percent);
        }
        int in_dbsnp = psql.get_count(Q1);
        String Q2 = "select count(*) from observations as a " + "left join (select snp_id from annotation where xlat_id = " + xlat_id + ") as b " + "on (a.snp_id = b.snp_id) " + "where a.library_id = " + library_id + " " + "and aligner_id = " + aligner_id + " " + "and b.snp_id is null";
        if (min_obs > 1) {
            Q2 = Q2.concat(" and a.variation_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            Q2 = Q2.concat(" and a.variation_obs/cast(a.coverage as float) >= " + min_percent);
        }
        int not_in_dbsnp = psql.get_count(Q2);
        Tuple<Integer, Integer> r = new Tuple<Integer, Integer>(in_dbsnp, not_in_dbsnp);
        return r;
    }

    public static Tuple<Integer, Integer> getConcordanceInserts(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver) {
        return getConcordanceInserts(psql, library_id, aligner_id, min_obs, min_percent, dbsnp_ver, 0);
    }

    private static Tuple<Integer, Integer> binarySearch(String key, ArrayList<IndelCoordinates> annotIndels, String keyName, Tuple<Integer, Integer> region) {
        int top = region.get_first();
        int bot = region.get_second();
        String CHROMOSOME = "chromosome";
        if (top < 0 || bot < 0) {
            return region;
        }
        boolean found = false;
        int mid = 0;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals(CHROMOSOME)) {
                if (annotIndels.get(mid).getChromosome().equals(key)) {
                    found = true;
                    break;
                } else if (annotIndels.get(mid).getChromosome().compareTo(key) > 0) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else {
                LB.debug(keyName + ".");
                LB.error(" 1. invalid keyName " + keyName);
                LB.die();
            }
        }
        if (!found) {
            return new Tuple<Integer, Integer>(-1, -1);
        }
        int saveMid = mid;
        int saveBot = bot;
        bot = saveMid;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals(CHROMOSOME)) {
                if (annotIndels.get(mid).getChromosome().equals(key)) {
                    bot = mid - 1;
                } else if (annotIndels.get(mid).getChromosome().compareTo(key) > 0) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else {
                LB.error(" 2. invalid keyName " + keyName);
                LB.die();
            }
        }
        int startRegion = -1;
        if (keyName.equals(CHROMOSOME)) {
            if (annotIndels.get(mid).getChromosome().equals(key)) {
                startRegion = mid;
            } else {
                startRegion = mid + 1;
            }
        } else {
            LB.error(" 4. invalid keyName " + keyName);
            LB.die();
        }
        bot = saveBot;
        top = saveMid;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals(CHROMOSOME)) {
                if (annotIndels.get(mid).getChromosome().equals(key)) {
                    top = mid + 1;
                } else if (annotIndels.get(mid).getChromosome().compareTo(key) > 0) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else {
                LB.error(" 3. invalid keyName " + keyName);
                LB.die();
            }
        }
        int endRegion = -1;
        if (keyName.equals(CHROMOSOME)) {
            if (annotIndels.get(mid).getChromosome().equals(key)) {
                endRegion = mid;
            } else {
                endRegion = mid - 1;
            }
        } else {
            LB.error(" 4. invalid keyName " + keyName);
            LB.die();
        }
        return new Tuple<Integer, Integer>(startRegion, endRegion);
    }

    @SuppressWarnings("unused")
    private static Tuple<Integer, Integer> binarySearch(int key, ArrayList<IndelCoordinates> annotIndels, String keyName, Tuple<Integer, Integer> region) {
        int top = region.get_first();
        int bot = region.get_second();
        if (top < 0 || bot < 0) {
            return region;
        }
        boolean found = false;
        int mid = 0;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals("start")) {
                if (annotIndels.get(mid).getStart() == key) {
                    found = true;
                    break;
                } else if (annotIndels.get(mid).getStart() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else if (keyName.equals("end")) {
                if (annotIndels.get(mid).getEnd() == key) {
                    found = true;
                    break;
                } else if (annotIndels.get(mid).getEnd() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else {
                LB.error(" invalid keyName ");
                LB.die();
            }
        }
        if (!found) {
            return new Tuple<Integer, Integer>(-1, -1);
        }
        int saveMid = mid;
        int saveBot = bot;
        bot = saveMid;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals("start")) {
                if (annotIndels.get(mid).getStart() == key) {
                    bot = mid - 1;
                } else if (annotIndels.get(mid).getStart() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else if (keyName.equals("end")) {
                if (annotIndels.get(mid).getEnd() == key) {
                    bot = mid - 1;
                } else if (annotIndels.get(mid).getEnd() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            }
        }
        int startRegion = -1;
        if (keyName.equals("start")) {
            if (annotIndels.get(mid).getStart() >= key) {
                startRegion = mid;
            } else {
                startRegion = mid + 1;
            }
        } else if (keyName.equals("end")) {
            if (annotIndels.get(mid).getEnd() >= key) {
                startRegion = mid;
            } else {
                startRegion = mid + 1;
            }
        }
        bot = saveBot;
        top = saveMid;
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals("start")) {
                if (annotIndels.get(mid).getStart() == key) {
                    top = mid + 1;
                } else if (annotIndels.get(mid).getStart() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else if (keyName.equals("end")) {
                if (annotIndels.get(mid).getEnd() == key) {
                    top = mid + 1;
                } else if (annotIndels.get(mid).getEnd() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            }
        }
        int endRegion = -1;
        if (keyName.equals("start")) {
            if (annotIndels.get(mid).getStart() <= key) {
                endRegion = mid;
            } else {
                endRegion = mid - 1;
            }
        } else if (keyName.equals("end")) {
            if (annotIndels.get(mid).getEnd() <= key) {
                endRegion = mid;
            } else {
                endRegion = mid - 1;
            }
        }
        return new Tuple<Integer, Integer>(startRegion, endRegion);
    }

    /**
	 * 
	 * @param keyValues
	 * @param annotIndels
	 * @param keyName
	 * @param searchRange
	 * @return
	 */
    private static Tuple<Integer, Integer> binarySearch2D(Tuple<Integer, Integer> keyValues, ArrayList<IndelCoordinates> annotIndels, String keyName, Tuple<Integer, Integer> searchRange) {
        int top = searchRange.get_first();
        int bot = searchRange.get_second();
        if (top < 0 || bot < 0) {
            return searchRange;
        }
        int mid = 0;
        int key = keyValues.get_first();
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals("start")) {
                if (annotIndels.get(mid).getStart() == key) {
                    bot = mid - 1;
                } else if (annotIndels.get(mid).getStart() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else if (keyName.equals("end")) {
                if (annotIndels.get(mid).getEnd() == key) {
                    bot = mid - 1;
                } else if (annotIndels.get(mid).getEnd() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else {
                LB.error(" invalid keyName ");
                LB.die();
            }
        }
        int startRegion = -1;
        if (keyName.equals("start")) {
            if (annotIndels.get(mid).getStart() >= key) {
                startRegion = mid;
            } else {
                startRegion = mid + 1;
            }
        } else if (keyName.equals("end")) {
            if (annotIndels.get(mid).getEnd() >= key) {
                startRegion = mid;
            } else {
                startRegion = mid + 1;
            }
        }
        top = searchRange.get_first();
        bot = searchRange.get_second();
        key = keyValues.get_second();
        while (top <= bot) {
            mid = (top + bot) / 2;
            if (keyName.equals("start")) {
                if (annotIndels.get(mid).getStart() == key) {
                    top = mid + 1;
                } else if (annotIndels.get(mid).getStart() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            } else if (keyName.equals("end")) {
                if (annotIndels.get(mid).getEnd() == key) {
                    top = mid + 1;
                } else if (annotIndels.get(mid).getEnd() > key) {
                    bot = mid - 1;
                } else {
                    top = mid + 1;
                }
            }
        }
        int endRegion = -1;
        if (keyName.equals("start")) {
            if (annotIndels.get(mid).getStart() <= key) {
                endRegion = mid;
            } else {
                endRegion = mid - 1;
            }
        } else if (keyName.equals("end")) {
            if (annotIndels.get(mid).getEnd() <= key) {
                endRegion = mid;
            } else {
                endRegion = mid - 1;
            }
        }
        return new Tuple<Integer, Integer>(startRegion, endRegion);
    }

    public static boolean indelExists(IndelCoordinates indel, ArrayList<IndelCoordinates> annotIndels, int window) {
        Tuple<Integer, Integer> searchRegion = new Tuple<Integer, Integer>(0, annotIndels.size() - 1);
        Tuple<Integer, Integer> r2 = binarySearch(indel.getChromosome(), annotIndels, "chromosome", searchRegion);
        Tuple<Integer, Integer> r3 = binarySearch2D(new Tuple<Integer, Integer>(indel.getEnd() - WINDOW_SEARCH_CONST - window, indel.getEnd() + window), annotIndels, "start", r2);
        return (linearSearch(new Tuple<Integer, Integer>(indel.getStart() - window, java.lang.Integer.MAX_VALUE), annotIndels, "end", r3));
    }

    /**
	 * returns the notes of the indels in annotIndel that overlap the input indel
	 * @param indel
	 * @param annotIndels
	 * @param window
	 * @return
	 */
    public static ArrayList<String> existingIndels(IndelCoordinates indel, ArrayList<IndelCoordinates> annotIndels, int window) {
        Tuple<Integer, Integer> searchRegion = new Tuple<Integer, Integer>(0, annotIndels.size() - 1);
        Tuple<Integer, Integer> r2 = binarySearch(indel.getChromosome(), annotIndels, "chromosome", searchRegion);
        Tuple<Integer, Integer> r3 = binarySearch2D(new Tuple<Integer, Integer>(indel.getEnd() - WINDOW_SEARCH_CONST - window, indel.getEnd() + window), annotIndels, "start", r2);
        return (linearSearchGetNotes(new Tuple<Integer, Integer>(indel.getStart() - window, java.lang.Integer.MAX_VALUE), annotIndels, "end", r3));
    }

    /**
	 * returns the notes of annotation indels that are within the range
	 * @param key
	 * @param annotIndels
	 * @param keyName
	 * @param searchRange
	 * @return
	 */
    private static ArrayList<String> linearSearchGetNotes(Tuple<Integer, Integer> key, ArrayList<IndelCoordinates> annotIndels, String keyName, Tuple<Integer, Integer> searchRange) {
        ArrayList<String> res = new ArrayList<String>();
        if (searchRange.get_first() < 0 || searchRange.get_second() < 0) {
            return res;
        }
        int lower = key.get_first();
        int upper = key.get_second();
        for (int i = searchRange.get_first(); i <= searchRange.get_second(); i++) {
            if (keyName.equals("start")) {
                int k = annotIndels.get(i).getStart();
                if (k <= upper && k >= lower) {
                    res.add(annotIndels.get(i).getNote());
                }
            }
            if (keyName.equals("end")) {
                int k = annotIndels.get(i).getEnd();
                if (k <= upper && k >= lower) {
                    res.add(annotIndels.get(i).getNote());
                }
            }
        }
        return res;
    }

    private static boolean linearSearch(Tuple<Integer, Integer> key, ArrayList<IndelCoordinates> annotIndels, String keyName, Tuple<Integer, Integer> searchRange) {
        if (searchRange.get_first() < 0 || searchRange.get_second() < 0) {
            return false;
        }
        int lower = key.get_first();
        int upper = key.get_second();
        for (int i = searchRange.get_first(); i <= searchRange.get_second(); i++) {
            if (keyName.equals("start")) {
                int k = annotIndels.get(i).getStart();
                if (k <= upper && k >= lower) {
                    return true;
                }
            }
            if (keyName.equals("end")) {
                int k = annotIndels.get(i).getEnd();
                if (k <= upper && k >= lower) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Tuple<Integer, Integer> getConcordanceDeletions(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver) {
        return getConcordanceDeletions(psql, library_id, aligner_id, min_obs, min_percent, dbsnp_ver, 0);
    }

    public static Tuple<Integer, Integer> getConcordanceDeletions(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver, int window) {
        return getConcordanceDeletions(psql, library_id, aligner_id, min_obs, min_percent, dbsnp_ver, window, 1);
    }

    public static Tuple<Integer, Integer> getConcordanceDeletions(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver, int window, int minQuality) {
        Tuple<Integer, Integer> r = null;
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        String Q1 = "SELECT d.del_id, d.chromosome, d.region_start, d.region_end " + "FROM obs_del as o , deletions as d where o.library_id = " + library_id + " AND o.aligner_id = " + aligner_id + " AND o.del_id = d.del_id";
        if (min_obs > 1) {
            Q1 = Q1.concat(" and o.indel_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            Q1 = Q1.concat(" and o.indel_obs/(cast(o.ref_obs as float) + cast(o.indel_obs as float)) >= " + min_percent);
        }
        if (minQuality != Integer.MIN_VALUE) {
            Q1 = Q1.concat(" and o.quality >= " + minQuality);
        }
        ArrayList<IndelCoordinates> obsIndels = new ArrayList<IndelCoordinates>();
        ResultSet rsq1 = psql.select_statement(Q1);
        while (PSQLutils.do_next(rsq1)) {
            int id = PSQLutils.get_field_Integer(rsq1, "del_id");
            String chr = PSQLutils.get_field_String(rsq1, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq1, "region_start");
            int end = PSQLutils.get_field_Integer(rsq1, "region_end");
            obsIndels.add(new IndelCoordinates(id, IndelCoordinates.DELETION, chr, start, end));
        }
        if (obsIndels.size() == 0) {
            LB.error("No indels in the specified library");
            LB.die();
        }
        String Q2 = "SELECT i.del_id, i.chromosome, i.region_start, i.region_end " + "FROM annotation_deletions as a , deletions as i where a.xlat_id = " + xlat_id + " AND a.del_id = i.del_id";
        ResultSet rsq2 = psql.select_statement(Q2);
        ArrayList<IndelCoordinates> annotIndels = new ArrayList<IndelCoordinates>();
        while (PSQLutils.do_next(rsq2)) {
            int id = PSQLutils.get_field_Integer(rsq2, "del_id");
            String chr = PSQLutils.get_field_String(rsq2, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq2, "region_start");
            int end = PSQLutils.get_field_Integer(rsq2, "region_end");
            annotIndels.add(new IndelCoordinates(id, IndelCoordinates.DELETION, chr, start, end));
        }
        if (annotIndels.size() == 0) {
            LB.error("No indels in the specified dbsnp version");
            LB.die();
        }
        Collections.sort(annotIndels);
        int inDbsnp = 0;
        int notInDbsnp = 0;
        ArrayList<Integer> obsId = new ArrayList<Integer>();
        for (IndelCoordinates indel : obsIndels) {
            if (indelExists(indel, annotIndels, window)) {
                obsId.add(indel.getId());
                inDbsnp += 1;
            } else {
                notInDbsnp += 1;
            }
        }
        r = new Tuple<Integer, Integer>(inDbsnp, notInDbsnp);
        return r;
    }

    public static Tuple<Integer, Integer> getConcordanceInsertsSql(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver, int window) {
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        Tuple<Integer, Integer> r = null;
        String query0 = "SELECT COUNT(*)" + "FROM obs_ins as o , insertions as d where o.library_id = " + library_id + " AND o.aligner_id = " + aligner_id + " AND o.ins_id = d.ins_id";
        if (min_obs > 1) {
            query0 = query0.concat(" and o.indel_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            query0 = query0.concat(" and o.indel_obs/(cast(o.ref_obs as float) + cast(o.indel_obs as float)) >= " + min_percent);
        }
        ResultSet rsq01 = psql.select_statement(query0);
        PSQLutils.do_next(rsq01);
        int total = PSQLutils.get_field_Integer(rsq01, 1);
        LB.debug("total observation in the library " + total);
        PSQLutils.close_resultset(rsq01);
        String Q1 = "SELECT COUNT (*) FROM (SELECT distinct b.ins_id from (";
        Q1 = Q1.concat("SELECT d.chromosome, d.region_start, d.region_end, o.ins_id as ins_id " + "FROM obs_ins as o , insertions as d where o.library_id = " + library_id + " AND o.aligner_id = " + aligner_id + " AND o.ins_id = d.ins_id");
        if (min_obs > 1) {
            Q1 = Q1.concat(" and o.indel_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            Q1 = Q1.concat(" and o.indel_obs/(cast(o.ref_obs as float) + cast(o.indel_obs as float)) >= " + min_percent);
        }
        Q1 = Q1.concat(" ) as b, ");
        Q1 = Q1.concat("(SELECT d.chromosome, d.region_start, d.region_end FROM insertions AS d, " + "annotation_insertions AS a WHERE a.xlat_id = " + xlat_id + " AND a.ins_id = d.ins_id) as c");
        Q1 = Q1.concat(" where c.chromosome = b.chromosome and c.region_end >= b.region_start - " + window + " and c.region_start <= b.region_end + " + window + ") as e");
        ResultSet rsq1 = psql.select_statement(Q1);
        PSQLutils.do_next(rsq1);
        int inDbsnp = PSQLutils.get_field_Integer(rsq1, 1);
        int notInDbsnp = total - inDbsnp;
        PSQLutils.close_resultset(rsq1);
        r = new Tuple<Integer, Integer>(inDbsnp, notInDbsnp);
        return r;
    }

    public static Tuple<Integer, Integer> getConcordanceInserts(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver, int window) {
        return getConcordanceInserts(psql, library_id, aligner_id, min_obs, min_percent, dbsnp_ver, window, 1);
    }

    public static Tuple<Integer, Integer> getConcordanceInserts(PSQLutils psql, int library_id, int aligner_id, int min_obs, float min_percent, int dbsnp_ver, int window, int minQuality) {
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        Tuple<Integer, Integer> r = null;
        String Q1 = "SELECT i.ins_id, i.chromosome, i.region_start, i.region_end " + "FROM obs_ins as o , insertions as i where o.library_id = " + library_id + " AND o.aligner_id = " + aligner_id + " AND o.ins_id = i.ins_id";
        if (min_obs > 1) {
            Q1 = Q1.concat(" and o.indel_obs >= " + min_obs);
        }
        if (min_percent > 0) {
            Q1 = Q1.concat(" and o.indel_obs/(cast(o.ref_obs as float) + cast(o.indel_obs as float)) >= " + min_percent);
        }
        if (minQuality != Integer.MIN_VALUE) {
            Q1 = Q1.concat(" and o.quality >= " + minQuality);
        }
        ArrayList<IndelCoordinates> obsIndels = new ArrayList<IndelCoordinates>();
        ResultSet rsq1 = psql.select_statement(Q1);
        while (PSQLutils.do_next(rsq1)) {
            int id = PSQLutils.get_field_Integer(rsq1, "ins_id");
            String chr = PSQLutils.get_field_String(rsq1, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq1, "region_start");
            int end = PSQLutils.get_field_Integer(rsq1, "region_end");
            obsIndels.add(new IndelCoordinates(id, IndelCoordinates.INSERTION, chr, start, end));
        }
        if (obsIndels.size() == 0) {
            LB.error("No indels in the specified library");
            LB.die();
        }
        String Q2 = "SELECT i.ins_id, i.chromosome, i.region_start, i.region_end " + "FROM annotation_insertions as a , insertions as i where a.xlat_id = " + xlat_id + " AND a.ins_id = i.ins_id";
        ResultSet rsq2 = psql.select_statement(Q2);
        ArrayList<IndelCoordinates> annotIndels = new ArrayList<IndelCoordinates>();
        while (PSQLutils.do_next(rsq2)) {
            int id = PSQLutils.get_field_Integer(rsq2, "ins_id");
            String chr = PSQLutils.get_field_String(rsq2, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq2, "region_start");
            int end = PSQLutils.get_field_Integer(rsq2, "region_end");
            annotIndels.add(new IndelCoordinates(id, IndelCoordinates.INSERTION, chr, start, end));
        }
        if (annotIndels.size() == 0) {
            LB.error("No indels in the specified dbsnp version");
            LB.die();
        }
        Collections.sort(annotIndels);
        int inDbsnp = 0;
        int notInDbsnp = 0;
        ArrayList<Integer> obsId = new ArrayList<Integer>();
        for (IndelCoordinates indel : obsIndels) {
            if (indelExists(indel, annotIndels, window)) {
                obsId.add(indel.getId());
                inDbsnp += 1;
            } else {
                notInDbsnp += 1;
            }
        }
        r = new Tuple<Integer, Integer>(inDbsnp, notInDbsnp);
        return r;
    }

    public static Tuple<Integer, Integer> getConcordanceInserts(PSQLutils psql, ArrayList<IndelCoordinates> obsIndels, int dbsnp_ver, int window) {
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        Tuple<Integer, Integer> r = null;
        if (obsIndels.size() == 0) {
            LB.error("No indels in the specified library");
            LB.die();
        }
        String Q2 = "SELECT i.ins_id, i.chromosome, i.region_start, i.region_end " + "FROM annotation_insertions as a , insertions as i where a.xlat_id = " + xlat_id + " AND a.ins_id = i.ins_id";
        ResultSet rsq2 = psql.select_statement(Q2);
        ArrayList<IndelCoordinates> annotIndels = new ArrayList<IndelCoordinates>();
        while (PSQLutils.do_next(rsq2)) {
            int id = PSQLutils.get_field_Integer(rsq2, "ins_id");
            String chr = PSQLutils.get_field_String(rsq2, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq2, "region_start");
            int end = PSQLutils.get_field_Integer(rsq2, "region_end");
            annotIndels.add(new IndelCoordinates(id, IndelCoordinates.INSERTION, chr, start, end));
        }
        if (annotIndels.size() == 0) {
            LB.error("No indels in the specified dbsnp version");
            LB.die();
        }
        Collections.sort(annotIndels);
        int inDbsnp = 0;
        int notInDbsnp = 0;
        ArrayList<Integer> obsId = new ArrayList<Integer>();
        for (IndelCoordinates indel : obsIndels) {
            if (indelExists(indel, annotIndels, window)) {
                obsId.add(indel.getId());
                inDbsnp += 1;
            } else {
                notInDbsnp += 1;
            }
        }
        r = new Tuple<Integer, Integer>(inDbsnp, notInDbsnp);
        return r;
    }

    public static Tuple<Integer, Integer> getConcordanceDeletions(PSQLutils psql, ArrayList<IndelCoordinates> obsIndels, int dbsnp_ver, int window) {
        Tuple<Integer, Integer> r = null;
        String q0 = "select xlat_id from annotation_xlat where field_name = 'dbsnp" + dbsnp_ver + "'";
        ResultSet rsq0 = psql.select_statement(q0);
        PSQLutils.do_next(rsq0);
        int xlat_id = PSQLutils.get_field_Integer(rsq0, "xlat_id");
        if (obsIndels.size() == 0) {
            LB.error("No indels in the specified library");
            LB.die();
        }
        String Q2 = "SELECT i.del_id, i.chromosome, i.region_start, i.region_end " + "FROM annotation_deletions as a , deletions as i where a.xlat_id = " + xlat_id + " AND a.del_id = i.del_id";
        ResultSet rsq2 = psql.select_statement(Q2);
        ArrayList<IndelCoordinates> annotIndels = new ArrayList<IndelCoordinates>();
        while (PSQLutils.do_next(rsq2)) {
            int id = PSQLutils.get_field_Integer(rsq2, "del_id");
            String chr = PSQLutils.get_field_String(rsq2, "chromosome");
            int start = PSQLutils.get_field_Integer(rsq2, "region_start");
            int end = PSQLutils.get_field_Integer(rsq2, "region_end");
            annotIndels.add(new IndelCoordinates(id, IndelCoordinates.DELETION, chr, start, end));
        }
        if (obsIndels.size() == 0) {
            LB.error("No indels in the specified dbsnp version");
            LB.die();
        }
        Collections.sort(annotIndels);
        int inDbsnp = 0;
        int notInDbsnp = 0;
        ArrayList<Integer> obsId = new ArrayList<Integer>();
        for (IndelCoordinates indel : obsIndels) {
            if (indelExists(indel, annotIndels, window)) {
                obsId.add(indel.getId());
                inDbsnp += 1;
            } else {
                notInDbsnp += 1;
            }
        }
        r = new Tuple<Integer, Integer>(inDbsnp, notInDbsnp);
        return r;
    }
}
