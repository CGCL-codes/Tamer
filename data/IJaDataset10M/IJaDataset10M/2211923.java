package at.fhj.itm10.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5508751885325648427L;

    private MainWindow parent;

    private JButton btnAddCustomer;

    public ControlPanel(MainWindow parent) {
        this.parent = parent;
        initContent();
    }

    private void initContent() {
        this.setLayout(new FlowLayout());
        this.btnAddCustomer = new JButton("Kunde hinzuf�gen");
        this.btnAddCustomer.setPreferredSize(new Dimension(parent.getSize().width, 30));
        this.btnAddCustomer.setFocusPainted(false);
        this.btnAddCustomer.addActionListener(this);
        this.add(btnAddCustomer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAddCustomer)) {
            parent.displayCustomerPanel(true);
        }
    }
}
