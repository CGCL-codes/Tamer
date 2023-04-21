package org.job.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A file filter which may be used to find classes through the class path. This
 * filter calls {@link #accept(Class)} for each file passed to method
 * {@link #accept(File)} if it is a class file. The list of accepted classes is
 * returned by method {@link #getAcceptedClasses()}.
 * @author Michael Watzek
 */
public class ClassPathFileFilter extends AbstractContextFileFilter {

    /** The list of classes accepted by method {@link #accept(Class)}. */
    protected List acceptedClasses = new ArrayList();

    /**
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File file) {
        boolean result = false;
        if (FileHelper.isClassFile(file)) {
            String className;
            Class clazz;
            try {
                File classPathElement = (File) getContext();
                className = FileHelper.getClassName(classPathElement, file);
                clazz = Class.forName(className);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (FileHelper.isConcreteClass(clazz) && accept(clazz)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Returns the list of classes accepted by method {@link #accept(Class)}.
     * @return the list of classes accepted by method {@link #accept(Class)}
     */
    public List getAcceptedClasses() {
        return this.acceptedClasses;
    }

    /**
     * Subclasses may override this method. If this method returns
     * <code>true</code> for the given <code>clazz</code>, then the given
     * <code>clazz</code> is accepted by this class path filter.
     * @param clazz the class
     * @return <code>true</code> to accept the given <code>clazz</code>
     */
    protected boolean accept(Class clazz) {
        this.acceptedClasses.add(clazz);
        return true;
    }
}
