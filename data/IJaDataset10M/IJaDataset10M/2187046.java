package gui;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagLayout;

public class SecundaryVisualClass extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu jMenu = null;

    private JMenu jMenu1 = null;

    private JMenuItem jMenuItem = null;

    private JMenuItem jMenuItem1 = null;

    private Panel panel = null;

    private Label label = null;

    private TextField textField = null;

    private Button button = null;

    private TextField textField1 = null;

    private TextField textField2 = null;

    private TextField textField3 = null;

    private TextField textField4 = null;

    private Label label1 = null;

    private Label label2 = null;

    private Label label3 = null;

    private Label label4 = null;

    private Panel panel1 = null;

    /**
	 * This is the default constructor
	 */
    public SecundaryVisualClass() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(748, 536);
        this.setJMenuBar(getJJMenuBar());
        this.setContentPane(getJContentPane());
        this.setTitle("My ANN Project");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            label = new Label();
            label.setBounds(new Rectangle(11, 16, 118, 23));
            label.setBackground(new Color(200, 255, 200));
            label.setText("Treinment Database");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getPanel(), null);
            jContentPane.add(label, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenu());
            jJMenuBar.add(getJMenu1());
        }
        return jJMenuBar;
    }

    /**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getJMenu() {
        if (jMenu == null) {
            jMenu = new JMenu();
            jMenu.setText("File");
            jMenu.add(getJMenuItem1());
        }
        return jMenu;
    }

    /**
	 * This method initializes jMenu1	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getJMenu1() {
        if (jMenu1 == null) {
            jMenu1 = new JMenu();
            jMenu1.setText("About");
            jMenu1.add(getJMenuItem());
        }
        return jMenu1;
    }

    /**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItem() {
        if (jMenuItem == null) {
            jMenuItem = new JMenuItem();
            jMenuItem.setText("Author");
        }
        return jMenuItem;
    }

    /**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItem1() {
        if (jMenuItem1 == null) {
            jMenuItem1 = new JMenuItem();
            jMenuItem1.setText("Open");
        }
        return jMenuItem1;
    }

    /**
	 * This method initializes panel	
	 * 	
	 * @return java.awt.Panel	
	 */
    private Panel getPanel() {
        if (panel == null) {
            label4 = new Label();
            label4.setBounds(new Rectangle(11, 155, 60, 23));
            label4.setText("Samples:");
            label3 = new Label();
            label3.setBounds(new Rectangle(12, 126, 59, 23));
            label3.setText("Classes:");
            label2 = new Label();
            label2.setBounds(new Rectangle(11, 96, 60, 23));
            label2.setText("Attributes:");
            label1 = new Label();
            label1.setBounds(new Rectangle(11, 65, 60, 23));
            label1.setText("Name:");
            panel = new Panel();
            panel.setLayout(null);
            panel.setBounds(new Rectangle(10, 39, 716, 206));
            panel.setBackground(new Color(200, 255, 200));
            panel.add(getTextField(), null);
            panel.add(getButton(), null);
            panel.add(getTextField1(), null);
            panel.add(getTextField2(), null);
            panel.add(getTextField3(), null);
            panel.add(getTextField4(), null);
            panel.add(label1, null);
            panel.add(label2, null);
            panel.add(label3, null);
            panel.add(label4, null);
            panel.add(getPanel1(), null);
        }
        return panel;
    }

    /**
	 * This method initializes textField	
	 * 	
	 * @return java.awt.TextField	
	 */
    private TextField getTextField() {
        if (textField == null) {
            textField = new TextField();
            textField.setBounds(new Rectangle(8, 11, 181, 21));
            textField.setEditable(false);
            textField.setText("<Load database>");
        }
        return textField;
    }

    /**
	 * This method initializes button	
	 * 	
	 * @return java.awt.Button	
	 */
    private Button getButton() {
        if (button == null) {
            button = new Button();
            button.setBounds(new Rectangle(194, 8, 32, 23));
            button.setLabel("...");
        }
        return button;
    }

    /**
	 * This method initializes textField1	
	 * 	
	 * @return java.awt.TextField	
	 */
    private TextField getTextField1() {
        if (textField1 == null) {
            textField1 = new TextField();
            textField1.setBounds(new Rectangle(75, 66, 125, 23));
        }
        return textField1;
    }

    /**
	 * This method initializes textField2	
	 * 	
	 * @return java.awt.TextField	
	 */
    private TextField getTextField2() {
        if (textField2 == null) {
            textField2 = new TextField();
            textField2.setBounds(new Rectangle(76, 96, 36, 23));
        }
        return textField2;
    }

    /**
	 * This method initializes textField3	
	 * 	
	 * @return java.awt.TextField	
	 */
    private TextField getTextField3() {
        if (textField3 == null) {
            textField3 = new TextField();
            textField3.setBounds(new Rectangle(77, 125, 36, 23));
        }
        return textField3;
    }

    /**
	 * This method initializes textField4	
	 * 	
	 * @return java.awt.TextField	
	 */
    private TextField getTextField4() {
        if (textField4 == null) {
            textField4 = new TextField();
            textField4.setBounds(new Rectangle(78, 155, 36, 23));
        }
        return textField4;
    }

    /**
	 * This method initializes panel1	
	 * 	
	 * @return java.awt.Panel	
	 */
    private Panel getPanel1() {
        if (panel1 == null) {
            panel1 = new Panel();
            panel1.setLayout(new GridBagLayout());
            panel1.setBounds(new Rectangle(293, 4, 418, 196));
        }
        return panel1;
    }
}
