package sun.font;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

public abstract class FontScaler implements DisposerRecord {

    private static FontScaler nullScaler = null;

    private static Constructor<FontScaler> scalerConstructor = null;

    static {
        Class scalerClass = null;
        Class arglst[] = new Class[] { Font2D.class, int.class, boolean.class, int.class };
        try {
            if (FontUtilities.isOpenJDK) {
                scalerClass = Class.forName("sun.font.FreetypeFontScaler");
            } else {
                scalerClass = Class.forName("sun.font.T2KFontScaler");
            }
        } catch (ClassNotFoundException e) {
            scalerClass = NullFontScaler.class;
        }
        try {
            scalerConstructor = scalerClass.getConstructor(arglst);
        } catch (NoSuchMethodException e) {
        }
    }

    public static FontScaler getScaler(Font2D font, int indexInCollection, boolean supportsCJK, int filesize) {
        FontScaler scaler = null;
        try {
            Object args[] = new Object[] { font, indexInCollection, supportsCJK, filesize };
            scaler = scalerConstructor.newInstance(args);
            Disposer.addObjectRecord(font, scaler);
        } catch (Throwable e) {
            scaler = nullScaler;
            FontManager fm = FontManagerFactory.getInstance();
            fm.deRegisterBadFont(font);
        }
        return scaler;
    }

    public static synchronized FontScaler getNullScaler() {
        if (nullScaler == null) {
            nullScaler = new NullFontScaler();
        }
        return nullScaler;
    }

    protected WeakReference<Font2D> font = null;

    protected long nativeScaler = 0;

    protected boolean disposed = false;

    abstract StrikeMetrics getFontMetrics(long pScalerContext) throws FontScalerException;

    abstract float getGlyphAdvance(long pScalerContext, int glyphCode) throws FontScalerException;

    abstract void getGlyphMetrics(long pScalerContext, int glyphCode, Point2D.Float metrics) throws FontScalerException;

    abstract long getGlyphImage(long pScalerContext, int glyphCode) throws FontScalerException;

    abstract Rectangle2D.Float getGlyphOutlineBounds(long pContext, int glyphCode) throws FontScalerException;

    abstract GeneralPath getGlyphOutline(long pScalerContext, int glyphCode, float x, float y) throws FontScalerException;

    abstract GeneralPath getGlyphVectorOutline(long pScalerContext, int[] glyphs, int numGlyphs, float x, float y) throws FontScalerException;

    public void dispose() {
    }

    abstract int getNumGlyphs() throws FontScalerException;

    abstract int getMissingGlyphCode() throws FontScalerException;

    abstract int getGlyphCode(char charCode) throws FontScalerException;

    abstract long getLayoutTableCache() throws FontScalerException;

    abstract Point2D.Float getGlyphPoint(long pScalerContext, int glyphCode, int ptNumber) throws FontScalerException;

    abstract long getUnitsPerEm();

    abstract long createScalerContext(double[] matrix, boolean fontType, int aa, int fm, float boldness, float italic);

    abstract void invalidateScalerContext(long ppScalerContext);
}
