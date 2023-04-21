package sw_emulator.hardware;

/**
 * The interface <code>powered</code> represents an electronical component that
 * can be powerd on and off.
 * The methods <code>powerOn</code> and <code>powerOff</code> are to be used
 * for giving on/off power to the component.
 *
 * @author Ice
 * @version 1.00 02/10/1999
 */
public interface powered {

    /**
   * Power on the electronic component
   */
    public void powerOn();

    /**
   * Power off the electronic component
   */
    public void powerOff();
}
