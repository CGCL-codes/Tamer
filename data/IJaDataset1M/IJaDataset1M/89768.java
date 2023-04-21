package wingset;

import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDefaultListCellRenderer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SGridBagLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.SToolBar;
import org.wings.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

/**
 * A visual control used in many WingSet demos.
 *
 * @author hengels
 */
public class ComponentControls extends SPanel {

    protected static final Object[] BORDERS = new Object[] { new Object[] { "default", SDefaultBorder.INSTANCE }, new Object[] { "none", null }, new Object[] { "raised", new SBevelBorder(SBevelBorder.RAISED) }, new Object[] { "lowered", new SBevelBorder(SBevelBorder.LOWERED) }, new Object[] { "line", new SLineBorder(2) }, new Object[] { "grooved", new SEtchedBorder(SEtchedBorder.LOWERED) }, new Object[] { "ridged", new SEtchedBorder(SEtchedBorder.RAISED) }, new Object[] { "titled", new STitledBorder("Border Title") }, new Object[] { "empty", new SEmptyBorder(5, 5, 5, 5) } };

    protected static final Object[] COLORS = new Object[] { new Object[] { "none", null }, new Object[] { "gray", Color.GRAY }, new Object[] { "yellow", new Color(255, 255, 100) }, new Object[] { "red", new Color(255, 100, 100) }, new Object[] { "green", new Color(100, 255, 100) }, new Object[] { "blue", new Color(100, 100, 255) } };

    protected static final Object[] FONTS = new Object[] { new Object[] { "default font", null }, new Object[] { "16pt sans bold", new SFont("Arial,sans-serif", SFont.BOLD, 16) }, new Object[] { "default serif", new SFont("Times,Times New Roman,serif", SFont.DEFAULT_SIZE, SFont.PLAIN) }, new Object[] { "24pt fantasy italic", new SFont("Comic,Comic Sans MS,fantasy", SFont.ITALIC, 24) } };

    protected final List components = new LinkedList();

    protected final SToolBar globalControls = new SToolBar();

    protected final SToolBar localControls = new SToolBar();

    protected final SButton applyButton;

    protected final STextField widthTextField = new STextField();

    protected final STextField heightTextField = new STextField();

    protected final STextField insetsTextField = new STextField();

    protected final SComboBox borderStyleComboBox = new SComboBox(BORDERS);

    protected final SComboBox borderColorComboBox = new SComboBox(COLORS);

    protected final STextField borderThicknessTextField = new STextField();

    protected final SComboBox backgroundComboBox = new SComboBox(COLORS);

    protected final SComboBox foregroundComboBox = new SComboBox(COLORS);

    protected final SComboBox fontComboBox = new SComboBox(FONTS);

    protected final SCheckBox formComponentCheckBox = new SCheckBox("form");

    private SLabel placeHolder = new SLabel("<html>&nbsp;");

    public ComponentControls() {
        super(new SGridBagLayout());
        setBackground(new Color(240, 240, 240));
        setPreferredSize(SDimension.FULLWIDTH);
        SLineBorder border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.BOTTOM);
        setBorder(border);
        applyButton = new SButton("Apply");
        applyButton.setName("apply");
        applyButton.setActionCommand("apply");
        applyButton.setHorizontalAlignment(SConstants.CENTER_ALIGN);
        applyButton.setVerticalAlignment(SConstants.CENTER_ALIGN);
        border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.LEFT);
        globalControls.setBorder(border);
        globalControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout) globalControls.getLayout()).setHgap(6);
        ((SBoxLayout) globalControls.getLayout()).setVgap(4);
        border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.LEFT);
        localControls.setBorder(border);
        localControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout) localControls.getLayout()).setHgap(6);
        ((SBoxLayout) localControls.getLayout()).setVgap(4);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridheight = 2;
        c.insets.left = 10;
        c.insets.right = 10;
        c.weightx = 0.01;
        add(applyButton, c);
        c.insets.left = 0;
        c.insets.right = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weightx = 0.99;
        add(globalControls, c);
        add(localControls, c);
        widthTextField.setColumns(3);
        widthTextField.setToolTipText("length with unit (example: '200px')");
        heightTextField.setColumns(3);
        heightTextField.setToolTipText("length with unit (example: '200px')");
        insetsTextField.setColumns(1);
        insetsTextField.setToolTipText("length only (example: '8')\n(applies to the border!)");
        borderThicknessTextField.setColumns(1);
        borderThicknessTextField.setToolTipText("length only (example: '2')");
        borderStyleComboBox.setRenderer(new ObjectPairCellRenderer());
        borderColorComboBox.setRenderer(new ObjectPairCellRenderer());
        backgroundComboBox.setRenderer(new ObjectPairCellRenderer());
        foregroundComboBox.setRenderer(new ObjectPairCellRenderer());
        fontComboBox.setRenderer(new ObjectPairCellRenderer());
        formComponentCheckBox.setToolTipText("show as form component .. i.e. trigger form submission");
        globalControls.add(new SLabel("width"));
        globalControls.add(widthTextField);
        globalControls.add(new SLabel(" height"));
        globalControls.add(heightTextField);
        globalControls.add(new SLabel(" border"));
        globalControls.add(borderThicknessTextField);
        globalControls.add(borderStyleComboBox);
        globalControls.add(borderColorComboBox);
        globalControls.add(new SLabel("insets"));
        globalControls.add(insetsTextField);
        globalControls.add(new SLabel(" foreground"));
        globalControls.add(foregroundComboBox);
        globalControls.add(new SLabel(" background"));
        globalControls.add(backgroundComboBox);
        globalControls.add(new SLabel(" font"));
        globalControls.add(fontComboBox);
        globalControls.add(new SLabel(""));
        globalControls.add(formComponentCheckBox);
        localControls.add(placeHolder);
        addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent event) {
                SDimension preferredSize = new SDimension();
                preferredSize.setWidth(widthTextField.getText());
                preferredSize.setHeight(heightTextField.getText());
                int insets = 0;
                try {
                    insets = Integer.parseInt(insetsTextField.getText());
                } catch (NumberFormatException e) {
                }
                int borderThickness = 1;
                try {
                    borderThickness = Integer.parseInt(borderThicknessTextField.getText());
                } catch (NumberFormatException e) {
                }
                SAbstractBorder border = (SAbstractBorder) getSelectedObject(borderStyleComboBox);
                if (border != null && border != SDefaultBorder.INSTANCE) {
                    border.setColor((Color) getSelectedObject(borderColorComboBox));
                    border.setInsets(new Insets(insets, insets, insets, insets));
                    border.setThickness(borderThickness);
                }
                for (Iterator iterator = components.iterator(); iterator.hasNext(); ) {
                    SComponent component = (SComponent) iterator.next();
                    if (widthTextField.isVisible()) component.setPreferredSize(preferredSize);
                    if (borderThicknessTextField.isVisible()) component.setBorder(border != null ? (SBorder) border.clone() : null);
                    if (backgroundComboBox.isVisible()) component.setBackground((Color) getSelectedObject(backgroundComboBox));
                    if (foregroundComboBox.isVisible()) component.setForeground((Color) getSelectedObject(foregroundComboBox));
                    if (formComponentCheckBox.isVisible()) component.setShowAsFormComponent(formComponentCheckBox.isSelected());
                    if (fontComboBox.isVisible()) component.setFont((SFont) getSelectedObject(fontComboBox));
                }
            }
        });
    }

    protected Object getSelectedObject(SComboBox combo) {
        return combo.getSelectedIndex() != -1 ? ((Object[]) combo.getSelectedItem())[1] : null;
    }

    public void removeGlobalControl(SComponent control) {
        int index = Arrays.asList(globalControls.getComponents()).indexOf(control);
        if (index >= 0) {
            globalControls.remove(index);
            globalControls.remove(index - 1);
        }
    }

    public void addControl(SComponent component) {
        if (localControls.getComponent(0) == placeHolder) localControls.removeAll();
        localControls.add(component);
    }

    public void addControllable(SComponent component) {
        components.add(component);
    }

    public List getControllables() {
        return components;
    }

    protected void addActionListener(ActionListener actionListener) {
        applyButton.addActionListener(actionListener);
    }

    /**
     * Renderer which expects <code>Object[]</code> values and returns the first value
     */
    protected static class ObjectPairCellRenderer extends SDefaultListCellRenderer {

        public SComponent getListCellRendererComponent(SComponent list, Object value, boolean selected, int row) {
            Object[] objects = (Object[]) value;
            value = objects[0];
            return super.getListCellRendererComponent(list, value, selected, row);
        }
    }
}
