package de.carne.fs.core.transfer;

import java.util.Properties;
import de.carne.fs.core.FileScannerResult;
import de.carne.nio.compression.Decoder;

/**
 * Data handler providing a standard text view to decoded scan results.
 */
public class DecodedTextDataHandler extends DefaultTextDataHandler {

    private final String[] decoderPropertyKeys;

    private final Properties decoderProperties = new Properties();

    /**
	 * Construct <code>RawTextDataHandler</code>.
	 * 
	 * @param result The scan result to provide text data for.
	 * @param decoder The <code>Decoder</code> that was used to decode the scan result.
	 */
    public DecodedTextDataHandler(FileScannerResult result, Decoder decoder) {
        super(result);
        this.decoderPropertyKeys = decoder.queryPropertyKeys();
        decoder.queryProperties(this.decoderProperties);
    }

    @Override
    protected void prepareReader(StaticTextDataReader reader) {
        super.prepareReader(reader);
        if (reader.linesCount() > 0) {
            reader.appendLine();
        }
        reader.appendLabel("Decoder properties:");
        for (final String propertyKey : this.decoderPropertyKeys) {
            reader.appendLine();
            reader.appendSymbol(String.format("Decoder[%s]", propertyKey));
            reader.appendOperator(" = ");
            reader.appendValue(this.decoderProperties.getProperty(propertyKey));
        }
    }
}
