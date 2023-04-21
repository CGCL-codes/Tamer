package lytt.disco;

import lytt.disco.Debug;
import org.hu.ti.wasmachine.ow.event.WaterLevelListener;
import org.hu.ti.wasmachine.ow.event.WaterLevelEvent;
import org.hu.ti.wasmachine.ow.WaterLevelSensor;
import org.hu.ti.wasmachine.ow.component.OWFactory;

/**
* Class for waterlevel
* getWaterLevel returns amount of liters in mV
* getWaterLevelLiter returns liter value in liter
*
* @author Thomas Ros
* @version 1.0
* @since 1.0
*/
public class WaterLevelManager implements WaterLevelListener {

    /**
	* Initiates default waterlevel value
	*/
    double waterLevel = 0.0;

    /**
	* Constructor initializes variables
	*/
    public WaterLevelManager() {
        Debug.dump("WaterLevelManager::WaterLevelManager() Contstructor initialized");
        WaterLevelSensor ws = OWFactory.getWaterLevelSensor();
        ws.addWaterLevelListener(this);
    }

    /**
	* Returns waterlevel in mV
	*/
    public double getWaterLevel() {
        return waterLevel;
    }

    /**
	* Returns waterlevel in liters
	*/
    public double getWaterLevelLiter() {
        double l = lytt.xenu.Converter.toLiters(0);
        return l;
    }

    /**
	* Called on state changed
	* @param e is a WaterLevel object
	*/
    public void waterLevelChanged(WaterLevelEvent e) {
        this.waterLevel = e.getWaterLevel();
    }
}
