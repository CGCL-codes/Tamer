package org.jfree.report.modules.gui.swing.pdf;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.jfree.report.ReportConfigurationException;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.swing.common.AbstractExportActionPlugin;
import org.jfree.report.modules.gui.swing.common.SwingGuiContext;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 02.12.2006, 15:27:01
 *
 * @author Thomas Morgner
 */
public class PdfExportActionPlugin extends AbstractExportActionPlugin {

    private ResourceBundleSupport resources;

    private static final String EXPORT_DIALOG_KEY = "org.jfree.report.modules.gui.swing.pdf.ExportDialog";

    public PdfExportActionPlugin() {
    }

    protected String getConfigurationPrefix() {
        return "org.jfree.report.modules.gui.swing.pdf.export.";
    }

    public boolean initialize(SwingGuiContext context) {
        super.initialize(context);
        resources = new ResourceBundleSupport(context.getLocale(), SwingPdfModule.BUNDLE_NAME);
        return true;
    }

    /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
    public String getDisplayName() {
        return resources.getString("action.export-to-pdf.name");
    }

    /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
    public String getShortDescription() {
        return resources.getString("action.export-to-pdf.description");
    }

    /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
    public Icon getSmallIcon() {
        return null;
    }

    /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
    public Icon getLargeIcon() {
        return null;
    }

    /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
    public KeyStroke getAcceleratorKey() {
        return resources.getOptionalKeyStroke("action.export-to-pdf.accelerator");
    }

    /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
    public Integer getMnemonicKey() {
        return resources.getOptionalMnemonic("action.export-to-pdf.mnemonic");
    }

    /**
   * Exports a report.
   *
   * @param report the report.
   * @return A boolean.
   */
    public boolean performExport(ReportJob job) {
        if (performShowExportDialog(job, EXPORT_DIALOG_KEY) == false) {
            return false;
        }
        try {
            final PdfExportTask task = new PdfExportTask(job);
            Thread worker = new Thread(task);
            setStatusText("Started Job");
            worker.start();
            return true;
        } catch (ReportConfigurationException e) {
            setStatusText("Failed to configure the export task.");
            return false;
        }
    }
}
