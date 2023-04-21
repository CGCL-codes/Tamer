package fi.tkk.ics.hadoop.bam.custom.samtools;

/**
 * Interface for SAMText and BAM file writers.  Clients need not care which they write to,
 * once the object is constructed.
 */
public interface SAMFileWriter {

    void addAlignment(SAMRecord alignment);

    SAMFileHeader getFileHeader();

    /**
     * Must be called to flush or file will likely be defective. 
     */
    void close();
}
