package takatuka.offlineGC.DFA.dataObjs.fields;

import java.util.*;
import takatuka.offlineGC.DFA.dataObjs.*;

/**
 * 
 * Description:
 * <p>
 * Unlike fieldinfos this field is use in GCHeap to indentify the type of a field.
 * A field is used only to store references. 
 * 
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class GCField {

    private int fieldNATIndex = -1;

    private HashSet referenceSet = new HashSet();

    public GCField(int fieldNATIndex) {
        this.fieldNATIndex = fieldNATIndex;
    }

    @Override
    public Object clone() {
        GCField newField = new GCField(fieldNATIndex);
        newField.add(referenceSet);
        return newField;
    }

    /**
     * 
     * @return
     */
    public int getFieldNATIndex() {
        return fieldNATIndex;
    }

    /**
     * 
     * @return
     */
    public HashSet<TTReference> get() {
        return (HashSet<TTReference>) referenceSet.clone();
    }

    /**
     * 
     * @param reference
     */
    public void add(TTReference reference) {
        referenceSet.add(reference);
    }

    /**
     * 
     * @param references
     */
    public void add(HashSet<TTReference> references) {
        referenceSet.addAll(references);
    }

    @Override
    public String toString() {
        return " GCField=[NaT=" + fieldNATIndex + ", References=" + referenceSet + "]";
    }
}
