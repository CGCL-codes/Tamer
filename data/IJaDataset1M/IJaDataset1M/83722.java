package fi.tkk.ics.hadoop.bam.custom.samtools;

/**
 * Constants used in reading & writing BAM files
 */
class BAMFileConstants {

    /**
     * The beginning of a BAMRecord is a fixed-size block of 8 int32s
     */
    static final int FIXED_BLOCK_SIZE = 8 * 4;

    /**
     * Sanity check -- we never expect BAMRecords to be as big as this.
     */
    static final int MAXIMUM_RECORD_LENGTH = 1024 * 1024;

    /**
     * BAM file magic number.  This is what is present in the gunzipped version of the file,
     * which never exists on disk.
     */
    static final byte[] BAM_MAGIC = "BAM\1".getBytes();

    /**
     * BAM index file magic number.
     */
    static final byte[] BAM_INDEX_MAGIC = "BAI\1".getBytes();
}
