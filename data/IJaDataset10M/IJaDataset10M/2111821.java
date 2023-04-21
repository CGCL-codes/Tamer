package cn.imgdpu.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PopupWindow extends Thread {

    public static Shell shell;

    protected int moveStep = 6;

    protected int upPosition;

    protected int downPosition;

    protected int leftPosition;

    public PopupWindow(final String message) {
        shell = new Shell(SWT.ON_TOP);
        final Label closeBtn = new Label(shell, SWT.NONE);
        closeBtn.setAlignment(SWT.CENTER);
        closeBtn.setText("X");
        closeBtn.setBounds(232, 4, 15, 15);
        closeBtn.addMouseListener(new MouseAdapter() {

            public void mouseDown(final MouseEvent arg0) {
                shell.close();
            }
        });
        Text text = new Text(shell, SWT.MULTI | SWT.WRAP);
        text.setBounds(10, 10, 228, 128);
        text.setBackground(shell.getBackground());
        text.setText(message);
        Rectangle area = Display.getDefault().getClientArea();
        upPosition = area.height - 150;
        downPosition = area.height + 150;
        leftPosition = area.width - 252;
        shell.setSize(250, 150);
        shell.setLocation(leftPosition, downPosition);
        shell.open();
    }

    public void run() {
        Display display = shell.getDisplay();
        boolean canstop = false;
        while (!canstop) {
            try {
                Thread.sleep(10);
                if ((downPosition - moveStep) > upPosition) {
                    display.asyncExec(new Runnable() {

                        public void run() {
                            if (!shell.isDisposed()) shell.setLocation(leftPosition, downPosition - moveStep);
                            downPosition -= moveStep;
                        }
                    });
                } else {
                    canstop = true;
                }
            } catch (InterruptedException e) {
                cn.imgdpu.util.CatException.getMethod().catException(e, "线程中断异常");
            }
        }
        try {
            sleep(1000 * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        display.asyncExec(new Runnable() {

            public void run() {
                System.out.print(" run in?");
                shell.close();
            }
        });
    }
}
