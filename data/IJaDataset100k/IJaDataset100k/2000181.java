package com.peterhi.player.action;

import static com.peterhi.player.Application.*;
import java.net.SocketException;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import com.peterhi.PeterHi;
import com.peterhi.net.message.EraseSessionMessage;
import com.peterhi.client.*;
import com.peterhi.player.*;
import com.peterhi.player.view.*;

public class DoReconnectAction extends AbstractAction {

    private static final Action instance = new DoReconnectAction();

    public static Action getInstance() {
        return instance;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JMemberView c = Application.getWindow().getView(JMemberView.class);
            DatagramClient dc = DatagramClient.getInstance();
            SocketClient sc = SocketClient.getInstance();
            int[] rows = c.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                JMemberView.Member cm = c.getModel().get(rows[i]);
                EraseSessionMessage message = new EraseSessionMessage();
                message.sender = get(KEY_ID, Integer.class);
                message.receiver = cm.getId();
                cm.getState().setP2PStatus(PeterHi.P2P_STATUS_CONNECT);
                DatagramSession ds = dc.remove(cm.getId());
                if (ds != null) {
                    ds.close();
                }
                sc.send(message);
            }
            c.updateTableUI();
        } catch (Exception ex) {
            Application.shutdown(ex);
        }
    }
}
