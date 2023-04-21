package org.jcvi.vics.shared.tasks;

import java.io.File;

public class GenbankFileInfo implements Comparable {

    private Long genomeProjectNodeId;

    private File genbankFile;

    private Long length;

    private Long lengthWithoutGaps;

    public GenbankFileInfo(Long genomeProjectNodeId, File genbankFile, Long length, Long lengthMinusGaps) {
        this.genomeProjectNodeId = genomeProjectNodeId;
        this.genbankFile = genbankFile;
        this.length = length;
        this.lengthWithoutGaps = lengthMinusGaps;
    }

    public Long getGenomeProjectNodeId() {
        return genomeProjectNodeId;
    }

    public File getGenbankFile() {
        return genbankFile;
    }

    public Long getLength() {
        return length;
    }

    public Long getLengthWithoutGaps() {
        return lengthWithoutGaps;
    }

    public int compareTo(Object o) {
        GenbankFileInfo gfi2 = (GenbankFileInfo) o;
        return this.length.compareTo(gfi2.getLength());
    }
}
