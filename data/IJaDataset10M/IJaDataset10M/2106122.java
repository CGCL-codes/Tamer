package com.aelitis.azureus.ui.swt.skin;

import org.eclipse.swt.graphics.Color;

/**
 * @author TuxPaper
 * @created Aug 4, 2006
 *
 */
public interface SWTSkinObjectText extends SWTSkinObject {

    public void setText(String text);

    public void setTextID(String id);

    public void setTextID(String id, String[] params);

    /**
	 * @return
	 *
	 * @since 3.1.1.1
	 */
    int getStyle();

    /**
	 * @param style
	 *
	 * @since 3.1.1.1
	 */
    void setStyle(int style);

    /**
	 * @return
	 *
	 * @since 4.1.0.5
	 */
    public String getText();

    /**
	 * @param l
	 *
	 * @since 4.2.0.7
	 */
    void addUrlClickedListener(SWTSkinObjectText_UrlClickedListener l);

    /**
	 * @param l
	 *
	 * @since 4.2.0.7
	 */
    void removeUrlClickedListener(SWTSkinObjectText_UrlClickedListener l);

    public void setTextColor(Color color);
}
