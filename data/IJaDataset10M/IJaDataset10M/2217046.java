package engine.api;

/**
 * classe MidiEffect.java
 * @author Marc Haussaire
 * 
 * A MidiEffect is a module that is Input and Output
 * You can write your own midiEffect by ovverrinding this class (or other subclass)
 * the method send(MidiMessage, long) is called 
 */
public abstract class MidiEffect extends MidiIn implements MidiOut {
}
