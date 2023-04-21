package net.sf.croputils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GenerateDemoImages {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("src/test/testimages/slinky.jpg");
            int w = 133;
            FileOutputStream out;
            out = new FileOutputStream("src/test/testimages/slinky-basicdownscale-w" + w + ".jpg");
            CropUtil.basicDownscaleJpeg(fis, out, w, -1, 0.8f);
            out.close();
            fis = new FileInputStream("src/test/testimages/slinky.jpg");
            out = new FileOutputStream("src/test/testimages/slinky-fastdownscale-w" + w + ".jpg");
            CropUtil.fastDownscaleJpeg(fis, out, w, -1, 0.8f);
            out.close();
            fis = new FileInputStream("src/test/testimages/slinky.jpg");
            out = new FileOutputStream("src/test/testimages/slinky-gooddownscale-w" + w + ".jpg");
            CropUtil.scaleJpeg(fis, out, w, -1, 0.8f);
            out.close();
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
