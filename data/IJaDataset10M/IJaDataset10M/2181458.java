package org.gunncs.actoriface;

/**
 *
 * @author kroo
 */
public interface Actor {

    public void act(Robot r);

    public void updateSensors(Sensor[] sensors);
}
