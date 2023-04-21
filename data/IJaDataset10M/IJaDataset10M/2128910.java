package org.cybergarage.upnp;

import java.util.logging.Logger;
import org.cybergarage.http.*;
import org.cybergarage.xml.*;
import org.cybergarage.util.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.xml.*;

/**
 *  UPnP State variable class. 
 *
 *  Notes:
 *
 *  This class mainly provides methods to access the underlying state
 *  variable XML nodes.  Data not associated with the device XML nodes
 *  are stored in a 'userObject node' of type StateVariableData. If you 
 *  are adding features to this class, add the actual data items to 
 *  StateVariableData, or things won't work right (without major mods
 *  elsewhere)
 */
public class StateVariable extends NodeData {

    private static Logger logger = Logger.getLogger("org.cybergarage.upnp");

    public static final String ELEM_NAME = "stateVariable";

    private Node stateVariableNode;

    private Node serviceNode;

    public Node getServiceNode() {
        return serviceNode;
    }

    public Service getService() {
        Node serviceNode = getServiceNode();
        if (serviceNode == null) return null;
        return new Service(serviceNode);
    }

    public Node getStateVariableNode() {
        return stateVariableNode;
    }

    public StateVariable() {
        this.serviceNode = null;
        this.stateVariableNode = new Node();
    }

    public StateVariable(Node serviceNode, Node stateVarNode) {
        this.serviceNode = serviceNode;
        this.stateVariableNode = stateVarNode;
    }

    public static boolean isStateVariableNode(Node node) {
        return StateVariable.ELEM_NAME.equals(node.getName());
    }

    private static final String NAME = "name";

    public void setName(String value) {
        getStateVariableNode().setNode(NAME, value);
    }

    public String getName() {
        return getStateVariableNode().getNodeValue(NAME);
    }

    private static final String DATATYPE = "dataType";

    public void setDataType(String value) {
        getStateVariableNode().setNode(DATATYPE, value);
    }

    public String getDataType() {
        return getStateVariableNode().getNodeValue(DATATYPE);
    }

    private static final String SENDEVENTS = "sendEvents";

    private static final String SENDEVENTS_YES = "yes";

    private static final String SENDEVENTS_NO = "no";

    public void setSendEvents(boolean state) {
        getStateVariableNode().setAttribute(SENDEVENTS, (state == true) ? SENDEVENTS_YES : SENDEVENTS_NO);
    }

    public boolean isSendEvents() {
        String state = getStateVariableNode().getAttributeValue(SENDEVENTS);
        if (state == null) return false;
        if (state.equalsIgnoreCase(SENDEVENTS_YES) == true) return true;
        return false;
    }

    public void set(StateVariable stateVar) {
        setName(stateVar.getName());
        setValue(stateVar.getValue());
        setDataType(stateVar.getDataType());
        setSendEvents(stateVar.isSendEvents());
    }

    public StateVariableData getStateVariableData() {
        Node node = getStateVariableNode();
        StateVariableData userData = (StateVariableData) node.getUserData();
        if (userData == null) {
            userData = new StateVariableData();
            node.setUserData(userData);
            userData.setNode(node);
        }
        return userData;
    }

    public void setValue(String value) {
        StateVariableData variableData = getStateVariableData();
        variableData.setValue(value);
        if (isSendEvents() == true) {
            Service service = getService();
            if (service == null) {
                logger.warning("no service for state variable!!!!");
                return;
            }
            service.notify(this);
        }
    }

    /**
   * Set variable value without sending event. Useful if stateVariable
   * instance resides on control point side
   */
    public void setValueNoEvent(String value) {
        StateVariableData variableData = getStateVariableData();
        variableData.setValue(value);
    }

    public void setValue(int value) {
        setValue(Integer.toString(value));
    }

    public void setValue(long value) {
        setValue(Long.toString(value));
    }

    public String getValue() {
        return getStateVariableData().getValue();
    }

    public boolean isDirty() {
        return getStateVariableData().isDirty();
    }

    public void setDirty() {
        getStateVariableData().setDirty();
    }

    public void clearDirty() {
        getStateVariableData().clearDirty();
    }

    public AllowedValueList getAllowedValueList() {
        AllowedValueList valueList = new AllowedValueList();
        Node valueListNode = getStateVariableNode().getNode(AllowedValueList.ELEM_NAME);
        if (valueListNode == null) return valueList;
        Node serviceNode = getServiceNode();
        int nNode = valueListNode.getNNodes();
        for (int n = 0; n < nNode; n++) {
            Node node = valueListNode.getNode(n);
            if (AllowedValue.isAllowedValueNode(node) == false) continue;
            String value = node.getValue();
            valueList.add(value);
        }
        return valueList;
    }

    public boolean hasAllowedValueList() {
        AllowedValueList valueList = getAllowedValueList();
        return (0 < valueList.size()) ? true : false;
    }

    public AllowedValueRange getAllowedValueRange() {
        Node valueRangeNode = getStateVariableNode().getNode(AllowedValueRange.ELEM_NAME);
        if (valueRangeNode == null) return null;
        return new AllowedValueRange(valueRangeNode);
    }

    public boolean hasAllowedValueRange() {
        return (getAllowedValueRange() != null) ? true : false;
    }

    public QueryListener getQueryListener() {
        return getStateVariableData().getQueryListener();
    }

    public void setQueryListener(QueryListener listener) {
        getStateVariableData().setQueryListener(listener);
    }

    public boolean performQueryListener(QueryRequest queryReq) {
        QueryListener listener = getQueryListener();
        if (listener == null) return false;
        QueryResponse queryRes = new QueryResponse();
        StateVariable retVar = new StateVariable();
        retVar.set(this);
        retVar.setValue("");
        retVar.setStatus(UPnPStatus.INVALID_VAR);
        if (listener.queryControlReceived(retVar) == true) {
            queryRes.setResponse(retVar);
        } else {
            UPnPStatus upnpStatus = retVar.getStatus();
            queryRes.setFaultResponse(upnpStatus.getCode(), upnpStatus.getDescription());
        }
        queryReq.post(queryRes);
        return true;
    }

    public QueryResponse getQueryResponse() {
        return getStateVariableData().getQueryResponse();
    }

    private void setQueryResponse(QueryResponse res) {
        getStateVariableData().setQueryResponse(res);
    }

    public UPnPStatus getQueryStatus() {
        return getQueryResponse().getUPnPError();
    }

    public boolean postQuerylAction() {
        QueryRequest queryReq = new QueryRequest();
        queryReq.setRequest(this);
        if (Debug.isOn() == true) queryReq.print();
        QueryResponse querylRes = queryReq.post();
        if (Debug.isOn() == true) querylRes.print();
        setQueryResponse(querylRes);
        int statCode = querylRes.getStatusCode();
        setStatus(statCode);
        if (statCode != HTTPStatus.OK) {
            setValue(querylRes.getReturnValue());
            return false;
        }
        setValue(querylRes.getReturnValue());
        return true;
    }

    private UPnPStatus upnpStatus = new UPnPStatus();

    public void setStatus(int code, String descr) {
        upnpStatus.setCode(code);
        upnpStatus.setDescription(descr);
    }

    public void setStatus(int code) {
        setStatus(code, UPnPStatus.code2String(code));
    }

    public UPnPStatus getStatus() {
        return upnpStatus;
    }
}
