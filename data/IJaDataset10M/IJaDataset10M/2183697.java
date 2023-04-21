package administrator;

import java.awt.EventQueue;

public class QuesSetUp {

    public JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    QuesSetUp window = new QuesSetUp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public QuesSetUp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(375, 150, 500, 366);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        JButton Addbtn = new JButton("Add Question");
        Addbtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddbtnActionPerformed(evt);
            }
        });
        Addbtn.setBounds(180, 127, 128, 23);
        frame.getContentPane().add(Addbtn);
        JButton Editbtn = new JButton("Edit Question");
        Editbtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditbtnActionPerformed(evt);
            }
        });
        Editbtn.setBounds(180, 173, 128, 23);
        frame.getContentPane().add(Editbtn);
        JButton Deletebtn = new JButton("Delete Question");
        Deletebtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeletebtnActionPerformed(evt);
            }
        });
        Deletebtn.setBounds(180, 218, 128, 23);
        frame.getContentPane().add(Deletebtn);
        JButton Backbtn = new JButton("Back");
        Backbtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackbtnActionPerformed(evt);
            }
        });
        Backbtn.setBounds(202, 278, 73, 23);
        frame.getContentPane().add(Backbtn);
        JPanel panel = new JPanel();
        panel.setBounds(10, 50, 464, 34);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        JLabel lblWhatWouldYou = new JLabel("What would you like to do?");
        lblWhatWouldYou.setBounds(0, 0, 464, 34);
        panel.add(lblWhatWouldYou);
        lblWhatWouldYou.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblWhatWouldYou.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void AddbtnActionPerformed(java.awt.event.ActionEvent evt) {
        AddQues aq = new AddQues();
        aq.frame.setVisible(true);
        this.frame.setVisible(false);
    }

    private void DeletebtnActionPerformed(java.awt.event.ActionEvent evt) {
        DelQues dq = new DelQues();
        dq.frame.setVisible(true);
        this.frame.setVisible(false);
    }

    private void EditbtnActionPerformed(java.awt.event.ActionEvent evt) {
        EditQues eq = new EditQues();
        eq.frame.setVisible(true);
        this.frame.setVisible(false);
    }

    private void BackbtnActionPerformed(java.awt.event.ActionEvent evt) {
        Admn_Menu am = new Admn_Menu();
        Admin_Login al = new Admin_Login();
        am.frame.setVisible(true);
        this.frame.setVisible(false);
        am.textField.setText(al.user_name);
    }
}
