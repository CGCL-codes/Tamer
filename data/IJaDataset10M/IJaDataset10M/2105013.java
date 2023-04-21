package jgnash.ui.qif;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;
import java.awt.event.*;
import javax.swing.*;
import jgnash.convert.qif.*;
import jgnash.ui.wizard.*;
import jgnash.ui.util.*;

/** Wizard Page for a partial qif import.
 * <p>
 * $Id: PartialSummary.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class PartialSummary extends JPanel implements WizardPage {

    PartialDialog dlg;

    QifAccount qAcc;

    private UIResource rb = (UIResource) UIResource.get();

    private JLabel destLabel;

    private JLabel transCount;

    public PartialSummary(QifAccount qAcc, PartialDialog dlg) {
        this.qAcc = qAcc;
        this.dlg = dlg;
        layoutMainPanel();
    }

    private void initComponents() {
        destLabel = new JLabel("Account");
        transCount = new JLabel("0");
        addComponentListener(new ComponentAdapter() {

            public void componentShown(ComponentEvent evt) {
                refreshInfo();
            }
        });
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("p, 8dlu, d:g", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        builder.appendSeparator(rb.getString("Title.ImpSum"));
        builder.nextLine();
        builder.appendRelatedComponentsGapRow();
        builder.nextLine();
        builder.append(rb.getString("Label.DestAccount"), destLabel);
        builder.append(rb.getString("Label.NumTrans"), transCount);
    }

    public boolean validatePage() {
        return true;
    }

    void refreshInfo() {
        destLabel.setText(dlg.getAccount().getPathName());
        transCount.setText(Integer.toString(qAcc.items.size()));
    }

    /** toString must return a valid description for this page that will
     * appear in the task list of the WizardDialog
     */
    public String toString() {
        return "3. " + rb.getString("Title.ImpSum");
    }
}
