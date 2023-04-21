package org.oasis.wsrp.test.impl.portlet.flow;

import java.util.ArrayList;
import java.util.List;
import javax.portlet.RenderRequest;

public class Commander {

    private List cases;

    public Commander() {
        cases = new ArrayList();
    }

    public String processRequest(RenderRequest request) {
        String jspName = null;
        String currentCaseID = (String) request.getAttribute(FlowConstants.CURRENT_CASE_PARAM_NAME);
        if (currentCaseID == null) {
            if (cases.size() != 0) {
                Case caze = (Case) cases.get(0);
                setCurrentCase(request, caze.getID());
                setPassCounter(request, 1);
                jspName = caze.processRequest(request, 1);
            } else {
            }
        } else {
            int caseIndex = getCaseIndex(currentCaseID);
            if (caseIndex == -1) {
                throw new RuntimeException("The current case id '" + currentCaseID + "' does not correspondent to any known case");
            } else {
                Case caze = (Case) cases.get(caseIndex);
                String passCounterStr = (String) request.getAttribute(FlowConstants.PASS_COUNTER_PARAM_NAME);
                if (passCounterStr != null) {
                    int passCounter = 0;
                    try {
                        passCounter = Integer.parseInt(passCounterStr);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException("Malformed pass counter value (" + passCounterStr + ") in request", nfe);
                    }
                    if (caze.isFinished(passCounter)) {
                        if (caseIndex < (cases.size() - 1)) {
                            caseIndex++;
                            caze = (Case) cases.get(caseIndex);
                            setCurrentCase(request, caze.getID());
                            setPassCounter(request, 1);
                            jspName = caze.processRequest(request, 1);
                        } else {
                        }
                    } else {
                        passCounter++;
                        setPassCounter(request, passCounter);
                        jspName = caze.processRequest(request, passCounter);
                    }
                } else {
                    throw new RuntimeException("Pass counter missing in the request");
                }
            }
        }
        return jspName;
    }

    private int getCaseIndex(String caseID) {
        int caseCount = cases.size();
        for (int i = 0; i < caseCount; i++) {
            if (((Case) cases.get(i)).getID().equals(caseID)) {
                return i;
            }
        }
        return -1;
    }

    private void setPassCounter(RenderRequest request, int index) {
        request.setAttribute(FlowConstants.PASS_COUNTER_PARAM_NAME, "" + index);
    }

    private void setCurrentCase(RenderRequest request, String caseID) {
        request.setAttribute(FlowConstants.CURRENT_CASE_PARAM_NAME, caseID);
    }

    public void addCase(Case caze) {
        cases.add(caze);
    }
}
