package org.argouml.cognitive.ui;

import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;
import org.tigris.gef.util.VectorSet;

public class ToDoByPoster extends ToDoPerspective implements ToDoListListener {

    public ToDoByPoster() {
        super("todo.perspective.poster");
        addSubTreeModel(new GoListToPosterToItem());
    }

    public void toDoItemsChanged(ToDoListEvent tde) {
        Vector items = tde.getToDoItems();
        int nItems = items.size();
        Object path[] = new Object[2];
        path[0] = Designer.TheDesigner.getToDoList();
        VectorSet posters = Designer.theDesigner().getToDoList().getPosters();
        java.util.Enumeration enu = posters.elements();
        while (enu.hasMoreElements()) {
            Poster p = (Poster) enu.nextElement();
            path[1] = p;
            int nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                Poster post = item.getPoster();
                if (post != p) continue;
                nMatchingItems++;
            }
            if (nMatchingItems == 0) continue;
            int childIndices[] = new int[nMatchingItems];
            Object children[] = new Object[nMatchingItems];
            nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                Poster post = item.getPoster();
                if (post != p) continue;
                childIndices[nMatchingItems] = getIndexOfChild(p, item);
                children[nMatchingItems] = item;
                nMatchingItems++;
            }
            fireTreeNodesChanged(this, path, childIndices, children);
        }
    }

    public void toDoItemsAdded(ToDoListEvent tde) {
        Vector items = tde.getToDoItems();
        int nItems = items.size();
        Object path[] = new Object[2];
        path[0] = Designer.TheDesigner.getToDoList();
        VectorSet posters = Designer.theDesigner().getToDoList().getPosters();
        java.util.Enumeration enu = posters.elements();
        while (enu.hasMoreElements()) {
            Poster p = (Poster) enu.nextElement();
            path[1] = p;
            int nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                Poster post = item.getPoster();
                if (post != p) continue;
                nMatchingItems++;
            }
            if (nMatchingItems == 0) continue;
            int childIndices[] = new int[nMatchingItems];
            Object children[] = new Object[nMatchingItems];
            nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                Poster post = item.getPoster();
                if (post != p) continue;
                childIndices[nMatchingItems] = getIndexOfChild(p, item);
                children[nMatchingItems] = item;
                nMatchingItems++;
            }
            fireTreeNodesInserted(this, path, childIndices, children);
        }
    }

    public void toDoItemsRemoved(ToDoListEvent tde) {
        ToDoList list = Designer.TheDesigner.getToDoList();
        Vector items = tde.getToDoItems();
        int nItems = items.size();
        Object path[] = new Object[2];
        path[0] = Designer.TheDesigner.getToDoList();
        java.util.Enumeration enu = list.getPosters().elements();
        while (enu.hasMoreElements()) {
            Poster p = (Poster) enu.nextElement();
            path[1] = p;
            fireTreeStructureChanged(path);
        }
    }

    public void toDoListChanged(ToDoListEvent tde) {
    }
}
