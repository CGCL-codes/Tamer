package fr.ens.transcriptome.teolenn.measurement.io;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;
import fr.ens.transcriptome.teolenn.Globals;
import fr.ens.transcriptome.teolenn.sequence.SequenceMeasurements;
import fr.ens.transcriptome.teolenn.util.FileUtils;

public class FilteredSequenceMeasurementsReader implements SequenceMeasurementsReader {

    private static Logger logger = Logger.getLogger(Globals.APP_NAME);

    private static final String SERIALIZED_FORMAT_VERSION = "TEOLENN__FILTERED_MES_1";

    private SequenceMeasurementsReader reader;

    private ObjectInputStream in;

    /**
   * Read the header of the file.
   * @throws IOException if an error occurs while reading the header of the file
   */
    private void readHeader() throws IOException {
        final String cookie = this.in.readUTF();
        if (!SERIALIZED_FORMAT_VERSION.equals(cookie)) {
            logger.severe("Invalid measurement version file");
            throw new IOException("Invalid measurement version file");
        }
    }

    /**
   * Get the next sequence measurement.
   * @return the next sequence measurement
   * @throws IOException if an error occurs while reading measurements
   */
    public SequenceMeasurements next() throws IOException {
        return next(null);
    }

    /**
   * Get the next sequence measurement. This version of the next method allow to
   * reuse previous sequence measurement object to save time and memory.
   * @return the next sequence measurement
   * @throws IOException if an error occurs while reading measurements
   */
    public SequenceMeasurements next(final SequenceMeasurements sm) throws IOException {
        SequenceMeasurements result = sm;
        final int nextId;
        try {
            nextId = in.readInt();
        } catch (EOFException e) {
            return null;
        }
        int readId = -1;
        while (readId != nextId) {
            result = this.reader.next(result);
            if (result == null) return null;
            readId = result.getId();
        }
        return result;
    }

    /**
   * Close the reader.
   * @throws IOException if an error occurs while closing the reader
   */
    public void close() throws IOException {
        this.reader.close();
        this.in.close();
    }

    /**
   * Public constructor.
   * @param file Sequence measurement file to parse
   */
    public FilteredSequenceMeasurementsReader(final File file, final SequenceMeasurementsReader reader) throws IOException {
        if (file == null) throw new NullPointerException("File is null");
        if (reader == null) throw new NullPointerException("Measurement file is null");
        this.in = FileUtils.createObjectInputReader(file);
        this.reader = reader;
        readHeader();
    }
}
