package src.projects.findPeaks;

import java.util.Vector;
import src.lib.Coverage;
import src.lib.ReducedAlignedReads;
import src.lib.Utilities;
import src.lib.ioInterfaces.Generic_AlignRead_Iterator;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.ioInterfaces.Wigwriter;
import src.lib.objects.AlignedRead;
import src.projects.findPeaks.FDR.ApplyControl;
import src.projects.findPeaks.objects.MapStore;
import src.projects.findPeaks.objects.Parameters;
import src.projects.findPeaks.objects.PeakStore;
import src.projects.findPeaks.objects.Peakdesc;
import src.projects.findPeaks.objects.SeqStore;

/**
 * @version $Revision: 848 $
 * @author 
 */
public class PeakDataSet {

    private static final int WINDOW_SIZE = 2000;

    private static boolean display_version = true;

    private static Log_Buffer LB;

    private MapStore maps;

    private PeakStore peaks;

    private SeqStore seqs;

    private String current_chromosome;

    private int[] start_map;

    Vector<AlignedRead> buffer_ahead = new Vector<AlignedRead>(0);

    /**
	 * must test if this.peaks == null after using this function.
	 * 
	 * @param logbuffer
	 * @param param
	 * @param it
	 * @param dist
	 */
    public PeakDataSet(Log_Buffer logbuffer, Parameters param, Generic_AlignRead_Iterator it, Distribution dist) {
        LB = logbuffer;
        if (display_version) {
            LB.Version("PeakDataSet Peak Locator", "$Revision: 848 $");
            display_version = false;
        }
        if (param.get_goldenpath() > 0) {
            seqs = new SeqStore(LB);
        } else {
            seqs = null;
        }
        if (dist.get_dist_type() == 2) {
            this.start_map = new int[(dist.get_max_ext_len() * 2) - 1];
        }
        if (dist.get_dist_type() == 3) {
            this.peaks = process_peaks_from_iterator_PET(it, param);
        } else {
            this.peaks = process_peaks_from_iterator(it, param, dist);
        }
    }

    public void clear() {
        if (maps != null) {
            maps.clear();
        }
        if (peaks != null) {
            peaks.clear();
        }
        if (seqs != null) {
            seqs.clear();
        }
        start_map = null;
    }

    /**
	 *  Peak Locator - will be the new peak locator for FindPeaks 4.0
	 * @param it
	 * @param param
	 * @param dist
	 * @return
	 */
    private PeakStore process_peaks_from_iterator(Generic_AlignRead_Iterator it, Parameters param, Distribution dist) {
        PeakStore ps = new PeakStore(LB);
        this.maps = new MapStore(LB, dist.intbased());
        int mapkey = 0;
        int cur_peak_start = 0;
        int cur_peak_end = 0;
        int reads_used = 0;
        int reads_filtered = 0;
        boolean first_time = true;
        Vector<AlignedRead> cur_reads = new Vector<AlignedRead>(0);
        Vector<Integer> start_locations = new Vector<Integer>(0);
        int frag_start = 0;
        int frag_end = 0;
        boolean subpeaks = param.get_subpeaks();
        boolean filter = param.get_filterDupes();
        AlignedRead ar = null;
        int max_length = dist.get_max_ext_len();
        LB.notice("max len is currently: " + max_length);
        while (it.hasNext() || buffer_ahead.size() > 0) {
            if (buffer_ahead.size() > 0) {
                ar = buffer_ahead.remove(0);
            } else {
                ar = it.next();
            }
            if (ar == null && cur_reads.size() == 0) {
                break;
            }
            if (first_time) {
                current_chromosome = ar.get_chromosome();
                LB.notice("Current chromosome : " + current_chromosome);
                first_time = false;
            } else if (ar != null && !ar.get_chromosome().equalsIgnoreCase(current_chromosome)) {
                buffer_ahead.add(ar);
                break;
            }
            if (cur_reads.size() == 0) {
                if (ar.get_direction() == '+') {
                    cur_peak_start = ar.get_alignStart();
                    cur_peak_end = ar.get_alignStart() + max_length;
                } else {
                    if (ar.get_alignEnd() < max_length) {
                        continue;
                    }
                    cur_peak_start = ar.get_alignEnd() - max_length;
                    cur_peak_end = ar.get_alignEnd();
                }
                cur_reads.add(ar);
                continue;
            } else {
                if (ar.get_alignStart() < cur_reads.firstElement().get_alignStart()) {
                    LB.error("The current file being processed has not been sorted.  Please Sort the " + "files (see SortFiles.jar) and re-start findpeaks.");
                    LB.die();
                }
            }
            if (ar.get_direction() == '+') {
                frag_start = ar.get_alignStart();
                frag_end = ar.get_alignStart() + max_length;
            } else {
                frag_start = ar.get_alignEnd() - max_length;
                frag_end = ar.get_alignEnd();
                if (frag_start < 0) {
                    continue;
                }
            }
            if ((frag_start >= cur_peak_start && frag_start <= cur_peak_end) || (frag_end >= cur_peak_start && frag_end <= cur_peak_end)) {
                cur_reads.add(ar);
                if (frag_end > cur_peak_end) {
                    cur_peak_end = frag_end;
                }
                if (frag_start < cur_peak_start) {
                    cur_peak_start = frag_start;
                }
            } else {
                boolean overlap = false;
                boolean far_enough = (frag_start > cur_peak_end + max_length) ? true : false;
                Vector<AlignedRead> local_buffer = new Vector<AlignedRead>();
                if (far_enough) {
                    buffer_ahead.insertElementAt(ar, 0);
                } else {
                    local_buffer.add(ar);
                }
                while (!far_enough && (it.hasNext() || buffer_ahead.size() > 0)) {
                    AlignedRead ar_tmp = null;
                    if (buffer_ahead.size() > 0) {
                        ar_tmp = buffer_ahead.remove(0);
                    } else {
                        ar_tmp = it.next();
                        if (ar_tmp != null && !ar_tmp.get_chromosome().equalsIgnoreCase(current_chromosome)) {
                            buffer_ahead.add(ar_tmp);
                            far_enough = true;
                            ar_tmp = null;
                        }
                    }
                    if (ar_tmp != null) {
                        local_buffer.add(ar_tmp);
                        if (ar_tmp.get_direction() == '+') {
                            far_enough = (ar_tmp.get_alignStart() - cur_peak_end > max_length) ? true : false;
                        } else {
                            if (ar_tmp.get_alignEnd() - max_length <= cur_peak_end) {
                                overlap = true;
                            } else if (ar_tmp.get_alignEnd() > max_length + cur_peak_end) {
                                far_enough = true;
                            }
                        }
                    }
                    if (overlap) {
                        while (local_buffer.size() > 0) {
                            AlignedRead a = local_buffer.remove(0);
                            if (a.get_direction() == '+') {
                                frag_start = a.get_alignStart();
                                frag_end = a.get_alignStart() + max_length;
                            } else {
                                frag_start = a.get_alignEnd() - max_length;
                                frag_end = a.get_alignEnd();
                            }
                            if (frag_end > cur_peak_end) {
                                cur_peak_end = frag_end;
                            }
                            if (frag_start < cur_peak_start) {
                                cur_peak_start = frag_start;
                            }
                            cur_reads.add(a);
                        }
                        far_enough = true;
                    } else if (far_enough) {
                        for (int r = 0; r < local_buffer.size(); r++) {
                            buffer_ahead.insertElementAt(local_buffer.get(r), r);
                        }
                        local_buffer.clear();
                    }
                }
                if (!it.hasNext() && buffer_ahead.size() == 0) {
                    far_enough = true;
                }
                if (far_enough) {
                    if (filter) {
                        int filtered_reads = cur_reads.size();
                        cur_reads = DuplicateFiltering.simplefilter(cur_reads);
                        filtered_reads -= cur_reads.size();
                        reads_filtered += filtered_reads;
                    }
                    if (cur_reads.size() == 1) {
                        ps.inc_LW_singles();
                    } else if (cur_reads.size() == 2) {
                        ps.inc_LW_doubles();
                    }
                    if (dist.intbased()) {
                        mapkey = process_int_based(ps, cur_reads, param, dist, cur_peak_start, cur_peak_end, subpeaks, mapkey);
                    } else {
                        mapkey = process_float_based(ps, cur_reads, param, dist, cur_peak_start, cur_peak_end, subpeaks, mapkey);
                    }
                    if (dist.get_dist_type() == 2) {
                        build_start_distribution(cur_reads, maps.get_max_location(mapkey), cur_peak_start, dist.get_max_ext_len() - 1);
                    }
                    reads_used += cur_reads.size();
                    cur_reads.clear();
                    start_locations.clear();
                    cur_reads = new Vector<AlignedRead>();
                }
                if (local_buffer.size() > 0) {
                    for (int r = 0; r < local_buffer.size(); r++) {
                        buffer_ahead.insertElementAt(local_buffer.get(r), r);
                    }
                    local_buffer.clear();
                }
            }
        }
        if (filter) {
            int filtered_reads = cur_reads.size();
            cur_reads = DuplicateFiltering.simplefilter(cur_reads);
            filtered_reads -= cur_reads.size();
            reads_filtered += filtered_reads;
        }
        if (cur_reads.size() == 1) {
            ps.inc_LW_singles();
        } else if (cur_reads.size() == 2) {
            ps.inc_LW_doubles();
        }
        if (dist.intbased()) {
            process_int_based(ps, cur_reads, param, dist, cur_peak_start, cur_peak_end, subpeaks, mapkey);
        } else {
            process_float_based(ps, cur_reads, param, dist, cur_peak_start, cur_peak_end, subpeaks, mapkey);
        }
        reads_used += cur_reads.size();
        ps.set_reads_used(reads_used);
        ps.set_reads_filtered(reads_filtered);
        maps.complete();
        LB.notice("Reads used: " + reads_used);
        LB.notice("Reads filtered by duplicate: " + reads_filtered);
        LB.notice("Reads filtered by iterator: " + it.iterator().get_NumberFilteredRead());
        return ps;
    }

    /**
	 * 
	 * @param ps
	 * @param cur_reads
	 * @param param
	 * @param dist
	 * @param cur_peak_start
	 * @param cur_peak_end
	 * @param subpeaks
	 * @param mapkey
	 * @return
	 */
    private int process_int_based(PeakStore ps, Vector<AlignedRead> cur_reads, Parameters param, Distribution dist, int cur_peak_start, int cur_peak_end, boolean subpeaks, int mapkey) {
        if (cur_reads.size() >= param.get_min_store_ht()) {
            int[] coverage_array = Coverage.generatePeakHeight_ld_int(cur_reads, dist, cur_peak_start, cur_peak_end);
            maps.put(coverage_array);
            PeakStore p = PeakLocator.process(LB, param, coverage_array, cur_peak_start, cur_peak_end, subpeaks, mapkey);
            if (param.get_goldenpath() > 0) {
                int width = param.get_goldenpath();
                for (Peakdesc pd : p) {
                    if (pd.get_height() > param.get_min_ht() && pd.get_height() > param.get_min_store_ht()) {
                        int t = seqs.add(GoldenPath(cur_reads, cur_peak_start, cur_peak_end, pd.get_max_loc() - width, pd.get_max_loc() + width));
                        pd.set_seq_key(t);
                    }
                }
            }
            ps.merge(p);
            mapkey++;
        } else {
            ps.add(new Peakdesc(0, cur_peak_end - cur_peak_start, (cur_peak_end - cur_peak_start) / 2, cur_peak_start, cur_reads.size(), dist.get_area() * cur_reads.size(), 0f, -1));
        }
        return mapkey;
    }

    private int process_float_based(PeakStore ps, Vector<AlignedRead> cur_reads, Parameters param, Distribution dist, int cur_peak_start, int cur_peak_end, boolean subpeaks, int mapkey) {
        if (cur_reads.size() >= param.get_min_store_ht()) {
            float[] coverage_array = Coverage.generatePeakHeight_ld_float(cur_reads, dist, cur_peak_start, cur_peak_end);
            maps.put(coverage_array);
            PeakStore p = PeakLocator.process(LB, param, coverage_array, cur_peak_start, cur_peak_end, subpeaks, mapkey);
            if (param.get_goldenpath() > 0) {
                int width = param.get_goldenpath();
                for (Peakdesc pd : p) {
                    if (pd.get_height() > param.get_min_ht() && pd.get_height() > param.get_min_store_ht()) {
                        int t = seqs.add(GoldenPath(cur_reads, cur_peak_start, cur_peak_end, pd.get_max_loc() - width, pd.get_max_loc() + width));
                        pd.set_seq_key(t);
                    }
                }
            }
            ps.merge(p);
            mapkey++;
        } else {
            ps.add(new Peakdesc(0, cur_peak_end - cur_peak_start, (cur_peak_end - cur_peak_start) / 2, cur_peak_start, cur_reads.size(), dist.get_area() * cur_reads.size(), 0f, -1));
        }
        return mapkey;
    }

    /**
	 * 
	 * @param cur_reads
	 * @param get_max_location
	 * @param cur_peak_start
	 * @param midpoint		(max_len - 1)
	 */
    private void build_start_distribution(Vector<AlignedRead> cur_reads, int get_max_location, int cur_peak_start, int midpoint) {
        int absolute_loc = get_max_location + cur_peak_start;
        for (AlignedRead r : cur_reads) {
            int rel_loc = r.get_alignStart() - absolute_loc + midpoint;
            if (rel_loc >= 0 && rel_loc < start_map.length) {
                start_map[rel_loc]++;
            }
        }
    }

    /**
	 *  Peak Locator - will be the new peak locator for FindPeaks 4.0
	 * @param it
	 * @param param
	 * @return
	 */
    private PeakStore process_peaks_from_iterator_PET(Generic_AlignRead_Iterator it, Parameters param) {
        PeakStore ps = new PeakStore(LB);
        this.maps = new MapStore(LB, true);
        int mapkey = 0;
        int cur_peak_start = 0;
        int cur_peak_end = 0;
        int reads_used = 0;
        int reads_filtered = 0;
        boolean first_time = true;
        Vector<AlignedRead> cur_reads = new Vector<AlignedRead>(0);
        int frag_start = 0;
        int frag_end = 0;
        boolean subpeaks = param.get_subpeaks();
        boolean filter = param.get_filterDupes();
        AlignedRead ar = null;
        while (it.hasNext() || buffer_ahead.size() > 0) {
            if (buffer_ahead.size() > 0) {
                ar = buffer_ahead.remove(0);
            } else {
                ar = it.next();
            }
            if (ar == null && cur_reads.size() == 0) {
                break;
            }
            if (first_time) {
                current_chromosome = ar.get_chromosome();
                LB.notice("Current chromosome : " + current_chromosome);
                first_time = false;
            } else if (ar != null && !ar.get_chromosome().equalsIgnoreCase(current_chromosome)) {
                buffer_ahead.add(ar);
                break;
            }
            if (cur_reads.size() == 0) {
                cur_peak_start = ar.get_alignStart();
                cur_peak_end = ar.get_alignEnd();
                cur_reads.add(ar);
                continue;
            } else {
                if (ar.get_alignStart() < cur_reads.firstElement().get_alignStart()) {
                    LB.error("The current file being processed has not been sorted.  Please Sort the " + "files (see SortFiles.jar) and re-start findpeaks.");
                    LB.die();
                }
            }
            frag_start = ar.get_alignStart();
            frag_end = ar.get_alignEnd();
            if ((frag_start >= cur_peak_start && frag_start <= cur_peak_end) || (frag_end >= cur_peak_start && frag_end <= cur_peak_end)) {
                cur_reads.add(ar);
                if (frag_end > cur_peak_end) {
                    cur_peak_end = frag_end;
                }
                if (frag_start < cur_peak_start) {
                    cur_peak_start = frag_start;
                }
            } else {
                boolean overlap = false;
                boolean far_enough = (frag_start - cur_peak_end > WINDOW_SIZE) ? true : false;
                Vector<AlignedRead> local_buffer = new Vector<AlignedRead>();
                if (far_enough) {
                    buffer_ahead.insertElementAt(ar, 0);
                } else {
                    local_buffer.add(ar);
                }
                while (!far_enough && (it.hasNext() || buffer_ahead.size() > 0)) {
                    AlignedRead ar_tmp = null;
                    if (buffer_ahead.size() > 0) {
                        ar_tmp = buffer_ahead.remove(0);
                    } else {
                        ar_tmp = it.next();
                        if (ar_tmp != null && !ar_tmp.get_chromosome().equalsIgnoreCase(current_chromosome)) {
                            buffer_ahead.add(ar_tmp);
                            far_enough = true;
                            ar_tmp = null;
                        }
                    }
                    if (ar_tmp != null) {
                        local_buffer.add(ar_tmp);
                        if (ar_tmp.get_alignStart() <= cur_peak_end) {
                            overlap = true;
                        } else if (ar_tmp.get_alignStart() - cur_peak_end > WINDOW_SIZE) {
                            far_enough = true;
                        }
                    }
                    if (overlap) {
                        while (local_buffer.size() > 0) {
                            AlignedRead a = local_buffer.remove(0);
                            frag_start = a.get_alignStart();
                            frag_end = a.get_alignEnd();
                            if (frag_end > cur_peak_end) {
                                cur_peak_end = frag_end;
                            }
                            if (frag_start < cur_peak_start) {
                                cur_peak_start = frag_start;
                            }
                            cur_reads.add(a);
                        }
                        far_enough = true;
                    } else if (far_enough) {
                        for (int r = 0; r < local_buffer.size(); r++) {
                            buffer_ahead.insertElementAt(local_buffer.get(r), r);
                        }
                        local_buffer.clear();
                    }
                }
                if (!it.hasNext() && buffer_ahead.size() == 0) {
                    far_enough = true;
                }
                if (far_enough) {
                    if (filter) {
                        int filtered_reads = cur_reads.size();
                        cur_reads = DuplicateFiltering.simplefilter(cur_reads);
                        filtered_reads -= cur_reads.size();
                        reads_filtered += filtered_reads;
                    }
                    if (cur_reads.size() == 1) {
                        ps.inc_LW_singles();
                    } else if (cur_reads.size() == 2) {
                        ps.inc_LW_doubles();
                    }
                    int[] coverage_array = Coverage.generatePeakHeight_PET(cur_reads, cur_peak_start, cur_peak_end);
                    maps.put(coverage_array);
                    ps.merge(PeakLocator.process(LB, param, coverage_array, cur_peak_start, cur_peak_end, subpeaks, mapkey));
                    mapkey++;
                    reads_used += cur_reads.size();
                    cur_reads.clear();
                    cur_reads = new Vector<AlignedRead>();
                }
                if (local_buffer.size() > 0) {
                    for (int r = 0; r < local_buffer.size(); r++) {
                        buffer_ahead.insertElementAt(local_buffer.get(r), r);
                    }
                    local_buffer.clear();
                }
            }
        }
        if (filter) {
            int filtered_reads = cur_reads.size();
            cur_reads = DuplicateFiltering.simplefilter(cur_reads);
            filtered_reads -= cur_reads.size();
            reads_filtered += filtered_reads;
        }
        if (cur_reads.size() == 1) {
            ps.inc_LW_singles();
        } else if (cur_reads.size() == 2) {
            ps.inc_LW_doubles();
        }
        reads_used += cur_reads.size();
        int[] coverage_array = Coverage.generatePeakHeight_PET(cur_reads, cur_peak_start, cur_peak_end);
        maps.put(coverage_array);
        ps.merge(PeakLocator.process(LB, param, coverage_array, cur_peak_start, cur_peak_end, subpeaks, mapkey));
        ps.set_reads_used(reads_used);
        ps.set_reads_filtered(reads_filtered);
        maps.complete();
        LB.notice("Reads used: " + reads_used);
        return ps;
    }

    /**
	 * 
	 * @param x
	 * @return
	 */
    private static final int base_to_int(char x) {
        char y = Character.toUpperCase(x);
        if (y == 'A') {
            return 0;
        } else if (y == 'C') {
            return 1;
        } else if (y == 'G') {
            return 2;
        } else if (y == 'T') {
            return 3;
        }
        return -1;
    }

    /**
	 * @param offset
	 * @param region_end
	 * @param reads
	 * @param start
	 * @param end
	 * @return
	 */
    private static String GoldenPath(Vector<AlignedRead> reads, int offset, int region_end, int start, int end) {
        int span = region_end - offset + 1;
        int[][] chart = new int[span][4];
        String x = "";
        for (AlignedRead ar : reads) {
            String s = ar.get_sequence();
            int s_pos = ar.get_alignStart();
            for (int i = 0; i < s.length(); i++) {
                int b = base_to_int(s.charAt(i));
                if (b >= 0) {
                    chart[s_pos - offset + i][base_to_int(s.charAt(i))]++;
                }
            }
        }
        if (start < 0) {
            start = 0;
        }
        for (int z = start; z < end; z++) {
            x += get_nucleotide(chart[z]);
        }
        return x;
    }

    /**
	 * Determines the nucleotide most observed at a position, when passed an
	 * array indicating the number of observed occurences. The structure is an int
	 * array[4], where [0] = 'A', [1] = 'C', [2] = 'G', [3] = 'T'.
	 * 
	 * @param a
	 * @return
	 */
    private static char get_nucleotide(int[] a) {
        assert (a.length == 4);
        if (a[0] > a[1]) {
            if (a[0] > a[2]) {
                if (a[0] > a[3]) {
                    return 'A';
                } else if (a[0] < a[3]) {
                    return 'T';
                } else {
                    return Utilities.get_canonical_char('A', 'T');
                }
            } else if (a[0] < a[2]) {
                if (a[2] > a[3]) {
                    return 'G';
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return Utilities.get_canonical_char('G', 'T');
                }
            } else {
                if (a[2] > a[3]) {
                    return Utilities.get_canonical_char('A', 'G');
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return 'N';
                }
            }
        } else if (a[0] < a[1]) {
            if (a[1] > a[2]) {
                if (a[1] > a[3]) {
                    return 'C';
                } else if (a[1] < a[3]) {
                    return 'T';
                } else {
                    return Utilities.get_canonical_char('C', 'T');
                }
            } else if (a[1] < a[2]) {
                if (a[2] > a[3]) {
                    return 'G';
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return Utilities.get_canonical_char('G', 'T');
                }
            } else {
                if (a[2] > a[3]) {
                    return Utilities.get_canonical_char('C', 'G');
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return 'N';
                }
            }
        } else {
            if (a[1] > a[2]) {
                if (a[1] > a[3]) {
                    return Utilities.get_canonical_char('A', 'C');
                } else if (a[1] < a[3]) {
                    return 'T';
                } else {
                    return 'N';
                }
            } else if (a[1] < a[2]) {
                if (a[2] > a[3]) {
                    return 'G';
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return 'N';
                }
            } else {
                if (a[2] > a[3]) {
                    return 'N';
                } else if (a[2] < a[3]) {
                    return 'T';
                } else {
                    return 'N';
                }
            }
        }
    }

    /**
	 * Performs the directional read correction, but does not work for mode 3.
	 * Actually, I don't really have much faith in this algorithm. we should
	 * probably re-consider how this works.
	 * 
	 * @param reads
	 * @param dist
	 * @param first_hit
	 * @param last_hit
	 * @param length
	 * @param max_extended_length
	 * @return a modified coverage array which uses directional reads.
	 * @deprecated
	 */
    private static float[] directional_reads(ReducedAlignedReads reads, Distribution dist, int first_hit, int last_hit, int length, int max_extended_length) {
        float a = 0;
        float b = 0;
        float c = 0;
        float d = 0;
        float[] pkEx = new float[length];
        int offset = reads.get_AlignStart(first_hit);
        for (int s = 0; s < length; s++) {
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            int span = 0;
            for (int y = first_hit; y <= last_hit; y++) {
                if (reads.get_direction(y) == '+') {
                    if (reads.get_AlignStart(y) <= s + offset) {
                        span = s + offset - reads.get_AlignStart(y);
                        a += (span < max_extended_length) ? dist.value_at(span) : 0;
                    } else if (reads.get_AlignStart(y) > s + offset) {
                        b++;
                    }
                } else {
                    if (reads.get_AlignStart(y) + max_extended_length < s + offset) {
                        c++;
                    } else if (reads.get_AlignStart(y) + max_extended_length >= s + offset) {
                        span = (reads.get_AlignStart(y) + max_extended_length) - (s + offset);
                        d += (span < max_extended_length) ? dist.value_at(span) : 0;
                    }
                }
            }
            pkEx[s] = (a + d);
        }
        return pkEx;
    }

    /**
	 * Function to write out wigfile. peakfile or RFile.
	 * @param dist
	 * @param pw
	 * @param wig
	 * @param threshold
	 * @param RMode
	 * @param m
	 */
    public void write_out_files(Distribution dist, PeakWriter pw, Wigwriter wig, float threshold, boolean RMode, int m) {
        if (RMode) {
            for (Peakdesc p : this.peaks) {
                if (p.get_height() >= threshold) {
                    RFunctions.write_R_line(pw, p.get_start(), m, p.get_height());
                }
            }
        }
        if (wig != null) {
            if (dist.intbased()) {
                int_based_output(pw, wig, threshold);
            } else {
                float_based_output(pw, wig, threshold);
            }
        } else {
            LB.warning("wigfile handle was null.  Will not write out wig file.");
        }
    }

    /**
	 * Function to write out wigfile. peakfile or RFile.
	 * @param dist
	 * @param pw
	 * @param wig
	 * @param threshold
	 * @param ac
	 */
    public void write_out_files(Distribution dist, PeakWriter pw, Wigwriter wig, float threshold, ApplyControl ac) {
        if (wig != null) {
            if (dist.intbased()) {
                int_based_output(pw, wig, threshold, ac);
            } else {
                float_based_output(pw, wig, threshold, ac);
            }
        } else {
            LB.warning("wigfile handle was null.  Will not write out wig file.");
        }
    }

    private void int_based_output(PeakWriter pw, Wigwriter wig, float threshold) {
        int[] a = null;
        for (Peakdesc p : this.peaks) {
            if (p.get_height() >= threshold) {
                pw.writePeak2(this.current_chromosome, p, (this.seqs != null) ? this.seqs.get(p.get_seq_key()) : "");
                wig.section_header(this.current_chromosome, p.get_offset());
                a = this.maps.get_i(p.get_hashkey());
                for (int x = p.get_start(); x <= p.get_end(); x++) {
                    wig.writeln(a[x]);
                }
            }
        }
    }

    private void int_based_output(PeakWriter pw, Wigwriter wig, float threshold, ApplyControl ac) {
        int[] a = null;
        for (Peakdesc p : this.peaks) {
            if (p.get_height() >= threshold) {
                pw.writePeak3(this.current_chromosome, p, ac.get_significance(p.get_height()), this.seqs.get(p.get_seq_key()));
                wig.section_header(this.current_chromosome, p.get_offset());
                a = this.maps.get_i(p.get_hashkey());
                for (int x = p.get_start(); x <= p.get_end(); x++) {
                    wig.writeln(a[x]);
                }
            }
        }
    }

    private void float_based_output(PeakWriter pw, Wigwriter wig, float threshold) {
        float[] a = null;
        for (Peakdesc p : this.peaks) {
            if (p.get_height() >= threshold) {
                pw.writePeak2(this.current_chromosome, p, (this.seqs != null) ? this.seqs.get(p.get_seq_key()) : "");
                wig.section_header(this.current_chromosome, p.get_offset());
                a = this.maps.get_f(p.get_hashkey());
                for (int x = p.get_start(); x <= p.get_end(); x++) {
                    wig.writeln(Utilities.DecimalPoints(a[x], 2));
                }
            }
        }
    }

    private void float_based_output(PeakWriter pw, Wigwriter wig, float threshold, ApplyControl ac) {
        float[] a = null;
        for (Peakdesc p : this.peaks) {
            if (p.get_height() >= threshold) {
                pw.writePeak3(this.current_chromosome, p, ac.get_significance(p.get_height()), this.seqs.get(p.get_seq_key()));
                wig.section_header(this.current_chromosome, p.get_offset());
                a = this.maps.get_f(p.get_hashkey());
                for (int x = p.get_start(); x <= p.get_end(); x++) {
                    wig.writeln(Utilities.DecimalPoints(a[x], 2));
                }
            }
        }
    }

    /**
	 * Getter - returns the peakstore generated.  May not be necessary.
	 * @return 
	 */
    public PeakStore get_peak_store() {
        return this.peaks;
    }

    /**
	 * Do null check 
	 * @return boolean
	 */
    public boolean is_null() {
        return (this.peaks == null) ? true : false;
    }

    public boolean is_empty() {
        return (this.peaks.get_size() > 1) ? false : true;
    }

    /**
	 * Getter - returns the mapstore generated.  May not be necessary.
	 * @return 
	 */
    public MapStore get_map_store() {
        return this.maps;
    }

    /**
	 * Getter - returns the chromosome that was most recently processed.
	 * @return 
	 */
    public String get_chromosome() {
        return this.current_chromosome;
    }

    /**
	 * Getter - returns the start_distributions compiled from this data set 
	 * @return 
	 */
    public int[] get_start_distribution() {
        return this.start_map;
    }

    /**
	 * getter to return the list of peak descriptions as an array
	 * @return
	 */
    public Peakdesc[] get_array_of_peakdesc() {
        return this.peaks.get_array_of_peaks();
    }

    /**
	 * 
	 * @param idx
	 * @param start in genomic coordinates.
	 * @param end
	 * @return
	 */
    public float highest_point(int idx, int start, int end) {
        Peakdesc n = this.peaks.get_peak(idx);
        int offset = n.get_offset();
        int s = start - offset;
        int e = end - offset;
        if (s < 0) {
            s = 0;
        }
        if (e > n.get_end()) {
            e = n.get_end();
        }
        return this.maps.get_max_height(idx, s, e);
    }
}
