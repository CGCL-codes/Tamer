package gnu.classpath.tools.appletviewer;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * StandaloneAppletViewer displays an applet in its own Frame.  Most
 * of the context that is available to an applet within a webpage is
 * available to it in StandaloneAppletViewer.
 */
class StandaloneAppletViewer extends Main {

    static ArrayList appletTags = new ArrayList();

    static ArrayList appletWindows = new ArrayList();

    StandaloneAppletViewer(String[] urls) throws MalformedURLException, IOException {
        for (int i = 0; i < urls.length; i++) {
            TagParser parser = new TagParser(urls[i]);
            appletTags.addAll(parser.parseAppletTags());
        }
        printTags();
        createWindows();
    }

    StandaloneAppletViewer(String code, String codebase, String archives, List parameters, Dimension dimensions) throws IOException {
        if (!(code.equals("") || code.endsWith(".class"))) {
            System.err.println("appletviewer: option '--code' requires a class filename");
            System.exit(1);
        }
        String tagString = "<EMBED" + " CODE=\"" + code + "\"" + " WIDTH=" + dimensions.width + " HEIGHT=" + dimensions.height + " CODEBASE=\"" + codebase + "\"" + " ARCHIVE=\"" + archives + "\">";
        Iterator pairs = parameters.iterator();
        while (pairs.hasNext()) {
            StringTokenizer paramTokenizer = new StringTokenizer((String) pairs.next(), ",");
            tagString += "<PARAM NAME=" + paramTokenizer.nextToken().trim() + " VALUE=" + paramTokenizer.nextToken().trim() + ">";
        }
        tagString += "</EMBED>";
        StringReader reader = new StringReader(tagString);
        String path = System.getProperty("user.dir") + File.separator;
        TagParser parser = new TagParser(reader, new URL("file", "", path));
        appletTags.addAll(parser.parseAppletTags());
        printTags();
        createWindows();
    }

    void printTags() {
        if (verbose) {
            System.out.println("parsed applet tags:");
            for (int i = 0; i < appletTags.size(); i++) {
                AppletTag tag = (AppletTag) appletTags.get(i);
                System.out.println(" tag " + i + ":");
                System.out.println(tag);
            }
        }
    }

    void createWindows() {
        for (int i = 0; i < appletTags.size(); i++) {
            AppletTag tag = (AppletTag) appletTags.get(i);
            new StandaloneAppletWindow(tag, appletWindows);
        }
    }
}
