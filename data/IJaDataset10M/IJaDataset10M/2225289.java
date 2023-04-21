package com.isaac4.lgpl.witl;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 * @author isaac
 * 
 * SetupFrame is a Swing GUI for configuring witl DEPRECATED
 */
public class SetupFrame extends JFrame {

    private JToolBar setupToolBar = null;

    private JLabel SmtpServerAddressLabel = null;

    private JTextField SmtpServerAddressTextField = null;

    private JLabel loginLabel = null;

    private JTextField loginTextField = null;

    private JLabel fromAddressTextLabel = null;

    private JTextField fromAddressTextField = null;

    private JTextField toAddressTextField = null;

    private JLabel toAddressLabel = null;

    private JLabel frequencyLabel = null;

    private JSlider frequencySlider = null;

    /**
	 * This is the default constructor
	 */
    public SetupFrame() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setContentPane(getSetupToolBar());
        this.setSize(300, 245);
        this.setTitle("JFrame");
    }

    /**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
    private JToolBar getSetupToolBar() {
        if (setupToolBar == null) {
            frequencyLabel = new JLabel();
            toAddressLabel = new JLabel();
            fromAddressTextLabel = new JLabel();
            loginLabel = new JLabel();
            SmtpServerAddressLabel = new JLabel();
            setupToolBar = new JToolBar();
            SmtpServerAddressLabel.setText("SMTP Server Address");
            loginLabel.setText("Login");
            setupToolBar.setOrientation(javax.swing.JToolBar.VERTICAL);
            fromAddressTextLabel.setText("From Address");
            toAddressLabel.setText("To Address");
            frequencyLabel.setText("Frequency");
            setupToolBar.add(SmtpServerAddressLabel);
            setupToolBar.add(getSmtpServerAddressTextField());
            setupToolBar.add(loginLabel);
            setupToolBar.add(getLoginTextField());
            setupToolBar.add(fromAddressTextLabel);
            setupToolBar.add(getFromAddressTextField());
            setupToolBar.add(toAddressLabel);
            setupToolBar.add(getToAddressTextField());
            setupToolBar.add(frequencyLabel);
            setupToolBar.add(getFrequencySlider());
        }
        return setupToolBar;
    }

    /**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getSmtpServerAddressTextField() {
        if (SmtpServerAddressTextField == null) {
            SmtpServerAddressTextField = new JTextField();
        }
        return SmtpServerAddressTextField;
    }

    /**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getLoginTextField() {
        if (loginTextField == null) {
            loginTextField = new JTextField();
        }
        return loginTextField;
    }

    /**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getFromAddressTextField() {
        if (fromAddressTextField == null) {
            fromAddressTextField = new JTextField();
        }
        return fromAddressTextField;
    }

    /**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getToAddressTextField() {
        if (toAddressTextField == null) {
            toAddressTextField = new JTextField();
        }
        return toAddressTextField;
    }

    /**
	 * This method initializes jSlider
	 * 
	 * @return javax.swing.JSlider
	 */
    private JSlider getFrequencySlider() {
        if (frequencySlider == null) {
            frequencySlider = new JSlider();
        }
        return frequencySlider;
    }
}
