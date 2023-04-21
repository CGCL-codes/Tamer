package archives.view.searchpanel.auto;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2011-12-30
 */
public class AutoCompletionFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public AutoCompletionFrame() {
        initComponents();
        Vector<String> v = new Vector<String>();
        File file = new File(System.getProperty("user.home"));
        String[] files = file.list();
        for (String filename : files) v.add(filename);
        autoCompletionField1.setFilter(new DefaultCompletionFilter(v));
    }

    private void initComponents() {
        autoCompletionField1 = new AutoCompletionField();
        jButton1 = new JButton();
        jLabel1 = new JLabel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jLabel1.setText("<html> <body> <h3>Welcome to swing demo!</h3> <ul> <li> The text field below provides completion list as you type.</li> <li>The list provided here is is the file list under your home directory. </li> <li>Double-click on the text field can invoke the completion drop list.</li> <li>Support for key navigations like up-down arrow or page up-down.</li> <li>Press exit button to terminate the program.</li> </ul> <p> <strong><i>Engjoy swing programming!</i></strong> </body> </html>  ");
        jLabel1.setBorder(BorderFactory.createEtchedBorder());
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE).addComponent(autoCompletionField1, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE).addComponent(jButton1, GroupLayout.Alignment.TRAILING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(autoCompletionField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addContainerGap()));
        pack();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AutoCompletionFrame().setVisible(true);
            }
        });
    }

    private AutoCompletionField autoCompletionField1;

    private JButton jButton1;

    private JLabel jLabel1;
}
