package net.sourceforge.deco.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import net.sourceforge.deco.data.SrcModule;
import net.sourceforge.deco.helpers.RessourceNameHelper;
import org.objectweb.asm.Type;

public class RessourcesBasedSrcModule extends SrcModule {

    private final File jarFilename;

    private final JarFile jarFile;

    RessourcesBasedSrcModule(File jarFile) throws IOException {
        this.jarFilename = jarFile;
        this.jarFile = new JarFile(jarFile);
    }

    public boolean contains(Type t) {
        String classEntryName = RessourceNameHelper.getRessourceName(t);
        return jarFile.getEntry(classEntryName) != null;
    }

    public String getName() {
        return jarFilename.toString();
    }

    public Iterator<Type> iterator() {
        final Enumeration<JarEntry> entries = jarFile.entries();
        return new Iterator<Type>() {

            Type next = null;

            {
                updateNext();
            }

            public boolean hasNext() {
                return next != null;
            }

            public Type next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                Type r = next;
                updateNext();
                return r;
            }

            private void updateNext() {
                next = null;
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        next = RessourceNameHelper.getType(entry.getName());
                        break;
                    }
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    protected InputStream getByteCodeInputStream(Type t) throws IOException, ClassNotFoundException {
        String classEntryName = RessourceNameHelper.getRessourceName(t);
        ZipEntry jarEntry = jarFile.getEntry(classEntryName);
        if (jarEntry == null) {
            throw new ClassNotFoundException(t.getClassName());
        }
        return jarFile.getInputStream(jarEntry);
    }
}
