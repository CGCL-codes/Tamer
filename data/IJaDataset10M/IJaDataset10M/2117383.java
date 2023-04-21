package imageUtils;

import libs.*;
import java.awt.image.*;
import javax.imageio.ImageReader;

public class imageLoaderThread {

    private int taskLen;

    private int curr = 0;

    private String outMsg = "";

    private String fileName;

    private RenderedImage outImage;

    /** Creates new evaluatorPlugin */
    public imageLoaderThread(int tl, String fn) {
        this.taskLen = tl;
        this.fileName = fn;
    }

    public imageLoaderThread(String fn) {
        this.taskLen = 100;
        this.fileName = fn;
    }

    public void go() {
        curr = 0;
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                return new loadImage();
            }
        };
        worker.start();
    }

    public boolean done() {
        if (curr >= taskLen) {
            return true;
        } else {
            return false;
        }
    }

    private void setCurr(int c) {
        this.curr = c;
    }

    private void setMsg(String msg) {
        this.outMsg = msg;
    }

    public int getTaskLen() {
        return taskLen;
    }

    public int getCurr() {
        return curr;
    }

    public void stop() {
        curr = taskLen;
    }

    public String getMessage() {
        return outMsg;
    }

    public RenderedImage getOutImage() {
        return outImage;
    }

    class loadImage {

        loadImage() {
            ImageReader reader = imageLoader.getReaderOnly(fileName);
            float percentDone = 0.0F;
            try {
                System.out.println("Reading image...");
                outImage = reader.read(0);
            } catch (java.io.IOException ioe) {
                System.out.println("There was an error loading the image" + fileName);
            }
            while (imageLoader.getDone() == false) {
                try {
                    Thread.sleep(5);
                    percentDone = imageLoader.getReadProgress();
                    System.out.println(percentDone);
                    setCurr((int) percentDone);
                    setMsg("Loading Image... ");
                } catch (Exception e) {
                }
            }
            reader.dispose();
            System.out.println("Done");
            setCurr(taskLen);
            setMsg("Done Loading Image!");
        }
    }
}
