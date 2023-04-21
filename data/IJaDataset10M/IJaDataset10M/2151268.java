package org.xaware.ide.xadev.tools.gui.packagetool;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * @author hcurtis
 * 
 */
public class NavigatorPackageTool extends PackageTool {

    /**
     * @param parent
     * @param title
     */
    public NavigatorPackageTool(final Shell parent, final String title) {
        super(parent, title);
    }

    /**
     * 
     */
    public NavigatorPackageTool() {
        super();
    }

    public PackageBuilder getNavigatorPackageBuilder() {
        if (pkgBuilder == null) {
            pkgBuilder = new PackageBuilder(this, nameVsFullName, fileTableModel, contentProvider);
        }
        PackageBuilder.progressMonitorRunning = false;
        return pkgBuilder;
    }

    /**
     * Set the destination package archive path
     * 
     * @param filename
     */
    public void setPackageFile(final String filename) {
        packageTF.setText(filename);
        packageFileName = filename;
    }

    /**
     * Accessor to the cancel button on the PackageTool dialog
     * 
     * @return cancel Button
     */
    public Button getCancelButton() {
        return cancelButton;
    }
}
