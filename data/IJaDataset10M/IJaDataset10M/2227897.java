package de.rowbuddy.client.events;

public class ListMembersEvent extends AbstractEvent<ListMembersPresenterChanger> {

    public static final Type<ListMembersPresenterChanger> TYPE = new Type<ListMembersPresenterChanger>();

    public static final String HISTORY_IDENTIFIER = "ListMembers";

    @Override
    public Type<ListMembersPresenterChanger> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toHistoryItem() {
        return HISTORY_IDENTIFIER;
    }
}
