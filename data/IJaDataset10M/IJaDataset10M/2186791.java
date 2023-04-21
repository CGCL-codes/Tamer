package launcher;

import java.lang.reflect.Method;

/**
 * ctl-run tool launcher
 */
public class CtlRunLauncher {

    private static final String QUEUE_TOOL_CLASSNAME = "com.controltier.ctl.cli.ctlrun.RunTool";

    public static void main(final String args[]) throws Exception {
        final ClassLoader loader = new CtlCLassLoader().getClassLoader();
        final Class cmdrClass;
        try {
            cmdrClass = Class.forName(QUEUE_TOOL_CLASSNAME, true, loader);
        } catch (ClassNotFoundException e) {
            throw new Exception("Caught ClassNotFoundException: " + e.getMessage());
        }
        final Method mainMethod = CtlCLassLoader.findMain(cmdrClass);
        if (null == mainMethod) {
            throw new Exception("no main() method in class: " + cmdrClass.getName());
        }
        mainMethod.invoke(null, new Object[] { args });
    }
}
