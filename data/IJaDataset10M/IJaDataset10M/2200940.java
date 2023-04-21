package org.jazzteam.snipple.plugin.views.remote;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jazzteam.snipple.plugin.model.Snippet;
import org.jazzteam.snipple.plugin.storage.remote.RemoteSnippetsMemoryStorage;

/**
 * Adapter between remote snippets view and remote snippets memory storage
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public class RemoteSnippetTableContentProvider implements IStructuredContentProvider {

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

    public Object[] getElements(Object parent) {
        RemoteSnippetsMemoryStorage storage = RemoteSnippetsMemoryStorage.getInstance();
        List<Snippet> snippets = storage.getSnippets();
        return snippets.toArray(new Object[snippets.size()]);
    }
}
