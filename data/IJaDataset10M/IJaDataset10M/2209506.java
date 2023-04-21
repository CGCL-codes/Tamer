package de.mpiwg.vspace.conversion.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import de.mpiwg.vspace.common.constants.PropertyKeys;
import de.mpiwg.vspace.common.constants.VSpaceExtensions;
import de.mpiwg.vspace.common.constants.VSpaceFilenames;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.common.properties.CommonPropertiesProvider;
import de.mpiwg.vspace.conversion.extension.IImportConverter;
import de.mpiwg.vspace.conversion.extension.IImportWizardPage;
import de.mpiwg.vspace.conversion.extension.internal.ExtensionProvider;
import de.mpiwg.vspace.conversion.extension.internal.WrappedImportConverter;
import de.mpiwg.vspace.conversion.internal.ProjectCopier;
import de.mpiwg.vspace.conversion.util.PropertyHandler;
import de.mpiwg.vspace.diagram.editor.ExhibitionEditorHelper;
import de.mpiwg.vspace.diagram.part.ExhibitionDiagramEditorPlugin;
import de.mpiwg.vspace.extension.ExceptionHandlingService;

public class ImportRunnable implements IRunnableWithProgress {

    private Map<String, IImportWizardPage> pagesMap;

    private ProjectPage projectPage;

    private CopyTargetPage copyTargetPage;

    public ImportRunnable(ProjectPage ppage, CopyTargetPage cpage, Map<String, IImportWizardPage> pagesMap) {
        this.projectPage = ppage;
        this.copyTargetPage = cpage;
        this.pagesMap = pagesMap;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        boolean successfulConverted = false;
        IFile diagramFile = null;
        try {
            monitor.beginTask(PropertyHandler.getInstance().getProperty("_import_monitor_copy"), 4);
            ProjectCopier copier = new ProjectCopier();
            String projectPath = projectPage.getProjectPath();
            File projectFile = new File(projectPath);
            if (!projectFile.exists() || !projectFile.isDirectory()) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_project_not_exists"));
            String targetPath = copyTargetPage.getCopyTargetPath();
            File targetFile = new File(targetPath);
            if (targetFile.exists()) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_target_exists"));
            boolean targetCreated = targetFile.mkdir();
            if (!targetCreated) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_target_not_created"));
            boolean success = copier.copyProject(projectFile, targetPath, monitor);
            if (!success) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_copy"));
            monitor.worked(1);
            monitor.setTaskName(PropertyHandler.getInstance().getProperty("_import_monitor_convert"));
            List<WrappedImportConverter> converter = ExtensionProvider.INSTANCE.getImportConverter();
            if ((converter == null) || (converter.size() == 0)) return;
            Collections.sort(converter, new PriorityComparator());
            List<File> additionalFiles = new ArrayList<File>();
            for (WrappedImportConverter c : converter) {
                IImportConverter iconverter = c.getImportConverter();
                List<String> ids = c.getPageIds();
                if (ids == null) iconverter.convert(targetFile, null); else {
                    List<IImportWizardPage> pages = new ArrayList<IImportWizardPage>();
                    for (String id : ids) {
                        IImportWizardPage p = pagesMap.get(id);
                        if (p != null) pages.add(p);
                    }
                    iconverter.convert(targetFile, pages);
                }
                success = true;
                File[] files = iconverter.getFilesToBeCopied();
                if (files != null) {
                    for (File f : files) {
                        success = success && copier.copyFiles(f, targetPath, monitor);
                        additionalFiles.add(f);
                    }
                }
                if (!success) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_copy"));
            }
            ProjectManager.getInstance().clearTempFolder(targetFile);
            monitor.worked(1);
            monitor.setTaskName(PropertyHandler.getInstance().getProperty("_import_monitor_create_project"));
            String newProjectName = copyTargetPage.getTargetFolderName();
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(newProjectName);
            if (!project.exists()) {
                try {
                    project.create(null);
                    if (!project.isOpen()) project.open(null);
                } catch (CoreException e1) {
                    success = false;
                    e1.printStackTrace();
                }
            }
            diagramFile = project.getFile(VSpaceFilenames.DIAGRAM_FILENAME + "." + VSpaceExtensions.DIAGRAM_EXTENSION);
            if (!diagramFile.exists()) {
                try {
                    diagramFile.createLink(new Path(VSpaceFilenames.DIAGRAM_FILENAME + "." + VSpaceExtensions.DIAGRAM_EXTENSION), IResource.ALLOW_MISSING_LOCAL, null);
                } catch (CoreException e) {
                    success = false;
                    e.printStackTrace();
                }
            }
            IFile modelFile = project.getFile(VSpaceFilenames.MODEL_FILENAME + "." + VSpaceExtensions.MODEL_EXTENSION);
            if (!modelFile.exists()) {
                try {
                    modelFile.createLink(new Path(VSpaceFilenames.MODEL_FILENAME + "." + VSpaceExtensions.MODEL_EXTENSION), IResource.ALLOW_MISSING_LOCAL, null);
                } catch (CoreException e) {
                    success = false;
                    e.printStackTrace();
                }
            }
            File[] moduleFiles = targetFile.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith("." + VSpaceExtensions.MODULE_EXTENSION)) return true;
                    return false;
                }
            });
            if (moduleFiles != null) {
                for (File f : moduleFiles) {
                    IFile moduleFile = project.getFile(f.getName());
                    if (!moduleFile.exists()) {
                        try {
                            moduleFile.createLink(new Path(f.getName()), IResource.ALLOW_MISSING_LOCAL, null);
                        } catch (CoreException e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }
            }
            File[] mapDiagramFiles = targetFile.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith("." + VSpaceExtensions.OVERVIEW_MAP_DIAGRAM_EXTENSION)) return true;
                    return false;
                }
            });
            if (mapDiagramFiles != null) {
                for (File f : mapDiagramFiles) {
                    IFile mapDFile = project.getFile(f.getName());
                    if (!mapDFile.exists()) {
                        try {
                            mapDFile.createLink(new Path(f.getName()), IResource.ALLOW_MISSING_LOCAL, null);
                        } catch (CoreException e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }
            }
            File[] mapFiles = targetFile.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith("." + VSpaceExtensions.OVERVIEW_MAP_EXTENSION)) return true;
                    return false;
                }
            });
            if (mapFiles != null) {
                for (File f : mapFiles) {
                    IFile mapFile = project.getFile(f.getName());
                    if (!mapFile.exists()) {
                        try {
                            mapFile.createLink(new Path(f.getName()), IResource.ALLOW_MISSING_LOCAL, null);
                        } catch (CoreException e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (File f : additionalFiles) {
                if (f.isFile()) {
                    IFile fFile = project.getFile(f.getName());
                    if (!fFile.exists()) {
                        try {
                            fFile.createLink(new Path(f.getName()), IResource.ALLOW_MISSING_LOCAL, null);
                        } catch (CoreException e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }
                if (f.isDirectory()) {
                    IFolder folder = project.getFolder(f.getName());
                    if (!folder.exists()) {
                        try {
                            folder.createLink(new Path(f.getName()), IResource.ALLOW_MISSING_LOCAL, null);
                        } catch (CoreException e) {
                            success = false;
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!success) throw new ConversionException(PropertyHandler.getInstance().getProperty("_import_conversion_error_creating_project"));
            monitor.worked(1);
            successfulConverted = true;
        } catch (ConversionException e) {
            final ConversionException ce = e;
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                public void run() {
                    Shell[] shells = PlatformUI.getWorkbench().getDisplay().getShells();
                    Shell currentShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                    if (currentShell == null) if (shells.length > 0) currentShell = shells[0]; else return;
                    MessageBox messageBox = new MessageBox(currentShell, SWT.OK | SWT.ICON_ERROR);
                    messageBox.setText(PropertyHandler.getInstance().getProperty("_import_conversion_error_title"));
                    messageBox.setMessage(ce.getMessage());
                    messageBox.open();
                }
            });
        } finally {
            monitor.done();
        }
        if (successfulConverted) {
            final IFile diagramFinalFile = diagramFile;
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                public void run() {
                    Shell[] shells = PlatformUI.getWorkbench().getDisplay().getShells();
                    Shell currentShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                    if (currentShell == null) if (shells.length > 0) currentShell = shells[0]; else return;
                    MessageDialog.openInformation(currentShell, PropertyHandler.getInstance().getProperty("_import_conversion_success_title"), PropertyHandler.getInstance().getProperty("_import_conversion_success"));
                    File projectFolder = new File(copyTargetPage.getCopyTargetPath());
                    if (!projectFolder.isDirectory()) return;
                    if (projectFolder.exists()) {
                        File propFile = new File(projectFolder.getAbsolutePath() + File.separator + CommonPropertiesProvider.PROPERTIES_FILENAME);
                        if (!propFile.exists()) try {
                            propFile.createNewFile();
                        } catch (IOException e1) {
                            ExceptionHandlingService.INSTANCE.handleException(e1);
                        }
                        FileInputStream fis = null;
                        Properties properties = new Properties();
                        try {
                            fis = new FileInputStream(propFile);
                        } catch (FileNotFoundException e) {
                            ExceptionHandlingService.INSTANCE.handleException(e);
                        }
                        try {
                            properties.load(fis);
                            String version = "";
                            Object o = Platform.getBundle(ExhibitionDiagramEditorPlugin.ID).getHeaders().get("Bundle-Version");
                            if ((o != null) && (o instanceof String)) version = o.toString();
                            properties.setProperty(PropertyKeys.VERSION, version);
                        } catch (IOException e) {
                            ExceptionHandlingService.INSTANCE.handleException(e);
                        }
                        try {
                            properties.store(new FileOutputStream(propFile), "");
                        } catch (FileNotFoundException e) {
                            ExceptionHandlingService.INSTANCE.handleException(e);
                        } catch (IOException e) {
                            ExceptionHandlingService.INSTANCE.handleException(e);
                        }
                        ExhibitionEditorHelper.openDiagram(diagramFinalFile);
                    }
                }
            });
        }
    }
}
