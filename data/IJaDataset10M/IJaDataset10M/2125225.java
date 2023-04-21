package org.jtools.siterenderer;

import javax.tools.Tool;
import org.jtools.filemanager.FileManager;

public interface SiteRendererTool extends Tool {

    void setSiteDescriptor(SiteDescriptor siteDescriptor);

    SiteDescriptor getSiteDescriptor();

    void setSiteDescriptorPath(String path);

    String getSiteDescriptorPath();

    void setSiteDescriptorClass(Class<? extends SiteDescriptor> siteDescriptorClass);

    Class<? extends SiteDescriptor> getSiteDescriptorClass();

    FileManager getFileManager();

    void setFileManager(FileManager fileManager);

    SiteRendererTask getTask();
}
