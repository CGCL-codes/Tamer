package org.openXpertya.print.pdf.text.pdf;

import java.util.ArrayList;
import org.openXpertya.print.pdf.text.Chunk;
import org.openXpertya.print.pdf.text.Font;
import org.openXpertya.print.pdf.text.Phrase;

/** Selects the appropriate fonts that contain the glyphs needed to
 * render text correctly. The fonts are checked in order until the 
 * character is found.
 * <p>
 * The built in fonts "Symbol" and "ZapfDingbats", if used, have a special encoding
 * to allow the characters to be referred by Unicode.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class FontSelector {

    protected ArrayList fonts = new ArrayList();

    /**
     * Adds a <CODE>Font</CODE> to be searched for valid characters.
     * @param font the <CODE>Font</CODE>
     */
    public void addFont(Font font) {
        if (font.getBaseFont() != null) {
            fonts.add(font);
            return;
        }
        BaseFont bf = font.getCalculatedBaseFont(true);
        Font f2 = new Font(bf, font.size(), font.getCalculatedStyle(), font.color());
        fonts.add(f2);
    }

    /**
     * Process the text so that it will render with a combination of fonts
     * if needed.
     * @param text the text
     * @return a <CODE>Phrase</CODE> with one or more chunks
     */
    public Phrase process(String text) {
        int fsize = fonts.size();
        if (fsize == 0) throw new IndexOutOfBoundsException("No font is defined.");
        char cc[] = text.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        Font font = null;
        int lastidx = -1;
        Phrase ret = new Phrase();
        for (int k = 0; k < len; ++k) {
            char c = cc[k];
            if (c == '\n' || c == '\r') {
                sb.append(c);
                continue;
            }
            for (int f = 0; f < fsize; ++f) {
                font = (Font) fonts.get(f);
                if (font.getBaseFont().charExists(c)) {
                    if (lastidx == f) sb.append(c); else {
                        if (sb.length() > 0 && lastidx != -1) {
                            Chunk ck = new Chunk(sb.toString(), (Font) fonts.get(lastidx));
                            ret.add(ck);
                            sb = new StringBuffer();
                        }
                        sb.append(c);
                        lastidx = f;
                    }
                    break;
                }
            }
        }
        if (sb.length() > 0) {
            Chunk ck = new Chunk(sb.toString(), (Font) fonts.get(lastidx));
            ret.add(ck);
        }
        return ret;
    }
}
