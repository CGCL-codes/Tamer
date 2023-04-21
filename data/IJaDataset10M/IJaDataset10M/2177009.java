package org.coosproject.examples.impl;

import org.coos.actorframe.ActorCS;
import org.coos.javaframe.State;
import org.coos.javaframe.StateMachine;
import org.coos.javaframe.messages.AFPropertyMsg;
import org.coos.javaframe.messages.ActorMsg;

/**
 * @author Geir Melby, Tellu AS
 */
public class ChatCS extends ActorCS {

    public ChatCS(String s) {
        super(s);
    }

    static int CHAT_INTERVAL = 10 * 1000;

    public State chatting = new State("chatting", this);

    public void execTrans(ActorMsg sig, State state, StateMachine stateMachine) {
        super.execTrans(sig, state, stateMachine);
        ChatSM asm = (ChatSM) curfsm;
        if (asm.saveDone) return;
        if (state == idle) {
            if (sig.equals(START_PLAYING_MSG)) {
                traceTask(getFullStateName() + " is initialized");
                asm.alias = sig.getString("alias");
                AFPropertyMsg pm = new AFPropertyMsg("Subscribe");
                pm.setProperty("alias", asm.alias);
                sendMessage(pm, getParentActorAddress());
                sameState();
                return;
            }
            if (sig.equals("Accepted")) {
                performExit();
                asm.chatRoomAddress = sig.getSenderRole();
                AFPropertyMsg pm = asm.createSendChatMessage("Hello");
                sendMessage(pm, asm.chatRoomAddress);
                startTimer(CHAT_INTERVAL, "SendTimer");
                nextState(chatting);
                return;
            }
        } else if (state == chatting) {
            if (sig.equals("Stop")) {
                performExit();
                asm.gui.clearMessageBuffer();
                sendMessage(new AFPropertyMsg("Goodbye").setProperty("alias", asm.alias), asm.chatRoomAddress);
                nextState(idle);
                return;
            }
            if (sig.equals("Message")) {
                String message = sig.getString("text");
                asm.gui.writeMessage(message);
                sameState();
                return;
            }
            if (sig.equals("Send")) {
                String message = asm.gui.readMessage();
                sendMessage(asm.createSendChatMessage(message), asm.chatRoomAddress);
                sameState();
                return;
            }
            if (sig.equals(TIMER_MSG) && sig.getString(TIMER_ID).equals("SendTimer")) {
                String message = asm.gui.readMessage();
                if (message == null) {
                    sendMessage("Stop", getMyActorAddress());
                } else {
                    sendMessage(asm.createSendChatMessage(message), asm.chatRoomAddress);
                    startTimer(CHAT_INTERVAL, "SendTimer");
                }
                sameState();
                return;
            }
        }
    }
}
