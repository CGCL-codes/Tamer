package org.jcvi.common.core.seq.read.trace.sanger.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.jcvi.common.core.symbol.Sequence;
import org.jcvi.common.core.symbol.ShortSymbol;
import org.jcvi.common.core.symbol.ShortGlyphFactory;
import org.jcvi.common.core.symbol.pos.SangerPeak;
import org.jcvi.common.core.symbol.qual.QualitySequence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;

public class ArtificialPhd implements Phd {

    private static final ShortGlyphFactory PEAK_FACTORY = ShortGlyphFactory.getInstance();

    /**
     * The Position of the first peak in a Newbler created
     * fake 454 phd record.
     */
    private static final int NEWBLER_454_START_POSITION = 15;

    /**
     * The number of positions between every basecall
     * in a Newbler created fake 454 phd record.
     */
    private static final int NEWBLER_454_PEAK_SPACING = 19;

    private final NucleotideSequence basecalls;

    private final QualitySequence qualities;

    private final Properties comments;

    private final List<PhdTag> tags;

    private SangerPeak fakePositions = null;

    private final int numberOfPositionsForEachPeak;

    private final int numberOfBases;

    private final int positionOfFirstPeak;

    private final String id;

    /**
    * Create an {@link ArtificialPhd} record that matches
    * the way Newbler creates phd records for 454 reads.
    * This is needed so tools like consed will correctly
    * space the fake chromatograms for 454 reads since it uses
    * 454 developed tools which rely on this spacing.
    * @param id the id for this Phd.
    * @param basecalls the basecalls for this Phd.
    * @param qualities the qualities for this Phd.
    * @param comments the comments for this Phd.
    * @param tags the {@link PhdTag}s for this Phd.
    * @return a new {@link ArtificialPhd} which has position data that matches
    * what would have been created by Newbler.
    */
    public static ArtificialPhd createNewbler454Phd(String id, NucleotideSequence basecalls, QualitySequence qualities, Properties comments, List<PhdTag> tags) {
        return new ArtificialPhd(id, basecalls, qualities, comments, tags, NEWBLER_454_START_POSITION, NEWBLER_454_PEAK_SPACING);
    }

    /**
    * Create an {@link ArtificialPhd} record that matches
    * the way Newbler creates phd records for 454 reads.
    * This is needed so tools like consed will correctly
    * space the fake chromatograms for 454 reads since it uses
    * 454 developed tools which rely on this spacing.
    * @param id the id for this Phd.
    * @param basecalls the basecalls for this Phd.
    * @param qualities the qualities for this Phd.
    * @param comments the comments for this Phd.
    * @return a new {@link ArtificialPhd} which has position data that matches
    * what would have been created by Newbler.
    */
    public static ArtificialPhd createNewbler454Phd(String id, NucleotideSequence basecalls, QualitySequence qualities, Properties comments) {
        return ArtificialPhd.createNewbler454Phd(id, basecalls, qualities, comments, Collections.<PhdTag>emptyList());
    }

    /**
    * Create an {@link ArtificialPhd} record that matches
    * the way Newbler creates phd records for 454 reads.
    * This is needed so tools like consed will correctly
    * space the fake chromatograms for 454 reads since it uses
    * 454 developed tools which rely on this spacing.
    * @param id the id for this Phd.
    * @param basecalls the basecalls for this Phd.
    * @param qualities the qualities for this Phd.
    * @return a new {@link ArtificialPhd} which has position data that matches
    * what would have been created by Newbler.
    */
    public static ArtificialPhd createNewbler454Phd(String id, NucleotideSequence basecalls, QualitySequence qualities) {
        return ArtificialPhd.createNewbler454Phd(id, basecalls, qualities, new Properties(), Collections.<PhdTag>emptyList());
    }

    /**
    * {@code buildArtificalPhd} creates a {@link DefaultPhd}
    * using the given basecalls and qualities
    * but creates artificial peak data spacing each
    * peak {@code numberOfPositionsForEachPeak} apart.
    * This Phd will have no comments and no {@link PhdTag}s.
    * This method is the same as calling
    * {@link #buildArtificalPhd(NucleotideSequence, Sequence, Properties, List, int)
    * buildArtificalPhd(basecalls, qualities, new Properties(),Collections.<PhdTag>emptyList(),numberOfPositionsForEachPeak)}
    * @param id the id for this Phd.
    * @param basecalls the basecalls for this Phd.
    * @param qualities the qualities for this Phd.
    * @param numberOfPositionsForEachPeak number of positions each
    * peak should be separated as.
    * @return a new DefaultPhd using the given values.
    * @see #buildArtificalPhd(NucleotideSequence, Sequence, Properties, List, int)
    */
    public ArtificialPhd(String id, NucleotideSequence basecalls, QualitySequence qualities, int numberOfPositionsForEachPeak) {
        this(id, basecalls, qualities, new Properties(), Collections.<PhdTag>emptyList(), numberOfPositionsForEachPeak);
    }

    /**
    * {@code buildArtificalPhd} creates a {@link DefaultPhd}
    * using the given basecalls and qualities, comments and tags
    * but creates artificial peak data spacing each
    * peak {@code numberOfPositionsForEachPeak} apart.
    * @param id the id for this Phd.
    * @param basecalls the basecalls for this Phd.
    * @param qualities the qualities for this Phd.
    * @param comments the comments for this Phd.
    * @param tags the {@link PhdTag}s for this Phd.
    * @param numberOfPositionsForEachPeak number of positions each
    * peak should be separated as.
    * @return a new DefaultPhd using the given values.
    */
    public ArtificialPhd(String id, NucleotideSequence basecalls, QualitySequence qualities, Properties comments, List<PhdTag> tags, int numberOfPositionsForEachPeak) {
        this(id, basecalls, qualities, comments, tags, numberOfPositionsForEachPeak, numberOfPositionsForEachPeak);
    }

    /**
     * {@code buildArtificalPhd} creates a {@link DefaultPhd}
     * using the given basecalls and qualities, comments and tags
     * but creates artificial peak data spacing each
     * peak {@code numberOfPositionsForEachPeak} apart.
     * @param id the id for this Phd.
     * @param basecalls the basecalls for this Phd.
     * @param qualities the qualities for this Phd.
     * @param comments the comments for this Phd.
     * @param tags the {@link PhdTag}s for this Phd.
     * @param numberOfPositionsForEachPeak number of positions each
     * peak should be separated as.
     * @return a new DefaultPhd using the given values.
     */
    public ArtificialPhd(String id, NucleotideSequence basecalls, QualitySequence qualities, Properties comments, List<PhdTag> tags, int positionOfFirstPeak, int numberOfPositionsForEachPeak) {
        this.id = id;
        this.basecalls = basecalls;
        this.qualities = qualities;
        this.tags = tags;
        this.comments = comments;
        this.numberOfBases = (int) basecalls.getLength();
        this.numberOfPositionsForEachPeak = numberOfPositionsForEachPeak;
        this.positionOfFirstPeak = positionOfFirstPeak;
    }

    @Override
    public Properties getComments() {
        return comments;
    }

    @Override
    public List<PhdTag> getTags() {
        return tags;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getNumberOfTracePositions() {
        if (numberOfBases == 0) {
            return 0;
        }
        return (numberOfBases + 1) * numberOfPositionsForEachPeak;
    }

    @Override
    public synchronized SangerPeak getPeaks() {
        if (fakePositions == null) {
            List<ShortSymbol> fakePositions = new ArrayList<ShortSymbol>(numberOfBases);
            for (int i = 0; i < numberOfBases; i++) {
                fakePositions.add(PEAK_FACTORY.getGlyphFor(i * numberOfPositionsForEachPeak + positionOfFirstPeak));
            }
            this.fakePositions = new SangerPeak(fakePositions);
        }
        return fakePositions;
    }

    @Override
    public NucleotideSequence getNucleotideSequence() {
        return basecalls;
    }

    @Override
    public QualitySequence getQualities() {
        return qualities;
    }
}
