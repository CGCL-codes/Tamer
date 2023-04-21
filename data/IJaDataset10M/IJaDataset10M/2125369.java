package pl.o2.asdluki.rozrusznik.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.util.Iterator;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import pl.o2.asdluki.rozrusznik.model.NMinMax;
import pl.o2.asdluki.rozrusznik.model.Sk;
import pl.o2.asdluki.rozrusznik.view.util.FactoryBeanApp;
import javax.swing.JTextArea;

public class InneFrame extends JFrame {

    private DefaultTableModel miniModel;

    private DefaultTableModel maxModel;

    private DefaultTableModel przModel;

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JLabel jLabel2 = null;

    private JScrollPane jScrollPane1 = null;

    private JTable jTable1 = null;

    private JLabel jLabel3 = null;

    private JTextArea jTextArea = null;

    private JScrollPane jScrollPane2 = null;

    private JTable jTable2 = null;

    /**
	 * This is the default constructor
	 */
    public InneFrame() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(398, 208);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel3 = new JLabel();
            jLabel3.setBounds(new Rectangle(241, 45, 142, 18));
            jLabel3.setText("Przekruj przewod�w:");
            jLabel2 = new JLabel();
            jLabel2.setBounds(new Rectangle(120, 45, 92, 18));
            jLabel2.setText("Maksymalne:");
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(15, 45, 74, 18));
            jLabel1.setText("Minimalne:");
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(16, 14, 196, 20));
            jLabel.setText("Pr�dko�ci rozruchowe:");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(jLabel, null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(jLabel2, null);
            jContentPane.add(getJScrollPane1(), null);
            jContentPane.add(jLabel3, null);
            jContentPane.add(getJScrollPane2(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(15, 73, 73, 100));
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        if (jTable == null) {
            miniModel = new DefaultTableModel();
            miniModel.addColumn("Min:");
            jTable = new JTable(miniModel);
            NMinMax min = (NMinMax) FactoryBeanApp.getBean("nMinMax");
            for (Iterator<Integer> i = min.getMini().iterator(); i.hasNext(); ) {
                miniModel.addRow(new Object[] { (int) i.next() });
            }
        }
        return jTable;
    }

    /**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(new Rectangle(120, 73, 75, 100));
            jScrollPane1.setViewportView(getJTable1());
        }
        return jScrollPane1;
    }

    /**
	 * This method initializes jTable1	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable1() {
        if (jTable1 == null) {
            maxModel = new DefaultTableModel();
            maxModel.addColumn("Max:");
            jTable1 = new JTable(maxModel);
            NMinMax min = (NMinMax) FactoryBeanApp.getBean("nMinMax");
            for (Iterator<Integer> i = min.getMax().iterator(); i.hasNext(); ) {
                maxModel.addRow(new Object[] { (int) i.next() });
            }
        }
        return jTable1;
    }

    /**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
        }
        return jTextArea;
    }

    /**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setBounds(new Rectangle(285, 73, 89, 100));
            jScrollPane2.setViewportView(getJTable2());
        }
        return jScrollPane2;
    }

    /**
	 * This method initializes jTable2	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable2() {
        if (jTable2 == null) {
            przModel = new DefaultTableModel();
            przModel.addColumn("SK mm 2");
            jTable2 = new JTable(przModel);
            Sk sk = (Sk) FactoryBeanApp.getBean("sk");
            for (Iterator<Integer> i = sk.getDl().iterator(); i.hasNext(); ) {
                przModel.addRow(new Object[] { (int) i.next() });
            }
        }
        return jTable2;
    }
}
