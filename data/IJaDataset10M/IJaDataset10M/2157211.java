package com.sandboxpix.jmpa;

import com.sandboxpix.sbp.SBP_ImageSplat;

public class MPAStampinator extends MPAOptionTool_Abstract {

    public MPAStampinator(int w, int h, int squarew, int squareh, String fname) {
        super(w, h, squarew, squareh, fname, 2);
        optionBarID = "optionPickerWidget";
        optionSelectorsID = "mpastampinatorStampOptionTools";
        optionDisplayID = "mpastampinatorStampOptionDisplay";
        setImageTool(new SBP_ImageSplat());
        setOptionID(1);
    }
}
