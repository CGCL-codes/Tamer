package org.processmining.framework.models.epcpack;

import java.io.*;
import java.util.*;
import org.processmining.framework.log.*;
import org.processmining.framework.models.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class EPCFunction extends EPCConfigurableObject implements LogEventProvider, DOTCodeWriter {

    private LogEvent logEvent;

    private ArrayList orgObjects = new ArrayList();

    private ArrayList dataObjects = new ArrayList();

    private ArrayList infSysObjects = new ArrayList();

    private void construct(LogEvent logModelElement) {
        this.logEvent = logModelElement;
        if (logEvent != null) {
            setIdentifier(logModelElement.getModelElementName() + "\\n" + logModelElement.getEventType());
        }
    }

    public EPCFunction(LogEvent logModelElement, ConfigurableEPC epc) {
        this(logModelElement, false, epc);
    }

    public EPCFunction(EPCFunction f) {
        super(f.isConfigurable(), (ConfigurableEPC) f.getSubgraph());
        construct(f.getLogEvent());
    }

    public EPCFunction(LogEvent logModelElement, boolean configurable, ConfigurableEPC epc) {
        super(configurable, epc);
        construct(logModelElement);
    }

    /**
	 *@deprecated Please now use getLogEvent()
	 *@see getLogEvent() This method returns the current LogEvent the
	 *      LogEventProvider has stored
	 * 
	 * @return LogEvent The stored LogEvent
	 */
    public LogEvent getModelElement() {
        return getLogEvent();
    }

    /**
	 *@deprecated Please now use setLogEvent(LogEvent le)
	 *@see setLogEvent(LogEvent le) This method sets the LogEvent for the
	 *      LogEventProvider to store
	 * 
	 * @param le
	 *            LogEvent The LogEvent to Store
	 */
    public void setModelElement(LogEvent le) {
        setLogEvent(le);
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }

    public void setLogEvent(LogEvent lme) {
        logEvent = lme;
    }

    public void writeDOTCode(Writer bw, HashMap nodeMapping) throws IOException {
        bw.write("node" + getId() + " [shape =\"box\",style=\"filled" + (isConfigurable() ? ",bold" : "") + "\",fillcolor=\"palegreen1\",label=\"");
        bw.write(getIdentifier());
        bw.write("\"];\n");
        nodeMapping.put(new String("node" + getId()), this);
        writeAdditionalObjectsDOTCode(bw, nodeMapping);
    }

    protected void writeAdditionalObjectsDOTCode(Writer bw, HashMap nodeMapping) throws IOException {
        if (orgObjects.size() > 0 && getEPC().showOrgObjects) {
            writeAdditionalDotCode(bw, nodeMapping, orgObjects, "org");
        }
        if (dataObjects.size() > 0 && getEPC().showDataObjects) {
            writeAdditionalDotCode(bw, nodeMapping, dataObjects, "data");
        }
        if (infSysObjects.size() > 0 && getEPC().showInfSysObjects) {
            writeAdditionalDotCode(bw, nodeMapping, infSysObjects, "infSys");
        }
    }

    private void writeAdditionalDotCode(Writer bw, HashMap nodeMapping, ArrayList additions, String label) throws IOException {
        bw.write(" subgraph cluster_" + label + "_" + getId() + " {\n");
        bw.write("  [ratio=\"auto\",nodesep=\".01\",ranksep=\".01\",ordering=\"out\",label=\"\",color=\"black\",rankdir=\"LR\"]\n");
        for (int i = 0; i < additions.size(); i++) {
            DOTCodeWriter o = (DOTCodeWriter) additions.get(i);
            o.writeDOTCode(bw, nodeMapping);
            if (i > 0) {
                bw.write("node" + ((DOTCodeWriter) additions.get(i - 1)).getId() + " -> node" + o.getId() + " [style=\"invis\"];\n");
            }
        }
        bw.write("}\n");
        DOTCodeWriter o = (DOTCodeWriter) additions.get(0);
        bw.write("node" + getId() + " -> node" + o.getId() + " [constraint=\"false\",arrowhead=\"none\",arrowtail=\"none\",lhead=\"cluster_" + label + "_" + getId() + "\"];\n");
    }

    private void addAdditionalObject(ModelGraphVertex v) {
    }

    private void removeAdditionalObject(ModelGraphVertex v) {
    }

    public void addOrgObject(EPCOrgObject orgObject) {
        addAdditionalObject(orgObject);
        orgObjects.add(orgObject);
    }

    public EPCOrgObject getOrgObject(int i) {
        return (EPCOrgObject) orgObjects.get(i);
    }

    public EPCOrgObject removeOrgObject(int i) {
        EPCOrgObject r = (EPCOrgObject) orgObjects.remove(i);
        removeAdditionalObject(r);
        return r;
    }

    public int getNumOrgObjects() {
        return orgObjects.size();
    }

    public void addDataObject(EPCDataObject dataObject) {
        addAdditionalObject(dataObject);
        dataObjects.add(dataObject);
    }

    public EPCDataObject getDataObject(int i) {
        return (EPCDataObject) dataObjects.get(i);
    }

    public EPCDataObject removeDataObject(int i) {
        EPCDataObject r = (EPCDataObject) dataObjects.remove(i);
        removeAdditionalObject(r);
        return r;
    }

    public int getNumDataObjects() {
        return dataObjects.size();
    }

    public void addInfSysObject(EPCInfSysObject infSysObject) {
        addAdditionalObject(infSysObject);
        infSysObjects.add(infSysObject);
    }

    public EPCInfSysObject getInfSysObject(int i) {
        return (EPCInfSysObject) infSysObjects.get(i);
    }

    public EPCInfSysObject removeInfSysObject(int i) {
        EPCInfSysObject r = (EPCInfSysObject) infSysObjects.remove(i);
        removeAdditionalObject(r);
        return r;
    }

    public int getNumInfSysObjects() {
        return infSysObjects.size();
    }
}
