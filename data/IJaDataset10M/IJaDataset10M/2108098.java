package com.android.im.engine;

public interface GroupMemberListener {

    /**
     * Notifies that a contact has joined into this group.
     *
     * @param contact the contact who has joined into this group.
     */
    public void onMemberJoined(ChatGroup group, Contact contact);

    /**
     * Notifies that a contact has left this group.
     *
     * @param contact the contact who has left the group.
     */
    public void onMemberLeft(ChatGroup group, Contact contact);

    /**
     * Called when a previous request to add or remove a member to/from a
     * group failed.
     *
     * @param error the error information
     */
    public void onError(ChatGroup group, ImErrorInfo error);
}
