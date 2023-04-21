package org.jcvi.common.core.align.pairwise;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.symbol.residue.aa.AminoAcid;
import org.jcvi.common.core.symbol.residue.aa.AminoAcids;

public class PropertyFileAminoAcidScoringMatrix implements AminoAcidScoringMatrix {

    private final float[][] matrix;

    public PropertyFileAminoAcidScoringMatrix(InputStream in) {
        Scanner scanner = new Scanner(in, IOUtil.UTF_8_NAME);
        List<AminoAcid> header = AminoAcids.parse(scanner.nextLine().replaceAll("\\s+", ""));
        int n = AminoAcid.values().length;
        matrix = new float[n][n];
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                AminoAcid aa = AminoAcid.parse(lineScanner.next());
                int x = aa.ordinal();
                for (int i = 0; i < header.size(); i++) {
                    float value = lineScanner.nextFloat();
                    int y = header.get(i).ordinal();
                    matrix[x][y] = value;
                }
            }
        }
        scanner.close();
    }

    @Override
    public float getScore(AminoAcid a, AminoAcid b) {
        return getScore(a.getOrdinalAsByte(), b.getOrdinalAsByte());
    }

    private float getScore(byte a, byte b) {
        return matrix[a][b];
    }
}
