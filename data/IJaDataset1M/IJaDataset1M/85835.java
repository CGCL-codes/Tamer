package org.jowidgets.spi.impl.swt.common.font;

import org.eclipse.swt.graphics.Font;

public interface IFontCache {

    Font getFont(FontDataKey fontData);
}
