package fr.ens.transcriptome.teolenn.measurement.io;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import fr.ens.transcriptome.teolenn.Globals;
import fr.ens.transcriptome.teolenn.TeolennException;
import fr.ens.transcriptome.teolenn.measurement.ChromosomeMeasurement;
import fr.ens.transcriptome.teolenn.resource.ChromosomeNameResource;
import fr.ens.transcriptome.teolenn.sequence.SequenceMeasurements;
import fr.ens.transcriptome.teolenn.util.FileUtils;

/**
 * This class define a sequence measurement writer based on serialization.
 * @author Laurent Jourdren
 */
public class SerializedSequenceMeasurementsWriter implements SequenceMeasurementsWriter {

    private static Logger logger = Logger.getLogger(Globals.APP_NAME);

    private static final String SERIALIZED_FORMAT_VERSION = "TEOLENN_MES_2";

    private ObjectOutputStream out;

    private ChromosomeNameResource resource;

    private final Map<String, Integer> indexChromosomeNames = new HashMap<String, Integer>();

    private int typeDataChromosomeName;

    private int indexChr;

    private boolean headerDone;

    private int[] types;

    /**
   * Write the header of the file.
   * @param sm the object that contains the name and type of measurements
   * @throws IOException if an error occurs while writing header
   */
    private void writeHeader(final SequenceMeasurements sm) throws IOException {
        if (headerDone) return;
        out.writeUTF(SERIALIZED_FORMAT_VERSION);
        out.writeUTF("Id");
        final String[] names = sm.getArrayMesurementNames();
        out.writeObject(names);
        this.types = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            final Object objType = sm.getMeasurement(names[i]).getType();
            if (Float.class == objType) this.types[i] = 1; else if (Integer.class == objType) this.types[i] = 3; else if (String.class == objType) this.types[i] = 5; else {
                logger.severe("Unknown datatype: " + objType);
                throw new IOException("Unknown datatype: " + objType);
            }
        }
        final List<String> chrNames = this.resource.getChromosomesNames();
        final int nbChr = chrNames.size();
        if (nbChr >= 0 && nbChr <= Byte.MAX_VALUE) this.typeDataChromosomeName = 2; else if (nbChr > Byte.MAX_VALUE && nbChr <= Short.MAX_VALUE) this.typeDataChromosomeName = 4; else if (nbChr > Short.MAX_VALUE) this.typeDataChromosomeName = 1;
        out.writeInt(this.typeDataChromosomeName);
        int i = 0;
        for (String chrName : chrNames) this.indexChromosomeNames.put(chrName, i++);
        final String[] chrNamesArray = new String[chrNames.size()];
        chrNames.toArray(chrNamesArray);
        out.writeObject(chrNamesArray);
        this.indexChr = -1;
        this.headerDone = true;
    }

    /**
   * Write a sequence measurement to the file.
   * @param sm Sequence measurement to write
   * @throws IOException if an error occurs while writing data
   */
    public void writeSequenceMesurement(final SequenceMeasurements sm) throws IOException {
        if (this.out == null) return;
        String lastChr = null;
        Object chrNameIndex = null;
        if (!headerDone) {
            writeHeader(sm);
            this.indexChr = sm.getIndexMeasurment(ChromosomeMeasurement.MEASUREMENT_NAME);
            this.types[this.indexChr] = this.typeDataChromosomeName;
        }
        final int id = sm.getId();
        out.writeInt(id);
        final Object[] values = sm.getArrayMeasurementValues();
        final int len = values.length;
        for (int i = 0; i < len; i++) {
            final Object val;
            if (i == this.indexChr) {
                final String chr = (String) values[i];
                if (chr != lastChr) {
                    final int idx = this.indexChromosomeNames.get(chr);
                    switch(this.typeDataChromosomeName) {
                        case 2:
                            chrNameIndex = (byte) idx;
                            break;
                        case 4:
                            chrNameIndex = (short) idx;
                            break;
                        case 3:
                            chrNameIndex = idx;
                            break;
                    }
                    lastChr = chr;
                }
                val = chrNameIndex;
            } else val = values[i];
            switch(this.types[i]) {
                case 1:
                    out.writeFloat((Float) val);
                    break;
                case 2:
                    out.writeByte((Byte) val);
                    break;
                case 3:
                    out.writeInt((Integer) val);
                    break;
                case 4:
                    out.writeShort((Short) val);
                    break;
                case 5:
                    out.writeUTF((String) val);
                    break;
                default:
                    break;
            }
        }
        if ((id % 100000 == 0)) out.reset();
    }

    /**
   * Close the writer.
   * @throws IOException if an error occurs while closing the writer
   */
    public void close() throws IOException {
        this.out.close();
        this.out = null;
    }

    /**
   * Public constructor.
   * @param file file to write
   */
    public SerializedSequenceMeasurementsWriter(final File file) throws IOException {
        if (file == null) throw new NullPointerException("File is null");
        this.out = FileUtils.createObjectOutputWriter(file);
        try {
            this.resource = ChromosomeNameResource.getRessource();
        } catch (TeolennException e) {
            throw new IOException("None chromosome name found");
        }
    }
}
