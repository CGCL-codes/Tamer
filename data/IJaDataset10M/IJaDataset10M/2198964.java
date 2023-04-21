package com.rb.lottery.analysis.client.UI.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import com.rb.lottery.analysis.common.FilePathConstants;
import com.rb.lottery.analysis.common.SystemConstants;

/**
 * @类功能说明: 
 * @类修改者:   
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-11-13 下午05:08:36
 * @版本:       1.0.0
 */
public class LotteryFilterAdvancePanel extends JPanel {

    /**
	 * @Fields serialVersionUID: TODO
	 */
    private static final long serialVersionUID = 8549353254251996307L;

    private JButton advancefilterAll;

    private JButton advanceallowerrBtn;

    private JTabbedPane filterAdvanceTab;

    private JPanel filterAdvanceA;

    private JCheckBox advanceA1;

    private JCheckBox advanceA2;

    private JCheckBox advanceA3;

    private JCheckBox advanceA4;

    private JCheckBox advanceA5;

    private JCheckBox advanceA6;

    private JCheckBox advanceA7;

    private JCheckBox advanceA8;

    private JCheckBox advanceA9;

    private JCheckBox advanceA10;

    private JCheckBox advanceA11;

    private JCheckBox advanceA12;

    private JCheckBox advanceA13;

    private JCheckBox advanceA14;

    private JCheckBox advanceA15;

    private JCheckBox advanceA16;

    private JCheckBox advanceA17;

    private JCheckBox advanceA18;

    private JTextField advanceAText1;

    private JTextField advanceAText2;

    private JTextField advanceAText3;

    private JTextField advanceAText4;

    private JTextField advanceAText5;

    private JTextField advanceAText6;

    private JTextField advanceAText7;

    private JTextField advanceAText8;

    private JTextField advanceAText9;

    private JTextField advanceAText10;

    private JTextField advanceAText11;

    private JTextField advanceAText12;

    private JTextField advanceAText13;

    private JTextField advanceAText14;

    private JTextField advanceAText15;

    private JTextField advanceAText16;

    private JTextField advanceAText17;

    private JTextField advanceAText18;

    private JButton advanceABtn1;

    private JButton advanceABtn2;

    private JButton advanceABtn3;

    private JButton advanceABtn4;

    private JButton advanceABtn5;

    private JButton advanceABtn6;

    private JButton advanceABtn7;

    private JButton advanceABtn8;

    private JButton advanceABtn9;

    private JButton advanceABtn10;

    private JButton advanceABtn11;

    private JButton advanceABtn12;

    private JButton advanceABtn13;

    private JButton advanceABtn14;

    private JButton advanceABtn15;

    private JButton advanceABtn16;

    private JButton advanceABtn17;

    private JButton advanceABtn18;

    private JCheckBox advanceAerr1;

    private JCheckBox advanceAerr2;

    private JCheckBox advanceAerr3;

    private JCheckBox advanceAerr4;

    private JCheckBox advanceAerr5;

    private JCheckBox advanceAerr6;

    private JCheckBox advanceAerr7;

    private JCheckBox advanceAerr8;

    private JCheckBox advanceAerr9;

    private JCheckBox advanceAerr10;

    private JCheckBox advanceAerr11;

    private JCheckBox advanceAerr12;

    private JCheckBox advanceAerr13;

    private JCheckBox advanceAerr14;

    private JCheckBox advanceAerr15;

    private JCheckBox advanceAerr16;

    private JCheckBox advanceAerr17;

    private JCheckBox advanceAerr18;

    private JPanel filterAdvanceB;

    private JCheckBox advanceB1;

    private JCheckBox advanceB2;

    private JCheckBox advanceB3;

    private JCheckBox advanceB4;

    private JCheckBox advanceB5;

    private JCheckBox advanceB6;

    private JCheckBox advanceB7;

    private JCheckBox advanceB8;

    private JCheckBox advanceB9;

    private JCheckBox advanceB10;

    private JCheckBox advanceB11;

    private JCheckBox advanceB12;

    private JCheckBox advanceB13;

    private JCheckBox advanceB14;

    private JTextField advanceBText1;

    private JTextField advanceBText2;

    private JTextField advanceBText3;

    private JTextField advanceBText4;

    private JTextField advanceBText5;

    private JTextField advanceBText6;

    private JTextField advanceBText7;

    private JTextField advanceBText8;

    private JTextField advanceBText9;

    private JTextField advanceBText10;

    private JTextField advanceBText11;

    private JTextField advanceBText12;

    private JTextField advanceBText13;

    private JTextField advanceBText14;

    private JButton advanceBBtn1;

    private JButton advanceBBtn2;

    private JButton advanceBBtn3;

    private JButton advanceBBtn4;

    private JButton advanceBBtn5;

    private JButton advanceBBtn6;

    private JButton advanceBBtn7;

    private JButton advanceBBtn8;

    private JButton advanceBBtn9;

    private JButton advanceBBtn10;

    private JButton advanceBBtn11;

    private JButton advanceBBtn12;

    private JButton advanceBBtn13;

    private JButton advanceBBtn14;

    private JCheckBox advanceBerr1;

    private JCheckBox advanceBerr2;

    private JCheckBox advanceBerr3;

    private JCheckBox advanceBerr4;

    private JCheckBox advanceBerr5;

    private JCheckBox advanceBerr6;

    private JCheckBox advanceBerr7;

    private JCheckBox advanceBerr8;

    private JCheckBox advanceBerr9;

    private JCheckBox advanceBerr10;

    private JCheckBox advanceBerr11;

    private JCheckBox advanceBerr12;

    private JCheckBox advanceBerr13;

    private JCheckBox advanceBerr14;

    public LotteryFilterAdvancePanel() {
        super();
        init();
    }

    public LotteryFilterAdvancePanel(LayoutManager layout) {
        super(layout);
        init();
    }

    /**
	 * @方法说明: 
	 * @参数:     
	 * @return    void
	 * @throws
	 */
    private void init() {
        this.setBorder(new EtchedBorder());
        this.setPreferredSize(new java.awt.Dimension(300, 420));
        advancefilterAll = new JButton(SystemConstants.FILTER_ALL);
        advancefilterAll.setPreferredSize(new java.awt.Dimension(40, 20));
        advancefilterAll.setBackground(Color.WHITE);
        advancefilterAll.setForeground(Color.BLUE);
        advancefilterAll.setBorder(null);
        this.add(advancefilterAll);
        JLabel keep = new JLabel(SystemConstants.EMPTY_STRING);
        keep.setPreferredSize(new java.awt.Dimension(185, 20));
        this.add(keep);
        advanceallowerrBtn = new JButton(SystemConstants.ALLOW_ERROR);
        advanceallowerrBtn.setPreferredSize(new java.awt.Dimension(40, 20));
        advanceallowerrBtn.setBackground(Color.WHITE);
        advanceallowerrBtn.setForeground(Color.BLUE);
        advanceallowerrBtn.setBorder(null);
        this.add(advanceallowerrBtn);
        initFilterAdvanceTab();
        this.add(filterAdvanceTab);
    }

    /**
	 * @方法说明:
	 * @参数:
	 * @return void
	 * @throws
	 */
    private void initFilterAdvanceTab() {
        filterAdvanceTab = new JTabbedPane(JTabbedPane.LEFT);
        filterAdvanceTab.setBorder(new EtchedBorder());
        filterAdvanceTab.setPreferredSize(new java.awt.Dimension(300, 380));
        initFilterAdvanceAPane();
        filterAdvanceTab.add(filterAdvanceA);
        filterAdvanceTab.setTitleAt(0, SystemConstants.A);
        initFilterAdvanceBPane();
        filterAdvanceTab.add(filterAdvanceB);
        filterAdvanceTab.setTitleAt(1, SystemConstants.B);
    }

    /**
	 * @方法说明:
	 * @参数:
	 * @return void
	 * @throws
	 */
    private void initFilterAdvanceBPane() {
        filterAdvanceB = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterAdvanceB.setPreferredSize(new java.awt.Dimension(300, 380));
        advanceB1 = new JCheckBox(SystemConstants.FILTER_ADVANCEB1);
        advanceB1.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB1);
        advanceBText1 = new JTextField();
        advanceBText1.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText1.setEditable(false);
        advanceBText1.setEnabled(false);
        filterAdvanceB.add(advanceBText1);
        advanceBBtn1 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn1.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn1);
        advanceBerr1 = new JCheckBox();
        advanceBerr1.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr1.setEnabled(false);
        filterAdvanceB.add(advanceBerr1);
        advanceB2 = new JCheckBox(SystemConstants.FILTER_ADVANCEB2);
        advanceB2.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB2);
        advanceBText2 = new JTextField();
        advanceBText2.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText2.setEditable(false);
        advanceBText2.setEnabled(false);
        filterAdvanceB.add(advanceBText2);
        advanceBBtn2 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn2.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn2);
        advanceBerr2 = new JCheckBox();
        advanceBerr2.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr2.setEnabled(false);
        filterAdvanceB.add(advanceBerr2);
        advanceB3 = new JCheckBox(SystemConstants.FILTER_ADVANCEB3);
        advanceB3.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB3);
        advanceBText3 = new JTextField();
        advanceBText3.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText3.setEditable(false);
        advanceBText3.setEnabled(false);
        filterAdvanceB.add(advanceBText3);
        advanceBBtn3 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn3.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn3);
        advanceBerr3 = new JCheckBox();
        advanceBerr3.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr3.setEnabled(false);
        filterAdvanceB.add(advanceBerr3);
        advanceB4 = new JCheckBox(SystemConstants.FILTER_ADVANCEB4);
        advanceB4.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB4);
        advanceBText4 = new JTextField();
        advanceBText4.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText4.setEditable(false);
        advanceBText4.setEnabled(false);
        filterAdvanceB.add(advanceBText4);
        advanceBBtn4 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn4.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn4);
        advanceBerr4 = new JCheckBox();
        advanceBerr4.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr4.setEnabled(false);
        filterAdvanceB.add(advanceBerr4);
        advanceB5 = new JCheckBox(SystemConstants.FILTER_ADVANCEB5);
        advanceB5.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB5);
        advanceBText5 = new JTextField();
        advanceBText5.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText5.setEditable(false);
        advanceBText5.setEnabled(false);
        filterAdvanceB.add(advanceBText5);
        advanceBBtn5 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn5.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn5);
        advanceBerr5 = new JCheckBox();
        advanceBerr5.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr5.setEnabled(false);
        filterAdvanceB.add(advanceBerr5);
        advanceB6 = new JCheckBox(SystemConstants.FILTER_ADVANCEB6);
        advanceB6.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB6);
        advanceBText6 = new JTextField();
        advanceBText6.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText6.setEditable(false);
        advanceBText6.setEnabled(false);
        filterAdvanceB.add(advanceBText6);
        advanceBBtn6 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn6.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn6);
        advanceBerr6 = new JCheckBox();
        advanceBerr6.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr6.setEnabled(false);
        filterAdvanceB.add(advanceBerr6);
        advanceB7 = new JCheckBox(SystemConstants.FILTER_ADVANCEB7);
        advanceB7.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB7);
        advanceBText7 = new JTextField();
        advanceBText7.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText7.setEditable(false);
        advanceBText7.setEnabled(false);
        filterAdvanceB.add(advanceBText7);
        advanceBBtn7 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn7.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn7);
        advanceBerr7 = new JCheckBox();
        advanceBerr7.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr7.setEnabled(false);
        filterAdvanceB.add(advanceBerr7);
        advanceB8 = new JCheckBox(SystemConstants.FILTER_ADVANCEB8);
        advanceB8.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB8);
        advanceBText8 = new JTextField();
        advanceBText8.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText8.setEditable(false);
        advanceBText8.setEnabled(false);
        filterAdvanceB.add(advanceBText8);
        advanceBBtn8 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn8.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn8);
        advanceBerr8 = new JCheckBox();
        advanceBerr8.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr8.setEnabled(false);
        filterAdvanceB.add(advanceBerr8);
        advanceB9 = new JCheckBox(SystemConstants.FILTER_ADVANCEB9);
        advanceB9.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB9);
        advanceBText9 = new JTextField();
        advanceBText9.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText9.setEditable(false);
        advanceBText9.setEnabled(false);
        filterAdvanceB.add(advanceBText9);
        advanceBBtn9 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn9.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn9);
        advanceBerr9 = new JCheckBox();
        advanceBerr9.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr9.setEnabled(false);
        filterAdvanceB.add(advanceBerr9);
        advanceB10 = new JCheckBox(SystemConstants.FILTER_ADVANCEB10);
        advanceB10.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB10);
        advanceBText10 = new JTextField();
        advanceBText10.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText10.setEditable(false);
        advanceBText10.setEnabled(false);
        filterAdvanceB.add(advanceBText10);
        advanceBBtn10 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn10.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn10);
        advanceBerr10 = new JCheckBox();
        advanceBerr10.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr10.setEnabled(false);
        filterAdvanceB.add(advanceBerr10);
        advanceB11 = new JCheckBox(SystemConstants.FILTER_ADVANCEB11);
        advanceB11.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB11);
        advanceBText11 = new JTextField();
        advanceBText11.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText11.setEditable(false);
        advanceBText11.setEnabled(false);
        filterAdvanceB.add(advanceBText11);
        advanceBBtn11 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn11.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn11);
        advanceBerr11 = new JCheckBox();
        advanceBerr11.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr11.setEnabled(false);
        filterAdvanceB.add(advanceBerr11);
        advanceB12 = new JCheckBox(SystemConstants.FILTER_ADVANCEB12);
        advanceB12.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB12);
        advanceBText12 = new JTextField();
        advanceBText12.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText12.setEditable(false);
        advanceBText12.setEnabled(false);
        filterAdvanceB.add(advanceBText12);
        advanceBBtn12 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn12.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn12);
        advanceBerr12 = new JCheckBox();
        advanceBerr12.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr12.setEnabled(false);
        filterAdvanceB.add(advanceBerr12);
        advanceB13 = new JCheckBox(SystemConstants.FILTER_ADVANCEB13);
        advanceB13.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB13);
        advanceBText13 = new JTextField();
        advanceBText13.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText13.setEditable(false);
        advanceBText13.setEnabled(false);
        filterAdvanceB.add(advanceBText13);
        advanceBBtn13 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn13.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn13);
        advanceBerr13 = new JCheckBox();
        advanceBerr13.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr13.setEnabled(false);
        filterAdvanceB.add(advanceBerr13);
        advanceB14 = new JCheckBox(SystemConstants.FILTER_ADVANCEB14);
        advanceB14.setPreferredSize(new java.awt.Dimension(78, 16));
        filterAdvanceB.add(advanceB14);
        advanceBText14 = new JTextField();
        advanceBText14.setPreferredSize(new java.awt.Dimension(106, 16));
        advanceBText14.setEditable(false);
        advanceBText14.setEnabled(false);
        filterAdvanceB.add(advanceBText14);
        advanceBBtn14 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceBBtn14.setPreferredSize(new java.awt.Dimension(20, 16));
        filterAdvanceB.add(advanceBBtn14);
        advanceBerr14 = new JCheckBox();
        advanceBerr14.setPreferredSize(new java.awt.Dimension(20, 16));
        advanceBerr14.setEnabled(false);
        filterAdvanceB.add(advanceBerr14);
    }

    /**
	 * @方法说明:
	 * @参数:
	 * @return void
	 * @throws
	 */
    private void initFilterAdvanceAPane() {
        filterAdvanceA = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterAdvanceA.setPreferredSize(new java.awt.Dimension(300, 380));
        advanceA1 = new JCheckBox(SystemConstants.FILTER_ADVANCEA1);
        advanceA1.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA1);
        advanceAText1 = new JTextField();
        advanceAText1.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText1.setEditable(false);
        advanceAText1.setEnabled(false);
        filterAdvanceA.add(advanceAText1);
        advanceABtn1 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn1.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn1);
        advanceAerr1 = new JCheckBox();
        advanceAerr1.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr1.setEnabled(false);
        filterAdvanceA.add(advanceAerr1);
        advanceA2 = new JCheckBox(SystemConstants.FILTER_ADVANCEA2);
        advanceA2.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA2);
        advanceAText2 = new JTextField();
        advanceAText2.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText2.setEditable(false);
        advanceAText2.setEnabled(false);
        filterAdvanceA.add(advanceAText2);
        advanceABtn2 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn2.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn2);
        advanceAerr2 = new JCheckBox();
        advanceAerr2.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr2.setEnabled(false);
        filterAdvanceA.add(advanceAerr2);
        advanceA3 = new JCheckBox(SystemConstants.FILTER_ADVANCEA3);
        advanceA3.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA3);
        advanceAText3 = new JTextField();
        advanceAText3.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText3.setEditable(false);
        advanceAText3.setEnabled(false);
        filterAdvanceA.add(advanceAText3);
        advanceABtn3 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn3.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn3);
        advanceAerr3 = new JCheckBox();
        advanceAerr3.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr3.setEnabled(false);
        filterAdvanceA.add(advanceAerr3);
        advanceA4 = new JCheckBox(SystemConstants.FILTER_ADVANCEA4);
        advanceA4.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA4);
        advanceAText4 = new JTextField();
        advanceAText4.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText4.setEditable(false);
        advanceAText4.setEnabled(false);
        filterAdvanceA.add(advanceAText4);
        advanceABtn4 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn4.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn4);
        advanceAerr4 = new JCheckBox();
        advanceAerr4.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr4.setEnabled(false);
        filterAdvanceA.add(advanceAerr4);
        advanceA5 = new JCheckBox(SystemConstants.FILTER_ADVANCEA5);
        advanceA5.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA5);
        advanceAText5 = new JTextField();
        advanceAText5.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText5.setEditable(false);
        advanceAText5.setEnabled(false);
        filterAdvanceA.add(advanceAText5);
        advanceABtn5 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn5.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn5);
        advanceAerr5 = new JCheckBox();
        advanceAerr5.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr5.setEnabled(false);
        filterAdvanceA.add(advanceAerr5);
        advanceA6 = new JCheckBox(SystemConstants.FILTER_ADVANCEA6);
        advanceA6.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA6);
        advanceAText6 = new JTextField();
        advanceAText6.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText6.setEditable(false);
        advanceAText6.setEnabled(false);
        filterAdvanceA.add(advanceAText6);
        advanceABtn6 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn6.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn6);
        advanceAerr6 = new JCheckBox();
        advanceAerr6.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr6.setEnabled(false);
        filterAdvanceA.add(advanceAerr6);
        advanceA7 = new JCheckBox(SystemConstants.FILTER_ADVANCEA7);
        advanceA7.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA7);
        advanceAText7 = new JTextField();
        advanceAText7.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText7.setEditable(false);
        advanceAText7.setEnabled(false);
        filterAdvanceA.add(advanceAText7);
        advanceABtn7 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn7.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn7);
        advanceAerr7 = new JCheckBox();
        advanceAerr7.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr7.setEnabled(false);
        filterAdvanceA.add(advanceAerr7);
        advanceA8 = new JCheckBox(SystemConstants.FILTER_ADVANCEA8);
        advanceA8.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA8);
        advanceAText8 = new JTextField();
        advanceAText8.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText8.setEditable(false);
        advanceAText8.setEnabled(false);
        filterAdvanceA.add(advanceAText8);
        advanceABtn8 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn8.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn8);
        advanceAerr8 = new JCheckBox();
        advanceAerr8.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr8.setEnabled(false);
        filterAdvanceA.add(advanceAerr8);
        advanceA9 = new JCheckBox(SystemConstants.FILTER_ADVANCEA9);
        advanceA9.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA9);
        advanceAText9 = new JTextField();
        advanceAText9.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText9.setEditable(false);
        advanceAText9.setEnabled(false);
        filterAdvanceA.add(advanceAText9);
        advanceABtn9 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn9.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn9);
        advanceAerr9 = new JCheckBox();
        advanceAerr9.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr9.setEnabled(false);
        filterAdvanceA.add(advanceAerr9);
        advanceA10 = new JCheckBox(SystemConstants.FILTER_ADVANCEA10);
        advanceA10.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA10);
        advanceAText10 = new JTextField();
        advanceAText10.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText10.setEditable(false);
        advanceAText10.setEnabled(false);
        filterAdvanceA.add(advanceAText10);
        advanceABtn10 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn10.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn10);
        advanceAerr10 = new JCheckBox();
        advanceAerr10.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr10.setEnabled(false);
        filterAdvanceA.add(advanceAerr10);
        advanceA11 = new JCheckBox(SystemConstants.FILTER_ADVANCEA11);
        advanceA11.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA11);
        advanceAText11 = new JTextField();
        advanceAText11.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText11.setEditable(false);
        advanceAText11.setEnabled(false);
        filterAdvanceA.add(advanceAText11);
        advanceABtn11 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn11.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn11);
        advanceAerr11 = new JCheckBox();
        advanceAerr11.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr11.setEnabled(false);
        filterAdvanceA.add(advanceAerr11);
        advanceA12 = new JCheckBox(SystemConstants.FILTER_ADVANCEA12);
        advanceA12.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA12);
        advanceAText12 = new JTextField();
        advanceAText12.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText12.setEditable(false);
        advanceAText12.setEnabled(false);
        filterAdvanceA.add(advanceAText12);
        advanceABtn12 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn12.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn12);
        advanceAerr12 = new JCheckBox();
        advanceAerr12.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr12.setEnabled(false);
        filterAdvanceA.add(advanceAerr12);
        advanceA13 = new JCheckBox(SystemConstants.FILTER_ADVANCEA13);
        advanceA13.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA13);
        advanceAText13 = new JTextField();
        advanceAText13.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText13.setEditable(false);
        advanceAText13.setEnabled(false);
        filterAdvanceA.add(advanceAText13);
        advanceABtn13 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn13.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn13);
        advanceAerr13 = new JCheckBox();
        advanceAerr13.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr13.setEnabled(false);
        filterAdvanceA.add(advanceAerr13);
        advanceA14 = new JCheckBox(SystemConstants.FILTER_ADVANCEA14);
        advanceA14.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA14);
        advanceAText14 = new JTextField();
        advanceAText14.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText14.setEditable(false);
        advanceAText14.setEnabled(false);
        filterAdvanceA.add(advanceAText14);
        advanceABtn14 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn14.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn14);
        advanceAerr14 = new JCheckBox();
        advanceAerr14.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr14.setEnabled(false);
        filterAdvanceA.add(advanceAerr14);
        advanceA15 = new JCheckBox(SystemConstants.FILTER_ADVANCEA15);
        advanceA15.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA15);
        advanceAText15 = new JTextField();
        advanceAText15.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText15.setEditable(false);
        advanceAText15.setEnabled(false);
        filterAdvanceA.add(advanceAText15);
        advanceABtn15 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn15.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn15);
        advanceAerr15 = new JCheckBox();
        advanceAerr15.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr15.setEnabled(false);
        filterAdvanceA.add(advanceAerr15);
        advanceA16 = new JCheckBox(SystemConstants.FILTER_ADVANCEA16);
        advanceA16.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA16);
        advanceAText16 = new JTextField();
        advanceAText16.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText16.setEditable(false);
        advanceAText16.setEnabled(false);
        filterAdvanceA.add(advanceAText16);
        advanceABtn16 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn16.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn16);
        advanceAerr16 = new JCheckBox();
        advanceAerr16.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr16.setEnabled(false);
        filterAdvanceA.add(advanceAerr16);
        advanceA17 = new JCheckBox(SystemConstants.FILTER_ADVANCEA17);
        advanceA17.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA17);
        advanceAText17 = new JTextField();
        advanceAText17.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText17.setEditable(false);
        advanceAText17.setEnabled(false);
        filterAdvanceA.add(advanceAText17);
        advanceABtn17 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn17.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn17);
        advanceAerr17 = new JCheckBox();
        advanceAerr17.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr17.setEnabled(false);
        filterAdvanceA.add(advanceAerr17);
        advanceA18 = new JCheckBox(SystemConstants.FILTER_ADVANCEA18);
        advanceA18.setPreferredSize(new java.awt.Dimension(78, 14));
        filterAdvanceA.add(advanceA18);
        advanceAText18 = new JTextField();
        advanceAText18.setPreferredSize(new java.awt.Dimension(106, 14));
        advanceAText18.setEditable(false);
        advanceAText18.setEnabled(false);
        filterAdvanceA.add(advanceAText18);
        advanceABtn18 = new JButton(new ImageIcon(FilePathConstants.EDIT_BUTTON_IMG));
        advanceABtn18.setPreferredSize(new java.awt.Dimension(20, 14));
        filterAdvanceA.add(advanceABtn18);
        advanceAerr18 = new JCheckBox();
        advanceAerr18.setPreferredSize(new java.awt.Dimension(20, 14));
        advanceAerr18.setEnabled(false);
        filterAdvanceA.add(advanceAerr18);
    }
}
