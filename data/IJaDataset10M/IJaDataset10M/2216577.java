package fi.tkk.ics.hadoop.bam.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Arrays;

/** An indexing tool for BGZF-compressed files, making them palatable to {@link
 * BGZFSplitFileInputFormat}. Writes BGZF block indices as understood by {@link
 * BGZFBlockIndex}.
 */
public final class BGZFBlockIndexer {

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Usage: BGZFBlockIndexer GRANULARITY [BGZF files...]\n\n" + "Writes, for each GRANULARITY gzip blocks in a BGZF file, its " + "virtual file offset\nas a big-endian 48-bit integer into " + "[filename].bgzfi. The file is terminated by\nthe BGZF file's " + "length, in the same format.");
            return;
        }
        int granularity;
        try {
            granularity = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            granularity = 0;
        }
        if (granularity <= 0) {
            System.err.printf("Granularity must be a positive integer, not '%s'!\n", args[0]);
            return;
        }
        final BGZFBlockIndexer indexer = new BGZFBlockIndexer(granularity);
        for (final String arg : Arrays.asList(args).subList(1, args.length)) {
            final File f = new File(arg);
            if (f.isFile() && f.canRead()) {
                System.out.printf("Indexing %s...", f);
                try {
                    indexer.index(f);
                    System.out.println(" done.");
                } catch (IOException e) {
                    System.out.println(" FAILED!");
                    e.printStackTrace();
                }
            } else System.err.printf("%s does not look like a file, won't index!\n", f);
        }
    }

    private final ByteBuffer byteBuffer;

    private final int granularity;

    private int pos = 0;

    private static final int PRINT_EVERY = 500 * 1024 * 1024;

    public BGZFBlockIndexer(int g) {
        granularity = g;
        byteBuffer = ByteBuffer.allocate(8);
    }

    private void index(final File file) throws IOException {
        final InputStream in = new FileInputStream(file);
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getPath() + ".bgzfi"));
        final LongBuffer lb = byteBuffer.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        long prevPrint = 0;
        pos = 0;
        for (int i = 0; ; ) {
            if (!skipBlock(in)) break;
            if (++i == granularity) {
                i = 0;
                lb.put(0, pos);
                out.write(byteBuffer.array(), 2, 6);
                if (pos - prevPrint >= PRINT_EVERY) {
                    System.out.print("-");
                    prevPrint = pos;
                }
            }
        }
        lb.put(0, file.length());
        out.write(byteBuffer.array(), 2, 6);
        out.close();
        in.close();
    }

    private boolean skipBlock(final InputStream in) throws IOException {
        final int read = readBytes(in, 4);
        if (read != 4) {
            if (read == 0) return false;
            ioError("Invalid gzip header: too short, no ID/CM/FLG");
        }
        final int magic = byteBuffer.order(ByteOrder.BIG_ENDIAN).getInt(0);
        if (magic != 0x1f8b0804) ioError("Invalid gzip header: bad ID/CM/FLG %#x != 0x1f8b0804", magic);
        if (!readExactlyBytes(in, 8)) ioError("Invalid gzip header: too short, no XLEN");
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        final int xlen = getUshort(6);
        for (int offset = 0; offset < xlen; ) {
            if (!readExactlyBytes(in, 4)) ioError("Invalid subfields: EOF after %d subfield bytes", offset);
            offset += 4;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            final int siAndSlen = byteBuffer.getInt(0);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            if ((siAndSlen & ~0xff) == 0x42430200) {
                if (!readExactlyBytes(in, 2)) ioError("Invalid BGZF subfield: missing BSIZE");
                offset += 2;
                final int bsize = getUshort(0);
                fullySkip(in, (xlen - offset) + (bsize - xlen - 19) + 8);
                pos += bsize + 1;
                return true;
            } else {
                final int slen = getUshort(2);
                fullySkip(in, slen);
                offset += slen;
            }
        }
        throw new IOException("Invalid BGZF file: block without BGZF subfield");
    }

    private int getUshort(final int idx) {
        return (int) byteBuffer.getShort(idx) & 0xffff;
    }

    private void fullySkip(final InputStream in, final int skip) throws IOException {
        for (int s = skip; s > 0; ) {
            final long skipped = in.skip(s);
            if (skipped == 0) throw new IOException("Skip failed");
            s -= skipped;
        }
    }

    private int readBytes(final InputStream in, final int n) throws IOException {
        assert n <= byteBuffer.capacity();
        int read = 0;
        while (read < n) {
            final int readNow = in.read(byteBuffer.array(), read, n - read);
            if (readNow <= 0) break;
            read += readNow;
        }
        return read;
    }

    private boolean readExactlyBytes(final InputStream in, final int n) throws IOException {
        return readBytes(in, n) == n;
    }

    private void ioError(String s, Object... va) throws IOException {
        throw new IOException(String.format(s, va));
    }
}
