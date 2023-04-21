package org.sodbeans.actions.tts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.sodbeans.phonemic.TextToSpeechFactory;
import org.sodbeans.phonemic.tts.TextToSpeech;
import org.sodbeans.tts.options.api.TextToSpeechOptions;

@ActionID(category = "TextToSpeech", id = "org.sodbeans.actions.tts.RaiseTextToSpeechVolume")
@ActionRegistration(displayName = "#CTL_RaiseTextToSpeechVolume")
@ActionReferences({ @ActionReference(path = "Menu/Tools/Text to Speech", position = 350), @ActionReference(path = "Shortcuts", name = "DO-F4") })
@Messages("CTL_RaiseTextToSpeechVolume=Raise Speech Volume")
public final class RaiseTextToSpeechVolume implements ActionListener {

    private TextToSpeech speech = TextToSpeechFactory.getDefaultTextToSpeech();

    public void actionPerformed(ActionEvent e) {
        if (!speech.canSetVolume()) {
            if (TextToSpeechOptions.isScreenReading()) {
                speech.speak("The engine you are using does not support volume changes.");
            }
            return;
        }
        double currentVolume = speech.getVolume();
        if (currentVolume < 1) {
            double newVolume = currentVolume + 0.1;
            speech.setVolume(newVolume);
            if (TextToSpeechOptions.isScreenReading()) {
                speech.speak("Text to Speech volume raised.");
            }
            if (newVolume >= 0 && newVolume <= 1.0) TextToSpeechOptions.setSpeechVolume((int) (newVolume * 100));
        } else {
            if (TextToSpeechOptions.isScreenReading()) {
                speech.speak("You are already at the highest volume.");
            }
        }
    }
}
