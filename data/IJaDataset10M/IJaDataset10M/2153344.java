package org.jcvi.trace.sanger.chromatogram.scf.section;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.jcvi.trace.sanger.chromatogram.scf.PrivateData;
import org.jcvi.trace.sanger.chromatogram.scf.SCFChromatogram;
import org.jcvi.trace.sanger.chromatogram.scf.header.SCFHeader;
import org.jcvi.trace.sanger.chromatogram.scf.section.EncodedSection;
import org.jcvi.trace.sanger.chromatogram.scf.section.PrivateDataCodec;
import org.jcvi.trace.sanger.chromatogram.scf.section.Section;
import org.jcvi.trace.sanger.chromatogram.scf.section.SectionEncoder;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TestPrivateDataEncoder {

    private byte[] data = new byte[] { 20, 30, 40, -20, -67, 125 };

    private PrivateData privateData = new PrivateData(data);

    SectionEncoder sut = new PrivateDataCodec();

    SCFHeader mockHeader;

    SCFChromatogram c;

    @Before
    public void setup() {
        mockHeader = createMock(SCFHeader.class);
        c = createMock(SCFChromatogram.class);
    }

    @Test
    public void valid() throws IOException {
        expect(c.getPrivateData()).andStubReturn(privateData);
        mockHeader.setPrivateDataSize(data.length);
        replay(mockHeader, c);
        EncodedSection actualEncodedSection = sut.encode(c, mockHeader);
        assertEquals(Section.PRIVATE_DATA, actualEncodedSection.getSection());
        assertArrayEquals(data, actualEncodedSection.getData().array());
        verify(mockHeader, c);
    }

    @Test
    public void nullPrivateDataShouldEncodeEmptySection() throws IOException {
        expect(c.getPrivateData()).andStubReturn(null);
        assertEncodedSectionIsEmpty();
    }

    @Test
    public void privateDataHasNullBufferShouldEncodeEmptySection() throws IOException {
        expect(c.getPrivateData()).andStubReturn(new PrivateData((ByteBuffer) null));
        assertEncodedSectionIsEmpty();
    }

    @Test
    public void emptyPrivateDataShouldEncodeEmptySection() throws IOException {
        expect(c.getPrivateData()).andStubReturn(new PrivateData(new byte[0]));
        assertEncodedSectionIsEmpty();
    }

    private void assertEncodedSectionIsEmpty() throws IOException {
        mockHeader.setPrivateDataSize(0);
        replay(mockHeader, c);
        EncodedSection actualEncodedSection = sut.encode(c, mockHeader);
        assertEquals(Section.PRIVATE_DATA, actualEncodedSection.getSection());
        assertArrayEquals(new byte[0], actualEncodedSection.getData().array());
        verify(mockHeader, c);
    }
}
