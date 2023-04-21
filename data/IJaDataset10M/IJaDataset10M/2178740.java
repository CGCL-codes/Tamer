package net.sourceforge.deco.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.deco.Logger;
import net.sourceforge.deco.helpers.RessourceNameHelper;
import org.objectweb.asm.Type;

public class DirBasedModule extends SrcModule {

    private final File baseDir;

    private final String name;

    private final Logger logger;

    private final Set<Type> types = new HashSet<Type>();

    public DirBasedModule(File baseDir, Logger logger) {
        this.baseDir = baseDir;
        this.logger = logger;
        this.name = baseDir.getName();
        scanDir(baseDir, "");
    }

    private void scanDir(File dir, String parentDir) {
        if (!dir.exists()) {
            logger.warning(dir + " doesn't exists");
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                scanDir(f, parentDir + f.getName() + "/");
            } else if (f.getName().endsWith(".class")) {
                types.add(RessourceNameHelper.getType(parentDir + f.getName()));
            }
        }
    }

    @Override
    public boolean contains(Type t) {
        return types.contains(t);
    }

    @Override
    protected InputStream getByteCodeInputStream(Type t) throws IOException, ClassNotFoundException {
        String classFileName = RessourceNameHelper.getRessourceName(t);
        File classFile = new File(baseDir, classFileName);
        if (!classFile.exists()) {
            throw new ClassNotFoundException(t.getClassName());
        } else {
            return new FileInputStream(classFile);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public Iterator<Type> iterator() {
        return types.iterator();
    }
}
