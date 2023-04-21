package org.op4j.functions;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 */
public final class FnSetOfListOf<T> extends FnSetOf<List<T>> {

    public final Function<Set<List<T>>, Set<T>> flattenLists() {
        return new FlattenLists<T>();
    }

    protected FnSetOfListOf(final Type<T> type) {
        super(Types.listOf(type));
    }

    static final class FlattenLists<T> extends FnCollection.FlattenCollectionOfCollections<T, Set<T>, Set<List<T>>> {

        FlattenLists() {
            super();
        }

        @Override
        Set<T> fromList(final List<T> object) {
            return new LinkedHashSet<T>(object);
        }
    }
}
