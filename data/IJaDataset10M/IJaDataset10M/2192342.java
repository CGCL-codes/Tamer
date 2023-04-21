package test;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarUI implements ActionListener {

    private static final String DEFAULT_STATUS = "Please Waiting";

    private JDialog dialog;

    private JProgressBar progressBar;

    private JLabel lbStatus;

    private JButton btnCancel;

    private Window parent;

    private Thread thread;

    private String statusInfo;

    private String resultInfo;

    private String cancelInfo;

    public static void show(Window parent, Thread thread) {
        new ProgressBarUI(parent, thread, DEFAULT_STATUS, null, null);
    }

    public static void show(Window parent, Thread thread, String statusInfo) {
        new ProgressBarUI(parent, thread, statusInfo, null, null);
    }

    public static void show(Window parent, Thread thread, String statusInfo, String resultInfo, String cancelInfo) {
        new ProgressBarUI(parent, thread, statusInfo, resultInfo, cancelInfo);
    }

    /**
	 * 
	 * @param parent
	 *            Window
	 * @param thread
	 *            Thread
	 * @param statusInfo
	 *            String
	 * @param resultInfo
	 *            String �����������Ϻ���ʾ����Ϣ
	 * @param cancelInfo
	 *            String
	 */
    private ProgressBarUI(Window parent, Thread thread, String statusInfo, String resultInfo, String cancelInfo) {
        this.parent = parent;
        this.thread = thread;
        this.statusInfo = statusInfo;
        this.resultInfo = resultInfo;
        this.cancelInfo = cancelInfo;
        initUI();
        startThread();
        dialog.setVisible(true);
    }

    private void initUI() {
        if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent, true);
        } else if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent, true);
        } else {
            dialog = new JDialog((Frame) null, true);
        }
        final JPanel mainPane = new JPanel(null);
        progressBar = new JProgressBar();
        lbStatus = new JLabel("" + statusInfo);
        btnCancel = new JButton("Cancel");
        progressBar.setIndeterminate(true);
        btnCancel.addActionListener(this);
        mainPane.add(progressBar);
        mainPane.add(lbStatus);
        dialog.getContentPane().add(mainPane);
        dialog.setUndecorated(true);
        dialog.setResizable(true);
        dialog.setSize(390, 100);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        mainPane.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                layout(mainPane.getWidth(), mainPane.getHeight());
            }
        });
    }

    private void startThread() {
        new Thread() {

            public void run() {
                try {
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dispose();
                    if (resultInfo != null && !resultInfo.trim().equals("")) {
                        String title = "��ʾ";
                        JOptionPane.showMessageDialog(parent, resultInfo, title, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }.start();
    }

    private void layout(int width, int height) {
        progressBar.setBounds(20, 20, 350, 15);
        lbStatus.setBounds(20, 50, 350, 25);
        btnCancel.setBounds(width - 85, height - 31, 75, 21);
    }

    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e) {
        resultInfo = cancelInfo;
        thread.interrupt();
    }

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread() {

            public void run() {
                int index = 0;
                while (index < 5) {
                    try {
                        sleep(1000);
                        System.out.println(++index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ProgressBarUI.show((Frame) null, thread, "Status", "Result", "Cancel");
    }
}
