package net.sf.smailstandalone.ui;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * 
 * @since 19.02.2011
 * @author S�bastien CHATEL
 */
public class SmailAppletContext implements AppletContext {

    private static final Log log = LogFactory.getLog(SmailAppletContext.class);

    private static final boolean debugEnabled = log.isDebugEnabled();

    private final Applet applet;

    private final ClassLoader classLoader;

    private final Map<URL, Image> cacheImages = new HashMap<URL, Image>();

    private final Map<URL, AudioClip> cacheAudioClips = new HashMap<URL, AudioClip>();

    private final TaskExecutor browserTaskExecutor;

    private final Pattern urlCleaner = Pattern.compile("[^\\p{Print}]");

    public SmailAppletContext(Applet applet, ClassLoader classLoader) {
        this.applet = applet;
        this.classLoader = classLoader;
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("Browser-");
        taskExecutor.setThreadPriority(Thread.MIN_PRIORITY);
        this.browserTaskExecutor = taskExecutor;
    }

    public void showStatus(String status) {
    }

    public void showDocument(URL url, String target) {
        final String urlToOpen = filterURI(url.toString());
        this.browserTaskExecutor.execute(new Runnable() {

            public void run() {
                try {
                    Desktop.getDesktop().browse(new URI(urlToOpen));
                } catch (Exception ex) {
                    log.error("Impossible d'afficher la page " + urlToOpen, ex);
                }
            }
        });
    }

    protected String filterURI(String url) {
        return this.urlCleaner.matcher(url).replaceAll("");
    }

    public void showDocument(URL url) {
        showDocument(url, null);
    }

    public void setStream(String key, InputStream stream) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Iterator<String> getStreamKeys() {
        throw new UnsupportedOperationException();
    }

    public InputStream getStream(String key) {
        throw new UnsupportedOperationException();
    }

    public Image getImage(URL url) {
        Image image = this.cacheImages.get(url);
        if (image == null) {
            image = loadImage(url);
            this.cacheImages.put(url, image);
        }
        return image;
    }

    public AudioClip getAudioClip(URL url) {
        AudioClip audioClip = this.cacheAudioClips.get(url);
        if (audioClip == null) {
            audioClip = loadAudioClip(url);
            this.cacheAudioClips.put(url, audioClip);
        }
        return audioClip;
    }

    private Image loadImage(URL url) {
        return Toolkit.getDefaultToolkit().createImage(getAppletResource(getLocalResourcePath(url)));
    }

    private AudioClip loadAudioClip(URL url) {
        return Applet.newAudioClip(getAppletResource(getLocalResourcePath(url)));
    }

    private String getLocalResourcePath(URL url) {
        return url.toString().substring(this.applet.getDocumentBase().toString().length());
    }

    private URL getAppletResource(String path) {
        if (debugEnabled) {
            log.debug("Loading applet resource " + path);
        }
        return this.classLoader.getResource(path);
    }

    public Enumeration<Applet> getApplets() {
        throw new UnsupportedOperationException();
    }

    public Applet getApplet(String name) {
        return this.applet;
    }
}
