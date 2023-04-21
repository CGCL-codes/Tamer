package bookshepherd.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import bookshepherd.model.Element;
import bookshepherd.model.ExcerptGroup;
import bookshepherd.model.ExcerptItem;
import bookshepherd.model.ExcerptItemGroup;
import bookshepherd.model.ModelRoot;
import bookshepherd.model.ReferenceGroup;
import bookshepherd.model.ReferenceItem;
import bookshepherd.model.ReferenceItemGroup;

public class ModelController {

    private String file;

    private long fileLastModified;

    private boolean isModified;

    private ModelRoot modelRoot;

    private List<ModelListener> listeners;

    public ModelController() {
        listeners = new ArrayList<ModelListener>();
        modelRoot = new ModelRoot();
    }

    public long getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(long fileLastModified) {
        this.fileLastModified = fileLastModified;
    }

    public void setModelRoot(ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        if (file != null) {
            this.fileLastModified = new File(file).lastModified();
        }
        this.file = file;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public void addListener(ModelListener lst) {
        listeners.add(lst);
    }

    public void removeListener(ModelListener lst) {
        listeners.remove(lst);
    }

    public ModelRoot getModelRoot() {
        return modelRoot;
    }

    public List<ReferenceItem> getItems() {
        List<ReferenceItem> list = new ArrayList<ReferenceItem>();
        for (int i = 0; i < modelRoot.getRefItemCount(); i++) {
            list.add(modelRoot.getRefItem(i));
        }
        return list;
    }

    public List<ReferenceItem> getItems(final String sort) {
        List<ReferenceItem> list = getItems();
        Collections.sort(list, new Comparator<ReferenceItem>() {

            public int compare(ReferenceItem arg0, ReferenceItem arg1) {
                String str1 = arg0.getAttribute(sort);
                String str2 = arg1.getAttribute(sort);
                if (str1 != null && str2 != null) {
                    return str1.compareTo(str2);
                }
                return 0;
            }
        });
        return list;
    }

    public List<ReferenceItem> getItems(ReferenceGroup group, final String sort) {
        List<ReferenceItem> list = new ArrayList<ReferenceItem>();
        addItems(group, list, false);
        Collections.sort(list, new Comparator<ReferenceItem>() {

            public int compare(ReferenceItem arg0, ReferenceItem arg1) {
                String str1 = arg0.getAttribute(sort);
                String str2 = arg1.getAttribute(sort);
                if (str1 != null && str2 != null) {
                    return str1.compareTo(str2);
                }
                return 0;
            }
        });
        return list;
    }

    public List<ExcerptItem> getExcerptItems(List<ReferenceItem> refs, final String sort) {
        List<ExcerptItem> list = new ArrayList<ExcerptItem>();
        Iterator<ExcerptItem> it;
        for (ReferenceItem ref : refs) {
            it = ref.getExcerpts().iterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
        }
        if (sort != null) {
            Collections.sort(list, new Comparator<ExcerptItem>() {

                public int compare(ExcerptItem arg0, ExcerptItem arg1) {
                    String str1 = arg0.getAttribute(sort);
                    String str2 = arg1.getAttribute(sort);
                    if (str1 != null && str2 != null) {
                        return str1.compareTo(str2);
                    }
                    return 0;
                }
            });
        }
        return list;
    }

    protected void addItems(ReferenceGroup group, List<ReferenceItem> list, boolean inclChildren) {
        Iterator<ReferenceItemGroup> it = group.getItems().iterator();
        while (it.hasNext()) {
            list.add(it.next().getItem());
        }
        if (inclChildren) {
            for (int i = 0; i < group.getChildren().size(); i++) {
                addItems(group.getChildren().get(i), list, inclChildren);
            }
        }
    }

    public List<ReferenceItem> getItemsSortedById() {
        List<ReferenceItem> list = getItems();
        Collections.sort(list, new Comparator<ReferenceItem>() {

            public int compare(ReferenceItem arg0, ReferenceItem arg1) {
                if (arg0.getId() > arg1.getId()) {
                    return 1;
                } else if (arg0.getId() < arg1.getId()) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }

    public void insert(ReferenceItem ref) {
        modelRoot.addRefItem(ref);
        fireElementsInserted(new Element[] { ref });
    }

    public void insert(List<ReferenceItem> lst) {
        Element[] refl = new Element[lst.size()];
        ReferenceItem elem;
        for (int i = 0; i < lst.size(); i++) {
            elem = lst.get(i);
            modelRoot.addRefItem(elem);
            refl[i] = elem;
        }
        fireElementsInserted(refl);
    }

    public void insertE(ReferenceItem item, List<ExcerptItem> lst) {
        Element[] refl = new Element[lst.size()];
        ExcerptItem elem;
        for (int i = 0; i < lst.size(); i++) {
            elem = lst.get(i);
            item.getExcerpts().add(elem);
            elem.setReference(item);
            refl[i] = elem;
        }
        fireElementsInserted(refl);
    }

    public void insert(ReferenceGroup group, ReferenceGroup parent) {
        insert(group, parent, -1);
    }

    public void insert(ExcerptGroup group, ExcerptGroup parent) {
        insert(group, parent, -1);
    }

    public void change(ReferenceGroup group) {
        fireElementsChanged(new Element[] { group });
    }

    public void change(ExcerptGroup group) {
        fireElementsChanged(new Element[] { group });
    }

    public void change(ReferenceItem group) {
        fireElementsChanged(new Element[] { group });
    }

    public ExcerptGroup getExcerptTree() {
        return modelRoot.getExcerptGroup();
    }

    public void change(List<ReferenceItem> lst) {
        Element[] refl = new Element[lst.size()];
        ReferenceItem elem;
        for (int i = 0; i < lst.size(); i++) {
            elem = lst.get(i);
            refl[i] = elem;
        }
        fireElementsChanged(refl);
    }

    public void changeE(List<ExcerptItem> lst) {
        Element[] refl = new Element[lst.size()];
        ExcerptItem elem;
        for (int i = 0; i < lst.size(); i++) {
            elem = lst.get(i);
            refl[i] = elem;
        }
        fireElementsChanged(refl);
    }

    public void insert(ReferenceGroup group, ReferenceGroup parent, int index) {
        if (parent == null) {
            modelRoot.setRefGroup(group);
        } else {
            parent.addChild(group, index);
        }
        fireElementsInserted(new Element[] { group });
    }

    public void insert(ExcerptGroup group, ExcerptGroup parent, int index) {
        if (parent == null) {
            modelRoot.setExcerptGroup(group);
        } else {
            parent.addChild(group, index);
        }
        fireElementsInserted(new Element[] { group });
    }

    public void remove(ReferenceGroup group, ReferenceGroup parent) {
        if (parent == null) {
            return;
        } else {
            parent.getChildren().remove(group);
        }
        Iterator<ReferenceItemGroup> it = group.getItems().iterator();
        List<ReferenceItemGroup> list = new ArrayList<ReferenceItemGroup>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        for (ReferenceItemGroup rig : list) {
            rig.getGroup().removeItem(rig.getItem());
            rig.getItem().removeGroup(rig.getGroup());
        }
        fireElementsRemoved(new Element[] { group });
    }

    public void remove(ExcerptGroup group, ExcerptGroup parent) {
        if (parent == null) {
            return;
        } else {
            parent.getChildren().remove(group);
            Iterator<ExcerptItemGroup> it = group.getItems().iterator();
            List<ExcerptItemGroup> list = new ArrayList<ExcerptItemGroup>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            for (ExcerptItemGroup rig : list) {
                rig.getGroup().removeItem(rig.getItem());
                rig.getItem().removeGroup(rig.getGroup());
            }
        }
        fireElementsRemoved(new Element[] { group });
    }

    public void remove(ReferenceItem item) {
        modelRoot.removeRefItem(item);
        Iterator<ReferenceItemGroup> it = item.getGroups().iterator();
        ReferenceItemGroup gr;
        while (it.hasNext()) {
            gr = it.next();
            item.removeGroup(gr.getGroup());
            gr.getGroup().removeItem(item);
        }
        for (ExcerptItem exc : item.getExcerpts()) {
            remove(exc);
        }
        fireElementsRemoved(new Element[] { item });
    }

    public void remove(ExcerptItem item) {
        item.getReference().removeItem(item);
        Iterator<ExcerptItemGroup> it = item.getGroups().iterator();
        ExcerptItemGroup gr;
        while (it.hasNext()) {
            gr = it.next();
            item.removeGroup(gr.getGroup());
            gr.getGroup().removeItem(item);
        }
        fireElementsRemoved(new Element[] { item });
    }

    public void remove(List<ReferenceItem> lst) {
        Element[] refl = new Element[lst.size()];
        ReferenceItem elem;
        for (int i = 0; i < lst.size(); i++) {
            elem = lst.get(i);
            remove(elem);
            refl[i] = elem;
        }
        fireElementsRemoved(refl);
    }

    public ReferenceGroup getReferenceTree() {
        return modelRoot.getRefGroup();
    }

    public void fireElementsInserted(Element[] elems) {
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).inserted(elems);
        }
    }

    public void fireElementsRemoved(Element[] elems) {
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).removed(elems);
        }
    }

    public void fireElementsChanged(Element[] elems) {
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).changed(elems);
        }
    }
}
