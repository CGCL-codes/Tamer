package com.dgtalize.netc.visual;

import javax.swing.ImageIcon;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.dgtalize.netc.visual.smiles.Smile;

/**
 *
 * @author DGtalize
 */
public class ChatWindow {

    public static final String STYLE_NICKNAMES = "Nicknames";

    public static final String STYLE_CHATTEXT = "ChatText";

    public static final String STYLE_SYSMSG = "SystemMessage";

    public static void putSmileOnText(StyledDocument styDoc, Smile smile) throws Exception {
        String texto = styDoc.getText(0, styDoc.getLength());
        int posText = -1;
        while ((posText = texto.indexOf(smile.getCode(), posText + 1)) != -1) {
            styDoc.remove(posText, smile.getCode().length());
            Style imgStyle;
            if ((imgStyle = styDoc.getStyle("sml_" + smile.getCode())) == null) {
                imgStyle = styDoc.addStyle("sml_" + smile.getCode(), null);
            }
            StyleConstants.setIcon(imgStyle, new ImageIcon(smile.getPath()));
            styDoc.insertString(posText, smile.getCode(), imgStyle);
        }
    }
}
