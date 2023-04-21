package jpicedt.format.output.latex;

import jpicedt.Localizer;
import jpicedt.graphic.toolkit.AbstractCustomizer;
import jpicedt.graphic.PEToolKit;
import jpicedt.widgets.DecimalNumberField;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import static jpicedt.format.output.latex.LatexConstants.*;
import static jpicedt.Localizer.*;

/**
 * a panel for LaTeX preferences editing (emulation parameters,...)
 * @author Sylvain Reynal
 * @since PicEdt 1.3
 * @version $Id: LatexCustomizer.java,v 1.9 2011/07/23 05:20:51 vincentb1 Exp $
 *
 */
public class LatexCustomizer extends AbstractCustomizer {

    private DecimalNumberField linethicknessTF, emLineLengthTF, maxEmLineSlopeTF, maxLatexDiskDiameterTF;

    private DecimalNumberField emCircleSegLengthTF, maxLatexCircleDiameterTF;

    private JTextArea prologTA, epilogTA;

    private Properties preferences;

    /**
	 * construct a new panel for LaTeX preferences editing.
	 * @param preferences Properties used to init the widgets fields, and to store the values
	 * when "storePreferences" is called.
	 */
    public LatexCustomizer(Properties preferences) {
        this.preferences = preferences;
        Box box = new Box(BoxLayout.Y_AXIS);
        JPanel p;
        JLabel l;
        p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), localize("format.latex.Parameters")));
        l = new JLabel(" " + localize("format.latex.MaxCircleDiameter.label") + "  (mm) :");
        l.setToolTipText(localize("format.latex.MaxCircleDiameter.tooltip"));
        p.add(l);
        maxLatexCircleDiameterTF = new DecimalNumberField(0, 10, true);
        p.add(maxLatexCircleDiameterTF);
        l = new JLabel(" " + localize("format.latex.MaxDiskDiameter.label") + "  (mm) :");
        l.setToolTipText(localize("format.latex.MaxDiskDiameter.tooltip"));
        p.add(l);
        maxLatexDiskDiameterTF = new DecimalNumberField(0, 10, true);
        p.add(maxLatexDiskDiameterTF);
        box.add(p);
        p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), localize("format.latex.Emulation")));
        l = new JLabel(" " + localize("format.latex.EmLineLength.label") + "  (mm) :");
        l.setToolTipText(localize("format.latex.EmLineLength.tooltip"));
        p.add(l);
        emLineLengthTF = new DecimalNumberField(0, 10, true);
        p.add(emLineLengthTF);
        l = new JLabel(" " + localize("format.latex.EmMaxLineSlope.label"));
        l.setToolTipText(localize("format.latex.EmMaxLineSlope.tooltip"));
        p.add(l);
        maxEmLineSlopeTF = new DecimalNumberField(0, 10, true);
        p.add(maxEmLineSlopeTF);
        l = new JLabel(" " + localize("format.latex.EmCircleSegLength.label") + "  (mm) :");
        l.setToolTipText(localize("format.latex.EmCircleSegLength.tooltip"));
        p.add(l);
        emCircleSegLengthTF = new DecimalNumberField(0, 10, true);
        p.add(emCircleSegLengthTF);
        box.add(p);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        p = new JPanel(gbl);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), localize("format.PrologEpilog")));
        l = new JLabel(" " + localize("format.Prolog"));
        l.setToolTipText(localize("format.Prolog.tooltip"));
        gbl.setConstraints(l, c);
        p.add(l);
        prologTA = new JTextArea(5, 50);
        gbl.setConstraints(prologTA, c);
        p.add(prologTA);
        l = new JLabel(" " + localize("format.Epilog"));
        l.setToolTipText(localize("format.Epilog.tooltip"));
        gbl.setConstraints(l, c);
        p.add(l);
        epilogTA = new JTextArea(5, 50);
        gbl.setConstraints(epilogTA, c);
        p.add(epilogTA);
        box.add(p);
        add(box, BorderLayout.NORTH);
    }

    /**
	 * @return the panel title, used e.g. for Border or Tabpane title. 
	 */
    public String getTitle() {
        return "LaTeX";
    }

    /**
	 * @return the Icon associated with this panel, used e.g. for TabbedPane decoration 
	 */
    public Icon getIcon() {
        return null;
    }

    /**
	 * @return the tooltip string associated with this panel 
	 */
    public String getTooltip() {
        return localize("format.latex.tooltip");
    }

    /**
	 * Load widgets display content with a default value retrieved from the LatexConstants interface.
	 */
    public void loadDefault() {
        emLineLengthTF.setValue(DEFAULT_EM_LINE_LENGTH);
        maxEmLineSlopeTF.setValue(DEFAULT_MAX_EM_LINE_SLOPE);
        maxLatexCircleDiameterTF.setValue(DEFAULT_MAX_CIRCLE_DIAMETER);
        maxLatexDiskDiameterTF.setValue(DEFAULT_MAX_DISK_DIAMETER);
        emCircleSegLengthTF.setValue(DEFAULT_MAX_EM_CIRCLE_SEGMENT_LENGTH);
        prologTA.setText(DEFAULT_FILE_WRAPPER_PROLOG);
        epilogTA.setText(DEFAULT_FILE_WRAPPER_EPILOG);
    }

    /**
	 * Load widgets value from the Properties object given in the constructor.
	 */
    public void load() {
        emLineLengthTF.setText(preferences.getProperty(KEY_EM_LINE_LENGTH, PEToolKit.doubleToString(DEFAULT_EM_LINE_LENGTH)));
        maxEmLineSlopeTF.setText(preferences.getProperty(KEY_MAX_EM_LINE_SLOPE, PEToolKit.doubleToString(DEFAULT_MAX_EM_LINE_SLOPE)));
        maxLatexCircleDiameterTF.setText(preferences.getProperty(KEY_MAX_CIRCLE_DIAMETER, PEToolKit.doubleToString(DEFAULT_MAX_CIRCLE_DIAMETER)));
        maxLatexDiskDiameterTF.setText(preferences.getProperty(KEY_MAX_DISK_DIAMETER, PEToolKit.doubleToString(DEFAULT_MAX_DISK_DIAMETER)));
        emCircleSegLengthTF.setText(preferences.getProperty(KEY_MAX_EM_CIRCLE_SEGMENT_LENGTH, PEToolKit.doubleToString(DEFAULT_MAX_EM_CIRCLE_SEGMENT_LENGTH)));
        prologTA.setText(preferences.getProperty(KEY_FILE_WRAPPER_PROLOG, DEFAULT_FILE_WRAPPER_PROLOG));
        epilogTA.setText(preferences.getProperty(KEY_FILE_WRAPPER_EPILOG, DEFAULT_FILE_WRAPPER_EPILOG));
    }

    /**
	 * Store current widgets value to the Properties object given in the constructor,
	 * then update LatexFormatter accordingly.
	 */
    public void store() {
        preferences.setProperty(KEY_EM_LINE_LENGTH, emLineLengthTF.getText());
        preferences.setProperty(KEY_MAX_CIRCLE_DIAMETER, maxLatexCircleDiameterTF.getText());
        preferences.setProperty(KEY_MAX_DISK_DIAMETER, maxLatexDiskDiameterTF.getText());
        preferences.setProperty(KEY_MAX_EM_CIRCLE_SEGMENT_LENGTH, emCircleSegLengthTF.getText());
        preferences.setProperty(KEY_MAX_EM_LINE_SLOPE, maxEmLineSlopeTF.getText());
        preferences.setProperty(KEY_FILE_WRAPPER_PROLOG, prologTA.getText());
        preferences.setProperty(KEY_FILE_WRAPPER_EPILOG, epilogTA.getText());
        LatexFormatter.configure(preferences);
    }
}
