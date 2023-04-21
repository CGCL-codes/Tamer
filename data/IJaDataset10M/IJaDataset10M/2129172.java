package org.jcvi.common.core.assembly.clc.cas;

import java.io.File;
import org.jcvi.common.core.seq.fastx.fastq.FastqQualityCodec;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

/**
 * @author dkatzel
 *
 *
 */
public final class TraceDetails {

    private final boolean hasFastaEdits;

    private final File chromatDir;

    private final DateTime phdDate;

    private final FastqQualityCodec fastqQualityCodec;

    public static class Builder implements org.jcvi.common.core.util.Builder<TraceDetails> {

        private boolean hasFastaEdits = false;

        private File chromatDir;

        private DateTime phdDate = null;

        private final FastqQualityCodec fastqQualityCodec;

        public Builder(FastqQualityCodec fastqQualityCodec) {
            if (fastqQualityCodec == null) {
                throw new NullPointerException("can not be null");
            }
            this.fastqQualityCodec = fastqQualityCodec;
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public TraceDetails build() {
            if (phdDate == null) {
                phdDate = new DateTime(DateTimeUtils.currentTimeMillis());
            }
            return new TraceDetails(chromatDir, phdDate, fastqQualityCodec, hasFastaEdits);
        }

        public Builder hasEdits(boolean hasEdits) {
            this.hasFastaEdits = hasEdits;
            return this;
        }

        public Builder phdDate(DateTime phdDate) {
            this.phdDate = phdDate;
            return this;
        }

        public Builder chromatDir(File chromatDir) {
            this.chromatDir = chromatDir;
            return this;
        }
    }

    private TraceDetails(File chromatDir, DateTime phdDate, FastqQualityCodec fastqQualityCodec, boolean hasFastaEdits) {
        this.chromatDir = chromatDir;
        this.phdDate = phdDate;
        this.fastqQualityCodec = fastqQualityCodec;
        this.hasFastaEdits = hasFastaEdits;
    }

    /**
     * @return the hasFastaEdits
     */
    public boolean hasFastaEdits() {
        return hasFastaEdits;
    }

    /**
     * @return the chromatDir
     */
    public File getChromatDir() {
        return chromatDir;
    }

    /**
     * @return the phdDate
     */
    public DateTime getPhdDate() {
        return phdDate;
    }

    /**
     * @return the fastqQualityCodec
     */
    public FastqQualityCodec getFastqQualityCodec() {
        return fastqQualityCodec;
    }

    public boolean hasChromatDir() {
        return chromatDir != null;
    }
}
