package com.vayoodoot.ui.img;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.net.URL;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: May 23, 2007
 * Time: 10:06:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageFactory {

    private static HashMap imageCache = new HashMap();

    public static String DIR_IMAGE = "folder.png";

    public static String SHARED_DIR_IMAGE = "shared_folder.png";

    public static String FILE_IMAGE = "file.png";

    public static String SHARED_FILE_IMAGE = "shared_file.png";

    public static String BUDDY_ICON = "buddy.png";

    public static String BUDDY_TRAY_ICON = "systray_small.gif";

    public static String FOLDER_ICON = "folder.gif";

    public static BufferedImage getImage(String imageName) throws IOException {
        BufferedImage image = (BufferedImage) imageCache.get(imageName);
        if (image == null) {
            image = loadCompatibleImage(ImageFactory.class.getResource("images/" + imageName));
            imageCache.put(imageName, image);
        }
        return image;
    }

    public static ImageIcon getImageIcon(String imageName) {
        ImageIcon image = (ImageIcon) imageCache.get(imageName);
        if (image == null) {
            image = new ImageIcon(ImageFactory.class.getResource("images/" + imageName));
            imageCache.put(imageName, image);
        }
        return image;
    }

    private static BufferedImage loadCompatibleImage(URL resource) throws IOException {
        BufferedImage image = ImageIO.read(resource);
        return image;
    }

    public static BufferedImage getImage(byte[] bytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }

    public static void main(String args[]) throws Exception {
        String dir = "C:\\sachin\\work\\shoonya\\svn\\trunk\\fileshare\\psd\\";
        FileOutputStream fos = new FileOutputStream(new File(dir, "online_small.jpg"));
        fos.write(resize(dir + "online.jpg", 30));
        fos.close();
    }

    public static byte[] resize(String original, int maxSize) throws IOException {
        File originalFile = new File(original);
        Image i = ImageIO.read(originalFile);
        Image resizedImage = null;
        int iWidth = i.getWidth(null);
        int iHeight = i.getHeight(null);
        if (iWidth > iHeight) {
            resizedImage = i.getScaledInstance(maxSize, (maxSize * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = i.getScaledInstance((maxSize * iWidth) / iHeight, maxSize, Image.SCALE_SMOOTH);
        }
        Image temp = new ImageIcon(resizedImage).getImage();
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    public static byte[] resize1(String original, int maxSize) throws IOException {
        File originalFile = new File(original);
        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
        Image i = ii.getImage();
        Image resizedImage = null;
        int iWidth = i.getWidth(null);
        int iHeight = i.getHeight(null);
        if (iWidth > iHeight) {
            resizedImage = i.getScaledInstance(maxSize, (maxSize * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = i.getScaledInstance((maxSize * iWidth) / iHeight, maxSize, Image.SCALE_SMOOTH);
        }
        Image temp = new ImageIcon(resizedImage).getImage();
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
        com.sun.image.codec.jpeg.JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
        param.setQuality(0.5f, true);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(bufferedImage);
        return outputStream.toByteArray();
    }
}
