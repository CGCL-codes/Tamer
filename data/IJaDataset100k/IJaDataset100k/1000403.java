package Gallery;

import java.util.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

public class LoaderThread extends Thread {

    private Toolkit toolkit;

    private MediaTracker mediaTracker;

    private ConfigFile config;

    private SlideList imageList;

    private boolean isCancelled;

    private boolean isLoading;

    private int percentComplete;

    public LoaderThread(Toolkit toolkit, MediaTracker mediaTracker, SlideList imageList) {
        this.imageList = imageList;
        this.toolkit = toolkit;
        this.mediaTracker = mediaTracker;
    }

    public boolean start(ConfigFile config) {
        isCancelled = false;
        isLoading = false;
        percentComplete = 0;
        this.config = config;
        if (config.getImageCount() > 0) {
            isLoading = true;
            this.start();
            return true;
        } else {
            isLoading = false;
            return false;
        }
    }

    public void cancel() {
        isCancelled = true;
        percentComplete = 100;
    }

    public void run() {
        System.out.println("starting image loader thread...");
        if (config.getConfigFile() != null) {
            for (int i = 0; i < config.getImageCount(); i++) {
                try {
                    System.out.println("loading '" + config.getImageFile(i) + "', " + config.getImageComment(i));
                    percentComplete = i * 100 / config.getImageCount();
                    URL url = new URL(config.getConfigFile(), config.getImageFile(i));
                    Image tmpImg = this.toolkit.getImage(url);
                    this.mediaTracker.addImage(tmpImg, i);
                    this.mediaTracker.waitForID(i);
                    Slide slide = new Slide(tmpImg, config.getImageComment(i), url);
                    imageList.add(slide);
                    if (isCancelled) break;
                } catch (MalformedURLException e) {
                    System.out.println("EXCEPTION: malformed URL: '" + config.getImageFile(i) + "'");
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println("EXCEPTION: connection interrupted: '" + config.getImageFile(i) + "'");
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println("EXCEPTION: IO error: '" + config.getImageFile(i) + "'");
                    System.out.println(e.getMessage());
                }
            }
        }
        isLoading = false;
        percentComplete = 100;
        System.out.println("ending image loader thread...");
    }

    public boolean isLoading() {
        return isLoading;
    }

    public int getPercentage() {
        return percentComplete;
    }
}
