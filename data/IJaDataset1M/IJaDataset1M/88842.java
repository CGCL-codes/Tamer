package swg.gui.resources;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import swg.SWGAide;
import swg.crafting.SWGValues;
import swg.crafting.resources.SWGResource;
import swg.crafting.resources.SWGResourceFilter;
import swg.crafting.resources.SWGResourceWeights;
import swg.gui.common.SWGGuiUtils;
import swg.tools.SimpleTraversalPolicy;

/**
 * A dialog for creating resource guards, setting their values and adding some
 * notes. Guards are added to galaxy-specific lists contained by an object of
 * {@link SWGResourceChecklist}
 * 
 * @author Simon Gronlund <a href="mailto:simongronlund@gmail.com">Simon
 *         Gronlund</a> aka Europe-Chimaera.Zimoon
 * @see SWGResourceChecklist
 */
@SuppressWarnings("serial")
public class SWGResourceGuardDialog extends JDialog implements ActionListener {

    /**
     * A check box for the alarm
     */
    private JCheckBox alarm;

    /**
     * A check box for considering missing stats
     */
    private JCheckBox allowZero;

    /**
     * The cancel button
     */
    private JButton cancelButton;

    /**
     * The guard currently being created/edited
     */
    private SWGResourceGuard currentGuard;

    /**
     * The resource type currently being edited
     */
    private SWGResource currentResource;

    /**
     * A text input field for filter stats; CD
     */
    private JTextField fieldCD;

    /**
     * A text input field for filter stats; CR
     */
    private JTextField fieldCR;

    /**
     * A text input field for filter stats; DR
     */
    private JTextField fieldDR;

    /**
     * A text input field for filter stats; ER
     */
    private JTextField fieldER;

    /**
     * A text input field for filter stats; FL
     */
    private JTextField fieldFL;

    /**
     * A text input field for filter stats; HR
     */
    private JTextField fieldHR;

    /**
     * A text input field for filter stats; MA
     */
    private JTextField fieldMA;

    /**
     * A text input field for filter stats; OQ
     */
    private JTextField fieldOQ;

    /**
     * A text input field for filter stats; PE
     */
    private JTextField fieldPE;

    /**
     * A text input field for filter stats; SR
     */
    private JTextField fieldSR;

    /**
     * A text input field for filter stats; UT
     */
    private JTextField fieldUT;

    /**
     * A text field for the name of the guard
     */
    private JTextField guardName;

    /**
     * A local flag used while cleaning the GUI
     */
    private boolean isCleaning;

    /**
     * A text input area for misc notes regarding this guard
     */
    private JTextArea notesField;

    /**
     * The OK button
     */
    private JButton okButton;

    /**
     * A GUI combo box for choosing resource class names
     */
    private JComboBox resourceClassNameList;

    /**
     * The object holding this GUI dialog
     */
    private final SWGResourceTab resourceTab;

    /**
     * The input text field for this guard's threshold, in x of 1000, used if
     * weighed guard
     */
    private JTextField threshold;

    /**
     * Creates a dialog for creating/editing resource guards
     * 
     * @param resourceTab
     *            an encompassing GUI component
     */
    public SWGResourceGuardDialog(SWGResourceTab resourceTab) {
        super(resourceTab.frame, "Resource Guard", false);
        this.resourceTab = resourceTab;
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 7, 7, 7));
        Vector<Component> travOrder = new Vector<Component>(20);
        contentPane.add(makeTopRow(travOrder), BorderLayout.PAGE_START);
        contentPane.add(makeValuesRow(travOrder), BorderLayout.CENTER);
        contentPane.add(makeBottomRow(travOrder), BorderLayout.PAGE_END);
        guardName.requestFocusInWindow();
        this.setContentPane(contentPane);
        this.pack();
        this.setMinimumSize(this.getSize());
        Point p = resourceTab.frame.getLocation();
        p.translate(100, 100);
        p = (Point) resourceTab.frame.getPrefsKeeper().get("resourceGuardDialogLocation", p);
        p = SWGGuiUtils.ensureOnScreen(p, this.getSize());
        this.setLocation(p);
        SimpleTraversalPolicy to = new SimpleTraversalPolicy(travOrder);
        this.setFocusTraversalPolicy(to);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cancelButton) ; else if (src == okButton) {
            if (isDialogOK()) saveGuard(); else return;
        } else {
            SWGAide.printError("GuardDialog:actionPerformed: " + src, null);
        }
        closeDialog();
    }

    /**
     * Cleans all text input fields
     */
    private void cleanFields() {
        isCleaning = true;
        if (currentResource == null && currentGuard == null) {
            String resCls = (String) resourceClassNameList.getSelectedItem();
            currentResource = getCurrentResource(resCls);
        }
        threshold.setText(null);
        threshold.setEditable(false);
        fieldCD.setText(null);
        fieldCR.setText(null);
        fieldDR.setText(null);
        fieldER.setText(null);
        fieldFL.setText(null);
        fieldHR.setText(null);
        fieldMA.setText(null);
        fieldOQ.setText(null);
        fieldPE.setText(null);
        fieldSR.setText(null);
        fieldUT.setText(null);
        resourceClassNameList.setSelectedIndex(-1);
        isCleaning = false;
    }

    /**
     * Called when about to close this dialog
     */
    private void closeDialog() {
        resourceTab.frame.getPrefsKeeper().add("resourceGuardDialogLocation", getLocation());
        setVisible(false);
    }

    /**
     * Prepares for creating a new guard
     */
    private void createNewGuardInit() {
        cleanFields();
        alarm.setSelected(true);
        allowZero.setSelected(resourceTab.currentResourceTab.filterConsiderNoStat.isEnabled());
        guardName.setText(null);
        notesField.setText(null);
        if (currentResource != null) {
            resourceClassNameList.setSelectedItem(currentResource.getTypeName());
        }
    }

    /**
     * Initiates the GUI dialog with the currently selected guard
     */
    private void editGuardInit() {
        guardName.setText(currentGuard.name);
        resourceClassNameList.setSelectedItem(currentGuard.resourceClass.getTypeName());
        fieldCD.setText(textValue(currentGuard.guardValues.getCD()));
        fieldCR.setText(textValue(currentGuard.guardValues.getCR()));
        fieldDR.setText(textValue(currentGuard.guardValues.getDR()));
        fieldER.setText(textValue(currentGuard.guardValues.getER()));
        fieldFL.setText(textValue(currentGuard.guardValues.getFL()));
        fieldHR.setText(textValue(currentGuard.guardValues.getHR()));
        fieldMA.setText(textValue(currentGuard.guardValues.getMA()));
        fieldOQ.setText(textValue(currentGuard.guardValues.getOQ()));
        fieldPE.setText(textValue(currentGuard.guardValues.getPE()));
        fieldSR.setText(textValue(currentGuard.guardValues.getSR()));
        fieldUT.setText(textValue(currentGuard.guardValues.getUT()));
        threshold.setText(currentGuard.limit > 0 ? Integer.toString((int) currentGuard.limit) : null);
        notesField.setText(currentGuard.notes);
        alarm.setSelected(currentGuard.useAlarm);
        allowZero.setSelected(currentGuard.acceptNoStats);
    }

    /**
     * Returns a dummy resource for the given <code>resourceClassName</code>, or
     * <code>null</code> if not possible
     * 
     * @param resourceClassName
     *            the name of the resource class to create, <code>null</code>
     *            will return <code>null</code>
     * @return a dummy resource for the given <code>resourceClassName</code>, or
     *         <code>null</code> if not possible
     */
    private SWGResource getCurrentResource(String resourceClassName) {
        SWGResource r = null;
        try {
            if (resourceClassName != null) {
                r = SWGResource.createResourceType(resourceClassName, null);
            }
        } catch (Exception e) {
            SWGAide.printError("SWGResourceGuardDialog:getCurrentResource", e);
        }
        return r;
    }

    /**
     * Returns the value of the input field threshold, or 0.0 if is not set,
     * adjusted to range [1 1000] if less than 100
     * 
     * @return the value of the input field threshold, or 0.0 if is not set,
     *         adjusted to range [1 1000] if less than 100
     */
    private double getDouble() {
        double tres = 0.0;
        try {
            tres = Double.parseDouble(threshold.getText());
        } catch (NumberFormatException e) {
        }
        return (tres > 100 ? tres : tres * 10.0);
    }

    /**
     * Returns a resource filter based of the content of the filter panel fields
     * 
     * @return a resource filter based of the content of the filter panel fields
     */
    private int[] getFilterFieldValues() {
        int[] flt = new int[11];
        flt[SWGValues.CD] = SWGResourceTab.parseInt(fieldCD.getText());
        flt[SWGValues.CR] = SWGResourceTab.parseInt(fieldCR.getText());
        flt[SWGValues.DR] = SWGResourceTab.parseInt(fieldDR.getText());
        flt[SWGValues.ER] = SWGResourceTab.parseInt(fieldER.getText());
        flt[SWGValues.FL] = SWGResourceTab.parseInt(fieldFL.getText());
        flt[SWGValues.HR] = SWGResourceTab.parseInt(fieldHR.getText());
        flt[SWGValues.MA] = SWGResourceTab.parseInt(fieldMA.getText());
        flt[SWGValues.OQ] = SWGResourceTab.parseInt(fieldOQ.getText());
        flt[SWGValues.PE] = SWGResourceTab.parseInt(fieldPE.getText());
        flt[SWGValues.SR] = SWGResourceTab.parseInt(fieldSR.getText());
        flt[SWGValues.UT] = SWGResourceTab.parseInt(fieldUT.getText());
        return flt;
    }

    /**
     * Returns a SWGValues object that is either a plain filter or a weighed
     * filter depending on the values parsed in the text input fields
     * 
     * @return a SWGValues object that is either a plain filter or a weighed
     *         filter depending on the values used
     */
    private SWGValues getGuardValues() {
        int[] values = getFilterFieldValues();
        int sum = SWGValues.sum(values);
        if (SWGResourceWeights.isValidWeight(sum)) {
            return new SWGResourceWeights(values);
        }
        return new SWGResourceFilter(values);
    }

    /**
     * Handles updates to the filter input text fields
     */
    void handleThreshold() {
        int sum = SWGValues.sum(getFilterFieldValues());
        threshold.setEditable(SWGResourceWeights.isValidWeight(sum));
    }

    /**
     * Returns <code>true</code> if the dialog has a resource class selected, a
     * name, and at least one value for a stat, <code>false</code> otherwise. If
     * <code>false</code> this method also raises a GUI message dialog on the
     * matter.
     * 
     * @return <code>true</code> if the dialog has valid content,
     *         <code>false</code> otherwise
     */
    private boolean isDialogOK() {
        String msg = null;
        String ttl = null;
        if (currentResource == null) {
            msg = "Select a resource class, please";
            ttl = "No resource class";
        } else if (guardName.getText() == null || guardName.getText().length() <= 0) {
            msg = "Enter a name for the guard, please";
            ttl = "No name";
        } else if (SWGValues.sum(getFilterFieldValues()) <= 0) {
            msg = "Enter values to at least one stat";
            ttl = "No values";
        }
        if (msg != null) {
            JOptionPane.showMessageDialog(alarm, msg, ttl, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Creates and returns the bottommost GUI component for the resource guard
     * dialog
     * 
     * @param travOrder
     *            a vector for the focus traversal order
     * @return the bottommost GUI component for the resource guard dialog
     */
    private Component makeBottomRow(Vector<Component> travOrder) {
        Box bottom = Box.createHorizontalBox();
        notesField = new JTextArea(4, 15);
        notesField.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesField.setLineWrap(true);
        notesField.setWrapStyleWord(true);
        JScrollPane ns = new JScrollPane(notesField, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ns.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel ln = new JLabel("Misc notes", SwingConstants.LEFT);
        Box nb = Box.createVerticalBox();
        nb.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        nb.add(ln);
        nb.add(ns);
        bottom.add(nb);
        Box vb = Box.createHorizontalBox();
        vb.setAlignmentX(Component.LEFT_ALIGNMENT);
        threshold = makeFieldTextField("The threshold for this guard in range [0 1000]");
        threshold.setMaximumSize(new Dimension(40, 21));
        vb.add(threshold);
        JLabel tl = makeFieldLabels(" Threshold");
        tl.setToolTipText("The threshold for this guard in range [0 1000]");
        travOrder.add(threshold);
        vb.add(tl);
        allowZero = new JCheckBox("Calc no-stat");
        allowZero.setToolTipText("Use missing stats in weighing");
        allowZero.setAlignmentX(Component.LEFT_ALIGNMENT);
        travOrder.add(allowZero);
        alarm = new JCheckBox("Alarm");
        alarm.setToolTipText("Alarm when a resource meets this guard");
        alarm.setAlignmentX(Component.LEFT_ALIGNMENT);
        travOrder.add(alarm);
        Box btb = Box.createHorizontalBox();
        okButton = new JButton("OK");
        okButton.setToolTipText("Add this guard to the list of guards");
        okButton.setMnemonic('O');
        okButton.addActionListener(this);
        travOrder.add(okButton);
        btb.add(okButton);
        btb.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(this);
        travOrder.add(cancelButton);
        btb.add(cancelButton);
        Box ob = Box.createVerticalBox();
        ob.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        ob.add(new JLabel("     "));
        ob.add(vb);
        ob.add(allowZero);
        ob.add(alarm);
        ob.add(btb);
        bottom.add(ob);
        return bottom;
    }

    /**
     * Returns a label with the text <code>str</code>
     * 
     * @param str
     *            the text for the label
     * @return a label with the text <code>str</code>
     */
    private JLabel makeFieldLabels(String str) {
        JLabel l = new JLabel(str);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    /**
     * Creates and returns a GUI text field with the supplied text as tool tip
     * text and a lowered bevel border
     * 
     * @param txt
     *            the text for the tool tip
     * @return the GUI component
     */
    private JTextField makeFieldTextField(String txt) {
        final JTextField fld = new JTextField();
        fld.setToolTipText(txt);
        fld.setBorder(BorderFactory.createLoweredBevelBorder());
        fld.setHorizontalAlignment(SwingConstants.RIGHT);
        ((AbstractDocument) fld.getDocument()).setDocumentFilter(new FilterFieldFilter());
        fld.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                handleThreshold();
            }

            public void insertUpdate(DocumentEvent e) {
                handleThreshold();
            }

            public void removeUpdate(DocumentEvent e) {
                handleThreshold();
            }
        });
        return fld;
    }

    /**
     * Returns the GUI component for the uppermost row
     * 
     * @param travOrder
     *            a vector for the focus traversal order
     * @return the GUI component for the uppermost row
     */
    private Component makeTopRow(Vector<Component> travOrder) {
        Box top = Box.createHorizontalBox();
        guardName = new JTextField("", 15);
        guardName.setToolTipText("Type a name for the guard");
        Box tn = Box.createVerticalBox();
        JLabel ln = new JLabel("Guard description");
        ln.setToolTipText("Type a name for the guard");
        tn.add(ln);
        tn.add(guardName);
        travOrder.add(guardName);
        top.add(tn);
        List<String> nameL = SWGResource.getResourceClassNames();
        Vector<String> nameV = new Vector<String>(nameL);
        resourceClassNameList = new JComboBox(nameV);
        resourceClassNameList.setPreferredSize(new Dimension(230, 25));
        resourceClassNameList.setAlignmentX(Component.LEFT_ALIGNMENT);
        resourceClassNameList.setAutoscrolls(true);
        resourceClassNameList.setToolTipText("Select a resource class for the guard");
        resourceClassNameList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resourceClassNameSelected();
            }
        });
        travOrder.add(resourceClassNameList);
        Box tc = Box.createVerticalBox();
        JLabel lc = new JLabel("Resource class", SwingConstants.LEFT);
        lc.setToolTipText("Select a resource class for the guard");
        tc.add(lc);
        tc.add(resourceClassNameList);
        top.add(tc);
        return top;
    }

    /**
     * Returns the GUI component for the guard values, the row with a text field
     * each possible stat
     * 
     * @param travOrder
     *            a vector for the focus traversal order
     * @return the GUI component for the guard values
     */
    private Component makeValuesRow(Vector<Component> travOrder) {
        JPanel b = new JPanel(new GridLayout(2, 11));
        String[] stats = new String[] { "ER", "CR", "CD", "DR", "FL", "HR", "MA", "PE", "OQ", "SR", "UT" };
        for (String s : stats) {
            b.add(makeFieldLabels(s));
        }
        fieldER = makeFieldTextField("ER - Entangle Resistance");
        travOrder.add(fieldER);
        b.add(fieldER);
        fieldCR = makeFieldTextField("CR - Cold Resistance");
        travOrder.add(fieldCR);
        b.add(fieldCR);
        fieldCD = makeFieldTextField("CD - Conductivity");
        travOrder.add(fieldCD);
        b.add(fieldCD);
        fieldDR = makeFieldTextField("DR - Decay Resistance");
        travOrder.add(fieldDR);
        b.add(fieldDR);
        fieldFL = makeFieldTextField("FL - Flavor");
        travOrder.add(fieldFL);
        b.add(fieldFL);
        fieldHR = makeFieldTextField("HR - Heat Resistance");
        travOrder.add(fieldHR);
        b.add(fieldHR);
        fieldMA = makeFieldTextField("MA - Malleability");
        travOrder.add(fieldMA);
        b.add(fieldMA);
        fieldPE = makeFieldTextField("PE - Potential Energy");
        travOrder.add(fieldPE);
        b.add(fieldPE);
        fieldOQ = makeFieldTextField("OQ - Overall Quality");
        travOrder.add(fieldOQ);
        b.add(fieldOQ);
        fieldSR = makeFieldTextField("SR - Shock Resistance");
        travOrder.add(fieldSR);
        b.add(fieldSR);
        fieldUT = makeFieldTextField("UT - Unit Toughness");
        travOrder.add(fieldUT);
        b.add(fieldUT);
        return b;
    }

    /**
     * Called when an item is selected at the resource class name GUI list
     */
    protected void resourceClassNameSelected() {
        if (isCleaning) return;
        String rClass = (String) resourceClassNameList.getSelectedItem();
        currentResource = getCurrentResource(rClass);
    }

    /**
     * Called when "OK" is clicked and will save the guard to the list of guards
     * for the current galaxy
     */
    private void saveGuard() {
        double tres = getDouble();
        SWGResourceGuard tmp = null;
        try {
            SWGValues v = getGuardValues();
            if (SWGResourceFilter.class.isAssignableFrom(v.getClass())) {
                tres = 0.0;
            }
            tmp = new SWGResourceGuard(guardName.getText(), currentResource.getTypeName(), v, alarm.isSelected(), allowZero.isSelected(), tres);
            tmp.notes = notesField.getText();
        } catch (Exception e) {
            SWGAide.printError("SWGResourceGuardDialog:saveGuard", e);
        }
        if (currentGuard != null) {
            resourceTab.frame.resourceChecklist.removeGuard(resourceTab.currentGalaxy, currentGuard);
        }
        resourceTab.frame.resourceChecklist.addGuard(resourceTab.currentGalaxy, tmp);
        resourceTab.currentResourceTab.resetGuardsList();
    }

    /**
     * Called when opening this dialog
     * 
     * @param resourceClass
     *            the resource class to build a guard for, <code>null</code> for
     *            "all resource classes" or when editing a guard
     * @param guard
     *            a guard to edit, <code>null</code> when about to create a new
     *            guard
     */
    public void showAndBegin(SWGResource resourceClass, SWGResourceGuard guard) {
        currentResource = resourceClass;
        currentGuard = guard;
        if (guard == null) createNewGuardInit(); else editGuardInit();
        setVisible(true);
    }

    /**
     * Returns <code>value</code> as text, or <code>null</code> if
     * <code>value</code> is 0
     * 
     * @param value
     *            the integer to parse as text
     * @return <code>value</code> as text, or <code>null</code> if
     *         <code>value</code> is 0
     */
    private String textValue(int value) {
        return (value > 0 ? Integer.toString(value) : null);
    }

    /**
     * A class that extends a document filter to enable input verification for
     * the filter fields
     * 
     * @author Simon Gronlund <a href="mailto:simongronlund@gmail.com">Simon
     *         Gronlund</a> aka Europe-Chimaera.Zimoon
     */
    public class FilterFieldFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb;
            sb = new StringBuilder(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);
            if (isValid(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else Toolkit.getDefaultToolkit().beep();
        }

        /**
         * Returns <code>true</code> if <code>value</code> is a number in the
         * range [0 1000], <code>false</code> otherwise
         * 
         * @param value
         *            the string to validate
         * @return <code>true</code> if value is anumber in range [1 1000],
         *         <code>false</code> otherwise
         */
        private boolean isValid(String value) {
            try {
                if (value == null || value.equals("")) return true;
                int val = Integer.parseInt(value);
                if (0 <= val && val <= 1000) return true;
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            Document doc = fb.getDocument();
            StringBuilder sb;
            sb = new StringBuilder(doc.getText(0, doc.getLength()));
            sb.replace(offset, (offset + length), text);
            if (isValid(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
