package fr.ign.cogit.geoxygene.util.viewer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Observable;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.datatools.Geodatabase;

/** * 2007 : amélioration de la gestion de la fermeture - grosso *  * @author Thierry Badard & Arnaud Braun * @version 1.1 *  */
public class ObjectViewer extends Observable {

    private static final String OBJECTVIEWER_TITLE = "GeOxygene Object Viewer";

    public static boolean flagWindowClosing = true;

    public ObjectViewerInterface objectViewerInterface;

    /** Creates a new ObjectViewer without connection to a Geodatabase. */
    public ObjectViewer() {
        this(null);
    }

    /** Creates a new ObjectViewer with a connection to a Geodatabase. */
    public ObjectViewer(Geodatabase db) {
        this.objectViewerInterface = new ObjectViewerInterface(ObjectViewer.OBJECTVIEWER_TITLE, db);
        this.addObserver(this.objectViewerInterface);
        this.objectViewerInterface.pack();
        this.objectViewerInterface.setSize(600, 400);
        this.objectViewerInterface.setVisible(true);
        this.objectViewerInterface.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (ObjectViewer.flagWindowClosing) {
                    System.exit(0);
                } else {
                    ObjectViewer.this.objectViewerInterface.dispose();
                }
            }
        });
    }

    /**   * Display objects stored in the FeatureCollection as a Theme with the given   * name.   */
    public void addFeatureCollection(IFeatureCollection<?> fColl, String themeName) {
        this.objectViewerInterface.addAFeatureCollectionTheme(fColl, themeName);
    }

    /**   * Refresh fully the FeatureCollection displayed in the viewer with this given   * name.   */
    public void refreshFeatureCollection(IFeatureCollection<?> fColl, String themeName) {
        this.objectViewerInterface.refreshAFeatureCollectionTheme(fColl, themeName);
    }

    /**   * Refresh the FeatureCollection displayed in the viewer with this given name   * with this feature.   *    * The feature must already belong to the collection.   */
    public void refreshFeatureCollection(IFeature feature, String themeName) {
        this.objectViewerInterface.refreshAFeatureCollectionTheme(feature, themeName);
    }

    /**   * Display objects stored in the shapefile given by url (without .shp) in an   * ObjectViewer.   */
    public void addAShapefile(URL url) {
        this.objectViewerInterface.addAShapefileTheme(url);
    }

    /** Display an image (.jpg or .gif) in an ObjectViewer. */
    public void addAnImage(URL url, int x, int y, int width, int height) {
        this.objectViewerInterface.addAnImageTheme(url.getFile(), x, y, width, height);
    }
}
