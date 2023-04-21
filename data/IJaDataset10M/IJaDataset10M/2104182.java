package imi.avatarloaders;

import imi.scene.SkeletonNode;
import imi.utils.ObjectInputStreamEx;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * This class is used for loading serialized binary head file (bhf) packs.
 *
 * @author Ronald E Dahlgren
 */
public final class BinaryHeadFileImporter {

    private static final int BUFFER_SIZE = 1024 * 64;

    /** Disabled **/
    private BinaryHeadFileImporter() {
    }

    /**
     * Load the binary head contained in the given InputStream.
     * <p>This InputStream should come from a FileInputStream or a call
     * to URL's openConnection() method on a valid BHF file.</p>
     * @param stream A valid and non-null input stream
     * @return The loaded head skeleton
     * @throws IOException On stream error
     * @throws IllegalArgumentException If stream == null, or if stream is not a valid bhf
     */
    public static SkeletonNode loadHeadFile(InputStream stream) throws IOException {
        if (stream == null) throw new IllegalArgumentException("Null stream provided!");
        SkeletonNode result = null;
        try {
            GZIPInputStream zis = new GZIPInputStream(stream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = zis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStreamEx inStream = new ObjectInputStreamEx(byteStream);
            result = (SkeletonNode) inStream.readObject();
            inStream.close();
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Stream was not a valid BHF file.");
        }
        return result;
    }
}
