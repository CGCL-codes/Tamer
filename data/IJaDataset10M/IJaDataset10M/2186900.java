package com.shieldsbetter.paramour.soundgraph;

import com.shieldsbetter.paramour.data.MakesNoContributionException;
import com.shieldsbetter.paramour.data.StandardSampleSource;
import com.shieldsbetter.paramour.resources.ResourceException;
import com.shieldsbetter.paramour.time.Extent;
import com.shieldsbetter.paramour.time.Time;
import java.io.IOException;

/**
 *
 * @author hamptos
 */
public class TimeOffsetSound extends AbstractSound {

    private Sound myBaseSound;

    private Time myOffset;

    private Extent myExtent;

    private EmbeddedListener myEmbeddedListener;

    public TimeOffsetSound(Sound baseSound, Time offset) {
        myBaseSound = baseSound;
        myOffset = offset;
        myEmbeddedListener = new EmbeddedListener();
        baseSound.addSoundChangeListener(myEmbeddedListener);
        updateExtent();
    }

    public Sound getBaseSound() {
        return myBaseSound;
    }

    public void setOffset(Time offset) {
        myOffset = offset;
        updateExtent();
    }

    public Time getOffset() {
        return myOffset;
    }

    @Override
    public StandardSampleSource getContributionOver(Extent window, float sampleRate) throws MakesNoContributionException, ResourceException, IOException {
        return myBaseSound.getContributionOver(window.minus(myOffset), sampleRate);
    }

    @Override
    public Extent getContributionExtent() throws MakesNoContributionException {
        if (myExtent == null) {
            throw new MakesNoContributionException(MakesNoContributionException.WindowPosition.ALONE);
        }
        return myExtent;
    }

    private void updateExtent() {
        if (myExtent != null) {
            fireSoundChanged(myExtent);
        }
        try {
            myExtent = myBaseSound.getContributionExtent().plus(myOffset);
            fireSoundChanged(myExtent.plus(myOffset));
        } catch (MakesNoContributionException e) {
            myExtent = null;
        }
    }

    private class EmbeddedListener implements SoundChangeListener {

        public void changed(Sound source, Extent extent) {
            updateExtent();
        }
    }
}
