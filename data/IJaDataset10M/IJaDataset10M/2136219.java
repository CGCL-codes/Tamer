package fi.tkk.ics.hadoop.bam.util.hadoop;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import net.sf.samtools.util.SeekableStream;
import fi.tkk.ics.hadoop.bam.custom.samtools.BAMIndex;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMFileHeader;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMFileReader;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMFileSpan;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMFileWriter;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMRecord;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMRecordIterator;
import fi.tkk.ics.hadoop.bam.custom.samtools.SAMTextWriter;
import fi.tkk.ics.hadoop.bam.util.WrapSeekable;

public class BAMReader extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(null, new BAMReader(), args));
    }

    @Override
    public int run(String[] args) throws ClassNotFoundException, IOException {
        if (args.length < 2) {
            System.out.println("Usage: BAMReader PATH referenceIndex:startPos-endPos [...]\n" + "\n" + "Reads the BAM file in PATH, expecting an index at PATH.bai, and " + "for each given\nregion, prints out the SAM records that overlap " + "with that region.\n" + "\n" + "PATH is a local file path if run outside Hadoop and an HDFS " + "path if run within\nit.");
            return args.length == 0 ? 0 : 2;
        }
        final Path path = new Path(args[0]);
        final FileSystem fs = path.getFileSystem(getConf());
        final SAMFileReader reader = new SAMFileReader(WrapSeekable.openPath(fs, path), WrapSeekable.openPath(fs, path.suffix(".bai")), false);
        reader.enableIndexCaching(true);
        final SAMFileHeader header = reader.getFileHeader();
        final BAMIndex index = reader.getIndex();
        final SAMTextWriter writer = new SAMTextWriter(System.out);
        boolean errors = false;
        for (final String arg : Arrays.asList(args).subList(1, args.length)) {
            final StringTokenizer st = new StringTokenizer(arg, ":-");
            final String refStr = st.nextToken();
            final int beg = parseCoordinate(st.nextToken());
            final int end = parseCoordinate(st.nextToken());
            if (beg < 0 || end < 0) {
                errors = true;
                continue;
            }
            int ref = header.getSequenceIndex(refStr);
            if (ref == -1) try {
                ref = Integer.parseInt(refStr);
            } catch (NumberFormatException e) {
                System.err.printf("Not a valid sequence name or index: '%s'\n", refStr);
                errors = true;
                continue;
            }
            final SAMFileSpan span = index.getSpanOverlapping(ref, beg, end);
            if (span == null) continue;
            final SAMRecordIterator it = reader.iterator(span);
            while (it.hasNext()) writer.writeAlignment(it.next());
        }
        writer.close();
        return errors ? 1 : 0;
    }

    private int parseCoordinate(String s) {
        int c;
        try {
            c = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            c = -1;
        }
        if (c < 0) System.err.printf("Not a valid coordinate: '%s'\n", s);
        return c;
    }
}
