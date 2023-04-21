package de.fraunhofer.isst.axbench.operations.checker.solver.utilities;

import java.util.ArrayList;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;

public class AxlElementConfigurationStates implements Comparable<AxlElementConfigurationStates> {

    IAXLangElement theAxlElement;

    ArrayList<BddConfigurationStatus> theConfigurations;

    public AxlElementConfigurationStates() {
        theConfigurations = new ArrayList<BddConfigurationStatus>();
    }

    public AxlElementConfigurationStates(IAXLangElement element) {
        this();
        theAxlElement = element;
    }

    public void addConfiguration(BddConfigurationStatus status) {
        theConfigurations.add(status);
    }

    public ArrayList<BddConfigurationStatus> getStatusOfAllConfigurations() {
        return theConfigurations;
    }

    public IAXLangElement axlElement() {
        return theAxlElement;
    }

    /**
	 * @brief Define sorting order based on the order of IAXLangElement
	 * @param that another AxlMetricsData_Component
	 * @return result of comparison
	 *  @retval <0 this element is less than the other
	 *  @retval 0 this element equals the other
	 *  @retval >0 this element is greater than the other
	 */
    public int compareTo(AxlElementConfigurationStates that) {
        if (this == that) {
            return 0;
        }
        return this.axlElement().compareTo(that.axlElement());
    }
}
