package es.eucm.eadventure.engine.core.control.animations;

import java.awt.Image;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;

/**
 * Class that represents an animation made up of frames in the engine. It uses
 * the logic in {@link es.eucm.eadventure.common.data.animation.Animation}.
 * 
 * 
 * @author Eugenio Marchiori
 */
public class FrameAnimation implements Animation {

    /**
     * The animation with the frames and the logic
     */
    private es.eucm.eadventure.common.data.animation.Animation animation;

    /**
     * The time accumulated in the playing of the animation
     */
    private long accumulatedTime;

    /**
     * The id of previous sound ID to stop the previous frame sound
     */
    private long previousFrameSoundID;

    /**
     * Create a new instance using an animation.
     * 
     * @param animation
     *            The animation with the frames and the logic
     */
    public FrameAnimation(es.eucm.eadventure.common.data.animation.Animation animation) {
        this.animation = animation;
        accumulatedTime = 0;
        previousFrameSoundID = -1;
    }

    public Image getImage() {
        Image temp = animation.getImage(accumulatedTime, es.eucm.eadventure.common.data.animation.Animation.ENGINE);
        String sound = animation.getNewSound();
        if (sound != null && sound != "") {
            if (previousFrameSoundID != -1) MultimediaManager.getInstance().stopPlaying(previousFrameSoundID);
            previousFrameSoundID = MultimediaManager.getInstance().loadSound(sound, false);
            MultimediaManager.getInstance().startPlaying(previousFrameSoundID);
        } else if (!MultimediaManager.getInstance().isPlaying(previousFrameSoundID)) previousFrameSoundID = -1;
        return temp;
    }

    public void stopMusic() {
        if (previousFrameSoundID != -1) {
            MultimediaManager.getInstance().stopPlaying(previousFrameSoundID);
            previousFrameSoundID = -1;
        }
    }

    public boolean isPlayingForFirstTime() {
        return !animation.finishedFirstTime(accumulatedTime);
    }

    public void start() {
        accumulatedTime = 0;
    }

    public void update(long elapsedTime) {
        accumulatedTime += elapsedTime;
    }

    public boolean nextImage() {
        boolean noMoreFrames = false;
        accumulatedTime = animation.skipFrame(accumulatedTime);
        if (accumulatedTime >= animation.getTotalTime()) {
            accumulatedTime %= animation.getTotalTime();
            noMoreFrames = true;
        }
        return noMoreFrames;
    }

    public void setAnimation(es.eucm.eadventure.common.data.animation.Animation animation) {
        this.animation = animation;
    }

    public void setMirror(boolean mirror) {
        animation.setMirror(mirror);
    }

    public void setFullscreen(boolean b) {
        animation.setFullscreen(b);
    }
}
