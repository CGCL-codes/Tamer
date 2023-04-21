package com.uglygreencar.pong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.samskivert.swing.GroupLayout;
import com.threerings.crowd.client.PlacePanel;
import com.threerings.util.MessageBundle;
import com.threerings.toybox.client.ChatPanel;
import com.threerings.toybox.util.ToyBoxContext;

/**
 * Contains the primary client interface for the game.
 */
public class PongPanel extends PlacePanel {

    /** Provides access to various client services. */
    protected ToyBoxContext _ctx;

    /** The board view. */
    protected PongBoardView _bview;

    /**
	 * Creates a Pong panel and its associated interface components.
	 */
    public PongPanel(ToyBoxContext ctx, PongController ctrl) {
        super(ctrl);
        _ctx = ctx;
        MessageBundle msgs = _ctx.getMessageManager().getBundle("pong");
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        add(_bview = new PongBoardView(ctx), BorderLayout.CENTER);
        JPanel sidePanel = GroupLayout.makeVStretchBox(5);
        JLabel vlabel = new JLabel(msgs.get("m.title"));
        vlabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        vlabel.setForeground(Color.black);
        sidePanel.add(vlabel, GroupLayout.FIXED);
        sidePanel.add(new ChatPanel(ctx));
        JButton back = PongController.createActionButton(msgs.get("m.back_to_lobby"), "backToLobby");
        sidePanel.add(back, GroupLayout.FIXED);
        add(sidePanel, BorderLayout.EAST);
    }
}
