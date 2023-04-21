package dioscuri.module.cpu32;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
public interface HardwareComponent extends Hibernatable {

    /**
     * @return -
     */
    public boolean initialised();

    /**
     * @param component
     */
    public void acceptComponent(HardwareComponent component);

    /**
     * @return -
     */
    public boolean reset();

    /**
     * @param output
     * @throws IOException
     */
    public void dumpState(DataOutput output) throws IOException;

    /**
     * @param input
     * @throws IOException
     */
    public void loadState(DataInput input) throws IOException;

    /**
     * @return -
     */
    public boolean updated();

    /**
     * @param component
     */
    public void updateComponent(HardwareComponent component);

    public void timerCallback();
}
