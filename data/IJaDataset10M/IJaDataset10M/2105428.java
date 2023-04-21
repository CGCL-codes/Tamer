package shu.util.shm.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import shu.util.shm.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ShareMemoryClient extends JFrame implements ShareMemoryObserver {

    protected JButton btnClose;

    protected JTextArea textArea;

    protected ShareMemory shm;

    public ShareMemoryClient() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        textArea = new JTextArea();
        contentPane.add(textArea, BorderLayout.CENTER);
        btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                destroy();
            }
        });
        JPanel panelBtn = new JPanel();
        panelBtn.add(btnClose);
        contentPane.add(panelBtn, BorderLayout.SOUTH);
        shm = new ShareMemory(this);
        setSize(new Dimension(400, 300));
        setTitle("Auto CP");
    }

    private void destroy() {
        shm.destroy();
        System.exit(0);
    }

    public void onDataReady(int datasize) {
        byte[] bytearray = null;
        if (datasize != -1) {
            bytearray = shm.readByteArrayFromMem(datasize);
        } else {
            bytearray = shm.readByteArrayFromMem();
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytearray) {
            buf.append((int) b);
            buf.append(' ');
        }
        textArea.setText(buf.toString());
    }

    public void onDataReady() {
        onDataReady(-1);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            destroy();
        }
    }

    public static void main(String[] args) {
        ShareMemoryClient frame = new ShareMemoryClient();
        frame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }
}
