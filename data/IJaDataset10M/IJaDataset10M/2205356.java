/*
 * Copyright 2010 Susanta Tewari.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package genomemap.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class cntains some statics on data in addition to some data. These statistics are maintained
 * in the test input data files and the testing code uses these values to test against those derived
 * from the test data input files.
 * @version 1.0 Dec 8, 2009
 * @author Susanta Tewari
 */
public class TestData {

    public static void setupData() {
        System.setProperty("data.properties", "testdata.properties");
    }

    public static final int GENE_COUNT = 7;

    public static final int GENOTYPE_SAMPLE_SIZE = 14;

    public static final int CLONE_COUNT = 17;

    public static final int PROBE_COUNT = 7;

    public static final Set<String> GENES = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(new String[]{"cyt-21", "vma-4", "sod-1", "nuo20.8", "ccg-4", "A/a",
                "mei-3"})));

    public static final Set<String> PROBES = Collections.unmodifiableSet(new HashSet<>(Arrays.
            asList(new String[]{"X122B09", "X129E08", "X151B04", "X152C01", "H019G06",
                "H027C09", "H052B06"})));

    public static final Set<String> CLONES = Collections.unmodifiableSet(new HashSet<>(Arrays.
            asList(new String[]{"X151B10", "X124H06", "H084B04", "H076H05", "X122H05",
                "H062C04", "H099G05", "H044G09", "X132H12", "X135B06", "X122B09", "X129E08",
                "X151B04", "X152C01", "H019G06", "H027C09", "H052B06"})));

    public static final Map<String, Set<String>> GW_COMPL_DATA =
            new HashMap<>();

    /**
     * gene-clone assignments based on complementation data
     */
    static {
        //fill GW_COMPL_DATA with data
        GW_COMPL_DATA.put("actin", new HashSet(Arrays.asList(new String[]{"X148B12"})));
        GW_COMPL_DATA.put("ad-7", new HashSet(Arrays.asList(new String[]{"X128H08", "X130H05"})));
        GW_COMPL_DATA.put("al-1", new HashSet(Arrays.asList(new String[]{"X107G07", "X108F02",
                    "X115H03"})));
        GW_COMPL_DATA.put("al-2", new HashSet(Arrays.asList(new String[]{"X120D06", "X130C01",
                    "X131F09", "X132G08"})));
        GW_COMPL_DATA.put("al-3", new HashSet(Arrays.asList(new String[]{"X123A07", "X125C05"})));
    }

    /**
     * gene-clone assignments based on sequence positions
     */
    public static final Map<String, Set<String>> GENE_COSMID_ASSIGNMENTS =
            new HashMap<>();

    static {
        // ch-1
        GENE_COSMID_ASSIGNMENTS.put("cyt-21", new HashSet(Arrays.asList(new String[]{"X122B09",
                    "X151B10"})));
        GENE_COSMID_ASSIGNMENTS.put("vma-4", new HashSet(Arrays.asList(new String[]{"X129E08"})));
        GENE_COSMID_ASSIGNMENTS.put("sod-1", new HashSet(Arrays.asList(new String[]{"X151B04"})));
        GENE_COSMID_ASSIGNMENTS.put("nuo20.8", new HashSet(Arrays.asList(new String[]{"X152C01"})));
        GENE_COSMID_ASSIGNMENTS.put("ccg-4", new HashSet(Arrays.asList(new String[]{"H019G06"})));
        GENE_COSMID_ASSIGNMENTS.put("A/a", new HashSet(Arrays.asList(new String[]{"X151B10"})));

        // ch-2
        GENE_COSMID_ASSIGNMENTS.put("hsps-1", new HashSet(Arrays.asList(new String[]{"H006F11",
                    "X107A03"})));
        GENE_COSMID_ASSIGNMENTS.put("AP32b.1", new HashSet(Arrays.asList(new String[]{"H003B09"})));
        GENE_COSMID_ASSIGNMENTS.put("Cen-II", new HashSet(Arrays.asList(new String[]{"H003C01"})));
        GENE_COSMID_ASSIGNMENTS.put("atp-2", new HashSet(Arrays.asList(new String[]{"H009C01"})));
        GENE_COSMID_ASSIGNMENTS.put("cya-4", new HashSet(Arrays.asList(new String[]{"H009E09"})));
        GENE_COSMID_ASSIGNMENTS.put("nuo78", new HashSet(Arrays.asList(new String[]{"X107A03"})));
    }

}
