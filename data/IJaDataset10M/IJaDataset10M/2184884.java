package eass.nxt;

import ail.syntax.Literal;
import ail.syntax.NumberTermImpl;
import lejos.nxt.SoundSensor;

public class EASSSoundSensor extends SoundSensor implements EASSSensor {

    public EASSSoundSensor(NXTBrick brick, int i) {
        super(brick.getSensorPort(i));
    }

    public void addPercept(EASSNXTEnvironment env) {
        int soundvalue = readValue();
        Literal sound = new Literal("sound");
        sound.addTerm(new NumberTermImpl(soundvalue));
        env.addUniquePercept("sound", sound);
    }
}
