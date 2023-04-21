package info.jonclark.lang;

import info.jonclark.util.FileUtils;
import info.jonclark.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.python.util.PythonInterpreter;

/**
 * JythonFactory to allow using Jython implementations of Java interfaces as
 * Java objects.
 */
public class JythonFactory {

    public static <T> T get(File pathToJythonModule, Class<T> javaInterface) {
        T javaInt = null;
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(pathToJythonModule.getAbsolutePath());
        String tempName = StringUtils.substringBefore(pathToJythonModule.getName(), ".");
        System.out.println(tempName);
        String instanceName = tempName.toLowerCase();
        String objectDef = "=" + tempName + "()";
        interpreter.exec(instanceName + objectDef);
        javaInt = getInstance(javaInterface, interpreter, instanceName);
        return javaInt;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getInstance(Class<T> javaInterface, PythonInterpreter interpreter, String instanceName) {
        return (T) interpreter.get(instanceName).__tojava__(javaInterface);
    }

    /**
	 * Scans the given directory for .py and .jy plugins that implement the
	 * given Java interface
	 * 
	 * @param <T>
	 * @param directory
	 * @param javaInterface
	 * @return
	 * @throws IOException
	 */
    public static <T> ArrayList<File> scanDirectoryForPlugins(File directory, Class<T> javaInterface) throws IOException {
        String interfaceName = javaInterface.getName();
        String interfacePattern = "(" + interfaceName + "):";
        File[] candidates = FileUtils.getFilesWithExt(directory, ".py", ".jy");
        ArrayList<File> plugins = new ArrayList<File>();
        for (File candidate : candidates) {
            BufferedReader in = new BufferedReader(new FileReader(candidate));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("class ") && line.endsWith(interfacePattern)) {
                    plugins.add(candidate);
                    break;
                }
            }
            in.close();
        }
        return plugins;
    }

    /***************************************************************************
	 * TEST CODE:
	 */
    public static interface Z {

        public void z();
    }

    public static void main(String[] args) throws Exception {
        File f = new File("/Users/jon/Documents/workspace/letras/scripts/Jythontest.jy");
        Z z = get(f, Z.class);
        z.z();
    }
}
