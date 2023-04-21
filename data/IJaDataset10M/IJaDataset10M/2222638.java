package edu.isi.misd.image.gateway.conversion.test;

import org.junit.Test;
import edu.isi.misd.image.gateway.conversion.ImageConversionFactory;
import edu.isi.misd.image.gateway.conversion.UnsupportedConversionException;

public class ImageConversionFactoryTest {

    private static final String source = ConvertImageTest.class.getResource("/test.jpg").getPath();

    private static final String destination = "converted.tiff";

    @Test(expected = IllegalArgumentException.class)
    public void testGetImageConversion_null1stArg() throws Exception {
        ImageConversionFactory.getImageConversion(null, destination);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetImageConversion_null2ndArg() throws Exception {
        ImageConversionFactory.getImageConversion(source, null);
    }

    @Test(expected = UnsupportedConversionException.class)
    public void testGetImageConversion_badSource() throws Exception {
        ImageConversionFactory.getImageConversion("myfile.badext", destination);
    }

    @Test(expected = UnsupportedConversionException.class)
    public void testGetImageConversion_badDest() throws Exception {
        ImageConversionFactory.getImageConversion(source, "myfile.badext");
    }
}
