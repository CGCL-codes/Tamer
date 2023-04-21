package com.sandboxpix.mpa;

import java.net.URL;
import com.sandboxpix.sbp.SBP_ImageSplat;
import com.sandboxpix.sbp.SBP_Tool_Interface;

public class MPAStampinator extends MPAOptionTool_Abstract {

    public MPAStampinator(int w, int h, int squarew, int squareh, URL iurl) {
        super(w, h, squarew, squareh, iurl, SBP_Tool_Interface.SBP_STAMP_TOOL_ID);
        optionBarID = "optionPickerWidget";
        optionSelectorsID = "mpastampinatorStampOptionTools";
        optionDisplayID = "mpastampinatorStampOptionDisplay";
        myOptionID = 1;
        tool = new SBP_ImageSplat();
    }
}
