package xmage.raster.codec.test;

import xmage.raster.Image;
import xmage.raster.RGBImage;
import xmage.raster.codec.PNMInputCodec;
import xmage.raster.codec.TGAInputCodec;
import huf.misc.tester.Tester;
import java.io.File;
import java.io.IOException;

public class TGAInputCodecTest {

    public TGAInputCodecTest(Tester t) {
        t.testClass(new TGAInputCodec());
        try {
            rgb(t);
        } catch (IOException ioe) {
            System.out.println("Tests failed!");
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private void rgb(Tester t) throws IOException {
        Image i = null;
        RGBImage gi = null;
        TGAInputCodec codec = new TGAInputCodec();
        byte[] pixels = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 100, (byte) 100, (byte) 100, (byte) 200, (byte) 200, (byte) 200, (byte) 255, (byte) 0, (byte) 0, (byte) 0, (byte) 255, (byte) 0, (byte) 0, (byte) 0, (byte) 255, (byte) 200, (byte) 0, (byte) 0, (byte) 0, (byte) 200, (byte) 0, (byte) 0, (byte) 0, (byte) 200, (byte) 255, (byte) 255, (byte) 0, (byte) 255, (byte) 0, (byte) 255, (byte) 0, (byte) 255, (byte) 255, (byte) 200, (byte) 200, (byte) 100, (byte) 200, (byte) 100, (byte) 200, (byte) 100, (byte) 200, (byte) 200 };
        byte[] cpppixels = new PNMInputCodec().read(new File("xmage/raster/codec/test/images/tga/cpp.ppm")).getBuffer().array();
        byte[] ppubpixels = new PNMInputCodec().read(new File("xmage/raster/codec/test/images/tga/ppub.ppm")).getBuffer().array();
        i = codec.read(new File("xmage/raster/codec/test/images/tga/gimp_24_nc_bottomup.tga"));
        t.test("rgb01 gimp nc bottomup", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb02 gimp nc bottomup", pixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/gimp_24_nc_topdown.tga"));
        t.test("rgb03 gimp nc topdown", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb04 gimp nc topdown", pixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/cpp_24_nc_norm.tga"));
        t.test("rgb05 cpp nc norm", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb06 cpp nc norm", cpppixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/cpp_24_nc_ext.tga"));
        t.test("rgb07 cpp nc ext", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb08 cpp nc ext", cpppixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/ppub_24.tga"));
        t.test("rgb0A ppub", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb0B ppub", ppubpixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/gimp_24_rle_bottomup.tga"));
        t.test("rgb09 gimp nc bottomup", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb10 gimp nc bottomup", pixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/gimp_24_rle_topdown.tga"));
        t.test("rgb11 gimp nc bottomup", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb12 gimp nc bottomup", pixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/cpp_24_rle_norm.tga"));
        t.test("rgb13 cpp nc norm", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb14 cpp nc norm", cpppixels, gi.getBuffer().array());
        i = codec.read(new File("xmage/raster/codec/test/images/tga/cpp_24_rle_ext.tga"));
        t.test("rgb15 cpp nc ext", i instanceof RGBImage);
        gi = (RGBImage) i;
        t.test("rgb16 cpp nc ext", cpppixels, gi.getBuffer().array());
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        new TGAInputCodecTest(t);
        t.totals();
    }
}
