package edu.ycp.egr.ydlx.config;

import java.util.ArrayList;
import java.util.List;
import edu.ycp.egr.ydlx.CPU;

/**
 * List of configuration commands that will
 * completely initialize/configure a simulation. 
 * 
 * @author David Hovemeyer
 */
public class CPUConfig implements CPUConfigItem {

    private List<CPUConfigItem> configItemList;

    /**
	 * Constructor.
	 * Initially, there are no configuration commands.
	 */
    public CPUConfig() {
        this.configItemList = new ArrayList<CPUConfigItem>();
    }

    /**
	 * Add a configuration command.
	 * 
	 * @param configItem the configuration command
	 */
    public void addConfigItem(CPUConfigItem configItem) {
        configItemList.add(configItem);
    }

    public void configure(CPU cpu) {
        for (CPUConfigItem configItem : configItemList) {
            configItem.configure(cpu);
        }
    }
}
