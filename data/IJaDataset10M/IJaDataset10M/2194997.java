package org.kalypso.nofdpidss.ui.application.commands.crosssection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;

/**
 * @author gernot
 */
public class SaveCrossSectionAction implements ICoreRunnableWithProgress {

    private static final Map<String, Integer> EXT_MAP = new HashMap<String, Integer>();

    static {
        EXT_MAP.put("bmp", SWT.IMAGE_BMP);
        EXT_MAP.put("ico", SWT.IMAGE_ICO);
        EXT_MAP.put("jpg", SWT.IMAGE_JPEG);
        EXT_MAP.put("gif", SWT.IMAGE_GIF);
        EXT_MAP.put("png", SWT.IMAGE_PNG);
        EXT_MAP.put("tif", SWT.IMAGE_TIFF);
    }

    private File m_target;

    public SaveCrossSectionAction() {
        throw new NotImplementedException();
    }

    /**
   * @see org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress#execute(org.eclipse.core.runtime.IProgressMonitor)
   */
    public IStatus execute(final IProgressMonitor monitor) {
        throw new NotImplementedException();
    }

    public File getTargetFile() {
        return m_target;
    }
}
