package com.drew.metadata.exif.test;

import com.drew.metadata.exif.CanonMakernoteDescriptor;
import com.drew.metadata.exif.CanonMakernoteDirectory;
import junit.framework.TestCase;

public class CanonMakernoteDescriptorTest extends TestCase {

    public CanonMakernoteDescriptorTest(String name) {
        super(name);
    }

    public void testGetFlashBiasDescription() throws Exception {
        CanonMakernoteDirectory directory = new CanonMakernoteDirectory();
        CanonMakernoteDescriptor descriptor = new CanonMakernoteDescriptor(directory);
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0xFFC0);
        assertEquals("-2.0 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0xffd4);
        assertEquals("-1.375 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0000);
        assertEquals("0.0 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x000c);
        assertEquals("0.375 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0010);
        assertEquals("0.5 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0014);
        assertEquals("0.625 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0020);
        assertEquals("1.0 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0030);
        assertEquals("1.5 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0034);
        assertEquals("1.625 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
        directory.setInt(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS, 0x0040);
        assertEquals("2.0 EV", descriptor.getDescription(CanonMakernoteDirectory.TAG_CANON_STATE2_FLASH_BIAS));
    }
}
