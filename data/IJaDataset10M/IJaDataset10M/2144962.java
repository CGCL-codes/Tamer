package jasperdesign.ui;

import jasperdesign.RW;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;

/**
 *  Description of the Class
 *
 * @author     manningj
 * @created    September 19, 2003
 */
public class ImagePanel extends JPanel {

    JasperDesign design = null;

    JRGraphicElement element = null;

    JComboBox scaleCbo = null;

    JComboBox typeCbo = null;

    JComboBox hAlignCbo = null;

    JComboBox vAlignCbo = null;

    JCheckBox cacheOpt = null;

    JTextArea expTxt = null;

    /**
     *  Constructor for the ImagePanel
     *
     * @param  aDesign    Description of the Parameter
     * @param  anElement  Description of the Parameter
     */
    public ImagePanel(JasperDesign aDesign, JRImage anElement) {
        this(aDesign);
        setElement(anElement);
    }

    /**
     *  Constructor for the ImagePanel
     *
     * @param  aDesign  Description of the Parameter
     */
    public ImagePanel(JasperDesign aDesign) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        JLabel lbl = null;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 5);
        lbl = new JLabel("Scale");
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        scaleCbo = new JComboBox(RW.SCALE_TYPES);
        add(scaleCbo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 5);
        lbl = new JLabel("Type");
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        typeCbo = new JComboBox(RW.IMAGE_EXPRESSION_TYPES);
        add(typeCbo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 5);
        lbl = new JLabel("Horiz alignment");
        add(lbl, gbc);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        hAlignCbo = new JComboBox(RW.HORIZONTAL_ALIGNMENTS);
        add(hAlignCbo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 5);
        lbl = new JLabel("Vert alignment");
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        vAlignCbo = new JComboBox(RW.VERTICAL_ALIGNMENTS);
        add(vAlignCbo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        cacheOpt = new JCheckBox("Using cache?");
        add(cacheOpt, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 5);
        lbl = new JLabel("Expression:");
        add(lbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.insets = new Insets(0, 0, 5, 5);
        expTxt = new JTextArea();
        add(new JScrollPane(expTxt), gbc);
    }

    /**
     *  Sets the element of the ImagePanel
     *
     * @param  element  The new element value
     */
    public void setElement(JRImage element) {
        scaleCbo.setSelectedItem(RW.getScaleTypeName(element.getScaleImage()));
        hAlignCbo.setSelectedItem(RW.getHorizontalAlignmentName(element.getHorizontalAlignment()));
        vAlignCbo.setSelectedItem(RW.getVerticalAlignmentName(element.getVerticalAlignment()));
        cacheOpt.setSelected(element.isUsingCache());
        JRExpression exp = element.getExpression();
        if (exp != null) {
            typeCbo.setSelectedItem(exp.getValueClass().getName());
            expTxt.setText(RW.getExpressionText(exp));
        }
    }

    /**
     *  Description of the Method
     *
     * @param  element  Description of the Parameter
     */
    public void updateElement(JRDesignImage element) {
        element.setScaleImage(RW.getScaleType("" + scaleCbo.getSelectedItem()));
        element.setHorizontalAlignment(RW.getHorizontalAlignment("" + hAlignCbo.getSelectedItem()));
        element.setVerticalAlignment(RW.getVerticalAlignment("" + vAlignCbo.getSelectedItem()));
        element.setUsingCache(cacheOpt.isSelected());
        JRDesignExpression exp = new JRDesignExpression();
        element.setExpression(exp);
        exp.setText(expTxt.getText());
        try {
            exp.setValueClass(Class.forName("" + typeCbo.getSelectedItem()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
