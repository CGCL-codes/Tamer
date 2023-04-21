package de.carne.fs.provider.mzimage;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.Format;
import de.carne.fs.core.format.FormatStruct;
import de.carne.fs.core.transfer.ExceptionTransferHandler;
import de.carne.io.InvalidDataException;

/**
 * MZ executable image decoder.
 */
public class MZImageFormat extends Format {

    private static final Pattern[] NAME_PATTERNS = { Pattern.compile("(?i).*\\.exe") };

    private static final FormatStruct[] HEADERS = { DOSHeader.THIS };

    private static final FormatStruct[] TRAILERS = {};

    /**
	 * Construct <code>MZImageFormat</code>.
	 */
    public MZImageFormat() {
        super(MZImage.NAME, ByteOrder.LITTLE_ENDIAN, NAME_PATTERNS, HEADERS, TRAILERS);
    }

    @Override
    public FileScannerResultNode decode(FileScannerResultNode parent, long position) throws IOException {
        final FileScannerResultNode decoded = parent.addFormat(MZImage.NAME, position, position);
        try {
            HEADERS[0].decode(decoded, decoded.end());
        } catch (final InvalidDataException e) {
            decoded.addDataHandler(new ExceptionTransferHandler(decoded, e));
        }
        return FileScannerResultNode.purgeDecodedNode(decoded);
    }
}
