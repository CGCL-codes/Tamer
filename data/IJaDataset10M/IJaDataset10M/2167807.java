package org.hibnet.lune.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.hibnet.lune.core.local.FilesInfo;
import org.hibnet.lune.ui.format.FileInfoFormatComposite;

public class IndexFileView extends ViewPart {

    /** the index file view ID */
    public static final String ID = "org.hibnet.lune.ui.view.indexFile";

    private FilesInfo infos;

    private FileInfoFormatComposite composite;

    public void setInfos(FilesInfo infos) {
        this.infos = infos;
        setPartName(infos.getName());
        composite.showFileInfo(infos);
    }

    public FilesInfo getInfos() {
        return infos;
    }

    @Override
    public void createPartControl(Composite parent) {
        composite = new FileInfoFormatComposite(parent, SWT.NONE);
    }

    @Override
    public void setFocus() {
        composite.setFocus();
    }
}
