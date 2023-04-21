package com.skruk.elvis.admin.gui;

/**
 *  Description of the Interface
 *
 * @author     skruk
 * @created    12 grudzień 2003
 */
public interface FrameCloseListener {

    /**  Description of the Field */
    public static final int FCL_ACCEPT = 0;

    /**  Description of the Field */
    public static final int FCL_CANCEL = 1;

    /**
	 *  Description of the Method
	 *
	 * @param  signal  Description of the Parameter
	 * @param  source  Description of the Parameter
	 */
    public void actionClosePerformed(Object source, int signal);
}
