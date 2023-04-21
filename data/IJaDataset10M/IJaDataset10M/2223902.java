package br.com.arsmachina.image.entity;

import org.testng.annotations.Test;
import br.com.arsmachina.image.enums.ImageFormat;

/**
 * Test class for {@link Image}.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ImageTest {

    /**
	 * Tests the {@link Image#setName(String)} method.
	 */
    @Test
    public void testSetName() {
        Image image = new Image();
        image.setName("lalala.jpg");
        assert image.getFormat() == ImageFormat.JPG;
        image.setName("la.lala.jpg");
        assert image.getFormat() == ImageFormat.JPG;
        image.setName("la.lala.JPg");
        assert image.getFormat() == ImageFormat.JPG;
        image.setName("la.lala.BMP");
        assert image.getFormat() == ImageFormat.BMP;
        image.setName("la.lala.bmp");
        assert image.getFormat() == ImageFormat.BMP;
        image.setName("la.lala.GIF");
        assert image.getFormat() == ImageFormat.GIF;
        image.setName("la.lala.gif");
        assert image.getFormat() == ImageFormat.GIF;
        boolean ok = false;
        try {
            image.setName(null);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        assert ok;
        ok = false;
        try {
            image.setName("nothing.www");
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        assert ok;
    }
}
