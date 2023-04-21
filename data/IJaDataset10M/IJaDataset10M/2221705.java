package com.perfmon.testdomain.as;

import com.perfmon.callstack.Layer;
import com.perfmon.callstack.WorkflowManager;
import com.perfmon.utils.PerfUtils;

public class DisplayGroupRouterAS {

    /**
	 * Get Flowsheet Configuration for the currently logged on user.
	 * 
	 * @param requestDto
	 *            the request DTO.
	 * @param serviceContext
	 *            The Soarian service context.
	 * 
	 * @return GetFlowsheetConfigurationResponseDTO
	 */
    public String getFlowsheetConfiguration(String param1) {
        WorkflowManager.registerMethodBeginn(Layer.AS, DisplayGroupRouterAS.class, "getFlowsheetConfiguration");
        System.out.println("The AS was called...");
        PerfUtils.sleep(300);
        WorkflowManager.registerMethodEnd();
        return "Halooo";
    }
}
