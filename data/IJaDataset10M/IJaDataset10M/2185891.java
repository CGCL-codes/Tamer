package com.apelon.beans.dts.subset.export;

import com.apelon.beans.dts.plugin.subset.export.SubsetDataExporter;
import com.apelon.common.log4j.Categories;
import com.apelon.common.util.SwingWorker;
import com.apelon.wizard.ApelonStandardWizardPanel;
import com.apelon.wizard.Wizard;
import com.apelon.wizard.WizardPanel;
import com.apelon.wizard.WizardContext;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * This panel shows the summary of the export which will be performed.
 * FinishExportWizardPanel is called next.
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public class PerformExportWizardPanel extends SubsetExportStandardWizardPanel {

    private JEditorPane summaryTextPane;

    /**
   * Creates a new <code>PerformExportWizardPanel</code>
   */
    public PerformExportWizardPanel() {
        try {
            displayPanels();
        } catch (Exception e) {
            Categories.uiView().error("Error initializing PerformExportWizardPanel", e);
        }
    }

    /**
   * Returns the context message image resource which is displayed in the Graphic panel
   * @return String
   */
    public String getContextImageResource() {
        return "/com/apelon/beans/apelres/dts/subsetexport/ready.gif";
    }

    /**
   * Right panel (Content Panel) of the wizard.
   * This includes
   * -- A JEditorPane to display export summary
   * -- A JLabel to display instruction for the user
   *
   * @return JPanel displaying contents
   */
    public JPanel getContentPanel() {
        String nextMsg = "<html><font size=4 face=arial>&nbsp;Click Next to perform export.</font></html>";
        summaryTextPane = new JEditorPane("text/html", "");
        summaryTextPane.setEditable(false);
        summaryTextPane.setOpaque(false);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(summaryTextPane, BorderLayout.NORTH);
        contentPanel.add(new JLabel(nextMsg), BorderLayout.SOUTH);
        return contentPanel;
    }

    private String getExportSummary() {
        return (String) getWizardContext().getAttribute(SubsetExportModel.ATTRIB_EXPORT_SUMMARY);
    }

    /**
   * Called when the panel is set. Changes "Next" button to "Export". Displays the
   * export summary.
   */
    public void display() {
        summaryTextPane.setText(getExportSummary());
        revalidate();
    }

    /**
   * Is there be a next panel?
   *
   * @return true since there is a panel to move to next
   */
    public boolean hasNext() {
        return true;
    }

    /**
   * Called to validate the panel before moving to next panel. Subset export is done here.
   *
   * @param list a List of error messages to be displayed.
   *
   * @return true if the panel is valid,
   */
    public boolean validateNext(java.util.List list) {
        return true;
    }

    /**
   * Get the next panel to go to.
   */
    public WizardPanel next() {
        return new FinishExportWizardPanel();
    }

    /**
   * Is there a help page?
   * @return boolean
   */
    public boolean hasHelp() {
        return true;
    }

    /**
   * Show the help page.
   */
    public void help() {
        try {
            com.apelon.beans.dts.subset.export.Utils.setHelpPage("dtssubsets.htm");
        } catch (Exception e) {
        }
    }
}
