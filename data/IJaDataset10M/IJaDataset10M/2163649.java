package net.sf.staccatocommons.io.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sf.staccatocommons.io.serialization.lifecycle.SerializationLifecycle;

/**
 * Abstract implementation for a {@link ByteSerializationManager} that resolves
 * the tasks of serializing from a file an back
 * 
 * @author flbulgarelli
 */
public abstract class AbstractByteSerializationManager implements SerializationManager {

    @Override
    public <T> T deserialize(final File input) {
        return new SerializationLifecycle.Deserialize<T>(this) {

            public InputStream initialize() throws IOException {
                return new FileInputStream(input);
            }
        }.value();
    }

    @Override
    public void serialize(Object target, final File output) {
        new SerializationLifecycle.Serialize(this, target) {

            public OutputStream initialize() throws IOException {
                return new FileOutputStream(output);
            }
        }.value();
    }
}
