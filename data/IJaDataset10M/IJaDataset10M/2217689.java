package com.peterhi.player.handler;

import static com.peterhi.player.Application.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import com.peterhi.*;
import com.peterhi.io.SwappableInputStream;
import com.peterhi.net.message.NewPeerMessage;
import com.peterhi.client.*;
import com.peterhi.player.*;
import com.peterhi.player.view.*;
import com.peterhi.player.audio.*;

public class NewPeerMessageHandler implements SocketHandler<NewPeerMessage> {

    public void handle(final NewPeerMessage message) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    JMemberView memberView = Application.getWindow().getView(JMemberView.class);
                    JMemberView.Member member = new JMemberView.Member();
                    member.setId(message.id);
                    member.setEmail(message.email);
                    member.setDisplayName(message.displayName);
                    if (get(KEY_TOPOLOGY, Integer.class) == PeterHi.STAR) {
                        message.state.setP2PStatus(PeterHi.P2P_STATUS_TURN);
                        member.setState(message.state);
                    } else {
                        member.setState(message.state);
                    }
                    AudioManager am = AudioManager.getAudioManager();
                    member.setSpeaker(new Speaker(am.playback()));
                    member.setDecoderPeer(Speex.decoder());
                    memberView.getModel().add(member);
                    memberView.notifyEnter(member);
                    memberView.updateTableUI();
                } catch (Exception ex) {
                    Application.shutdown(ex);
                }
            }
        });
    }
}
