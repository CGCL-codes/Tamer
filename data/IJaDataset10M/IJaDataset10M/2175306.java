package com.inetmon.jn.statistic.general.single.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import com.inetmon.jn.statistic.general.single.views.IGeneralConstant;

/**
 * Class to display the value of statistics per second
 * 
 * @author Arnaud MARTIN
 */
public class PanelLabel extends Composite {

    /**
	 * Display for the label of the current value
	 */
    private JLabel current;

    /**
	 * Display for the label of the average value
	 */
    private JLabel average;

    /**
	 * Display for the label of the peak value
	 */
    private JLabel peak;

    /**
	 * Display for the label of the date of the peak value
	 */
    private JLabel timePeak;

    /**
	 * Display for the value of the current value
	 */
    private JLabel currentValue;

    /**
	 * Display for the value of the average value
	 */
    private JLabel averageValue;

    /**
	 * Display for the value of the peak value
	 */
    private JLabel peakValue;

    /**
	 * Display for the value of the date of the peak value
	 */
    private JLabel timePeakValue;

    /**
	 * Display for separators between the labels and the values
	 */
    private JLabel[] separator;

    /**
	 * Display for the units
	 */
    private JLabel[] units;

    /**
	 * Display for the label of the ip value
	 */
    private JLabel ip;

    /**
	 * Display for the value of the ip of the ip value
	 */
    private JLabel ipValue;

    /**
	 * Constructor
	 * 
	 * @param the
	 *            parent of the panel
	 */
    public PanelLabel(Composite parent) {
        super(parent, SWT.EMBEDDED);
        RGB bgRGB = this.getBackground().getRGB();
        Color color = new Color(bgRGB.red, bgRGB.green, bgRGB.blue);
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel labelPanel = new JPanel(new GridLayout(5, 2));
        JPanel valuePanel = new JPanel(new GridLayout(5, 1));
        JPanel currentPanel = new JPanel(new GridLayout(1, 2));
        JPanel averagePanel = new JPanel(new GridLayout(1, 3));
        JPanel peakPanel = new JPanel(new GridLayout(1, 3));
        JPanel currentColorPanel1 = new JPanel(new GridLayout(1, 2));
        JPanel averageColorPanel1 = new JPanel(new GridLayout(1, 2));
        JPanel peakColorPanel1 = new JPanel(new GridLayout(1, 2));
        JPanel currentColorPanel2 = new JPanel(new GridLayout(1, 1));
        JPanel averageColorPanel2 = new JPanel(new GridLayout(1, 1));
        JPanel peakColorPanel2 = new JPanel(new GridLayout(1, 1));
        JPanel currentColorPanel3 = new JPanel(new GridLayout(1, 1));
        JPanel averageColorPanel3 = new JPanel(new GridLayout(1, 1));
        JPanel peakColorPanel3 = new JPanel(new GridLayout(1, 1));
        mainPanel.setBackground(color);
        labelPanel.setBackground(color);
        valuePanel.setBackground(color);
        currentPanel.setBackground(color);
        averagePanel.setBackground(color);
        peakPanel.setBackground(color);
        currentColorPanel2.setBackground(color);
        averageColorPanel2.setBackground(color);
        peakColorPanel2.setBackground(color);
        currentColorPanel3.setBackground(IGeneralConstant.COLORS[0]);
        averageColorPanel3.setBackground(IGeneralConstant.COLORS[1]);
        peakColorPanel3.setBackground(color);
        Frame awtFrame = SWT_AWT.new_Frame(this);
        awtFrame.add(mainPanel);
        createLabel();
        labelPanel.add(current);
        labelPanel.add(separator[0]);
        labelPanel.add(average);
        labelPanel.add(separator[1]);
        labelPanel.add(peak);
        labelPanel.add(separator[2]);
        labelPanel.add(timePeak);
        labelPanel.add(separator[3]);
        labelPanel.add(ip);
        labelPanel.add(separator[4]);
        mainPanel.add(labelPanel);
        currentPanel.add(currentValue);
        currentPanel.add(units[0]);
        currentPanel.add(currentColorPanel1);
        currentColorPanel1.add(currentColorPanel2);
        currentColorPanel1.add(currentColorPanel3);
        averagePanel.add(averageValue);
        averagePanel.add(units[1]);
        averagePanel.add(averageColorPanel1);
        averageColorPanel1.add(averageColorPanel2);
        averageColorPanel1.add(averageColorPanel3);
        peakPanel.add(peakValue);
        peakPanel.add(units[2]);
        peakPanel.add(peakColorPanel1);
        peakColorPanel1.add(peakColorPanel2);
        peakColorPanel1.add(peakColorPanel3);
        valuePanel.add(currentPanel);
        valuePanel.add(averagePanel);
        valuePanel.add(peakPanel);
        valuePanel.add(timePeakValue);
        valuePanel.add(ipValue);
        mainPanel.add(valuePanel);
    }

    /**
	 * Create the labels
	 */
    private void createLabel() {
        ip = new JLabel();
        ipValue = new JLabel();
        current = new JLabel();
        currentValue = new JLabel();
        average = new JLabel();
        averageValue = new JLabel();
        peak = new JLabel();
        peakValue = new JLabel();
        timePeak = new JLabel();
        timePeakValue = new JLabel();
        separator = new JLabel[5];
        for (int i = 0; i < separator.length; i++) {
            separator[i] = new JLabel(":");
            separator[i].setFont(new Font(null, Font.PLAIN, 12));
        }
        units = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            units[i] = new JLabel();
            units[i].setFont(new Font(null, Font.PLAIN, 12));
        }
        currentValue.setHorizontalAlignment(JLabel.TRAILING);
        averageValue.setHorizontalAlignment(JLabel.TRAILING);
        peakValue.setHorizontalAlignment(JLabel.TRAILING);
        timePeakValue.setHorizontalAlignment(JLabel.CENTER);
        ipValue.setHorizontalAlignment(JLabel.CENTER);
        ip.setFont(new Font(null, Font.PLAIN, 12));
        ipValue.setFont(new Font(null, Font.PLAIN, 12));
        current.setFont(new Font(null, Font.PLAIN, 12));
        currentValue.setFont(new Font(null, Font.PLAIN, 12));
        average.setFont(new Font(null, Font.PLAIN, 12));
        averageValue.setFont(new Font(null, Font.PLAIN, 12));
        peak.setFont(new Font(null, Font.PLAIN, 12));
        peakValue.setFont(new Font(null, Font.PLAIN, 12));
        timePeak.setFont(new Font(null, Font.PLAIN, 12));
        timePeakValue.setFont(new Font(null, Font.PLAIN, 12));
    }

    /**
	 * Set the text of the labels
	 * 
	 * @param currentL
	 *            text for the current label
	 * @param averageL
	 *            text for the average label
	 * @param peakL
	 *            text for the peak label
	 * @param datePeakL
	 *            text for the date of the peak label
	 */
    public void setTextLabels(String currentL, String averageL, String peakL, String datePeakL, String ipL) {
        current.setText(currentL);
        average.setText(averageL);
        peak.setText(peakL);
        timePeak.setText(datePeakL);
        ip.setText(ipL);
    }

    /**
	 * Set the text of the values
	 * 
	 * @param currentValueL
	 *            currentL text for the current value
	 * @param averageValueL
	 *            averageL text for the average value
	 * @param peakValueL
	 *            peakL text for the peak value
	 * @param date
	 *            datePeakL text for the date of the peak
	 */
    public void setTextValueLabel(String currentValueL, String averageValueL, String peakValueL, Date date, String ipValueL) {
        currentValue.setText(currentValueL);
        averageValue.setText(averageValueL);
        peakValue.setText(peakValueL);
        timePeakValue.setText(date.toString());
        ipValue.setText(ipValueL);
    }

    /**
	 * Set the text for the units
	 * 
	 * @param unit
	 *            the unit
	 */
    public void setUnits(String unit) {
        for (int i = 0; i < 3; i++) {
            units[i].setText(unit);
        }
    }
}
