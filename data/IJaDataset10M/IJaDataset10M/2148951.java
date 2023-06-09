package org.jpublish.util;

import java.util.Set;
import java.util.Stack;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.servlet.ServletContext;

/** Depth first iterator which iterates through all files which are decendents
    of the specified root path.
    
    @author Anthony Eden
    @since 2.0
*/
public class DepthFirstPathTreeIterator implements Iterator {

    private String root;

    private int currentIndex = 0;

    private String[] currentList;

    private Stack directories;

    private Stack indeces;

    private String nextFile;

    private boolean endOfTree = false;

    private ServletContext servletContext;

    /** Construct a new DepthFirstPathTreeIterator with the specified root.
    
        @param root The root path
    */
    public DepthFirstPathTreeIterator(String root, ServletContext servletContext) {
        this.root = root;
        this.servletContext = servletContext;
        this.currentIndex = 0;
        this.currentList = getPathArray(root);
        this.directories = new Stack();
        this.indeces = new Stack();
    }

    /** Returns true if the iteration has more elements. (In other words, 
        returns true if next would return an element rather than throwing 
        an exception.)
        
        @return True if the iteration has more elements
    */
    public boolean hasNext() {
        return false;
    }

    /** Returns the next element in the iteration.
    
        @return The next element in the iteration
    */
    public Object next() {
        throw new NoSuchElementException();
    }

    /** Removes from the underlying collection the last element returned by 
        the iterator (optional operation). This method can be called only 
        once per call to next. The behavior of an iterator is unspecified 
        if the underlying collection is modified while the iteration is in 
        progress in any way other than by calling this method. 

        @throws UnsupportedOperationException
    */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    protected String getNextFile() {
        if (nextFile == null) {
            nextFile = findNextFile();
        }
        return nextFile;
    }

    protected String findNextFile() {
        while (currentIndex < currentList.length) {
            if (currentList[currentIndex].endsWith("/")) {
                directories.push(currentList[currentIndex]);
                indeces.push(new Integer(currentIndex));
                currentIndex = 0;
            } else {
                String file = currentList[currentIndex];
                currentIndex++;
                return file;
            }
        }
        while (!directories.empty()) {
            String directory = (String) directories.pop();
            currentList = getPathArray(directory);
            currentIndex = ((Integer) indeces.pop()).intValue();
            String file = findNextFile();
            if (file != null) {
                return file;
            }
        }
        endOfTree = true;
        return null;
    }

    protected String[] getPathArray(String path) {
        Set pathSet = servletContext.getResourcePaths(path);
        return (String[]) pathSet.toArray(new String[pathSet.size()]);
    }
}
