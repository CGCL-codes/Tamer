package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.section;

import static org.easymock.EasyMock.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.section.AbstractBasesSectionCodec;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.section.Version3BasesSectionCodec;
import org.jcvi.common.core.testUtil.EasyMockUtil;

public class TestVersion3BasesSection extends AbstractTestBasesSection {

    @Override
    protected ByteBuffer createRequiredExpectedEncodedBases() {
        ByteBuffer result = ByteBuffer.wrap(new byte[(int) encodedBases.getLength() * 12]);
        bulkPutAsInts(result, peaks);
        result.put(aConfidence);
        result.put(cConfidence);
        result.put(gConfidence);
        result.put(tConfidence);
        result.put(DECODED_BASES.getBytes());
        return result;
    }

    @Override
    protected InputStream createValidMockInputStreamWithoutOptionalConfidence(long skipDistance) throws IOException {
        InputStream mockInputStream = createMock(InputStream.class);
        expect(mockInputStream.skip(skipDistance)).andReturn(skipDistance);
        expectPeakReads(mockInputStream);
        expectRequiredConfidenceReads(mockInputStream);
        expectBasesRead(mockInputStream);
        expectEmptyConfidenceData(mockInputStream);
        expectEmptyConfidenceData(mockInputStream);
        expectEmptyConfidenceData(mockInputStream);
        return mockInputStream;
    }

    @Override
    protected InputStream createValidMockInputStreamWithOptionalConfidence(long skipDistance) throws IOException {
        InputStream mockInputStream = createMock(InputStream.class);
        expect(mockInputStream.skip(skipDistance)).andReturn(skipDistance);
        expectPeakReads(mockInputStream);
        expectRequiredConfidenceReads(mockInputStream);
        expectBasesRead(mockInputStream);
        expectFullConfidenceRead(mockInputStream, subsitutionConfidence);
        expectFullConfidenceRead(mockInputStream, insertionConfidence);
        expectFullConfidenceRead(mockInputStream, deletionConfidence);
        return mockInputStream;
    }

    private void expectEmptyConfidenceData(InputStream mockInputStream) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq((int) encodedBases.getLength()))).andAnswer(EasyMockUtil.writeArrayToInputStream(EMPTY_CONFIDENCE));
    }

    private void expectBasesRead(InputStream mockInputStream) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq((int) encodedBases.getLength()))).andAnswer(EasyMockUtil.writeArrayToInputStream(DECODED_BASES.getBytes()));
    }

    private void expectFullConfidenceRead(InputStream mockInputStream, byte[] confidence) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(confidence.length))).andAnswer(EasyMockUtil.writeArrayToInputStream(confidence));
    }

    private void expectRequiredConfidenceReads(InputStream mockInputStream) throws IOException {
        expectFullConfidenceRead(mockInputStream, aConfidence);
        expectFullConfidenceRead(mockInputStream, cConfidence);
        expectFullConfidenceRead(mockInputStream, gConfidence);
        expectFullConfidenceRead(mockInputStream, tConfidence);
    }

    @Override
    protected ByteBuffer createEncodedBasesWithAllOptionalData() {
        final ByteBuffer expectedRequiredExpectedEncodedBases = createRequiredExpectedEncodedBases();
        expectedRequiredExpectedEncodedBases.put(subsitutionConfidence);
        expectedRequiredExpectedEncodedBases.put(insertionConfidence);
        expectedRequiredExpectedEncodedBases.put(deletionConfidence);
        return expectedRequiredExpectedEncodedBases;
    }

    @Override
    protected ByteBuffer createEncodedBasesWithoutSubstutionData() {
        final ByteBuffer expectedRequiredExpectedEncodedBases = createRequiredExpectedEncodedBases();
        expectedRequiredExpectedEncodedBases.put(EMPTY_CONFIDENCE);
        expectedRequiredExpectedEncodedBases.put(insertionConfidence);
        expectedRequiredExpectedEncodedBases.put(deletionConfidence);
        return expectedRequiredExpectedEncodedBases;
    }

    @Override
    protected ByteBuffer createEncodedBasesWithoutDeletionData() {
        final ByteBuffer expectedRequiredExpectedEncodedBases = createRequiredExpectedEncodedBases();
        expectedRequiredExpectedEncodedBases.put(subsitutionConfidence);
        expectedRequiredExpectedEncodedBases.put(insertionConfidence);
        expectedRequiredExpectedEncodedBases.put(EMPTY_CONFIDENCE);
        return expectedRequiredExpectedEncodedBases;
    }

    @Override
    protected ByteBuffer createEncodedBasesWithoutInsertionData() {
        final ByteBuffer expectedRequiredExpectedEncodedBases = createRequiredExpectedEncodedBases();
        expectedRequiredExpectedEncodedBases.put(subsitutionConfidence);
        expectedRequiredExpectedEncodedBases.put(EMPTY_CONFIDENCE);
        expectedRequiredExpectedEncodedBases.put(deletionConfidence);
        return expectedRequiredExpectedEncodedBases;
    }

    @Override
    protected AbstractBasesSectionCodec createAbstractBasesSectionHandler() {
        return new Version3BasesSectionCodec();
    }
}
