package msa2snp.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import msa2snp.MSA2SNP;
import msa2snp.Parameter;
import msa2snp.gui.dialogs.FilterDialog;
import msa2snp.gui.dialogs.FilterDistanceDialog;
import msa2snp.gui.dialogs.FilterFreqDialog;
import msa2snp.gui.dialogs.FilterQualityDialog;
import msa2snp.msa.snp.SNP;
import msa2snp.util.PlatformUtils;

public class Toolbar extends JToolBar implements ActionListener {

    private JToggleButton btnC, btnV, btnPi, btnS, btnNone, btnSelected;

    private MonoLabel lblDist, lblQual;

    private BiLabel lblFreq;

    private JRadioButton btnNone2, btnSelected2;

    private Font font1 = PlatformUtils.getPlatformSpecificFont(14, Font.BOLD);

    private Font font2 = PlatformUtils.getPlatformSpecificFont(12, Font.PLAIN, UICaption.locale);

    class DropButton extends JToggleButton {

        private Color arrowColor = Palette.ROYALBLUE;

        public DropButton() {
            super();
            addActionListener(Toolbar.this);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected()) upwards(g2); else downwards(g2);
        }

        public void upwards(Graphics2D g) {
            Polygon p = new Polygon(new int[] { 0, 5, 10 }, new int[] { 10, 3, 10 }, 3);
            Color oldColor = g.getColor();
            g.setColor(arrowColor);
            g.fillPolygon(p);
            g.setColor(oldColor);
            g.drawPolygon(p);
        }

        public void downwards(Graphics2D g) {
            Polygon p = new Polygon(new int[] { 0, 5, 10 }, new int[] { 3, 10, 3 }, 3);
            Color oldColor = g.getColor();
            g.setColor(arrowColor);
            g.fillPolygon(p);
            g.setColor(oldColor);
            g.drawPolygon(p);
        }
    }

    class MonoLabel {

        JRadioButton label;

        DropButton btnDrop;

        FilterDialog filterDialog;

        public MonoLabel() {
            super();
            label = new JRadioButton();
            btnDrop = new DropButton();
            btnDrop.setPreferredSize(Size.toolbar_dropbtn);
        }

        public MonoLabel(String preStr, String subStr, int min) {
            this();
            setText(preStr, subStr, min);
        }

        public void setText(String preStr, String subStr, int min) {
            label.setFont(font2);
            label.setText(preStr + '>' + min + subStr);
        }

        public void addFilterDialog(FilterDialog filterDialog) {
            this.filterDialog = filterDialog;
        }
    }

    class BiLabel {

        JRadioButton label;

        DropButton btnDrop;

        FilterDialog filterDialog;

        public BiLabel() {
            super();
            label = new JRadioButton();
            btnDrop = new DropButton();
            btnDrop.setPreferredSize(Size.toolbar_dropbtn);
        }

        public BiLabel(String preStr, String subStr, int min, int max) {
            this();
            setText(preStr, subStr, min, max);
        }

        public void setText(String preStr, String subStr, int min, int max) {
            label.setFont(font2);
            label.setText(preStr + min + '-' + max + subStr);
        }

        public void addFilterDialog(FilterDialog filterDialog) {
            this.filterDialog = filterDialog;
        }
    }

    public Toolbar() {
        super(JToolBar.HORIZONTAL);
        setPreferredSize(Size.main_frame_north);
        setFloatable(false);
        btnC = new JToggleButton("C");
        btnV = new JToggleButton("V");
        btnPi = new JToggleButton("Pi");
        btnS = new JToggleButton("S");
        btnNone = new JToggleButton();
        btnNone2 = new JRadioButton();
        lblFreq = new BiLabel(UICaption.toolbar_filter_freq, "%", Parameter.filter_freq_min, Parameter.filter_freq_max);
        lblDist = new MonoLabel(UICaption.toolbar_filter_distance, "bp", Parameter.filter_dist_min);
        lblQual = new MonoLabel(UICaption.toolbar_filter_quality, "", Parameter.filter_quality_min);
        lblFreq.addFilterDialog(new FilterFreqDialog());
        lblDist.addFilterDialog(new FilterDistanceDialog());
        lblQual.addFilterDialog(new FilterQualityDialog());
        ButtonGroup group2 = new ButtonGroup();
        group2.add(lblFreq.label);
        group2.add(lblDist.label);
        group2.add(lblQual.label);
        group2.add(btnNone2);
        btnC.setFont(font1);
        btnV.setFont(font1);
        btnPi.setFont(font1);
        btnS.setFont(font1);
        btnC.setForeground(Color.MAGENTA);
        btnV.setForeground(Color.MAGENTA);
        btnPi.setForeground(Color.MAGENTA);
        btnS.setForeground(Color.MAGENTA);
        btnC.setPreferredSize(Size.toolbar_btn1);
        btnV.setPreferredSize(Size.toolbar_btn1);
        btnPi.setPreferredSize(Size.toolbar_btn1);
        btnS.setPreferredSize(Size.toolbar_btn1);
        btnNone.setSelected(true);
        btnSelected = btnNone;
        btnSelected2 = btnNone2;
        ButtonGroup group = new ButtonGroup();
        group.add(btnC);
        group.add(btnV);
        group.add(btnPi);
        group.add(btnS);
        group.add(btnNone);
        add(btnC);
        add(btnV);
        add(btnPi);
        add(btnS);
        add(new JLabel("     "));
        add(lblFreq.label);
        add(new JLabel("  "));
        add(lblFreq.btnDrop);
        add(new JLabel("     "));
        add(lblDist.label);
        add(new JLabel("  "));
        add(lblDist.btnDrop);
        add(new JLabel("     "));
        add(lblQual.label);
        add(new JLabel("  "));
        add(lblQual.btnDrop);
        setToolTipTexts();
    }

    public Toolbar(ActionListener al) {
        this();
        addActionListeners(al);
    }

    private void addActionListeners(ActionListener al) {
        btnC.addActionListener(al);
        btnV.addActionListener(al);
        btnPi.addActionListener(al);
        btnS.addActionListener(al);
        lblFreq.label.addActionListener(al);
        lblDist.label.addActionListener(al);
        lblQual.label.addActionListener(al);
    }

    public JToggleButton btnC() {
        return btnC;
    }

    public JToggleButton btnV() {
        return btnV;
    }

    public JToggleButton btnPi() {
        return btnPi;
    }

    public JToggleButton btnS() {
        return btnS;
    }

    public JToggleButton btnNone() {
        return btnNone;
    }

    public JToggleButton btnSelected() {
        return btnSelected;
    }

    public JRadioButton btnSelected2() {
        return btnSelected2;
    }

    public JRadioButton btnFreq() {
        return lblFreq.label;
    }

    public JRadioButton btnDist() {
        return lblDist.label;
    }

    public JRadioButton btnQual() {
        return lblQual.label;
    }

    public JRadioButton btnNone2() {
        return btnNone2;
    }

    public void setBtnSelected(byte type) {
        switch(type) {
            case SNP.CONSTANT:
                btnSelected = btnC;
                break;
            case SNP.VARIABLE:
                btnSelected = btnV;
                break;
            case SNP.PARSIMONY_INFORMATIVE:
                btnSelected = btnPi;
                break;
            case SNP.SINGLETON:
                btnSelected = btnS;
                break;
            case SNP.RESET:
                btnSelected = btnNone;
                break;
            default:
                break;
        }
        btnSelected.setSelected(true);
    }

    public void setBtnSelected2(JRadioButton btn) {
        btnSelected2 = btn;
        btnSelected2.setSelected(true);
    }

    public void setToolTipTexts() {
        btnC.setToolTipText(UICaption.info_snp_constant_sites);
        btnV.setToolTipText(UICaption.info_snp_variable_sites);
        btnPi.setToolTipText(UICaption.info_snp_parsimony_informative_sites);
        btnS.setToolTipText(UICaption.info_snp_singleton_sites);
    }

    public void setLabelTexts() {
        font2 = PlatformUtils.getPlatformSpecificFont(12, Font.PLAIN, UICaption.locale);
        lblFreq.setText(UICaption.toolbar_filter_freq, "%", Parameter.filter_freq_min, Parameter.filter_freq_max);
        lblDist.setText(UICaption.toolbar_filter_distance, "bp", Parameter.filter_dist_min);
        lblQual.setText(UICaption.toolbar_filter_quality, "", Parameter.filter_quality_min);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object obj = ae.getSource();
        if (obj instanceof DropButton) {
            DropButton btnDrop = (DropButton) obj;
            if (btnDrop == lblFreq.btnDrop) {
                if (btnDrop.isSelected()) {
                    lblFreq.filterDialog.showDialog(lblFreq.btnDrop.getLocationOnScreen());
                } else {
                    lblFreq.filterDialog.hideDialog();
                    resetLblFreq();
                }
            } else if (btnDrop == lblDist.btnDrop) {
                if (btnDrop.isSelected()) {
                    lblDist.filterDialog.showDialog(lblDist.btnDrop.getLocationOnScreen());
                } else {
                    lblDist.filterDialog.hideDialog();
                    resetLblDist();
                }
            } else if (btnDrop == lblQual.btnDrop) {
                if (btnDrop.isSelected()) {
                    lblQual.filterDialog.showDialog(lblQual.btnDrop.getLocationOnScreen());
                } else {
                    lblQual.filterDialog.hideDialog();
                    resetLblQual();
                }
            }
        }
    }

    public FilterDialog filterFreqDialog() {
        return lblFreq.filterDialog;
    }

    public FilterDialog filterDistDialog() {
        return lblDist.filterDialog;
    }

    public FilterDialog filterQualDialog() {
        return lblQual.filterDialog;
    }

    public void resetLblDist() {
        lblDist.setText(UICaption.toolbar_filter_distance, "bp", Parameter.filter_dist_min);
    }

    public void resetLblQual() {
        lblQual.setText(UICaption.toolbar_filter_quality, "", Parameter.filter_quality_min);
    }

    public void resetLblFreq() {
        lblFreq.setText(UICaption.toolbar_filter_freq, "%", Parameter.filter_freq_min, Parameter.filter_freq_max);
    }
}
