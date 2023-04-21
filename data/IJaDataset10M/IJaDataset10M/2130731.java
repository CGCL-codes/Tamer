package edu.emory.mathcs.restoretools.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;

/**
 * Simple utility class for creating forms that have a column of labels and a
 * column of fields. All of the labels have the same width, determined by the
 * width of the widest label component.
 * <P>
 * Philip Isenhour - 060628 - http://javatechniques.com/
 */
public class FormUtility {

    /**
     * Grid bag constraints for fields and labels
     */
    private GridBagConstraints lastConstraints = null;

    private GridBagConstraints middleConstraints = null;

    private GridBagConstraints labelConstraints = null;

    public FormUtility() {
        lastConstraints = new GridBagConstraints();
        lastConstraints.fill = GridBagConstraints.HORIZONTAL;
        lastConstraints.anchor = GridBagConstraints.NORTHWEST;
        lastConstraints.weightx = 1.0;
        lastConstraints.gridwidth = GridBagConstraints.REMAINDER;
        lastConstraints.insets = new Insets(2, 2, 2, 2);
        middleConstraints = (GridBagConstraints) lastConstraints.clone();
        middleConstraints.gridwidth = GridBagConstraints.RELATIVE;
        labelConstraints = (GridBagConstraints) lastConstraints.clone();
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;
        labelConstraints.anchor = GridBagConstraints.CENTER;
    }

    /**
     * Adds an arbitrary label component, starting a new row if appropriate. The
     * width of the component will be set to the minimum width of the widest
     * component on the form.
     */
    public void addLabel(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, labelConstraints);
        parent.add(c);
    }

    /**
     * Adds a JLabel with the given string to the label column
     */
    public JLabel addLabel(String s, Container parent) {
        JLabel c = new JLabel(s);
        addLabel(c, parent);
        return c;
    }

    /**
     * Adds a field component. Any component may be used. The component will be
     * stretched to take the remainder of the current row.
     */
    public void addLastField(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, lastConstraints);
        parent.add(c);
    }

    /**
     * Adds a "middle" field component. Any component may be used. The component
     * will be stretched to take all of the space between the label and the
     * "last" field. All "middle" fields in the layout will be the same width.
     */
    public void addMiddleField(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, middleConstraints);
        parent.add(c);
    }
}
