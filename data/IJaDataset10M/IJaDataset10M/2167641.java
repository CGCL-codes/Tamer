package com.shine.framework.SafeCode;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分析验证码
 * @author viruscode@gmail.com
 *
 */
public class SafeCodeUtils {

    private final String LANG_OPTION = "-l";

    private final String EOL = System.getProperty("line.separator");

    private String tessPath = new File("tesseract").getAbsolutePath();

    public String recognizeText(File imageFile, String imageFormat) throws Exception {
        File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
        File outputFile = new File(imageFile.getParentFile(), "output");
        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<String>();
        cmd.add("C:\\Program Files\\Java\\tesseract-2.04\\tesseract.exe");
        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
        cmd.add("eng");
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());
        cmd.set(1, tempImage.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int w = process.waitFor();
        System.out.println("Exit value = {}:" + w);
        tempImage.delete();
        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append(EOL);
            }
            in.close();
        } else {
            String msg;
            switch(w) {
                case 1:
                    msg = "Errors accessing files. There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recognize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }
            tempImage.delete();
            throw new RuntimeException(msg);
        }
        new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString();
    }

    public static String filter(String data) {
        String result = "";
        for (int index = 0; index < data.length(); index++) {
            if (data.charAt(index) >= '0' && data.charAt(index) <= '9') result += data.charAt(index);
        }
        return result;
    }
}
