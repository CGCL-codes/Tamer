package com.ruanko.server;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Middle1 extends JPanel {

    private JButton btn1;

    private JButton btn2;

    private JButton btn3;

    private JButton btn4;

    public Middle1() {
        try {
            this.setSize(400, 200);
            this.setLayout(null);
            btn1 = new JButton();
            btn2 = new JButton();
            btn3 = new JButton();
            btn4 = new JButton();
            btn1.setText("��");
            btn2.setText("��");
            btn3.setText("��");
            btn4.setText("��");
            btn1.setVisible(true);
            btn2.setVisible(true);
            btn3.setVisible(true);
            btn4.setVisible(true);
            this.add(btn1);
            btn1.setBounds(3, 3, (int) (this.getWidth() * 0.15), 45);
            this.add(btn2);
            btn2.setBounds(6 + (int) (this.getWidth() * 0.15), 3, (int) (this.getWidth() * 0.15), 45);
            this.add(btn3);
            btn3.setBounds(9 + 2 * (int) (this.getWidth() * 0.15), 3, (int) (this.getWidth() * 0.15), 45);
            this.add(btn4);
            btn4.setBounds(12 + 3 * (int) (this.getWidth() * 0.15), 3, (int) (this.getWidth() * 0.15), 45);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        try {
            {
                this.setLayout(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
