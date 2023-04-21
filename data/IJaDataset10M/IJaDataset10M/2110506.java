package dropandsend.net;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import dropandsend.data.FTSettings;
import dropandsend.gui.FTHostPane;
import dropandsend.gui.FileDropListener;
import dropandsend.gui.FileDropTarget;
import dropandsend.resources.icons.IconLocator;

/**
 * Representing a host, connected via TCP/IP socket.
 * 
 * @author skoeppen
 * @since 22.12.2005
 * 
 * Drop&Send Tool for distribute files.
 * Copyright (C) [2005]  [Sandro K�ppen dropsendprj@arcor.de]
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 * You should have received a copy of the GNU General Public License along with 
 * this program; if not, write to the 
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 * 
 * http://www.gnu.org/licenses/gpl.html
 */
public class FTConnection extends Thread implements FTHost {

    private InputStream m_Input = null;

    private OutputStream m_Output = null;

    private Socket m_Socket = null;

    private String m_Id = "";

    private FTHostPane m_pane = null;

    private int m_state = STATE_INIT;

    private FTConnector m_connector = null;

    private Vector m_buffer = null;

    /**
	 * Constructor 
	 * @param socket Socket that holds the connection
	 * @param connector Connector object
	 */
    public FTConnection(Socket socket, FTConnector connector) {
        m_Socket = socket;
        m_connector = connector;
        m_Id = m_Socket.getRemoteSocketAddress().toString();
        System.out.println("Connection: " + m_Id);
        try {
            m_Input = m_Socket.getInputStream();
            m_Output = m_Socket.getOutputStream();
            this.start();
            m_state = STATE_REDAY;
        } catch (Exception e) {
        }
    }

    /**
	 * Returns vector containing names of files in buffer.
	 * @return
	 */
    private Vector getBuffer() {
        if (m_buffer == null) m_buffer = new Vector();
        return m_buffer;
    }

    public int getBufferSize() {
        return getBuffer().size();
    }

    public FTHostPane getPane() {
        if (m_pane == null) {
            m_pane = new FTHostPane(getHostName());
            m_pane.getDisconnectBtn().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    disconnect();
                }
            });
            FileDropListener da = new FileDropListener((FileDropTarget) this, (Component) m_pane.getHostLogo());
        }
        return m_pane;
    }

    /**
	 * Disconnect host
	 *
	 */
    private void disconnect() {
        m_connector.disconnect(this.getConnectionId());
        m_connector = null;
        this.stop();
    }

    /**
	 * Is called when host was diosconnected.
	 *
	 */
    private void wasDisconnected() {
        m_connector.wasDisconnected(this.getConnectionId());
        m_connector = null;
        this.stop();
    }

    /**
	 * Returns socket
	 * @return
	 */
    public Socket getSocket() {
        return m_Socket;
    }

    public String getConnectionId() {
        return m_Id;
    }

    public String getHostName() {
        return getSocket().getInetAddress().getHostName();
    }

    /**
	 * Main loop whaiting for incomming data.
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        int c;
        String cmd = "";
        try {
            while ((c = m_Input.read()) != -1) {
                if ((char) c == '\n') {
                    handleCommand(cmd);
                    cmd = "";
                } else {
                    cmd = cmd + (char) c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            wasDisconnected();
        }
    }

    public boolean notifyDisconnection() {
        String cmd = "disconnect\n";
        try {
            m_Output.write(cmd.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            wasDisconnected();
            return false;
        }
    }

    public void transferFile(String filename) {
        if (m_state >= STATE_SENDING) {
            System.out.println("allready sending, put on buffer");
            getBuffer().add(filename);
            getPane().updateBufferSize(getBufferSize());
            return;
        }
        m_state += STATE_SENDING;
        File f = new File(filename);
        long l = f.length();
        JProgressBar pbar = getPane().getPBarOut();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setValue(0);
        String cmd = "transfer:" + f.getName() + ":" + String.valueOf(l) + "\n";
        try {
            m_Output.write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            wasDisconnected();
        }
        new Thread(new FileSender(pbar, m_Output, filename, l) {

            public void run() {
                try {
                    FileInputStream in = new FileInputStream(m_filename);
                    byte[] buf = new byte[4096];
                    int len;
                    long readBytes = 0;
                    while ((len = in.read(buf)) > 0) {
                        m_Output.write(buf, 0, len);
                        readBytes += len;
                        SwingUtilities.invokeLater(new myRunable(m_pbar, m_len, readBytes) {

                            public void run() {
                                int prc = (int) ((100.0 / (double) m_len) * (double) m_readBytes);
                                m_pbar.setValue(prc);
                            }
                        });
                        Thread.sleep(1);
                    }
                    m_pbar.setValue(0);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    wasDisconnected();
                }
                m_state -= STATE_SENDING;
                if (getBufferSize() > 0) {
                    String fname = (String) getBuffer().get(0);
                    getBuffer().remove(0);
                    getPane().updateBufferSize(getBufferSize());
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }
                    transferFile(fname);
                }
            }
        }).start();
        this.getPane().normalHostLogo();
    }

    /**
	 * Handle imcomming command.
	 * @param cmd
	 */
    private void handleCommand(String cmd) {
        System.out.println("handleCommand: " + cmd);
        if (cmd.startsWith("transfer:")) {
            handleFiletransfer(cmd);
        }
        if (cmd.startsWith("disconnect")) {
            wasDisconnected();
        }
    }

    /**
	 * Handle incomming file.
	 * @param cmd
	 */
    private void handleFiletransfer(String cmd) {
        m_state += STATE_RECIVING;
        String params[] = cmd.split(":");
        String filename = params[1];
        long l = Long.valueOf(params[2]).longValue();
        JProgressBar pbar = getPane().getPBarIn();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setValue(0);
        String storepath = FTSettings.getInstance().getProperty("storepath");
        if (!storepath.endsWith(String.valueOf(File.separatorChar)) && !storepath.equals("")) storepath += File.separatorChar;
        String m_filename = storepath + filename;
        System.out.println("store at: " + m_filename);
        long m_filelen = l;
        JProgressBar m_pbar = pbar;
        try {
            FileOutputStream out = new FileOutputStream(m_filename);
            int c;
            long len = 0;
            int sublen = 0;
            byte[] buf = new byte[4096];
            while (true) {
                if ((len + 4096) > m_filelen) {
                    int size = (int) (m_filelen - len);
                    buf = new byte[size];
                }
                sublen = m_Input.read(buf);
                out.write(buf, 0, sublen);
                len += sublen;
                System.out.println(len + " / " + m_filelen);
                if (len >= m_filelen) break;
                SwingUtilities.invokeLater(new myRunable(m_pbar, l, len) {

                    public void run() {
                        int prc = (int) ((100.0 / (double) m_len) * (double) m_readBytes);
                        m_pbar.setValue(prc);
                    }
                });
                Thread.sleep(1);
            }
            m_pbar.setValue(0);
            out.close();
            System.out.println("transfer finished");
        } catch (Exception e) {
            e.printStackTrace();
            wasDisconnected();
        }
        m_state -= STATE_RECIVING;
    }

    public void handleDroppedText(String text) {
        transferFile(text);
    }

    public void handleDragOver() {
        this.getPane().highlightHostLogo();
    }

    public void handleDragExit() {
        this.getPane().normalHostLogo();
    }
}

/**
 * Object for asynchron sending of a file.
 * 
 * @author skoeppen
 *
 */
abstract class FileSender implements Runnable {

    JProgressBar m_pbar = null;

    OutputStream m_Output = null;

    String m_filename = "";

    long m_len = 0;

    public FileSender(JProgressBar pbar, OutputStream out, String filename, long len) {
        m_filename = filename;
        m_pbar = pbar;
        m_Output = out;
        m_len = len;
    }
}

/**
 * Class for showing progress in a progressbar.
 * @author skoeppen
 *
 */
abstract class myRunable implements Runnable {

    JProgressBar m_pbar = null;

    long m_len = 0;

    long m_readBytes = 0;

    public myRunable(JProgressBar pbar, long len, long readBytes) {
        m_pbar = pbar;
        m_len = len;
        m_readBytes = readBytes;
    }
}
