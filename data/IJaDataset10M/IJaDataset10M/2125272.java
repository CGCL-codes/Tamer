package net.narusas.cafelibrary.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;
import javax.swing.JOptionPane;
import net.narusas.cafelibrary.Library;
import net.narusas.cafelibrary.serial.HTMLPublisher;
import net.narusas.cafelibrary.serial.LoginFailException;

public class FTPPublishController {

    private final Library lib;

    private final MainFrame f;

    protected boolean doPublish;

    private Color original;

    public FTPPublishController(Library lib, MainFrame f) {
        this.lib = lib;
        this.f = f;
    }

    public void show() {
        doPublish = false;
        final FTPPublishDialog d = new FTPPublishDialog(f);
        Properties props = new Properties();
        try {
            props.load(new FileReader(new File("ftp.cfg")));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        d.getServerTextField().setText(props.getProperty("server"));
        d.getIdTextField().setText(props.getProperty("id"));
        d.getPasswordTextField().setText(props.getProperty("password"));
        d.getPathTextField().setText(props.getProperty("path"));
        if (original == null) {
            original = d.getPublishButton().getBackground();
        }
        d.getPublishButton().setBackground(original);
        d.getPublishButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                d.getPublishButton().setBackground(d.getBackground());
                d.setVisible(false);
                doPublish = true;
            }
        });
        d.getCancelButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                d.setVisible(false);
                doPublish = false;
            }
        });
        d.setModalityType(ModalityType.APPLICATION_MODAL);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - d.getWidth()) / 2;
        int y = (screenSize.height - d.getHeight()) / 2;
        d.setLocation(x, y);
        d.setVisible(true);
        if (doPublish) {
            saveProps(d);
            publish(d);
        }
    }

    private void saveProps(FTPPublishDialog d) {
        Properties props = new Properties();
        props.setProperty("server", d.getServerTextField().getText());
        props.setProperty("id", d.getIdTextField().getText());
        props.setProperty("password", d.getPasswordTextField().getText());
        props.setProperty("path", d.getPathTextField().getText());
        try {
            props.store(new FileWriter(new File("ftp.cfg")), "FTP Publihs configuration");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void publish(FTPPublishDialog d) {
        final String server = d.getServerTextField().getText();
        final String id = d.getIdTextField().getText();
        final String passwd = d.getPasswordTextField().getText();
        final String path = d.getPathTextField().getText();
        new Thread() {

            public void run() {
                try {
                    String html = HTMLPublisher.toHTML(lib);
                    HTMLPublisher.publish(server, id, passwd, path, html);
                    System.out.println(html);
                    f.getBookTableControlPanel().getPublishButton().setToolTipText("���ǿ� �����߽��ϴ�");
                } catch (LoginFailException e) {
                    e.printStackTrace();
                    new Warning(f.getBookTableControlPanel().getPublishButton(), "FTP �α��ο� ���� �߽��ϴ�.���̵�� ��ȣ�� Ȯ���� �ּ���." + e.getMessage());
                } catch (SocketException e) {
                    e.printStackTrace();
                    new Warning(f.getBookTableControlPanel().getPublishButton(), "FTP���ǿ� �����߽��ϴ�1." + e.getMessage());
                } catch (FileNotFoundException e) {
                    new Warning(f.getBookTableControlPanel().getPublishButton(), "FTP���ǿ� �����߽��ϴ�2." + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    new Warning(f.getBookTableControlPanel().getPublishButton(), "FTP���ǿ� �����߽��ϴ�3." + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    new Warning(f.getBookTableControlPanel().getPublishButton(), "FTP ���ῡ  �����߽��ϴ�. �����ּҳ� ��Ʈ�� ������ Ȯ���� �ּ���." + e.getMessage());
                }
            }
        }.start();
    }
}
