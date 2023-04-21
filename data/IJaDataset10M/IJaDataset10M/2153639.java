package net.sourceforge.javagg.dataio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A factory to create
 * {@link RandomIOFileFactory} instances. It may reuse previously 
 * created instances.
 */
final class RandomIOFileFactoryCreator {

    private RandomIOFileFactoryCreator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new {@link RandomIOFileFactory} or returns an
     * existing instance.
     * @return a {@link RandomIOFileFactory} 
     */
    public static RandomIOFileFactory createRandomIOFileFactory() {
        return new RandomIOFileFactoryImpl();
    }

    private static final class RandomIOFileFactoryImpl implements RandomIOFileFactory {

        private static final String FILE_MODE = "rws";

        public RandomFileIO createRandomIO(final File f) throws FileNotFoundException {
            final RandomAccessFile delegate = new RandomAccessFile(f, FILE_MODE);
            return new RandomFileIO() {

                public void readSet() {
                    throw new UnsupportedOperationException();
                }

                public void writeSet() {
                    throw new UnsupportedOperationException();
                }

                public boolean readBoolean() throws IOException {
                    return delegate.readBoolean();
                }

                public char readChar() throws IOException {
                    return delegate.readChar();
                }

                public double readDouble() throws IOException {
                    return delegate.readDouble();
                }

                public int readInt() throws IOException {
                    return delegate.readInt();
                }

                public String readUTF() throws IOException {
                    return delegate.readUTF();
                }

                public void writeBoolean(boolean arg0) throws IOException {
                    delegate.writeBoolean(arg0);
                }

                public void writeChar(int arg0) throws IOException {
                    delegate.writeChar(arg0);
                }

                public void writeDouble(double arg0) throws IOException {
                    delegate.writeDouble(arg0);
                }

                public void writeInt(int arg0) throws IOException {
                    delegate.writeInt(arg0);
                }

                public void writeUTF(String arg0) throws IOException {
                    delegate.writeUTF(arg0);
                }
            };
        }
    }
}
