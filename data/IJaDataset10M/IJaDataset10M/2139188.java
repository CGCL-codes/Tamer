package org.armedbear.j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public final class BufferList implements Constants, PreferencesChangeListener {

    private final ArrayList list = new ArrayList();

    private boolean alpha;

    private boolean reorder;

    private boolean modified;

    public BufferList() {
        Preferences p = Editor.preferences();
        if (p != null) {
            alpha = p.getBooleanProperty(Property.SORT_BUFFER_LIST);
            if (alpha) reorder = false; else reorder = p.getIntegerProperty(Property.REORDER_BUFFERS) > 0;
            p.addPreferencesChangeListener(this);
        } else Debug.bug();
    }

    public synchronized Iterator iterator() {
        if (alpha && modified) sort();
        return list.iterator();
    }

    public synchronized void add(Buffer buf) {
        list.add(buf);
        modified = true;
    }

    public synchronized boolean remove(Buffer buf) {
        return list.remove(buf);
    }

    public synchronized boolean move(Buffer buf, int index) {
        if (index < 0 || index >= size()) return false;
        int previousIndex = list.indexOf(buf);
        if (index == previousIndex || previousIndex < 0) return false;
        list.remove(previousIndex);
        list.add(index, buf);
        modified();
        return true;
    }

    public synchronized int size() {
        return list.size();
    }

    public synchronized boolean contains(Buffer buf) {
        return indexOf(buf) >= 0;
    }

    public synchronized Buffer getFirstBuffer() {
        if (list.size() > 0) {
            if (alpha && modified) sort();
            return (Buffer) list.get(0);
        }
        return null;
    }

    public synchronized Buffer getNextPrimaryBuffer(Buffer buffer) {
        if (alpha && modified) sort();
        if (buffer.isSecondary()) {
            Debug.assertTrue(buffer.getPrimary() != null);
            Debug.assertTrue(buffer.getPrimary().isPrimary());
            Debug.assertFalse(buffer.getPrimary().isSecondary());
            return getNextPrimaryBuffer(buffer.getPrimary());
        }
        int index = indexOf(buffer);
        if (index < 0) return null;
        while (true) {
            if (index < size() - 1) ++index; else index = 0;
            Buffer buf = (Buffer) list.get(index);
            if (buf == buffer) break;
            if (buf.isPrimary()) return buf;
        }
        return null;
    }

    public synchronized Buffer getPreviousPrimaryBuffer(Buffer buffer) {
        if (alpha && modified) sort();
        if (buffer.isSecondary()) {
            Debug.assertTrue(buffer.getPrimary() != null);
            Debug.assertTrue(buffer.getPrimary().isPrimary());
            Debug.assertFalse(buffer.getPrimary().isSecondary());
            return getPreviousPrimaryBuffer(buffer.getPrimary());
        }
        int index = indexOf(buffer);
        if (index < 0) return null;
        while (true) {
            if (index > 0) --index; else index = size() - 1;
            Buffer buf = (Buffer) list.get(index);
            if (buf == buffer) break;
            if (buf.isPrimary()) return buf;
        }
        return null;
    }

    public synchronized Buffer findBuffer(File f) {
        if (f != null) {
            for (int i = list.size(); i-- > 0; ) {
                Buffer buf = (Buffer) list.get(i);
                if (buf instanceof WebBuffer) continue;
                if (f.equals(buf.getFile())) return buf;
            }
        }
        return null;
    }

    public synchronized void makeNext(final Buffer nextBuffer, final Buffer currentBuffer) {
        if (!reorder) return;
        if (currentBuffer == null) Log.debug("makeNext currentBuffer is null size = " + list.size());
        if (currentBuffer != null) Debug.assertTrue(list.contains(currentBuffer));
        Debug.assertTrue(nextBuffer != null);
        Debug.assertTrue(list.contains(nextBuffer));
        if (nextBuffer == currentBuffer) return;
        remove(nextBuffer);
        try {
            for (int i = 0; i < list.size(); i++) {
                Buffer buf = (Buffer) list.get(i);
                if (buf == currentBuffer) {
                    list.add(i + 1, nextBuffer);
                    return;
                }
            }
            Debug.assertTrue(currentBuffer == null || !list.contains(currentBuffer));
            Debug.assertFalse(list.contains(nextBuffer));
            list.add(nextBuffer);
        } finally {
            if (currentBuffer != null) Debug.assertTrue(list.contains(currentBuffer));
            Debug.assertTrue(list.contains(nextBuffer));
        }
    }

    public synchronized void replace(Buffer o, Buffer n) {
        Debug.assertTrue(list.contains(o));
        Debug.assertTrue(list.contains(n));
        for (EditorIterator iter = new EditorIterator(); iter.hasNext(); ) {
            Editor ed = iter.nextEditor();
            if (ed.getBuffer() == o) ed.activate(n);
            ed.views.remove(o);
            if (ed.getBuffer() == n) ed.updateDisplay();
        }
        list.remove(n);
        Debug.assertFalse(list.contains(n));
        for (int i = list.size(); i-- > 0; ) {
            if (list.get(i) == o) {
                list.set(i, n);
                modified = true;
                break;
            }
        }
        Debug.assertTrue(list.contains(n));
        Debug.assertFalse(list.contains(o));
        Sidebar.setUpdateFlagInAllFrames(SIDEBAR_BUFFER_LIST_CHANGED);
    }

    public final synchronized void modified() {
        modified = true;
    }

    public synchronized void preferencesChanged() {
        Preferences p = Editor.preferences();
        boolean b = p.getBooleanProperty(Property.SORT_BUFFER_LIST);
        if (b != alpha) {
            alpha = b;
            if (alpha) {
                sort();
                Sidebar.setUpdateFlagInAllFrames(SIDEBAR_BUFFER_LIST_CHANGED);
            }
        }
        if (alpha) reorder = false; else reorder = p.getIntegerProperty(Property.REORDER_BUFFERS) > 0;
    }

    private static final String userHome = Platform.isPlatformUnix() ? Utilities.getUserHome() : null;

    public synchronized String getUniqueName(Buffer buf) {
        final File file = buf.getFile();
        final String name = file.getName();
        boolean qualify = false;
        Debug.assertTrue(file != null);
        Debug.assertTrue(file.isLocal());
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Buffer b = (Buffer) it.next();
            if (b == buf) continue;
            if (b.getFile() != null && b.getFile().getName().equals(name)) {
                qualify = true;
                break;
            }
        }
        if (qualify) {
            FastStringBuffer sb = new FastStringBuffer(name);
            sb.append(" [");
            String dir = file.getParent();
            if (userHome != null && userHome.length() > 0) {
                if (dir.equals(userHome)) dir = "~";
                if (dir.startsWith(userHome.concat("/"))) dir = "~".concat(dir.substring(userHome.length()));
            }
            sb.append(dir);
            sb.append(']');
            return sb.toString();
        }
        return name;
    }

    public int indexOf(Buffer buf) {
        for (int i = list.size(); i-- > 0; ) {
            if (list.get(i) == buf) return i;
        }
        return -1;
    }

    private static Comparator comparator;

    private void sort() {
        if (alpha) {
            if (comparator == null) {
                comparator = new Comparator() {

                    public int compare(Object o1, Object o2) {
                        return o1.toString().compareToIgnoreCase(o2.toString());
                    }
                };
            }
            Collections.sort(list, comparator);
        }
        modified = false;
    }
}
