package org.jcvi.datastore.zip;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import org.jcvi.datastore.DataStore;

/**
 * A {@code ZipDataStore} is a {@link DataStore} implementation
 * of a ZIP file.  The ids in this DataStore are the {@link ZipEntry}s
 * and the objects returned are {@link InputStream}s of the Files
 * contained in the zip.  NOTE: Since JAR files are actually ZIP
 * files, {@link ZipDataStore} can be used to read JARS as well.
 * @author dkatzel
 *
 *
 */
public interface ZipDataStore extends DataStore<InputStream> {
}
