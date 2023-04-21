package ExamplesJaCoP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import JaCoP.fz.Fz2jacop;

/**
 * This class executes the provided example. It also allows to specify 
 * the arguments for the executed example.
 * 
 * @author Radoslaw Szymanek
 * @version 3.0
 *
 */
public class RunExample {

    /**
	 * It executes the example as specified by the first argument. The remaining arguments
	 * will be forwarded to the executed example.
	 * 
	 * @param args name of the example and its arguments.
	 */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify as the first argument the name of the example");
            System.out.println("All remaining arguments will be passed to the example.");
            System.out.println("The name of the example is either the class name from ExamplesJaCoP" + " or name of .fzn file.");
            System.exit(-1);
        }
        if (args[args.length - 1].endsWith(".fzn")) {
            Fz2jacop.main(args);
            return;
        } else try {
            Class<?>[] mainArgs = new Class[1];
            mainArgs[0] = Class.forName("[Ljava.lang.String;");
            Method mainMethod = Class.forName("ExamplesJaCoP." + args[0]).getMethod("main", mainArgs);
            if ((mainMethod.getModifiers() & Modifier.STATIC) == 0) {
                System.err.println("examplesloader: main method in target class is not static");
                System.exit(-1);
            }
            if (mainMethod.getReturnType() != void.class) {
                System.err.println("examplesloader: target class main must return void");
                System.exit(-1);
            }
            String[] processedArgs = new String[args.length - 1];
            System.arraycopy(args, 1, processedArgs, 0, args.length - 1);
            Object[] actualArgs = new Object[1];
            actualArgs[0] = (Object) processedArgs;
            mainMethod.invoke(null, actualArgs);
        } catch (ClassNotFoundException e) {
            System.err.println("exampleloader: can't find class \"" + e.getMessage() + "\" when loading target class \"" + args[0] + "\"");
            System.exit(-1);
        } catch (NoSuchMethodException e) {
            System.err.println("exampleloader: no main(String[]) method found in class " + args[0]);
            System.exit(-1);
        } catch (IllegalAccessException e) {
            System.err.println("exampleloader: error calling main method in class " + args[0]);
            System.exit(-1);
        } catch (InvocationTargetException e) {
            System.err.println("exampleloader: error calling main method in class " + args[0]);
            System.exit(-1);
        }
    }
}
