package org.deft.repository.xfsr.reference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.deft.repository.RepositoryFactory;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.Fragment;
import org.deft.repository.fragment.Project;

public abstract class CollectorManager<C extends FragmentRefCollector<R, F>, R extends Reference, F extends Fragment> {

    protected C tempAddRefs;

    protected Set<R> tempRemoveRefSet = new HashSet<R>();

    protected Map<UUID, C> refsMap = new HashMap<UUID, C>();

    public CollectorManager() {
        tempAddRefs = createCollector();
    }

    protected abstract C createCollector();

    public C getReferences(Project project) {
        C refs = refsMap.get(project.getUUID());
        if (refs == null) {
            refs = createCollector();
            refsMap.put(project.getUUID(), refs);
        }
        return refs;
    }

    public List<R> getCommitedReferences(Chapter chapter) {
        Project project = RepositoryFactory.getRepository().getProjectOf(chapter);
        List<R> retList = new LinkedList<R>();
        if (project != null) {
            retList.addAll(getReferences(project).getReferencesFromChapter(chapter));
        }
        return retList;
    }

    public List<R> getCommitedReferences(F fragment) {
        Project project = RepositoryFactory.getRepository().getProjectOf(fragment);
        List<R> retList = new LinkedList<R>();
        if (project != null) {
            retList.addAll(getReferences(project).getReferencesFromFragment(fragment));
        }
        return retList;
    }

    public List<R> getTemporarilyAddedReferences(Chapter chapter) {
        List<R> retList = new LinkedList<R>();
        retList.addAll(tempAddRefs.getReferencesFromChapter(chapter));
        return retList;
    }

    public List<R> getTemporarilyAddedReferences(F fragment) {
        List<R> retList = new LinkedList<R>();
        retList.addAll(tempAddRefs.getReferencesFromFragment(fragment));
        return retList;
    }

    public List<R> getTemporarilyRemovedReferences(Chapter chapter) {
        List<R> retList = new LinkedList<R>();
        for (R ref : tempRemoveRefSet) {
            if (ref.getChapter() == chapter) {
                retList.add(ref);
            }
        }
        return retList;
    }

    public List<R> getTemporarilyRemovedReferences(F fragment) {
        List<R> retList = new LinkedList<R>();
        for (R ref : tempRemoveRefSet) {
            if (ref.getFragment() == fragment) {
                retList.add(ref);
            }
        }
        return retList;
    }

    public R addReference(Chapter chapter, F fragment) {
        return tempAddRefs.addFragmentToChapter(chapter, fragment);
    }

    public void removeReference(R ref) {
        if (tempAddRefs.getReference(ref.getRefId()) != null) {
            tempAddRefs.removeReference(ref);
        } else {
            tempRemoveRefSet.add(ref);
        }
    }

    public void commit(Chapter chapter) {
        Project project = RepositoryFactory.getRepository().getProjectOf(chapter);
        if (project != null) {
            for (Iterator<R> it = tempRemoveRefSet.iterator(); it.hasNext(); ) {
                R ref = it.next();
                if (ref.getChapter() == chapter) {
                    getReferences(project).removeReference(ref);
                    it.remove();
                }
            }
            for (R ref : tempAddRefs.getReferencesFromChapter(chapter)) {
                getReferences(project).addReference(ref);
                tempAddRefs.removeReference(ref);
            }
        }
    }

    public void commit(R ref) {
        Project project = RepositoryFactory.getRepository().getProjectOf(ref.getChapter());
        if (project != null) {
            if (tempRemoveRefSet.contains(ref)) {
                getReferences(project).removeReference(ref);
                tempRemoveRefSet.remove(ref);
            }
            if (tempAddRefs.getAllReferences().contains(ref)) {
                getReferences(project).addReference(ref);
                tempAddRefs.removeReference(ref);
            }
        }
    }

    public void rollback(Chapter chapter) {
        for (Iterator<R> it = tempRemoveRefSet.iterator(); it.hasNext(); ) {
            R ref = it.next();
            if (ref.getChapter() == chapter) {
                it.remove();
            }
        }
        for (R ref : tempAddRefs.getReferencesFromChapter(chapter)) {
            tempAddRefs.removeReference(ref);
        }
    }

    public void clear() {
        tempAddRefs = createCollector();
        tempRemoveRefSet.clear();
        refsMap.clear();
    }

    private Map<UUID, Reference> idRefMap = new HashMap<UUID, Reference>();

    private Map<UUID, List<UUID>> chapterRefMap = new HashMap<UUID, List<UUID>>();

    private Map<UUID, List<UUID>> fragmentRefMap = new HashMap<UUID, List<UUID>>();

    public Map<UUID, Reference> getIdRefMap() {
        return idRefMap;
    }

    public Map<UUID, List<UUID>> getChapterRefMap() {
        return chapterRefMap;
    }

    public Map<UUID, List<UUID>> getFragmentRefMap() {
        return fragmentRefMap;
    }

    public void updateMaps() {
        idRefMap.clear();
        chapterRefMap.clear();
        fragmentRefMap.clear();
        for (C coll : refsMap.values()) {
            idRefMap.putAll(coll.getIdRefMap());
            for (UUID id : coll.getChapterRefMap().keySet()) {
                if (!coll.getChapterRefMap().get(id).isEmpty()) {
                    chapterRefMap.put(id, coll.getChapterRefMap().get(id));
                }
            }
            for (UUID id : coll.getFragmentRefMap().keySet()) {
                if (!coll.getFragmentRefMap().get(id).isEmpty()) {
                    fragmentRefMap.put(id, coll.getFragmentRefMap().get(id));
                }
            }
        }
    }
}
