package org.echarts.edt.core;

import org.eclipse.core.runtime.CoreException;

/**
 * Exception thrown when there are problems in the ECharts Model.
 * @author Stephen Gevers
 *
 * @version $Revision: 1.5 $
 */
public class EChartsModelException extends CoreException {

    /**
	 * Serial number for serialization.
	 */
    private static final long serialVersionUID = 2627936955677587594L;

    /**
	 * Constructor for EChartsModelException.
	 * @param status IEChartsModelStatus the description of the model error.
	 */
    EChartsModelException(IEChartsModelStatus status) {
        super(status);
    }
}
