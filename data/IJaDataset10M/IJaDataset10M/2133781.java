package com.peralex.utilities.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;

/**
 * This class provides layout of forms which are arranged as 1 or more column
 * pairs where each column pair consists of 2 columns - one for field names, and
 * one for fields.
 * 
 * Note that there are 4 grid-columns and 2 grid-rows in a form "cell": 
 * <code>
 *   | fieldLabel | spacer | field | spacer | 
 *   |------------  spacer  ----------------|
 *   | fieldLabel | spacer | field | spacer | 
 *   |------------  spacer  ----------------|
 * </code>
 * 
 * It also supports adding components to the form where more precise control
 * over filling and resizing are desired.
 * 
 * For example, the following will add a component that fills the allocated
 * space, and also stretches to occupy any extra space.
 * <code>
 *		builder.appendLn(new JTable(), 1, 1, 
 *				GBFormBuilder.Fill.BOTH, GBFormBuilder.Stretch.BOTH);
 *</code>
 *
 * Debugging
 * -----------------
 * calling <code>setDebugColor(Color.RED)</code> will cause the spaces between
 * form elements to be filled with red.
 * This is useful when debugging layout issues.
 *  
 * @author Noel Grandin
 */
public class GBFormBuilder {

    /**
	 * alternate name: AlignFill?
	 * 
	 * Specifies how the form and individual components should be resized
	 * to fit the space made available to them. 
	 * This is normally done so that components in different rows or columns
	 * will have the same size.
	 */
    public static enum Fill {

        HORIZONTAL(GridBagConstraints.HORIZONTAL), VERTICAL(GridBagConstraints.VERTICAL), BOTH(GridBagConstraints.BOTH), NONE(GridBagConstraints.NONE);

        private final int gridBagConstraint;

        private Fill(int gridBagConstraint) {
            this.gridBagConstraint = gridBagConstraint;
        }

        public int toGridBagConstraint() {
            return gridBagConstraint;
        }
    }

    /**
	 * alternate name: ExtraSpaceFill?
	 * 
	 * Specifies how individual components react when excess space needs to 
	 * be allocated.
	 * Only of use when setFormFill() has been called.
	 */
    public static enum Stretch {

        HORIZONTAL(GridBagConstraints.HORIZONTAL), VERTICAL(GridBagConstraints.VERTICAL), BOTH(GridBagConstraints.BOTH), NONE(GridBagConstraints.NONE);

        private final int gridBagConstraint;

        private Stretch(int gridBagConstraint) {
            this.gridBagConstraint = gridBagConstraint;
        }

        public int toGridBagConstraint() {
            return gridBagConstraint;
        }
    }

    /**
	 * Specifies where a component should anchor itself within the available space.
	 */
    public static enum Anchor {

        CENTER(GridBagConstraints.CENTER), NORTH(GridBagConstraints.NORTH), NORTHEAST(GridBagConstraints.NORTHEAST), EAST(GridBagConstraints.EAST), SOUTHEAST(GridBagConstraints.SOUTHEAST), SOUTH(GridBagConstraints.SOUTH), SOUTHWEST(GridBagConstraints.SOUTHWEST), WEST(GridBagConstraints.WEST), NORTHWEST(GridBagConstraints.NORTHWEST);

        private final int gridBagConstraint;

        private Anchor(int gridBagConstraint) {
            this.gridBagConstraint = gridBagConstraint;
        }

        public int toGridBagConstraint() {
            return gridBagConstraint;
        }
    }

    /**
	 * specifies how the form uses excess space
	 */
    private Stretch formStretch = Stretch.NONE;

    /**
	 * specifies where the form is anchored when there is excess space
	 */
    private Anchor formAnchor = Anchor.NORTHWEST;

    /**
	 * I use 2 panels - the inner panel contains the actual fields, and the
	 * outer panel makes sure that the inner panel is top-left aligned if we are
	 * given more space then we need.
	 */
    private final JPanel outerPanel;

    private final JPanel innerPanel;

    /**
	 * the Y/vertical position where the next component will be placed
	 */
    private int gridY = 0;

    /**
	 * the current columnPair index 
	 */
    private int currentColumnPair;

    /**
	 * grid spacing specifiers
	 */
    private int interRowSpacePixels;

    private int interColumnPairPixels;

    private int interColumnPixels;

    /**
	 * if not null, creates a border of the specified colour between form elements to help debug form layout.
	 */
    private Color debugColor = null;

    public GBFormBuilder(JPanel panel) {
        if (panel == null) {
            throw new IllegalStateException("panel may not be null");
        }
        this.outerPanel = panel;
        this.innerPanel = new JPanel();
        this.innerPanel.setBackground(outerPanel.getBackground());
        this.interRowSpacePixels = defaultInterRowPixels(this.innerPanel);
        this.interColumnPairPixels = defaultInterColumnPairPixels(this.innerPanel);
        this.interColumnPixels = defaultInterColumnPixels(this.innerPanel);
        configureInnerOuter();
    }

    public GBFormBuilder() {
        this(new JPanel());
    }

    /**
	 * I use 2 panels - the inner panel contains the actual fields, and the
	 * outer panel makes sure that the inner panel is top-left aligned if we are
	 * given more space then we need.
	 */
    private void configureInnerOuter() {
        this.innerPanel.setLayout(new GridBagLayout());
        this.outerPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1f;
        gbc.weighty = 1f;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        outerPanel.add(innerPanel, gbc);
    }

    /**
	 * Creates a default border.
	 * The size is calculated with a heuristic - 80% of the current font height.
	 */
    public final void setDefaultBorder() {
        final int size = Math.round(outerPanel.getFontMetrics(outerPanel.getFont()).getHeight() * 0.8f);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    public final void setBorder(Border border) {
        outerPanel.setBorder(border);
    }

    /**
	 * By default the form occupies only the space it needs and is
	 * left-justified.
	 * 
	 * @param stretch
	 *            default NONE. 
	 *            If HORIZONTAL, the form will expand to fill the
	 *              horizontal space allocated to it. 
	 *            If VERTICAL, the form will
	 *              expand to fill the vertical space allocated to it. 
	 *            If BOTH...... you figure it out.
	 */
    public void setFormStretch(Stretch stretch) {
        this.formStretch = stretch;
        resetFormAnchorAndStretch();
    }

    /**
	 * By default the form anchored on the top-left (NORTHWEST)
	 * 
	 */
    public void setFormAnchor(Anchor anchor) {
        this.formAnchor = anchor;
        resetFormAnchorAndStretch();
    }

    /**
	 * reset the anchor and fill of the form
	 */
    private void resetFormAnchorAndStretch() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1f;
        gbc.weighty = 1f;
        gbc.anchor = this.formAnchor.toGridBagConstraint();
        gbc.fill = this.formStretch.toGridBagConstraint();
        outerPanel.remove(innerPanel);
        outerPanel.add(innerPanel, gbc);
        outerPanel.revalidate();
        outerPanel.repaint();
    }

    /**
	 * Sets the space between rows in pixels.
	 */
    public void setInterRowSpacePixels(int pixels) {
        this.interRowSpacePixels = pixels;
    }

    /**
	 * returns the space between rows in pixels.
	 */
    public int getInterRowSpacePixels() {
        return this.interRowSpacePixels;
    }

    /**
	 * @return the space between column pairs in pixels
	 */
    public int getInterColumnPairPixels() {
        return interColumnPairPixels;
    }

    /**
	 * Set the space between column pairs in pixels
	 */
    public void setInterColumnPairPixels(int interColumnPairPixels) {
        this.interColumnPairPixels = interColumnPairPixels;
    }

    /**
	 * @return the space between columns in pixels
	 */
    public int getInterColumnPixels() {
        return interColumnPixels;
    }

    /**
	 * Set the space between columns in pixels
	 */
    public void setInterColumnPixels(int interColumnPixels) {
        this.interColumnPixels = interColumnPixels;
    }

    /**
	 * Creates a debug strut.
	 */
    private static Component horizontalDebugStrut(int pixels, Color debugColor) {
        final JLabel debugFiller = new JLabel(" ");
        debugFiller.setMinimumSize(new Dimension(pixels, 2));
        debugFiller.setPreferredSize(new Dimension(pixels, 2));
        debugFiller.setOpaque(true);
        debugFiller.setBackground(debugColor);
        debugFiller.setForeground(debugColor);
        return debugFiller;
    }

    /**
	 * Create a strut that provides a default amount of space between column pairs.
	 */
    private Component interColumnPairStrut() {
        return Box.createHorizontalStrut(interColumnPairPixels);
    }

    /**
	 * Amount of space in pixels to leave between column pairs.
	 */
    private static int defaultInterColumnPairPixels(Component c) {
        return c.getFontMetrics(c.getFont()).getMaxAdvance();
    }

    /**
	 * Create a strut that provides a default amount of space between columns.
	 */
    private Component interColumnStrut() {
        return Box.createHorizontalStrut(interColumnPixels);
    }

    /**
	 * Amount of space in pixels to leave between columns.
	 */
    private static int defaultInterColumnPixels(Component c) {
        return c.getFontMetrics(c.getFont()).getMaxAdvance();
    }

    /**
	 * Default amount of space in pixels to leave between rows.
	 */
    private static int defaultInterRowPixels(Component c) {
        return Math.round(c.getFontMetrics(c.getFont()).getHeight() * 0.35f);
    }

    /**
	 * Adds a empty separator (i.e. just a line) that spans the current column
	 * pair, and move to next line.
	 */
    public void appendSeparatorLn() {
        appendSeparator();
        nextLine();
    }

    /**
	 * Adds a empty separator (i.e. just a line) that spans the current column
	 * pair.
	 */
    private void appendSeparator() {
        appendSeparator("");
    }

    /**
	 * Adds a separator with the given text that spans the current column pair.
	 * 
	 * @param text
	 *            the separator title text
	 */
    private Separator appendSeparator(String text) {
        return appendSeparator(text, 1);
    }

    /**
	 * Adds a separator with the given text that spans the current column pair,
	 * and move to the next line.
	 * 
	 * @param text
	 *            the separator title text
	 */
    public Separator appendSeparatorLn(String text) {
        Separator sep = appendSeparator(text, 1);
        nextLine();
        return sep;
    }

    /**
	 * Adds a separator with the given text that spans the specified number of
	 * columns, and move to next line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param text
	 *            the separator title text
	 */
    public Separator appendSeparatorLn(String text, int numFormColumns) {
        Separator sep = appendSeparator(text, numFormColumns);
        nextLine();
        return sep;
    }

    /**
	 * Adds a separator with the given text that spans the specified number of
	 * columns.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param text
	 *            the separator title text
	 */
    private Separator appendSeparator(String text, int numFormColumns) {
        final Separator titledSeparator = createSeparator(text);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = numFormColumns * 4 - 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        innerPanel.add(titledSeparator, gbc);
        return titledSeparator;
    }

    /**
	 * Append 2 components, next to each other, and move to next line.
	 */
    public void appendLn(Component component1, Component component2) {
        append(component1, component2);
        nextLine();
    }

    /**
	 * Append 2 components, next to each other.
	 */
    private void append(Component component1, Component component2) {
        append(component1, defaultFirstColumnGBC(), component2, defaultSecondColumnGBC());
    }

    private GridBagConstraints defaultFirstColumnGBC() {
        final GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.WEST;
        if (this.formStretch == Stretch.HORIZONTAL || this.formStretch == Stretch.BOTH) {
            gbc1.weightx = 1;
        }
        return gbc1;
    }

    private GridBagConstraints defaultSecondColumnGBC() {
        final GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.WEST;
        if (this.formStretch == Stretch.HORIZONTAL || this.formStretch == Stretch.BOTH) {
            gbc2.weightx = 1;
        }
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        return gbc2;
    }

    /**
	 * Append 2 components, next to each other, with custom constraints.
	 * If either constraint is null, that constraint will be replaced by the default constraint for that column.
	 * 
	 * Note that the gridx and gridy fields will be overwritten to position the
	 * components correctly.
	 */
    public void appendLn(Component component1, GridBagConstraints gbc1, Component component2, GridBagConstraints gbc2) {
        append(component1, gbc1, component2, gbc2);
        nextLine();
    }

    /**
	 * Append 2 components, next to each other, with custom constraints.
	 * Either of the component parameters may be null, which means that their space is empty.
	 * Either of the constraints parameters may be null, which means that they will be defaulted to the normal constraints.
	 * 
	 * Note that the gridx and gridy fields will be overwritten to position the
	 * components correctly.
	 */
    private void append(Component component1, GridBagConstraints gbc1, Component component2, GridBagConstraints gbc2) {
        if (component1 != null) {
            if (gbc1 == null) gbc1 = defaultFirstColumnGBC();
            gbc1.gridx = currentColumnPair * 4;
            gbc1.gridy = gridY;
            innerPanel.add(component1, gbc1);
        }
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = currentColumnPair * 4 + 1;
        gbc.gridy = gridY;
        gbc.fill = GridBagConstraints.NONE;
        final Component filler;
        if (debugColor == null) {
            filler = interColumnStrut();
        } else {
            filler = horizontalDebugStrut(interColumnPixels, debugColor);
            gbc.fill = GridBagConstraints.BOTH;
        }
        innerPanel.add(filler, gbc);
        if (component2 != null) {
            if (gbc2 == null) gbc2 = defaultSecondColumnGBC();
            gbc2.gridx = currentColumnPair * 4 + 2;
            gbc2.gridy = gridY;
            innerPanel.add(component2, gbc2);
        }
    }

    /**
	 * Append label and component, and move to next line.
	 */
    public JLabel appendLn(String textWithMnemonic, Component component) {
        final JLabel label = append(textWithMnemonic, component);
        nextLine();
        return label;
    }

    /**
	 * get the label which was created for the component (assuming the component was added to this form)
	 */
    public static JLabel getLabelFor(Container container, Component findComponent) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component comp = container.getComponent(i);
            if (comp instanceof JLabel) {
                if (((JLabel) comp).getLabelFor() == findComponent) {
                    return (JLabel) comp;
                }
            } else if (comp instanceof Container) {
                JLabel label = getLabelFor((Container) comp, findComponent);
                if (label != null) return label;
            }
        }
        return null;
    }

    /**
	 * Append label and component.
	 */
    private JLabel append(String textWithMnemonic, Component component) {
        final JLabel label = createLabelFor(component, textWithMnemonic);
        label.setLabelFor(component);
        append(label, component);
        return label;
    }

    public void appendLn() {
        nextLine();
    }

    private void nextLine() {
        gridY++;
        if (this.interRowSpacePixels > 0) {
            final GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = currentColumnPair * 4;
            gbc.gridy = gridY;
            gbc.gridwidth = 3;
            final JComponent filler;
            if (debugColor == null) {
                filler = (JComponent) Box.createVerticalStrut(this.interRowSpacePixels);
            } else {
                filler = new JLabel(" ");
                filler.setMinimumSize(new Dimension(2, this.interRowSpacePixels));
                filler.setPreferredSize(new Dimension(2, this.interRowSpacePixels));
                filler.setOpaque(true);
                filler.setBackground(debugColor);
                gbc.fill = GridBagConstraints.HORIZONTAL;
            }
            innerPanel.add(filler, gbc);
            gridY++;
        }
    }

    /**
	 * Append a component that spans one or more form columns, and move to next
	 * line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 */
    public void appendLn(Component component, int numFormColumns) {
        append(component, numFormColumns);
        nextLine();
    }

    /**
	 * Append a component that spans one or more form columns.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 */
    private void append(Component component, int numFormColumns) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = numFormColumns * 4 - 1;
        gbc.weightx = 1;
        innerPanel.add(component, gbc);
    }

    /**
	 * Append a component that spans multiple form columns and form rows, and
	 * move to next line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 */
    public void appendLn(Component component, int noFormColumns, int noFormRows) {
        append(component, noFormColumns, noFormRows);
        nextLine();
    }

    /**
	 * Append a component that spans multiple form columns and form rows.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 */
    private void append(Component component, int noFormColumns, int noFormRows) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = noFormColumns * 4 - 1;
        gbc.gridheight = noFormRows * 2 - 1;
        gbc.weightx = 1;
        innerPanel.add(component, gbc);
        gridY += (noFormRows * 2) - 2;
    }

    /**
	 * Append a component that spans multiple form columns and form rows, and
	 * move to next line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill
	 *            see GridBagConstraints.fill
	 */
    public void appendLn(Component component, int noFormColumns, int noFormRows, Fill fill) {
        append(component, noFormColumns, noFormRows, fill);
        nextLine();
    }

    /**
	 * Append a component that spans multiple form columns and form rows.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill see GridBagConstraints.fill
	 * @param stretch indicate what happens to the component is there is excess space
	 *   to be allocated
	 */
    private void append(Component component, int noFormColumns, int noFormRows, Fill fill, Stretch stretch) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = noFormColumns * 4 - 1;
        gbc.gridheight = noFormRows * 2 - 1;
        gbc.fill = fill.toGridBagConstraint();
        if (stretch == Stretch.HORIZONTAL || stretch == Stretch.BOTH) {
            gbc.weightx = 1;
        }
        if (stretch == Stretch.VERTICAL || stretch == Stretch.BOTH) {
            gbc.weighty = 1;
        }
        innerPanel.add(component, gbc);
        gridY += (noFormRows * 2) - 2;
    }

    /**
	 * Append a component that spans multiple form columns and form rows, and move
	 * to next line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill see GridBagConstraints.fill
	 * @param stretch indicate what happens to the component is there is excess space
	 *   to be allocated
	 */
    public void appendLn(Component component, int noFormColumns, int noFormRows, Fill fill, Stretch stretch) {
        append(component, noFormColumns, noFormRows, fill, stretch);
        nextLine();
    }

    /**
	 * Append a component that spans multiple form columns and form rows.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill
	 *            see GridBagConstraints.fill
	 */
    private void append(Component component, int noFormColumns, int noFormRows, Fill fill) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = noFormColumns * 4 - 1;
        gbc.gridheight = noFormRows * 2 - 1;
        gbc.fill = fill.toGridBagConstraint();
        gbc.weightx = 1;
        innerPanel.add(component, gbc);
        gridY += (noFormRows * 2) - 2;
    }

    /**
	 * Append a component that spans multiple form columns and form rows, and
	 * move to next line.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill
	 *            see GridBagConstraints.fill
	 * @param anchor
	 *            see GridBagConstraints.anchor
	 */
    public void appendLn(Component component, int noFormColumns, int noFormRows, Fill fill, Anchor anchor) {
        append(component, noFormColumns, noFormRows, fill, anchor);
        nextLine();
    }

    /**
	 * Append a component that spans multiple form columns and form rows.
	 * 
	 * One "form column" is a column consisting of field-label/field pairs.
	 * 
	 * @param fill
	 *            see GridBagConstraints.fill
	 * @param anchor
	 *            see GridBagConstraints.anchor
	 */
    private void append(Component component, int noFormColumns, int noFormRows, Fill fill, Anchor anchor) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = anchor.toGridBagConstraint();
        gbc.gridx = currentColumnPair * 4;
        gbc.gridy = gridY;
        gbc.gridwidth = noFormColumns * 4 - 1;
        gbc.gridheight = noFormRows * 2 - 1;
        gbc.fill = fill.toGridBagConstraint();
        gbc.weightx = 1;
        innerPanel.add(component, gbc);
        gridY += (noFormRows * 2) - 2;
    }

    /**
	 * Moves to the next column pair we are working on, and resets the row
	 * index. The first column is column zero.
	 */
    public void nextColumnPair() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = currentColumnPair * 4 + 3;
        gbc.gridy = 0;
        final Component filler;
        if (debugColor == null) {
            filler = interColumnPairStrut();
        } else {
            filler = horizontalDebugStrut(interColumnPairPixels, debugColor);
            gbc.fill = GridBagConstraints.BOTH;
        }
        innerPanel.add(filler, gbc);
        this.currentColumnPair++;
        this.gridY = 0;
    }

    public JPanel getPanel() {
        return this.outerPanel;
    }

    private static JLabel createLabelFor(Component component, String text) {
        if (component instanceof JCheckBox) {
            return new CheckboxLabel((JCheckBox) component, text);
        }
        return new JLabel(text);
    }

    private static Separator createSeparator(String text) {
        return new Separator(text);
    }

    /**
	 * adding checkboxes into forms is tricky. If you add it so that the field
	 * name lines up nicely with everything else you lose the ability to click
	 * on the name and have the checkbox change state. This class creates
	 * special labels that forward mouse clicks to the relevant checkbox.
	 */
    private static class CheckboxLabel extends JLabel {

        private final JCheckBox checkbox;

        public CheckboxLabel(JCheckBox _checkbox, String text) {
            super(text);
            this.checkbox = _checkbox;
            super.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    checkbox.doClick();
                }
            });
        }
    }

    /**
	 * Set the debug color. This makes the spacers between the components be this color.
	 * The default value is null.
	 */
    public Color getDebugColor() {
        return debugColor;
    }

    /**
	 * Get the debug color.
	 */
    public void setDebugColor(Color debugColor) {
        this.debugColor = debugColor;
    }

    /**
	 * Build a panel containing the buttons, centered, with the buttons all sized to the same width.
	 */
    public JPanel buildCenteredBar(AbstractButton... buttons) {
        final JPanel innerBarPanel = new JPanel(new GridLayout(1, 0, this.interColumnPixels, this.interRowSpacePixels));
        for (AbstractButton button : buttons) {
            innerBarPanel.add(button);
        }
        final JPanel outerBarPanel = new JPanel(new BorderLayout());
        outerBarPanel.add(innerBarPanel, BorderLayout.CENTER);
        return outerBarPanel;
    }

    public static class Separator extends JPanel {

        private final JLabel label;

        public Separator(String text) {
            setLayout(new GridBagLayout());
            label = new JLabel(text);
            final GridBagConstraints gbc1 = new GridBagConstraints();
            add(label, gbc1);
            final GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 1;
            add(new JLabel(" "), gbc2);
            final GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 2;
            gbc3.fill = GridBagConstraints.HORIZONTAL;
            gbc3.weightx = 1;
            add(new JSeparator(), gbc3);
        }

        /**
		 * set the separator string/label
		 */
        public void setText(String s) {
            label.setText(s);
        }
    }
}
