package com.ibm.celldt.sputiming.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IResourceConfiguration;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.ui.CElementLabelProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import com.ibm.celldt.preferences.ui.PreferenceConstants;
import com.ibm.celldt.sputiming.core.LaunchConfigurationConstants;
import com.ibm.celldt.sputiming.message.GUIMessages;

public class SPUTimingMainTab extends AbstractLaunchConfigurationTab {

    protected Label fMessageLabel;

    protected Label fCompilerNameLabel, fCompilerFlagsLabel;

    protected Text fCompilerNameText, fCompilerFlagsText;

    protected Button fCompilerCommandSearch;

    protected Label fProfilerTargetLabel;

    protected Text fProfilerTargetText;

    protected Button fProfilerTargetSearch;

    protected Label fProfilerProjectLabel;

    protected Text fProfilerProjectText;

    protected Button fProfilerProjectSearch;

    protected Label fSPUTimingExecutableLabel;

    protected Text fSPUTimingExecutableText;

    protected Label fSPUTimingSourceFileLabel;

    protected Text fSPUTimingAssemblyFileText;

    private Label fSPUTimingArchitectureLabel;

    private Combo fSPUTimingArchitectureCombo;

    protected ModifyListener textModifyListener;

    public SPUTimingMainTab() {
        super();
        textModifyListener = new TextBoxListener();
    }

    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        GridLayout topLayout = new GridLayout();
        topLayout.marginHeight = 0;
        topLayout.verticalSpacing = 1;
        comp.setLayout(topLayout);
        createMessageGroup(comp, 1);
        createProfilerGroup(comp, 1);
        createCompilationGroup(comp, 1);
        createSPUTimingGroup(comp, 1);
    }

    /**
	 * Generates a group containing a simple message in a label
	 * 
	 * @param comp
	 * @param i
	 */
    protected void createMessageGroup(Composite parent, int colSpan) {
    }

    protected void handleSearchSourceButtonAction() {
        if (getCProject() == null) {
            MessageDialog.openInformation(getShell(), "Project required!", "Enter project before searching for source file");
            return;
        }
        IFile csource = selectCSource();
        if (csource == null) return;
        fProfilerTargetText.setText(csource.getProjectRelativePath().toOSString());
    }

    void createProfilerGroup(Composite parent, int colSpan) {
        Group projComp = new Group(parent, SWT.SHADOW_ETCHED_IN);
        projComp.setText(GUIMessages.SPUTimingMainTab_ProjectGroup_GroupLabel);
        GridLayout projLayout = new GridLayout();
        projLayout.verticalSpacing = 0;
        projLayout.numColumns = 2;
        projLayout.marginTop = 0;
        projLayout.marginBottom = 1;
        projLayout.marginWidth = 3;
        projComp.setLayout(projLayout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = colSpan;
        projComp.setLayoutData(gd);
        fProfilerProjectLabel = new Label(projComp, SWT.NONE);
        fProfilerProjectLabel.setText(GUIMessages.SPUTimingMainTab_ProjectGroup_ProjectLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fProfilerProjectLabel.setLayoutData(gd);
        fProfilerProjectText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fProfilerProjectText.setLayoutData(gd);
        fProfilerProjectText.addModifyListener(textModifyListener);
        fProfilerProjectSearch = createPushButton(projComp, GUIMessages.SPUTimingMainTab_ProjectGroup_SearchProjectButton, null);
        fProfilerProjectSearch.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent evt) {
                handleSearchProjectButtonAction();
                updateLaunchConfigurationDialog();
            }
        });
        fProfilerTargetLabel = new Label(projComp, SWT.NONE);
        fProfilerTargetLabel.setText(GUIMessages.SPUTimingMainTab_ProjectGroup_SourceLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fProfilerTargetLabel.setLayoutData(gd);
        fProfilerTargetText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fProfilerTargetText.setLayoutData(gd);
        fProfilerTargetText.addModifyListener(textModifyListener);
        fProfilerTargetSearch = createPushButton(projComp, GUIMessages.SPUTimingMainTab_ProjectGroup_SearchTargetButton, null);
        fProfilerTargetSearch.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent evt) {
                handleSearchSourceButtonAction();
                updateLaunchConfigurationDialog();
            }
        });
    }

    /**
	 * Show a dialog that lets the user select a project. This in turn provides
	 * context for the main type, allowing the user to key a main type name, or
	 * constraining the search for main types to the specified project.
	 * 
	 * Also, select the default compiler and compiler options and fill the
	 * appropriate textbox.
	 */
    protected void handleSearchProjectButtonAction() {
        ICProject cproject = selectCProject();
        if (cproject == null) {
            return;
        }
        String projectName = cproject.getElementName();
        fProfilerProjectText.setText(projectName);
    }

    /**
	 * Realize a C Project selection dialog and return the first selected
	 * project, or null if there was none.
	 */
    protected ICProject selectCProject() {
        try {
            ICProject[] projects = getCProjects();
            ILabelProvider labelProvider = new CElementLabelProvider();
            ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
            dialog.setTitle("Projects");
            dialog.setMessage("Select the project whose file will be profiled");
            dialog.setElements(projects);
            ICProject cProject = getCProject();
            if (cProject != null) {
                dialog.setInitialSelections(new Object[] { cProject });
            }
            if (dialog.open() == Window.OK) {
                return (ICProject) dialog.getFirstResult();
            }
        } catch (CModelException e) {
            System.out.println("CModelException " + e.getMessage());
        }
        return null;
    }

    protected void createCompilationGroup(Composite parent, int colSpan) {
        Group projComp = new Group(parent, SWT.SHADOW_ETCHED_IN);
        projComp.setText(GUIMessages.SPUTimingMainTab_CompilerGroup_GroupLabel);
        GridLayout projLayout = new GridLayout();
        projLayout.numColumns = 2;
        projLayout.marginTop = 0;
        projLayout.marginBottom = 1;
        projLayout.marginWidth = 3;
        projLayout.verticalSpacing = 0;
        projComp.setLayout(projLayout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = colSpan;
        projComp.setLayoutData(gd);
        fCompilerNameLabel = new Label(projComp, SWT.NONE);
        fCompilerNameLabel.setText(GUIMessages.SPUTimingMainTab_CompilerGroup_CompilerLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fCompilerNameLabel.setLayoutData(gd);
        fCompilerNameText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCompilerNameText.setLayoutData(gd);
        fCompilerNameText.addModifyListener(textModifyListener);
        fCompilerFlagsLabel = new Label(projComp, SWT.NONE);
        fCompilerFlagsLabel.setText(GUIMessages.SPUTimingMainTab_CompilerGroup_FlagsLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fCompilerFlagsLabel.setLayoutData(gd);
        fCompilerFlagsText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCompilerFlagsText.setLayoutData(gd);
        fCompilerFlagsText.addModifyListener(textModifyListener);
    }

    protected void createSPUTimingGroup(Composite parent, int colSpan) {
        Group projComp = new Group(parent, SWT.SHADOW_ETCHED_IN);
        projComp.setText(GUIMessages.SPUTimingMainTab_SPUTimingGroup_GroupLabel);
        GridLayout projLayout = new GridLayout();
        projLayout.numColumns = 2;
        projLayout.marginTop = 0;
        projLayout.marginBottom = 1;
        projLayout.marginWidth = 3;
        projLayout.verticalSpacing = 2;
        projComp.setLayout(projLayout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = colSpan;
        projComp.setLayoutData(gd);
        fSPUTimingExecutableLabel = new Label(projComp, SWT.NONE);
        fSPUTimingExecutableLabel.setText(GUIMessages.SPUTimingMainTab_SPUTimingGroup_SPUTimingLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fSPUTimingExecutableLabel.setLayoutData(gd);
        fSPUTimingExecutableText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fSPUTimingExecutableText.setLayoutData(gd);
        fSPUTimingExecutableText.addModifyListener(textModifyListener);
        gd = new GridData();
        gd.horizontalSpan = 2;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        fSPUTimingExecutableText.setLayoutData(gd);
        fSPUTimingSourceFileLabel = new Label(projComp, SWT.NONE);
        fSPUTimingSourceFileLabel.setText(GUIMessages.SPUTimingMainTab_SPUTimingGroup_AssemblyFileLabel);
        gd = new GridData();
        gd.horizontalSpan = 2;
        fSPUTimingSourceFileLabel.setLayoutData(gd);
        fSPUTimingAssemblyFileText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fSPUTimingAssemblyFileText.setLayoutData(gd);
        fSPUTimingAssemblyFileText.addModifyListener(textModifyListener);
        gd = new GridData();
        gd.horizontalSpan = 2;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        fSPUTimingAssemblyFileText.setLayoutData(gd);
        fSPUTimingArchitectureLabel = new Label(projComp, SWT.NONE);
        fSPUTimingArchitectureLabel.setText(GUIMessages.SPUTimingMainTab_SPUTimingGroup_ArchitectureComboLabel);
        fSPUTimingArchitectureCombo = new Combo(projComp, SWT.DROP_DOWN | SWT.READ_ONLY);
        fSPUTimingArchitectureCombo.add(GUIMessages.SPUTimingMainTab_SPUTimingGroup_ArchitectureCell);
        fSPUTimingArchitectureCombo.add(GUIMessages.SPUTimingMainTab_SPUTimingGroup_ArchitectureSoma);
        fSPUTimingArchitectureCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                updateLaunchConfigurationDialog();
            }
        });
    }

    /**
	 * Extract ICProject information from the ProjectText field
	 */
    protected ICProject getCProject() {
        String projectName = fProfilerProjectText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        ICProject cproj = CoreModel.getDefault().getCModel().getCProject(projectName);
        if (!cproj.exists()) return null;
        return cproj;
    }

    /**
	 * Return an array of ICProjects.
	 * 
	 * Filter projects. Allowed only those who dont have Build information
	 * (standard make) and those who have build information and project type
	 * which are for the spu (executable and library).
	 */
    protected ICProject[] getCProjects() throws CModelException {
        ICProject cproject[] = CoreModel.getDefault().getCModel().getCProjects();
        ArrayList list = new ArrayList(cproject.length);
        for (int i = 0; i < cproject.length; i++) {
            IProject project = cproject[i].getProject();
            if (ManagedBuildManager.canGetBuildInfo(project) == false) list.add(cproject[i]);
        }
        return (ICProject[]) list.toArray(new ICProject[list.size()]);
    }

    /**
	 * Create a dialog containing all sources of the project. Return the
	 * selected one.
	 */
    protected IFile selectCSource() {
        IFile[] sourcefiles = getCSources();
        if (sourcefiles == null) return null;
        ILabelProvider lblProvider = new CElementLabelProvider();
        ElementListSelectionDialog sourcedialog = new ElementListSelectionDialog(getShell(), lblProvider);
        sourcedialog.setTitle(GUIMessages.SPUTimingMainTab_SelectCSource_DialogTitle);
        sourcedialog.setMessage(GUIMessages.SPUTimingMainTab_SelectCSource_DialogMessage);
        sourcedialog.setElements(sourcefiles);
        if (sourcefiles != null) sourcedialog.setInitialSelections(new Object[] { sourcefiles[0] });
        if (sourcedialog.open() == Window.OK) return (IFile) sourcedialog.getFirstResult();
        return null;
    }

    protected IFile getCSource() {
        String fileName = fProfilerTargetText.getText().trim();
        if (fileName.length() < 1) {
            return null;
        }
        ICProject cproject = getCProject();
        if (cproject == null) return null;
        IFile cfile = cproject.getProject().getFile(fileName);
        if (!cfile.exists()) return null;
        return cfile;
    }

    protected IFile[] getCSources() {
        ICProject cproj = getCProject();
        LinkedList filelist = new LinkedList();
        IResource[] resourcelist;
        try {
            resourcelist = cproj.getProject().members();
        } catch (CoreException e1) {
            return null;
        }
        if (ManagedBuildManager.canGetBuildInfo(cproj.getProject())) {
            IManagedBuildInfo buildinfo = ManagedBuildManager.getBuildInfo(cproj.getProject());
            String[] toolidmatchinglist = new String[2];
            if (CoreModel.hasCNature(cproj.getProject())) {
                toolidmatchinglist[0] = "cell.spu.xl.c.compiler";
                toolidmatchinglist[1] = "cell.spu.gnu.c.compiler";
                if (CoreModel.hasCCNature(cproj.getProject())) {
                    toolidmatchinglist[0] = "cell.spu.xl.cpp.compiler";
                    toolidmatchinglist[1] = "cell.spu.gnu.cpp.compiler";
                }
            } else return null;
            ITool[] tools;
            tools = buildinfo.getDefaultConfiguration().getFilteredTools();
            HashSet extensionset = new HashSet();
            for (int j = 0; j < tools.length; j++) {
                ITool tool = tools[j];
                if ((tool.getId().indexOf(toolidmatchinglist[0]) != -1) || (tool.getId().indexOf(toolidmatchinglist[1]) != -1)) {
                    String[] extensions = tool.getAllInputExtensions();
                    for (int i = 0; i < extensions.length; i++) extensionset.add(extensions[i]);
                }
            }
            for (int i = 0; i < resourcelist.length; i++) {
                if (resourcelist[i] instanceof IFile) {
                    IFile tempfile = (IFile) resourcelist[i];
                    if (extensionset.contains(tempfile.getFileExtension())) filelist.add(tempfile);
                }
            }
        } else {
            try {
                filelist.addAll(collectSourceFiles(resourcelist));
            } catch (CoreException e) {
                System.out.println(GUIMessages.SPUTimingMainTab_GetCSources_SourceListError);
            }
        }
        IFile[] temp = new IFile[1];
        return (IFile[]) filelist.toArray(temp);
    }

    /**
	 * Loop over all IFile resources of a given resourcelist. If the resource is
	 * of the IFile kind, add it to the a list. If the resource is of the
	 * IDirectory kind, enter it and execute the algorithm again.
	 * 
	 * @return IFile [] The list of IFile kind resources.
	 * @throws CoreException
	 */
    protected LinkedList collectSourceFiles(IResource[] resourcelist) throws CoreException {
        LinkedList filelist = new LinkedList();
        for (int i = 0; i < resourcelist.length; i++) {
            if ((resourcelist[i] instanceof IFile) && (resourcelist[i].getFileExtension() != null) && isSourceFile((IFile) resourcelist[i])) {
                filelist.add((IFile) resourcelist[i]);
            } else if (resourcelist[i] instanceof IFolder) {
                IFolder tempfolder = (IFolder) resourcelist[i];
                IResource[] sublist = tempfolder.members();
                filelist.addAll(collectSourceFiles(sublist));
            }
        }
        return filelist;
    }

    /**
	 * Validate the source file
	 * 
	 * @return Boolean The IFile is a valid source file or not
	 */
    protected boolean isSourceFile(IFile file) {
        if (file.getFileExtension().equalsIgnoreCase("c") || file.getFileExtension().equalsIgnoreCase("c++") || file.getFileExtension().equalsIgnoreCase("cc") || file.getFileExtension().equalsIgnoreCase("cxx") || file.getFileExtension().equalsIgnoreCase("cpp")) return true;
        return false;
    }

    /**
	 * Return the compiler used by the resource (if it exists), or the compiler
	 * associated to the source file extension.
	 * 
	 * @return ITool
	 */
    protected ITool getCCompiler() {
        IProject project = getCProject().getProject();
        IFile csource = getCSource();
        if (ManagedBuildManager.canGetBuildInfo(project)) {
            IManagedBuildInfo buildinfo = ManagedBuildManager.getBuildInfo(project);
            IResourceConfiguration resconf = buildinfo.getDefaultConfiguration().getResourceConfiguration(csource.getFullPath().toOSString());
            if (resconf == null) return buildinfo.getToolFromInputExtension(csource.getFileExtension());
            return resconf.getTools()[0];
        } else return null;
    }

    /**
	 * Handles the pressing of the Search Compiler button
	 * 
	 * @author Richard Maciel
	 * @since 1.0
	 */
    protected void handleSearchCCompilerButtonAction() {
        if (getCProject() == null) {
            MessageDialog.openInformation(getShell(), "Project required!", "Enter project before searching for compiler information");
            return;
        }
        IFile source = getCSource();
        if (source == null) {
            MessageDialog.openInformation(getShell(), "Source file required!", "Enter source file before searching for compiler information");
            return;
        }
        ITool tool = getCCompiler();
        if (tool == null) {
            MessageDialog.openInformation(getShell(), "No managed build info!", "Cannot extract compiler information from a non-managed build project");
            return;
        }
        String toolcommand = tool.getToolCommand();
        if (toolcommand == null) fCompilerNameText.setText(""); else fCompilerNameText.setText(toolcommand);
        String flaglist = null;
        try {
            ITool assemblertool = ManagedBuildManager.getExtensionTool("celldt.managedbuild.tool.cell.spu.gnu.assembler");
            if (assemblertool == null) {
                MessageDialog.openInformation(getShell(), GUIMessages.SPUTimingMainTab_SearchCCompilerButtonAction_CDTErrorTitle, GUIMessages.SPUTimingMainTab_SearchCCompilerButtonAction_CDTErrorMessage);
                return;
            }
            String[] assemblerextlist = assemblertool.getAllInputExtensions();
            if (assemblerextlist.length == 0) {
                MessageDialog.openInformation(getShell(), GUIMessages.SPUTimingMainTab_SearchCCompilerButtonAction_CDTErrorTitle, GUIMessages.SPUTimingMainTab_SearchCCompilerButtonAction_CDTErrorMessage);
                return;
            }
            IManagedBuildInfo imbi = ManagedBuildManager.getBuildInfo(getCProject().getProject());
            String builddir = imbi.getConfigurationName();
            IPath outputfilenamewoext = source.getFullPath().removeFileExtension().makeRelative().removeFirstSegments(1);
            String outputextension = imbi.getOutputExtension(source.getFileExtension());
            IPath outputfilename = source.getLocation().removeLastSegments(1).addTrailingSeparator().append(builddir).addTrailingSeparator().append(outputfilenamewoext.toString() + "." + outputextension);
            flaglist = tool.getToolCommandFlagsString(source.getLocation(), outputfilename) + " -S";
        } catch (BuildException e) {
            System.out.println(GUIMessages.SPUTimingMainTab_SearchCCompilerButtonAction_FlagsFetchingMessage);
            fCompilerFlagsText.setText("");
        }
        if (flaglist != null) fCompilerFlagsText.setText(flaglist);
    }

    /**
	 * Extracts the SPUTiming executable name from the compiler ID info.
	 * 
	 * @return Name of the SPUTiming executable
	 * @author Richard Maciel
	 * @since 1.0
	 */
    protected String getSPUTimingExecutableName() {
        ITool ccompiler = getCCompiler();
        if (ccompiler != null) {
            return "spu_timing";
        }
        return null;
    }

    /**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 * @author Richard Maciel
	 * @since 1.0
	 */
    public boolean isValid(ILaunchConfiguration launchConfig) {
        setErrorMessage(null);
        setMessage(GUIMessages.SPUTimingMainTab_MessageGroup_);
        String projName = fProfilerProjectText.getText().trim();
        if (projName.length() == 0) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_NoProjectNameError);
            return false;
        }
        if (!ResourcesPlugin.getWorkspace().getRoot().getProject(projName).exists()) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_ProjectMustExistError);
            return false;
        }
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
        if (!project.isOpen()) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_ProjectMustBeOpened);
            return false;
        }
        String sourceName = fProfilerTargetText.getText().trim();
        if (sourceName.length() == 0) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_NoSourceFileError);
            return false;
        }
        if (!project.getFile(sourceName).exists()) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_SourceFileMustExistError);
            return false;
        }
        if (sourceName.equals(".") || sourceName.equals("..")) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_InvalidFilenameError);
            return false;
        }
        String compCmd = fCompilerNameText.getText().trim();
        if (compCmd.length() == 0) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_NoCompilerError);
            return false;
        }
        if (fCompilerFlagsText.getText().trim().length() == 0) {
            setMessage(GUIMessages.SPUTimingMainTab_IsValid_AssemblyFlagMessage + "flag to generate output file name " + "compatible with the assembly file name field");
        }
        String sputCommand = fSPUTimingExecutableText.getText().trim();
        File sputCmdFile = new File(sputCommand);
        if (!sputCmdFile.exists() || !sputCmdFile.isFile()) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_ValidSPUTimingPathError);
            return false;
        }
        String asmFile = fSPUTimingAssemblyFileText.getText().trim();
        if (asmFile.length() == 0) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_ValidAssemblyFilenameError);
            return false;
        }
        int index = fSPUTimingArchitectureCombo.getSelectionIndex();
        if (index == -1) {
            setErrorMessage(GUIMessages.SPUTimingMainTab_IsValid_ValidArchitectureError);
            return false;
        }
        return true;
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            fCompilerNameText.setText(configuration.getAttribute(LaunchConfigurationConstants.COMPILER_NAME, ""));
            fCompilerFlagsText.setText(configuration.getAttribute(LaunchConfigurationConstants.COMPILER_FLAGS, ""));
            fProfilerTargetText.setText(configuration.getAttribute(LaunchConfigurationConstants.SOURCE_FILE_NAME, ""));
            fProfilerProjectText.setText(configuration.getAttribute(LaunchConfigurationConstants.PROJECT_NAME, ""));
            fSPUTimingAssemblyFileText.setText(configuration.getAttribute(LaunchConfigurationConstants.ASSEMBLY_FILE_NAME, ""));
            if (configuration.getAttribute(LaunchConfigurationConstants.SPU_EXECUTABLE_NAME, "") == null || configuration.getAttribute(LaunchConfigurationConstants.SPU_EXECUTABLE_NAME, "") == "") {
                PreferenceConstants preferences = PreferenceConstants.getInstance();
                fSPUTimingExecutableText.setText(preferences.getTIMING_SPUBIN().toOSString());
            } else {
                fSPUTimingExecutableText.setText(configuration.getAttribute(LaunchConfigurationConstants.SPU_EXECUTABLE_NAME, ""));
            }
            if (configuration.getAttribute(LaunchConfigurationConstants.SPU_ARCH_TYPE, LaunchConfigurationConstants.SPU_ARCH_SPU).equals(LaunchConfigurationConstants.SPU_ARCH_SPU)) {
                fSPUTimingArchitectureCombo.select(0);
            } else if (configuration.getAttribute(LaunchConfigurationConstants.SPU_ARCH_TYPE, LaunchConfigurationConstants.SPU_ARCH_SPU).equals(LaunchConfigurationConstants.SPU_ARCH_SPUEFP)) {
                fSPUTimingArchitectureCombo.select(1);
            } else {
                fSPUTimingArchitectureCombo.select(0);
            }
        } catch (CoreException e) {
        }
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        PreferenceConstants preferences = PreferenceConstants.getInstance();
        configuration.setAttribute(LaunchConfigurationConstants.SPU_EXECUTABLE_NAME, preferences.getTIMING_SPUBIN().toOSString());
        configuration.setAttribute(LaunchConfigurationConstants.SPU_ARCH_TYPE, LaunchConfigurationConstants.SPU_ARCH_SPU);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(LaunchConfigurationConstants.COMPILER_NAME, fCompilerNameText.getText());
        configuration.setAttribute(LaunchConfigurationConstants.COMPILER_FLAGS, fCompilerFlagsText.getText());
        configuration.setAttribute(LaunchConfigurationConstants.PROJECT_NAME, fProfilerProjectText.getText());
        configuration.setAttribute(LaunchConfigurationConstants.SOURCE_FILE_NAME, fProfilerTargetText.getText());
        configuration.setAttribute(LaunchConfigurationConstants.SPU_EXECUTABLE_NAME, fSPUTimingExecutableText.getText());
        configuration.setAttribute(LaunchConfigurationConstants.ASSEMBLY_FILE_NAME, fSPUTimingAssemblyFileText.getText());
        IFile file = getCSource();
        if (file != null) {
            configuration.setAttribute(LaunchConfigurationConstants.PROJECT_DIR, getCProject().getProject().getLocation().toOSString());
        } else {
            configuration.setAttribute(LaunchConfigurationConstants.PROJECT_DIR, (String) null);
        }
        if (fSPUTimingArchitectureCombo.getSelectionIndex() == 0) {
            configuration.setAttribute(LaunchConfigurationConstants.SPU_ARCH_TYPE, LaunchConfigurationConstants.SPU_ARCH_SPU);
        } else if (fSPUTimingArchitectureCombo.getSelectionIndex() == 1) {
            configuration.setAttribute(LaunchConfigurationConstants.SPU_ARCH_TYPE, LaunchConfigurationConstants.SPU_ARCH_SPUEFP);
        }
    }

    public String getName() {
        return GUIMessages.SPUTimingMainTab_Title;
    }

    public class TextBoxListener implements ModifyListener {

        public void modifyText(ModifyEvent event) {
            updateLaunchConfigurationDialog();
        }
    }
}
