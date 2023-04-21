package com.triplyx.volume;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.UUID;
import junit.framework.TestCase;

public class ThirdVolumeFactoryTest extends TestCase {

    public void testWritesCorrectRawDataToVolume() throws IOException {
    }

    public void testReadsHardCodedVolume() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, "8859_1"));
        writeln(writer, "#Triplyx Volume");
        writeln(writer, "#" + new Date().toString());
        writeln(writer, "Member=2");
        writeln(writer, "StripeSize=2");
        writeln(writer, "VolumeUUID=41764bce-d168-49f6-a269-f6c74aa4e1b1");
        writeln(writer, "OverallUUID=57be101e-5e0b-49b6-94ec-cb3bb1a154df");
        writeln(writer, "Version=2.0");
        writer.flush();
        baos.write(0);
        baos.write(72);
        baos.write(101);
        baos.write(108);
        baos.write(108);
        baos.write(111);
        baos.write(32);
        baos.write(49);
        baos.write(50);
        baos.write(51);
        baos.close();
        StorageView testStorageView = new StorageView() {

            public InputStream getInputStream() {
                return new ByteArrayInputStream(baos.toByteArray());
            }

            public OutputStream getOutputStream() {
                fail("Should not get here");
                return null;
            }

            public boolean isWriteable() {
                return false;
            }
        };
        ThirdVolumeFactory fbtvf = new ThirdVolumeFactory();
        ThirdVolume tv = fbtvf.openThirdVolume(testStorageView);
        assertNotNull(tv);
        assertEquals(ThirdVolume.MEMBER_D2B_AND_A, tv.getMember());
        assertEquals(2, tv.getStripeSize());
        assertFalse(tv.isWriteable());
        assertEquals("57be101e-5e0b-49b6-94ec-cb3bb1a154df", tv.getOverallVolumeIdentifier().toString());
        assertEquals("41764bce-d168-49f6-a269-f6c74aa4e1b1", tv.getVolumeIdentifier().toString());
        assertEquals("2.0", tv.getVersionIdentifier());
    }

    public void testRejectsIfNoZeroByteAfterHeader() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, "8859_1"));
        writeln(writer, "#Triplyx Volume");
        writeln(writer, "#" + new Date().toString());
        writeln(writer, "Member=2");
        writeln(writer, "StripeSize=2");
        writeln(writer, "VolumeUUID=41764bce-d168-49f6-a269-f6c74aa4e1b1");
        writeln(writer, "OverallUUID=57be101e-5e0b-49b6-94ec-cb3bb1a154df");
        writeln(writer, "Version=2.0");
        writer.flush();
        baos.write(72);
        baos.write(101);
        baos.write(108);
        baos.write(108);
        baos.write(111);
        baos.write(32);
        baos.write(49);
        baos.write(50);
        baos.write(51);
        baos.close();
        StorageView testStorageView = new StorageView() {

            public InputStream getInputStream() {
                return new ByteArrayInputStream(baos.toByteArray());
            }

            public OutputStream getOutputStream() {
                fail("Should not get here");
                return null;
            }

            public boolean isWriteable() {
                return false;
            }
        };
        ThirdVolumeFactory fbtvf = new ThirdVolumeFactory();
        try {
            fbtvf.openThirdVolume(testStorageView);
            fail("Did not throw IOException");
        } catch (IOException e) {
            assertEquals("Reached end of input before finding terminating zero byte", e.getMessage());
        }
    }

    private void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    public void testRefusesNonsenseMember() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        try {
            tvf.createThirdVolume(0, 1024, new UUID(12345678L, 87654321L), new TestStorageView("/mnt/usb/vol2"));
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRefusesNonsenseStripeSize() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        try {
            tvf.createThirdVolume(ThirdVolume.MEMBER_D2B_AND_A, 0, new UUID(12345678L, 87654321L), new TestStorageView("/mnt/usb/vol2"));
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRefusesNullUUID() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        try {
            tvf.createThirdVolume(ThirdVolume.MEMBER_D2B_AND_A, 512, null, new TestStorageView("/mnt/usb/vol2"));
        } catch (NullPointerException e) {
        }
    }

    public void testRefusesNullStorage() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        try {
            tvf.createThirdVolume(ThirdVolume.MEMBER_D2B_AND_A, 512, new UUID(12345678L, 87654321L), null);
        } catch (NullPointerException e) {
        }
    }

    public void testHeaderFinishesWithZeroByte() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        ThirdVolume volumeDAandB = tvf.createThirdVolume(ThirdVolume.MEMBER_D2B_AND_A, 1024, new UUID(12345678L, 87654321L), new TestStorageView("/mnt/usb/vol2"));
        InputStream rawInputStream = volumeDAandB.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int input = rawInputStream.read();
            if (input == -1) break;
            baos.write(input);
        }
        byte[] capture = baos.toByteArray();
        assertTrue(capture.length > 32);
        assertTrue((capture[capture.length - 1] == (byte) 0));
    }

    public void testCreatesVolumes() throws IOException {
        ThirdVolumeFactory tvf = new ThirdVolumeFactory();
        final UUID overallUUID = new UUID(12345678L, 87654321L);
        ThirdVolume volumeD1AandB = tvf.createThirdVolume(ThirdVolume.MEMBER_D1A_AND_B, 512, overallUUID, new TestStorageView("/mnt/usb/vol1"));
        assertNotNull(volumeD1AandB);
        assertEquals(ThirdVolume.MEMBER_D1A_AND_B, volumeD1AandB.getMember());
        assertEquals(512, volumeD1AandB.getStripeSize());
        assertTrue(volumeD1AandB.isWriteable());
        assertEquals(overallUUID, volumeD1AandB.getOverallVolumeIdentifier());
        assertNotNull(volumeD1AandB.getVolumeIdentifier());
        assertFalse(overallUUID.equals(volumeD1AandB.getVolumeIdentifier()));
        ThirdVolume volumeD2BandA = tvf.createThirdVolume(ThirdVolume.MEMBER_D2B_AND_A, 1024, overallUUID, new TestStorageView("/mnt/usb/vol2"));
        assertNotNull(volumeD2BandA);
        assertEquals(ThirdVolume.MEMBER_D2B_AND_A, volumeD2BandA.getMember());
        assertEquals(1024, volumeD2BandA.getStripeSize());
        assertTrue(volumeD2BandA.isWriteable());
        assertEquals(overallUUID, volumeD2BandA.getOverallVolumeIdentifier());
        assertNotNull(volumeD2BandA.getVolumeIdentifier());
        assertFalse(overallUUID.equals(volumeD1AandB.getVolumeIdentifier()));
        ThirdVolume volumeD1D2AandD1D2B = tvf.createThirdVolume(ThirdVolume.MEMBER_D1D2A_AND_D1D2B, 8, overallUUID, new TestStorageView("/mnt/usb/vol3"));
        assertNotNull(volumeD1D2AandD1D2B);
        assertEquals(ThirdVolume.MEMBER_D1D2A_AND_D1D2B, volumeD1D2AandD1D2B.getMember());
        assertEquals(8, volumeD1D2AandD1D2B.getStripeSize());
        assertTrue(volumeD1D2AandD1D2B.isWriteable());
        assertEquals(overallUUID, volumeD1D2AandD1D2B.getOverallVolumeIdentifier());
        assertNotNull(volumeD1D2AandD1D2B.getVolumeIdentifier());
        assertFalse(overallUUID.equals(volumeD1D2AandD1D2B.getVolumeIdentifier()));
    }
}
