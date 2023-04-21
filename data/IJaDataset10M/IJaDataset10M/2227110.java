package com.arsenal.client.observer;

public interface RemoveInactiveUserObserver {

    public void doRemoveInactiveUserAction(Object object);

    public void registerRemoveInactiveUserListener(RemoveInactiveUserObserver removeInactiveUserObserver);
}
