package net.leibman.sprinko.services;

import net.leibman.sprinko.SprinkoException;
import net.leibman.sprinko.loader.SprinkoServiceInstance;

/**
 * @author roberto
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IrrigationValveService10 extends Service, SprinkoServiceInstance {

    public void on() throws SprinkoException;

    public void off() throws SprinkoException;

    public boolean isOn() throws SprinkoException;

    public void setFlowRate(int flowRate) throws SprinkoException;

    public int getCurrentFlowRate() throws SprinkoException;

    public int getMaxFlowRate() throws SprinkoException;

    public boolean supportsFlowControl() throws SprinkoException;
}
