package rath.msnm.swing.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rath.msnm.MSNMessenger;
import rath.msnm.event.MsnAdapter;
import rath.msnm.swing.ToolBox;

/**
 * ģ�� ����ϴ� ���̾�α�
 *
 * @author Jang-Ho Hwang, rath@linuxkorea.co.kr
 * @version $Id: AddFriendDialog.java,v 1.2 2002/03/05 13:41:42 xrath Exp $
 */
public class AddFriendDialog extends DefaultDialog implements ToolBox, ActionListener {

    private MSNMessenger msnm = null;

    private JTextField emailField = null;

    private JButton addButton, cancelButton;

    private JLabel commentLabel = null;

    private JProgressBar progress = null;

    private Timer timer = null;

    private MsnAdapter baseAdapter = new Adapter();

    public AddFriendDialog(Frame owner, MSNMessenger msnm) {
        super(owner);
        setTitle("���ο� ģ���� ����մϴ�.");
        this.msnm = msnm;
        this.msnm.addMsnListener(baseAdapter);
        createComponents();
    }

    private class Adapter extends MsnAdapter {

        public void addFailed(int err) {
            String msg = null;
            switch(err) {
                case 210:
                    msg = "����Ʈ�� �ѵ� ũ�⸦ �Ѿ� ����� �� ����ϴ�.";
                    break;
                case 215:
                    msg = "�̹� ��ϵǾ��ִ� ������Դϴ�.";
                    break;
                case 205:
                case 208:
                    msg = "MSN�� ��ϵǾ����� ���� ����� �̸��Դϴ�.";
                    break;
            }
            enableAll(msg);
        }

        public void buddyListModified() {
            String email = emailField.getText().trim();
            enableAll(email + "���� ���������� �߰��Ͽ����ϴ�.");
        }
    }

    private void createComponents() {
        setSize(420, 160);
        JPanel panel = (JPanel) getContentPane();
        JPanel mainPanel = new JPanel();
        JLabel label = new JLabel("����� ������� ���� �ּ�");
        label.setFont(FONT);
        emailField = new JTextField(20);
        emailField.setFont(FONT);
        emailField.addActionListener(this);
        mainPanel.add(label, "West");
        mainPanel.add(emailField, "Center");
        FlowLayout fl = new FlowLayout();
        JPanel centerPanel = new JPanel(fl);
        centerPanel.add(mainPanel);
        commentLabel = new JLabel("ģ���� ����� ������� �����ּҸ� �Է��ϼ���");
        commentLabel.setBorder(BorderFactory.createEtchedBorder());
        commentLabel.setPreferredSize(new Dimension(getSize().width - 20, 24));
        commentLabel.setFont(FONT);
        progress = new JProgressBar(1, 15);
        progress.setPreferredSize(new Dimension(getSize().width - 20, 16));
        timer = new Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                progress.setValue(progress.getValue() + 1);
            }
        });
        fl.setAlignment(FlowLayout.LEADING);
        centerPanel.add(progress);
        centerPanel.add(commentLabel);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        addButton = new JButton("�߰�(A)");
        addButton.setFont(FONT);
        addButton.setMnemonic('A');
        addButton.addActionListener(this);
        cancelButton = new JButton("���");
        cancelButton.setFont(FONT);
        cancelButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        panel.add(centerPanel, "Center");
        panel.add(buttonPanel, "South");
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cancelButton) {
            dispose();
        } else if (src == emailField || src == addButton) {
            String email = emailField.getText().trim();
            if (email.length() == 0) {
                emailField.requestFocus();
                return;
            }
            processAdd(email);
        }
    }

    protected void disableAll() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                emailField.setEnabled(false);
                addButton.setEnabled(false);
                cancelButton.setEnabled(false);
                progress.setValue(0);
                timer.start();
            }
        });
    }

    protected void enableAll(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                commentLabel.setText(msg);
                emailField.setEnabled(true);
                addButton.setEnabled(true);
                cancelButton.setEnabled(true);
                timer.stop();
                progress.setValue(100);
            }
        });
    }

    protected void processAdd(String email) {
        try {
            disableAll();
            msnm.addFriend(email);
        } catch (Exception e) {
        }
    }

    public void dispose() {
        msnm.removeMsnListener(baseAdapter);
        super.dispose();
    }
}
