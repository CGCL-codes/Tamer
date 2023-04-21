package org.designerator.image.jpeg;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class PngPlteChunk extends PngChunk {

    int paletteSize;

    PngPlteChunk(PaletteData palette) {
        super(palette.getRGBs().length * 3);
        paletteSize = length / 3;
        setType(TYPE_PLTE);
        setPaletteData(palette);
        setCRC(computeCRC());
    }

    PngPlteChunk(byte[] reference) {
        super(reference);
        paletteSize = length / 3;
    }

    int getChunkType() {
        return CHUNK_PLTE;
    }

    /**
 * Get the number of colors in this palette.
 */
    int getPaletteSize() {
        return paletteSize;
    }

    /**
 * Get a PaletteData object representing the colors
 * stored in this PLTE chunk.
 * The result should be cached as the PLTE chunk
 * does not store the palette data created.
 */
    PaletteData getPaletteData() {
        RGB[] rgbs = new RGB[paletteSize];
        for (int i = 0; i < rgbs.length; i++) {
            int offset = DATA_OFFSET + (i * 3);
            int red = reference[offset] & 0xFF;
            int green = reference[offset + 1] & 0xFF;
            int blue = reference[offset + 2] & 0xFF;
            rgbs[i] = new RGB(red, green, blue);
        }
        return new PaletteData(rgbs);
    }

    /**
 * Set the data of a PLTE chunk to the colors
 * stored in the specified PaletteData object.
 */
    void setPaletteData(PaletteData palette) {
        RGB[] rgbs = palette.getRGBs();
        for (int i = 0; i < rgbs.length; i++) {
            int offset = DATA_OFFSET + (i * 3);
            reference[offset] = (byte) rgbs[i].red;
            reference[offset + 1] = (byte) rgbs[i].green;
            reference[offset + 2] = (byte) rgbs[i].blue;
        }
    }

    /**
 * Answer whether the chunk is a valid PLTE chunk.
 */
    void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
        if (!readState.readIHDR || readState.readPLTE || readState.readTRNS || readState.readIDAT || readState.readIEND) {
            SWT.error(SWT.ERROR_INVALID_IMAGE);
        } else {
            readState.readPLTE = true;
        }
        super.validate(readState, headerChunk);
        if (getLength() % 3 != 0) SWT.error(SWT.ERROR_INVALID_IMAGE);
        if (1 << headerChunk.getBitDepth() < paletteSize) {
            SWT.error(SWT.ERROR_INVALID_IMAGE);
        }
        if (256 < paletteSize) SWT.error(SWT.ERROR_INVALID_IMAGE);
    }

    void contributeToString(StringBuffer buffer) {
        buffer.append("\n\tPalette size:");
        buffer.append(paletteSize);
    }
}
