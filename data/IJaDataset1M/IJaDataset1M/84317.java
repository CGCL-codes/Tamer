package org.privale.ui.ggui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NewPostFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private GGUI G;

    private JLabel Attach;

    private JLabel GroupLabel;

    private JLabel Author;

    private JTextField Subject;

    private JTextArea TextArea;

    private Group Group;

    private JFileChooser AttachChooser;

    private File[] BinaryData;

    public NewPostFrame(GGUI g) {
        G = g;
        setTitle("New Post");
        JPanel top = new JPanel(new GridLayout(0, 1));
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton b = new JButton("Post");
        b.addActionListener(this);
        b.setActionCommand("POST");
        jp.add(b);
        b = new JButton("Cancel");
        b.addActionListener(this);
        b.setActionCommand("CANCEL");
        jp.add(b);
        b = new JButton("Attach Binary");
        b.addActionListener(this);
        b.setActionCommand("ATTACH");
        jp.add(b);
        top.add(jp);
        jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Attach = new JLabel("");
        jp.add(new JLabel("Attachment:"));
        jp.add(Attach);
        top.add(jp);
        jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        GroupLabel = new JLabel("");
        jp.add(new JLabel("Group:"));
        jp.add(GroupLabel);
        top.add(jp);
        jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Author = new JLabel("");
        jp.add(new JLabel("Author:"));
        jp.add(Author);
        top.add(jp);
        jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jl = new JLabel("Subject: ");
        jp.add(jl);
        Subject = new JTextField("");
        Subject.setColumns(30);
        jp.add(Subject);
        top.add(jp);
        TextArea = new JTextArea();
        JScrollPane jsp = new JScrollPane(TextArea);
        JSplitPane sp0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, jsp);
        add(sp0);
        setSize(600, 400);
    }

    public void setUser(String usr) {
        Author.setText(usr);
    }

    public void setGroup(Group g) {
        GroupLabel.setText(g.getGroupName());
        Group = g;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("POST")) {
            setVisible(false);
            Group group = Group;
            if (G != null && group != null) {
                System.out.println("HERE!!");
                try {
                    if (BinaryData != null) {
                        for (int cnt = 0; cnt < BinaryData.length; cnt++) {
                            System.out.println("HERE2!!!!!!!!!");
                            new GroupPost(G, group, Subject.getText(), Author.getText(), TextArea.getText(), BinaryData[cnt]);
                        }
                    } else {
                        new GroupPost(G, group, Subject.getText(), Author.getText(), TextArea.getText(), null);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    GGUI.OpenMessageWindow(false, "18 " + e1.getMessage());
                }
            }
        }
        if (e.getActionCommand().equals("CANCEL")) {
            setVisible(false);
        }
        if (e.getActionCommand().equals("ATTACH")) {
            if (AttachChooser == null) {
                AttachChooser = new JFileChooser();
                AttachChooser.setMultiSelectionEnabled(true);
            }
            BinaryData = null;
            Attach.setText("No attachment.");
            int val = AttachChooser.showOpenDialog(this);
            System.out.println("Dialog value : " + val);
            if (val == JFileChooser.APPROVE_OPTION) {
                BinaryData = AttachChooser.getSelectedFiles();
                String ats = BinaryData[0].getPath();
                for (int cnt = 1; cnt < BinaryData.length; cnt++) {
                    ats = ats + ", " + BinaryData[cnt].getPath();
                }
                Attach.setText(ats);
            }
        }
    }
}
