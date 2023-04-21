package net.sf.opendf.cal.i2.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.opendf.cal.i2.Environment;
import net.sf.opendf.cal.i2.environment.DynamicEnvironmentFrame;
import net.sf.opendf.cal.i2.environment.SingleEntryEnvironment;
import net.sf.opendf.cal.i2.shell.Shell;
import net.sf.opendf.cal.interpreter.util.NullOutputStream;
import net.sf.opendf.cal.interpreter.util.UnsatisfiedImportException;

/**
 * This import handler imports the definitions made as the result of executing a Cal
 * script file (see also {@link net.sf.opendf.cal.shell.Shell the shell definition}).
 * 
 * It reads the script as the path corresponding to the package name plus the 
 * extension ".cal", relative to the class loader. If such a file 
 * exists, it is executed as a script and the resulting
 * bindings become the package that is either imported as a whole, or imported
 * from.
 * 
 * @author jornj
 */
public class CalScriptImportHandler extends AbstractImportHandler {

    protected Environment extendWithPackageImport(Environment env, String packagePrefix) {
        Map m = loadShellScript(getPlatform(), packagePrefix);
        if (m == null) return null;
        DynamicEnvironmentFrame v = new DynamicEnvironmentFrame(env);
        for (Iterator i = m.keySet().iterator(); i.hasNext(); ) {
            Object var = i.next();
            v.bind(var, m.get(var), null);
        }
        return v;
    }

    protected Environment extendWithSingleImport(Environment env, String packagePrefix, String className, String alias) {
        Map m = loadShellScript(getPlatform(), packagePrefix);
        if (m == null) return null;
        if (m.keySet().contains(className)) {
            return new SingleEntryEnvironment(env, alias, m.get(className), null);
        } else {
            throw new UnsatisfiedImportException(packagePrefix, className, alias);
        }
    }

    public CalScriptImportHandler(Platform platform) {
        this(platform, CalScriptImportHandler.class.getClassLoader());
    }

    public CalScriptImportHandler(Platform platform, ClassLoader classLoader) {
        super(platform.configuration());
        this.classLoader = classLoader;
        this.platform = platform;
    }

    private Map loadShellScript(Platform platform, String packagePrefix) {
        InputStream in = null;
        try {
            String scriptName = packagePrefix.replace('.', '/') + ".cal";
            in = classLoader.getResourceAsStream(scriptName);
            if (in == null) return null;
            Shell shell = new Shell(platform, new HashMap(), in, NullOutputStream.devNull, NullOutputStream.devNull, false);
            return shell.executeAll();
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private Platform getPlatform() {
        return platform;
    }

    private ClassLoader classLoader;

    private Platform platform;
}
