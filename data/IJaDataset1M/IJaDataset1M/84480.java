package org.jcvi.common.core.assembly.asm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jcvi.common.core.Direction;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.assembly.AbstractContig;
import org.jcvi.common.core.assembly.ContigBuilder;
import org.jcvi.common.core.assembly.PlacedReadBuilder;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequenceBuilder;

/**
 * @author dkatzel
 *
 *
 */
public final class DefaultAsmContig extends AbstractContig<AsmPlacedRead> implements AsmContig {

    private final boolean isDegenerate;

    public static AsmContigBuilder createBuilder(String id, NucleotideSequence consensus) {
        return createBuilder(id, consensus, false);
    }

    public static AsmContigBuilder createBuilder(String id, NucleotideSequence consensus, boolean isDegenerate) {
        return new DefaultAsmContigBuilder(id, consensus, isDegenerate);
    }

    private DefaultAsmContig(String id, NucleotideSequence consensus, Set<AsmPlacedRead> reads, boolean isDegenerate) {
        super(id, consensus, reads);
        this.isDegenerate = isDegenerate;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isDegenerate() {
        return isDegenerate;
    }

    private static class DefaultAsmContigBuilder implements AsmContigBuilder {

        private NucleotideSequence fullConsensus;

        private final NucleotideSequenceBuilder mutableConsensus;

        private String contigId;

        private final Map<String, AsmPlacedReadBuilder> aceReadBuilderMap = new HashMap<String, AsmPlacedReadBuilder>();

        boolean isDegenerate;

        DefaultAsmContigBuilder(String id, NucleotideSequence consensus, boolean isDegenerate) {
            this.contigId = id;
            this.fullConsensus = consensus;
            this.mutableConsensus = new NucleotideSequenceBuilder(fullConsensus);
            this.isDegenerate = isDegenerate;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public ContigBuilder<AsmPlacedRead, AsmContig> setContigId(String contigId) {
            this.contigId = contigId;
            return this;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public String getContigId() {
            return contigId;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public int numberOfReads() {
            return aceReadBuilderMap.size();
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public ContigBuilder<AsmPlacedRead, AsmContig> addRead(AsmPlacedRead placedRead) {
            return addRead(placedRead.getId(), placedRead.getNucleotideSequence().toString(), (int) placedRead.getGappedContigStart(), placedRead.getDirection(), placedRead.getValidRange(), placedRead.getUngappedFullLength(), placedRead.isRepeatSurrogate());
        }

        /**
          * {@inheritDoc}
          */
        @Override
        public AsmContigBuilder addRead(String readId, String validBases, int offset, Direction dir, Range clearRange, int ungappedFullLength, boolean isSurrogate) {
            aceReadBuilderMap.put(readId, DefaultAsmPlacedRead.createBuilder(this.fullConsensus, readId, validBases, offset, dir, clearRange, ungappedFullLength, isSurrogate));
            return this;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public ContigBuilder<AsmPlacedRead, AsmContig> addAllReads(Iterable<AsmPlacedRead> reads) {
            for (AsmPlacedRead read : reads) {
                addRead(read);
            }
            return this;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public Collection<? extends PlacedReadBuilder<AsmPlacedRead>> getAllPlacedReadBuilders() {
            return aceReadBuilderMap.values();
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public PlacedReadBuilder<AsmPlacedRead> getPlacedReadBuilder(String readId) {
            return aceReadBuilderMap.get(readId);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public void removeRead(String readId) {
            aceReadBuilderMap.remove(readId);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public NucleotideSequenceBuilder getConsensusBuilder() {
            return mutableConsensus;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public AsmContig build() {
            Set<AsmPlacedRead> reads = new HashSet<AsmPlacedRead>(aceReadBuilderMap.size() + 1);
            for (AsmPlacedReadBuilder builder : aceReadBuilderMap.values()) {
                reads.add(builder.build());
            }
            aceReadBuilderMap.clear();
            return new DefaultAsmContig(contigId, mutableConsensus.build(), reads, isDegenerate);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public void setDegenerate(boolean isDegenerate) {
            this.isDegenerate = isDegenerate;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public boolean isDegenerate() {
            return isDegenerate;
        }
    }
}
