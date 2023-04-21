package org.netbeans.core.startup.layers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import org.openide.ErrorManager;
import org.openide.filesystems.*;

/** Extends LocalFileSystem by useful features. It is used as
 * delegates being part of SystemFileSystem.
 *
 * @author  Vita Stejskal
 */
public final class LocalFileSystemEx extends LocalFileSystem {

    /** name -> FileObject */
    private static HashMap allLocks = new HashMap(7);

    private static HashSet pLocks = new HashSet(7);

    public static String[] getLocks() {
        synchronized (allLocks) {
            removeInvalid(pLocks);
            LinkedList l = new LinkedList();
            l.addAll(allLocks.keySet());
            l.addAll(pLocks);
            return (String[]) l.toArray(new String[l.size()]);
        }
    }

    public static boolean hasLocks() {
        synchronized (allLocks) {
            removeInvalid(pLocks);
            return !allLocks.isEmpty() || !pLocks.isEmpty();
        }
    }

    public static void potentialLock(String name) {
        synchronized (allLocks) {
            pLocks.add(name);
        }
    }

    public static void potentialLock(String o, String n) {
        synchronized (allLocks) {
            if (pLocks.remove(o)) {
                pLocks.add(n);
            }
        }
    }

    private static void removeInvalid(Set names) {
        FileSystem sfs = Repository.getDefault().getDefaultFileSystem();
        Iterator i = names.iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            if (null == sfs.findResource(name)) {
                i.remove();
            }
        }
    }

    /** Creates new LocalFileSystemEx */
    public LocalFileSystemEx() {
        super();
    }

    protected void lock(String name) throws IOException {
        super.lock(name);
        synchronized (allLocks) {
            FileObject fo = findResource(name);
            allLocks.put(name, fo);
            pLocks.remove(name);
        }
    }

    protected void unlock(String name) {
        synchronized (allLocks) {
            if (allLocks.containsKey(name)) {
                allLocks.remove(name);
            } else {
                FileObject fo = findResource(name);
                if (fo != null) {
                    Iterator i = allLocks.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        if (fo.equals(entry.getValue())) {
                            allLocks.remove(entry.getKey());
                            break;
                        }
                    }
                } else {
                    ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, new Throwable("Can't unlock file " + name + ", it's lock was not found or it wasn't locked."));
                }
            }
        }
        super.unlock(name);
    }
}
