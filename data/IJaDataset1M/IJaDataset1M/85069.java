package gui;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class GeluidSpeler {

    private Clip achtergrondGeluid;

    private Clip geluidsEffecten;

    private boolean achtergrondAan = true;

    private boolean voorgrondGeluidAan = true;

    private GUIController guiController;

    public GeluidSpeler(GUIController guiController) {
        this.guiController = guiController;
    }

    public void speelGeluid(String naam, boolean voorgrond) {
        if (!voorgrond && achtergrondAan) {
            achtergrondGeluid = guiController.geefAudioClip(naam);
            achtergrondGeluid.setFramePosition(0);
            achtergrondGeluid.start();
            achtergrondGeluid.loop(Clip.LOOP_CONTINUOUSLY);
        } else if (voorgrondGeluidAan) {
            geluidsEffecten = guiController.geefAudioClip(naam);
            geluidsEffecten.setFramePosition(0);
            geluidsEffecten.start();
        }
    }

    public void veranderVolumeGeluid(int gain, boolean voorgrond) {
        if (!voorgrond && achtergrondGeluid != null) {
            FloatControl volume = (FloatControl) achtergrondGeluid.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(gain);
        } else if (geluidsEffecten != null) {
            FloatControl volume1 = (FloatControl) geluidsEffecten.getControl(FloatControl.Type.MASTER_GAIN);
            volume1.setValue(gain);
        }
    }

    public void stopGeluid(boolean voorgrond) {
        if (!voorgrond && achtergrondGeluid != null) {
            achtergrondGeluid.stop();
            achtergrondAan = false;
        } else if (geluidsEffecten != null) {
            geluidsEffecten.stop();
            voorgrondGeluidAan = false;
        }
    }

    public void hervatGeluid(boolean voorgrond) {
        if (!voorgrond && achtergrondGeluid != null) achtergrondGeluid.start(); else if (geluidsEffecten != null) geluidsEffecten.start();
    }
}
