package com.cosmos.acacia.crm.assembling;

import com.cosmos.acacia.crm.data.assembling.AssemblingSchemaItemValue;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Miro
 */
public class FinalAlgorithmResult extends AlgorithmResult implements Serializable {

    private List<AssemblingSchemaItemValue> assemblingSchemaItemValues;

    public FinalAlgorithmResult(List<AssemblingSchemaItemValue> assemblingSchemaItemValues) {
        super(Type.Final);
        this.assemblingSchemaItemValues = assemblingSchemaItemValues;
    }

    @Override
    public List<AssemblingSchemaItemValue> getAssemblingSchemaItemValues() {
        return assemblingSchemaItemValues;
    }
}
