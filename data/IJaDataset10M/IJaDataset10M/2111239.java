package org.objectwiz.geotools;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.objectwiz.entityDAO.exceptions.NonexistentEntityException;
import org.objectwiz.function.ManipulateEntity;

/**
 *
 * @author xym
 */
public class EndIntervention extends JDialog {

    private JTextField t1;

    private JTextArea ta;

    public EndIntervention() {
        initUI();
    }

    public final void initUI() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel label = new JLabel("Please input the id number");
        label.setFont(new Font("Serif", Font.BOLD, 13));
        label.setAlignmentX(0.5f);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 5, 5));
        panel.setLayout(new GridLayout(2, 2, 5, 5));
        t1 = new JTextField(15);
        JLabel l1 = new JLabel("ID");
        ta = new JTextArea(1, 15);
        ta.setVisible(false);
        panel.add(l1);
        panel.add(t1);
        panel.add(ta);
        add(panel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        JButton finish = new JButton("Finish");
        finish.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                ManipulateEntity me = new ManipulateEntity();
                int id = Integer.parseInt(t1.getText());
                try {
                    me.endIntervention(id);
                    t1.setText(null);
                    ta.setText("End successful");
                    ta.setVisible(true);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EndIntervention.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(EndIntervention.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(finish);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Delete Client");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(300, 150);
    }
}
