package org.force4spring.support;

import java.util.HashMap;
import java.util.Map;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;

public class FieldIndex extends HashMap<String, Field> {

    public FieldIndex(Map source) {
        super(source);
    }

    public FieldIndex(DescribeSObjectResult result) {
        Field[] fields = result.getFields();
        for (int i = 0; i < fields.length; i++) {
            put(fields[i].getName(), fields[i]);
        }
    }
}
