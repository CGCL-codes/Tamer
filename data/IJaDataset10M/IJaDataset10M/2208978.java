package GUI;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.Font;

public class IpIncorrect extends JPanel {

    public JLabel getJLabelIP() {
        return jLabelIP;
    }

    public void setJLabelIP(JLabel labelIP) {
        jLabelIP = labelIP;
    }

    private static final long serialVersionUID = 1L;

    private JLabel jLabelIP = null;

    /**
	 * This is the default constructor
	 */
    public IpIncorrect() {
        super();
        System.out.println("IpIncorrect C'tor:: START");
        initialize();
        System.out.println("IpIncorrect C'tor:: END");
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        jLabelIP = new JLabel();
        jLabelIP.setBounds(new Rectangle(65, 13, 194, 111));
        jLabelIP.setText("Incorrect IP, Bye Bye..");
        jLabelIP.setFont(new Font("David", Font.BOLD, 18));
        jLabelIP.setForeground(Color.RED);
        this.setSize(521, 557);
        this.setLayout(null);
        this.add(jLabelIP, null);
    }
}
