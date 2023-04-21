package com.zwl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Drawpng {

    public static void createPng(String fileName) throws Exception {
        InputStream inputStream = new FileInputStream(new File(fileName));
        JpdlModel jpdlModel = new JpdlModel(inputStream);
        ImageIO.write(new JpdlModelDrawer().draw(jpdlModel), "png", new File(fileName.substring(0, fileName.length() - 9) + ".png"));
    }
}
