package com.butterfill.opb.groups;

import com.butterfill.opb.util.OpbValueWrapper;

/**
 * Provides access to a group member.
 * This interface has special meaning to the groups OpbManyMembersGroup and
 * OpbSingleMemberGroup.
 *
 * @author Peter Butterfill
 *
 * @param <E> The type of value wrapped.
 *
 * @see OpbManyMembersGroup
 * @see OpbSingleMemberGroup
 */
public interface OpbGroupMemberWrapper<E> extends OpbValueWrapper<E> {

    /**
     * Returns the member wrapped by this wrapper.
     * @return the member wrapped by this wrapper
     */
    E getValue();
}
