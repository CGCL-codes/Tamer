package tico.interpreter.actions;

import java.awt.event.ActionEvent;
import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.components.TInterpreterAccumulatedCell;
import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterWavSound;

/**
 * 
 * @author Antonio Rodríguez
 *
 */
public class TInterpreterReadAction extends TInterpreterAbstractAction {

    TInterpreterWavSound nAudio = null;

    TInterpreterMp3Sound nMp3Audio = null;

    public TInterpreterReadAction(TInterpreter interpreter) {
        super(interpreter, TLanguage.getString("TInterpreterRead.NAME"));
    }

    public void actionPerformed(ActionEvent e) {
        interpreter = getInterpreter();
        if (nMp3Audio != null) {
            nMp3Audio.TJoin();
        }
        if (nAudio != null) {
            try {
                nAudio.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        int index = 0;
        for (index = 0; index < TInterpreter.accumulatedCellsList.size(); index++) {
            String nameSound = ((TInterpreterAccumulatedCell) (TInterpreter.accumulatedCellsList.get(index))).getSound();
            if (nameSound != null) {
                String extension = TFileUtils.getExtension(nameSound);
                if (extension.equals("mp3")) {
                    nMp3Audio = new TInterpreterMp3Sound(nameSound);
                    nMp3Audio.TPlay();
                    nMp3Audio.TJoin();
                } else {
                    nAudio = new TInterpreterWavSound(nameSound);
                    nAudio.start();
                    try {
                        nAudio.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
