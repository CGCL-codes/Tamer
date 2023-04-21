package de.carne.fs.provider.elf;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.Format;
import de.carne.fs.core.format.FormatStruct;
import de.carne.fs.core.transfer.ExceptionTransferHandler;
import de.carne.io.InvalidDataException;

/**
 * ELF (Executable and Linking Format) Image decoder.
 */
public class ELFImageFormat extends Format {

    private static final Pattern[] NAME_PATTERNS = {};

    private static final FormatStruct[] HEADERS = { ELFIdent.THIS };

    private static final FormatStruct[] TRAILERS = {};

    /**
	 * Construct <code>ELFImageFormat</code>.
	 */
    public ELFImageFormat() {
        super(ELFImage.NAME, ByteOrder.LITTLE_ENDIAN, NAME_PATTERNS, HEADERS, TRAILERS);
    }

    @Override
    public FileScannerResultNode decode(FileScannerResultNode parent, long position) throws IOException {
        final FileScannerResultNode decoded = parent.addFormat(ELFImage.NAME, position, position);
        final ELFImageFormatContext context = new ELFImageFormatContext();
        try {
            context.baseNode = decoded;
            context.baseoff = position;
            HEADERS[0].decode(decoded, position);
        } catch (final InvalidDataException e) {
            decoded.addDataHandler(new ExceptionTransferHandler(decoded, e));
        } finally {
            context.dispose();
        }
        return FileScannerResultNode.purgeDecodedNode(decoded);
    }
}
