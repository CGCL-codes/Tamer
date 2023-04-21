package net.sourceforge.swinguiloc.util;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sourceforge.swinguiloc.beans.LButton;
import net.sourceforge.swinguiloc.beans.LDialog;
import net.sourceforge.swinguiloc.beans.LLabel;
import net.sourceforge.swinguiloc.beans.LTitledBorder;
import net.sourceforge.swinguiloc.trans.LanguageSwitch;

public class LocaleDisplay extends LDialog {

    private static final long serialVersionUID = 0L;

    private Locale loc;

    private JPanel jContentPane = null;

    private LButton cmdClose = null;

    private JPanel datePanel = null;

    private LLabel labelDateShort = null;

    private LLabel labelDateMed = null;

    private LLabel labelDateLong = null;

    private LLabel labelDateFull = null;

    private JPanel timePanel = null;

    private LLabel labelTimeShort = null;

    private LLabel labelTimeMed = null;

    private LLabel labelTimeLong = null;

    private LLabel labelTimeFull = null;

    private JTextField inDateShort = null;

    private JTextField inDateMed = null;

    private JTextField inDateLong = null;

    private JTextField inDateFull = null;

    private JTextField inTimeShort = null;

    private JTextField inTimeMed = null;

    private JTextField inTimeLong = null;

    private JTextField inTimeFull = null;

    private JPanel infoPanel = null;

    private LLabel labelLang = null;

    private LLabel labelCountry = null;

    private LLabel labelName = null;

    private LLabel labelVariant = null;

    private JTextField inLang = null;

    private JTextField inCountry = null;

    private JTextField inName = null;

    private JTextField inVariant = null;

    private JPanel numPanel = null;

    private LLabel labelNumFormat = null;

    private LLabel labelPercentFormat = null;

    private LLabel labelCurrFormat = null;

    private JTextField inNumFormat = null;

    private JTextField inPercentFormat = null;

    private JTextField inCurrFormat = null;

    public LocaleDisplay() throws HeadlessException {
        super();
        initialize();
    }

    public LocaleDisplay(Frame arg0, Locale loc) throws HeadlessException {
        super(arg0);
        this.loc = loc;
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new java.awt.Dimension(639, 343));
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setCaptionTag("LocaleDisplayer");
        this.setTitle("Locale Displayer");
        this.setContentPane(getJContentPane());
        if (this.getOwner() instanceof LanguageSwitch) this.setTranslator(((LanguageSwitch) this.getOwner()).getTranslator());
        inLang.setText(loc.getDisplayLanguage());
        inCountry.setText(loc.getDisplayCountry());
        inName.setText(loc.getDisplayName());
        inVariant.setText(loc.getDisplayVariant());
        NumberFormat nf = NumberFormat.getInstance(loc);
        inNumFormat.setText(nf.format(123456789.1234));
        NumberFormat ci = NumberFormat.getCurrencyInstance(loc);
        inCurrFormat.setText(ci.format(1234.56));
        NumberFormat pi = NumberFormat.getPercentInstance(loc);
        inPercentFormat.setText(pi.format(12.3));
        Calendar cal = Calendar.getInstance();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        inDateShort.setText(df.format(cal.getTime()));
        df = DateFormat.getDateInstance(DateFormat.MEDIUM, loc);
        inDateMed.setText(df.format(cal.getTime()));
        df = DateFormat.getDateInstance(DateFormat.LONG, loc);
        inDateLong.setText(df.format(cal.getTime()));
        df = DateFormat.getDateInstance(DateFormat.FULL, loc);
        inDateFull.setText(df.format(cal.getTime()));
        DateFormat tf;
        tf = DateFormat.getTimeInstance(DateFormat.SHORT, loc);
        inTimeShort.setText(tf.format(cal.getTime()));
        tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, loc);
        inTimeMed.setText(tf.format(cal.getTime()));
        tf = DateFormat.getTimeInstance(DateFormat.LONG, loc);
        inTimeLong.setText(tf.format(cal.getTime()));
        tf = DateFormat.getTimeInstance(DateFormat.FULL, loc);
        inTimeFull.setText(tf.format(cal.getTime()));
        setLocation(Math.abs((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - getBounds().width / 2), Math.abs((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - getBounds().height / 2));
    }

    /**
     * This method initializes jContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getCmdClose(), null);
            jContentPane.add(getDatePanel(), null);
            jContentPane.add(getTimePanel(), null);
            jContentPane.add(getInfoPanel(), null);
            jContentPane.add(getNumPanel(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes cmdClose	
     * 	
     * @return net.sourceforge.swinguiloc.beans.LButton	
     */
    private LButton getCmdClose() {
        if (cmdClose == null) {
            cmdClose = new LButton();
            cmdClose.setLocation(new java.awt.Point(266, 286));
            cmdClose.setCaptionTag("cmdClose");
            cmdClose.setSize(new java.awt.Dimension(100, 25));
            cmdClose.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    LocaleDisplay.this.dispose();
                }
            });
        }
        return cmdClose;
    }

    /**
     * This method initializes datePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getDatePanel() {
        if (datePanel == null) {
            labelDateFull = new LLabel();
            labelDateFull.setBounds(new java.awt.Rectangle(8, 95, 83, 25));
            labelDateFull.setCaptionTag("labelDateFull");
            labelDateFull.setText("JLabel");
            labelDateLong = new LLabel();
            labelDateLong.setBounds(new java.awt.Rectangle(8, 69, 83, 25));
            labelDateLong.setCaptionTag("labelDateLong");
            labelDateLong.setText("JLabel");
            labelDateMed = new LLabel();
            labelDateMed.setBounds(new java.awt.Rectangle(8, 43, 83, 25));
            labelDateMed.setCaptionTag("labelDateMed");
            labelDateMed.setText("JLabel");
            labelDateShort = new LLabel();
            labelDateShort.setText("JLabel");
            labelDateShort.setSize(new java.awt.Dimension(83, 25));
            labelDateShort.setCaptionTag("labelDateShort");
            labelDateShort.setLocation(new java.awt.Point(8, 17));
            LTitledBorder lTitledBorder = new LTitledBorder("");
            lTitledBorder.setCaptionTag("labelDate");
            addTranslatable(lTitledBorder);
            datePanel = new JPanel();
            datePanel.setLayout(null);
            datePanel.setBounds(new java.awt.Rectangle(19, 149, 286, 130));
            datePanel.setBorder(lTitledBorder);
            datePanel.add(labelDateShort, null);
            datePanel.add(labelDateMed, null);
            datePanel.add(labelDateLong, null);
            datePanel.add(labelDateFull, null);
            datePanel.add(getInDateShort(), null);
            datePanel.add(getInDateMed(), null);
            datePanel.add(getInDateLong(), null);
            datePanel.add(getInDateFull(), null);
        }
        return datePanel;
    }

    /**
     * This method initializes timePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getTimePanel() {
        if (timePanel == null) {
            labelTimeFull = new LLabel();
            labelTimeFull.setBounds(new java.awt.Rectangle(8, 95, 83, 25));
            labelTimeFull.setCaptionTag("labelTimeFull");
            labelTimeFull.setText("JLabel");
            labelTimeLong = new LLabel();
            labelTimeLong.setBounds(new java.awt.Rectangle(8, 69, 83, 25));
            labelTimeLong.setCaptionTag("labelTimeLong");
            labelTimeLong.setText("JLabel");
            labelTimeMed = new LLabel();
            labelTimeMed.setBounds(new java.awt.Rectangle(8, 43, 83, 25));
            labelTimeMed.setCaptionTag("labelTimeMed");
            labelTimeMed.setText("JLabel");
            labelTimeShort = new LLabel();
            labelTimeShort.setText("JLabel");
            labelTimeShort.setBounds(new java.awt.Rectangle(8, 17, 83, 25));
            labelTimeShort.setCaptionTag("labelTimeShort");
            LTitledBorder lTitledBorder1 = new LTitledBorder("");
            lTitledBorder1.setCaptionTag("labelTime");
            addTranslatable(lTitledBorder1);
            timePanel = new JPanel();
            timePanel.setLayout(null);
            timePanel.setBounds(new java.awt.Rectangle(324, 13, 286, 130));
            timePanel.setBorder(lTitledBorder1);
            timePanel.add(labelTimeShort, null);
            timePanel.add(labelTimeMed, null);
            timePanel.add(labelTimeLong, null);
            timePanel.add(labelTimeFull, null);
            timePanel.add(getInTimeShort(), null);
            timePanel.add(getInTimeMed(), null);
            timePanel.add(getInTimeLong(), null);
            timePanel.add(getInTimeFull(), null);
        }
        return timePanel;
    }

    /**
     * This method initializes inDateShort	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInDateShort() {
        if (inDateShort == null) {
            inDateShort = new JTextField();
            inDateShort.setBounds(new java.awt.Rectangle(92, 17, 181, 25));
            inDateShort.setEditable(true);
        }
        return inDateShort;
    }

    /**
     * This method initializes inDateMed	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInDateMed() {
        if (inDateMed == null) {
            inDateMed = new JTextField();
            inDateMed.setBounds(new java.awt.Rectangle(92, 43, 181, 25));
            inDateMed.setEditable(true);
        }
        return inDateMed;
    }

    /**
     * This method initializes inDateLong	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInDateLong() {
        if (inDateLong == null) {
            inDateLong = new JTextField();
            inDateLong.setBounds(new java.awt.Rectangle(92, 69, 181, 25));
            inDateLong.setEditable(true);
        }
        return inDateLong;
    }

    /**
     * This method initializes inDateFull	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInDateFull() {
        if (inDateFull == null) {
            inDateFull = new JTextField();
            inDateFull.setBounds(new java.awt.Rectangle(92, 95, 181, 25));
            inDateFull.setEditable(true);
        }
        return inDateFull;
    }

    /**
     * This method initializes inTimeShort	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInTimeShort() {
        if (inTimeShort == null) {
            inTimeShort = new JTextField();
            inTimeShort.setBounds(new java.awt.Rectangle(93, 16, 180, 25));
            inTimeShort.setEditable(true);
        }
        return inTimeShort;
    }

    /**
     * This method initializes inTimeMed	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInTimeMed() {
        if (inTimeMed == null) {
            inTimeMed = new JTextField();
            inTimeMed.setBounds(new java.awt.Rectangle(93, 43, 180, 25));
            inTimeMed.setEditable(true);
        }
        return inTimeMed;
    }

    /**
     * This method initializes inTimeLong	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInTimeLong() {
        if (inTimeLong == null) {
            inTimeLong = new JTextField();
            inTimeLong.setBounds(new java.awt.Rectangle(93, 69, 180, 25));
            inTimeLong.setEditable(true);
        }
        return inTimeLong;
    }

    /**
     * This method initializes inTimeFull	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInTimeFull() {
        if (inTimeFull == null) {
            inTimeFull = new JTextField();
            inTimeFull.setBounds(new java.awt.Rectangle(93, 95, 180, 25));
            inTimeFull.setEditable(true);
        }
        return inTimeFull;
    }

    /**
     * This method initializes infoPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getInfoPanel() {
        if (infoPanel == null) {
            labelVariant = new LLabel();
            labelVariant.setBounds(new java.awt.Rectangle(8, 95, 83, 25));
            labelVariant.setCaptionTag("labelVariant");
            labelVariant.setText("JLabel");
            labelName = new LLabel();
            labelName.setBounds(new java.awt.Rectangle(8, 69, 83, 25));
            labelName.setCaptionTag("labelName");
            labelName.setText("JLabel");
            labelCountry = new LLabel();
            labelCountry.setBounds(new java.awt.Rectangle(8, 43, 83, 25));
            labelCountry.setCaptionTag("labelCountry");
            labelCountry.setText("JLabel");
            labelLang = new LLabel();
            labelLang.setBounds(new java.awt.Rectangle(8, 17, 83, 25));
            labelLang.setCaptionTag("labelLang");
            labelLang.setText("JLabel");
            LTitledBorder lTitledBorder2 = new LTitledBorder("");
            lTitledBorder2.setCaptionTag("labelInfo");
            addTranslatable(lTitledBorder2);
            infoPanel = new JPanel();
            infoPanel.setLayout(null);
            infoPanel.setBounds(new java.awt.Rectangle(19, 13, 286, 130));
            infoPanel.setBorder(lTitledBorder2);
            infoPanel.add(labelLang, null);
            infoPanel.add(labelCountry, null);
            infoPanel.add(labelName, null);
            infoPanel.add(labelVariant, null);
            infoPanel.add(getInLang(), null);
            infoPanel.add(getInCountry(), null);
            infoPanel.add(getInName(), null);
            infoPanel.add(getInVariant(), null);
        }
        return infoPanel;
    }

    /**
     * This method initializes inLang	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInLang() {
        if (inLang == null) {
            inLang = new JTextField();
            inLang.setBounds(new java.awt.Rectangle(92, 17, 181, 25));
        }
        return inLang;
    }

    /**
     * This method initializes inCountry	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInCountry() {
        if (inCountry == null) {
            inCountry = new JTextField();
            inCountry.setBounds(new java.awt.Rectangle(92, 43, 181, 25));
            inCountry.setEditable(true);
        }
        return inCountry;
    }

    /**
     * This method initializes inName	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInName() {
        if (inName == null) {
            inName = new JTextField();
            inName.setBounds(new java.awt.Rectangle(92, 69, 181, 25));
            inName.setEditable(true);
        }
        return inName;
    }

    /**
     * This method initializes inVariant	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInVariant() {
        if (inVariant == null) {
            inVariant = new JTextField();
            inVariant.setBounds(new java.awt.Rectangle(92, 95, 181, 25));
            inVariant.setEditable(true);
        }
        return inVariant;
    }

    /**
     * This method initializes panelNum	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getNumPanel() {
        if (numPanel == null) {
            labelCurrFormat = new LLabel();
            labelCurrFormat.setBounds(new java.awt.Rectangle(8, 69, 126, 25));
            labelCurrFormat.setCaptionTag("labelCurrFormat");
            labelCurrFormat.setText("JLabel");
            labelPercentFormat = new LLabel();
            labelPercentFormat.setBounds(new java.awt.Rectangle(8, 43, 126, 25));
            labelPercentFormat.setCaptionTag("labelPercentFormat");
            labelPercentFormat.setText("JLabel");
            labelNumFormat = new LLabel();
            labelNumFormat.setBounds(new java.awt.Rectangle(8, 17, 126, 25));
            labelNumFormat.setCaptionTag("labelNumFormat");
            labelNumFormat.setText("JLabel");
            LTitledBorder lTitledBorder3 = new LTitledBorder("");
            lTitledBorder3.setCaptionTag("labelNum");
            addTranslatable(lTitledBorder3);
            numPanel = new JPanel();
            numPanel.setLayout(null);
            numPanel.setBounds(new java.awt.Rectangle(324, 149, 286, 107));
            numPanel.setBorder(lTitledBorder3);
            numPanel.add(labelNumFormat, null);
            numPanel.add(labelPercentFormat, null);
            numPanel.add(labelCurrFormat, null);
            numPanel.add(getInNumFormat(), null);
            numPanel.add(getInPercentFormat(), null);
            numPanel.add(getInCurrFormat(), null);
        }
        return numPanel;
    }

    /**
     * This method initializes inNumFormat	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInNumFormat() {
        if (inNumFormat == null) {
            inNumFormat = new JTextField();
            inNumFormat.setBounds(new java.awt.Rectangle(136, 17, 137, 25));
        }
        return inNumFormat;
    }

    /**
     * This method initializes inPercentFormat	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInPercentFormat() {
        if (inPercentFormat == null) {
            inPercentFormat = new JTextField();
            inPercentFormat.setBounds(new java.awt.Rectangle(136, 43, 137, 25));
        }
        return inPercentFormat;
    }

    /**
     * This method initializes inCurrFormat	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInCurrFormat() {
        if (inCurrFormat == null) {
            inCurrFormat = new JTextField();
            inCurrFormat.setBounds(new java.awt.Rectangle(136, 69, 137, 25));
        }
        return inCurrFormat;
    }
}
