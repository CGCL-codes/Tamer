package edu.princeton.wordnet.browser.component;

import java.awt.Color;
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
import edu.princeton.wordnet.browser.Browser;
import edu.princeton.wordnet.browser.JavaVersion;

/**
 * About dialog
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
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
    static final String theCopyright = "Copyright (c) 2010";

    /**
	 * Constructor
	 * 
	 * @param thisProduct
	 *            product
	 * @param thisVersion
	 *            string
	 */
    public AboutDialog(final String thisProduct, final String thisVersion) {
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
    private void initialize(final String thisProduct, final String thisVersion) {
        setTitle(Messages.getString("AboutDialog.0"));
        final JLabel thisTitleLabel = new JLabel(thisProduct);
        thisTitleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        final JLabel thisAuthorLabel = new JLabel(AboutDialog.theAuthor);
        final JLabel thisEmailLabel = new JLabel(AboutDialog.theEmail);
        thisEmailLabel.setForeground(Color.BLUE);
        final JLabel thisCopyrightLabel = new JLabel(AboutDialog.theCopyright);
        final JLabel thisVersionLabel = new JLabel(thisVersion);
        final String theseProps = JavaVersion.getJavaPropsString() + "memory.heap " + Browser.mem();
        final JTextArea thisJavaInfo = new JTextArea();
        thisJavaInfo.setEditable(false);
        thisJavaInfo.setText(theseProps);
        thisJavaInfo.setCaretPosition(0);
        thisJavaInfo.setLineWrap(false);
        final JLabel thisImage = new JLabel();
        thisImage.setIcon(new ImageIcon(AboutDialog.class.getResource("images/logo.png")));
        final JButton thisOKButton = new JButton(Messages.getString("AboutDialog.1"));
        thisOKButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });
        final JPanel thisCommandPanel = new JPanel();
        thisCommandPanel.setLayout(new FlowLayout());
        thisCommandPanel.add(thisOKButton);
        final JPanel thisPanel = new JPanel();
        thisPanel.setLayout(new GridBagLayout());
        thisPanel.add(thisTitleLabel, new GridBagConstraints(0, 0, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 10, 0, 10), 0, 0));
        thisPanel.add(thisVersionLabel, new GridBagConstraints(0, 10, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisImage, new GridBagConstraints(0, 11, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        thisPanel.add(thisAuthorLabel, new GridBagConstraints(0, 21, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisEmailLabel, new GridBagConstraints(0, 22, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(thisCopyrightLabel, new GridBagConstraints(0, 23, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        thisPanel.add(new JScrollPane(thisJavaInfo), new GridBagConstraints(0, 30, 1, 1, 1., 1., GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        thisPanel.add(thisCommandPanel, new GridBagConstraints(0, 40, 1, 1, 0., 0., GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
        setContentPane(thisPanel);
    }

    @Override
    public void setVisible(final boolean thisFlag) {
        if (thisFlag) {
            pack();
            Utils.center(this);
        }
        super.setVisible(thisFlag);
    }
}
