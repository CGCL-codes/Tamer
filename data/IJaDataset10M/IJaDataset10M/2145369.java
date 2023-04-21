package net.sf.japi.progs.jeduca.swing.io;

import java.io.IOException;
import org.xml.sax.SAXException;

/** Adapter class to have a caching implementation of {@link Importer} where the Importer-implementation cannot extend {@link AbstractCachingImporter}.
 * @param <T> Type which is imported.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class CachingImporterAdapter<T> extends AbstractCachingImporter<T> {

    /** Importer to adapt. */
    private final Importer<T> importer;

    /** Create a CachingImporter for another Importer.
     * @param importer Importer to adapt
     */
    public CachingImporterAdapter(final Importer<T> importer) {
        this.importer = importer;
    }

    /** {@inheritDoc}
     * Invokes canLoad of the adapted importer.
     */
    @Override
    protected boolean canLoadImpl(final String url) {
        return importer.canLoad(url);
    }

    /** {@inheritDoc}
     * Invokes load of the adapted importer.
     */
    public T load(final String url) throws IOException, SAXException {
        return importer.load(url);
    }

    /** {@inheritDoc}
     * Invokes getName of the adapted importer.
     */
    public String getName() {
        return importer.getName();
    }

    /** {@inheritDoc}
     * Invokes getType of the adapted importer.
     */
    public Class<T> getType() {
        return importer.getType();
    }
}
