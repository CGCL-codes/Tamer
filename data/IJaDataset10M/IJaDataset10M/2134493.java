package takatuka.offlineGC.freeLocCalc.intraMethod.dataObjs;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class InvokeInstrDataController {

    private TreeMap<Long, InvokeInstrData> record = new TreeMap<Long, InvokeInstrData>();

    private static final InvokeInstrDataController myObj = new InvokeInstrDataController();

    /**
     * 
     */
    private InvokeInstrDataController() {
    }

    /**
     * 
     * @return
     */
    public static InvokeInstrDataController getInstanceOf() {
        return myObj;
    }

    /**
     * 
     */
    public void clear() {
        record.clear();
    }

    /**
     *
     * @param invokeData
     */
    public void add(InvokeInstrData invokeData) {
        record.put(invokeData.getInvokeInstrId(), invokeData);
    }

    /**
     * 
     * @param instrId
     * @return
     */
    public InvokeInstrData get(long instrId) {
        return record.get(instrId);
    }

    @Override
    public String toString() {
        String ret = "";
        Iterator<InvokeInstrData> it = record.values().iterator();
        while (it.hasNext()) {
            InvokeInstrData invInstrData = it.next();
            ret += invInstrData + "\n";
        }
        return ret;
    }
}
