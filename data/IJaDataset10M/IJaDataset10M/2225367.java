package org.formaria.svg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import org.formaria.swing.TextArea;
import org.formaria.aria.Project;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.TextHolder;
import org.formaria.aria.helper.AriaUtilities;

/**
 *
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.2 $</p>
 */
public class PopUpWindow extends JComponent implements TextHolder {

    protected int fontSize;

    protected double x, y, width, height, scale1, scale2, scale3, scale4, scale5, scale6, scale7, scale8;

    protected String text, captionStyle;

    protected Stroke stroke;

    protected TextArea textArea;

    protected boolean first;

    protected Project currentProject = ProjectManager.getCurrentProject();

    /** Creates a new instance of PopUpWindow 
   * @param x <CODE>int</CODE> specifying the x-coordinate of the pop-up window.
   * @param y <CODE>int</CODE> specifying the y-coordinate of the pop-up window.
   * @param text <CODE>String</CODE> the text to be displayed in the pop-up window.
   */
    public PopUpWindow() {
        first = true;
        textArea = new TextArea();
        stroke = new BasicStroke(2.0F);
        width = 180.0;
        height = 170.0;
        fontSize = 12;
        scale1 = 0.33333333333333333333333333333333;
        scale2 = 0.52941176470588235294117647058824;
        scale3 = 0.47058823529411764705882352941176;
        scale4 = 0.63529411764705882352941176470588;
        scale5 = 0.88888888888888888888888888888889;
        scale6 = 0.41176470588235294117647058823529;
        scale7 = 0.27777777777777777777777777777778;
        scale8 = 0.94117647058823529411764705882353;
        setSize(1, 1);
        setOpaque(true);
    }

    /**
   * Set the size of the pop-up window.
   * @param x1 <CODE>int</CODE> specifying the x coordinate of the component.
   * @param y1 <CODE>int</CODE> specifying the y coordinate of the component.
   * @param w1 <CODE>int</CODE> specifying the width of the component.
   * @param h1 <CODE>int<CODE> specifying the height of the component.
   */
    public void setBounds(int x1, int y1, int w1, int h1) {
        super.setBounds(x1, y1, w1, h1);
        x = w1 * 0.34;
        y = h1 + 1;
        width = w1 - 3.0;
        height = h1;
    }

    /**
   * Sets the content of the pop-up window.
   * @param text <CODE>String</CODE> specifying the content.
   */
    public void setText(String text) {
        this.text = text;
        textArea.setText(text);
    }

    /** 
   * Returns the content currently displayed in the pop-up window.
   * @return <CODE>String</CODE> containing the currently displayed text.  
   */
    public String getText() {
        return text;
    }

    /**
   * Sets the size of the font displayed in the pop-up window.
   * @param fontSize <CODE>String</CODE> specifying the size of the font.
   */
    public void setFontSize(String fontSize) {
        this.fontSize = Integer.parseInt(fontSize);
        textArea.setFont(new Font("Arial", Font.PLAIN, Integer.parseInt(fontSize)));
    }

    /**
   * Sets the size of the font displayed in the pop-up window.
   * @param fontSize <CODE>int</CODE> specifying the size of the font.
   */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    /** 
   * Returns the font size of the currently displayed text.
   * @return <CODE>int</CODE> specifying the font size of the currently displayed text.  
   */
    public int getFontSize() {
        return fontSize;
    }

    /** 
   * Set the text style
   * @param newStyle the new text style
   */
    public void setStyle(String newStyle) {
        captionStyle = newStyle;
        AriaUtilities.applyStyle(currentProject, textArea, newStyle);
    }

    /**
   * Paints the pop-up window within its parent component.
   * @param g the delegate <CODE>Graphics</CODE> object.
   */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(stroke);
        g2.setColor(Color.gray);
        GeneralPath path = new GeneralPath();
        RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(2, 2, width, (height * scale2), 20.0, 20.0);
        g2.draw(roundRect);
        Line2D.Double line = new Line2D.Double((width * scale1), height, (width * scale4), (height * scale2));
        path.append(line, true);
        Line2D.Double line2 = new Line2D.Double((width * scale3), (height * scale2), (width * scale1), height);
        path.append(line2, true);
        g2.draw(path);
        g2.setColor(Color.decode("#FFF6D5"));
        g2.fill(roundRect);
        g2.fill(path);
        g2.setColor(Color.black);
        if (first) {
            textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setBackground(Color.decode("#FFF6D5"));
            add(textArea);
            first = false;
        }
        textArea.setBounds((int) (x - (width * scale7)), (int) (y - (height * scale8)), (int) (width * scale5), (int) (height * scale6));
        g2.dispose();
    }
}
