package org.jscsi.scsi.protocol;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import org.jscsi.scsi.protocol.inquiry.StandardInquiryData;
import org.junit.BeforeClass;
import org.junit.Test;

public class StandardInquiryDataTest {

    private static final String DEFAULT_PACKAGE = "org.jscsi.scsi.protocol.inquiry";

    private static String INQUIRY_DATA_NO_VENDOR_SPECIFIC_1 = "StandardInquiryData," + "PeripheralQualifier=3:[0b000;0b001;0b010;0b011]," + "PeripheralDeviceType=5:0x00," + "RMB=1:0x00," + "reserved=7:0x00," + "Version=8:[0x00;0x03;0x04;0x05]," + "reserved=2:0x00," + "NormACA=1:0x01," + "HiSup=1:0x00," + "ResponseDataFormat=4:0x2," + "AdditionalLength=8:0x5B," + "SCCS=1:0x01," + "ACC=1:0x00," + "TPGS=2:[0b00;0b01;0b10;0b11]," + "ThreePC=1:0x01," + "reserved=2:0x00," + "Protect=1:0x00," + "BQue=1:0x01," + "EncServ=1:0x00," + "VS1=1:0x00," + "MultiP=1:0x01," + "MChngr=1:0x00," + "reserved=7:0x00," + "Linked=1:0x01," + "reserved=1:0x00," + "CmdQue=1:0x00," + "VS2=1:0x00," + "reserved=64:0x00," + "reserved=128:0x00," + "reserved=32:0x00," + "reserved=160:0x00," + "reserved=16:0x00," + "VersionDescriptor1=16:random," + "VersionDescriptor2=16:random," + "VersionDescriptor3=16:random," + "VersionDescriptor4=16:random," + "VersionDescriptor5=16:random," + "VersionDescriptor6=16:random," + "VersionDescriptor7=16:random," + "VersionDescriptor8=16:random," + "reserved=176:0x00";

    private static String INQUIRY_DATA_NO_VENDOR_SPECIFIC_2 = "StandardInquiryData," + "PeripheralQualifier=3:std," + "PeripheralDeviceType=5:0x00," + "RMB=1:0x01," + "reserved=7:0x00," + "Version=8:std," + "reserved=2:0x00," + "NormACA=1:0x00," + "HiSup=1:0x01," + "ResponseDataFormat=4:0x2," + "AdditionalLength=8:0x5B," + "SCCS=1:0x00," + "ACC=1:0x01," + "TPGS=2:std," + "ThreePC=1:0x00," + "reserved=2:0x00," + "Protect=1:0x01," + "BQue=1:0x00," + "EncServ=1:0x01," + "VS1=1:0x00," + "MultiP=1:0x00," + "MChngr=1:0x01," + "reserved=7:0x00," + "Linked=1:0x00," + "reserved=1:0x00," + "CmdQue=1:0x01," + "VS2=1:0x00," + "reserved=64:0x00," + "reserved=128:0x00," + "reserved=32:0x00," + "reserved=160:0x00," + "reserved=16:0x00," + "VersionDescriptor1=16:random," + "VersionDescriptor2=16:random," + "VersionDescriptor3=16:random," + "VersionDescriptor4=16:random," + "VersionDescriptor5=16:random," + "VersionDescriptor6=16:random," + "VersionDescriptor7=16:random," + "VersionDescriptor8=16:random," + "reserved=176:0x00";

    private static Serializer serializer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        serializer = new StandardInquiryData();
    }

    private void runTest(String specification) {
        try {
            new SerializerTest(serializer, DEFAULT_PACKAGE, specification).runTest();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void parseInquiryDataNoVendorSpecific1() {
        runTest(INQUIRY_DATA_NO_VENDOR_SPECIFIC_1);
    }

    @Test
    public void parseInquiryDataNoVendorSpecific2() {
        runTest(INQUIRY_DATA_NO_VENDOR_SPECIFIC_2);
    }

    @Test
    public void testT10VendorIdentification() throws IOException {
        byte[] t10VendorIdentification = new byte[8];
        Random random = new Random();
        random.nextBytes(t10VendorIdentification);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeLong(0);
        dataOut.write(t10VendorIdentification);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        StandardInquiryData sid = new StandardInquiryData();
        sid.decode(ByteBuffer.wrap(byteOut.toByteArray()));
        sid.decode(ByteBuffer.wrap(sid.encode()));
        byte[] returnedT10VendorIdentification = sid.getT10VendorIdentification();
        assertTrue(Arrays.equals(returnedT10VendorIdentification, t10VendorIdentification));
    }

    @Test
    public void testProductIdentification() throws IOException {
        byte[] productIdentification = new byte[16];
        Random random = new Random();
        random.nextBytes(productIdentification);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.write(productIdentification);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        StandardInquiryData sid = new StandardInquiryData();
        sid.decode(ByteBuffer.wrap(byteOut.toByteArray()));
        sid.decode(ByteBuffer.wrap(sid.encode()));
        byte[] returnedProductIdentification = sid.getProductIdentification();
        assertTrue(Arrays.equals(returnedProductIdentification, productIdentification));
    }

    @Test
    public void testProductRevisionLevel() throws IOException {
        byte[] productRevisionLevel = new byte[4];
        Random random = new Random();
        random.nextBytes(productRevisionLevel);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.write(productRevisionLevel);
        dataOut.writeInt(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        dataOut.writeLong(0);
        StandardInquiryData sid = new StandardInquiryData();
        sid.decode(ByteBuffer.wrap(byteOut.toByteArray()));
        sid.decode(ByteBuffer.wrap(sid.encode()));
        byte[] returnedProductRevisionLevel = sid.getProductRevisionLevel();
        assertTrue(Arrays.equals(returnedProductRevisionLevel, productRevisionLevel));
    }
}
