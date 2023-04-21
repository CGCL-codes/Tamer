package org.jowidgets.common.widgets;

import org.jowidgets.common.types.Markup;

public interface ITextLabelCommon extends IControlCommon {

    void setFontSize(int size);

    void setFontName(String fontName);

    void setMarkup(Markup markup);

    void setText(String text);
}
