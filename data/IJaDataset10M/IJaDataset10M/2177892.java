package com.alexmcchesney.notifications;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

/**
 * Class representing the actual notification window.
 * @author amcchesney
 *
 */
class NotificationWindow extends AnimatedWindow {

    /** Default shell size */
    private Point DEFAULT_SIZE = new Point(200, 75);

    /** Default rate of fade-in */
    private static final int DEFAULT_FADE_IN_SPEED = 30;

    /** Default rate of fade-out */
    private static final int DEFAULT_FADE_OUT_SPEED = -5;

    /** Default maximum alpha-level */
    private static final int DEFAULT_MAX_ALPHA = 200;

    /** Default time until fade-out begins */
    private static final int DEFAULT_FADE_OUT_DELAY = 2500;

    /** Timer counting down to fadeout */
    private DisplayTimer m_fadeoutTimer = null;

    /** Shell background colour */
    private Color m_backgroundCol = null;

    /** Text colour */
    private Color m_textCol = null;

    /**
	 * Constructor
	 *
	 */
    public NotificationWindow(String sMessage, Image icon, NotificationWindow lastWindow) {
        super(Display.getDefault(), SWT.NO_TRIM | SWT.ON_TOP);
        m_backgroundCol = m_shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        m_textCol = m_shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        m_shell.setBackground(m_backgroundCol);
        m_shell.setForeground(m_textCol);
        FillLayout layout = new FillLayout();
        layout.marginHeight = 3;
        layout.marginWidth = 3;
        m_shell.setLayout(layout);
        Composite masterComp = new Composite(m_shell, SWT.BORDER);
        masterComp.setBackground(m_backgroundCol);
        masterComp.setForeground(m_textCol);
        GridLayout compLayout = new GridLayout(2, false);
        compLayout.marginWidth = 5;
        compLayout.marginHeight = 5;
        masterComp.setLayout(compLayout);
        Label iconLabel = null;
        if (icon != null) {
            iconLabel = new Label(masterComp, SWT.NONE);
            iconLabel.setImage(icon);
            GridData iconData = new GridData(GridData.CENTER, GridData.CENTER, false, true);
            iconLabel.setLayoutData(iconData);
        }
        iconLabel.setBackground(m_backgroundCol);
        iconLabel.setForeground(m_textCol);
        Label messageLabel = new Label(masterComp, SWT.CENTER | SWT.WRAP);
        messageLabel.setBackground(m_backgroundCol);
        messageLabel.setForeground(m_textCol);
        GridData labelData = new GridData(GridData.CENTER, GridData.CENTER, true, true);
        messageLabel.setLayoutData(labelData);
        messageLabel.setText(sMessage);
        messageLabel.addMouseTrackListener(new MouseTrackListener() {

            public void mouseEnter(MouseEvent e) {
                if (m_fadeoutTimer != null) {
                    m_fadeoutTimer.cancel();
                    m_fadeoutTimer = null;
                }
                cancelFade();
                m_shell.setAlpha(DEFAULT_MAX_ALPHA);
                m_shell.redraw();
            }

            public void mouseExit(MouseEvent e) {
                beginFadeTimer();
            }

            public void mouseHover(MouseEvent e) {
            }
        });
        m_shell.layout();
        m_shell.setSize(DEFAULT_SIZE);
        Rectangle clientArea = Display.getCurrent().getPrimaryMonitor().getClientArea();
        Point shellSize = m_shell.getSize();
        int baseLine;
        if (lastWindow != null) {
            baseLine = lastWindow.getLocation().y;
        } else {
            baseLine = clientArea.height;
        }
        Point newPos = new Point(clientArea.width - shellSize.x - 5, baseLine - shellSize.y - 5);
        m_shell.setLocation(newPos);
    }

    /**
	 * Actually shows the notification
	 *
	 */
    public void show() {
        m_shell.setAlpha(0);
        m_shell.setVisible(true);
        fade(DEFAULT_FADE_IN_SPEED, 0, DEFAULT_MAX_ALPHA, null);
        beginFadeTimer();
    }

    /**
	 * Starts the timer that will cause the notification window to fade out.
	 *
	 */
    private void beginFadeTimer() {
        m_fadeoutTimer = DisplayTimer.start(DEFAULT_FADE_OUT_DELAY, new ITimerListener() {

            public void onTimeout() {
                fade(DEFAULT_FADE_OUT_SPEED, DEFAULT_MAX_ALPHA, 0, new IAnimationListener() {

                    public void onAnimationComplete() {
                        m_shell.dispose();
                    }
                });
            }
        });
    }

    public void addDisposeListener(DisposeListener listener) {
        m_shell.addDisposeListener(listener);
    }

    public Point getLocation() {
        return m_shell.getLocation();
    }

    public boolean isDisposed() {
        return m_shell.isDisposed();
    }
}
