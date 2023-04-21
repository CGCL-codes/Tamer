package easyexplore;

import org.jdesktop.layout.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import easyconnect.easymail.*;
import com.docuverse.swt.flash.FlashPlayer;

public class Attach extends JDialog {

    public String[][] contacts;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static FlashPlayer mainPlayer;

    public String file;

    public Boolean doc_pic;

    public static void main(String args[]) {
        Attach program = new Attach(new JFrame(), mainPlayer);
        program.display(" ", true);
    }

    public Attach(Frame parent, FlashPlayer player) {
        super(parent);
        mainPlayer = player;
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
    }

    public void display(String f, Boolean d) {
        ReadContacts read = new ReadContacts();
        contacts = read.ReadContacts();
        file = f;
        doc_pic = d;
        label_title = new JLabel();
        jScrollPane1 = new JScrollPane();
        list_contacts = new JList();
        btn_select = new JButton();
        btn_cancel = new JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        label_title.setFont(new Font("Tahoma", 0, 24));
        label_title.setText("Please select a friend to email this to:");
        list_contacts.setFont(new Font("Tahoma", 0, 36));
        list_contacts.setModel(new AbstractListModel() {

            public int getSize() {
                return contacts.length;
            }

            public Object getElementAt(int i) {
                return contacts[i][0];
            }
        });
        jScrollPane1.setViewportView(list_contacts);
        btn_select.setFont(new Font("Tahoma", 1, 14));
        btn_select.setText("Select");
        btn_select.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                btn_selectActionPerformed(evt);
            }
        });
        btn_cancel.setFont(new java.awt.Font("Tahoma", 1, 14));
        btn_cancel.setText("Cancel");
        btn_cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(28, 28, 28).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 416, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(label_title))).add(layout.createSequentialGroup().add(134, 134, 134).add(btn_select, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(15, 15, 15).add(btn_cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap(29, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(label_title).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 198, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btn_select, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE).add(btn_cancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)).addContainerGap()));
        setLocation(screenSize.width / 4, screenSize.height / 4);
        setVisible(true);
        toFront();
        pack();
    }

    JButton btn_select;

    JButton btn_cancel;

    JScrollPane jScrollPane1;

    JLabel label_title;

    JList list_contacts;

    public void btn_cancelActionPerformed(ActionEvent evt) {
        mainPlayer.setVariable("rtaskbar", "type");
        dispose();
    }

    public void btn_selectActionPerformed(ActionEvent evt) {
        EasyMail em = new EasyMail(new JFrame(), mainPlayer);
        dispose();
        em.display(file, list_contacts.getSelectedIndex(), doc_pic);
    }
}
