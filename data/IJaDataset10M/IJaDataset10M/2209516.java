package com.tensegrity.palowebviewer.modules.widgets.client.text;

import java.util.ArrayList;
import java.util.List;
import com.tensegrity.palowebviewer.modules.util.client.Assertions;

public class DocumentListenerCollection implements IDocumentListener {

    private final List listenerList = new ArrayList();

    public void addListener(IDocumentListener listener) {
        Assertions.assertNotNull(listener, "Listener");
        listenerList.add(listener);
    }

    public void removeListener(IDocumentListener listener) {
        listenerList.remove(listener);
    }

    public void onEditStart() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IDocumentListener listener = (IDocumentListener) listeners[i];
            listener.onEditStart();
        }
    }

    public void onEditStop() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IDocumentListener listener = (IDocumentListener) listeners[i];
            listener.onEditStop();
        }
    }

    public void onTextChanged() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IDocumentListener listener = (IDocumentListener) listeners[i];
            listener.onTextChanged();
        }
    }
}
