package org.charvolant.tmsnet.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * A test object for marshalling.
 * <p>
 * A list of records.
 * 
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@TMSNetElement(type = TMSNetType.COMMAND, id = 3004)
public class TestCommand4 {

    @TMSNetField(type = TMSNetFieldType.RECORD)
    private List<TestRecord2> values;

    public TestCommand4() {
        super();
        this.values = new ArrayList<TestRecord2>();
    }

    public void addValue(TestRecord2 value) {
        this.values.add(value);
    }

    public List<TestRecord2> getValues() {
        return this.values;
    }

    public void setValues(List<TestRecord2> values) {
        this.values = values;
    }
}
