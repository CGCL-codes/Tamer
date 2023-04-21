package test;

import java.net.*;
import java.io.*;
import java.util.logging.*;
import java.lang.reflect.*;
import org.xito.boot.*;

/**
 *
 * @author  Deane
 */
public class AppStarterService {

    private static Logger logger = Logger.getLogger(AppStarterService.class.getName());

    /** Creates a new instance of AppStarterService */
    public AppStarterService() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        logger.info("**** Started App Starter *******");
        testClassLoader2();
    }

    public static void testApp(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("TestApp", "TestApplication");
            appDesc.addClassPathEntry(new ClassPathEntry(new File(Boot.getBootDir(), "app/testapp.jar").toURL()));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("test.app.TestApp");
            appDesc.setNewAppContext(appContext);
            appDesc.setMainArgs(new String[] { "test1", "test2", "test3" });
            launcher.launchInternal(appDesc, true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void testSwingSet(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("SwingSet2", "SwingSet2");
            String codebase = "http://java.sun.com/products/jfc/jws/";
            appDesc.addClassPathEntry(new ClassPathEntry(new URL(codebase + "SwingSet2.jar")));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("SwingSet2");
            appDesc.setNewAppContext(appContext);
            launcher.launchInternal(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testAsteroids(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("Asteroids", "Asteroids");
            String codebase = "http://xito.sourceforge.net/apps/games/asteroids/";
            appDesc.addClassPathEntry(new ClassPathEntry(new URL(codebase + "asteroids.jar")));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("org.xito.asteroids.MainApp");
            appDesc.setNewAppContext(appContext);
            launcher.launchInternal(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testEditor(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("Editor", "Editor");
            String codebase = "http://xito.sourceforge.net/apps/tools/editor/";
            appDesc.addClassPathEntry(new ClassPathEntry(new URL(codebase + "xito-editor.jar")));
            appDesc.addClassPathEntry(new ClassPathEntry(new URL(codebase + "nb-editor.jar")));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("org.xito.editor.Editor");
            appDesc.setNewAppContext(appContext);
            launcher.launchInternal(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testJGoodies(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("JGoodies", "JGoodies");
            String codebase = "http://www.jgoodies.com/download/demos/metamorphosis/";
            URL u = new URL(codebase + "metamorphosis.jar");
            appDesc.addClassPathEntry(new ClassPathEntry(u));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("com.jgoodies.metamorphosis.Metamorphosis");
            appDesc.setNewAppContext(appContext);
            launcher.launchInternal(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testJDiskReport(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("JGoodies", "JGoodies");
            String codebase = "http://www.jgoodies.com/download/jdiskreport/";
            URL u = new URL(codebase + "jdiskreport.jar");
            appDesc.addClassPathEntry(new ClassPathEntry(u));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("com.jgoodies.jdiskreport.JDiskReport");
            appDesc.setNewAppContext(appContext);
            launcher.launchBackground(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testJPathReport(boolean appContext) {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("JGoodies", "JGoodies");
            String codebase = "http://www.jgoodies.com/download/jpathreport/";
            URL u = new URL(codebase + "jpathreport.jar");
            appDesc.addClassPathEntry(new ClassPathEntry(u));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("com.jgoodies.jpathreport.JPathReport");
            appDesc.setNewAppContext(appContext);
            launcher.launchInternal(appDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testClassLoader2() {
        try {
            AppDesc appDesc = new AppDesc("Asteroids", "Asteroids");
            String codebase = "http://xito.sourceforge.net/apps/games/asteroids/";
            appDesc.addClassPathEntry(new ClassPathEntry(new URL(codebase + "asteroids.jar")));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("org.xito.asteroids.MainApp");
            CacheClassLoader loader = new CacheClassLoader(appDesc, null);
            Class cls = loader.loadClass(appDesc.getMainClass());
            System.out.println("Made it HERE !!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testMemoryCleanUp() {
        AppLauncher launcher = new AppLauncher();
        try {
            AppDesc appDesc = new AppDesc("TestApp", "TestApplication");
            appDesc.addClassPathEntry(new ClassPathEntry(new File(Boot.getBootDir(), "app/testapp.jar").toURL()));
            appDesc.setPermissions(appDesc.getAllPermissions());
            appDesc.setMainClass("test.app.MemoryTestApp");
            appDesc.setNewAppContext(true);
            for (int i = 0; i < 100; i++) {
                System.out.println("***************** Launching Number:" + i);
                launcher.launchBackground(appDesc);
                Thread.currentThread().sleep(3000);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
