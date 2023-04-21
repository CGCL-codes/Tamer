package ymsg.support;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MessageDecoderSettings {

    boolean emoticonsOn = false;

    EmoticonLoader emoticonLoader = null;

    String defFontFace = null;

    int defFontSize = -1;

    Color defFg = null;

    String overFontFace = null;

    int overMaxFontSize = -1, overMinFontSize = -1;

    Color overFg = null;

    boolean respectFade = false;

    boolean respectAlt = false;

    Icon getEmoticon(int i) {
        if (emoticonLoader != null) {
            Icon ic = emoticonLoader.loadEmoticon(i);
            if (ic != null) return ic;
        }
        return new ImageIcon(getClass().getResource("images/" + i + ".gif"));
    }

    public void setEmoticonsDecoded(boolean b) {
        emoticonsOn = b;
    }

    public void setEmoticonLoader(EmoticonLoader l) {
        emoticonLoader = l;
    }

    public void setDefaultFontFace(String s) {
        defFontFace = s;
    }

    public void setDefaultFontSize(int sz) {
        defFontSize = sz;
    }

    public void setDefaultForeground(Color col) {
        defFg = col;
    }

    public void setOverrideFontFace(String s) {
        overFontFace = s;
    }

    public void setOverrideMaxFontSize(int sz) {
        overMaxFontSize = sz;
    }

    public void setOverrideMinFontSize(int sz) {
        overMinFontSize = sz;
    }

    public void setOverrideForeground(Color col) {
        overFg = col;
    }

    public void setDefaultFont(String face, int sz, Color fgCol) {
        defFontFace = face;
        defFontSize = sz;
        defFg = fgCol;
    }

    public void setOverrideFont(String face, int min, int max, Color fgCol) {
        overFontFace = face;
        overMinFontSize = min;
        overMaxFontSize = max;
        overFg = fgCol;
    }

    public void setRespectTextFade(boolean b) {
        respectFade = b;
    }

    public void setRespectTextAlt(boolean b) {
        respectAlt = b;
    }

    public boolean getEmoticonsDecoded() {
        return emoticonsOn;
    }

    public EmoticonLoader getEmoticonLoader() {
        return emoticonLoader;
    }

    public String getDefaultFontFace() {
        return defFontFace;
    }

    public int getDefaultFontSize() {
        return defFontSize;
    }

    public Color getDefaultForeground() {
        return defFg;
    }

    public String getOverrideFontFace() {
        return overFontFace;
    }

    public int getOverrideMaxFontSize() {
        return overMaxFontSize;
    }

    public int getOverrideMinFontSize() {
        return overMinFontSize;
    }

    public Color getOverrideForeground() {
        return overFg;
    }

    public boolean getRespectTextFade() {
        return respectFade;
    }

    public boolean getRespectTextAlt() {
        return respectAlt;
    }
}
