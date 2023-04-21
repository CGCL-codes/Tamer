package eu.annocultor.core.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import javax.imageio.ImageIO;

/**
 * Given a list of files, downloads them and checks for various broken images,
 * such as single-colored images, etc.
 * 
 * @author Borys Omelayenko
 * 
 */
public class ImageDownloader {

    public static final String EXISTING_IMAGES_LOCAL_FILENAME = "_online_status_of_images.txt";

    /**
	 * Downloads all images listed in file <code>list-of-all-images.txt</code>
	 * into the working directory and creates file
	 * <code>list-of-images-assumed-to-exist.txt</code> there.
	 * 
	 * @param args
	 *          0 - URL prefix
	 */
    public static void main(String[] args) throws Exception {
        downloadImages(args[0], EXISTING_IMAGES_LOCAL_FILENAME, ".");
        createListOfImagesThatExist(EXISTING_IMAGES_LOCAL_FILENAME, ".");
    }

    private static void downloadImages(String uriPrefix, String fileWithListOfImages, String outputDir) throws Exception {
        Properties list = new Properties();
        list.load(new FileInputStream(fileWithListOfImages));
        long current = 0;
        long startTime = System.currentTimeMillis();
        System.out.println("Downloading files from " + uriPrefix + " to " + outputDir);
        System.out.println("Total " + list.size() + " files, think of 1 sec per file");
        System.out.println("Images with the same size as the 'no-image' image are ignored and not displayed");
        for (Iterator<Object> it = list.keySet().iterator(); it.hasNext(); ) {
            String urlString = it.next().toString().replace('\\', '/');
            if (urlString.contains("=")) throw new Exception("Symbol '=' found in file names, while it is reserved for denoting '/' in flat-file names");
        }
        for (Iterator<Object> it = list.keySet().iterator(); it.hasNext(); ) {
            String urlString = it.next().toString().replace('\\', '/');
            URL url = new URL(uriPrefix + urlString);
            File file = new File(outputDir + "/" + imageNameToFileName(urlString));
            current++;
            if (!file.exists()) {
                try {
                    BufferedInputStream in = new BufferedInputStream(url.openStream(), 64000);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file), 64000);
                    int c;
                    while ((c = in.read()) != -1) {
                        out.write(c);
                    }
                    out.close();
                    in.close();
                    System.out.print(current + " /" + list.size() + ". " + file.getName() + ", ");
                    System.out.println("saved");
                } catch (Exception e) {
                    System.out.print(current + " /" + list.size() + ". " + file.getName() + ", FAILED, error: " + e.getMessage());
                }
            } else {
                System.out.print(current + " /" + list.size() + ". " + file.getName() + ", ");
                System.out.println("already exists");
            }
            if (current / 100 == current / 100.0) {
                long delta = (System.currentTimeMillis() - startTime);
                System.out.println("Total time " + delta + " ms, " + (delta / current) + " ms/file");
            }
        }
        long delta = (System.currentTimeMillis() - startTime);
        System.out.println("Total time " + delta + " ms, " + (delta / list.size()) + " ms/file");
    }

    private static void createListOfImagesThatExist(String fileWithListOfImages, String outputDir) throws Exception {
        Properties list = new Properties();
        list.load(new FileInputStream(fileWithListOfImages));
        long cntTotal = 0;
        long cntOneColor = 0;
        long cntMissing = 0;
        long cntNoImage = 0;
        long cntOk = 0;
        for (Object fileNameObject : list.keySet()) {
            String fileName = fileNameObject.toString();
            if (!list.getProperty(fileName, "bad").equals("ok")) {
                cntTotal++;
                System.out.print("Analyzing file: " + cntTotal + " " + fileName);
                File file = new File(outputDir + "/" + imageNameToFileName(fileName));
                if (file.exists() && file.isFile()) {
                    if (file.length() > 0 && file.length() != 2425) {
                        BufferedImage image = ImageIO.read(file);
                        int width = image.getWidth(null);
                        int height = image.getHeight(null);
                        int firstPixel = -1;
                        boolean sameColor = true;
                        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
                            int pixel = image.getRGB(x, y);
                            if (x == 0 && y == 0) {
                                firstPixel = pixel;
                            } else {
                                if (firstPixel != pixel) {
                                    sameColor = false;
                                }
                            }
                        }
                        if (sameColor) {
                            list.setProperty(fileName, "bad, one color");
                            cntOneColor++;
                        } else {
                            list.setProperty(fileName, "ok");
                            cntOk++;
                        }
                    } else {
                        list.setProperty(fileName, "bad, no-image");
                        cntNoImage++;
                    }
                } else {
                    list.setProperty(fileName, "bad, missing");
                    cntMissing++;
                }
                System.out.println(list.getProperty(fileName));
                if (cntTotal / 100 == cntTotal / 100.0) System.out.print("*");
                if (cntTotal / 1000 == cntTotal / 1000.0) System.out.println();
            }
        }
        list.store(new FileOutputStream(fileWithListOfImages), String.format("Local image file names and their online status, only 'ok' is okey, " + "\n#total %d, ok %d, missing %d, noimage %d", cntTotal, cntOk, cntMissing, cntNoImage));
    }

    private static String imageNameToFileName(String fileName) {
        return fileName.replace('\\', '/').replace('/', '=');
    }
}
