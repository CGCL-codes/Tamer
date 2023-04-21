package be.lassi.cues;

/**
 * Default implementation of the <code>CuesListener</code> interface
 * that does not really do anything.
 */
public class DefaultCuesListener implements CuesListener {

    /**
     * {@inheritDoc}
     */
    public void added(final int index, final Cue definition) {
    }

    /**
     * {@inheritDoc}
     */
    public void channelLevelChanged(final int cueIndex, final int channelIndex) {
    }

    /**
     * {@inheritDoc}
     */
    public void currentChanged() {
    }

    /**
     * {@inheritDoc}
     */
    public void levelChanged(final int levelIndex) {
    }

    /**
     * {@inheritDoc}
     */
    public void removed(final int index, final Cue definition) {
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChanged() {
    }

    /**
     * {@inheritDoc}
     */
    public void cueNumbersChanged() {
    }

    /**
     * {@inheritDoc}
     */
    public void submasterLevelChanged(final int cueIndex, final int submasterIndex) {
    }
}
