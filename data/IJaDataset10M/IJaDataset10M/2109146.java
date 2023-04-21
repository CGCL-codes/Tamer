package org.jcvi.common.core.assembly.tasm;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.util.iter.CloseableIterator;

/**
 * {@code TigrAssemblerWriter} writes out TIGR Assembler
 * formated files (.tasm).  This assembly format 
 * probably does not have much use outside of 
 * JCVI since the format is specially tailored to the 
 * legacy TIGR Project Database.
 * @author dkatzel
 *
 */
public final class TigrAssemblerWriter {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final byte[] BLANK_LINE = "\n".getBytes(UTF_8);

    private static final byte[] CONTIG_SEPARATOR = "|\n".getBytes(UTF_8);

    public static void writeContigSeparator(OutputStream out) throws IOException {
        out.write(CONTIG_SEPARATOR);
    }

    public static void write(TigrAssemblerContigDataStore datastore, OutputStream out) throws IOException {
        if (datastore == null) {
            throw new NullPointerException("data store can not be null");
        }
        Iterator<String> contigIds;
        try {
            contigIds = datastore.idIterator();
            while (contigIds.hasNext()) {
                TigrAssemblerContig contig = datastore.get(contigIds.next());
                write(contig, out);
                if (contigIds.hasNext()) {
                    out.write(CONTIG_SEPARATOR);
                }
            }
        } catch (DataStoreException e) {
            throw new IOException("error writing tasm file", e);
        }
    }

    public static void write(TigrAssemblerContig contig, OutputStream out) throws IOException {
        for (TigrAssemblerContigAttribute contigAttribute : TigrAssemblerContigAttribute.values()) {
            if (contig.hasAttribute(contigAttribute)) {
                String assemblyTableColumn = contigAttribute.getAssemblyTableColumn();
                StringBuilder row = new StringBuilder(assemblyTableColumn);
                int padding = 4 - assemblyTableColumn.length() % 4;
                if (padding > 0) {
                    row.append("\t");
                }
                row.append(String.format("%s\n", contig.getAttributeValue(contigAttribute)));
                out.write(row.toString().getBytes(UTF_8));
            }
        }
        if (contig.getNumberOfReads() > 0) {
            out.write(BLANK_LINE);
            CloseableIterator<TigrAssemblerPlacedRead> placedReadIterator = null;
            try {
                placedReadIterator = contig.getReadIterator();
                while (placedReadIterator.hasNext()) {
                    TigrAssemblerPlacedRead read = placedReadIterator.next();
                    for (TigrAssemblerReadAttribute readAttribute : TigrAssemblerReadAttribute.values()) {
                        String assemblyTableColumn = readAttribute.getAssemblyTableColumn();
                        int padding = 4 - assemblyTableColumn.length() % 4;
                        StringBuilder row = new StringBuilder(assemblyTableColumn);
                        if (padding > 0) {
                            row.append("\t");
                        }
                        if (read.hasAttribute(readAttribute)) {
                            row.append(String.format("%s\n", read.getAttributeValue(readAttribute)));
                        } else {
                            row.append("\n");
                        }
                        out.write(row.toString().getBytes(UTF_8));
                    }
                    if (placedReadIterator.hasNext()) {
                        out.write(BLANK_LINE);
                    }
                }
            } finally {
                IOUtil.closeAndIgnoreErrors(placedReadIterator);
            }
        }
    }
}
