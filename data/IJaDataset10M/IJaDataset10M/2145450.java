package org.game.morris.sound;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.sf.thyvin.core.res.IResourceLocator;
import org.event.GameEvent;
import org.event.GenericAction;
import org.game.morris.event.MorrisMessageTypes;
import org.game.morris.logic.MorrisGameState;
import org.game.morris.logic.PossibleMoves;

public class MorrisSoundHandler {

    private final String mooFileName = "morris/sound/cowmoo1.wav";

    private final String machineGunName = "morris/sound/machinegun.wav";

    private final IResourceLocator loc;

    private Clip mooClip = null;

    private Clip machineGunClip = null;

    private boolean isDoubleMilling = false;

    public MorrisSoundHandler(IResourceLocator aLoc) {
        this.loc = aLoc;
        {
            AudioInputStream ais = null;
            BufferedInputStream bis = null;
            try {
                mooClip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
                bis = new BufferedInputStream(loc.requireResource(mooFileName).getInputStream());
                ais = AudioSystem.getAudioInputStream(bis);
                mooClip.open(ais);
                ais.close();
                machineGunClip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
                bis = new BufferedInputStream(loc.requireResource(machineGunName).getInputStream());
                ais = AudioSystem.getAudioInputStream(bis);
                machineGunClip.open(ais);
                ais.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } finally {
                handleClosing(ais, bis);
            }
        }
    }

    private static final void handleClosing(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void executeGameEvent(GameEvent gameEvent, MorrisGameState gameState) {
        switch(MorrisMessageTypes.values()[gameEvent.getType()]) {
            case MOVE:
                {
                    GenericAction action = (GenericAction) gameEvent;
                    int lastMoveNodeIdFrom = gameState.getNode(action.getPayload()[0]).getId();
                    int lastMoveNodeIdTo = gameState.getNode(action.getPayload()[1]).getId();
                    isDoubleMilling = PossibleMoves.isDoubleMilling(gameState, gameState.getNode(lastMoveNodeIdFrom), gameState.getNode(lastMoveNodeIdTo));
                    break;
                }
            case DELETE_UNITS:
                {
                    if (isDoubleMilling) {
                        if (machineGunClip != null) {
                            if (!machineGunClip.isRunning()) {
                                machineGunClip.stop();
                            }
                            machineGunClip.setFramePosition(0);
                            machineGunClip.start();
                        }
                    } else {
                        if (mooClip != null) {
                            if (!mooClip.isRunning()) {
                                mooClip.stop();
                            }
                            mooClip.setFramePosition(0);
                            mooClip.start();
                        }
                    }
                    break;
                }
        }
    }
}
