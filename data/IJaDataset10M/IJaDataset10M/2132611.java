package takatuka.drivers.interfaces;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Jet Tang
 * @version 1.0
 */
public interface IPowerManager {

    /**
     * set power level of the mote
     * @param level is the desired power level. The mote will work with given level.
     */
    void setPowerLevel(int level);
}
