package net.sourceforge.jffmpeg.codecs.audio.vorbis.residue;

import net.sourceforge.jffmpeg.codecs.audio.vorbis.CodeBook;
import net.sourceforge.jffmpeg.codecs.audio.vorbis.OggReader;

public class Residue2 extends Residue0 {

    public void inverse(OggReader oggRead, float[][] in, int[] nonZero, int ch) {
        int samples_per_partition = grouping;
        int partitions_per_word = phrasebook.getDim();
        int n = end - begin;
        int partvals = n / samples_per_partition;
        int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        long[][] partword = new long[partwords][];
        int i;
        for (i = 0; i < ch; i++) if (nonZero[i] != 0) break;
        if (i == ch) return;
        for (int s = 0; s < stages; s++) {
            int l = 0;
            for (i = 0; i < partvals; l++) {
                if (s == 0) {
                    int temp = phrasebook.decode(oggRead);
                    partword[l] = decodemap[temp];
                }
                for (int k = 0; k < partitions_per_word && i < partvals; k++, i++) {
                    if ((secondstages[(int) partword[l][k]] & (1 << s)) != 0) {
                        CodeBook stagebook = partbooks[(int) partword[l][k]][s];
                        if (stagebook != null) {
                            stagebook.decodevv_add(in, i * samples_per_partition + begin, ch, oggRead, samples_per_partition);
                        }
                    }
                }
            }
        }
    }
}
