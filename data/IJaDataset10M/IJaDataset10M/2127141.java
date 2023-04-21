package com.google.devtools.build.wireless.testing.java.injector;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class adapter inserts and starts the playback test thread in the 
 * correct location in the applications byte code according to the
 * requirements of the target platform.
 *  
 * <p>After the instrumentation the target class (according to the platform) will 
 * create a playback thread with a specific testbase and will start it.
 * 
 * <p>In BB and J2SE application the test thread needs to be started at the 
 * beginning of the main, because it can also never terminate.
 * 
 * @author Michele Sama
 * 
 * TODO: rename this class like AcceptanceTestRunnerClassAdapter.
 */
public class ReplayClassAdapter extends ManagedClassAdapter {

    private String owner;

    private Platform targetPlatform = null;

    protected String playbackTestBase = null;

    private boolean needsToBeInjected = false;

    protected static Logger logger = Logger.getLogger("ReplayClassAdapter");

    /**
   * Creates a ReplayClassAdapter which will inject a specific test base.
   * 
   * @param targetPlatform The target platform of the injection
   * @param cv The parent ClassVisitor
   * @param classManager The ClassManager which needs to be used
   * @param testBase The test base which is going to be injected
   */
    public ReplayClassAdapter(Platform targetPlatform, ClassVisitor cv, ClassManager classManager, String testBase) {
        super(cv, classManager);
        playbackTestBase = testBase;
        this.targetPlatform = targetPlatform;
    }

    /**
   * Decides if the current class has to be injected with the replay thread 
   * which starts the regression test.
   * 
   * <p>If the current class is a valid candidate an interface is injected 
   * in order to avoid multiple injections. With the current implementations 
   * multiple injections are NOT possible, but the code is safe for possible 
   * extensions.
   */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        owner = name;
        implementedInterfaces = interfaces;
        if (targetPlatform.getPlatformSpecificMainSuperClass(superName) && !implementsInterface(ClassNames.PLAYABLE)) {
            needsToBeInjected = true;
            implementedInterfaces = Arrays.copyOf(interfaces, interfaces.length + 1);
            implementedInterfaces[interfaces.length] = ClassNames.PLAYABLE;
        }
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    /**
   * Injects a starting point for regression tests.
   * 
   * <p>If the class needs to be injected and if the method is the right one 
   * according to the target platform then the method visit is delegated to 
   * the respective MethodVisitor.
   * 
   * @see org.objectweb.asm.ClassAdapter#visitMethod(int, java.lang.String, 
   *    java.lang.String, java.lang.String, java.lang.String[])
   */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        String targetMethod = targetPlatform.getPlatformSpecificStartupMethod();
        if (needsToBeInjected && name.equals(targetMethod)) {
            mv = new StartReplayMethodAdapter(mv, access, this.owner, name, desc, playbackTestBase, targetPlatform);
        }
        return mv;
    }

    /**
   * Attach a replay thread at the beginning of the method into the target 
   * class which are platform specific.
   * 
   * <p>At the beginning of the visited method, this adapter 
   * injects instructions to create and run a playback thread. 
   * Ideally after the application has been loaded the playback thread is 
   * automatically started.
   * 
   * <p>In the target class the generated code is going to be like:
   * <code>public targetMethod() {
   *   Playback pb = new Playback(new FooTestBase());
   *   pb.start();
   *   ...
   * }
   * </code>
   * 
   * @author Michele Sama
   *
   */
    class StartReplayMethodAdapter extends InjectorMethodAdapter {

        /**
     * Creates an instance which will inject a playback thread for the specified 
     * testbase.
     * 
     * @param mv The nested MethodVisitor.
     * @param access The access flag for the method.
     * @param owner The currenClass class.
     * @param name The method's name.
     * @param desc The method's description
     * @param testBase The testbase to inject.
     * @param platform The target {@link Platform}.
     */
        public StartReplayMethodAdapter(MethodVisitor mv, int access, String owner, String name, String desc, String testBase, Platform platform) {
            super(mv, access, owner, name, desc, platform);
            playbackTestBase = testBase.replace('.', '/');
        }

        /**
     * Injects, at the beginning of the specified method (which is supposed 
     * to be MIDlet.startApp()) the set of instructions which will created and 
     * run the playback thread.
     *
     * @see org.objectweb.asm.MethodAdapter#visitCode()
     */
        @Override
        public void visitCode() {
            targetPlatform.injectPlayback(this, playbackTestBase);
            mv.visitCode();
        }
    }
}
