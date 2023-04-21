package com.peterhi.player.action;

import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import com.peterhi.net.message.ChangeStateMessage;
import com.peterhi.client.SocketClient;
import com.peterhi.PeterHi;
import com.peterhi.State;
import com.peterhi.player.*;
import com.peterhi.player.view.*;

public class SetAllMicEnabledAction extends AbstractAction {

    private static final Action instance = new SetAllMicEnabledAction();

    public static Action getInstance() {
        return instance;
    }

    public void actionPerformed(ActionEvent e) {
        JMemberView c = Application.getWindow().getView(JMemberView.class);
        int[] array = c.getSelectedRows();
        if (array == null || array.length <= 0) {
            return;
        }
        int index = array[0];
        SocketClient sc = SocketClient.getInstance();
        Iterator<JMemberView.Member> itor = c.getModel().iterator();
        while (itor.hasNext()) {
            JMemberView.Member item = itor.next();
            State state = item.getState();
            if (state.getRole() == PeterHi.ROLE_STUDENT) {
                ChangeStateMessage message = new ChangeStateMessage();
                message.sender = item.getId();
                state.setTalkEnabled(true);
                state.setTalking(false);
                message.state = state;
                message.changedBits = PeterHi.TALK_ENABLED_CHANGED | PeterHi.TALKING_CHANGED;
                message.toSender = true;
                try {
                    sc.send(message);
                } catch (Exception ex) {
                    Application.shutdown(ex);
                }
            }
        }
    }
}
