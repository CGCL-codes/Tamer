package es.nom.morenojuarez.modulipse.core.builder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import es.nom.morenojuarez.modulipse.core.ModulipseCorePlugin;
import es.nom.morenojuarez.modulipse.core.lang.Modula2AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2Parser;
import es.nom.morenojuarez.modulipse.core.lang.ParsingProblem;

/**
 * Builder for Modula-2 source files.
 */
public class Modula2Builder extends IncrementalProjectBuilder {

    /**
	 * File extension for Windows executable files.
	 */
    public static final String WINDOWS_EXE_FILE_EXTENSION = "exe";

    /**
	 * File extension for Modula-2 definition modules.
	 */
    public static final String DEF_FILE_EXTENSION = "def";

    /**
	 * File extension for Modula-2 Implementation o Program modules.
	 */
    public static final String MOD_FILE_EXTENSION = "mod";

    private static final String ARGUMENT_SEPARATOR = " ";

    private static final String ONLY_COMPILE_OPTION = " -c ";

    private static final String OUTPUT_FILE_OPTION = " -o ";

    private static final String INCLUDE_DEBUG_INFO_OPTION = " -g ";

    /**
	 * Builder identifier.
	 */
    public static final String BUILDER_ID = "modulipse_core.modula2Builder";

    /**
	 * Object files extension.
	 */
    public static final String OBJECT_EXTENSION = "o";

    private String compilerCommand;

    private String linkCommand;

    private List<IBuildListener> buildListeners;

    private File compilerCommandFile;

    /**
	 * File extension separator (dot).
	 */
    public static final String FILE_EXTENSION_SEPARATOR = ".";

    /**
	 * Creates a new Modula2Builder object.
	 */
    public Modula2Builder() {
        buildListeners = new ArrayList<IBuildListener>();
        buildListeners.add(ModulipseCorePlugin.getDefault().getBuildListener());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        fireBuildStarted();
        IPreferenceStore prefs = ModulipseCorePlugin.getDefault().getPreferenceStore();
        String compilerExecutable = prefs.getString(ModulipseCorePlugin.COMPILER_PATH_PREFERENCE);
        compilerCommand = compilerExecutable + ARGUMENT_SEPARATOR + prefs.getString(ModulipseCorePlugin.EXTRA_COMPILER_ARGS_PREFERENCE) + ONLY_COMPILE_OPTION + INCLUDE_DEBUG_INFO_OPTION;
        linkCommand = compilerExecutable + ARGUMENT_SEPARATOR;
        compilerCommandFile = new File(compilerExecutable);
        if (!compilerCommandFile.exists()) {
            fireBuilderMessage(Messages.Modula2Builder_1);
        }
        if (kind == FULL_BUILD) {
            fullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        fireBuildEnded(getProject().getName());
        return null;
    }

    private void fireBuildEnded(String projectName) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.buildEnded(projectName);
        }
    }

    private void fireBuildStarted() {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.buildStarted(getProject().getName());
        }
    }

    /**
	 * Performs a full build of the project.
	 * 
	 * @param monitor	progress monitor
	 */
    protected void fullBuild(final IProgressMonitor monitor) {
        try {
            getProject().accept(new IResourceVisitor() {

                public boolean visit(IResource resource) throws CoreException {
                    compileResource(resource);
                    return true;
                }
            });
            getProject().accept(new IResourceVisitor() {

                public boolean visit(IResource resource) throws CoreException {
                    linkResource(resource);
                    return true;
                }
            });
        } catch (CoreException e) {
            ModulipseCorePlugin.log(e);
        }
    }

    /**
	 * Incremental build on a resource delta.
	 * 
	 * @param delta		changes in the resource
	 * @param monitor	progress monitor
	 * @throws CoreException	thrown when an error occurs in the resource handling
	 */
    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        delta.accept(new IResourceDeltaVisitor() {

            public boolean visit(IResourceDelta delta) throws CoreException {
                IResource resource = delta.getResource();
                compileResource(resource);
                return true;
            }
        });
        delta.accept(new IResourceDeltaVisitor() {

            public boolean visit(IResourceDelta delta) throws CoreException {
                IResource resource = delta.getResource();
                linkResource(resource);
                return true;
            }
        });
    }

    @Override
    protected void clean(final IProgressMonitor monitor) throws CoreException {
        monitor.beginTask(Messages.Modula2Builder_CleaningTaskTitle, getProject().members().length);
        fireCleanStarted();
        getProject().accept(new IResourceVisitor() {

            private int resourceCounter = 0;

            public boolean visit(IResource resource) throws CoreException {
                monitor.subTask(resource.getName());
                cleanResource(resource);
                monitor.worked(++resourceCounter);
                return true;
            }
        });
        fireCleanEnded();
        monitor.done();
    }

    private void fireCleanEnded() {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.cleanEnded(getProject().getName());
        }
    }

    private void fireCleanStarted() {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.cleanStarted(getProject().getName());
        }
    }

    /**
	 * Cleans the object files for this resource and delete the problem markers.
	 * 
	 * @param resource			resource whose object files will be cleaned
	 * 
	 * @throws CoreException	thrown if problem markers could not be deleted
	 */
    void cleanResource(IResource resource) throws CoreException {
        String extension = resource.getFileExtension();
        if (MOD_FILE_EXTENSION.equals(extension)) {
            resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
            final IPath fileNameWithoutExtension = resource.getLocation().removeFileExtension();
            final IPath objectFile = fileNameWithoutExtension.addFileExtension(OBJECT_EXTENSION);
            final IPath exeFile = addExeExtension(resource);
            if (objectFile.toFile().delete()) {
                fireDeletingFile(objectFile.toOSString());
            }
            if (exeFile.toFile().delete()) {
                fireDeletingFile(exeFile.toOSString());
            }
        }
    }

    private void fireDeletingFile(String fileName) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.deletingFile(fileName);
        }
    }

    /**
	 * Compiles a single resource.
	 * 
	 * @param resource	resource to compile
	 * @throws CoreException	thrown when an error occurs in the resource handling
	 */
    void compileResource(IResource resource) throws CoreException {
        String extension = resource.getFileExtension();
        boolean resourceIsMod;
        if ((resourceIsMod = MOD_FILE_EXTENSION.equals(extension)) || DEF_FILE_EXTENSION.equals(extension)) {
            IDocument doc;
            try {
                doc = new FileDocument((IFile) resource);
                updateProblemMarkers(resource, doc);
            } catch (IOException e) {
                ModulipseCorePlugin.log(e);
            } catch (ANTLRException e) {
                ModulipseCorePlugin.log(e);
            }
            if (resourceIsMod) {
                if (compilerCommandFile.exists()) {
                    try {
                        String resourceName = resource.getName();
                        String currentBuildCommand = compilerCommand + resourceName + OUTPUT_FILE_OPTION + resource.getLocation().removeFileExtension().addFileExtension(OBJECT_EXTENSION);
                        fireBuildingFile(resourceName, currentBuildCommand);
                        Process proc = Runtime.getRuntime().exec(currentBuildCommand, null, resource.getLocation().removeLastSegments(1).toFile());
                        BufferedReader processOutput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line;
                        closeCloseable(proc.getOutputStream());
                        while ((line = processOutput.readLine()) != null) {
                            fireBuilderMessage(line);
                        }
                        closeCloseable(processOutput);
                        int exitValue = proc.waitFor();
                        if (exitValue == 0) {
                            final IPath fileNameWithoutExtension = resource.getProjectRelativePath().removeFileExtension();
                            final IPath objectFile = fileNameWithoutExtension.addFileExtension(OBJECT_EXTENSION);
                            fireObjectFileGenerated(objectFile.toOSString());
                        } else {
                            fireObjectFileNotGenerated(exitValue);
                        }
                    } catch (IOException e) {
                        ModulipseCorePlugin.log(e);
                    } catch (InterruptedException e) {
                        ModulipseCorePlugin.log(e);
                    }
                }
            }
        }
    }

    private void fireObjectFileNotGenerated(int exitValue) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.objectFileNotGenerated(exitValue);
        }
    }

    private void fireObjectFileGenerated(String objectFileName) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.objectFileGenerated(objectFileName);
        }
    }

    private void fireBuilderMessage(String line) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.builderMessage(line);
        }
    }

    private void fireBuildingFile(String resourceName, String currentCompilerCommand) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.buildingFile(resourceName, currentCompilerCommand);
        }
    }

    /**
	 * Update the problem markers in the specified resource.
	 * 
	 * @param resource	resource whose markers will be updated
	 * @param doc		document to retrieve the problem position
	 * @throws CoreException		thrown when a problem occurs while managing
	 * 								markers or retrieving the resource contents
	 * @throws RecognitionException thrown on a recognition problem while
	 * 								parsing the Modula-2 syntax in the resource
	 * @throws TokenStreamException	thrown on a token stream problem while
	 * 								parsing the Modula-2 syntax in the resource
	 */
    public static void updateProblemMarkers(IResource resource, IDocument doc) throws CoreException, TokenStreamException, RecognitionException {
        resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        Modula2Parser parser;
        InputStream input = ((IFile) resource).getContents();
        parser = Modula2Parser.getRootModula2Parser(input);
        Iterator<ParsingProblem> problemIterator = parser.getProblems().iterator();
        while (problemIterator.hasNext()) {
            ParsingProblem problem = problemIterator.next();
            IMarker marker = resource.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
            marker.setAttribute(IMarker.SEVERITY, problem.getSeverity());
            marker.setAttribute(ParsingProblem.TYPE_ATTRIBUTE_NAME, problem.getType());
            try {
                marker.setAttribute(IMarker.CHAR_START, doc.getLineOffset(problem.getStartLine() - 1) + problem.getStartCol() - 1);
                marker.setAttribute(IMarker.CHAR_END, doc.getLineOffset(problem.getEndLine() - 1) + problem.getEndCol() - 1);
            } catch (BadLocationException e) {
                ModulipseCorePlugin.log(e);
            }
        }
    }

    /**
	 * Invokes the linker for the current resource.
	 * 
	 * @param resource			resource to link
	 * @throws CoreException	thrown whenever an error occurs while getting
	 * 							the resource contents
	 */
    void linkResource(IResource resource) throws CoreException {
        String extension = resource.getFileExtension();
        if (MOD_FILE_EXTENSION.equals(extension)) {
            if (compilerCommandFile.exists()) {
                try {
                    if (Modula2AST.isProgramModule(((IFile) resource).getContents())) {
                        final String resourceName = resource.getName();
                        final IPath exeFile = addExeExtension(resource);
                        String currentLinkCommand = linkCommand + OUTPUT_FILE_OPTION + exeFile + ARGUMENT_SEPARATOR + resourceName;
                        fireLinkingFile(resourceName, currentLinkCommand);
                        Process proc = Runtime.getRuntime().exec(currentLinkCommand, null, resource.getLocation().removeLastSegments(1).toFile());
                        BufferedReader processOutput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line;
                        closeCloseable(proc.getOutputStream());
                        while ((line = processOutput.readLine()) != null) {
                            fireBuilderMessage(line);
                        }
                        closeCloseable(processOutput);
                        int exitValue = proc.waitFor();
                        if (exitValue == 0) {
                            fireExeFileGenerated(exeFile.toOSString());
                        } else {
                            fireExeFileNotGenerated(exitValue);
                        }
                    }
                } catch (IOException e) {
                    ModulipseCorePlugin.log(e);
                } catch (InterruptedException e) {
                    ModulipseCorePlugin.log(e);
                }
            }
        }
    }

    private void fireExeFileNotGenerated(int exitValue) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.exeFileNotGenerated(exitValue);
        }
    }

    private void fireExeFileGenerated(String exeFileName) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.exeFileGenerated(exeFileName);
        }
    }

    private void fireLinkingFile(String resourceName, String currentBuildCommand) {
        for (IBuildListener buildListener : buildListeners) {
            buildListener.linkingFile(resourceName, currentBuildCommand);
        }
    }

    /**
	 * Closes a {@link Closeable} object without reporting any {@link IOException}.
	 * 
	 * @param obj	object to close
	 */
    private static void closeCloseable(Closeable obj) {
        try {
            obj.close();
        } catch (IOException e) {
            ModulipseCorePlugin.log(e);
        }
    }

    /**
	 * Adds the appropriate executable extension to the given {@link IPath}
	 * object.
	 * 
	 * @param resource	path to add the executable extension to
	 * @return	the base name of the path with the appropriate executable
	 * 			extension (if any)
	 */
    public static IPath addExeExtension(IResource resource) {
        String exeExtension;
        if (Platform.OS_WIN32.equals(Platform.getOS())) {
            exeExtension = "." + WINDOWS_EXE_FILE_EXTENSION;
        } else {
            exeExtension = "";
        }
        String fileNameWithoutExtension = resource.getLocation().removeFileExtension().toPortableString();
        if (fileNameWithoutExtension.endsWith(FILE_EXTENSION_SEPARATOR)) {
            fileNameWithoutExtension = fileNameWithoutExtension.substring(0, fileNameWithoutExtension.length() - 1);
        }
        return Path.fromPortableString(fileNameWithoutExtension.concat(exeExtension));
    }
}
