package net.sf.hipster.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TagsFolder extends Folder {

    @SuppressWarnings("unchecked")
    @Override
    public List<HipsterTreeNode> getChildren() {
        List<HipsterTreeNode> taggedTasks = new ArrayList<HipsterTreeNode>();
        taggedTasks = Library.getInstance().getSession().createQuery("from Task as task where task.text like :tag").setParameter("tag", "%#%").setCacheable(true).list();
        HashSet<Tag> tagSet = new HashSet<Tag>();
        for (HipsterTreeNode task : taggedTasks) {
            tagSet.addAll(((Task) task).getTags());
        }
        List<HipsterTreeNode> tags = new ArrayList<HipsterTreeNode>();
        tags.addAll(tagSet);
        Collections.sort(tags);
        return tags;
    }
}
