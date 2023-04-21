package nl.alterra.openmi.sdk.wrapper;

import java.util.HashMap;
import org.openmi.standard.IDataType;
import org.openmi.standard.IQuantity;
import org.openmi.standard.IScalarSet;
import org.openmi.standard.ITime;
import org.openmi.standard.IValueSet;
import nl.alterra.openmi.sdk.backbone.ScalarSet;
import nl.alterra.openmi.sdk.buffer.SmartBuffer;
import nl.alterra.openmi.sdk.spatial.ElementMapper;

/**
 * SmartOutputLink class
 * a part of the smart wrapper engine
 */
public class SmartOutputLink extends SmartLink {

    private static final long serialVersionUID = 1L;

    private SmartBuffer smartBuffer = null;

    private ElementMapper elementMapper = null;

    private boolean useSpatialMapping = false;

    private HashMap bufferStates = null;

    /**
     * GETTER for smart buffer
     *
     * @return te smart buffer
     */
    public SmartBuffer getSmartBuffer() {
        return smartBuffer;
    }

    /**
     * the initialise implementation
     *
     * @see SmartLink#initialize(IRunEngine)
     */
    public void initialize(IRunEngine engineApiAccess) {
        int i;
        smartBuffer = new SmartBuffer();
        useSpatialMapping = false;
        bufferStates = new HashMap();
        this.engineApiAccess = engineApiAccess;
        HashMap dataOperationsHash = new HashMap();
        for (i = 0; i < link.getDataOperationsCount(); i++) {
            for (int n = 0; n < link.getDataOperation(i).getArgumentCount(); n++) {
                dataOperationsHash.put(link.getDataOperation(i).getArgument(n).getKey(), link.getDataOperation(i).getArgument(n).getValue());
            }
            if (dataOperationsHash.containsKey("Type")) {
                if (dataOperationsHash.get("Type") == "SpatialMapping") {
                    useSpatialMapping = true;
                    elementMapper = new ElementMapper();
                    elementMapper.initialise((String) dataOperationsHash.get("Description"), link.getSourceElementSet(), link.getTargetElementSet());
                }
            }
        }
    }

    /**
     * To update the buffer at a given time
     *
     * @param time the time of the update
     */
    public void updateBuffer(ITime time) throws Exception {
        if (link.getSourceDataType() != null && link.getSourceElementSet() != null) {
            IValueSet valueSet = this.engineApiAccess.getValues(link.getSourceDataType().getID(), link.getSourceElementSet().getID());
            if (useSpatialMapping == true) {
                this.smartBuffer.addValues(time, elementMapper.mapValues(valueSet));
            } else {
                this.smartBuffer.addValues(time, valueSet);
            }
        }
        smartBuffer.clearBefore(link.getTargetComponent().getEarliestInputTime());
    }

    /**
     * The getValue method
     *
     * @param time the time
     * @return the values
     */
    public IValueSet getValue(ITime time) {
        IValueSet values = null;
        try {
            values = smartBuffer.getValues(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertUnit(values);
    }

    /**
     * To keep the current buffer state
     *
     * @param bufferStateID the corresponding string ID of the buffer state
     */
    public void keepCurrentBufferState(String bufferStateID) {
        bufferStates.put(bufferStateID, new SmartBuffer(this.getSmartBuffer()));
    }

    /**
     * To restore the buffer state
     *
     * @param bufferStateID the corresponding string ID of the buffer state
     */
    public void restoreBufferState(String bufferStateID) {
        this.smartBuffer = new SmartBuffer((SmartBuffer) bufferStates.get(bufferStateID));
    }

    /**
     * To clear a given buffer state
     *
     * @param bufferStateID the corresponding string ID of the buffer state
     */
    public void clearBufferState(String bufferStateID) {
        bufferStates.remove(bufferStateID);
    }

    private IValueSet convertUnit(IValueSet values) {
        IDataType sourceDT = link.getSourceDataType();
        IDataType targetDT = link.getTargetDataType();
        if ((sourceDT instanceof IQuantity) && (targetDT instanceof IQuantity)) {
            double aSource = ((IQuantity) sourceDT).getUnit().getConversionFactorToSI();
            double bSource = ((IQuantity) sourceDT).getUnit().getOffSetToSI();
            double aTarget = ((IQuantity) targetDT).getUnit().getConversionFactorToSI();
            double bTarget = ((IQuantity) targetDT).getUnit().getOffSetToSI();
            if (aSource != aTarget || bSource != bTarget) {
                double[] x = new double[values.getCount()];
                for (int i = 0; i < values.getCount(); i++) {
                    x[i] = (((IScalarSet) values).getScalar(i) * aSource + bSource - bTarget) / aTarget;
                }
                return new ScalarSet(x);
            }
        }
        return values;
    }
}
