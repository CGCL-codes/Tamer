package superpodder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import superpodder.gui.MainView;

public class SuperPodder {

    /**
     * Main class of the rssfeed manager.
     * 
     * @param args
     */
    public static void main(String[] args) {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println("URL file names " + urls[i].getFile());
        }
        final String path = System.getProperty("user.dir");
        try {
            final File file = new File(path + "\\SuperPodder.lock");
            if (file.exists()) {
                System.out.println("Lock exists!");
                if (file.delete()) {
                    System.out.println("Lock deleted!");
                } else {
                    System.out.println("An instance of this application is already running.");
                    System.exit(0);
                }
            }
        } catch (final Exception nop) {
        }
        FileWriter lock;
        try {
            lock = new FileWriter(path + "\\SuperPodder.lock");
        } catch (final IOException nop) {
        }
        final MainView view = new MainView();
        view.showCentered();
    }
}
