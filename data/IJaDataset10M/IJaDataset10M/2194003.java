package net.sf.ovanttasks.ov4native.pefile.codec;

import java.awt.Toolkit;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.imageio.ImageIO;
import net.sf.ovanttasks.ov4native.pefile.res.IconFile;
import org.junit.Ignore;
import org.junit.Test;

public class IcoCodecTest {

    @Ignore
    @Test
    public void test_1bpp_1bit_alpha_2slot() throws Exception {
        IconFile icoFile = IcoCodec.readIcoFile(IcoCodecTest.class.getResourceAsStream("test_1bpp_1bit_alpha_2slot.ico"));
        BufferedImage image = IcoCodec.getBufferedImage(icoFile.data[0]);
        assertEquals(0xff947773, image.getRGB(0, 0));
        assertEquals(0xff947773, image.getRGB(1, 0));
        assertEquals(0xff947773, image.getRGB(2, 0));
        assertEquals(0xff947773, image.getRGB(3, 0));
        assertEquals(0xff947773, image.getRGB(0, 1));
        assertEquals(0xff947773, image.getRGB(1, 1));
        assertEquals(0xff947773, image.getRGB(2, 1));
        assertEquals(0xff947773, image.getRGB(3, 1));
        assertEquals(0xff947773, image.getRGB(0, 2));
        assertEquals(0xff947773, image.getRGB(1, 2));
        assertEquals(0xff947773, image.getRGB(2, 2));
        assertEquals(0xff947773, image.getRGB(3, 2));
        assertEquals(0xff947773, image.getRGB(0, 3));
        assertEquals(0xff947773, image.getRGB(1, 3));
        assertEquals(0xff947773, image.getRGB(2, 3));
        assertEquals(0x00000000, image.getRGB(3, 3));
    }

    @Ignore
    @Test
    public void test_4bpp_1bit_alpha_16slot() throws Exception {
        IconFile icoFile = IcoCodec.readIcoFile(IcoCodecTest.class.getResourceAsStream("test_4bpp_1bit_alpha_16slot.ico"));
        BufferedImage image = IcoCodec.getBufferedImage(icoFile.data[0]);
        ImageIO.write(image, "png", new File("/home/aploese/img-test.png"));
        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xffffffff, image.getRGB(1, 0));
        assertEquals(0xff0000ff, image.getRGB(2, 0));
        assertEquals(0xff00ff00, image.getRGB(3, 0));
        assertEquals(0xffff0000, image.getRGB(0, 1));
        assertEquals(0xffffff00, image.getRGB(1, 1));
        assertEquals(0xff00ffff, image.getRGB(2, 1));
        assertEquals(0xffff00ff, image.getRGB(3, 1));
        assertEquals(0xff808080, image.getRGB(0, 2));
        assertEquals(0xff800000, image.getRGB(1, 2));
        assertEquals(0xff008000, image.getRGB(2, 2));
        assertEquals(0xff000080, image.getRGB(3, 2));
        assertEquals(0xff808000, image.getRGB(0, 3));
        assertEquals(0xff008080, image.getRGB(1, 3));
        assertEquals(0xff800080, image.getRGB(2, 3));
        assertEquals(0x00000000, image.getRGB(3, 3));
    }

    @Ignore
    @Test
    public void test_8bpp_1bit_alpha_256slot() throws Exception {
        IconFile icoFile = IcoCodec.readIcoFile(IcoCodecTest.class.getResourceAsStream("test_8bpp_1bit_alpha_256slot.ico"));
        BufferedImage image = IcoCodec.getBufferedImage(icoFile.data[0]);
        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xffffffff, image.getRGB(1, 0));
        assertEquals(0xff0000ff, image.getRGB(2, 0));
        assertEquals(0xff00ff00, image.getRGB(3, 0));
        assertEquals(0xffff0000, image.getRGB(0, 1));
        assertEquals(0xffffff00, image.getRGB(1, 1));
        assertEquals(0xff00ffff, image.getRGB(2, 1));
        assertEquals(0xffff00ff, image.getRGB(3, 1));
        assertEquals(0xff808080, image.getRGB(0, 2));
        assertEquals(0xff800000, image.getRGB(1, 2));
        assertEquals(0xff008000, image.getRGB(2, 2));
        assertEquals(0xff000080, image.getRGB(3, 2));
        assertEquals(0xff808000, image.getRGB(0, 3));
        assertEquals(0xff008080, image.getRGB(1, 3));
        assertEquals(0xff800080, image.getRGB(2, 3));
        assertEquals(0x00000000, image.getRGB(3, 3));
    }

    @Ignore
    @Test
    public void test_32bpp_8bit_alpha_no_slot() throws Exception {
        IconFile icoFile = IcoCodec.readIcoFile(IcoCodecTest.class.getResourceAsStream("test_32bpp_8bit_alpha_no_slot.ico"));
        BufferedImage image = IcoCodec.getBufferedImage(icoFile.data[0]);
        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xffffffff, image.getRGB(1, 0));
        assertEquals(0xff0000ff, image.getRGB(2, 0));
        assertEquals(0xff00ff00, image.getRGB(3, 0));
        assertEquals(0xffff0000, image.getRGB(0, 1));
        assertEquals(0xffffff00, image.getRGB(1, 1));
        assertEquals(0xff00ffff, image.getRGB(2, 1));
        assertEquals(0xffff00ff, image.getRGB(3, 1));
        assertEquals(0xff808080, image.getRGB(0, 2));
        assertEquals(0xff800000, image.getRGB(1, 2));
        assertEquals(0xff008000, image.getRGB(2, 2));
        assertEquals(0xff000080, image.getRGB(3, 2));
        assertEquals(0xff808000, image.getRGB(0, 3));
        assertEquals(0xff008080, image.getRGB(1, 3));
        assertEquals(0xff800080, image.getRGB(2, 3));
        assertEquals(0x52aaaaaa, image.getRGB(3, 3));
    }

    @Ignore
    @Test
    public void test_8bpp_1bit_alpha_256slot_with_ByteBuffer() throws Exception {
        InputStream is = IcoCodecTest.class.getResourceAsStream("test_8bpp_1bit_alpha_256slot.ico");
        ReadableByteChannel ch = Channels.newChannel(is);
        IconFile icoFile = IcoCodec.readIcoFile(ch);
        BufferedImage image = IcoCodec.getBufferedImage(icoFile.data[0]);
        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xffffffff, image.getRGB(1, 0));
        assertEquals(0xff0000ff, image.getRGB(2, 0));
        assertEquals(0xff00ff00, image.getRGB(3, 0));
        assertEquals(0xffff0000, image.getRGB(0, 1));
        assertEquals(0xffffff00, image.getRGB(1, 1));
        assertEquals(0xff00ffff, image.getRGB(2, 1));
        assertEquals(0xffff00ff, image.getRGB(3, 1));
        assertEquals(0xff808080, image.getRGB(0, 2));
        assertEquals(0xff800000, image.getRGB(1, 2));
        assertEquals(0xff008000, image.getRGB(2, 2));
        assertEquals(0xff000080, image.getRGB(3, 2));
        assertEquals(0xff808000, image.getRGB(0, 3));
        assertEquals(0xff008080, image.getRGB(1, 3));
        assertEquals(0xff800080, image.getRGB(2, 3));
        assertEquals(0x00000000, image.getRGB(3, 3));
        ByteBuffer outBuffer = ByteBuffer.allocate(32000);
        outBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }
}
