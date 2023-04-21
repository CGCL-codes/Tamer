package org.activebpel.rt.bpel.impl.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.AeMessages;
import org.activebpel.rt.bpel.IAeVariable;
import org.activebpel.rt.bpel.impl.AeBusinessProcess;
import org.activebpel.rt.bpel.impl.AeVariableDeserializer;
import org.activebpel.rt.bpel.impl.activity.support.AeCorrelationSet;
import org.activebpel.rt.bpel.impl.fastdom.AeFastDocument;
import org.activebpel.rt.bpel.impl.visitors.AeProcessSnapshotVisitor;
import org.activebpel.rt.util.AeIntMap;
import org.w3c.dom.Document;

/**
 * Implements a process snapshot that contain all variables and correlation
 * sets that are live for normal or compensation processing.
 */
public class AeProcessSnapshot implements IAeProcessSnapshot {

    /** The process for this snapshot. */
    private AeBusinessProcess mProcess;

    /**
    * Maps location paths to 2nd-level maps that map version numbers to
    * correlation sets.
    */
    private Map mCorrelationSetLocationPathsMap;

    /** The set of correlation sets in the snapshot. */
    private Set mCorrelationSets;

    /**
    * Maps location paths to 2nd-level maps that map version numbers to
    * variables.
    */
    private Map mVariableLocationPathsMap;

    /** The set of variables in the snapshot. */
    private Set mVariables;

    /** The <code>Set</code> of pending <code>AeActivityInvokeImpl</code> instances. */
    private Set mPendingInvokes;

    /**
    * Constructor.
    *
    * @param aProcess
    */
    public AeProcessSnapshot(AeBusinessProcess aProcess) throws AeBusinessProcessException {
        setProcess(aProcess);
    }

    /**
    * Returns the correlation set with the specified location path and version
    * number.
    */
    protected AeCorrelationSet getCorrelationSet(String aLocationPath, int aVersionNumber) {
        AeIntMap versionNumbersMap = (AeIntMap) getCorrelationSetLocationPathsMap().get(aLocationPath);
        return (versionNumbersMap == null) ? null : (AeCorrelationSet) versionNumbersMap.get(aVersionNumber);
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#getCorrelationSetLocationPaths()
    */
    public Set getCorrelationSetLocationPaths() {
        return getCorrelationSetLocationPathsMap().keySet();
    }

    /**
    * Returns a <code>Map</code> from location paths to 2nd-level maps that map
    * version numbers to correlation sets.
    */
    protected Map getCorrelationSetLocationPathsMap() {
        if (mCorrelationSetLocationPathsMap == null) {
            Map locationPathsMap = new HashMap();
            for (Iterator i = getCorrelationSets().iterator(); i.hasNext(); ) {
                AeCorrelationSet correlationSet = (AeCorrelationSet) i.next();
                String locationPath = correlationSet.getLocationPath();
                AeIntMap versionNumbersMap = (AeIntMap) locationPathsMap.get(locationPath);
                if (versionNumbersMap == null) {
                    versionNumbersMap = new AeIntMap();
                    locationPathsMap.put(locationPath, versionNumbersMap);
                }
                int versionNumber = correlationSet.getVersionNumber();
                versionNumbersMap.put(versionNumber, correlationSet);
            }
            mCorrelationSetLocationPathsMap = locationPathsMap;
        }
        return mCorrelationSetLocationPathsMap;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#getCorrelationSetVersionNumbers(java.lang.String)
    */
    public Set getCorrelationSetVersionNumbers(String aLocationPath) {
        AeIntMap versionNumbersMap = (AeIntMap) getCorrelationSetLocationPathsMap().get(aLocationPath);
        return (versionNumbersMap == null) ? Collections.EMPTY_SET : versionNumbersMap.keySet();
    }

    /**
    * Returns the <code>Set</code> of correlation sets in the snapshot.
    */
    protected Set getCorrelationSets() {
        return mCorrelationSets;
    }

    /**
    * Returns the <code>Set</code> of pending <code>AeActivityInvokeImpl</code> instances.
    */
    public Set getPendingInvokes() {
        return mPendingInvokes;
    }

    /**
    * Returns the process for this snapshot.
    */
    protected AeBusinessProcess getProcess() {
        return mProcess;
    }

    /**
    * Returns the variable with the specified location path and version number.
    *
    * @throws AeBusinessProcessException if there is no such variable.
    */
    public IAeVariable getVariable(String aLocationPath, int aVersionNumber) throws AeBusinessProcessException {
        AeIntMap versionNumbersMap = (AeIntMap) getVariableLocationPathsMap().get(aLocationPath);
        if (versionNumbersMap == null) {
            throw new AeBusinessProcessException(AeMessages.getString("AeProcessSnapshot.ERROR_0") + getProcess().getProcessId() + ": " + aLocationPath);
        }
        IAeVariable variable = (IAeVariable) versionNumbersMap.get(aVersionNumber);
        if (variable == null) {
            throw new AeBusinessProcessException(AeMessages.getString("AeProcessSnapshot.ERROR_0") + getProcess().getProcessId() + ": " + aLocationPath + ", " + aVersionNumber);
        }
        return variable;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#getVariableLocationPaths()
    */
    public Set getVariableLocationPaths() {
        return getVariableLocationPathsMap().keySet();
    }

    /**
    * Returns a <code>Map</code> from location paths to 2nd-level maps that map
    * version numbers to variables.
    */
    protected Map getVariableLocationPathsMap() {
        if (mVariableLocationPathsMap == null) {
            Map locationPathsMap = new HashMap();
            for (Iterator i = getVariables().iterator(); i.hasNext(); ) {
                IAeVariable variable = (IAeVariable) i.next();
                String locationPath = variable.getLocationPath();
                AeIntMap versionNumbersMap = (AeIntMap) locationPathsMap.get(locationPath);
                if (versionNumbersMap == null) {
                    versionNumbersMap = new AeIntMap();
                    locationPathsMap.put(locationPath, versionNumbersMap);
                }
                int versionNumber = variable.getVersionNumber();
                versionNumbersMap.put(versionNumber, variable);
            }
            mVariableLocationPathsMap = locationPathsMap;
        }
        return mVariableLocationPathsMap;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#getVariableVersionNumbers(java.lang.String)
    */
    public Set getVariableVersionNumbers(String aLocationPath) {
        AeIntMap versionNumbersMap = (AeIntMap) getVariableLocationPathsMap().get(aLocationPath);
        return (versionNumbersMap == null) ? Collections.EMPTY_SET : versionNumbersMap.keySet();
    }

    /**
    * Returns the <code>Set</code> of variables in the snapshot.
    */
    protected Set getVariables() {
        return mVariables;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#serializeCorrelationSet(java.lang.String, int)
    */
    public AeFastDocument serializeCorrelationSet(String aLocationPath, int aVersionNumber) throws AeBusinessProcessException {
        throw new UnsupportedOperationException();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#serializeProcess(boolean)
    */
    public AeFastDocument serializeProcess(boolean aForPersistence) throws AeBusinessProcessException {
        return getProcess().fastSerializeState(aForPersistence);
    }

    /**
    * Serializes the variable
    * @param variable
    * @throws AeBusinessProcessException
    */
    public AeFastDocument serializeVariable(IAeVariable variable) throws AeBusinessProcessException {
        return getProcess().fastSerializeVariable(variable);
    }

    /**
    * Sets the process to use for this snapshot.
    *
    * @param aProcess
    * @throws AeBusinessProcessException
    */
    public void setProcess(AeBusinessProcess aProcess) throws AeBusinessProcessException {
        mProcess = aProcess;
        AeProcessSnapshotVisitor visitor = new AeProcessSnapshotVisitor();
        getProcess().accept(visitor);
        mCorrelationSets = visitor.getCorrelationSets();
        mVariables = visitor.getVariables();
        mPendingInvokes = visitor.getPendingInvokes();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#setCorrelationSetData(java.lang.String, int, org.w3c.dom.Document)
    */
    public void setCorrelationSetData(String aLocationPath, int aVersionNumber, Document aDocument) throws AeBusinessProcessException {
        throw new UnsupportedOperationException();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.storage.IAeProcessSnapshot#setVariableData(java.lang.String, int, org.w3c.dom.Document)
    */
    public void setVariableData(String aLocationPath, int aVersionNumber, Document aDocument) throws AeBusinessProcessException {
        IAeVariable variable = getVariable(aLocationPath, aVersionNumber);
        AeVariableDeserializer deserializer = new AeVariableDeserializer(getProcess().getEngine());
        deserializer.setVariable(variable);
        deserializer.restoreVariable(aDocument);
    }
}
