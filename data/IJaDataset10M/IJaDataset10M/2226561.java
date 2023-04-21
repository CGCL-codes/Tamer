package edu.mbhs.sclawren.lib.search;

import java.util.*;
import java.io.*;
import edu.mbhs.sclawren.lib.collection.*;

/**
 * 
 * @author Scott Lawrence
 *
 */
@SuppressWarnings("unchecked")
public class MultipleFileSearcher extends Searcher {

    ArrayList<File> files;

    public MultipleFileSearcher() {
        files = new ArrayList<File>();
    }

    @Override
    public ArrayList<OrderedObjectPair<String, Integer>> find(Object o) {
        return null;
    }

    public HashMap<String, Integer> getFrequencies(String o) {
        HashMap<String, Integer> freq = new HashMap<String, Integer>();
        for (File f : files) {
            FileSearcher s = new FileSearcher(f);
            freq.put(f.getPath(), s.getFrequency(o));
        }
        return freq;
    }

    public void addFile(String fileName) {
        files.add(new File(fileName));
    }

    public void addFile(File file) {
        files.add(file);
    }

    /**
	 * Adds a directory of files to the search space, including sub-directories.
	 * @param directoryName The name of the directory of which all files will be added
	 */
    public void addDirectory(String directoryName) {
        addDirectory(new File(directoryName));
    }

    /**
	 * Adds a directory of files to the search space, including sub-directories.
	 * @param directory The directory of which all files will be added
	 */
    public void addDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    addFile(f);
                } else if (f.isDirectory()) {
                    addDirectory(f);
                }
            }
        }
    }

    /**
	 * Adds a directory of files to the search space, including sub-directories.
	 * @param directory The directory of which all files will be added
	 * @param ext The required extension
	 */
    public void addDirectoryWithExtension(File directory, String ext) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File f : files) {
                if (f.isFile() && f.toString().endsWith("." + ext)) {
                    addFile(f);
                } else if (f.isDirectory()) {
                    addDirectory(f);
                }
            }
        }
    }

    /**
	 * Adds a directory of files to the search space, including sub-directories.
	 * @param directoryName The directory of which all files will be added
	 * @param ext The required extension
	 */
    public void addDirectoryWithExtension(String directoryName, String ext) {
        addDirectoryWithExtension(new File(directoryName), ext);
    }
}
