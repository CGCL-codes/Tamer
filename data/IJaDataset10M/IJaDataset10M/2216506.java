package org.apache.axis2.dataretrieval;

/**
 * Axis 2 Data Locator responsibles for retrieving Policy metadata. The class is
 * created as model for policy specific data locator; and also easier for any
 * future implementation policy specific data retrieval logic.
 */
public class PolicyDataLocator extends BaseAxisDataLocator implements AxisDataLocator {

    protected PolicyDataLocator() {
    }

    /**
	 * Constructor
	 */
    protected PolicyDataLocator(ServiceData[] data) {
        dataList = data;
    }
}
