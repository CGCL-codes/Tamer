package net.beeger.osmorc.make;

import aQute.bnd.main.bnd;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.DummyCompileContext;
import com.intellij.openapi.compiler.PackagingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.compiler.make.BuildInstruction;
import com.intellij.openapi.compiler.make.BuildInstructionVisitor;
import com.intellij.openapi.compiler.make.BuildRecipe;
import com.intellij.openapi.compiler.make.FileCopyInstruction;
import com.intellij.openapi.deployment.DeploymentUtil;
import com.intellij.openapi.deployment.ModuleLink;
import com.intellij.openapi.deployment.PackagingMethod;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.JdkOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gnu.trove.THashSet;
import net.beeger.osmorc.facet.OsmorcFacet;
import net.beeger.osmorc.facet.OsmorcFacetConfiguration;
import net.beeger.osmorc.i18n.OsmorcBundle;
import net.beeger.osmorc.misc.CachingBundleInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.framework.Constants;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * This is a compiler step that builds up a bundle. Depending on user settings the compiler either uses a user-edited
 * manifest or builds up a manifest using bnd.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id$
 */
public class BundleCompiler implements PackagingCompiler {

    private static String getCachesPath() {
        String path = PathManager.getSystemPath() + File.separator + "osmorc" + File.separator + "caches";
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    Logger logger = Logger.getInstance("#net.beeger.osmorc.make.BundleCompiler");

    /**
   * Deletes the jar file of a bundle when it is outdated.
   *
   * @param compileContext the compile context
   * @param s              ??
   * @param validityState  the validity state of the item that is outdated
   */
    public void processOutdatedItem(CompileContext compileContext, String s, @Nullable final ValidityState validityState) {
        if (validityState != null) {
            ApplicationManager.getApplication().runReadAction(new Runnable() {

                public void run() {
                    BundleValidityState myvalstate = (BundleValidityState) validityState;
                    String jarUrl = myvalstate.getOutputJarUrl();
                    if (jarUrl != null) {
                        FileUtil.delete(new File(jarUrl));
                    }
                }
            });
        }
    }

    /**
   * Returns all processingitems (== Bundles to be created) for the givne compile context
   *
   * @param compileContext the compile context
   * @return a list of bundles that need to be compiled
   */
    @NotNull
    public ProcessingItem[] getProcessingItems(final CompileContext compileContext) {
        return ApplicationManager.getApplication().runReadAction(new Computable<ProcessingItem[]>() {

            public ProcessingItem[] compute() {
                CompileScope compilescope = compileContext.getCompileScope();
                Module affectedModules[] = compilescope.getAffectedModules();
                if (affectedModules.length == 0) {
                    return ProcessingItem.EMPTY_ARRAY;
                }
                Project project = affectedModules[0].getProject();
                Module modules[] = ModuleManager.getInstance(project).getModules();
                THashSet<Module> thashset = new THashSet<Module>();
                for (Module module : modules) {
                    if (!OsmorcFacet.hasOsmorcFacet(module)) {
                        continue;
                    }
                    thashset.add(module);
                }
                ProcessingItem[] result = new ProcessingItem[thashset.size()];
                int i = 0;
                for (Object aThashset : thashset) {
                    Module module = (Module) aThashset;
                    if (module.getModuleFile() != null) {
                        result[i++] = new BundleProcessingItem(module);
                    }
                }
                return result;
            }
        });
    }

    /**
   * Processes a processing item (=module)
   *
   * @param compileContext  the compile context
   * @param processingItems the list of processing items
   * @return the list of processing items that remain for further processing (if any)
   */
    public ProcessingItem[] process(CompileContext compileContext, ProcessingItem[] processingItems) {
        try {
            for (ProcessingItem processingItem : processingItems) {
                Module module = ((BundleProcessingItem) processingItem).getModule();
                buildBundle(module, compileContext.getProgressIndicator());
            }
        } catch (IOException ioexception) {
            logger.error(ioexception);
        }
        return processingItems;
    }

    /**
   * Builds the bundle for a given module.
   *
   * @param module            the module
   * @param progressIndicator the progress indicator
   * @throws IOException in case something goes wrong.
   */
    static void buildBundle(final Module module, final ProgressIndicator progressIndicator) throws IOException {
        final File jarFile = new File(VfsUtil.urlToPath(getJarFileName(module)));
        jarFile.delete();
        FileUtil.createParentDirs(jarFile);
        final BuildRecipe buildrecipe = (new ReadAction<BuildRecipe>() {

            protected void run(Result<BuildRecipe> result) {
                result.setResult(getBuildRecipe(module));
            }
        }).execute().getResultObject();
        Manifest manifest = DeploymentUtil.getInstance().createManifest(buildrecipe);
        if (manifest == null) {
            manifest = new Manifest();
        }
        OsmorcFacetConfiguration configuration = OsmorcFacet.getInstance(module).getConfiguration();
        if (!configuration.isOsmorcControlsManifest()) {
            progressIndicator.setText2(OsmorcBundle.getTranslation("bundlecompiler.merging.manifest"));
            VirtualFile manifestFile = getManifestFile(module);
            if (manifestFile != null) {
                Manifest fromFile = new Manifest(manifestFile.getInputStream());
                Attributes atts = fromFile.getMainAttributes();
                for (Map.Entry<Object, Object> entry : atts.entrySet()) {
                    manifest.getMainAttributes().put(entry.getKey(), entry.getValue());
                }
            }
        }
        String mainClass = null;
        if (!Comparing.strEqual(mainClass, null)) {
            manifest.getMainAttributes().putValue(Attributes.Name.MAIN_CLASS.toString(), mainClass);
        }
        final File tempFile = File.createTempFile("___" + FileUtil.getNameWithoutExtension(jarFile), ".jar", jarFile.getParentFile());
        final JarOutputStream jaroutputstream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)), manifest);
        final THashSet<String> writtenItems = new THashSet<String>();
        try {
            buildrecipe.visitInstructionsWithExceptions(new BuildInstructionVisitor() {

                public boolean visitInstruction(BuildInstruction buildinstruction) throws IOException {
                    ProgressManager.getInstance().checkCanceled();
                    if (buildinstruction instanceof FileCopyInstruction) {
                        FileCopyInstruction filecopyinstruction = (FileCopyInstruction) buildinstruction;
                        File file2 = filecopyinstruction.getFile();
                        if (file2 == null || !file2.exists()) {
                            return true;
                        }
                        String s2 = FileUtil.toSystemDependentName(file2.getPath());
                        if (progressIndicator != null) {
                            progressIndicator.setText2(IdeBundle.message("jar.build.processing.file.progress", s2));
                        }
                    }
                    buildinstruction.addFilesToJar(DummyCompileContext.getInstance(), tempFile, jaroutputstream, buildrecipe, writtenItems, ManifestFileFilter.Instance);
                    return true;
                }
            }, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        jaroutputstream.close();
        if (configuration.isOsmorcControlsManifest()) {
            progressIndicator.setText2(OsmorcBundle.getTranslation("bundlecompiler.creatingmanifest"));
            bnd bnd = new bnd();
            Map<String, String> additionalProperties = new HashMap<String, String>();
            additionalProperties.put(Constants.BUNDLE_SYMBOLICNAME, configuration.getBundleSymbolicName());
            additionalProperties.put(Constants.BUNDLE_ACTIVATOR, configuration.getBundleActivator());
            Map<String, String> propsFromFacetSetup = configuration.getAdditionalPropertiesAsMap();
            additionalProperties.putAll(propsFromFacetSetup);
            try {
                bnd.doWrap(null, tempFile, tempFile, null, 0, additionalProperties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtil.rename(tempFile, jarFile);
        } catch (IOException ioexception) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {

                public void run() {
                    String s2 = IdeBundle.message("jar.build.cannot.overwrite.error", FileUtil.toSystemDependentName(jarFile.getPath()), FileUtil.toSystemDependentName(tempFile.getPath()));
                    Messages.showErrorDialog(module.getProject(), s2, IdeBundle.message("jar.build.error.title"));
                }
            });
        }
    }

    /**
   * Returns the manifest file for the given module if it exists
   *
   * @param module the module
   * @return the manifest file or null if it doesnt exist
   */
    @Nullable
    public static VirtualFile getManifestFile(@NotNull Module module) {
        OsmorcFacet facet = OsmorcFacet.getInstance(module);
        ModuleRootManager manager = ModuleRootManager.getInstance(module);
        for (VirtualFile root : manager.getContentRoots()) {
            VirtualFile result = VfsUtil.findRelativeFile(facet.getConfiguration().getManifestLocation(), root);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
   * @return a description (prolly not used)
   */
    @NotNull
    public String getDescription() {
        return "bundle compile";
    }

    /**
   * Checks the configuration.
   *
   * @param compileScope the compilescope
   * @return true if the configuration is valid, false otherwise
   */
    public boolean validateConfiguration(CompileScope compileScope) {
        return true;
    }

    /**
   * Recreates a validity state from a data input stream
   *
   * @param dataInputStream stream containing the data
   * @return the validity state
   * @throws IOException in case something goes wrong
   */
    public ValidityState createValidityState(DataInputStream dataInputStream) throws IOException {
        return new BundleValidityState(dataInputStream);
    }

    /**
   * Creates a build recipe for the given module
   *
   * @param module the module
   * @return the build recipe
   */
    static BuildRecipe getBuildRecipe(Module module) {
        DummyCompileContext dummycompilecontext = DummyCompileContext.getInstance();
        BuildRecipe buildrecipe = DeploymentUtil.getInstance().createBuildRecipe();
        ModuleLink link = DeploymentUtil.getInstance().createModuleLink(module, null);
        link.setPackagingMethod(PackagingMethod.COPY_FILES);
        link.setURI("/");
        ModuleLink[] modules = new ModuleLink[] { link };
        DeploymentUtil.getInstance().addJavaModuleOutputs(module, modules, buildrecipe, dummycompilecontext, null, IdeBundle.message("jar.build.module.presentable.name", module.getName()));
        return buildrecipe;
    }

    /**
   * Bundlifies all libraries that belong to the given module and that are not bundles. The bundles are cached, so if
   * the source library does not change, it will not be bundlified again.
   *
   * @param module the module whose libraries are to be bundled.
   * @return a string array containing the urls of the bundlified libraries.
   */
    public static String[] bundlifyLibraries(Module module) {
        ArrayList<String> result = new ArrayList<String>();
        ModuleRootManager manager = ModuleRootManager.getInstance(module);
        OrderEntry[] entries = manager.getModifiableModel().getOrderEntries();
        for (OrderEntry entry : entries) {
            if (entry instanceof JdkOrderEntry) {
                continue;
            }
            String[] urls = entry.getUrls(OrderRootType.CLASSES);
            for (String url : urls) {
                url = url.replaceAll("!.*", "");
                url = url.replace("jar://", "file://");
                String displayName = CachingBundleInfoProvider.getBundleSymbolicName(url);
                if (displayName == null) {
                    result.add(bundlify(url));
                } else {
                    result.add(url);
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
   * Takes the given jar file and transforms it into a bundle. The bundle is stored inside a temp folder of the current
   * user's home.
   *
   * @param bundleFileUrl url of the file to be bundlified
   * @return the url from where the bundlified file can be installed. returns null if the bundlification failed for any
   *         reason.
   */
    private static String bundlify(String bundleFileUrl) {
        try {
            File targetDir = new File(getCachesPath());
            File sourceFile = new File(VfsUtil.urlToPath(bundleFileUrl));
            File targetFile = new File(targetDir.getPath() + File.separator + sourceFile.getName());
            if (!targetFile.exists() || targetFile.lastModified() < sourceFile.lastModified()) {
                bnd bnd = new bnd();
                bnd.doWrap(null, sourceFile, targetFile, null, 0, null);
            }
            return VfsUtil.pathToUrl(targetFile.getCanonicalPath());
        } catch (Exception e) {
            Messages.showErrorDialog(OsmorcBundle.getTranslation("bundlecompiler.bundlifying.problem.message", bundleFileUrl, e.toString()), OsmorcBundle.getTranslation("error"));
            return null;
        }
    }

    /**
   * Builds the name of the jar file for a given module.
   *
   * @param module the module
   * @return the name of the jar file that will be produced for this module by this compiler
   */
    public static String getJarFileName(final Module module) {
        return OsmorcFacet.getInstance(module).getConfiguration().getJarFileLocation();
    }
}
