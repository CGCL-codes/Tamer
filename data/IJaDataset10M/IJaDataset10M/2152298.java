package org.speech.asr.recognition.acoustic;

import org.speech.asr.recognition.decoder.SearchGraphSegment;
import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface AcousticModel<S extends StateDescriptor> {

    List<PhoneticUnit<S>> getPhoneSet();

    List<S> getAllStates();

    S getState(String id);

    PhoneticUnit<S> getPhoneticUnit(String name);

    SearchGraphSegment getGraphSegment(String phonemeName);
}
