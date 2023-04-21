package de.fmf.multiclip.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SwtDialogs {

    public SwtDialogs() {
    }

    public static String openDirDialog(Shell s, String predefPath) {
        DirectoryDialog dd = new DirectoryDialog(s);
        dd.setFilterPath((predefPath != null) ? predefPath : "");
        dd.setText("open dir");
        String selected = dd.open();
        return (selected != null) ? selected : "";
    }

    public static String openFileDialog(Shell s, String predefFileName, String predefPath, String[] filterExt) {
        FileDialog fd = new FileDialog(s, SWT.OPEN);
        fd.setText("open file");
        fd.setFilterPath((predefPath != null) ? predefPath : "");
        fd.setFilterExtensions(filterExt);
        fd.setFileName((predefFileName != null) ? predefFileName : "");
        String selected = fd.open();
        return (selected != null) ? selected : "";
    }

    public static String saveFileDialog(Shell s, String predefFileName, String predefPath, String[] filterExt) {
        FileDialog fd = new FileDialog(s, SWT.SAVE);
        fd.setText("save");
        fd.setFilterPath((predefPath != null) ? predefPath : "");
        fd.setFilterExtensions(filterExt);
        fd.setFileName((predefFileName != null) ? predefFileName : "");
        String selected = fd.open();
        return (selected != null) ? selected : "";
    }

    public static String saveFileDialog(Shell s, String initial) {
        FileDialog fd = new FileDialog(s, SWT.SAVE);
        fd.setText("save");
        String[] filterExt = { "*" };
        fd.setFilterExtensions(filterExt);
        fd.setFileName((initial != null) ? initial : "");
        String selected = fd.open();
        return (selected != null) ? selected : "";
    }

    public static void fontDialog() {
    }

    public static void printDialog() {
    }

    public static void colorDialog() {
    }

    public static void showInfoMessage(final Shell s, final String message) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageBox messageBox = new MessageBox(s, SWT.ICON_INFORMATION | SWT.OK);
                messageBox.setText("info");
                messageBox.setMessage(message);
                messageBox.open();
            }
        });
    }

    public static void showErrorMessage(final Shell s, final String message) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageBox messageBox = new MessageBox(s, SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("error");
                messageBox.setMessage(message);
                messageBox.open();
            }
        });
    }

    public static int showYesNoQuestion(Shell s, String question) {
        MessageBox messageBox = new MessageBox(s, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        messageBox.setText("question");
        messageBox.setMessage(question);
        return messageBox.open();
    }

    public static int showYesNoError(Shell s, String msg) {
        MessageBox messageBox = new MessageBox(s, SWT.ICON_ERROR | SWT.YES | SWT.NO);
        messageBox.setText("question");
        messageBox.setMessage(msg);
        return messageBox.open();
    }

    public static void showErrorMessage(final Shell s, final String message, final Exception e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageBox messageBox = new MessageBox(s, SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("error");
                StringBuffer sb = new StringBuffer();
                StackTraceElement[] trace = e.getStackTrace();
                sb.append(e.toString() + "\n");
                for (int i = 0; i < trace.length; i++) {
                    sb.append("\t" + "at " + trace[i] + "\n");
                }
                messageBox.setMessage(message + "\n" + sb.toString());
                messageBox.open();
            }
        });
    }

    public static Rectangle centerWindow(Rectangle parent, Rectangle child) {
        Rectangle bounds = parent;
        Rectangle rect = child;
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        return new Rectangle(x, y, child.x, child.y);
    }
}
