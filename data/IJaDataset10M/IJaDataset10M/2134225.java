package net.sf.refactorit.classmodel.references;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.common.util.CollectionUtil;
import java.util.Collection;

/**
 *
 * @author Arseni Grigorjev
 */
public class CollectionReference extends BinItemReference {

    private final BinItemReference arrayReference;

    public CollectionReference(final Collection collection) {
        arrayReference = BinItemReference.create(collection.toArray());
    }

    public Object findItem(Project project) {
        final Object[] o = (Object[]) arrayReference.restore(project);
        return CollectionUtil.toMutableList(o);
    }
}
