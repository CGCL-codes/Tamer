package com.spukmk2me.video;

import com.spukmk2me.Util;

/**
 *  Interface for character font renderer.
 */
public abstract class ICFontRenderer {

    protected ICFontRenderer(IVideoDriver vdriver) {
        m_vdriver = vdriver;
    }

    protected abstract void RenderCharacter(ICFont font, char character);

    public final void RenderString(char[] s, ICFont font, int offset, int length, int x, int y) {
        if (s == null) return;
        int border = offset + length;
        if (border > s.length) border = s.length;
        if (offset >= border) return;
        short clipX, clipY, clipW, clipH;
        char character;
        boolean additionalSpace = false;
        {
            long clipping = m_vdriver.GetClipping();
            clipX = (short) ((clipping >> 48) & 0x0000FFFF);
            if ((clipX & 0x00008000) != 0) clipX |= 0xFFFF0000;
            clipY = (short) ((clipping >> 32) & 0x0000FFFF);
            if ((clipY & 0x00008000) != 0) clipY |= 0xFFFF0000;
            clipW = (short) ((clipping >> 16) & 0x0000FFFF);
            if ((clipW & 0x00008000) != 0) clipW |= 0xFFFF0000;
            clipH = (short) (clipping & 0x0000FFFF);
            if ((clipH & 0x00008000) != 0) clipH |= 0xFFFF0000;
        }
        m_rasterX = (short) x;
        m_rasterY = (short) y;
        m_charHeight = (short) font.GetLineHeight();
        while (offset != border) {
            character = s[offset];
            if (character == '\n') {
                m_rasterX = (short) x;
                m_rasterY += m_charHeight;
                additionalSpace = false;
            } else {
                if (character != ' ') {
                    m_charWidth = (short) font.GetCharWidth(character);
                    if (Util.RectIntersect(m_rasterX, m_rasterY, m_charWidth, m_charHeight, clipX, clipY, clipW, clipH)) RenderCharacter(font, character);
                    m_rasterX += font.GetSpaceBetweenCharacters() + m_charWidth;
                    additionalSpace = true;
                } else {
                    if (additionalSpace) m_rasterX -= font.GetSpaceBetweenCharacters();
                    m_rasterX += font.GetCharWidth(' ');
                    additionalSpace = false;
                }
            }
            ++offset;
        }
    }

    public final void RenderString(String s, ICFont font, int offset, int length, int x, int y) {
        if (s == null) return;
        int borderOffset = Math.min(offset + length, s.length());
        char[] buffer = new char[borderOffset - offset];
        s.getChars(offset, borderOffset, buffer, 0);
        RenderString(buffer, font, offset, borderOffset - offset, x, y);
    }

    protected IVideoDriver m_vdriver;

    protected short m_rasterX, m_rasterY, m_charWidth, m_charHeight;
}
