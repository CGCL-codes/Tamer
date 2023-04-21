package org.apache.harmony.awt.datatransfer.appengine;

import org.apache.harmony.awt.datatransfer.DTK;
import org.apache.harmony.awt.datatransfer.NativeClipboard;
import com.google.code.appengine.awt.dnd.DragGestureEvent;
import com.google.code.appengine.awt.dnd.DropTargetContext;
import com.google.code.appengine.awt.dnd.peer.DragSourceContextPeer;
import com.google.code.appengine.awt.dnd.peer.DropTargetContextPeer;

public class AppEngineDTK extends DTK {

    @Override
    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dge) {
        return null;
    }

    @Override
    public DropTargetContextPeer createDropTargetContextPeer(DropTargetContext context) {
        return null;
    }

    @Override
    public void initDragAndDrop() {
    }

    @Override
    protected NativeClipboard newNativeClipboard() {
        return null;
    }

    @Override
    protected NativeClipboard newNativeSelection() {
        return null;
    }

    @Override
    public void runEventLoop() {
    }
}
