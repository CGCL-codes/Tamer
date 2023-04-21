package com.anotherbigidea.flash.movie;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.interfaces.SWFVectors;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;

/**
 * Font loading utilities
 */
public class FontLoader extends SWFTagTypesImpl {

    protected FontDefinition fontDef;

    public FontLoader() {
        super(null);
    }

    /**
     * Load the first font from the given Flash movie
     * 
     * @return null if no font was found
     */
    public static FontDefinition loadFont(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        FontDefinition def = loadFont(in);
        in.close();
        return def;
    }

    /**
     * Load the first font from the given Flash movie
     * 
     * @return null if no font was found
     */
    public static FontDefinition loadFont(InputStream flashMovie) throws IOException {
        FontLoader fontloader = new FontLoader();
        SWFTags swfparser = new TagParser(fontloader);
        SWFReader swfreader = new SWFReader(swfparser, flashMovie);
        swfreader.readFile();
        return fontloader.fontDef;
    }

    /**
     * SWFTagTypes Interface
     */
    @Override
    public SWFVectors tagDefineFont2(int id, int flags, String name, int numGlyphs, int ascent, int descent, int leading, int[] codes, int[] advances, Rect[] bounds, int[] kernCodes1, int[] kernCodes2, int[] kernAdjustments) throws IOException {
        if (fontDef != null) return null;
        double twips = SWFConstants.TWIPS;
        fontDef = new FontDefinition(name, (ascent) / twips, (descent) / twips, (leading) / twips, (flags & SWFConstants.FONT2_SMALLTEXT) != 0, (flags & SWFConstants.FONT2_SHIFTJIS) != 0, (flags & SWFConstants.FONT2_ANSI) != 0, (flags & SWFConstants.FONT2_ITALIC) != 0, (flags & SWFConstants.FONT2_BOLD) != 0, (flags & SWFConstants.FONT2_HAS_LAYOUT) != 0);
        if (kernCodes1 != null && kernCodes1.length > 0) {
            ArrayList kerns = fontDef.getKerningPairList();
            for (int i = 0; i < kernCodes1.length; i++) {
                FontDefinition.KerningPair pair = new FontDefinition.KerningPair(kernCodes1[i], kernCodes2[i], (kernAdjustments[i]) / twips);
                kerns.add(pair);
            }
        }
        return new VectorImpl(codes, advances, bounds);
    }

    protected class VectorImpl implements SWFVectors {

        protected int[] codes;

        protected int[] advances;

        protected Rect[] bounds;

        protected int i;

        protected Shape shape;

        protected int currx;

        protected int curry;

        protected double twips = SWFConstants.TWIPS;

        protected VectorImpl(int[] codes, int[] advances, Rect[] bounds) {
            this.codes = codes;
            this.advances = advances;
            this.bounds = bounds;
            i = 0;
            shape = new Shape();
        }

        public void done() {
            double advance = (advances == null) ? 0.0 : (advances[i]) / twips;
            int code = codes[i];
            Rect rect = bounds[i];
            shape.minX = (rect.getMinX()) / twips;
            shape.minY = (rect.getMinY()) / twips;
            shape.maxX = (rect.getMaxX()) / twips;
            shape.maxY = (rect.getMaxY()) / twips;
            FontDefinition.Glyph g = new FontDefinition.Glyph(shape, advance, code);
            fontDef.getGlyphList().add(g);
            i++;
            if (i < codes.length) shape = new Shape();
            currx = curry = 0;
        }

        public void line(int dx, int dy) {
            currx += dx;
            curry += dy;
            shape.line(currx / twips, curry / twips);
        }

        public void curve(int cx, int cy, int dx, int dy) {
            cx += currx;
            cy += curry;
            dx += cx;
            dy += cy;
            currx = dx;
            curry = dy;
            shape.curve(dx / twips, dy / twips, cx / twips, cy / twips);
        }

        public void move(int x, int y) {
            currx = x;
            curry = y;
            shape.move(x / twips, y / twips);
        }
    }
}
