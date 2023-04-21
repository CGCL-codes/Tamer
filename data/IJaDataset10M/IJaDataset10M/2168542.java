package org.ocl4java.jdt;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Classpath-List to be returned by JDTProjectHelper
 * that inizialized lazy on the first list-access.
 */
public class LazyClasspathList extends LinkedList<File> {

    private IJavaProject javaProject;

    private boolean initialized = false;

    public LazyClasspathList(IJavaProject javaProject) {
        super();
        if (javaProject == null) throw new IllegalArgumentException("null javaProject given!");
        this.javaProject = javaProject;
    }

    private synchronized void initialize() {
        if (initialized) return;
        initialized = true;
        try {
            addAll(JDTProjectHelper.getSourcesNonLazy(javaProject));
        } catch (JavaModelException x) {
            x.printStackTrace();
        }
    }

    public int size() {
        if (!initialized) initialize();
        return super.size();
    }

    public boolean isEmpty() {
        if (!initialized) initialize();
        return super.isEmpty();
    }

    public boolean contains(java.lang.Object arg0) {
        if (!initialized) initialize();
        return super.contains(arg0);
    }

    public java.util.Iterator iterator() {
        if (!initialized) initialize();
        return super.iterator();
    }

    public java.lang.Object[] toArray() {
        if (!initialized) initialize();
        return super.toArray();
    }

    public java.lang.Object[] toArray(java.lang.Object[] arg0) {
        if (!initialized) initialize();
        return super.toArray(arg0);
    }

    public boolean remove(java.lang.Object arg0) {
        if (!initialized) initialize();
        return super.remove(arg0);
    }

    public boolean containsAll(java.util.Collection arg0) {
        if (!initialized) initialize();
        return super.containsAll(arg0);
    }

    public boolean addAll(java.util.Collection arg0) {
        if (!initialized) initialize();
        return super.addAll(arg0);
    }

    public boolean addAll(int arg0, java.util.Collection arg1) {
        if (!initialized) initialize();
        return super.addAll(arg0, arg1);
    }

    public boolean removeAll(java.util.Collection arg0) {
        if (!initialized) initialize();
        return super.removeAll(arg0);
    }

    public boolean retainAll(java.util.Collection arg0) {
        if (!initialized) initialize();
        return super.retainAll(arg0);
    }

    public void clear() {
        if (!initialized) initialized = true;
        super.clear();
    }

    public boolean equals(java.lang.Object arg0) {
        if (!initialized) initialize();
        return super.equals(arg0);
    }

    public int hashCode() {
        if (!initialized) initialize();
        return super.hashCode();
    }

    public File get(int arg0) {
        if (!initialized) initialize();
        return super.get(arg0);
    }

    public File set(int arg0, File arg1) {
        if (!initialized) initialize();
        return super.set(arg0, arg1);
    }

    public void add(int arg0, File arg1) {
        if (!initialized) initialize();
        super.add(arg0, arg1);
    }

    public File remove(int arg0) {
        if (!initialized) initialize();
        return super.remove(arg0);
    }

    public int indexOf(java.lang.Object arg0) {
        if (!initialized) initialize();
        return super.indexOf(arg0);
    }

    public int lastIndexOf(java.lang.Object arg0) {
        if (!initialized) initialize();
        return super.lastIndexOf(arg0);
    }

    public ListIterator<File> listIterator() {
        if (!initialized) initialize();
        return super.listIterator();
    }

    public ListIterator<File> listIterator(int arg0) {
        if (!initialized) initialize();
        return super.listIterator(arg0);
    }

    public List<File> subList(int arg0, int arg1) {
        if (!initialized) initialize();
        return super.subList(arg0, arg1);
    }
}
