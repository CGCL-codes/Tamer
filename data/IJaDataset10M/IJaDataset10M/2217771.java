package com.peterhi.client.managers;

public interface StoreListener {

    void onVoiceTest(StoreEvent e);

    void onTalking(StoreEvent e);

    void onVoiceEnabled(StoreEvent e);

    void onTextEnabled(StoreEvent e);
}
