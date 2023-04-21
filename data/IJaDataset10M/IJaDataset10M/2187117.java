package de.joergjahnke.gameboy.core;

import de.joergjahnke.common.io.Serializable;
import de.joergjahnke.common.util.DefaultObservable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Implements the Gameboy's joypad
 * 
 * @author Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public class Joypad extends DefaultObservable implements Serializable {

    /**
     * button value for the right joypad button
     */
    public static final int RIGHT = 0x01;

    /**
     * button value for the left joypad button
     */
    public static final int LEFT = 0x02;

    /**
     * button value for the up joypad button
     */
    public static final int UP = 0x04;

    /**
     * button value for the down joypad button
     */
    public static final int DOWN = 0x08;

    /**
     * button value for the joypad button A
     */
    public static final int A = 0x01;

    /**
     * button value for the joypad button B
     */
    public static final int B = 0x02;

    /**
     * button value for the joypad button Select
     */
    public static final int SELECT = 0x04;

    /**
     * button value for the joypad button Start
     */
    public static final int START = 0x08;

    /**
     * Gameboy we work for
     */
    private final Gameboy gameboy;

    /**
     * direction states of the joypad
     */
    private int directions = 0;

    /**
     * buttons pressed on the joypad
     */
    private int buttons = 0;

    /**
     * Create a new Joypad
     * 
     * @param   gameboy gameboy the joypad is attached to
     */
    public Joypad(final Gameboy gameboy) {
        this.gameboy = gameboy;
    }

    /**
     * Get the direction keys of the joypad
     * 
     * @return  value $0-$f
     */
    public int getDirections() {
        return this.directions;
    }

    /**
     * Set direction key of the joypad
     * 
     * @param   directions  value $0-$f
     */
    public void setDirections(final int directions) {
        if (directions != this.directions) {
            this.directions = directions;
            this.gameboy.getCPU().writeIO(0xff00, (byte) (0x10 + directions));
            setChanged(true);
            notifyObservers(new Integer(directions));
        }
    }

    /**
     * Get the pressed buttons of the joypad
     * 
     * @return  value $0-$f
     */
    public int getButtons() {
        return this.buttons;
    }

    /**
     * Set the pressed buttons of the joypad
     * 
     * @param   buttons value $0-$f
     */
    public void setButtons(final int buttons) {
        if (buttons != this.buttons) {
            this.buttons = buttons;
            this.gameboy.getCPU().writeIO(0xff00, (byte) (0x20 + buttons));
            setChanged(true);
            notifyObservers(new Integer(buttons));
        }
    }

    public void serialize(final DataOutputStream out) throws IOException {
        out.writeInt(this.directions);
        out.writeInt(this.buttons);
    }

    public void deserialize(final DataInputStream in) throws IOException {
        this.directions = in.readInt();
        this.buttons = in.readInt();
    }
}
