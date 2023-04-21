package org.iisc.mile.indickeyboards.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class KeyboardHook {

    public KeyboardHook() {
        (new PollThread(this)).start();
    }

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    public void addEventListener(KeyboardEventListener listener) {
        listenerList.add(KeyboardEventListener.class, listener);
    }

    public void removeEventListener(KeyboardEventListener listener) {
        listenerList.remove(KeyboardEventListener.class, listener);
    }

    void keyPressed(KeyboardEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == KeyboardEventListener.class) {
                ((KeyboardEventListener) listeners[i + 1]).GlobalKeyPressed(event);
            }
        }
    }

    void keyReleased(KeyboardEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == KeyboardEventListener.class) {
                ((KeyboardEventListener) listeners[i + 1]).GlobalKeyReleased(event);
            }
        }
    }
}

class PollThread extends Thread {

    public native void checkKeyboardChanges();

    private KeyboardHook kbh;

    public PollThread(KeyboardHook kh) {
        kbh = kh;
        try {
            System.loadLibrary("indic-keyboards-sysHook");
            System.loadLibrary("indic-keyboards-opChars");
        } catch (UnsatisfiedLinkError e) {
            String windowsSysHookLibraryName = "indic-keyboards-sysHook.dll or indic-keyboards-opChars.dll";
            Display display = Display.getCurrent();
            Shell shell = new Shell(display);
            MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
            messageBox.setText("Missing Library");
            messageBox.setMessage("The libraries necessary to run indic-keyboards are missing.\n" + "Please put the " + windowsSysHookLibraryName + " in the program folder.");
            messageBox.open();
            shell.dispose();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        for (; ; ) {
            checkKeyboardChanges();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void Callback(boolean ts, int vk, boolean ap, boolean ek) {
        KeyboardEvent event = new KeyboardEvent(this, ts, vk, ap, ek);
        if (ts) {
            kbh.keyPressed(event);
        } else {
            kbh.keyReleased(event);
        }
    }
}
