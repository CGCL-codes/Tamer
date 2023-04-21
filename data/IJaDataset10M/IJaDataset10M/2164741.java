package com.blackberry.facebook.inf;

import com.blackberry.facebook.AsyncCallback;

public interface FriendList extends com.blackberry.facebook.inf.Object {

    public String getName();

    public Profile[] getMembers();

    public Profile[] getMembers(final AsyncCallback listener, final java.lang.Object state);

    public boolean addMember(String pId);

    public boolean addMember(final String pId, final AsyncCallback listener, final java.lang.Object state);

    public boolean deleteMember(String pId);

    public boolean deleteMember(final String pId, final AsyncCallback listener, final java.lang.Object state);
}
