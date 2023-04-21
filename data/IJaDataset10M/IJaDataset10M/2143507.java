package org.speech.asr.recognition.acoustic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MultivariateGaussian;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 15, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Phoneme<S extends StateDescriptor> implements PhoneticUnit<S>, Serializable {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(Phoneme.class.getName());

    private String name;

    private List<S> states;

    public Phoneme(String name, List<S> stateSequence) {
        this.name = name;
        this.states = stateSequence;
    }

    public Phoneme(String name, int noSubStates) {
        this.name = name;
        states = new LinkedList();
        LogScale logScale = AsrContext.getContext().getLogScale();
        for (int i = 0; i < noSubStates; i++) {
            MultivariateGaussian gaussian = new MultivariateGaussian(logScale, 1, 1);
            GaussianMixture gmm = new GaussianMixture(logScale, gaussian);
            states.add((S) new StateDescriptor(name + "_" + (i + 1), gmm, logScale.getLogHalf()));
        }
    }

    public String getName() {
        return name;
    }

    public List<S> getStatesSequence() {
        return new LinkedList(states);
    }
}
