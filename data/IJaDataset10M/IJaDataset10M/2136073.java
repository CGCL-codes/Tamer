package org.proteusframework.core.api.model;

public interface IGlyphRenderer {

    /**
     * Renders an IGlyph using a native platform type representation. On a Java SE platform, this would likely by
     * <code>java.awt.BufferedImage</code>, whereas on an Android device it would be
     * <code>android.graphics.Bitmap</code>.
     *
     * @param glyph              Glyph to render
     * @param targetRenderedType Target type
     * @param <T>                Specifies the target type of the rendering operation
     * @return Rendered glyph
     */
    <T> T renderGlyph(IGlyph glyph, Class<T> targetRenderedType);
}
