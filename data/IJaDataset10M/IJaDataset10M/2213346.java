package jds.com.service.impl;

import jds.com.service.Command;
import org.lab.dataUniverse.InputFeederData;

public abstract class BaseCommand implements Command {

    protected InputFeederData inputFeederData;

    /**
	 * Testea si tiene parseado todo lo que necesita para realizar su labor
	 * 
	 * 
	 * @throws Exception
	 */
    public abstract void testState() throws Exception;

    public InputFeederData getInputFeederData() {
        return inputFeederData;
    }

    public void setInputFeederData(InputFeederData inputFeederData) {
        this.inputFeederData = inputFeederData;
    }
}
