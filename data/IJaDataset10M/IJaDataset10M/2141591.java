package org.argouml.cognitive.ui;

import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;

public class ToDoByType extends ToDoPerspective implements ToDoListListener {

    public ToDoByType() {
        super("todo.perspective.type");
        addSubTreeModel(new GoListToTypeToItem());
    }

    public void toDoItemsChanged(ToDoListEvent tde) {
        Vector items = tde.getToDoItems();
        int nItems = items.size();
        Object path[] = new Object[2];
        path[0] = Designer.TheDesigner.getToDoList();
        java.util.Enumeration enu = KnowledgeTypeNode.getTypes().elements();
        while (enu.hasMoreElements()) {
            KnowledgeTypeNode ktn = (KnowledgeTypeNode) enu.nextElement();
            String kt = ktn.getName();
            path[1] = ktn;
            int nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (!item.containsKnowledgeType(kt)) continue;
                nMatchingItems++;
            }
            if (nMatchingItems == 0) continue;
            int childIndices[] = new int[nMatchingItems];
            Object children[] = new Object[nMatchingItems];
            nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (!item.containsKnowledgeType(kt)) continue;
                childIndices[nMatchingItems] = getIndexOfChild(ktn, item);
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
        java.util.Enumeration enu = KnowledgeTypeNode.getTypes().elements();
        while (enu.hasMoreElements()) {
            KnowledgeTypeNode ktn = (KnowledgeTypeNode) enu.nextElement();
            String kt = ktn.getName();
            path[1] = ktn;
            int nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (!item.containsKnowledgeType(kt)) continue;
                nMatchingItems++;
            }
            if (nMatchingItems == 0) continue;
            int childIndices[] = new int[nMatchingItems];
            Object children[] = new Object[nMatchingItems];
            nMatchingItems = 0;
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (!item.containsKnowledgeType(kt)) continue;
                childIndices[nMatchingItems] = getIndexOfChild(ktn, item);
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
        java.util.Enumeration enu = KnowledgeTypeNode.getTypes().elements();
        while (enu.hasMoreElements()) {
            KnowledgeTypeNode ktn = (KnowledgeTypeNode) enu.nextElement();
            boolean anyInKT = false;
            String kt = ktn.getName();
            for (int i = 0; i < nItems; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (item.containsKnowledgeType(kt)) anyInKT = true;
            }
            if (!anyInKT) continue;
            path[1] = ktn;
            fireTreeStructureChanged(path);
        }
    }

    public void toDoListChanged(ToDoListEvent tde) {
    }
}
