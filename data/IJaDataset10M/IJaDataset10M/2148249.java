package takatuka.offlineGC.dataObjs.fields;

import takatuka.offlineGC.dataObjs.*;
import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.logic.constants.*;
import takatuka.classreader.logic.factory.*;
import takatuka.optimizer.cpGlobalization.dataObjs.constantPool.GCP.*;
import takatuka.optimizer.cpGlobalization.logic.util.*;
import takatuka.verifier.logic.dataflow.*;
import takatuka.verifier.logic.factory.*;
import takatuka.classreader.logic.util.*;
import takatuka.offlineGC.dataObjs.attribute.GCInstruction;

/**
 * 
 * Description:
 * <p>
 * Heap is corresponds to each newtype (Each new will create a heap)
 * Heap will have collections of CGFields
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class GCHeap implements GCHeapInterface {

    private int newInstrId = 0;

    private int classId = 0;

    /**
     * Key of the map is NameAndTypeIndex and value is the GCField
     */
    private HashMap<Integer, GCField> fields = new HashMap<Integer, GCField>();

    /**
     * 
     * @param newInstrId
     * @param classId
     */
    public GCHeap(int newInstrId, int classId) {
        this.newInstrId = newInstrId;
        this.classId = classId;
    }

    /**
     * If the newInstrId is equals to -1 then return. Otherwise,
     * first find field in the current class. If field does not exist then find it
     * in the superclasses and set the newId.
     *
     * @param nATIndex
     * @param value
     * @param method
     * @param callingParams
     * @param instr
     * @throws java.lang.Exception
     */
    @Override
    public void putField(int nATIndex, GCType value, MethodInfo method, Vector callingParams, GCInstruction instr) throws Exception {
        if (!value.isReference()) {
            System.err.println("Error # 2344");
            System.exit(1);
        }
        GCField field = fields.get(nATIndex);
        if (field == null) {
            field = new GCField(nATIndex);
            fields.put(nATIndex, field);
        }
        GCField dummy = new GCField(nATIndex);
        dummy.add(value.getReferences());
        gpRec.addForPutField(method, callingParams, dummy);
        field.add(value.getReferences());
    }

    /**
     * 
     * @return
     */
    public int getNewInstrId() {
        return newInstrId;
    }

    /**
     * 
     * @param nameAndTypeValue
     * @param method
     * @param callingParams
     * @param instr
     * @return
     */
    @Override
    public GCField getField(int nameAndTypeValue, MethodInfo method, Vector callingParams, GCInstruction instr) {
        GCField field = fields.get(nameAndTypeValue);
        gpRec.addForGetField(method, callingParams, field);
        return field;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GCHeap)) {
            return false;
        }
        GCHeap input = (GCHeap) obj;
        if (input.newInstrId == newInstrId) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.newInstrId;
        return hash;
    }

    @Override
    public String toString() {
        String ret = "GCHeap =";
        ret = ret + newInstrId + ", Fields = **{" + this.fields + "}**";
        return ret;
    }
}
