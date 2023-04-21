package com.inetmon.jn.nwd;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import com.inetmon.jn.core.CorePlugin;
import com.inetmon.jn.nwd.dialogs.MoreDlg;
import com.inetmon.jn.nwd.dialogs.PopupDlg;
import com.inetmon.jn.nwd.toaster.Popup;
import com.inetmon.jn.nwd.views.WormView;

/**
 * This class provides public function that deal with the
 * UI position and notification.
 * 
 * @author Lo Peng Foo
 * @version 1.0.0
 */
public class GeneralFunc {

    static MoreDlg moreDlg = new MoreDlg(new Shell());

    ;

    static URL url = Platform.find(Platform.getBundle("com.inetmon.jn.nwd"), new Path("wav/chimes.wav"));

    static AudioClip clip = Applet.newAudioClip(url);

    static FileWriter printer;

    /**
	 * Get the center position coordinates.
	 * 
	 * @param parent parent control
	 * @param compW current control's width
	 * @param compH current control's height
	 * @return point of center screen position
	 */
    public static Point centerScreen(Composite parent, int compW, int compH) {
        int tempH;
        int tempW;
        tempW = (parent.getDisplay().getBounds().width - compW) / 2;
        tempH = (parent.getDisplay().getBounds().height - compH) / 2;
        return new Point(tempW, tempH);
    }

    /**
	 * Show balloon at the right-bottom corner with the 
	 * message specified.
	 *  
	 * @param message message to user
	 */
    public static void showBalloon(String message, int severity) {
        TrayItem tray = CorePlugin.getDefault().getTrayItem();
        Display disp = tray.getParent().getDisplay();
        Shell shell = disp.getActiveShell();
        if (shell == null) {
            Shell[] shell2 = disp.getShells();
            for (int i = 0; i < shell2.length; i++) {
                if (shell2[i] != null) {
                    shell = shell2[i];
                }
            }
        }
        if (severity == Severity.Alert) {
            final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
            tip.setMessage(message);
            tip.setText("Alert : Worm is detected!");
            tip.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    tip.setVisible(false);
                    tip.dispose();
                }
            });
            tray.setToolTip(tip);
            tip.setVisible(true);
        } else if (severity == Severity.Warning) {
            final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
            tip.setMessage(message);
            tip.setText("Warning : Suspicious Packet.");
            tip.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    tip.setVisible(false);
                    tip.dispose();
                }
            });
            tray.setToolTip(tip);
            tip.setVisible(true);
        }
    }

    /**
	 * Show popup to alert user about detected worm.
	 * 
	 * @param wormDetected detected worm object
	 */
    public static void showPopup(DetectedWorm wormDetected) {
        if (wormDetected.getSeverity() == Severity.Alert && Activator.getValue(NWDConfigKey.ALERT_POPUP)) {
            if (Activator.popExist) {
                Activator.pop.update(Activator.wormlist);
            } else {
                Activator.pop = new Popup(Activator.wormlist);
                Activator.pop.showToaster();
            }
        }
        if (wormDetected.getSeverity() == Severity.Warning && Activator.getValue(NWDConfigKey.WARN_POPUP)) {
            if (Activator.popExist) {
                Activator.pop.update(Activator.wormlist);
            } else {
                Activator.pop = new Popup(Activator.wormlist);
                Activator.pop.showToaster();
            }
        }
    }

    /**
	 * Show more details of worm based on the worm name given.
	 * 
	 * @param wormName worm name
	 */
    public static void showMoreDetails(String wormName) {
        moreDlg.openDlg(wormName);
    }

    /**
	 * Add an detected worm to the worm history.
	 * 
	 * @param worm detected worm object
	 */
    public static void addToHistory(DetectedWorm worm) {
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(WormView.ID);
        } catch (PartInitException e) {
        }
        WormView.addWorm(worm);
    }

    public static void showHistory(final IWorkbenchWindow win) {
        try {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    try {
                        win.getShell().setVisible(true);
                        win.getShell().setMaximized(true);
                        win.getActivePage().showView("com.inetmon.jn.nwd.views.WormView");
                    } catch (PartInitException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("show error ");
            e.printStackTrace();
        }
    }

    /**
	 * Play sound to indicate alert to user.
	 */
    public static void playVoice() {
        clip.play();
    }

    /**
	 * Write detected worm history to a log file.
	 * 
	 * @param worm detected worm object
	 */
    public static void writeWormLog(DetectedWorm worm) {
        DateFormat date = new SimpleDateFormat("dd MMM yyyy");
        try {
            printer = new FileWriter(Activator.getAppPath() + "/wormLog.txt", true);
            if (worm.getSeverity() == Severity.Warning) printer.write(date.format(worm.getDateTime()) + "\t" + DateFormat.getTimeInstance(DateFormat.SHORT).format(worm.getDateTime()) + "\t\t" + worm.getSrcIP() + "\t\t" + worm.getDestIP() + "\t" + Integer.toString(worm.getAttackPort()) + "\tWarning" + "\t" + worm.getWormName() + "\r\n"); else if (worm.getSeverity() == Severity.Alert) printer.write(date.format(worm.getDateTime()) + "\t" + DateFormat.getTimeInstance(DateFormat.SHORT).format(worm.getDateTime()) + "\t\t" + worm.getSrcIP() + "\t\t" + worm.getDestIP() + "\t" + Integer.toString(worm.getAttackPort()) + "\tAlert" + "\t" + worm.getWormName() + "\r\n");
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
