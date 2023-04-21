package editor.source.test;

import java.util.Iterator;
import editor.common.IConstants;
import editor.common.Node;
import editor.common.SourceDetail;
import editor.config.ConfigManager;
import editor.source.ISource;
import editor.source.SourceException;
import editor.source.SourceFactory;

public class SourceTest implements IConstants {

    public static void main(String[] args) throws SourceException {
        SourceTest sourceTest = new SourceTest();
        sourceTest.testSourceFactory();
        sourceTest.testSourceInterface();
    }

    private void testSourceFactory() throws SourceException {
        log("Testing SourceFactory ...");
        log("Calling init() ...");
        SourceFactory.init();
        log("Registered Sources are:");
        showRegisteredSources();
        log("Creating local Source ...");
        createLocalSource();
        log("Creating ftp Source ...");
        createFtpSource();
        log("Now Registered Sources are:");
        showRegisteredSources();
        log("Testing SourceFactory completed.");
    }

    private ISource createFtpSource() throws SourceException {
        SourceDetail sourceDetail = new SourceDetail(ConfigManager.getGUIConfig().getLabel(LOCAL), null, null, null, ConfigManager.getGUIConfig().getLabel(LOCAL));
        return SourceFactory.createSource(sourceDetail);
    }

    private ISource createLocalSource() throws SourceException {
        SourceDetail sourceDetail = new SourceDetail(ConfigManager.getGUIConfig().getLabel(LOCAL), null, null, null, ConfigManager.getGUIConfig().getLabel(LOCAL));
        return SourceFactory.createSource(sourceDetail);
    }

    private void showRegisteredSources() {
        Iterator<String> iterator = SourceFactory.getRegisteredSourceNames().iterator();
        while (iterator.hasNext()) {
            log(iterator.next());
        }
    }

    private void testSourceInterface() throws SourceException {
        log("Testing Source Interface ...");
        log("Testing Local Source Interface ...");
        ISource source = createLocalSource();
        log("Testing getRoots() ...");
        log("Available roots are:");
        Node[] roots = null;
        try {
            roots = source.getRoots();
            for (Node root : roots) {
                log(root.toString());
            }
        } catch (SourceException e) {
            log("Testing getRoots() failed.");
            System.exit(0);
        }
        log("Testing getChildern() ...");
        Node[] childern = null;
        try {
            for (Node root : roots) {
                childern = source.getChildern(root);
                log("Available Childern for root " + root.getAlias() + " are:");
                for (Node child : childern) {
                    log("Name:" + child.toString() + " Type:" + (child.isLeaf() == true ? "File" : "Folder"));
                }
            }
        } catch (SourceException e) {
            log("Testing getChildern() failed.");
            System.exit(0);
        }
        log("Testing Source Interface completed.");
    }

    private void log(String message) {
        System.out.println(message);
    }
}
