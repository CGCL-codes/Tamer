package org.structbeans.test.case2;

import org.structbeans.annotations.StructBean;
import org.structbeans.annotations.StructField;
import org.structbeans.core.ByteOrder;
import org.structbeans.core.FieldType;

@StructBean(byteOrder = ByteOrder.LITTLE_ENDIAN, attributeOrder = { "myValues" })
public class SampleBean {

    @StructField(fieldType = FieldType.UNSIGNED_INTEGER)
    private int yourValue;

    public SampleBean() {
    }

    public int getYourValue() {
        return yourValue;
    }

    public void setYourValue(int yourValue) {
        this.yourValue = yourValue;
    }
}
