package com.umc.gui.widgets;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import com.umc.gui.GuiController;
import com.umc.helper.UMCConstants;

public abstract class AbstractWidget extends JPanel implements IWidget {

    private static final long serialVersionUID = 1L;

    private static boolean isAlreadyInstaciated = false;

    protected long remainingSleeptime = 300000;

    private boolean systemWidget = false;

    protected JPanel container = null;

    protected Color colorInfo = Color.ORANGE;

    protected Font textBold10 = new java.awt.Font("Helvetica", Font.BOLD, 10);

    protected Font textBold12 = new java.awt.Font("Helvetica", Font.BOLD, 12);

    protected boolean showInfo = false;

    protected boolean isOpen = true;

    private int widgetHeight = 0;

    private boolean isRefreshEnabled = false;

    private Timer timer = new Timer();

    /**
	 * If set to true, the widget will get a refresh button so the user can refresh the content in the widget
	 * prior to the 300 seconds until an automatic refresh will be executed.
	 * 
	 * @param b true/false
	 */
    public void setRefreshEnabled(boolean b) {
        isRefreshEnabled = b;
    }

    /**
	 * Per default a widget will be executed every 300 seconds if the refresh option has been enabled.
	 * To use a different period, call this method with an appropriate value.
	 * 
	 * @param seconds time in seconds 
	 */
    public void setRefreshTime(long seconds) {
        remainingSleeptime = seconds * 1000;
        timer.schedule(new WidgetRefresher(this), 1, remainingSleeptime);
        timer.schedule(new RemainingSleeptimeRefresher(this), 1, 1000);
    }

    public void setSystemWidget(boolean isSystemWidget) {
        systemWidget = isSystemWidget;
    }

    public boolean isSystemWidget() {
        return systemWidget;
    }

    public void paintFrame(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(7, 7, getWidth() - 14, getHeight() - 14, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 7, getWidth() - 10, 10, 10, 10);
        g2.fillRect(5, 8, getWidth() - 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(getName(), 10, 16);
        int x = 300;
        if (!isRefreshEnabled) x = 316;
        g2.setColor(Color.darkGray);
        g2.fillOval(x, 7, 10, 10);
        g2.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("-", x + 3, 15);
        if (isRefreshEnabled) {
            x += 16;
            g2.setColor(Color.darkGray);
            g2.fillOval(x, 7, 10, 10);
            g2.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 8));
            g2.setColor(Color.white);
            g2.drawString("R", x + 2, 15);
        }
        x += 16;
        g2.setColor(Color.darkGray);
        g2.fillOval(x, 7, 10, 10);
        g2.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 8));
        g2.setColor(Color.white);
        g2.drawString("?", x + 3, 15);
        g2.setFont(new java.awt.Font("HELVETICA", Font.PLAIN, 11));
    }

    public AbstractWidget() {
        setLayout(new CardLayout());
        setMinimumSize(new Dimension(350, 150));
        setPreferredSize(new Dimension(350, 150));
        setBackground(UMCConstants.guiColor);
        MouseListener ml = new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                int xMinimize = 300;
                if (!isRefreshEnabled) xMinimize = 316;
                if (e.getX() > xMinimize && e.getX() < (xMinimize + 10) && e.getY() > 7 && e.getY() < 17) {
                    if (isOpen) {
                        widgetHeight = getHeight();
                        setBounds(getX(), getY(), getWidth(), 25);
                        setPreferredSize(new Dimension(getWidth(), 25));
                        isOpen = false;
                        container.revalidate();
                        container.repaint();
                    } else {
                        setBounds(getX(), getY(), getWidth(), widgetHeight);
                        setPreferredSize(new Dimension(getWidth(), widgetHeight));
                        isOpen = true;
                        container.revalidate();
                        container.repaint();
                    }
                }
                if (isRefreshEnabled) {
                    xMinimize += 16;
                    if (e.getX() > xMinimize && e.getX() < (xMinimize + 10) && e.getY() > 7 && e.getY() < 17) {
                        remainingSleeptime = 10;
                        repaint();
                        revalidate();
                    }
                }
                xMinimize += 16;
                if (e.getX() > xMinimize && e.getY() < 17) {
                    showInfo = showInfo ? false : true;
                    repaint();
                }
            }
        };
        addMouseListener(ml);
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public void setWidgetHeight(int widgetHeight) {
        this.widgetHeight = widgetHeight;
    }
}
