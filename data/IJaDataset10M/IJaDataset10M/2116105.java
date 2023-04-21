package com.bigfatgun.fixjures.serializable;

import com.bigfatgun.fixjures.FixtureException;
import com.bigfatgun.fixjures.FixtureSource;
import com.bigfatgun.fixjures.FixtureStream;
import com.bigfatgun.fixjures.FixtureType;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * This fixture source implements {@link com.bigfatgun.fixjures.FixtureStream}, meaning it can be used to provide
 * multiple fixture objects in its lifetime.
 * <p/>
 * It lazily creates an {@code ObjectInputStream} from the underlying {@code ReadableByteChannel} during the first call
 * to {@link #createFixture(com.bigfatgun.fixjures.FixtureType)} and does not close it until {@link #close()} is
 * invoked.
 */
public class ObjectInputStreamSource extends FixtureSource implements FixtureStream {

    public static ObjectInputStreamSource newFile(final File file) throws FileNotFoundException {
        checkNotNull(file);
        return new ObjectInputStreamSource(new RandomAccessFile(file, "r").getChannel());
    }

    public static ObjectInputStreamSource newResource(final ClassLoader clsLoader, final String resourceName) throws FileNotFoundException {
        checkNotNull(resourceName);
        return new ObjectInputStreamSource(Channels.newChannel(clsLoader.getResourceAsStream(resourceName)));
    }

    public static ObjectInputStreamSource newObjectInputStream(final ReadableByteChannel channel) {
        return new ObjectInputStreamSource(channel);
    }

    private ObjectInputStream objIn;

    /**
	 * Initializes the source.
	 *
	 * @param source source data
	 */
    ObjectInputStreamSource(final ReadableByteChannel source) {
        super(source);
    }

    /**
	 * Lazy-creates an {@code ObjectInputStream} and reads an object from it.
	 *
	 * @param type fixture object type
	 * @return new fixture object
	 */
    @Override
    public Object createFixture(final FixtureType type) {
        try {
            if (objIn == null) {
                objIn = new ObjectInputStream(Channels.newInputStream(getSource()));
            }
            return type.getType().cast(objIn.readObject());
        } catch (Exception e) {
            throw FixtureException.convert(e);
        }
    }

    /**
	 * Closes the {@code ObjectInputStream} and then calls {@link com.bigfatgun.fixjures.FixtureSource#close()}.
	 *
	 * @throws IOException if there is any error closing the io streams
	 */
    @Override
    public void close() throws IOException {
        objIn.close();
        super.close();
    }

    public FixtureSource asSourceStream() {
        return this;
    }
}
