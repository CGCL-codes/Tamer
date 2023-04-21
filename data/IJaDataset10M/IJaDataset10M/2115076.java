package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf;

import java.util.Arrays;
import java.util.Map;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.BasicChromatogramBuilder;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.Chromatogram;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.Confidence;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.DefaultConfidence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.core.util.Builder;

/**
 * {@code SCFChromatogramBuilder} uses the Builder pattern
 * to create a new {@link SCFChromatogram} instance.
 * @author dkatzel
 *
 *
 */
public final class SCFChromatogramBuilder implements Builder<SCFChromatogram> {

    private byte[] substitutionConfidence;

    private byte[] insertionConfidence;

    private byte[] deletionConfidence;

    private byte[] privateData;

    private final BasicChromatogramBuilder basicBuilder;

    public SCFChromatogramBuilder(String id) {
        basicBuilder = new BasicChromatogramBuilder(id);
    }

    public SCFChromatogramBuilder(Chromatogram copy) {
        basicBuilder = new BasicChromatogramBuilder(copy);
    }

    public SCFChromatogramBuilder(SCFChromatogram copy) {
        this((Chromatogram) copy);
        substitutionConfidence(copy.getSubstitutionConfidence().getData());
        deletionConfidence(copy.getDeletionConfidence().getData());
        insertionConfidence(copy.getInsertionConfidence().getData());
        privateData(copy.getPrivateData().getData().array());
    }

    /**
     * @return the substitutionConfidence
     */
    public byte[] substitutionConfidence() {
        return substitutionConfidence == null ? null : Arrays.copyOf(substitutionConfidence, substitutionConfidence.length);
    }

    /**
     * @param substitutionConfidence the substitutionConfidence to set
     */
    public SCFChromatogramBuilder substitutionConfidence(byte[] substitutionConfidence) {
        this.substitutionConfidence = substitutionConfidence == null ? null : Arrays.copyOf(substitutionConfidence, substitutionConfidence.length);
        return this;
    }

    /**
     * @return the insertionConfidence
     */
    public byte[] insertionConfidence() {
        return insertionConfidence == null ? null : Arrays.copyOf(insertionConfidence, insertionConfidence.length);
    }

    /**
     * @param insertionConfidence the insertionConfidence to set
     */
    public SCFChromatogramBuilder insertionConfidence(byte[] insertionConfidence) {
        this.insertionConfidence = insertionConfidence == null ? null : Arrays.copyOf(insertionConfidence, insertionConfidence.length);
        return this;
    }

    /**
     * @return the deletionConfidence
     */
    public byte[] deletionConfidence() {
        return deletionConfidence == null ? null : Arrays.copyOf(deletionConfidence, deletionConfidence.length);
    }

    /**
     * @param deletionConfidence the deletionConfidence to set
     */
    public SCFChromatogramBuilder deletionConfidence(byte[] deletionConfidence) {
        this.deletionConfidence = deletionConfidence == null ? null : Arrays.copyOf(deletionConfidence, deletionConfidence.length);
        return this;
    }

    /**
     * @return the privateData
     */
    public byte[] privateData() {
        return privateData == null ? null : Arrays.copyOf(privateData, privateData.length);
    }

    /**
     * @param privateData the privateData to set
     */
    public SCFChromatogramBuilder privateData(byte[] privateData) {
        this.privateData = privateData == null ? null : Arrays.copyOf(privateData, privateData.length);
        return this;
    }

    public SCFChromatogram build() {
        Chromatogram basicChromo = basicBuilder.build();
        return new SCFChromatogramImpl(basicChromo, createOptionalConfidence(substitutionConfidence()), createOptionalConfidence(insertionConfidence()), createOptionalConfidence(deletionConfidence()), createPrivateData());
    }

    private Confidence createOptionalConfidence(byte[] confidence) {
        if (confidence == null) {
            return null;
        }
        return new DefaultConfidence(confidence);
    }

    private PrivateData createPrivateData() {
        if (privateData() == null) {
            return null;
        }
        return new PrivateData(privateData());
    }

    public final short[] peaks() {
        return basicBuilder.peaks();
    }

    public SCFChromatogramBuilder peaks(short[] peaks) {
        basicBuilder.peaks(peaks);
        return this;
    }

    public final NucleotideSequence basecalls() {
        return basicBuilder.basecalls();
    }

    public SCFChromatogramBuilder basecalls(NucleotideSequence basecalls) {
        basicBuilder.basecalls(basecalls);
        return this;
    }

    public final byte[] aConfidence() {
        return basicBuilder.aConfidence();
    }

    public final SCFChromatogramBuilder aConfidence(byte[] confidence) {
        basicBuilder.aConfidence(confidence);
        return this;
    }

    public final byte[] cConfidence() {
        return basicBuilder.cConfidence();
    }

    public final SCFChromatogramBuilder cConfidence(byte[] confidence) {
        basicBuilder.cConfidence(confidence);
        return this;
    }

    public final byte[] gConfidence() {
        return basicBuilder.gConfidence();
    }

    public final SCFChromatogramBuilder gConfidence(byte[] confidence) {
        basicBuilder.gConfidence(confidence);
        return this;
    }

    public final byte[] tConfidence() {
        return basicBuilder.tConfidence();
    }

    public final SCFChromatogramBuilder tConfidence(byte[] confidence) {
        basicBuilder.tConfidence(confidence);
        return this;
    }

    public final short[] aPositions() {
        return basicBuilder.aPositions();
    }

    public final SCFChromatogramBuilder aPositions(short[] positions) {
        basicBuilder.aPositions(positions);
        return this;
    }

    public final short[] cPositions() {
        return basicBuilder.cPositions();
    }

    public final SCFChromatogramBuilder cPositions(short[] positions) {
        basicBuilder.cPositions(positions);
        return this;
    }

    public final short[] gPositions() {
        return basicBuilder.gPositions();
    }

    public final SCFChromatogramBuilder gPositions(short[] positions) {
        basicBuilder.gPositions(positions);
        return this;
    }

    public final short[] tPositions() {
        return basicBuilder.tPositions();
    }

    public final SCFChromatogramBuilder tPositions(short[] positions) {
        basicBuilder.tPositions(positions);
        return this;
    }

    public final Map<String, String> properties() {
        return basicBuilder.properties();
    }

    public final SCFChromatogramBuilder properties(Map<String, String> properties) {
        basicBuilder.properties(properties);
        return this;
    }
}
