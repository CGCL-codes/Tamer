package jgnash.ui.print.checks;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.*;
import jgnash.ui.UIApplication;
import jgnash.ui.util.*;
import jgnash.xml.XMLFileInputStream;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/** Displays a dialog that shows the available options for printing a check.
 * <p>
 * $Id: PrintCheckDialog.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author  Craig Cavanaugh
 */
public class PrintCheckDialog extends JDialog implements ActionListener {

    private UIResource rb = (UIResource) UIResource.get();

    private JButton cancelButton;

    private JCheckBox incCheckBox;

    private JTextField layoutField;

    private JButton printButton;

    private JButton selectButton;

    private JSpinner startSpinner;

    private CheckLayout checkLayout;

    private static final String LAST_LAYOUT = "lastlayout";

    private static final String CURRENT_DIR = "cwd";

    private static final String INC_NUM = "incrementNumbers";

    private boolean returnStatus = false;

    private Preferences pref = Preferences.userRoot().node("/jgnash/print");

    public PrintCheckDialog() {
        super(UIApplication.getFrame(), true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        layoutMainPanel();
        this.setLocationRelativeTo(UIApplication.getFrame());
        if (pref.get(LAST_LAYOUT, null) != null) {
            loadLayout(pref.get(LAST_LAYOUT, null));
        }
    }

    private void initComponents() {
        layoutField = new JTextField();
        selectButton = new JButton(rb.getString("Button.Select"));
        startSpinner = new JSpinner();
        incCheckBox = new JCheckBox(rb.getString("Button.IncCheckNums"));
        printButton = new JButton(rb.getString("Button.Print"));
        cancelButton = new JButton(rb.getString("Button.Cancel"));
        ((SpinnerNumberModel) startSpinner.getModel()).setValue(new Integer(1));
        cancelButton.addActionListener(this);
        printButton.addActionListener(this);
        selectButton.addActionListener(this);
        incCheckBox.setSelected(pref.getBoolean(INC_NUM, false));
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("p, 4dlu, 85dlu:g, 4dlu, p", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setRowGroupingEnabled(true);
        builder.append(rb.getString("Label.CheckLayout"), layoutField, selectButton);
        builder.append(rb.getString("Label.StartPos"), startSpinner);
        builder.nextLine();
        builder.append(incCheckBox, 5);
        builder.setRowGroupingEnabled(false);
        builder.nextLine();
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(printButton, cancelButton), 5);
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        pack();
    }

    private void closeDialog() {
        pref.putBoolean(INC_NUM, incCheckBox.isSelected());
        setVisible(false);
        dispose();
    }

    private void setupSpinner() {
        ((SpinnerNumberModel) startSpinner.getModel()).setMinimum(new Integer(1));
        ((SpinnerNumberModel) startSpinner.getModel()).setMaximum(new Integer(checkLayout.getNumChecks()));
    }

    /**
     * dir can be null, The JFileChooser will sort it out
     */
    private JFileChooser createFileChooser(String dir) {
        JFileChooser chooser = new JFileChooser(dir);
        FileFilterEx filter = new FileFilterEx("lay.xml", "jGnash Files(*.lay.xml)");
        chooser.setFileFilter(filter);
        return chooser;
    }

    /**
     * Show a file selection dialog for the check layout using the last known
     * directory.  If successful, save the working directoy and file path.
     */
    private void selectLayout() {
        JFileChooser chooser = createFileChooser(pref.get(CURRENT_DIR, null));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            pref.put(CURRENT_DIR, chooser.getCurrentDirectory().getAbsolutePath());
            String file = chooser.getSelectedFile().getAbsolutePath();
            chooser = null;
            pref.put(LAST_LAYOUT, file);
            loadLayout(file);
        }
    }

    /** Position starts at index 0 */
    public int getStartPosition() {
        return ((Number) startSpinner.getValue()).intValue() - 1;
    }

    public boolean getReturnStatus() {
        return returnStatus;
    }

    public CheckLayout getCheckLayout() {
        return checkLayout;
    }

    public boolean incrementCheckNumbers() {
        return incCheckBox.isSelected();
    }

    private void loadLayout(String file) {
        if (new File(file).exists()) {
            Object o = null;
            try {
                XMLFileInputStream xmlstream = new XMLFileInputStream(file);
                o = xmlstream.readXMLObject("layout");
            } catch (Exception e) {
                System.err.println(e.toString());
                e.printStackTrace();
            }
            if (o != null) {
                checkLayout = (CheckLayout) o;
                setupSpinner();
                layoutField.setText(file);
            }
        }
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            closeDialog();
        } else if (e.getSource() == printButton) {
            if (checkLayout != null) {
                returnStatus = true;
            } else {
                returnStatus = false;
            }
            closeDialog();
        } else if (e.getSource() == selectButton) {
            selectLayout();
        }
    }
}
