package de.sharpner.jcmd.graphic;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import de.sharpner.jcmd.system.FileDownload;
import de.sharpner.jcmd.system.StatusCheck;

public class DownloadDialogues {

    public static String getDownloadName(int x, int y) {
        class DName extends JDialog {

            private JTextField textField = new JTextField("http://");

            private JButton ok = new JButton("ok");

            private JButton cancel = new JButton("cancel");

            private String pressed = null;

            private JLabel label = new JLabel("Enter URL:");

            public DName(int x, int y) {
                this.setTitle("Enter URL:");
                textField.setCaretPosition("http://".length());
                textField.setSize(180, 20);
                this.setModalityType(ModalityType.APPLICATION_MODAL);
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        pressed = textField.getText();
                        setVisible(false);
                        dispose();
                    }
                });
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        dispose();
                    }
                });
                this.setBounds(x, y, 300, 110);
                JPanel center_it = new JPanel(new GridLayout(2, 1));
                center_it.add(label);
                center_it.add(textField);
                Box hbox = Box.createHorizontalBox();
                hbox.add(cancel);
                hbox.add(Box.createHorizontalGlue());
                hbox.add(ok);
                ok.setMnemonic(KeyEvent.VK_O);
                cancel.setMnemonic(KeyEvent.VK_C);
                this.add(label, BorderLayout.NORTH);
                this.add(center_it, BorderLayout.CENTER);
                this.add(hbox, BorderLayout.SOUTH);
            }

            public String getLink() {
                return pressed;
            }
        }
        DName dialog = new DName(x, y);
        dialog.setVisible(true);
        return dialog.getLink();
    }

    public static int DownloadProgress(int x, int y, StatusCheck file) {
        class StatCheck extends Thread {

            private JFrame frame;

            private StatusCheck file;

            private JProgressBar currbar = new JProgressBar();

            private JProgressBar totalbar = new JProgressBar();

            public StatCheck(int x, int y, StatusCheck file) {
                frame = new JFrame();
                this.file = file;
                file.start();
                frame.setLayout(new GridLayout(5, 1));
                currbar.setToolTipText(file.toString());
                totalbar.setToolTipText(file.toString());
                JButton ok = new JButton("ok");
                JButton cancel = new JButton("cancel");
                JLabel current = new JLabel("Current:");
                current.setToolTipText(file.toString());
                JLabel total = new JLabel("Total:");
                total.setToolTipText(file.toString());
                Box hbox = Box.createHorizontalBox();
                hbox.add(cancel);
                hbox.add(Box.createHorizontalGlue());
                hbox.add(ok);
                ok.setMnemonic(KeyEvent.VK_O);
                cancel.setMnemonic(KeyEvent.VK_C);
                frame.setSize(350, 100);
                frame.add(current);
                frame.add(currbar);
                frame.add(total);
                frame.add(totalbar);
                frame.add(hbox);
                frame.setVisible(true);
            }

            public void run() {
                int currLast = 0;
                int totalLast = 0;
                frame.setTitle("Speed:" + file.getSpeed());
                while (file.getStatus() != 100) {
                    try {
                        int currStat = file.getStatus();
                        int totalStat = file.getTotalStatus();
                        if (totalStat != totalLast) {
                            totalbar.setValue(totalStat);
                        }
                        if (currStat != currLast) {
                            currLast = currStat;
                            currbar.setValue(currStat);
                            frame.setTitle("Status: " + currStat + "%");
                        }
                        frame.setTitle(file.getSpeed() + "kb/s - " + file.getStatus() + "%");
                        Thread.sleep(200);
                    } catch (Exception e) {
                        System.out.print("Error: ");
                        e.printStackTrace();
                    }
                }
                currbar.setValue(100);
                totalbar.setValue(100);
                frame.setTitle("Status: 100% - Done");
            }
        }
        StatCheck stat = new StatCheck(x, y, file);
        stat.start();
        return 0;
    }

    public static void main(String[] args) {
        String temp = DownloadDialogues.getDownloadName(100, 100);
        if (temp != null) {
            System.out.println(temp);
            FileDownload fd = new FileDownload(temp, "/home/sharpner/nino.img");
            if (fd.getError() == 0) {
                fd.connect();
                DownloadDialogues.DownloadProgress(1, 1, fd);
            }
        }
        System.out.println(temp);
    }
}
