package jdvi;

import java.applet.*;
import java.awt.*;
import java.net.*;
import java.util.*;

/**
 * this interface describes methods JDviPanel needs for interacting 
 * with its surrounding. JDviPanel assumes, that it has got an implementation
 * of this interface set with setJDviContext();
 */
public interface JDviContext {

    /**
     * this mehod is used to get any images that replace PSfiles
     */
    public abstract Image getImage(URL url);

    /**
     * getAudioClip(URL) is needed for applets that want to play audio.
     * It might return null.
     */
    public abstract AudioClip getAudioClip(URL url);

    /**
     * These methods are called for external links
     */
    public abstract void showDocument(URL url);

    public abstract void showDocument(URL url, String s);

    /**
     * This method returns the code base
     */
    public abstract URL getCodeBase();

    /**
     * This method returns the document base
     */
    public abstract URL getDocumentBase();

    /**
     * this mathod is called when status messages are send out.
     */
    public abstract void inform(String s);

    /**
     * this method returns a properties object that may store keys for jdvi
     * At the moment the following keys are used:<p>
     * jdvi.font.path=<;-seperated list of URLs> : the path where jdvi will look for fonts
     * jdvi.resolution=<integer> : jdvi's home=max resolution<p>
     * jdvi.paper.width=<float> : the paper width jdvi draws on<p>
     * jdvi.paper.height=<float> : the paper height jdvi draws on<p>
     * jdvi.image.formats=<;-seperated list of extensions e.g. gif;jpg;jpeg> : the file extensions jdvi will use to
     * find images that replace the ps files.<p>
     * jdvi.color.<colorName>=<integer> : colorName can then be used in the named color model of
     *                                    TeX's color package (use driver dvips...). There are three special
     * color names:
     * jdvi.color.textColor
     * jdvi.color.linkColor
     * jdvi.color.targetColor
     * jdvi.color.scribbleColor<p>
     */
    public abstract String getProperty(String name);

    public abstract String getProperty(String name, String defRes);
}
