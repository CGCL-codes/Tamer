package org.speech.asr.recognition.trainer;

import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import java.util.List;
import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TrainSentence {

    private Integer index;

    private List<PhoneticUnit> transcription;

    private List<Feature> observations;

    /**
   * Getter for property 'observations'.
   *
   * @return Value for property 'observations'.
   */
    public List<Feature> getObservations() {
        return observations;
    }

    /**
   * Setter for property 'observations'.
   *
   * @param observations Value to set for property 'observations'.
   */
    public void setObservations(List<Feature> observations) {
        this.observations = observations;
    }

    /**
   * Getter for property 'transcription'.
   *
   * @return Value for property 'transcription'.
   */
    public List<PhoneticUnit> getTranscription() {
        return transcription;
    }

    /**
   * Setter for property 'transcription'.
   *
   * @param transcription Value to set for property 'transcription'.
   */
    public void setTranscription(List<PhoneticUnit> transcription) {
        this.transcription = transcription;
    }

    /**
   * Getter for property 'index'.
   *
   * @return Value for property 'index'.
   */
    public Integer getIndex() {
        return index;
    }

    /**
   * Setter for property 'index'.
   *
   * @param index Value to set for property 'index'.
   */
    public void setIndex(Integer index) {
        this.index = index;
    }
}
