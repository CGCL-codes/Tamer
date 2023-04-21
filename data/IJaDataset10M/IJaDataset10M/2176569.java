package com.scully.korat;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.scully.korat.CandidateState;
import com.scully.korat.finitization.FieldDomain;
import com.scully.korat.finitization.ObjField;

/**
 * @author scully
 * 
 */
public class CandidateStateTest extends KoratBaseTest {

    CandidateState candidateState;

    @Before
    public void initCandidateState() throws Exception {
        this.candidateState = this.korat.getCandidateState();
    }

    /**
     * Test method for {@link com.scully.korat.CandidateState#initStateValueIndexes()}.
     */
    @Test
    public void testInitStateValueIndexes() {
        assertEquals("Wrong number of fields in map.", 12, this.candidateState.getStateValueIndexes().size());
        Collection<Integer> values = this.candidateState.getStateValueIndexes().values();
        Integer zero = new Integer(0);
        for (Integer index : values) {
            assertEquals("Field value index not set to zero.", zero, index);
        }
    }

    /**
     * Test method for
     * {@link com.scully.korat.CandidateState#setValueAtIndex(com.scully.korat.finitization.ObjField, int)}.
     */
    @Test
    public void testSetValueAtIndexObjFieldInt() {
        Map<ObjField, Integer> stateValueIndexes = new LinkedHashMap<ObjField, Integer>();
        ObjField field = null;
        int index = 0;
        for (int i = 0; i < this.candidateState.stateFields.length; i++) {
            field = this.candidateState.stateFields[i];
            if (boolean.class.equals(field.getField().getType())) {
                index = RandomUtils.nextInt(2);
            } else {
                index = RandomUtils.nextInt(3);
            }
            stateValueIndexes.put(field, new Integer(index));
            this.candidateState.setValueAtIndex(field, index);
        }
        for (int i = 0; i < this.candidateState.stateFields.length; i++) {
            field = this.candidateState.stateFields[i];
            FieldDomain fieldDomain = (FieldDomain) this.candidateState.stateSpace.get(field);
            Object indexValue = fieldDomain.getValueAtIndex(stateValueIndexes.get(field));
            assertEquals("Field value set to wrong index.", indexValue, field.getFieldValue());
        }
    }
}
