package grammarbrowser.browser.dialog;

import grammarbrowser.browser.JavaVersion;
import grammarbrowser.browser.FileUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * About dialog
 * 
 * @author Bernard Bou
 * 
 */
public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Author
     */
    static final String theAuthor = "Bernard Bou, bsys";

    /**
     * Email
     */
    static final String theEmail = "<bbou@ac-toulouse.fr>";

    /**
     * Copyright notice
     */
    static final String theCopyright = "Copyright (c) 2007-2008";

    /**
     * Constructor
     * 
     * @param thisProduct
     *            product
     * @param thisVersion
     *            string
     */
    public AboutDialog(String thisProduct, String thisVersion) {
        initialize(thisProduct, thisVersion);
    }

    /**
     * Initialize component
     * 
     * @param thisProduct
     *            product
     * @param thisVersion
     *            string
     */
    private void initialize(String thisProduct, String thisVersion) {
        setTitle("Grammar Browser");
        JLabel thisTitleLabel = new JLabel();
        thisTitleLabel.setText("Grammar browser");
        thisTitleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        thisTitleLabel.setOpaque(true);
        thisTitleLabel.setBackground(Color.RED);
        thisTitleLabel.setForeground(Color.WHITE);
        thisTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thisTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel thisDescription = new JLabel();
        thisDescription.setText("a browser for Stanford Parser grammatical relations");
        thisDescription.setFont(new Font("Dialog", 0, 10));
        thisDescription.setHorizontalAlignment(SwingConstants.CENTER);
        thisDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel thisAuthorLabel = new JLabel(theAuthor);
        JLabel thisEmailLabel = new JLabel(theEmail);
        thisEmailLabel.setForeground(Color.BLUE);
        JLabel thisCopyrightLabel = new JLabel(theCopyright);
        JLabel thisVersionLabel = new JLabel(thisVersion);
        JLabel thisStanfordParserLabel = new JLabel("Stanford Parser");
        JLabel thisStanfordParserVersion = new JLabel(FileUtils.getVersion(".*stanford-parser.jar"));
        String theseProps = JavaVersion.getJavaPropsString();
        JTextArea thisJavaInfo = new JTextArea();
        thisJavaInfo.setEditable(false);
        thisJavaInfo.setText(theseProps);
        thisJavaInfo.setCaretPosition(0);
        thisJavaInfo.setLineWrap(false);
        JLabel thisImage = new JLabel();
        thisImage.setIcon(new ImageIcon(AboutDialog.class.getResource("images/logo.png")));
        JButton thisOKButton = new JButton("Ok");
        thisOKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        JPanel thisCommandPanel = new JPanel();
        thisCommandPanel.setLayout(new FlowLayout());
        thisCommandPanel.add(thisOKButton);
        JPanel thisPanel = new JPanel();
        thisPanel.setLayout(new GridBagLayout());
        thisPanel.add(thisTitleLabel, new GridBagConstraints(0, 0, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 0, 10), 0, 0));
        thisPanel.add(thisVersionLabel, new GridBagConstraints(0, 10, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisImage, new GridBagConstraints(0, 11, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        thisPanel.add(thisAuthorLabel, new GridBagConstraints(0, 21, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisEmailLabel, new GridBagConstraints(0, 22, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisCopyrightLabel, new GridBagConstraints(0, 23, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisStanfordParserLabel, new GridBagConstraints(0, 30, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 0, 10), 0, 0));
        thisPanel.add(thisStanfordParserVersion, new GridBagConstraints(0, 31, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 20, 10), 0, 0));
        thisPanel.add(new JScrollPane(thisJavaInfo), new GridBagConstraints(0, 40, 1, 1, 1., 1., GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        thisPanel.add(thisCommandPanel, new GridBagConstraints(0, 50, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
        setContentPane(thisPanel);
    }

    @Override
    public void setVisible(boolean thisFlag) {
        if (thisFlag) {
            pack();
            Utils.center(this);
        }
        super.setVisible(thisFlag);
    }
}
