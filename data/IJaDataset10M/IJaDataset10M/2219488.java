package com.shine.framework.SafeCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class Example {

    public static void main(String args[]) throws Exception {
        String code = "http://3n.gxsti.net/gx3n/aspsite/inc/getCode.asp";
        for (int i = 0; i < 100; i++) {
            BufferedImage image = ImageIO.read(new URL(code));
            String path = "e:\\123\\key" + i + ".bmp";
            String format = path.substring(path.indexOf('.') + 1);
            ImageIO.write(image, format, new File(path));
            String result = SafeCodeUtils.filter(new SafeCodeUtils().recognizeText(new File(path), format));
            if (result.length() == 4) {
                System.out.println(i + ":" + result);
            }
        }
    }
}
