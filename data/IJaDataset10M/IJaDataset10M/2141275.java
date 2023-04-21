package name.nirav.refactoringman.refactorings;

import java.util.ArrayList;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;

@SuppressWarnings("unchecked")
public class TestBuffer implements IBuffer {

    IOpenable owner;

    ArrayList changeListeners;

    char[] contents = null;

    boolean hasUnsavedChanges = false;

    public TestBuffer(IOpenable owner) {
        this.owner = owner;
    }

    public void addBufferChangedListener(IBufferChangedListener listener) {
        if (this.changeListeners == null) {
            this.changeListeners = new ArrayList(5);
        }
        if (!this.changeListeners.contains(listener)) {
            this.changeListeners.add(listener);
        }
    }

    public void append(char[] text) {
        this.hasUnsavedChanges = true;
        notifyListeners();
    }

    public void append(String text) {
        this.hasUnsavedChanges = true;
        notifyListeners();
    }

    public void close() {
        this.contents = null;
        if (this.changeListeners != null) {
            notifyListeners();
            this.changeListeners = null;
        }
    }

    private void notifyListeners() {
        if (this.changeListeners == null) return;
        BufferChangedEvent event = null;
        event = new BufferChangedEvent(this, 0, 0, null);
        for (int i = 0, size = this.changeListeners.size(); i < size; ++i) {
            IBufferChangedListener listener = (IBufferChangedListener) this.changeListeners.get(i);
            listener.bufferChanged(event);
        }
    }

    public char getChar(int position) {
        return 0;
    }

    public char[] getCharacters() {
        return this.contents;
    }

    public String getContents() {
        return new String(this.contents);
    }

    public int getLength() {
        return this.contents.length;
    }

    public IOpenable getOwner() {
        return this.owner;
    }

    public String getText(int offset, int length) {
        return null;
    }

    public IResource getUnderlyingResource() {
        return null;
    }

    public boolean hasUnsavedChanges() {
        return this.hasUnsavedChanges;
    }

    public boolean isClosed() {
        return this.contents == null;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void removeBufferChangedListener(IBufferChangedListener listener) {
        if (this.changeListeners != null) {
            this.changeListeners.remove(listener);
            if (this.changeListeners.size() == 0) {
                this.changeListeners = null;
            }
        }
    }

    public void replace(int position, int length, char[] text) {
        this.hasUnsavedChanges = true;
        notifyListeners();
    }

    public void replace(int position, int length, String text) {
        this.hasUnsavedChanges = true;
        notifyListeners();
    }

    public void save(IProgressMonitor progress, boolean force) {
        this.hasUnsavedChanges = false;
    }

    public void setContents(char[] characters) {
        this.contents = characters;
        this.hasUnsavedChanges = true;
        notifyListeners();
    }

    public void setContents(String characters) {
        this.contents = characters.toCharArray();
        this.hasUnsavedChanges = true;
        notifyListeners();
    }
}
