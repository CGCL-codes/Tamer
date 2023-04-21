package org.dmp.chillout.ccd.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.xml.bind.JAXBException;
import org.dmp.chillout.ccd.util.ExampleFileFilter;

/**
 * <p>Title: Meta Panel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006 CEDEO.net</p>
 * <p>Company: CEDEO.net - http://www.cedeo.net</p>
 * @author Filippo Chiariglione
 * @version 1.0
 */
public class MetaPanel extends JPanel {

    private JLabel jLabelTitle = null;

    private JTextField jTextFieldTitle = null;

    private JLabel jLabelAbstract = null;

    private JLabel jLabelGenre = null;

    private JTextField jTextFieldGenre = null;

    private JPanel jPanelCreator = null;

    private JLabel jLabelFirstName = null;

    private JLabel jLabellastName = null;

    private JTextField jTextFieldFirstName = null;

    private JTextField jTextFieldLastName = null;

    private JTextField jTextFieldYYYY = null;

    private JLabel jLabelMinimumAge = null;

    private JLabel jLabelDateCreation = null;

    private JLabel jLabelCopyright = null;

    private JTextField jTextFieldCopyright = null;

    private JScrollPane jScrollPane1 = null;

    private JTextPane jTextPaneAbstract = null;

    private String[] ratingOptions = { "Rating", "G", "PG", "PG-13", "R", "NC-17" };

    private String[] monthsList = { "Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };

    private String[] dayList = { "Day", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };

    private String[] role = { "Role", "Actor", "Producer", "Director", "Performer", "Composer", "Distributor", "Publisher" };

    private JComboBox jComboBoxMM = null;

    private JComboBox jComboBoxRating = null;

    private JComboBox jComboBoxDD = null;

    private JLabel jLabelRole = null;

    private JComboBox jComboBoxRole = null;

    private JButton jButtonIcon = null;

    private JTextField jTextFieldIcon = null;

    private JLabel jLabelIcon = null;

    /**
	 * This is the default constructor
	 */
    public MetaPanel() {
        super();
        initialize();
    }

    public int getComboBoxIndex(String vectorName, String key) {
        if (vectorName.equalsIgnoreCase("ratingsOptions")) return getIndex(this.ratingOptions, key); else if (vectorName.equalsIgnoreCase("monthsList")) return getIndex(this.monthsList, key); else if (vectorName.equalsIgnoreCase("dayList")) return getIndex(this.dayList, key); else if (vectorName.equalsIgnoreCase("role")) return getIndex(this.role, key); else return -1;
    }

    private int getIndex(String[] items, String key) {
        if (key.equals("")) return 0;
        for (int i = 0; i < items.length; i++) if (items[i].equalsIgnoreCase(key)) return i;
        return -1;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        jLabelIcon = new JLabel();
        jLabelIcon.setBounds(new Rectangle(21, 155, 67, 17));
        jLabelIcon.setText("Thumbnail");
        jLabelCopyright = new JLabel();
        jLabelCopyright.setBounds(new java.awt.Rectangle(318, 102, 67, 17));
        jLabelCopyright.setText("Copyright");
        jLabelDateCreation = new JLabel();
        jLabelDateCreation.setBounds(new java.awt.Rectangle(318, 129, 67, 17));
        jLabelDateCreation.setText("Creation date");
        jLabelMinimumAge = new JLabel();
        jLabelMinimumAge.setBounds(new java.awt.Rectangle(21, 129, 67, 17));
        jLabelMinimumAge.setText("Rating");
        jLabelGenre = new JLabel();
        jLabelGenre.setBounds(new java.awt.Rectangle(21, 102, 67, 17));
        jLabelGenre.setText("Genre");
        jLabelAbstract = new JLabel();
        jLabelAbstract.setText("Abstract");
        jLabelAbstract.setSize(new java.awt.Dimension(67, 17));
        jLabelAbstract.setLocation(new java.awt.Point(22, 50));
        jLabelTitle = new JLabel();
        jLabelTitle.setText("Title");
        jLabelTitle.setSize(new java.awt.Dimension(67, 17));
        jLabelTitle.setLocation(new java.awt.Point(22, 21));
        this.setLayout(null);
        this.setSize(628, 188);
        this.add(jLabelTitle, null);
        this.add(getJTextFieldTitle(), null);
        this.add(jLabelAbstract, null);
        this.add(jLabelGenre, null);
        this.add(getJTextFieldGenre(), null);
        this.add(getJPanelCreator(), null);
        this.add(getJTextFieldYYYY(), null);
        this.add(jLabelMinimumAge, null);
        this.add(jLabelDateCreation, null);
        this.add(jLabelCopyright, null);
        this.add(getJTextFieldCopyright(), null);
        this.add(getJScrollPane(), null);
        this.add(getJComboBoxMM(), null);
        this.add(getJComboBoxRating(), null);
        this.add(getJComboBoxDD(), null);
        this.add(getJButtonIcon(), null);
        this.add(getJTextFieldIcon(), null);
        this.add(jLabelIcon, null);
    }

    public void clearPanel() {
        getJTextFieldTitle().setText("");
        getJTextPaneAbstract().setText("");
        getJTextFieldGenre().setText("");
        getJComboBoxRating().setSelectedIndex(0);
        getJTextFieldFirstName().setText("");
        getJTextFieldLastName().setText("");
        getJComboBoxRole().setSelectedIndex(0);
        getJTextFieldCopyright().setText("");
        getJTextFieldYYYY().setText("YYYY");
        getJComboBoxMM().setSelectedIndex(0);
        getJComboBoxDD().setSelectedIndex(0);
    }

    /**
	 * This method initializes jTextFieldTitle	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldTitle() {
        if (jTextFieldTitle == null) {
            jTextFieldTitle = new JTextField();
            jTextFieldTitle.setLocation(new java.awt.Point(107, 21));
            jTextFieldTitle.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextFieldTitle.setBackground(new Color(255, 255, 190));
            jTextFieldTitle.setSize(new java.awt.Dimension(183, 17));
        }
        return jTextFieldTitle;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldGenre() {
        if (jTextFieldGenre == null) {
            jTextFieldGenre = new JTextField();
            jTextFieldGenre.setBounds(new java.awt.Rectangle(107, 102, 183, 17));
            jTextFieldGenre.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
        }
        return jTextFieldGenre;
    }

    /**
	 * This method initializes jPanelCreator	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelCreator() {
        if (jPanelCreator == null) {
            jLabelRole = new JLabel();
            jLabelRole.setBounds(new java.awt.Rectangle(11, 44, 53, 17));
            jLabelRole.setText("Role");
            jLabellastName = new JLabel();
            jLabellastName.setText("Last Name");
            jLabellastName.setSize(new java.awt.Dimension(53, 17));
            jLabellastName.setLocation(new java.awt.Point(148, 19));
            jLabelFirstName = new JLabel();
            jLabelFirstName.setText("First Name");
            jLabelFirstName.setSize(new java.awt.Dimension(53, 17));
            jLabelFirstName.setLocation(new java.awt.Point(11, 19));
            jPanelCreator = new JPanel();
            jPanelCreator.setLayout(null);
            jPanelCreator.setBounds(new java.awt.Rectangle(319, 21, 289, 70));
            jPanelCreator.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Creator", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanelCreator.add(jLabelFirstName, null);
            jPanelCreator.add(jLabellastName, null);
            jPanelCreator.add(getJTextFieldFirstName(), null);
            jPanelCreator.add(getJTextFieldLastName(), null);
            jPanelCreator.add(jLabelRole, null);
            jPanelCreator.add(getJComboBoxRole(), null);
        }
        return jPanelCreator;
    }

    /**
	 * This method initializes jTextFieldFirstName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldFirstName() {
        if (jTextFieldFirstName == null) {
            jTextFieldFirstName = new JTextField();
            jTextFieldFirstName.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextFieldFirstName.setSize(new java.awt.Dimension(63, 17));
            jTextFieldFirstName.setLocation(new java.awt.Point(75, 19));
        }
        return jTextFieldFirstName;
    }

    /**
	 * This method initializes jTextFieldlastName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldLastName() {
        if (jTextFieldLastName == null) {
            jTextFieldLastName = new JTextField();
            jTextFieldLastName.setPreferredSize(new java.awt.Dimension(11, 20));
            jTextFieldLastName.setSize(new java.awt.Dimension(63, 17));
            jTextFieldLastName.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextFieldLastName.setLocation(new java.awt.Point(213, 19));
        }
        return jTextFieldLastName;
    }

    /**
	 * This method initializes jTextFieldYYYY	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldYYYY() {
        if (jTextFieldYYYY == null) {
            jTextFieldYYYY = new JTextField();
            jTextFieldYYYY.setBounds(new java.awt.Rectangle(424, 129, 49, 17));
            jTextFieldYYYY.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextFieldYYYY.setText("YYYY");
            jTextFieldYYYY.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusGained(java.awt.event.FocusEvent e) {
                    getJTextFieldYYYY().setText("");
                }
            });
        }
        return jTextFieldYYYY;
    }

    /**
	 * This method initializes jTextFieldCopyright	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldCopyright() {
        if (jTextFieldCopyright == null) {
            jTextFieldCopyright = new JTextField();
            jTextFieldCopyright.setLocation(new java.awt.Point(424, 102));
            jTextFieldCopyright.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextFieldCopyright.setSize(new java.awt.Dimension(183, 17));
        }
        return jTextFieldCopyright;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(new java.awt.Rectangle(107, 50, 183, 41));
            jScrollPane1.setViewportView(getJTextPaneAbstract());
        }
        return jScrollPane1;
    }

    /**
	 * This method initializes jTextPaneAbstract	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    public JTextPane getJTextPaneAbstract() {
        if (jTextPaneAbstract == null) {
            jTextPaneAbstract = new JTextPane();
            jTextPaneAbstract.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            jTextPaneAbstract.setSize(new java.awt.Dimension(181, 43));
        }
        return jTextPaneAbstract;
    }

    /**
	 * This method initializes jComboBoxMM	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    public JComboBox getJComboBoxMM() {
        if (jComboBoxMM == null) {
            jComboBoxMM = new JComboBox(monthsList);
            jComboBoxMM.setBounds(new java.awt.Rectangle(489, 129, 54, 17));
        }
        return jComboBoxMM;
    }

    /**
	 * This method initializes jComboBoxRating	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    public JComboBox getJComboBoxRating() {
        if (jComboBoxRating == null) {
            jComboBoxRating = new JComboBox(ratingOptions);
            jComboBoxRating.setBounds(new java.awt.Rectangle(107, 129, 183, 17));
        }
        return jComboBoxRating;
    }

    /**
	 * This method initializes jComboBoxDD	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    public JComboBox getJComboBoxDD() {
        if (jComboBoxDD == null) {
            jComboBoxDD = new JComboBox(dayList);
            jComboBoxDD.setBounds(new java.awt.Rectangle(561, 129, 46, 17));
        }
        return jComboBoxDD;
    }

    /**
	 * This method initializes jComboBoxRole	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    public JComboBox getJComboBoxRole() {
        if (jComboBoxRole == null) {
            jComboBoxRole = new JComboBox(role);
            jComboBoxRole.setBounds(new java.awt.Rectangle(75, 44, 201, 17));
        }
        return jComboBoxRole;
    }

    /**
	 * This method initializes jButtonIcon	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonIcon() {
        if (jButtonIcon == null) {
            jButtonIcon = new JButton();
            jButtonIcon.setBounds(new Rectangle(489, 155, 118, 17));
            jButtonIcon.setText("Browse");
            jButtonIcon.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getJTextFieldIcon().setText(getThumbnailPathName());
                }
            });
        }
        return jButtonIcon;
    }

    private String getThumbnailPathName() {
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("jpg");
        filter.addExtension("png");
        filter.addExtension("gif");
        filter.setDescription("Images");
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(filter);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (JFileChooser.APPROVE_OPTION == jFileChooser.showDialog(null, "OK")) {
            return jFileChooser.getSelectedFile().getPath();
        } else return null;
    }

    /**
	 * This method initializes jTextFieldIcon	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    public JTextField getJTextFieldIcon() {
        if (jTextFieldIcon == null) {
            jTextFieldIcon = new JTextField();
            jTextFieldIcon.setBounds(new Rectangle(107, 155, 368, 17));
        }
        return jTextFieldIcon;
    }
}
