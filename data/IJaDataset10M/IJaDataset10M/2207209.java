package edu.mbhs.omnicon.engine.model;

import java.util.*;
import java.util.logging.*;
import java.io.*;
import edu.mbhs.omnicon.*;
import edu.mbhs.omnicon.engine.*;
import edu.mbhs.omnicon.exceptions.*;
import javax.swing.*;
import javax.swing.event.*;

public class FileList implements ListModel, EngineComponent {

    private static FileList singleton;

    private static final Logger l = Logger.getLogger("Engine");

    private HashMap<String, FileInfo> files;

    private List<ListDataListener> listeners = new LinkedList<ListDataListener>();

    private FileList() {
        l.fine("Creating FileList");
        files = new HashMap<String, FileInfo>();
        readFileList();
    }

    @SuppressWarnings("unchecked")
    private void readFileList() {
        l.fine("Reading file list");
        File f = new File(System.getProperty("user.home") + File.separator + Messages.getString("appName"));
        f.mkdir();
        f = new File(f, ".files");
        l.config("Location of file list: " + f.toString());
        try {
            if (!f.exists()) {
                l.fine("File list does not exist, creating");
                f.createNewFile();
            } else {
                l.fine("Reading from file list");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Object o = ois.readObject();
                files = (HashMap<String, FileInfo>) o;
            }
        } catch (Exception e) {
            l.severe("Exception occured when reading file list, aborting");
            l.info("Possible cause is application upgrade");
            l.severe("File list will be empty");
            files.clear();
            return;
        }
    }

    private void writeList() {
        l.fine("Writing file list");
        File f = new File(System.getProperty("user.home") + File.separator + Messages.getString("appName"));
        f = new File(f, ".files");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(files);
        } catch (Exception e) {
            l.severe("Exception occured when writing file list, aborting");
            l.severe("Data will not be written");
        }
    }

    public FileInfo getFileInfo(String s) {
        return files.get(s);
    }

    public static void writeFileList() {
        getFileList().writeList();
    }

    public ArrayList<String> getFileNames() {
        ArrayList<String> n = new ArrayList<String>(files.keySet());
        Collections.sort(n);
        return n;
    }

    public static FileList getFileList() {
        if (singleton == null) singleton = new FileList();
        return singleton;
    }

    /**
	 * Does nothing
	 */
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public Object getElementAt(int index) {
        return getFileNames().get(index);
    }

    public int getSize() {
        return files.size();
    }

    /**
	 * Removes the data listener.
	 * @see ListModel#removeListDataListener(ListDataListener)
	 */
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void addFile(String name) throws DuplicateFileException {
        if (files.containsKey(name)) throw new DuplicateFileException(name);
        files.put(name, new FileInfo(name));
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getFileNames().indexOf(name), getFileNames().indexOf(name)));
        }
    }

    public void removeFile(String name) throws NonexistentFileException {
        if (!files.containsKey(name)) throw new NonexistentFileException(name);
        for (ListDataListener l : listeners) {
            l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, getFileNames().indexOf(name), getFileNames().indexOf(name)));
        }
        files.remove(name);
    }

    public String toString() {
        return "FileList";
    }

    @Override
    public List<EngineComponent> getChildren() {
        List<EngineComponent> r = new ArrayList<EngineComponent>();
        r.addAll(files.values());
        return r;
    }

    @Override
    public List<Object> getObjects() {
        List<Object> r = new ArrayList<Object>();
        r.add(files);
        r.add(listeners);
        return r;
    }
}
