package com.zildo;

import java.io.InputStream;
import java.nio.ByteBuffer;
import zildo.fwk.file.EasyBuffering;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author Tchegito
 *
 */
public class AndroidReadingFile extends EasyBuffering {

    public static AssetManager assetManager;

    static byte[] buf = new byte[4000];

    public AndroidReadingFile(String path) {
        super(null);
        String completeFilename = "resources/" + path;
        Log.d("file", "open " + path);
        int done = 0;
        boolean finished = false;
        try {
            InputStream stream = assetManager.open(completeFilename);
            data = ByteBuffer.allocate(stream.available());
            while (!finished) {
                int read = stream.read(buf);
                data.put(buf, 0, read);
                if (read == -1) {
                    throw new RuntimeException("Something went horribly wrong");
                }
                done += read;
                finished = (read < buf.length);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read " + path);
        }
        data.flip();
    }
}
