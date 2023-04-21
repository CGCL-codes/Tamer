package org.zhouer.zterm.view;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.Timer;
import org.zhouer.protocol.Protocol;
import org.zhouer.protocol.SSH2;
import org.zhouer.protocol.Telnet;
import org.zhouer.utils.Convertor;
import org.zhouer.utils.TextUtils;
import org.zhouer.vt.Application;
import org.zhouer.vt.Config;
import org.zhouer.vt.VT100;
import org.zhouer.zterm.model.Model;
import org.zhouer.zterm.model.Resource;
import org.zhouer.zterm.model.Site;

public class SessionPane extends JPanel implements Runnable, Application, AdjustmentListener, MouseWheelListener {

    private class AntiIdleTask implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            final long now = new Date().getTime();
            if (SessionPane.this.antiidle && SessionPane.this.isConnected() && (now - SessionPane.this.lastInputTime > SessionPane.this.antiIdleInterval)) {
                if (SessionPane.this.site.getProtocol().equalsIgnoreCase(Protocol.TELNET)) {
                    final String buf = TextUtils.BSStringToString(SessionPane.this.resource.getStringValue(Resource.ANTI_IDLE_STRING));
                    final char[] ca = buf.toCharArray();
                    SessionPane.this.writeChars(ca, 0, ca.length);
                }
            }
        }
    }

    public static final int STATE_ALERT = 4;

    public static final int STATE_CLOSED = 3;

    public static final int STATE_CONNECTED = 2;

    public static final int STATE_TRYING = 1;

    private static final long serialVersionUID = 2180544188833033537L;

    private int state;

    private boolean antiidle;

    private final Convertor conv = Convertor.getInstance();

    private boolean hasTab;

    private InputStream is;

    private long lastInputTime, antiIdleInterval;

    private final Model model;

    private Protocol network;

    private OutputStream os;

    private final Resource resource;

    private JScrollBar scrollbar;

    private int scrolllines;

    private final Site site;

    private long startTime;

    private Timer ti;

    private final VT100 vt;

    public SessionPane(final Site site, final BufferedImage image) {
        this.site = site;
        this.resource = Resource.getInstance();
        this.model = Model.getInstance();
        this.hasTab = true;
        this.setBackground(Color.BLACK);
        this.vt = new VT100(this, this.resource, image);
        this.vt.setEncoding(this.site.getEncoding());
        this.vt.setEmulation(this.site.getEmulation());
        this.setLayout(new BorderLayout());
        this.add(this.vt, BorderLayout.CENTER);
        if (this.resource.getBooleanValue(Resource.SHOW_SCROLL_BAR)) {
            this.scrolllines = this.resource.getIntValue(Config.TERMINAL_SCROLLS);
            this.scrollbar = new JScrollBar(Adjustable.VERTICAL, this.scrolllines - 1, 24, 0, this.scrolllines + 23);
            this.scrollbar.addAdjustmentListener(this);
            this.add(this.scrollbar, BorderLayout.EAST);
            this.addMouseWheelListener(this);
        }
    }

    public void adjustmentValueChanged(final AdjustmentEvent ae) {
        this.vt.setScrollUp(this.scrollbar.getMaximum() - this.scrollbar.getValue() - this.scrollbar.getVisibleAmount());
    }

    public void bell() {
        this.model.bell(this);
    }

    public void bell(final SessionPane s) {
        if (this.resource.getBooleanValue(Resource.USE_CUSTOM_BELL)) {
            try {
                java.applet.Applet.newAudioClip(new File(this.resource.getStringValue(Resource.CUSTOM_BELL_PATH)).toURI().toURL()).play();
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
        if (!this.model.isTabForeground(s)) {
            s.setState(SessionPane.STATE_ALERT);
        }
    }

    /**
	 * Close the connection.
	 * @param fromRemote true, if disconnection from remote server; false, if disconnection from client user
	 */
    public void close(final boolean fromRemote) {
        if (this.isClosed()) {
            return;
        }
        this.removeMouseWheelListener(this);
        this.network.disconnect();
        if (this.ti != null) {
            this.ti.stop();
        }
        if (this.vt != null) {
            this.vt.close();
        }
        this.setState(SessionPane.STATE_CLOSED);
        final boolean autoreconnect = this.resource.getBooleanValue(Resource.AUTO_RECONNECT);
        if (autoreconnect && fromRemote) {
            final long reopenTime = this.resource.getIntValue(Resource.AUTO_RECONNECT_TIME);
            final long reopenInterval = this.resource.getIntValue(Resource.AUTO_RECONNECT_INTERVAL);
            final long now = new Date().getTime();
            if ((now - this.startTime <= reopenTime * 1000) || (reopenTime == 0)) {
                try {
                    Thread.sleep(reopenInterval);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                this.model.reopenSession(this);
            }
        }
    }

    public void colorCopy() {
        this.model.colorCopy();
    }

    public void colorPaste() {
        this.model.colorPaste();
    }

    public void copy() {
        this.model.copy();
    }

    public String getEmulation() {
        return this.site.getEmulation();
    }

    public String getSelectedColorText() {
        return this.vt.getSelectedColorText();
    }

    public String getSelectedText() {
        return this.vt.getSelectedText();
    }

    public Site getSite() {
        return this.site;
    }

    public String getURL() {
        return this.site.getURL();
    }

    public boolean isClosed() {
        if (this.network == null) {
            return true;
        }
        return this.network.isClosed();
    }

    public boolean isConnected() {
        if (this.network == null) {
            return false;
        }
        return this.network.isConnected();
    }

    public boolean isTabForeground() {
        return this.model.isTabForeground(this);
    }

    public void mouseWheelMoved(final MouseWheelEvent arg0) {
        this.scroll(arg0.getWheelRotation());
    }

    public void openExternalBrowser(final String url) {
        this.model.openExternalBrowser(url);
    }

    public void paste() {
        this.model.paste();
    }

    public void pasteColorText(final String str) {
        this.vt.pasteColorText(str);
    }

    public void pasteText(final String str) {
        this.vt.pasteText(str);
    }

    public int readBytes(final byte[] buf) throws IOException {
        return this.is.read(buf);
    }

    public void remove() {
        this.hasTab = false;
    }

    public boolean requestFocusInWindow() {
        return this.vt.requestFocusInWindow();
    }

    public void resetSelected() {
        this.vt.resetSelected();
    }

    public void run() {
        this.setState(SessionPane.STATE_TRYING);
        if (this.site.getProtocol().equalsIgnoreCase(Protocol.TELNET)) {
            this.network = new Telnet(this.site.getHost(), this.site.getPort());
            this.network.setTerminalType(this.site.getEmulation());
        } else if (this.site.getProtocol().equalsIgnoreCase(Protocol.SSH)) {
            this.network = new SSH2(this.site.getHost(), this.site.getPort());
            this.network.setTerminalType(this.site.getEmulation());
        } else {
            System.err.println("Unknown protocol: " + this.site.getProtocol());
        }
        if (this.network.connect() == false) {
            this.setState(SessionPane.STATE_CLOSED);
            return;
        }
        this.is = this.network.getInputStream();
        this.os = this.network.getOutputStream();
        this.setState(SessionPane.STATE_CONNECTED);
        this.resource.addFavorite(this.site);
        this.model.updateFavoriteMenu();
        this.updateAntiIdleTime();
        this.lastInputTime = new Date().getTime();
        this.ti = new Timer(1000, new AntiIdleTask());
        this.ti.start();
        this.startTime = new Date().getTime();
        this.vt.run();
    }

    public void scroll(final int amount) {
        this.scrollbar.setValue(this.scrollbar.getValue() + amount);
    }

    public void setEmulation(final String emu) {
        site.setEmulation(emu);
        this.network.setTerminalType(emu);
        this.vt.setEmulation(emu);
    }

    public void setEncoding(final String enc) {
        site.setEncoding(enc);
        this.vt.setEncoding(this.site.getEncoding());
        requestScreenData();
    }

    public void requestScreenData() {
        final int CTRL_L = 12;
        this.writeChar((char) CTRL_L);
    }

    public void setState(final int s) {
        this.state = s;
        this.model.updateTabState(s, this);
    }

    public void showMessage(final String msg) {
        if (this.hasTab) {
            this.model.showMessage(msg);
        }
    }

    public void showPopup(final int x, final int y) {
        final Point p = this.vt.getLocationOnScreen();
        String link;
        if (this.vt.isOverURL(x, y)) {
            link = this.vt.getURL(x, y);
        } else {
            link = null;
        }
        this.model.showPopup(p.x + x, p.y + y, link);
    }

    public void updateAntiIdleTime() {
        this.antiidle = this.resource.getBooleanValue(Resource.ANTI_IDLE);
        this.antiIdleInterval = this.resource.getIntValue(Resource.ANTI_IDLE_INTERVAL) * 1000;
    }

    public void updateImage(final BufferedImage bi) {
        this.vt.updateImage(bi);
    }

    public void updateScreen() {
        this.vt.updateScreen();
    }

    public void updateSize() {
        this.vt.updateSize();
    }

    public void writeByte(final byte b) {
        this.lastInputTime = new Date().getTime();
        try {
            this.os.write(b);
        } catch (final IOException e) {
            System.out.println("Caught IOException in Session::writeByte(...)");
            this.close(true);
        }
    }

    public void writeBytes(final byte[] buf, final int offset, final int len) {
        this.lastInputTime = new Date().getTime();
        try {
            this.os.write(buf, offset, len);
        } catch (final IOException e) {
            System.out.println("Caught IOException in Session::writeBytes(...)");
            this.close(true);
        }
    }

    public void writeChar(final char c) {
        byte[] buf;
        buf = this.conv.charToBytes(c, this.site.getEncoding());
        this.writeBytes(buf, 0, buf.length);
    }

    public void writeChars(final char[] buf, final int offset, final int len) {
        int count = 0;
        final byte[] tmp = new byte[len * 4];
        byte[] tmp2;
        for (int i = 0; i < len; i++) {
            tmp2 = this.conv.charToBytes(buf[offset + i], this.site.getEncoding());
            for (int j = 0; j < tmp2.length; j++) {
                tmp[count++] = tmp2[j];
            }
        }
        this.writeBytes(tmp, 0, count);
    }

    /**
	 * Getter of state
	 *
	 * @return the state
	 */
    public int getState() {
        return state;
    }
}
