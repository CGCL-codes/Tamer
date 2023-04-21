package org.jcvi.autoTasker.featureFinder.contig.sanger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jcvi.autoTasker.CodingRegionedScaffold;
import org.jcvi.autoTasker.SampleData;
import org.jcvi.autoTasker.feature.DefaultCompositeFeature;
import org.jcvi.autoTasker.feature.DefaultContigFeature;
import org.jcvi.autoTasker.feature.DefaultScaffoldFeature;
import org.jcvi.autoTasker.feature.CommonFeatureTypes;
import org.jcvi.autoTasker.feature.ScaffoldFeature;
import org.jcvi.autoTasker.featureFinder.FeatureFinderException;
import org.jcvi.autoTasker.featureFinder.FeatureFinderListener;
import org.jcvi.autoTasker.featureFinder.contig.AbstractContigFeatureFinder;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.Ranges;
import org.jcvi.common.core.assembly.Contig;
import org.jcvi.common.core.assembly.ContigDataStore;
import org.jcvi.common.core.assembly.PlacedContig;
import org.jcvi.common.core.assembly.PlacedRead;
import org.jcvi.common.core.assembly.Scaffold;
import org.jcvi.common.core.assembly.ScaffoldUtil;
import org.jcvi.common.core.assembly.util.coverage.CoverageMap;
import org.jcvi.common.core.assembly.util.coverage.CoverageRegion;
import org.jcvi.common.core.assembly.util.coverage.DefaultCoverageMap;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.core.util.iter.IteratorIterableAdapter;

public abstract class AbstractSangerCloneCoverageFeatureFinder<PR extends PlacedRead, C extends Contig<PR>> extends AbstractContigFeatureFinder<PR, C> {

    /** The minimum acceptable level of clone coverage. */
    private final int minCoverage;

    /**
     * Constructs a new <code>CloneCoverageAnalyzer</code>.
     * @param contigDataStore the contigDataStore which stores all the contig
     * data that will be referenced.
     * @param listeners the {@link FeatureFinderListener} that will
     * be notified if any features are detected..
     * @param minCoverage The minimum acceptable level of clone coverage.
     * @throws NullPointerException if the contigDataStore or listeners are null.
     * @throws IllegalArgumentException if minCoverage or clonePrefixLength are <0
     * or if listeners is empty.
     */
    public AbstractSangerCloneCoverageFeatureFinder(ContigDataStore<PR, C> contigDataStore, Collection<? extends FeatureFinderListener> listeners, int minCoverage) {
        super(contigDataStore, listeners);
        if (minCoverage < 0) {
            throw new IllegalArgumentException("min coverage must be >=0 :" + minCoverage);
        }
        this.minCoverage = minCoverage;
    }

    @Override
    public void beginFeatureFinding() {
    }

    @Override
    public void findFeaturesFor(CodingRegionedScaffold codingRegionedScaffold) throws FeatureFinderException {
        Scaffold scaffold = codingRegionedScaffold.getScaffold();
        List<Range> cloneRanges = createCloneRangesFor(scaffold);
        final CoverageMap<CoverageRegion<Range>> coverage = DefaultCoverageMap.buildCoverageMap(cloneRanges);
        for (final CoverageRegion<Range> region : coverage.getRegions()) {
            final int coverageDepth = region.getCoverage();
            if (coverageDepth > 0 && coverageDepth < this.minCoverage) {
                final Range ungappedCloneCoverageRegionRange = region.asRange();
                ScaffoldFeature scaffoldFeature = new DefaultScaffoldFeature(CommonFeatureTypes.LOWCLONECOVERAGE, scaffold.getId(), ungappedCloneCoverageRegionRange);
                Map<String, List<Range>> placedContigRanges = new HashMap<String, List<Range>>();
                for (CoverageRegion<PlacedContig> contigRegion : scaffold.getContigCoverageMap().getRegionsWhichIntersect(ungappedCloneCoverageRegionRange)) {
                    for (PlacedContig contig : contigRegion) {
                        String id = contig.getContigId();
                        if (!placedContigRanges.containsKey(id)) {
                            placedContigRanges.put(id, new ArrayList<Range>());
                        }
                        Range ungappedContigRange = Range.createOfLength(ungappedCloneCoverageRegionRange.getBegin() - contig.getBegin(), ungappedCloneCoverageRegionRange.getLength());
                        placedContigRanges.get(id).add(ungappedContigRange);
                    }
                }
                DefaultCompositeFeature.Builder compositeFeatureBuilder = new DefaultCompositeFeature.Builder(scaffoldFeature);
                for (Entry<String, List<Range>> entry : placedContigRanges.entrySet()) {
                    String contigId = entry.getKey();
                    NucleotideSequence consensus;
                    try {
                        consensus = getContigDataStore().get(contigId).getConsensus();
                    } catch (DataStoreException e) {
                        throw new FeatureFinderException("error querying contig datastore for " + contigId, e);
                    }
                    for (Range ungappedContigRange : Ranges.merge(entry.getValue())) {
                        Range gappedContigRange = Range.create(consensus.getGappedOffsetFor((int) ungappedContigRange.getBegin()), consensus.getGappedOffsetFor((int) ungappedContigRange.getEnd()));
                        compositeFeatureBuilder.add(new DefaultContigFeature(CommonFeatureTypes.LOWCLONECOVERAGE, contigId, gappedContigRange, ungappedContigRange));
                    }
                }
                foundFeature(compositeFeatureBuilder.build());
            }
        }
    }

    private List<Range> createCloneRangesFor(Scaffold scaffold) throws FeatureFinderException {
        return createCloneRangesFor(scaffold, getContigDataStore());
    }

    private List<Range> createCloneRangesFor(Scaffold scaffold, ContigDataStore<PR, C> contigDatastore) throws FeatureFinderException {
        Map<String, List<Range>> cloneCoverageRangeMap = createCloneCoverageRangeMapFor(scaffold, contigDatastore);
        final List<Range> cloneRanges = new ArrayList<Range>();
        for (final List<Range> ranges : cloneCoverageRangeMap.values()) {
            cloneRanges.addAll(Ranges.merge(ranges));
        }
        return cloneRanges;
    }

    protected abstract boolean isSangerRead(String readId);

    protected abstract String getCloneNameFor(String readId);

    private Map<String, List<Range>> createCloneCoverageRangeMapFor(Scaffold scaffold, ContigDataStore<PR, C> contigDatastore) throws FeatureFinderException {
        Map<String, List<Range>> cloneCoverageRangeMap = new HashMap<String, List<Range>>();
        try {
            for (String contigId : IteratorIterableAdapter.createIterableAdapterFor(scaffold.getContigIds())) {
                Contig<? extends PlacedRead> contig = contigDatastore.get(contigId);
                for (final PlacedRead read : contig.getPlacedReads()) {
                    String readId = read.getId();
                    if (isSangerRead(readId)) {
                        final String clone = getCloneNameFor(readId);
                        if (!cloneCoverageRangeMap.containsKey(clone)) {
                            cloneCoverageRangeMap.put(clone, new ArrayList<Range>());
                        }
                        Range readRange = read.asRange();
                        Range scaffoldRange = ScaffoldUtil.convertGappedContigRangeToUngappedScaffoldRange(contig, readRange, scaffold);
                        cloneCoverageRangeMap.get(clone).add(scaffoldRange);
                    }
                }
            }
        } catch (DataStoreException e) {
            throw new FeatureFinderException("error trying to access contig datastore", e);
        }
        return cloneCoverageRangeMap;
    }

    @Override
    public void endFeatureFinding() {
    }

    @Override
    public void findFeaturesFor(SampleData<PR, C> sampleData) throws FeatureFinderException {
        Scaffold scaffold = sampleData.getCodingRegionedScaffold().getScaffold();
        ContigDataStore<PR, C> contigDataStore = sampleData.getContigDataStore();
        List<Range> cloneRanges = createCloneRangesFor(scaffold, contigDataStore);
        final CoverageMap<CoverageRegion<Range>> coverage = DefaultCoverageMap.buildCoverageMap(cloneRanges);
        for (final CoverageRegion<Range> region : coverage.getRegions()) {
            final int coverageDepth = region.getCoverage();
            if (coverageDepth > 0 && coverageDepth < this.minCoverage) {
                final Range ungappedCloneCoverageRegionRange = region.asRange();
                ScaffoldFeature scaffoldFeature = new DefaultScaffoldFeature(CommonFeatureTypes.LOWCLONECOVERAGE, scaffold.getId(), ungappedCloneCoverageRegionRange);
                Map<String, List<Range>> placedContigRanges = new HashMap<String, List<Range>>();
                for (CoverageRegion<PlacedContig> contigRegion : scaffold.getContigCoverageMap().getRegionsWhichIntersect(ungappedCloneCoverageRegionRange)) {
                    for (PlacedContig contig : contigRegion) {
                        String id = contig.getContigId();
                        if (!placedContigRanges.containsKey(id)) {
                            placedContigRanges.put(id, new ArrayList<Range>());
                        }
                        Range ungappedContigRange = Range.createOfLength(ungappedCloneCoverageRegionRange.getBegin() - contig.getBegin(), ungappedCloneCoverageRegionRange.getLength());
                        placedContigRanges.get(id).add(ungappedContigRange);
                    }
                }
                DefaultCompositeFeature.Builder compositeFeatureBuilder = new DefaultCompositeFeature.Builder(scaffoldFeature);
                for (Entry<String, List<Range>> entry : placedContigRanges.entrySet()) {
                    String contigId = entry.getKey();
                    NucleotideSequence consensus;
                    try {
                        consensus = contigDataStore.get(contigId).getConsensus();
                    } catch (DataStoreException e) {
                        throw new FeatureFinderException("error querying contig datastore for " + contigId, e);
                    }
                    for (Range ungappedContigRange : Ranges.merge(entry.getValue())) {
                        Range gappedContigRange = Range.create(consensus.getGappedOffsetFor((int) ungappedContigRange.getBegin()), consensus.getGappedOffsetFor((int) ungappedContigRange.getEnd()));
                        compositeFeatureBuilder.add(new DefaultContigFeature(CommonFeatureTypes.LOWCLONECOVERAGE, contigId, gappedContigRange, ungappedContigRange));
                    }
                }
                foundFeature(compositeFeatureBuilder.build());
            }
        }
    }
}
