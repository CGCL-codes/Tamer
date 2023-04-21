package org.jiopi.ibean.kernel.context.classloader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.jiopi.framework.FrameworkInitializer;
import org.jiopi.framework.core.loader.CentralConsoleKernelLoaderFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.jiopi.ibean.kernel.IBeanCentralConsoleKernel;
import org.jiopi.ibean.kernel.NameVersion;
import org.jiopi.ibean.kernel.context.ContextCentralConsoleKernel;
import org.jiopi.ibean.kernel.repository.ModuleResource;
import org.jiopi.ibean.kernel.repository.Repository;
import org.jiopi.ibean.kernel.util.ObjectAccessor;

public class BlueprintClassLoaderManagerTestCase {

    @BeforeClass
    public static void init() {
        FrameworkInitializer.initialize();
    }

    /**
	 * only run in Debug mode
	 * @throws ClassNotFoundException
	 */
    @Ignore
    @Test
    public void getBlueprintClassLoader_Right() throws ClassNotFoundException {
        IBeanCentralConsoleKernel icck = (IBeanCentralConsoleKernel) CentralConsoleKernelLoaderFactory.loadCentralConsoleKernel();
        ContextCentralConsoleKernel ccck = ObjectAccessor.Accessible.method(icck, "getContextCentralConsoleKernel", ContextCentralConsoleKernel.class);
        BlueprintClassLoaderManager bcm = new BlueprintClassLoaderManager(ccck);
        ClassLoader cl = bcm.getBlueprintClassLoader("jiopi.ibean.testblueprint", "0.1");
        assertNotNull(cl);
        ClassLoader c2 = bcm.getBlueprintClassLoader("jiopi.ibean.testblueprint", "0.1");
        assertSame(cl, c2);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Class<?> classControlPanelClass = null;
        Class<?> ContextClassControlPanelClass = null;
        Class<?> moduleClassControlPanelClass = null;
        classControlPanelClass = cl.loadClass("org.jiopi.ibean.example.blueprint.testblueprint.ClassControlPanel");
        ContextClassControlPanelClass = contextClassLoader.loadClass("org.jiopi.ibean.example.blueprint.testblueprint.ClassControlPanel");
        NameVersion needNameVersion = new NameVersion("jiopi.ibean.testmodule", "0.1.0.0");
        ModuleResource mr = Repository.getModuleResource(needNameVersion, Repository.MODULE);
        ClassLoader moduleConsoleClassLoader = new IBeanClassLoader(needNameVersion, mr, ccck, null, null);
        moduleClassControlPanelClass = moduleConsoleClassLoader.loadClass("org.jiopi.ibean.example.blueprint.testblueprint.ClassControlPanel");
        assertNotNull(classControlPanelClass);
        assertSame(classControlPanelClass, ContextClassControlPanelClass);
        assertSame(classControlPanelClass, moduleClassControlPanelClass);
    }
}
