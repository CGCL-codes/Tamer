package com.rb.lottery.analysis.client.UI.panel;

import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.rb.lottery.analysis.common.ColorConstants;
import com.rb.lottery.analysis.common.SystemConstants;

/**
 * @类功能说明: 
 * @类修改者:   
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-11-13 下午04:54:29
 * @版本:       1.0.0
 */
public class LotteryFilterBetPanel extends JPanel {

    /**
	 * @Fields serialVersionUID: TODO
	 */
    private static final long serialVersionUID = 1709698805083054834L;

    private JCheckBox allowerr;

    private JComboBox lefterr;

    private JComboBox righterr;

    private JButton filter;

    public LotteryFilterBetPanel() {
        super();
        init();
    }

    public LotteryFilterBetPanel(LayoutManager layout) {
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
        this.setPreferredSize(new java.awt.Dimension(300, 25));
        this.setBorder(null);
        allowerr = new JCheckBox();
        allowerr.setPreferredSize(new java.awt.Dimension(30, 25));
        this.add(allowerr);
        lefterr = new JComboBox(SystemConstants.ALLOW_ERROR_COMBO);
        lefterr.setPreferredSize(new java.awt.Dimension(50, 25));
        this.add(lefterr);
        JLabel tmp = new JLabel(SystemConstants.ALLOW_ERROR_COUNT);
        tmp.setPreferredSize(new java.awt.Dimension(65, 25));
        this.add(tmp);
        righterr = new JComboBox(SystemConstants.ALLOW_ERROR_COMBO);
        righterr.setPreferredSize(new java.awt.Dimension(50, 25));
        this.add(righterr);
        filter = new JButton(SystemConstants.FILTER);
        filter.setPreferredSize(new java.awt.Dimension(60, 25));
        filter.setBackground(ColorConstants.BUTTON_COLOR);
        this.add(filter);
    }
}
