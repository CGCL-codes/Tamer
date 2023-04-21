package org.plazmaforge.studio.reportviewer.actions;

import java.io.File;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.plazmaforge.studio.reportengine.export.ExportFactory;
import org.plazmaforge.studio.reportviewer.IReportViewer;

/** 
 * @author Oleh Hapon
 * $Id: ExportAsRtfAction.java,v 1.2 2010/04/28 06:44:06 ohapon Exp $
 */
public class ExportAsRtfAction extends AbstractExportAction {

    private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(ExportAsRtfAction.class, "images/save.gif");

    private static final ImageDescriptor DISABLED_ICON = ImageDescriptor.createFromFile(ExportAsRtfAction.class, "images/saved.gif");

    /**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
    public ExportAsRtfAction(IReportViewer viewer) {
        super(viewer);
        setText(Messages.getString("ExportAsRtfAction.label"));
        setToolTipText(Messages.getString("ExportAsRtfAction.tooltip"));
        setImageDescriptor(ICON);
        setDisabledImageDescriptor(DISABLED_ICON);
        setFileExtensions(new String[] { "*.rtf" });
        setFilterNames(new String[] { Messages.getString("ExportAsRtfAction.filterName") });
        setDefaultFileExtension("rtf");
    }

    /**
	 * @see org.plazmaforge.studio.reportviewer.actions.AbstractExportAction#exportWithProgress(java.io.File, net.sf.jasperreports.engine.export.JRExportProgressMonitor)
	 */
    protected void exportWithProgress(File file, JRExportProgressMonitor monitor) throws JRException {
        ExportFactory.getExporter("RTF", getReportViewer().getDocument(), file, monitor).exportReport();
    }
}
