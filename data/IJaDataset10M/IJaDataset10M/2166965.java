package edu.cmu.sphinx.decoder.scorer;

import java.io.IOException;
import java.util.List;
import edu.cmu.sphinx.util.props.Configurable;

/**
 * Provides a mechanism for scoring a set of HMM states
 *
 */
public interface AcousticScorer extends Configurable {

    /**
     * Allocates resources for this scorer
     *
     */
    public void allocate() throws IOException;

    /**
     * Deallocates resouces for this scorer
     *
     */
    public void deallocate();

    /**
     * starts the scorer
     */
    public void startRecognition();

    /**
     * Scores the given set of states
     *
     * @param scorableList a list containing Scorable objects to
     * be scored
     *
     * @return the best scoring scorable, or null if there are no
     * more frames to score
     */
    public Scoreable calculateScores(List scorableList);

    /**
     * stops the scorer
     */
    public void stopRecognition();
}
