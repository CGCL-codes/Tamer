package org.dms.wicket.repository.page;

import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.upload.FileItem;
import org.dms.wicket.repository.panel.RepositoryPanel;
import org.xaloon.wicket.component.mounting.MountPage;
import org.xaloon.wicket.component.repository.FileRepository;
import org.xaloon.wicket.component.uploadify.UploadifyFileProcessPage;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas racelis
 */
@MountPage(path = "/process")
public class UploadFilePage extends UploadifyFileProcessPage {

    @SpringBean
    private FileRepository fileRepository;

    public UploadFilePage(PageParameters params) {
        super(params);
    }

    @Override
    protected void processFileItem(FileItem fi) throws Exception {
        String name = fi.getName();
        fileRepository.storeFile(RepositoryPanel.PARENT_NAME + name.substring(0, name.indexOf(".")), name, "images/jpeg", fi.getInputStream());
    }
}
