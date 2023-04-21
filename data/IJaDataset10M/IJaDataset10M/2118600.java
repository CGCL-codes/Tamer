package net.sourceforge.justin.core.test.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

/**
 * Used to create manifest in tests.
 * 
 * @author Xavier Detant <xavier.detant@gmail.com>
 * @version 0.0.1 Type creation
 */
public final class ManifestCreator {

    /**
     * Instantiates a new manifest creator.
     */
    private ManifestCreator() {
        super();
    }

    /**
     * Gets a manifest file as a manifest object.
     * 
     * @param path the path
     * @return the manifest
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Manifest getManifest(final String path) throws IOException {
        final File manifestFile = new File(TestConstants.TEST_MANIFESTS_PATH + path);
        final InputStream manifestIS = new FileInputStream(manifestFile);
        return new Manifest(manifestIS);
    }
}
