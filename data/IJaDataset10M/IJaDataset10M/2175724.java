package org.tigr.common.sequencingData.chromatogram.ztr.chunk;

import java.io.InputStream;
import org.tigr.common.sequencingData.chromatogram.Chromatogram;
import org.tigr.common.sequencingData.chromatogram.ChromatogramFileParserException;
import org.tigr.common.sequencingData.chromatogram.EncodedByteData;

/**
 * Implementation of the CNF Chunk type in the ZTR format.<p>
 * (from ZTR RFC :)<p>
 * The first byte of this chunk is 0 (raw format). This is then followed by a
series confidence values for the called base. Next comes all the remaining
confidence values for A, C, G and T excluding those that have already been
written (ie the called base). So for a sequence AGT we would store confidences
A1 G2 T3 C1 G1 T1 A2 C2 T2 A3 C3 G3.<p>

The purpose of this is to group the (likely) highest confidence value (those
for the called base) at the start of the chunk followed by the remaining
values. Hence if phred confidence values are written in a CNF4 chunk the first
quarter of chunk will consist of phred confidence values and the last three
quarters will (assuming no ambiguous base calls) consist entirely of zeros.
<p>
For the purposes of storage the confidence value for a base call that is not
A, C, G or T (in any case) is stored as if the base call was T.
<p>
The confidence values should be from the "-10 * log10 (1-probability)". These
values are then converted to their nearest integral value.
If a program wishes to store confidence values in a different range then this
should be stored in a different chunk type.
<p>
If this chunk exists it must exist after a BASE chunk.

 * @author dkatzel
 *
 *
 */
public class CNFChunk extends Chunk {

    public CNFChunk(InputStream inputStream) throws ChromatogramFileParserException {
        super(inputStream);
    }

    @Override
    protected void parseData(byte[] unEncodedData, Chromatogram chromatogram) throws ChromatogramFileParserException {
        String basecalls = chromatogram.getBasecalls();
        int nBases = basecalls.length();
        byte aConfidence[] = new byte[nBases];
        byte cConfidence[] = new byte[nBases];
        byte gConfidence[] = new byte[nBases];
        byte tConfidence[] = new byte[nBases];
        int j = nBases + 1;
        for (int i = 0; i < nBases; i++) {
            char currentChar = basecalls.charAt(i);
            if (currentChar == 'a' || currentChar == 'A') {
                aConfidence[i] = unEncodedData[i + 1];
                cConfidence[i] = unEncodedData[j++];
                gConfidence[i] = unEncodedData[j++];
                tConfidence[i] = unEncodedData[j++];
            } else if (currentChar == 'c' || currentChar == 'C') {
                aConfidence[i] = unEncodedData[j++];
                cConfidence[i] = unEncodedData[i + 1];
                gConfidence[i] = unEncodedData[j++];
                tConfidence[i] = unEncodedData[j++];
            } else if (currentChar == 'g' || currentChar == 'G') {
                aConfidence[i] = unEncodedData[j++];
                cConfidence[i] = unEncodedData[j++];
                gConfidence[i] = unEncodedData[i + 1];
                tConfidence[i] = unEncodedData[j++];
            } else {
                aConfidence[i] = unEncodedData[j++];
                cConfidence[i] = unEncodedData[j++];
                gConfidence[i] = unEncodedData[j++];
                tConfidence[i] = unEncodedData[i + 1];
            }
        }
        chromatogram.setAConfidence(new EncodedByteData(aConfidence));
        chromatogram.setCConfidence(new EncodedByteData(cConfidence));
        chromatogram.setGConfidence(new EncodedByteData(gConfidence));
        chromatogram.setTConfidence(new EncodedByteData(tConfidence));
    }
}
