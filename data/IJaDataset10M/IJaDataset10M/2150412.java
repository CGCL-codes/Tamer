package org.fudaa.ctulu.gis.gml;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.xml.sax.SAXException;
import com.vividsolutions.jts.io.ParseException;
import com.memoire.fu.FuLog;

/**
 * @author fred deniger
 * @version $Id: GMLFeatureReader.java,v 1.4 2007-05-04 13:43:21 deniger Exp $
 */
public class GMLFeatureReader implements FeatureReader {

    FeatureIterator it_;

    FeatureType type_;

    final File f_;

    boolean init_;

    public GMLFeatureReader(final File _f) {
        f_ = _f;
    }

    private void initRead() throws IOException {
        if (!init_) {
            init_ = true;
            try {
                final GMLReader reader = new GMLReader();
                final FeatureCollection collection = reader.open(f_);
                if (collection != null) {
                    it_ = collection.features();
                } else {
                    it_ = null;
                }
                type_ = reader.gmlInput_.toFeatureSchema();
            } catch (final SAXException _evt) {
                FuLog.error(_evt);
                throw new IOException(_evt.getMessage());
            } catch (final ParseException _evt) {
                FuLog.error(_evt);
                throw new IOException(_evt.getMessage());
            } catch (final SchemaException _evt) {
                FuLog.error(_evt);
                throw new IOException(_evt.getMessage());
            }
        }
    }

    /**
   * Lu et ferme automatiquement.
   */
    public void close() throws IOException {
    }

    public FeatureType getFeatureType() {
        try {
            initRead();
        } catch (final IOException _evt) {
            FuLog.error(_evt);
            return null;
        }
        return type_;
    }

    public boolean hasNext() throws IOException {
        initRead();
        return it_ != null && it_.hasNext();
    }

    public Feature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        initRead();
        return it_ == null ? null : it_.next();
    }
}
