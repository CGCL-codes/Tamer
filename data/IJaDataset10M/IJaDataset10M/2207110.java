package org.fudaa.ctulu.gis.factory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import com.vividsolutions.jts.geom.Envelope;
import com.memoire.bu.BuFileFilter;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gis.gml.GMLDataStoreFactory;
import org.fudaa.ctulu.gis.mif.MIFDataStoreFactory;
import org.fudaa.ctulu.gis.shapefile.ShapefileDataStoreFactory;

/**
 * A utiliser lorsqu'on veut exporter des donn�es dans un format SIG.
 * 
 * @author fred deniger
 * @version $Id: GISExportDataStoreFactory.java,v 1.1 2007-01-17 10:45:27 deniger Exp $
 */
public final class GISExportDataStoreFactory {

    private GISExportDataStoreFactory() {
    }

    /**
   * Construit les filtres par default.
   * 
   * @param _import true si utilise pour l'import
   * @return table FileFilter->FileDataStoreFactorySpi
   */
    public static Map buildFileFilterMap(final boolean _import) {
        final HashMap fileFilterDataStore = new HashMap();
        final FileDataStoreFactorySpi[] stores = new FileDataStoreFactorySpi[] { new ShapefileDataStoreFactory(), new MIFDataStoreFactory(), new GMLDataStoreFactory() };
        for (int i = 0; i < stores.length; i++) {
            fileFilterDataStore.put(buildFileFilterFor(stores[i]), stores[i]);
        }
        return fileFilterDataStore;
    }

    /**
   * Construit et retoune le filtre correspondant au DataStore.
   * @param _store Le data store.
   * @return 
   */
    public static BuFileFilter buildFileFilterFor(FileDataStoreFactorySpi _store) {
        final String[] ex = _store.getFileExtensions();
        if (ex != null) {
            for (int j = ex.length - 1; j >= 0; j--) {
                if (ex[j].startsWith(CtuluLibString.DOT)) {
                    ex[j] = ex[j].substring(1);
                }
            }
        }
        final String desc = _store.getDescription();
        final BuFileFilter ft = new BuFileFilter(ex, desc);
        ft.setExtensionListInDescription(false);
        return ft;
    }

    /**
   * Permet de creer un DataStore en specifier les limites (utilise par MapInfo).
   * 
   * @param _fac l'usine a utiliser
   * @param _url le fichier de dest
   * @param _bounds l'enveloppe des donnees. Utiliser par la sortie MID/MIF
   * @return le DataStore correctement initialise
   * @throws IOException
   */
    public static DataStore createDataStore(final FileDataStoreFactorySpi _fac, final URL _url, final Envelope _bounds, final boolean _write) throws IOException {
        if (_write) {
            final File f = new File(_url.getFile());
            if (f.exists()) {
                f.delete();
            }
        }
        if (_fac instanceof MIFDataStoreFactory) {
            final Envelope e = new Envelope(_bounds);
            e.expandToInclude(Math.floor(e.getMinX()), Math.floor(e.getMinY()));
            e.expandToInclude(Math.floor(e.getMaxX() + 1D), Math.floor(e.getMaxY() + 1D));
            return ((MIFDataStoreFactory) _fac).createDataStore(_url, e);
        }
        return _fac.createDataStore(_url);
    }
}
