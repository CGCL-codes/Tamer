package org.pustefixframework.ide.eclipse.plugin.builder;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.pustefixframework.ide.eclipse.plugin.Activator;
import org.pustefixframework.ide.eclipse.plugin.Environment;
import org.pustefixframework.ide.eclipse.plugin.Logger;
import org.pustefixframework.ide.eclipse.plugin.core.internal.PustefixCore;
import org.pustefixframework.ide.eclipse.plugin.ui.preferences.PreferenceConstants;
import org.pustefixframework.ide.eclipse.plugin.util.PustefixVersion;
import org.pustefixframework.ide.eclipse.plugin.util.VersionCheck;

public class PustefixBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = "org.pustefixframework.ide.eclipse.plugin.builder.pustefixbuilder";

    private static Logger LOG = Activator.getLogger();

    private IPreferencesService prefService;

    private IScopeContext[] prefScopes;

    private Environment environment;

    private BuilderDelegate iwrpBuilder;

    private BuilderDelegate scodeBuilder;

    private VersionLoader versionLoader;

    private boolean initialized;

    private PustefixVersion pustefixVersion;

    public PustefixBuilder() {
        System.out.println("CREATE");
    }

    private void init() {
        System.out.println("INIT");
        if (!initialized) {
            prefService = Platform.getPreferencesService();
            prefScopes = new IScopeContext[] { new ProjectScope(getProject()), new InstanceScope(), new DefaultScope() };
            environment = new Environment(prefService, prefScopes);
            try {
                versionLoader = new VersionLoader();
            } catch (IOException e) {
                throw new RuntimeException("Can't get VersionLoader", e);
            }
            checkVersion();
            initialized = true;
        }
    }

    public void checkVersion() {
        PustefixVersion oldVersion = pustefixVersion;
        pustefixVersion = VersionCheck.getPustefixVersion(getProject());
        if (pustefixVersion == null) {
            pustefixVersion = new PustefixVersion();
            pustefixVersion.setMajorVersion(0);
            pustefixVersion.setMinorVersion(16);
            pustefixVersion.setMicroVersion(0);
        }
        System.out.println(">>> OLD VERSION: " + oldVersion);
        System.out.println(">>> NEW VERSION: " + pustefixVersion);
        if (oldVersion == null || oldVersion.compareTo(pustefixVersion) != 0) {
            StatusCodeGenerator scodeGenerator;
            Class<?> clazz = versionLoader.loadClass("StatusCodeGeneratorImpl", pustefixVersion);
            try {
                scodeGenerator = (StatusCodeGenerator) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            scodeBuilder = new StatusCodeBuilderDelegate(getProject(), environment, scodeGenerator);
            URL url = versionLoader.loadResource("iwrapper.xsl", pustefixVersion);
            iwrpBuilder = new IWrapperBuilderDelegate(getProject(), environment, url);
        }
    }

    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        System.out.println("*** BUILD ***");
        if (kind == AUTO_BUILD) System.out.println(">>> AUTO"); else if (kind == CLEAN_BUILD) System.out.println(">>> CLEAN"); else if (kind == FULL_BUILD) System.out.println(">>> FULL"); else if (kind == INCREMENTAL_BUILD) System.out.println(">>> INC"); else System.out.println(">>> " + kind);
        try {
            init();
            if (kind == FULL_BUILD) {
                System.out.println("DELTA: " + getDelta(getProject()));
                fullBuild(monitor);
            } else {
                IResourceDelta delta = getDelta(getProject());
                if (delta == null) {
                    fullBuild(monitor);
                } else {
                    incrementalBuild(delta, monitor);
                }
            }
        } catch (Exception x) {
            LOG.error(x);
        }
        return null;
    }

    @Override
    protected void clean(IProgressMonitor monitor) throws CoreException {
        System.out.println("*** CLEAN ***");
        init();
        iwrpBuilder.clean(monitor);
        scodeBuilder.clean(monitor);
    }

    private void fullBuild(final IProgressMonitor monitor) throws CoreException {
        if (PustefixCore.getInstance().hasClassPathChanged(getProject())) {
            checkVersion();
            PustefixCore.getInstance().classPathChangeHandled(getProject());
        }
        boolean iwrpGen = prefService.getBoolean(Activator.PLUGIN_ID, PreferenceConstants.PREF_GENERATEIWRAPPERS, false, prefScopes);
        if (iwrpGen) iwrpBuilder.fullBuild(monitor);
        boolean scodeGen = prefService.getBoolean(Activator.PLUGIN_ID, PreferenceConstants.PREF_GENERATESTATUSCODES, false, prefScopes);
        if (scodeGen) scodeBuilder.fullBuild(monitor);
    }

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        if (PustefixCore.getInstance().hasClassPathChanged(getProject())) {
            checkVersion();
            PustefixCore.getInstance().classPathChangeHandled(getProject());
        }
        boolean iwrpGen = prefService.getBoolean(Activator.PLUGIN_ID, PreferenceConstants.PREF_GENERATEIWRAPPERS, false, prefScopes);
        if (iwrpGen) iwrpBuilder.incrementalBuild(delta, monitor);
        boolean scodeGen = prefService.getBoolean(Activator.PLUGIN_ID, PreferenceConstants.PREF_GENERATESTATUSCODES, false, prefScopes);
        if (scodeGen) scodeBuilder.incrementalBuild(delta, monitor);
    }
}
