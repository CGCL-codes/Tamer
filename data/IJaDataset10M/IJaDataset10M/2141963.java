package org.proteinarchitect.analysis.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.fmi.bioinformatics.db.DAOFactory;
import org.fmi.bioinformatics.system.io.PropertiesManager;
import org.proteinarchitect.analysis.blast.BlastHit;
import org.proteinarchitect.core.JobResources;
import org.proteinarchitect.format.ProteinSequenceI;
import org.proteinarchitect.format.SequenceCollectionI;

/**
 * @author mhaimel
 *
 */
public class FilterByTaxonomy extends AbstractProteinAnalysis {

    public static final String BLAST_FILTER_MAX_PER_SPECIES = "tools.blast.filter.taxonomy.maxperspecies";

    public static final String BLAST_FILTER_BY_TAXID = "tools.blast.filter.taxonomy.incl";

    /**
	 * @param res
	 */
    public FilterByTaxonomy(JobResources res, DAOFactory dao) {
        super(res, dao);
    }

    @Override
    protected void testInit() {
        PropertiesManager.getInstance().getDefaultProperties().setProperty(BLAST_FILTER_MAX_PER_SPECIES, "1");
    }

    public void analyse(SequenceCollectionI<ProteinSequenceI> collSeq) {
        getLog().info("Filter sequences by Taxonomy ...");
        addParameterToDBIfNotEmpty("Proteins per species displayed:", String.valueOf(getMaxPerSpecies()));
        List<ProteinSequenceI> seqList = new ArrayList<ProteinSequenceI>();
        {
            List<ProteinSequenceI> origSeq = collSeq.getSequences();
            seqList.add(origSeq.remove(0));
            List<ProteinSequenceI> allowedSeq = filterAllowedSequences(origSeq);
            Map<String, SortedSet<ProteinSequenceI>> map = sortResultsByTaxid(allowedSeq);
            for (SortedSet<ProteinSequenceI> entry : map.values()) {
                int i = 0;
                for (ProteinSequenceI valEntry : entry) {
                    if (i >= getMaxPerSpecies()) {
                        break;
                    }
                    seqList.add(valEntry);
                    ++i;
                }
            }
            getLog().debug("Use " + seqList.size() + " from " + collSeq.getSequences().size());
        }
        collSeq.setSequences(seqList);
    }

    private List<ProteinSequenceI> filterAllowedSequences(List<ProteinSequenceI> origSeq) {
        List<ProteinSequenceI> validList = new ArrayList<ProteinSequenceI>();
        Set<String> allowedTaxids = getAllowedTaxids();
        for (ProteinSequenceI seq : origSeq) {
            if (allowedTaxids.contains(seq.getTaxId().getProperty().getTaxid())) {
                validList.add(seq);
            }
        }
        return validList;
    }

    private Set<String> getAllowedTaxids() {
        String propString = PropertiesManager.getInstance().getDefaultProperties().getProperty(BLAST_FILTER_BY_TAXID);
        Set<String> taxId = new HashSet<String>();
        for (String s : propString.split(";")) {
            s = s.trim();
            if (s.length() > 0) {
                taxId.add(s);
            }
        }
        return taxId;
    }

    private Map<String, SortedSet<ProteinSequenceI>> sortResultsByTaxid(List<ProteinSequenceI> list) {
        Map<String, SortedSet<ProteinSequenceI>> map = new HashMap<String, SortedSet<ProteinSequenceI>>();
        for (ProteinSequenceI protSeq : list) {
            String taxId = "-";
            if (protSeq.getTaxId().hasProperty()) {
                taxId = protSeq.getTaxId().getProperty().getTaxid();
            }
            SortedSet<ProteinSequenceI> sortedSet = map.get(taxId);
            if (null == sortedSet) {
                sortedSet = new TreeSet<ProteinSequenceI>(new MyCompara());
                map.put(taxId, sortedSet);
            }
            sortedSet.add(protSeq);
        }
        return map;
    }

    private Integer getMaxPerSpecies() {
        return Integer.valueOf(PropertiesManager.getInstance().getDefaultProperties().getProperty(BLAST_FILTER_MAX_PER_SPECIES));
    }

    private static class MyCompara implements Comparator<ProteinSequenceI> {

        public int compare(ProteinSequenceI o1, ProteinSequenceI o2) {
            BlastHit h1 = o1.getSequenceProperties().getBlastHit().getProperty();
            BlastHit h2 = o2.getSequenceProperties().getBlastHit().getProperty();
            int res = h1.getPcIdentity().compareTo(h2.getPcIdentity()) * -1;
            if (res == 0) {
                res = h1.getEValue().compareTo(h2.getEValue());
                if (res == 0) {
                    res = h1.getBitScore().compareTo(h2.getBitScore()) * -1;
                    if (res == 0) {
                        res = h1.getAlignLength().compareTo(h2.getAlignLength()) * -1;
                        if (res == 0) {
                            res = o1.getPrimary().getProperty().compareTo(o2.getPrimary().getProperty()) * -1;
                        }
                    }
                }
            }
            return res;
        }
    }

    ;
}
