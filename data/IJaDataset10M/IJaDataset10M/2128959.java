package ggc.gui.dialogs.ratio;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.atech.graphics.components.DateTimeComponent;
import com.atech.help.HelpCapable;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     RatioCalculatorDialog  
 *  Description:  Ratio Calculator dialog
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */
public class RatioCalculatorDialog extends JDialog implements ActionListener, KeyListener, HelpCapable, FocusListener {

    /**
     * 
     */
    private static final long serialVersionUID = -1240982985415603758L;

    JComboBox cb_time_range, cb_icarb_rule, cb_sens_rule;

    /** 
     * focusGained
     */
    public void focusGained(FocusEvent arg0) {
    }

    boolean in_action = false;

    /** 
     * focusLost
     */
    public void focusLost(FocusEvent ev) {
        if (in_action) return;
        in_action = true;
        if (ev.getSource().equals(this.ftf_bg1)) {
            int val = m_da.getJFormatedTextValueInt(ftf_bg1);
            float v_2 = m_da.getBGValueDifferent(DataAccess.BG_MGDL, val);
            this.ftf_bg2.setValue(new Float(v_2));
        } else if (ev.getSource().equals(this.ftf_bg2)) {
            float val = m_da.getJFormatedTextValueFloat(ftf_bg2);
            int v_2 = (int) m_da.getBGValueDifferent(DataAccess.BG_MMOL, val);
            this.ftf_bg1.setValue(new Integer(v_2));
        } else System.out.println("focus lost: unknown");
        in_action = false;
    }

    private I18nControl m_ic = I18nControl.getInstance();

    private DataAccess m_da = DataAccess.getInstance();

    private boolean m_actionDone = false;

    JTextField DateField, TimeField, ActField, CommentField, UrineField;

    JComboBox cob_bg_type;

    JFormattedTextField ftf_ins1, ftf_ins2, ftf_bg1, ftf_ch, ftf_bg2;

    JLabel label_title = new JLabel();

    JLabel label_food;

    JCheckBox cb_food_set;

    DateTimeComponent dtc;

    JButton AddButton;

    String sDate = null;

    DailyValues dV = null;

    DailyValuesRow m_dailyValuesRow = null;

    NumberFormat bg_displayFormat, bg_editFormat;

    JComponent components[] = new JComponent[9];

    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);

    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);

    boolean in_process;

    boolean debug = true;

    JButton help_button = null;

    JPanel main_panel = null;

    private boolean m_add_action = true;

    private Container m_parent = null;

    /**
     * Constructor
     * 
     * @param dialog
     */
    public RatioCalculatorDialog(JFrame dialog) {
        super(dialog, "", true);
        m_parent = dialog;
        setTitle(m_ic.getMessage("RATIO_CALCULATOR"));
        label_title.setText(m_ic.getMessage("RATIO_CALCULATOR"));
        init();
        this.setVisible(true);
    }

    @SuppressWarnings("unused")
    private void load() {
        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());
        if (m_dailyValuesRow.getBG() > 0) {
            this.ftf_bg1.setValue(new Integer((int) m_dailyValuesRow.getBGRaw()));
            this.ftf_bg2.setValue(new Float(m_da.getBGValueDifferent(DataAccess.BG_MGDL, m_dailyValuesRow.getBGRaw())));
        }
        this.ftf_ins1.setValue(new Integer((int) this.m_dailyValuesRow.getIns1()));
        this.ftf_ins2.setValue(new Integer((int) this.m_dailyValuesRow.getIns2()));
        this.ftf_ch.setValue(new Float(this.m_dailyValuesRow.getCH()));
        ActField.setText(this.m_dailyValuesRow.getActivity());
        UrineField.setText(this.m_dailyValuesRow.getUrine());
        this.cb_food_set.setEnabled(false);
        this.cb_food_set.setSelected(this.m_dailyValuesRow.areMealsSet());
        this.cb_food_set.setEnabled(true);
        CommentField.setText(this.m_dailyValuesRow.getComment());
    }

    private void init() {
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 500;
        Rectangle bnd = m_parent.getBounds();
        x = (bnd.width / 2) + bnd.x - (width / 2);
        y = (bnd.height / 2) + bnd.y - (height / 2);
        this.setBounds(x, y, width, height);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);
        main_panel = panel;
        this.getContentPane().add(panel);
        label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);
        JLabel l = new JLabel(m_ic.getMessage("RATIO_TIME_SELECT_DESC"));
        l.setBounds(30, 70, 330, 80);
        panel.add(l);
        addLabel(m_ic.getMessage("SELECT_RANGE") + ":", 165, panel);
        Object o[] = { m_ic.getMessage("1_WEEK"), m_ic.getMessage("2_WEEKS"), m_ic.getMessage("3_WEEKS"), m_ic.getMessage("1_MONTH") };
        Object o1[] = { m_ic.getMessage("RULE_500"), m_ic.getMessage("RULE_450"), m_ic.getMessage("RULE_300") };
        Object o2[] = { m_ic.getMessage("RULE_1800"), m_ic.getMessage("RULE_1500") };
        this.cb_time_range = new JComboBox(o);
        this.cb_time_range.setBounds(180, 160, 140, 25);
        panel.add(this.cb_time_range);
        addComponent(l = new JLabel(m_ic.getMessage("INSULIN_CARB_RATIO")), 30, 210, 150, panel);
        l.setFont(this.f_bold);
        addLabel(m_ic.getMessage("SELECT_RULE") + ":", 240, panel);
        this.cb_icarb_rule = new JComboBox(o1);
        this.cb_icarb_rule.setBounds(140, 240, 210, 25);
        panel.add(this.cb_icarb_rule);
        addLabel(m_ic.getMessage("1_UNIT_INSULIN") + ":", 270, panel);
        addComponent(l = new JLabel(m_ic.getMessage("SENSITIVITY_FACTOR")), 30, 300, 150, panel);
        l.setFont(this.f_bold);
        addLabel(m_ic.getMessage("SELECT_RULE") + ":", 340, panel);
        this.cb_sens_rule = new JComboBox(o2);
        this.cb_sens_rule.setBounds(140, 340, 210, 25);
        panel.add(this.cb_sens_rule);
        addLabel(m_ic.getMessage("1_UNIT_INSULIN") + ":", 390, panel);
        help_button = m_da.createHelpButtonByBounds(260, 420, 110, 25, this);
        panel.add(help_button);
        m_da.enableHelp(this);
    }

    private void addLabel(String text, int posY, JPanel parent) {
        JLabel label = new JLabel(text);
        label.setBounds(30, posY, 100, 25);
        label.setFont(f_bold);
        parent.add(label);
    }

    private void addComponent(JComponent comp, int posX, int posY, int width, JPanel parent) {
        addComponent(comp, posX, posY, width, 23, true, parent);
    }

    private void addComponent(JComponent comp, int posX, int posY, int width, int height, boolean change_font, JPanel parent) {
        comp.setBounds(posX, posY, width, height);
        comp.addKeyListener(this);
        parent.add(comp);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("cancel")) {
            this.dispose();
        } else if (action.equals("ok")) {
            cmdOk();
        } else System.out.println("RatioDialog::unknown command: " + action);
    }

    private void cmdOk() {
        if (this.m_add_action) {
            if (debug) System.out.println("dV: " + dV);
            this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
            float f = m_da.getJFormatedTextValueFloat(ftf_bg1);
            if (f > 0.0) {
                this.m_dailyValuesRow.setBG(1, f);
            }
            this.m_dailyValuesRow.setIns1(m_da.getJFormatedTextValueInt(this.ftf_ins1));
            this.m_dailyValuesRow.setIns2(m_da.getJFormatedTextValueInt(this.ftf_ins2));
            this.m_dailyValuesRow.setCH(m_da.getJFormatedTextValueFloat(this.ftf_ch));
            this.m_dailyValuesRow.setActivity(ActField.getText());
            this.m_dailyValuesRow.setUrine(UrineField.getText());
            this.m_dailyValuesRow.setComment(CommentField.getText());
            dV.addRow(this.m_dailyValuesRow);
            this.m_actionDone = true;
            this.dispose();
        } else {
            this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
            float f = m_da.getJFormatedTextValueFloat(ftf_bg1);
            if (f > 0.0) {
                this.m_dailyValuesRow.setBG(1, f);
            }
            this.m_dailyValuesRow.setIns1(m_da.getJFormatedTextValueInt(this.ftf_ins1));
            this.m_dailyValuesRow.setIns2(m_da.getJFormatedTextValueInt(this.ftf_ins2));
            this.m_dailyValuesRow.setCH(m_da.getJFormatedTextValueFloat(this.ftf_ch));
            this.m_dailyValuesRow.setActivity(ActField.getText());
            this.m_dailyValuesRow.setUrine(UrineField.getText());
            this.m_dailyValuesRow.setComment(CommentField.getText());
            this.m_actionDone = true;
            this.dispose();
        }
    }

    /**
     * Action Succesfull
     * 
     * @return
     */
    public boolean actionSuccesful() {
        return m_actionDone;
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cmdOk();
        }
    }

    /** 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent() {
        return this.getRootPane();
    }

    /** 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton() {
        return this.help_button;
    }

    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId() {
        return "pages.GGC_BG_Daily_Add";
    }
}
