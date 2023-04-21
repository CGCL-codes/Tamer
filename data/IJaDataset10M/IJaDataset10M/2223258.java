package com.google.code.p.keytooliui.shared.lang.bool;

/**
    for use in table, contains:
    . a black "checked" image
    . a booleanChecked y/n
    
    if (value==true)
    {
        returns:
        . a "checked" icon
        . a nil text
    }
    
    else
    {
        returns:
        . a nil icon
        . a "[????]" text
    }
    
**/
public final class BOCheckedCandidateRed extends BOCheckedCandidateAbs {

    private static final String _f_s_strIcon = "tick_red9x11.gif";

    ;

    public BOCheckedCandidateRed(boolean blnChecked) {
        super(BOCheckedCandidateRed._f_s_strIcon, blnChecked);
    }

    public BOCheckedCandidateRed() {
        super(BOCheckedCandidateRed._f_s_strIcon);
    }
}
