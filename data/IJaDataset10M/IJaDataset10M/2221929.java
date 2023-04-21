package org.jcvi.glk.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.hibernate.Session;
import org.jcvi.Range;
import org.jcvi.assembly.Clone;
import org.jcvi.assembly.Contig;
import org.jcvi.assembly.PlacedRead;
import org.jcvi.glk.AssemblySequenceRead;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.Feature;
import org.jcvi.glk.Sequence;
import org.jcvi.glk.SequenceRead;
import org.jcvi.glk.Well;
import org.jcvi.glk.elvira.AmpliconExtent;
import org.jcvi.glk.elvira.ElviraUtil;
import org.jcvi.glk.helpers.GLKHelper;
import org.jcvi.glyph.nuc.DefaultNucleotideEncodedGlyphs;
import org.jcvi.sequence.Read;

public class AmpliconCloneAdapter<T extends Read> implements Clone<T> {

    private final AmpliconExtent amplicon;

    private final String name;

    private final Set<T> reads;

    AmpliconCloneAdapter(AmpliconExtent amplicon, String runId, Set<T> reads) {
        this.amplicon = amplicon;
        this.name = amplicon.getDescription() + " run " + runId;
        this.reads = reads;
    }

    public static List<AmpliconCloneAdapter<Read>> buildClonesFrom(AmpliconExtent amplicon) {
        Map<String, Set<Read>> readsByRun = new HashMap<String, Set<Read>>();
        for (SequenceRead seqRead : amplicon.getAllSequences()) {
            final Sequence sequence = seqRead.getSequence();
            String runId = ElviraUtil.getPCRRunFor(sequence.getName());
            if (!readsByRun.containsKey(runId)) {
                readsByRun.put(runId, new HashSet<Read>());
            }
            final Feature clv = sequence.getFeaturesByType().get(Feature.Type.CLV);
            Range validRange = Range.buildRange(clv.getEnd5(), clv.getEnd3());
            readsByRun.get(runId).add(new ReadAdapter<DefaultNucleotideEncodedGlyphs>(sequence, new DefaultNucleotideEncodedGlyphs(sequence.getCurrentBasecalls().getBases(), validRange)));
        }
        List<AmpliconCloneAdapter<Read>> result = new ArrayList<AmpliconCloneAdapter<Read>>();
        for (Entry<String, Set<Read>> entry : readsByRun.entrySet()) {
            result.add(new AmpliconCloneAdapter(amplicon, entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public static List<AmpliconCloneAdapter<PlacedRead>> buildClonesFrom(Contig<? extends PlacedRead> contig, GLKHelper helper, Session session) {
        ExtentType ampliconType = helper.getExtentType("AMPLICON");
        Map<AmpliconExtent, Map<Well, Map<String, Set<PlacedRead>>>> ampliconrunMap = new HashMap<AmpliconExtent, Map<Well, Map<String, Set<PlacedRead>>>>();
        final int assemblyId = Integer.parseInt(contig.getId());
        for (AssemblySequenceRead assemblySequenceRead : helper.getAssemblySequenceReadsIn(assemblyId)) {
            final SequenceRead sequenceRead = assemblySequenceRead.getSequenceRead();
            final Sequence sequence = sequenceRead.getSequence();
            if (contig.containsPlacedRead(sequence.getName())) {
                Extent extent = sequenceRead.getExtent().getFirstParentWhoseTypeIs(ampliconType);
                final AmpliconExtent amplicon = (AmpliconExtent) session.get(AmpliconExtent.class, extent.getId());
                if (!ampliconrunMap.containsKey(amplicon)) {
                    ampliconrunMap.put(amplicon, new HashMap<Well, Map<String, Set<PlacedRead>>>());
                }
                Well well = ElviraUtil.getWellFor(sequence.getName());
                Map<Well, Map<String, Set<PlacedRead>>> wellMap = ampliconrunMap.get(amplicon);
                if (!wellMap.containsKey(well)) {
                    wellMap.put(well, new HashMap<String, Set<PlacedRead>>());
                }
                String runId = ElviraUtil.getPCRRunFor(sequence.getName());
                Map<String, Set<PlacedRead>> readsByRun = wellMap.get(well);
                if (!readsByRun.containsKey(runId)) {
                    readsByRun.put(runId, new HashSet<PlacedRead>());
                }
                readsByRun.get(runId).add(contig.getPlacedReadById(sequence.getName()));
            }
        }
        List<AmpliconCloneAdapter<PlacedRead>> result = new ArrayList<AmpliconCloneAdapter<PlacedRead>>();
        for (Entry<AmpliconExtent, Map<Well, Map<String, Set<PlacedRead>>>> ampliconEntry : ampliconrunMap.entrySet()) {
            for (Entry<Well, Map<String, Set<PlacedRead>>> wellEntry : ampliconEntry.getValue().entrySet()) {
                for (Entry<String, Set<PlacedRead>> entry : wellEntry.getValue().entrySet()) {
                    result.add(new AmpliconCloneAdapter<PlacedRead>(ampliconEntry.getKey(), entry.getKey(), entry.getValue()));
                }
            }
        }
        return result;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public Set<T> getReads() {
        return reads;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Clone)) return false;
        Clone other = (Clone) obj;
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public AmpliconExtent getAmpliconExtent() {
        return amplicon;
    }
}
