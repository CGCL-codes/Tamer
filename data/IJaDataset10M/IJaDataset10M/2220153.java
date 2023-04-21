package org.plog4u.wiki.export.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.TreeSet;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.plog4u.wiki.actions.mediawiki.exceptions.ConfigurationException;
import org.plog4u.wiki.builder.CreatePageAction;
import org.plog4u.wiki.editor.WikiEditorPlugin;
import org.plog4u.wiki.preferences.Util;

public final class WikiHTMLExporter {

    public static final String HTML_EXTENSION = ".html";

    public static final String WORKSPACE = "workspace";

    private TreeSet index;

    public WikiHTMLExporter() {
        index = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    }

    public void export(IContainer folder, String exportDirectoryName, String srcBasePath, IProgressMonitor monitor) throws IOException, CoreException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        IResource[] resources = folder.members(IResource.FILE);
        String templateFileName = Util.getExportTemplate(folder);
        for (int i = 0; i < resources.length; i++) {
            if (resources[i].exists()) {
                if (resources[i] instanceof IFile) {
                    monitor.subTask(WikiEditorPlugin.getResourceString("Export.exportFile") + resources[i].getLocation());
                    try {
                        CreatePageAction.createPage(templateFileName, (IFile) resources[i], exportDirectoryName, srcBasePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    monitor.worked(1);
                } else if (resources[i] instanceof IFolder) {
                    monitor.subTask(WikiEditorPlugin.getResourceString("Export.exportFolder") + resources[i].getLocation());
                    export((IFolder) resources[i], exportDirectoryName, srcBasePath, monitor);
                    monitor.worked(1);
                }
            }
        }
    }

    public void exportImages(IContainer folder, String exportDirectoryName, String srcBasePath, IProgressMonitor monitor) throws IOException, CoreException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        IResource[] resources = folder.members(IResource.FILE);
        IFile file;
        File srcFile;
        String extension;
        for (int i = 0; i < resources.length; i++) {
            if (resources[i].exists()) {
                if (resources[i] instanceof IFile) {
                    monitor.subTask(WikiEditorPlugin.getResourceString("Export.exportFile") + resources[i].getLocation());
                    file = (IFile) resources[i];
                    extension = file.getLocation().getFileExtension();
                    if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                        try {
                            String destFilename = Util.getHTMLFileName(file, exportDirectoryName, srcBasePath);
                            srcFile = new File(file.getLocation().toOSString());
                            if (srcFile.exists()) {
                                copyFile(srcFile, new File(destFilename));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    monitor.worked(1);
                } else if (resources[i] instanceof IFolder) {
                    monitor.subTask(WikiEditorPlugin.getResourceString("Export.exportFolder") + resources[i].getLocation());
                    exportImages((IFolder) resources[i], exportDirectoryName, srcBasePath, monitor);
                    monitor.worked(1);
                }
            }
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel destinationChannel = new FileOutputStream(dest).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }

    private boolean isWikiFile(IResource resource) {
        return resource instanceof IFile && resource.getFileExtension().equals("wp");
    }
}
