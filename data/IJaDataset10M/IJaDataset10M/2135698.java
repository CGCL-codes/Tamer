package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;

public class Loca extends Table {

    /**points to location of glyph programs*/
    int[] glyphIndexStart;

    public Loca(FontFile2 currentFontFile, int glyphCount, int format) {
        int startPointer = currentFontFile.selectTable(FontFile2.LOCA);
        int i = 0;
        int locaLength = currentFontFile.getOffset(FontFile2.LOCA);
        glyphIndexStart = new int[glyphCount + 1];
        if (startPointer != 0) {
            glyphIndexStart[0] = 0;
            if (format == 1) {
                if ((locaLength / 4) != (glyphCount + 1)) LogWriter.writeLog("Incorrect length");
                for (i = 0; i < glyphCount; i++) glyphIndexStart[i] = currentFontFile.getNextUint32();
            } else {
                if ((locaLength / 2) != (glyphCount + 1)) LogWriter.writeLog("Incorrect length");
                for (i = 0; i < glyphCount; i++) glyphIndexStart[i] = (currentFontFile.getNextUint16() * 2);
            }
            glyphIndexStart[glyphCount] = currentFontFile.getOffset(FontFile2.GLYF);
        }
    }

    public int[] getIndices() {
        return glyphIndexStart;
    }
}
