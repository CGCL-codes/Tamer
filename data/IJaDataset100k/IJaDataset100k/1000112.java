package de.mud.jta.plugin;

import de.mud.jta.Plugin;
import de.mud.jta.FilterPlugin;
import de.mud.jta.PluginBus;
import de.mud.jta.PluginConfig;
import de.mud.jta.VisualPlugin;
import de.mud.jta.event.ConfigurationListener;
import de.mud.jta.event.OnlineStatusListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import de.mud.jta.event.SocketListener;
import de.mud.jta.event.OnlineStatus;
import de.mud.login.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import de.mud.jta.event.SecondaryWindowEvent;
import de.mud.jta.event.SecondaryWindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 * Secure Shell plugin for the Java Telnet Application (JTA). This is a plugin
 * to be used instead of Telnet for secure remote terminal sessions over
 * insecure networks.
 * <p>
 * This plugin works in conjuction with JSch, a open source client site pure
 * java implementation of the SSH protocol version 2 with a BSD styled 
 * license.
 * </p><p>
 * This plugin was written mainly because JSch is a more complete
 * implementation of SSH2 than the SSH plugin that comes with JTA. JSch comes 
 * with proxy and socks5 support, zlib compression and X11 forwarding. After 
 * copying the compiled class file of this source to de.mud.jta.plugin, it is
 * invoked like this:
 * </p><p><code>
 * java -cp <include jta and jsch here> de.mud.jta.Main -plugins Status,JTAJSch,Terminal [hostname]
 * </code></p><p>
 * Known Bugs:<br/><ul>
 * <li>Port always defaults to 22 and cannot be changed.</li>
 * <li>No SFTP support.</li>
 * </ul></p><p>
 * <b>Maintainer:</b> Thomas Pasch <thomas.pasch@gmx.de>
 * </p>
 *
 * @see <a href="http://www.sourceforge.net/projects/jta/">Java Telnet Application</a>
 * @see <a href="http://www.jcraft.com/jsch/">JSch homepage</a>
 * @see <a href="http://www.openssh.org/">free Open Source version of SSH</a>
 * @see <a href="http://www.ssh.com/">Official SSH homepage</a>
 * @version $Id: JTAJSch.java,v 1.4 2008/03/06 04:04:53 kostaservis Exp $
 * @author Thomas Pasch <thomas.pasch@gmx.de>
 */
public class JTAJSch extends Plugin implements FilterPlugin, VisualPlugin, LoginPlugin {

    private static final String MENU = "JSch";

    private static final String MENU_HTTP_PROXY = "Http Proxy ...";

    private static final String MENU_SOCKS_PROXY = "Socks Proxy ...";

    private static final String MENU_X11 = "X11 ...";

    private static final String MENU_LOCAL_PORT = "Local Port ...";

    private static final String MENU_REMOTE_PORT = "Remote Port ...";

    private static final JSch jSch_ = new JSch();

    private String proxyHttpHost_ = null;

    private int proxyHttpPort_;

    private String proxySOCKS5Host_ = null;

    private int proxySOCKS5Port_;

    private String xHost_ = null;

    private int xPort_;

    protected FilterPlugin source;

    protected Session session_;

    private Channel channel_;

    private InputStream in_;

    private OutputStream out_;

    private String host_;

    private int port_;

    private LoginJPanel iPanel;

    private boolean auth = false;

    protected MyUserInfo userInfo_;

    /**
     * Create a new ssh plugin.
     */
    public JTAJSch(final PluginBus bus, final String id) {
        super(bus, id);
        iPanel = new LoginJPanel(this);
        userInfo_ = new MyUserInfo();
        bus.registerPluginListener(new ConfigurationListener() {

            public void setConfiguration(PluginConfig config) {
                System.err.println("ConfigurationListener.setConfiguration()");
                String user = config.getProperty("SSH", id, "user");
                String pass = config.getProperty("SSH", id, "password");
                String passp = config.getProperty("SSH", id, "passphrase");
                String s;
                if ((s = config.getProperty("Socket.host")) != null && s.trim().length() > 0) {
                    host_ = new String(s.trim());
                }
                if ((s = config.getProperty("Socket.port")) != null && s.trim().length() > 0) {
                    port_ = (new Integer(s.trim())).intValue();
                }
                port_ = port_ <= 0 ? 22 : port_;
                userInfo_.setUser(user);
                userInfo_.setPassword(pass);
                userInfo_.setPassphrase(passp);
            }
        });
        bus.registerPluginListener(new OnlineStatusListener() {

            public void online() {
                System.err.println("OnlineStatusListener.online()");
                if (!auth) {
                    bus.broadcast(new OnlineStatus(false));
                }
                iPanel.setOnline(auth);
            }

            public void offline() {
                System.err.println("OnlineStatusListener.offline()");
                iPanel.setOnline(auth);
            }
        });
        bus.registerPluginListener(new SocketListener() {

            public void connect(String host, int port) {
                host_ = host;
                port_ = port <= 0 ? port_ : port;
                if (userInfo_ != null && userInfo_.getUser() != null && userInfo_.getPassword() != null && !auth) {
                    JTAJSch.this.connect();
                }
                iPanel.setOnline(auth);
            }

            public void disconnect() {
                JTAJSch.this.disconnect();
            }
        });
        bus.registerPluginListener(new SecondaryWindowListener() {

            public void secondaryWindowActivated(SecondaryWindowEvent e) {
                Container c = iPanel.getTopLevelAncestor();
                if (c instanceof Window) ((Window) c).setVisible(false);
            }

            public void secondaryWindowCanceled(SecondaryWindowEvent e) {
                Container c = iPanel.getTopLevelAncestor();
                if (c instanceof Window) ((Window) c).setVisible(true);
            }

            public void secondaryWindowRefreshed(SecondaryWindowEvent e) {
                Container c = iPanel.getTopLevelAncestor();
                if (c == null) SwingUtilities.updateComponentTreeUI(iPanel); else SwingUtilities.updateComponentTreeUI(c);
                if (c instanceof Window) ((Window) c).pack();
            }
        });
    }

    public void setFilterSource(FilterPlugin source) {
    }

    public FilterPlugin getFilterSource() {
        return null;
    }

    private byte buffer[];

    private int pos;

    /**
     * Read data from the backend and decrypt it. This is a buffering read
     * as the encrypted information is usually smaller than its decrypted
     * pendant. So it will not read from the backend as long as there is
     * data in the buffer.
     * @param b the buffer where to read the decrypted data in
     * @return the amount of bytes actually read.
     */
    public int read(byte[] b) throws IOException {
        int bytesRead = 0;
        while (!auth) try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (in_.available() >= 0) bytesRead = in_.read(b); else disconnect();
        if (bytesRead < 0) disconnect();
        return bytesRead;
    }

    /**
     * Write data to the back end. This hands the data over to the ssh
     * protocol handler who encrypts the information and writes it to
     * the actual back end pipe.
     * @param b the unencrypted data to be encrypted and sent
     */
    public void write(byte[] b) throws IOException {
        if (!auth) return;
        try {
            out_.write(b);
            out_.flush();
        } catch (IOException e) {
            disconnect();
        }
    }

    public JComponent getPluginVisual() {
        return iPanel;
    }

    public JMenu getPluginMenu() {
        final JMenu menu = new JMenu(MENU);
        JMenuItem item = new JMenuItem(MENU_HTTP_PROXY);
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String foo = getProxyHttpHost();
                int bar = getProxyHttpPort();
                String proxy = JOptionPane.showInputDialog("HTTP proxy server (hostname:port)", ((foo != null && bar != 0) ? foo + ":" + bar : ""));
                if (proxy == null) return;
                if (proxy.length() == 0) {
                    setProxyHttp(null, 0);
                    return;
                }
                try {
                    foo = proxy.substring(0, proxy.indexOf(':'));
                    bar = Integer.parseInt(proxy.substring(proxy.indexOf(':') + 1));
                    if (foo != null) {
                        setProxyHttp(foo, bar);
                    }
                } catch (Exception ee) {
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(MENU_SOCKS_PROXY);
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String foo = getProxySOCKS5Host();
                int bar = getProxySOCKS5Port();
                String proxy = JOptionPane.showInputDialog("SOCKS5 server (hostname:1080)", ((foo != null && bar != 0) ? foo + ":" + bar : ""));
                if (proxy == null) return;
                if (proxy.length() == 0) {
                    setProxySOCKS5(null, 0);
                    return;
                }
                try {
                    foo = proxy.substring(0, proxy.indexOf(':'));
                    bar = Integer.parseInt(proxy.substring(proxy.indexOf(':') + 1));
                    if (foo != null) {
                        setProxySOCKS5(foo, bar);
                    }
                } catch (Exception ee) {
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(MENU_X11);
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String display = JOptionPane.showInputDialog("XDisplay name (hostname:0)", (getXHost() == null) ? "" : (getXHost() + ":" + getXPort()));
                try {
                    if (display != null) {
                        String host = display.substring(0, display.indexOf(':'));
                        int port = Integer.parseInt(display.substring(display.indexOf(':') + 1));
                        setXForwarding(host, port);
                    }
                } catch (Exception ee) {
                    setXForwarding(null, -1);
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(MENU_LOCAL_PORT);
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (session_ == null) {
                    JOptionPane.showMessageDialog(menu.getParent(), "Establish the connection before this setting.");
                    return;
                }
                try {
                    String title = "Local port forwarding (port:host:hostport)";
                    String foo = JOptionPane.showInputDialog(title, "");
                    if (foo == null) return;
                    int port1 = Integer.parseInt(foo.substring(0, foo.indexOf(':')));
                    foo = foo.substring(foo.indexOf(':') + 1);
                    String host = foo.substring(0, foo.indexOf(':'));
                    int port2 = Integer.parseInt(foo.substring(foo.indexOf(':') + 1));
                    setPortForwardingL(port1, host, port2);
                } catch (Exception ee) {
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(MENU_REMOTE_PORT);
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (session_ == null) {
                    JOptionPane.showMessageDialog(menu.getParent(), "Establish the connection before this setting.");
                    return;
                }
                try {
                    String title = "Remote port forwarding (port:host:hostport)";
                    String foo = JOptionPane.showInputDialog(title, "");
                    if (foo == null) return;
                    int port1 = Integer.parseInt(foo.substring(0, foo.indexOf(':')));
                    foo = foo.substring(foo.indexOf(':') + 1);
                    String host = foo.substring(0, foo.indexOf(':'));
                    int port2 = Integer.parseInt(foo.substring(foo.indexOf(':') + 1));
                    setPortForwardingR(port1, host, port2);
                } catch (Exception ee) {
                }
            }
        });
        menu.add(item);
        return menu;
    }

    private boolean isProxyHttp() {
        return proxyHttpHost_ != null;
    }

    private String getProxyHttpHost() {
        return proxyHttpHost_;
    }

    private int getProxyHttpPort() {
        return proxyHttpPort_;
    }

    private void setProxyHttp(String host, int port) {
        proxyHttpHost_ = host;
        switch(port) {
            case -1:
                proxyHttpHost_ = null;
                proxyHttpPort_ = port;
                break;
            case 0:
                proxyHttpPort_ = 3128;
                break;
            default:
                proxyHttpPort_ = port;
        }
    }

    private boolean isProxySOCKS5() {
        return proxySOCKS5Host_ != null;
    }

    private String getProxySOCKS5Host() {
        return proxySOCKS5Host_;
    }

    private int getProxySOCKS5Port() {
        return proxySOCKS5Port_;
    }

    private void setProxySOCKS5(String host, int port) {
        proxySOCKS5Host_ = host;
        switch(port) {
            case -1:
                proxySOCKS5Host_ = null;
                proxySOCKS5Port_ = port;
                break;
            case 0:
                proxySOCKS5Port_ = 3128;
                break;
            default:
                proxySOCKS5Port_ = port;
        }
    }

    private boolean isXForwarding() {
        return xHost_ != null;
    }

    private String getXHost() {
        return xHost_;
    }

    private int getXPort() {
        return xPort_;
    }

    private void setXForwarding(String host, int port) {
        xHost_ = host;
        switch(port) {
            case -1:
                xHost_ = null;
                xPort_ = port;
                break;
            default:
                xPort_ = port;
        }
    }

    private void setPortForwardingR(int port1, String host, int port2) {
        try {
            session_.setPortForwardingR(port1, host, port2);
        } catch (JSchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setPortForwardingL(int port1, String host, int port2) {
        try {
            session_.setPortForwardingL(port1, host, port2);
        } catch (JSchException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setLogin(String s) {
        userInfo_.setUser(s);
        iPanel.getPassword();
    }

    public void setPasswd(String s) {
        userInfo_.setPassword(s);
    }

    public void connect() {
        System.err.println("LoginPlugin connect()");
        if (auth) {
            throw new IllegalStateException();
        }
        try {
            SwingUtilities.getRoot(iPanel).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            session_ = jSch_.getSession(userInfo_.getUser(), host_, port_);
            session_.setUserInfo(userInfo_);
            if (isProxyHttp()) {
                session_.setProxy(new ProxyHTTP(proxyHttpHost_, proxyHttpPort_));
            } else if (isProxySOCKS5()) {
                session_.setProxy(new ProxySOCKS5(proxySOCKS5Host_, proxySOCKS5Port_));
            }
            if (isXForwarding()) {
                session_.setX11Host(xHost_);
                session_.setX11Port(xPort_);
            }
            session_.connect();
            channel_ = session_.openChannel("shell");
            try {
                in_ = channel_.getInputStream();
                out_ = channel_.getOutputStream();
            } catch (IOException ee) {
                ee.printStackTrace();
                throw new RuntimeException(ee);
            }
            channel_.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            iPanel.setOnline(false);
            auth = false;
            bus.broadcast(new OnlineStatus(false));
            SwingUtilities.getRoot(iPanel).setCursor(Cursor.getDefaultCursor());
            JOptionPane.showMessageDialog(null, "Connect fail, reason:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        System.err.println("Connected! \nRemote version:" + session_.getServerVersion());
        System.err.println("Client version:" + session_.getClientVersion());
        iPanel.setOnline(true);
        auth = true;
        bus.broadcast(new OnlineStatus(true));
        SwingUtilities.getRoot(iPanel).setCursor(Cursor.getDefaultCursor());
    }

    public void disconnect() {
        System.err.println("LoginPlugin disconnect()");
        try {
            SwingUtilities.getRoot(iPanel).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (in_ != null) in_.close();
            if (out_ != null) out_.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            SwingUtilities.getRoot(iPanel).setCursor(Cursor.getDefaultCursor());
            userInfo_.setUser(null);
            userInfo_.setPassword(null);
            if (channel_ != null) channel_.disconnect();
            if (session_ != null) session_.disconnect();
            iPanel.setOnline(false);
            auth = false;
            bus.broadcast(new OnlineStatus(false));
        }
    }
}

final class MyUserInfo implements UserInfo {

    private String user_;

    private String password_;

    private String passphrase_;

    public MyUserInfo(String user, String password, String passphrase) {
        user_ = user;
        password_ = password;
        passphrase_ = passphrase;
    }

    public MyUserInfo() {
    }

    public void setPassphrase(String passphrase_) {
        this.passphrase_ = passphrase_;
    }

    public void setPassword(String password_) {
        this.password_ = password_;
    }

    public void setUser(String user_) {
        this.user_ = user_;
    }

    public String getUser() {
        return user_;
    }

    public String getPassphrase() {
        return passphrase_;
    }

    public String getPassword() {
        return password_;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public boolean promptYesNo(String message) {
        return true;
    }

    public void showMessage(String message) {
    }
}
