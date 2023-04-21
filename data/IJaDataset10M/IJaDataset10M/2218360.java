package org.lwjgl.test.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Tests the ARB_create_context extension through the use of the ContextAttribs class.
 *
 * @author Spasi
 */
public final class VersionTest {

    private VersionTest() {
    }

    public static void main(String[] args) {
        initialize(args);
        cleanup();
        System.exit(0);
    }

    private static void initialize(String[] args) {
        if (args.length < 2) argsError("Insufficient number of arguments");
        int majorInput = 1;
        int minorInput = 0;
        try {
            majorInput = Integer.parseInt(args[0]);
            minorInput = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            argsError("Invalid number format");
        }
        ContextAttribs ca = new ContextAttribs(majorInput, minorInput);
        if (2 < args.length) {
            for (int i = 2; i < args.length; i++) {
                if (Pattern.matches("[0-9]+", args[i])) ca = ca.withLayer(Integer.parseInt(args[i])); else if ("debug".equalsIgnoreCase(args[i])) ca = ca.withDebug(true); else if ("fc".equalsIgnoreCase(args[i])) ca = ca.withForwardCompatible(true); else if ("core".equalsIgnoreCase(args[i])) ca = ca.withProfileCore(true); else if ("compatibility".equalsIgnoreCase(args[i])) ca = ca.withProfileCompatibility(true); else if ("es".equalsIgnoreCase(args[i])) ca = ca.withProfileES(true); else argsError("Unknown argument: \'" + args[i] + "\'");
            }
        }
        try {
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            DisplayMode displayMode;
            displayMode = chooseMode(modes, 1024, 768);
            if (displayMode == null) displayMode = chooseMode(modes, 800, 600);
            if (displayMode == null) displayMode = chooseMode(modes, 640, 480);
            if (displayMode == null) kill("Failed to set an appropriate display mode.");
            System.out.println("Setting display mode to: " + displayMode);
            Display.setDisplayMode(displayMode);
            Display.create(new PixelFormat(8, 24, 0), ca);
        } catch (LWJGLException e) {
            kill(e.getMessage());
        }
        System.out.println("\n---------\n");
        System.out.println("Requested " + ca);
        final String version = glGetString(GL_VERSION);
        boolean deprecated = false;
        try {
            glVertex3f(0.0f, 0.0f, 0.0f);
            deprecated = true;
        } catch (Throwable t) {
        }
        final StringTokenizer version_tokenizer = new StringTokenizer(version, ". ");
        int majorVersion = Integer.parseInt(version_tokenizer.nextToken());
        int minorVersion = Integer.parseInt(version_tokenizer.nextToken());
        final boolean compatibilityProfile;
        final boolean coreProfile;
        if (3 < majorVersion || (majorVersion == 3 && 2 <= minorVersion)) {
            final int profileMask = glGetInteger(GL_CONTEXT_PROFILE_MASK);
            compatibilityProfile = (profileMask & GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) != 0;
            coreProfile = (profileMask & GL_CONTEXT_CORE_PROFILE_BIT) != 0;
        } else {
            compatibilityProfile = false;
            coreProfile = false;
        }
        System.out.println("\nGL_VERSION returned : " + version);
        System.out.println("\tCore profile: " + coreProfile);
        System.out.println("\tCompatibility profile: " + compatibilityProfile);
        System.out.println("ARB_compatibility present: " + GLContext.getCapabilities().GL_ARB_compatibility);
        System.out.println("Deprecated functionality present: " + deprecated);
        if (!deprecated && GLContext.getCapabilities().GL_ARB_compatibility) System.out.println("\tARB_compatibility is present, but LWJGL has enabled pseudo-forward compatible mode.");
        System.out.println("\n---------");
        boolean success = false;
        boolean check;
        if (majorInput < 3 || (majorInput == 3 && minorInput == 0)) {
            System.out.println("\nA version less than or equal to 3.0 is requested, the context\n" + "returned may implement any of the following versions:");
            System.out.println("\n1) Any version no less than that requested and no greater than 3.0.");
            check = (majorInput < majorVersion || (majorInput == majorVersion && minorInput <= minorVersion)) && (majorVersion < 3 || (majorVersion == 3 && minorVersion == 0));
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\n2) Version 3.1, if the GL_ARB_compatibility extension is also implemented.");
            check = majorVersion == 3 && minorVersion == 1 && GLContext.getCapabilities().GL_ARB_compatibility;
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\n3) The compatibility profile of version 3.2 or greater.");
            check = compatibilityProfile;
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\nTEST " + (success ? "SUCCEEDED" : "FAILED"));
            if (!success && ca.isForwardCompatible()) System.out.println("\t(probably because the forward compatible flag was set)");
        } else if (majorInput == 3 && minorInput == 1) {
            System.out.println("\nVersion 3.1 is requested, the context returned may implement\n" + "any of the following versions:");
            System.out.println("\n1) Version 3.1. The GL_ARB_compatibility extension may or may not\n" + "be implemented, as determined by the implementation.");
            check = majorVersion == 3 && minorVersion == 1;
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\n2) The core profile of version 3.2 or greater.");
            check = coreProfile;
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\nTEST " + (success ? "SUCCEEDED" : "FAILED"));
        } else {
            System.out.println("\nVersion 3.2 or greater is requested, the context returned may\n" + "implement any of the following versions:");
            System.out.println("\n1) The requested profile of the requested version.");
            check = majorInput == majorVersion && minorInput == minorVersion && (!ca.isProfileCompatibility() || compatibilityProfile) && (!ca.isProfileCore() || coreProfile);
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\n2) The requested profile of any later version, so long as no\n" + "features have been removed from that later version and profile.");
            check = majorInput < majorVersion || (majorInput == majorVersion && minorInput < minorVersion) && (!ca.isProfileCompatibility() || compatibilityProfile) && (!ca.isProfileCore() || coreProfile);
            System.out.println("\t" + check);
            success |= check;
            System.out.println("\nTEST " + (success ? "SUCCEEDED" : "FAILED"));
        }
    }

    private static DisplayMode chooseMode(DisplayMode[] modes, int width, int height) {
        DisplayMode bestMode = null;
        for (DisplayMode mode : modes) {
            if (mode.getWidth() == width && mode.getHeight() == height && mode.getFrequency() <= 85) {
                if (bestMode == null || (mode.getBitsPerPixel() >= bestMode.getBitsPerPixel() && mode.getFrequency() > bestMode.getFrequency())) bestMode = mode;
            }
        }
        return bestMode;
    }

    private static void cleanup() {
        if (Display.isCreated()) Display.destroy();
    }

    private static void argsError(final String msg) {
        System.out.println("\nInvalid arguments error: " + msg);
        System.out.println("\nUsage: VersionTest <majorVersion> <minorVersion> {'core'|'compatibility', <layer>, 'debug', 'fc'}:\n");
        System.out.println("majorVersion\t- Major OpenGL version.");
        System.out.println("majorVersion\t- Minor OpenGL version.");
        System.out.println("core\t- Sets the Core Profile bit (optional, requires 3.2+).");
        System.out.println("compatibility\t- Sets the Compatibility Profile bit (optional, requires 3.2+).");
        System.out.println("ws\t- Sets the OpenGL ES Profile bit (optional, requires 2.0).");
        System.out.println("layer\t- Layer plane (optional).");
        System.out.println("debug\t- Enables debug mode (optional).");
        System.out.println("fc\t- Enables forward compatibility mode (optional, requires 3.0+).");
        cleanup();
        System.exit(-1);
    }

    static void kill(String reason) {
        System.out.println("The VersionTest program was terminated because an error occured.\n");
        System.out.println("Reason: " + (reason == null ? "Unknown" : reason));
        cleanup();
        System.exit(-1);
    }
}
