package com.google.gdt.eclipse.platform.ui;

import com.google.gdt.eclipse.platform.shared.ui.IPixelConverter;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;

/**
 * Implementation of the generic {@link IPixelConverter} interface for Eclipse
 * 3.7.
 */
public class PixelConverterImpl implements IPixelConverter {

    private PixelConverter pixelConverter;

    public PixelConverterImpl(Control control) {
        pixelConverter = new PixelConverter(control);
    }

    public PixelConverterImpl(Font font) {
        pixelConverter = new PixelConverter(font);
    }

    public int convertHeightInCharsToPixels(int chars) {
        return pixelConverter.convertHeightInCharsToPixels(chars);
    }

    public int convertWidthInCharsToPixels(int chars) {
        return pixelConverter.convertWidthInCharsToPixels(chars);
    }
}
