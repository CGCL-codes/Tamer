package com.weespers.ui.player.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import com.weespers.ui.player.YouTubePlayer;
import com.weespers.ui.player.YouTubePlayerView;

public class PlayerAction extends Action {

    protected Object result = null;

    public PlayerAction(String text) {
        super(text);
    }

    public PlayerAction(String text, ImageDescriptor image) {
        super(text, image);
    }

    protected YouTubePlayer getPlayer() {
        return YouTubePlayerView.INSTANCE.getPlayer();
    }
}
