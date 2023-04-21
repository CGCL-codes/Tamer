package org.nightlabs.jfire.prop.validation;

import java.util.Collection;
import java.util.LinkedList;
import org.nightlabs.jfire.prop.DataBlock;
import org.nightlabs.jfire.prop.DataField;
import org.nightlabs.jfire.prop.StructBlock;
import org.nightlabs.jfire.prop.exception.PropertyException;
import org.nightlabs.jfire.prop.id.StructFieldID;

/**
 * Implementation of  {@link IPropertySetEvaluationContext} for {@link DataBlock}s.
 * It holds a single {@link DataBlock} along with the corresponding {@link StructBlock}.
 *
 * @author Tobias Langner <!-- tobias[dot]langner[at]nightlabs[dot]de -->
 */
public class DataBlockEvaluationContext implements IPropertySetEvaluationContext<DataField> {

    private DataBlock dataBlock;

    private StructBlock structBlock;

    public DataBlockEvaluationContext(DataBlock dataBlock, StructBlock structBlock) {
        this.dataBlock = dataBlock;
        this.structBlock = structBlock;
    }

    @Override
    public Collection<DataField> getDataFields(StructFieldID structFieldID) {
        Collection<DataField> dataFields = new LinkedList<DataField>();
        try {
            dataFields.add(dataBlock.getDataField(structFieldID));
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        }
        return dataFields;
    }

    public StructBlock getStructBlock() {
        return structBlock;
    }
}
