package gnu.java.awt.font.opentype.truetype;

import java.awt.FontFormatException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

/**
 * Locates glyph outlines in a TrueType or OpenType <code>glyf</code> table.
 * 
 * @see <a href= "http://partners.adobe.com/asn/tech/type/opentype/loca.html"
 *      >Adobe&#x0027;s specification of the OpenType &#x0027;loca&#x0027;
 *      table</a>
 * 
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
abstract class GlyphLocator {

    /**
     * The actual glyph data of the font, which is contained in the 'glyf'
     * table.
     */
    protected ByteBuffer glyfTable;

    /**
     * @param format
     *            the format of the <code>loca</code> table. The value must be 0
     *            for two-byte offsets or 1 for four-byte offsets. TrueType and
     *            OpenType fonts indicate the format in the
     *            <code>indexToLoc</code> field of the <a href=
     *            "http://partners.adobe.com/asn/tech/type/opentype/head.html"
     *            >font header</a>.
     * @param loca
     *            the <code>loca</code> table of the font, which contains the
     *            position of each glyph in the <code>glyf</code> table.
     * @param glyf
     *            the <code>glyf</code> table of the font, which contains the
     *            outline data of each glyph.
     * @return a new GlyphLocator for the specified <code>loca</code> table.
     * @throws FontFormatException
     *             if <code>format</code> is neither 0 nor 1.
     */
    public static GlyphLocator forTable(int format, ByteBuffer loca, ByteBuffer glyf) throws FontFormatException {
        switch(format) {
            case 0:
                return new GlyphLocator.TwoByte(loca, glyf);
            case 1:
                return new GlyphLocator.FourByte(loca, glyf);
            default:
                throw new FontFormatException("unsupported loca format");
        }
    }

    /**
     * Locates the outline data for a glyph.
     * <p>
     * For efficiency, the glyph locator does not create a new buffer for each
     * invocation. Instead, this method always returns the same buffer object.
     * Therefore, the data of a glyph must have been read completely before
     * another glyph of the same font gets requested through this method.
     * 
     * @param glyph
     *            the number of the glyph whose outlines are to be retrieved.
     * @return a buffer whose position is set to the first byte of glyph data,
     *         and whose limit is set to disallow accessing any data that does
     *         not belong to the glyph. If there is no outline data for the
     *         requested glyph, as would be the case for the space glyph, the
     *         result will be <code>null</code>.
     */
    public abstract ByteBuffer getGlyphData(int glyph);

    /**
     * A GlyphLocator that locates glyphs using two-byte offsets, interpreting
     * <code>loca</code> tables of format 0.
     */
    private static final class TwoByte extends GlyphLocator {

        final CharBuffer indexToLoc;

        TwoByte(ByteBuffer loca, ByteBuffer glyf) {
            this.glyfTable = glyf;
            this.indexToLoc = loca.asCharBuffer();
        }

        @Override
        public ByteBuffer getGlyphData(int glyph) {
            int offset, limit;
            offset = (this.indexToLoc.get(glyph)) << 1;
            limit = (this.indexToLoc.get(glyph + 1)) << 1;
            if (offset >= limit) {
                return null;
            }
            this.glyfTable.limit(limit).position(offset);
            return this.glyfTable;
        }
    }

    /**
     * A GlyphLocator that locates glyphs using four-byte offsets, interpreting
     * <code>loca</code> tables of format 1.
     */
    private static final class FourByte extends GlyphLocator {

        final IntBuffer indexToLoc;

        FourByte(ByteBuffer loca, ByteBuffer glyf) {
            this.glyfTable = glyf;
            this.indexToLoc = loca.asIntBuffer();
        }

        @Override
        public ByteBuffer getGlyphData(int glyph) {
            int offset, limit;
            offset = this.indexToLoc.get(glyph);
            limit = this.indexToLoc.get(glyph + 1);
            if (offset >= limit) {
                return null;
            }
            this.glyfTable.limit(limit).position(offset);
            return this.glyfTable;
        }
    }
}
