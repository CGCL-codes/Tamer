package skanque;

import org.apache.log4j.Logger;
import skanque.gui.DownloaderSplash;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

class WomenNameListDownloader implements Runnable {

    private static Logger logger = Logger.getLogger(WomenNameListDownloader.class);

    private final boolean forceUpdate;

    public WomenNameListDownloader(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void run() {
        String alphabet = "q w e r t y u i o p a s d f g h j k l z x c v b n m æ ø å";
        logger.info("initiating women names downloader thread");
        int line = 0;
        long start = System.currentTimeMillis();
        try {
            File f = new File(Application.getSettings().getHome() + "/skanque_women.skqr");
            if (f.exists() && !forceUpdate) {
                logger.info("women file already exists, terminating.");
                return;
            }
            FileOutputStream fout = new FileOutputStream(f);
            PrintStream p = new PrintStream(fout);
            String[] strings = alphabet.split(" ");
            Arrays.sort(strings);
            DownloaderSplash downloaderSplash = new DownloaderSplash(strings.length);
            Application.centralizeComponent(downloaderSplash);
            downloaderSplash.setVisible(true);
            for (int i = 0; i < strings.length; i++) {
                String current = strings[i];
                String get = "http://www.b-a-b-y.dk/artikler/navngiv/database/km-p_3.asp?Forbogstav=" + current;
                URL url = new URL(get);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                Scanner sc = new Scanner(con.getInputStream());
                String find = "onFocus=this.blur() class=nav>";
                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    if (s.contains(find) && !s.startsWith("<IMG") && !s.contains("<FONT COLOR=#CF0000>")) {
                        int index = s.indexOf(find) + find.length();
                        int lastIndex = s.indexOf("</", index);
                        s = s.substring(index, lastIndex);
                        if (!s.contains("<IMG")) {
                            p.println(s);
                            line++;
                        }
                    }
                }
                double v = (double) i * 100 / (double) strings.length;
                downloaderSplash.tick(v);
                logger.info("downloaded " + v + "%");
            }
            p.close();
            fout.close();
            long stop = System.currentTimeMillis();
            logger.info("women file downloaded succesfully in " + (stop - start) / 1000 + " seconds");
            downloaderSplash.dispose();
        } catch (Exception e) {
            logger.error("error occured downloading women list", e);
        }
    }
}
