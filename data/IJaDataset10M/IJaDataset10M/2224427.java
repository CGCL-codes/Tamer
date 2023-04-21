package org.simplextensions.scanner.impl;

import javassist.bytecode.ClassFile;
import org.simplextensions.scanner.ClassFileIterator;
import org.simplextensions.scanner.ClassScannerBundle;
import org.simplextensions.scanner.IClassScanner;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassLoaderCSB extends ClassScannerBundle {

    public class ClassLoaderIterator extends ClassFileIterator {

        private Iterator<String> classNamesIterator;

        public ClassLoaderIterator(ClassLoaderCSB csb) {
            super(csb);
            classNamesIterator = csb.getClassNames().iterator();
        }

        @Override
        protected ClassFile searchNext() {
            if (!classNamesIterator.hasNext()) return null;
            String className = classNamesIterator.next();
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
                if (resourceAsStream != null) return new ClassFile(new DataInputStream(resourceAsStream));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (resourceAsStream != null) {
                    try {
                        resourceAsStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    public ClassLoaderCSB(String name, Map<String, Map<Integer, List<IClassScanner>>> scanListeners, Set<String> classNames) {
        super(name, scanListeners, classNames);
    }

    public Iterator<ClassFile> classFileIterator() {
        return new ClassLoaderIterator(this);
    }
}
