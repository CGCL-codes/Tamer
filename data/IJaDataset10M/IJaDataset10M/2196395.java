package ch.oois.infofeeder.sportidentreader;

import ch.oois.infofeeder.LifecycleControl;

/**
 * The SportIdentReader reads data records from the SportIdent stations
 * on a RS-232 port.
 * SportIdentReader is a marker interface. A SportIdentReader does not offer any services
 * to other blocks.
 *
 * @author Mario D�pp
 */
public interface SportIdentReaderControl extends LifecycleControl {

    /**
   * Constant for number mode. The ID of the SICard will
   * be interpreted as being the SICard number.
   */
    public static final int SICARD_NUMBER = 0;

    /**
   * Constant for number mode. The ID of the SICard will
   * be interpreted as being the start number.
   */
    public static final int START_NUMBER = 1;

    /**
   * Constant for time mode. The time sent by the SPORTident stations
   * will be interpreted as being in 12 hour format.
   */
    public static final int HOUR12 = 0;

    /**
   * Constant for time mode. The time sent by the SPORTident stations
   * will be interpreted as beeing in 24 hour format.
   */
    public static final int HOUR24 = 1;

    /**
   * Set the finish control.
   *
   * @param controlId the ID of the control which is designated as start control
   * */
    public void setFinishControl(int controlId);

    /**
   * Get the finish control.
   *
   * @return int the ID of the control which is designated as start control
   */
    public int getFinishControl();

    /**
   * Set the start control.
   *
   * @param controlId the ID of the control which is designated as start control
   * */
    public void setStartControl(int controlId);

    /**
   * Get the start control.
   *
   * @return int the ID of the control which is designated as start control
   */
    public int getStartControl();

    /**
   * Set the number mode.
   * Warning: The number mode should not be set while data is arriving
   * from the SPORTident station. Doing so may result in the current
   * date record being corrupted.
   *
   * @param numberMode the number mode. One of SICARD_NUMBER or
   * START_NUMBER
   * */
    public void setNumberMode(int numberMode);

    /**
   * Get the number mode.
   *
   * @return int the number mode. One of SICARD_NUMBER or
   * START_NUMBER
   */
    public int getNumberMode();

    /**
   * Set the time mode.
   * Warning: The time mode should not be set while data is arriving
   * from the SPORTident station. Doing so may result in the current
   * date record being corrupted.
   *
   * @param timeMode the time mode. One of HOUR12 or HOUR24
   * */
    public void setTimeMode(int timeMode);

    /**
   * Get the time mode.
   *
   * @return int the time mode. One of HOUR12 or HOUR24
   */
    public int getTimeMode();

    /**
   * Set the COM port. Calling this method will cause a restart of
   * this component.
   *
   * @param port the COM port. Note: the value is OS specific
   * (e.g. /dev/ttyS0 in *nix or COM1 in Windows)
   */
    public void setPort(String port);

    /**
   * Get the COM port.
   *
   * @return String the COM port. Note: the value is OS specific
   * (e.g. /dev/ttyS0 in *nix or COM1 in Windows)
   */
    public String getPort();

    /**
   * Get the status.
   *
   * @return int the status. One of the constants in Status.
   * @see ch.isbiel.oois.infofeeder.Status
   */
    public int getStatus();
}
