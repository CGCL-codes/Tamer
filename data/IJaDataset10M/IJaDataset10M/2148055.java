package com.trnaWebapp.server;

import java.io.File;
import java.util.ArrayList;

/**
	 * Utility class collecing recuresevly all Files in a directory.
	 *
	 * @author ra
	 *
	 */
public class CollectFilesRecursiveLowMemory {

    /** All Files go here. */
    private ArrayList<String> collectedFilesVector = new ArrayList<String>();

    /**
	        * A method collecting all files in a directory recursively.
	        *
	        * @param directoryFile The Directory where I shoud descend.
	        * @return Vector a Vector containing all retrieved files in this directury
	        * and underneath.
	        */
    public ArrayList<String> collectAllFilesInDirectory(final String directoryFile) {
        traverse(directoryFile);
        return collectedFilesVector;
    }

    /**
	        * The recursive method. Going down and collecting everything
	        * can exclude specific filenames from collecting...
	        *
	        * @param dir File or directory. If File then added to the collectedFiles
	        * Vector if Directory it is called recursively.
	        * @param excludeFileList Array of Strings containing Filenames that
	        * should be excluded from this array.
	        */
    private void traverse(final String dirString) {
        File dir = new File(dirString);
        if (dir.isFile()) {
            collectedFilesVector.add(dir.getAbsolutePath());
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                traverse(dir.getPath() + File.separator + children[i]);
            }
        }
    }
}
