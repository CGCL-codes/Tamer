package com.germinus.xpression.groupware.communities;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author agonzalez
 *
 */
public class CommunityMembershipIterator implements Iterator<CommunityMembership>, ListIterator<CommunityMembership> {

    private ListIterator<CommunityMembership> internalIterator;

    /**
     * 
     */
    public CommunityMembershipIterator(ListIterator<CommunityMembership> internalIterator) {
        super();
        this.internalIterator = internalIterator;
    }

    public void add(CommunityMembership arg0) {
        add(arg0);
    }

    public void addCommunityMembership(CommunityMembership communityMembership) {
        internalIterator.add(communityMembership);
    }

    public boolean hasNext() {
        return internalIterator.hasNext();
    }

    public boolean hasPrevious() {
        return internalIterator.hasPrevious();
    }

    public CommunityMembership next() {
        return internalIterator.next();
    }

    public CommunityMembership nextCommunityMembership() {
        return next();
    }

    public int nextIndex() {
        return internalIterator.nextIndex();
    }

    public CommunityMembership previous() {
        return internalIterator.previous();
    }

    public CommunityMembership previousCommunityMembership() {
        return previous();
    }

    public int previousIndex() {
        return internalIterator.previousIndex();
    }

    public void remove() {
        internalIterator.remove();
    }

    public void set(CommunityMembership arg0) {
        setCommunityMembership(arg0);
    }

    public void setCommunityMembership(CommunityMembership communityMembership) {
        internalIterator.set(communityMembership);
    }
}
