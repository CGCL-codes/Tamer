package net.sourceforge.oopinyinguide;

import net.sourceforge.pinyinlookup.PinyinLookup;

/**
 * Configuration class.
 * 
 * @author Vincent Petry <PVince81@users.sourceforge.net>
 */
public class Config {

    private short defaultRubyAdjust;

    private short defaultRubyPosition;

    private String defaultRubyStyle;

    private int maxSplitSize;

    private boolean useLastChoiceAsDefault;

    private boolean useToneSandhi;

    private String[] dictList;

    public Config() {
        defaultRubyAdjust = (short) 0;
        defaultRubyPosition = (short) 0;
        defaultRubyStyle = "Rubies";
        maxSplitSize = 10;
        useLastChoiceAsDefault = true;
        useToneSandhi = true;
        dictList = PinyinLookup.DEFAULT_DICTS;
    }

    public short getDefaultRubyAdjust() {
        return defaultRubyAdjust;
    }

    public short getDefaultRubyPosition() {
        return defaultRubyPosition;
    }

    public String getDefaultRubyStyle() {
        return defaultRubyStyle;
    }

    public int getMaxSplitSize() {
        return maxSplitSize;
    }

    public boolean isUseLastChoiceAsDefault() {
        return useLastChoiceAsDefault;
    }

    public boolean isUseToneSandhi() {
        return useToneSandhi;
    }

    public String[] getDictList() {
        return dictList;
    }
}
