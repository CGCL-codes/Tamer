package jgnash.ui.archive;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;
import javax.swing.*;
import javax.swing.text.*;
import jgnash.ui.components.*;
import jgnash.ui.wizard.*;
import jgnash.ui.util.*;

/**
 * $Id: ArchiveCutOffDate.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author  Craig Cavanaugh
 */
public class ArchiveCutOffDate extends JPanel implements WizardPage {

    private UIResource rb = (UIResource) UIResource.get();

    private DatePanel dateField;

    private ArchiveObject archiveObject;

    private JEditorPane helpPane;

    public ArchiveCutOffDate(ArchiveObject archiveObject) {
        this.archiveObject = archiveObject;
        layoutMainPanel();
    }

    private void initComponents() {
        helpPane = new JEditorPane();
        helpPane.setEditable(false);
        helpPane.setEditorKit(new StyledEditorKit());
        helpPane.setBackground(getBackground());
        helpPane.setText(TextResource.getString("ArchiveDate.txt"));
        dateField = new DatePanel();
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("p, 8dlu, f:d:g", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        builder.appendSeparator(rb.getString("Title.CutOffDate"));
        builder.nextLine();
        builder.appendRelatedComponentsGapRow();
        builder.nextLine();
        builder.append(helpPane, 3);
        builder.nextLine();
        builder.appendRelatedComponentsGapRow();
        builder.nextLine();
        builder.appendRow("f:p");
        builder.append(rb.getString("Label.Date"), dateField);
    }

    public boolean validatePage() {
        archiveObject.setCutOffDate(dateField.getDate());
        return true;
    }

    /**
     * toString must return a valid description for this page that will
     * appear in the task list of the WizardDialog
     */
    public String toString() {
        return "3. " + rb.getString("Title.CutOffDate");
    }
}
