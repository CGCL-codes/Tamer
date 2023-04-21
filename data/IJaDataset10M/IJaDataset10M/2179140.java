package com.arsenal.client.observer;

public interface RemoveGroupObserver {

    public void doRemoveGroupAction(Object object);

    public void registerRemoveGroupListener(RemoveGroupObserver removeGroupObserver);
}
