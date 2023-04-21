package org.wensefu.v2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author wensefu.jerry.Ling<br/>
 *         wrote on 2011-1-26
 */
public class AppFrame extends JFrame {

    private static final long serialVersionUID = 4157793076882258559L;

    private static int width = 600;

    private static int height = 400;

    private static String title = "QQ�ռ���־������v1.0 author:wensefu";

    private static final String initStr = "��ǰû�п���ʾ����Ϣ";

    private JLabel tip = new JLabel("������QQ��:");

    private JTextField qq = new JTextField(10);

    private JButton get = new JButton("������־");

    private final JLabel pathJl = new JLabel("ѡ����Ŀ¼:");

    private final JTextField path = new JTextField(20);

    private final JButton brown = new JButton("���...");

    private JTextArea console = new JTextArea(initStr, 15, 53);

    private JScrollPane js = new JScrollPane(console);

    private Kernel kernel = new Kernel();

    public AppFrame() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setLocation(screenWidth / 4, screenHeight / 4);
        JPanel p1 = new JPanel();
        p1.add(tip);
        p1.add(qq);
        p1.add(get);
        add(p1, BorderLayout.NORTH);
        JPanel p2 = new JPanel();
        p2.add(pathJl);
        p2.add(path);
        p2.add(brown);
        add(p2, BorderLayout.CENTER);
        JPanel p3 = new JPanel();
        console.setEditable(false);
        p3.add(js);
        add(p3, BorderLayout.SOUTH);
    }

    private class DownLoadTask implements Runnable {

        @Override
        public void run() {
            String qqNum = qq.getText();
            String savePath = path.getText();
            if (null == savePath || "".equals(savePath)) {
                JOptionPane.showMessageDialog(AppFrame.this, "��ѡ�񱣴�·��!");
                return;
            }
            if (!qqNum.matches("[1-9]\\d{4}\\d*")) {
                JOptionPane.showMessageDialog(AppFrame.this, "�������QQ��������!");
                return;
            }
            console.setText("");
            console.append("���ڻ�ȡ��־�б�...");
            console.append("\n");
            int cnt = kernel.getBlogNo(qqNum);
            if (cnt == 0) {
                console.append("��ȡ��־�б�ʧ��,�������û�û����־���������.");
            } else {
                console.append("��־�б��ȡ�ɹ�,��ʼ������־.");
                console.append("\n");
            }
            for (int i = 0; i < cnt; i++) {
                console.append("�������ص�" + (i + 1) + "ƪ��־...");
                console.append("\n");
                kernel.saveBlogtoLocal(savePath, qqNum, i);
                console.append("��" + (i + 1) + "ƪ��־�������.");
                console.append("\n");
            }
            console.append("��־����ȫ�����,�ѱ�����" + path);
            console.append("\n");
        }
    }

    private void setListener() {
        brown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jc = new JFileChooser();
                jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int reValue = jc.showOpenDialog(AppFrame.this);
                if (reValue == JFileChooser.APPROVE_OPTION) {
                    path.setText(jc.getSelectedFile().getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "���ֶ�����Ŀ¼");
                }
            }
        });
        get.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new DownLoadTask()).start();
            }
        });
    }

    public static void main(String[] args) {
        AppFrame app = new AppFrame();
        SwingFramework.lunch(app, width, height, title);
        app.setListener();
    }
}
