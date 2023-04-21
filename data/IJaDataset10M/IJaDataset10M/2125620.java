package com.genia.toolbox.projects.toolbox_basics_project.spring.manager.impl;

import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.FileContent;
import com.genia.toolbox.projects.toolbox_basics_project.spring.manager.GwtFileContentManager;
import com.genia.toolbox.web.gwt.basics.bean.FileInformation;
import com.genia.toolbox.web.gwt.basics.manager.GwtFileUploadManager;

/**
 * implementation of {@link GwtFileContentManager}.
 */
public abstract class AbstractGwtFileContentManager extends FileContentManagerImpl implements GwtFileContentManager {

    /**
   * the {@link GwtFileUploadManager} to use.
   */
    private GwtFileUploadManager gwtFileUploadManager;

    /**
   * generate a {@link FileInformation} for an existing {@link FileContent}.
   * 
   * @param fileContent
   *          the {@link FileContent} to translate
   * @return the session identifier of the {@link FileInformation} that contains
   *         the given {@link FileContent}
   * @throws TechnicalException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.GwtFileContentManager#getFileInformation(com.genia.toolbox.projects.toolbox_basics_project.bean.model.FileContent)
   */
    public Long getFileInformation(FileContent fileContent) throws TechnicalException {
        FileInformation fileInformation = createFileInformation();
        fileInformation.setDataContainer(getDataContainer(fileContent));
        fileInformation.setPermanentIdentifier(fileContent.getIdentifier());
        return getGwtFileUploadManager().registerFileInformation(fileInformation);
    }

    /**
   * factory method for a {@link FileInformation}.
   * 
   * @return a new {@link FileInformation}
   */
    public abstract FileInformation createFileInformation();

    /**
   * getter for the gwtFileUploadManager property.
   * 
   * @return the gwtFileUploadManager
   */
    public GwtFileUploadManager getGwtFileUploadManager() {
        return gwtFileUploadManager;
    }

    /**
   * setter for the gwtFileUploadManager property.
   * 
   * @param gwtFileUploadManager
   *          the gwtFileUploadManager to set
   */
    public void setGwtFileUploadManager(GwtFileUploadManager gwtFileUploadManager) {
        this.gwtFileUploadManager = gwtFileUploadManager;
    }
}
