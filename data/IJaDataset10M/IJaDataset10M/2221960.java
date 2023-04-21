package com.peterhi.player;

import java.util.EventListener;

public interface StateListener extends EventListener {

    void idChanged(StateEvent e);

    void emailChanged(StateEvent e);

    void stateChanged(StateEvent e);

    void channelIdChanged(StateEvent e);

    void channelChanged(StateEvent e);

    void topologyChanged(StateEvent e);

    void roleChanged(StateEvent e);

    void onlineStatusChanged(StateEvent e);

    void displayNameChanged(StateEvent e);

    void talkEnabledChanged(StateEvent e);

    void talkingChanged(StateEvent e);

    void handRaisedChanged(StateEvent e);

    void applausedChanged(StateEvent e);

    void choiceChanged(StateEvent e);

    void udpContactEstablishedChanged(StateEvent e);
}
