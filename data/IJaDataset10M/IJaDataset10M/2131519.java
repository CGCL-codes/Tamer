package org.dubh.common.collect;

import java.util.List;
import com.google.common.base.Preconditions;

/**
 * Provides access to factory methods for creating lists.
 *
 * @author brian.duff@dubh.org
 */
public final class Lists {

    private Lists() {
    }

    /**
     * Creates a {@link List} implementation that will decorate the specified
     * <tt>list</tt>, recording the stack trace of any modifications to the
     * list.<p>
     *
     * If a {@link java.util.ConcurrentModificationException} occurs while
     * iterating the list, the cause of the exception will be set to the stack
     * trace of the last modification.
     *
     * @param list the list to decorate.
     * @return a list that will record modifications.
     */
    public static <E> List<E> newAuditedWriteList(List<E> list) {
        Preconditions.checkNotNull(list);
        return new AuditedWriteList<E>(list);
    }
}
