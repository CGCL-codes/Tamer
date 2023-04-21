package org.dcm4chee.xero.wado;

import static org.dcm4chee.xero.wado.DicomImageFilter.calculateDesiredSubsamplingFactorForOneDimension;
import static org.dcm4chee.xero.wado.DicomImageFilter.calculateFinalSizeFromSubsampling;
import static org.dcm4chee.xero.wado.DicomImageFilter.calculateSubregionFromRegionFloatArray;
import static org.dcm4chee.xero.wado.DicomImageFilter.isPowerOfTwo;
import static org.dcm4chee.xero.wado.DicomImageFilter.updateParamFromRegion;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageReadParam;
import javax.imageio.metadata.IIOMetadata;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author lpeters
 *
 */
public class DicomImageFilterTest {

    @Test
    public void calculateFinalSizeFromSubsamplingTest_InsertSaneParameters_ReturnNewSize() {
        int[] startSizes = new int[] { 512, 511, 513, 133, 2 };
        int[] subSampleIndices = new int[] { 2, 1, 200, 13, 5 };
        int[][] finalSizes = new int[][] { { 256, 512, 3, 40, 103 }, { 256, 511, 3, 40, 103 }, { 257, 513, 3, 40, 103 }, { 67, 133, 1, 11, 27 }, { 1, 2, 1, 1, 1 } };
        for (int i = 0; i < startSizes.length; i++) {
            for (int j = 0; j < subSampleIndices.length; j++) {
                assertEquals(calculateFinalSizeFromSubsampling(startSizes[i], subSampleIndices[j]), finalSizes[i][j]);
            }
        }
    }

    @Test
    public void calculateFinalSizeFromSubsamplingTest_InsertZeroSubSample_ReturnStartSize() {
        Assert.assertEquals(calculateFinalSizeFromSubsampling(256, 0), 256);
    }

    @Test
    public void isPowerOfTwoTest_GivenPowersOfTwo_ReturnTrue() {
        for (int i = 0; i < 31; i++) {
            assertTrue(isPowerOfTwo(1 << i));
        }
    }

    @Test
    public void isPowerOfTwoTest_GivenNotPowersOfTwo_ReturnFalse() {
        assertFalse(isPowerOfTwo(7));
        assertFalse(isPowerOfTwo(5));
        assertFalse(isPowerOfTwo(3));
        assertFalse(isPowerOfTwo(0));
        assertFalse(isPowerOfTwo(-2));
        for (int i = 3; i < 31; i++) {
            assertFalse(isPowerOfTwo((1 << i) + 1));
            assertFalse(isPowerOfTwo((1 << i) - 1));
            assertFalse(isPowerOfTwo((1 << i) + (1 << (i - 1))));
        }
    }

    @Test
    public void calculateDesiredSubsamplingFactorForOneDimensionTest_GivenData_OutputSubsampleFactorGivesSizeGreaterOrEqualToDesiredSize() {
        for (int startSize = 16; startSize < 69; startSize++) {
            for (int desiredSize = 1; desiredSize < 70; desiredSize++) {
                int subsampleFactor = calculateDesiredSubsamplingFactorForOneDimension(startSize, desiredSize);
                int finalSize = calculateFinalSizeFromSubsampling(startSize, subsampleFactor);
                int smallerThanDesiredSize = calculateFinalSizeFromSubsampling(startSize, subsampleFactor * 2);
                String msg = "startSize= " + startSize + ", desiredSize= " + desiredSize;
                if (desiredSize > startSize) {
                    assertTrue(finalSize == startSize, msg);
                    assertTrue((smallerThanDesiredSize < desiredSize) || ((smallerThanDesiredSize == desiredSize) && (smallerThanDesiredSize == finalSize)), msg);
                } else {
                    assertTrue(finalSize >= desiredSize, msg);
                    assertTrue((smallerThanDesiredSize < desiredSize) || ((smallerThanDesiredSize == desiredSize) && (smallerThanDesiredSize == finalSize)), msg);
                }
            }
        }
    }

    @Test
    public void calculateSubregionFromRegionFloatArrayTest() {
        float[] regionFull = { 0.0f, 0.0f, 1.0f, 1.0f };
        for (int fullWidth = 500; fullWidth < 533; fullWidth++) {
            int fullHeight = fullWidth + 4979;
            Rectangle rect = calculateSubregionFromRegionFloatArray(regionFull, fullWidth, fullHeight);
            Rectangle expectedRect = new Rectangle(fullWidth, fullHeight);
            assertEquals(rect, expectedRect, "fullWidth= " + fullWidth);
        }
        float[] region = new float[4];
        int fullWidth = 511;
        int fullHeight = 2542;
        System.arraycopy(regionFull, 0, region, 0, 4);
        for (float x0 = -0.1f; x0 <= 1.1; x0 += 0.003f) {
            region[0] = x0;
            Rectangle rect = calculateSubregionFromRegionFloatArray(region, fullWidth, fullHeight);
            if (x0 < 0.0f) {
                assertEquals(rect.x, 0);
            } else if (x0 >= 1.0f) {
                assertTrue(rect.isEmpty());
            } else {
                assertEquals((int) Math.round(x0 * fullWidth), rect.x);
            }
        }
        System.arraycopy(regionFull, 0, region, 0, 4);
        final float y1Test = 0.8f;
        region[3] = y1Test;
        for (float y0 = -0.1f; y0 <= 1.1; y0 += 0.003f) {
            region[1] = y0;
            Rectangle rect = calculateSubregionFromRegionFloatArray(region, fullWidth, fullHeight);
            if (y0 < 0.0f) {
                assertEquals(rect.y, 0);
            } else if (y0 >= y1Test) {
                assertTrue(rect.isEmpty());
            } else {
                assertEquals((int) Math.round(y0 * fullHeight), rect.y);
            }
        }
        System.arraycopy(regionFull, 0, region, 0, 4);
        for (float x1 = -0.1f; x1 <= 1.1; x1 += 0.003f) {
            region[2] = x1;
            Rectangle rect = calculateSubregionFromRegionFloatArray(region, fullWidth, fullHeight);
            if (x1 < region[0]) {
                assertTrue(rect.isEmpty());
            } else if (x1 >= 1.0f) {
                assertEquals(rect.width, fullWidth);
            } else {
                assertEquals((int) Math.round(x1 * fullWidth), rect.width);
            }
        }
        System.arraycopy(regionFull, 0, region, 0, 4);
        final float y0Test = 0.11f;
        region[1] = y0Test;
        for (float y1 = -0.1f; y1 <= 1.1; y1 += 0.003f) {
            region[3] = y1;
            Rectangle rect = calculateSubregionFromRegionFloatArray(region, fullWidth, fullHeight);
            if (y1 < region[1]) {
                assertTrue(rect.isEmpty());
            } else if (y1 >= 1.0f) {
                assertEquals(rect.height, (int) Math.round((1.0f - y0Test) * fullHeight));
            } else {
                assertEquals((int) Math.round((y1 - y0Test) * fullHeight), rect.height, "here.");
            }
        }
    }

    @Test
    public void updateParamFromRegionTest_GivenRowsColumnsAndRegion_ReturnStringWithSubsampleAndCropRect() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(WadoParams.ROWS, 512);
        params.put(WadoParams.COLUMNS, 64);
        params.put(WadoParams.REGION, new float[] { 0.25f, 0, 0.75f, 1.0f });
        int width = 1024;
        int height = 2048;
        String ret = updateParamFromRegion(new ImageReadParam(), params, width, height);
        assertEquals(ret, "-s8,4-r256,0,512,2048");
    }

    protected Element makeRangeNode(int smallestPixelValue, int largestPixelValue, int bitsAfterResampling) throws ParserConfigurationException {
        final String nodeName = "pixelRange";
        Element pixelRangeNode;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document document = parser.newDocument();
        pixelRangeNode = document.createElement(nodeName);
        pixelRangeNode.setAttribute(ReduceBitsFilter.SMALLEST_IMAGE_PIXEL_VALUE, String.valueOf(smallestPixelValue));
        pixelRangeNode.setAttribute(ReduceBitsFilter.LARGEST_IMAGE_PIXEL_VALUE, String.valueOf(largestPixelValue));
        pixelRangeNode.setAttribute(ReduceBitsFilter.REDUCED_BITS, String.valueOf(bitsAfterResampling));
        return pixelRangeNode;
    }

    protected Element makeTransferSyntaxNode(String transferSyntax, int subsampleX, int subsampleY) throws ParserConfigurationException {
        final String nodeName = "transferSyntax";
        Element rawTransferSyntaxNode;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document document = parser.newDocument();
        rawTransferSyntaxNode = document.createElement(nodeName);
        rawTransferSyntaxNode.setAttribute("transferSyntax", transferSyntax);
        rawTransferSyntaxNode.setAttribute("subsampleX", String.valueOf(subsampleX));
        rawTransferSyntaxNode.setAttribute("subsampleY", String.valueOf(subsampleY));
        return rawTransferSyntaxNode;
    }

    @Test
    public void setPixelRangeInformationTest() throws IOException, ParserConfigurationException {
        WadoImage wi = new WadoImage(null, 0, null);
        DicomImageFilter dicomFilter = new DicomImageFilter();
        final String nodeName = "pixelRange";
        Element pixelRangeNode = makeRangeNode(-1000, 7889, 12);
        IIOMetadata metadataMock = createNiceMock(IIOMetadata.class);
        expect(metadataMock.getAsTree(eq(nodeName))).andReturn(pixelRangeNode);
        replay(metadataMock);
        DicomImageReader readerMock = createNiceMock(DicomImageReader.class);
        expect(readerMock.getImageMetadata(0)).andReturn(metadataMock);
        replay(readerMock);
        wi = dicomFilter.setPixelRangeInformation(readerMock, 0, wi);
        String filename = wi.getFilename();
        assertTrue(filename.contains("-pixelRange-1000,7889,12"));
        assertEquals(-1000, ReduceBitsFilter.getPreviousSmallestPixelValue(wi));
        assertEquals(7889, ReduceBitsFilter.getPreviousLargestPixelValue(wi));
        assertEquals(12, ReduceBitsFilter.getPreviousReducedBits(wi));
    }

    @Test
    public void testGetReaderRawSubsampleIndices() throws ParserConfigurationException, IOException {
        DicomImageFilter dicomFilter = new DicomImageFilter();
        final String nodeName = "transferSyntax";
        Element rawTransferSyntaxNode = makeTransferSyntaxNode(UID.JPEGExtended24, 2, 4);
        IIOMetadata metadataMock = createNiceMock(IIOMetadata.class);
        expect(metadataMock.getAsTree(eq(nodeName))).andReturn(rawTransferSyntaxNode);
        replay(metadataMock);
        DicomImageReader readerMock = createNiceMock(DicomImageReader.class);
        expect(readerMock.getImageMetadata(0)).andReturn(metadataMock);
        replay(readerMock);
        int frame = 0;
        Point subsample = dicomFilter.getReaderRawSubsampleIndices(readerMock, frame);
        assertEquals(2, subsample.x);
        assertEquals(4, subsample.y);
    }

    @Test
    public void testGetReaderRawTransferSyntax_whenIIOMetadataIsAvailable_shouldReturnRawTransferSyntaxFromMetadata() throws ParserConfigurationException, IOException {
        DicomImageFilter dicomFilter = new DicomImageFilter();
        final String nodeName = "transferSyntax";
        Element rawTransferSyntaxNode = makeTransferSyntaxNode(UID.JPEGExtended24, 2, 4);
        IIOMetadata metadataMock = createNiceMock(IIOMetadata.class);
        expect(metadataMock.getAsTree(eq(nodeName))).andReturn(rawTransferSyntaxNode);
        replay(metadataMock);
        DicomImageReader readerMock = createNiceMock(DicomImageReader.class);
        expect(readerMock.getImageMetadata(0)).andReturn(metadataMock);
        replay(readerMock);
        int frame = 0;
        String tsUID = dicomFilter.getReaderRawTransferSyntax(null, readerMock, frame);
        assertEquals(UID.JPEGExtended24, tsUID);
    }

    @Test
    public void testGetReaderRawTransferSyntax_whenIIOMetadataIsNotAvailable_shouldReturnRawTransferSyntaxFromDicomHeader() throws ParserConfigurationException, IOException {
        DicomImageFilter dicomFilter = new DicomImageFilter();
        DicomImageReader readerMock = createNiceMock(DicomImageReader.class);
        expect(readerMock.getImageMetadata(0)).andReturn(null);
        replay(readerMock);
        DicomObject header = new BasicDicomObject();
        header.putString(Tag.TransferSyntaxUID, VR.UI, UID.JPEGExtended24);
        int frame = 0;
        String tsUID = dicomFilter.getReaderRawTransferSyntax(header, readerMock, frame);
        assertEquals(UID.JPEGExtended24, tsUID);
    }

    @Test
    public void testGetReadAsRawBytes_whenSupportedTransferSyntaxStringsAreSubsetsOrSupersetsOfTheReturnedOne_ShouldReturnFalse() {
        Point subsampleForTest = new Point(2, 4);
        DicomImageFilter dicomFilter = new DicomImageFilter() {

            protected Point getReaderRawSubsampleIndices(DicomImageReader reader, int frame) {
                return new Point(2, 4);
            }

            protected String getReaderRawTransferSyntax(DicomObject ds, DicomImageReader reader, int frame) {
                return "1.2.4.222";
            }
        };
        ImageReadParam readParamMock = createNiceMock(ImageReadParam.class);
        expect(readParamMock.getSourceXSubsampling()).andStubReturn(subsampleForTest.x);
        expect(readParamMock.getSourceYSubsampling()).andStubReturn(subsampleForTest.y);
        replay(readParamMock);
        Map<String, Object> params = new HashMap<String, Object>();
        ArrayList<String> allowedTsList = new ArrayList<String>();
        allowedTsList.add("1.2");
        allowedTsList.add("1.2.4");
        allowedTsList.add("1.2.4.222");
        params.put(WadoImage.IMG_AS_BYTES_ONLY_FOR_TRANSFER_SYNTAXES, allowedTsList);
        assertTrue(dicomFilter.getReadAsRawBytes(params, null, readParamMock, 0, null));
        params = new HashMap<String, Object>();
        allowedTsList.remove("1.2.4.222");
        allowedTsList.add("1.2.4.222.3");
        assertFalse(dicomFilter.getReadAsRawBytes(params, null, readParamMock, 0, null));
    }
}
