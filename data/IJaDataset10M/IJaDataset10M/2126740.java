package fecchi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author rcc4
 */
public class Main implements Observer {

    DownloaderFactory df;

    URL testURL;

    URL locURL;

    String myName;

    ArrayList<Downloader> downloaders;

    public Main(String[] args) {
        this.df = df.getInstance();
        this.downloaders = new ArrayList<Downloader>();
        if (args.length != 2) {
            System.out.println("Error, For testing please supply a URL on the command line");
            System.out.println("java Main <URL> <PATH TO SAVE>");
            return;
        }
        try {
            testURL = new URL(args[0]);
            locURL = new URL(args[1]);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        TransferRequest tr = new TransferRequest(testURL, locURL);
        Downloader downloader = df.getDownloader(tr);
        if (downloader == null) {
            System.out.println("Sorry, " + tr.getUrl().getProtocol() + " Not supported yet");
            return;
        }
        try {
            downloader.prep(tr);
        } catch (DownloaderException ex) {
            ex.printStackTrace();
        }
        this.downloaders.add(downloader);
        downloader.getTransferRequest().addObserver(this);
        Thread t1 = new Thread(downloader);
        t1.start();
        System.out.println("Thread Spawned - Waiting");
        try {
            this.wait(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Exiting...");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(args.length + " Array Elements");
        new Main(args);
    }

    public void update(Observable o, Object arg) {
        System.out.println("Thread at " + o + "Reports: " + (String) arg);
    }
}
