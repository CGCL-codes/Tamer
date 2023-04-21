package proguard.io;

import proguard.util.*;
import java.io.*;

/**
 * This DataEntryReader writes the ZIP entries and files that it reads to a
 * given DataEntryWriter.
 *
 * @author Eric Lafortune
 */
public class DataEntryCopier implements DataEntryReader {

    private static final int BUFFER_SIZE = 1024;

    private DataEntryWriter dataEntryWriter;

    private byte[] buffer = new byte[BUFFER_SIZE];

    public DataEntryCopier(DataEntryWriter dataEntryWriter) {
        this.dataEntryWriter = dataEntryWriter;
    }

    public void read(DataEntry dataEntry) throws IOException {
        try {
            OutputStream outputStream = dataEntryWriter.getOutputStream(dataEntry);
            if (outputStream != null) {
                InputStream inputStream = dataEntry.getInputStream();
                copyData(inputStream, outputStream);
                dataEntry.closeInputStream();
            }
        } catch (IOException ex) {
            System.err.println("Warning: can't write resource [" + dataEntry.getName() + "] (" + ex.getMessage() + ")");
        }
    }

    /**
     * Copies all data that it can read from the given input stream to the
     * given output stream.
     */
    private void copyData(InputStream inputStream, OutputStream outputStream) throws IOException {
        while (true) {
            int count = inputStream.read(buffer);
            if (count < 0) {
                break;
            }
            outputStream.write(buffer, 0, count);
        }
        outputStream.flush();
    }

    /**
     * A main method for testing file/jar/war/directory copying.
     */
    public static void main(String[] args) {
        try {
            String input = args[0];
            String output = args[1];
            boolean outputIsJar = output.endsWith(".jar");
            boolean outputIsWar = output.endsWith(".war");
            boolean outputIsEar = output.endsWith(".ear");
            boolean outputIsZip = output.endsWith(".zip");
            DataEntryWriter writer = new DirectoryWriter(new File(output), outputIsJar || outputIsWar || outputIsEar || outputIsZip);
            if (!outputIsJar) {
                DataEntryWriter zipWriter = new JarWriter(writer);
                if (outputIsZip) {
                    writer = zipWriter;
                } else {
                    writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".zip"))), zipWriter, writer);
                }
                DataEntryWriter warWriter = new JarWriter(writer);
                if (outputIsWar) {
                    writer = warWriter;
                } else {
                    writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".war"))), warWriter, writer);
                }
            }
            DataEntryWriter jarWriter = new JarWriter(writer);
            if (outputIsJar) {
                writer = jarWriter;
            } else {
                writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".jar"))), jarWriter, writer);
            }
            DataEntryReader reader = new DataEntryCopier(writer);
            boolean inputIsJar = input.endsWith(".jar");
            boolean inputIsWar = input.endsWith(".war");
            boolean inputIsZip = input.endsWith(".zip");
            DataEntryReader jarReader = new JarReader(reader);
            if (inputIsJar) {
                reader = jarReader;
            } else {
                reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".jar")), jarReader, reader);
                DataEntryReader warReader = new JarReader(reader);
                if (inputIsWar) {
                    reader = warReader;
                } else {
                    reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".war")), warReader, reader);
                }
                DataEntryReader zipReader = new JarReader(reader);
                if (inputIsZip) {
                    reader = zipReader;
                } else {
                    reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".zip")), zipReader, reader);
                }
            }
            DirectoryPump directoryReader = new DirectoryPump(new File(input));
            directoryReader.pumpDataEntries(reader);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
