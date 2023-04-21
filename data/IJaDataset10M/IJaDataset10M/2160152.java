package com.loribel.commons.util;

import com.loribel.commons.abstraction.GB_InfoOwnerSet;

/**
 * Tools for GB_InfoOwner.
 *
 * @author Gregory Borelli
 */
public final class GB_InfoOwnerTools {

    public static class MyThreadFromClipboard extends Thread {

        private long delay;

        private boolean stop;

        private GB_InfoOwnerSet info;

        public MyThreadFromClipboard(long a_delay, GB_InfoOwnerSet a_info) {
            super("InfoClipboard");
            delay = a_delay;
            info = a_info;
        }

        public void run() {
            while (!stop) {
                try {
                    String l_value = GB_ClipboardTools.getText();
                    Thread.sleep(delay);
                    String l_value2 = GB_ClipboardTools.getText();
                    if (!GB_EqualsTools.equalsString(l_value, l_value2)) {
                        info.setInfo(l_value2);
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        public void setStop() {
            stop = true;
        }
    }

    private static MyThreadFromClipboard thread;

    public static void startThreadFromClipboard(long a_delay, GB_InfoOwnerSet a_info) {
        stopThreadFromClipboard();
        thread = new MyThreadFromClipboard(a_delay, a_info);
        thread.run();
    }

    public static void stopThreadFromClipboard() {
        if (thread != null) {
            thread.setStop();
        }
    }

    private GB_InfoOwnerTools() {
    }
}
