package phex.rules.condition;

import java.util.*;
import org.apache.commons.collections.set.ListOrderedSet;
import phex.common.MediaType;
import phex.download.RemoteFile;
import phex.query.Search;
import phex.xml.sax.rules.DCondition;
import phex.xml.sax.rules.DMediaTypeCondition;

public class MediaTypeCondition implements Condition {

    private ListOrderedSet types;

    public MediaTypeCondition() {
        types = new ListOrderedSet();
    }

    public MediaTypeCondition(MediaType type) {
        this();
        addType(type);
    }

    public MediaTypeCondition(MediaTypeCondition condition) {
        this();
        update(condition);
    }

    public synchronized void update(MediaTypeCondition condition) {
        types.clear();
        types.addAll(condition.types);
    }

    public synchronized Set<MediaType> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    public synchronized MediaTypeCondition addType(MediaType type) {
        types.add(type);
        return this;
    }

    public synchronized void removeType(MediaType type) {
        types.remove(type);
    }

    public synchronized boolean isMatched(Search search, RemoteFile remoteFile) {
        Iterator iterator = types.iterator();
        while (iterator.hasNext()) {
            MediaType type = (MediaType) iterator.next();
            if (type.isFilenameOf(remoteFile.getFilename())) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isComplete() {
        return types.size() > 0;
    }

    public synchronized Object clone() {
        try {
            MediaTypeCondition clone = (MediaTypeCondition) super.clone();
            clone.types = new ListOrderedSet();
            clone.types.addAll(types);
            return clone;
        } catch (CloneNotSupportedException exp) {
            throw new InternalError();
        }
    }

    public synchronized DCondition createDCondition() {
        DMediaTypeCondition dCond = new DMediaTypeCondition();
        List newList = new ArrayList(types);
        dCond.setTypes(newList);
        return dCond;
    }
}
