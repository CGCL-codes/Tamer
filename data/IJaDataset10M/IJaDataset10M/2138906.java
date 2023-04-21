package com.loribel.commons.util;

/**
 * Launcher for Thread Clipboard.
 *
 * @author Gregory Borelli
 */
public final class GB_ClipboardThreadLauncher {

    private GB_ClipboardThreadLauncher() {
    }

    public static void main(String[] args) {
        int l_delay = 200;
        String l_stop = "X";
        Thread l_thread = new GB_ClipboardTools.MyThreadCopyClipboard(l_delay, l_stop);
        l_thread.start();
    }
}
