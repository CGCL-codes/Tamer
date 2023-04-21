package com.rapidminer.operator.io;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;

/** 
 * Super class of all operators requiring no input and creating an {@link ExampleSet}.
 *  
 * @author Simon Fischer 
 */
public abstract class AbstractExampleSource extends AbstractReader<ExampleSet> {

    public AbstractExampleSource(OperatorDescription description) {
        super(description, ExampleSet.class);
    }

    @Override
    public MetaData getGeneratedMetaData() throws OperatorException {
        return new ExampleSetMetaData();
    }

    /** Creates (or reads) the ExampleSet that will be returned by {@link #apply()}. */
    public abstract ExampleSet createExampleSet() throws OperatorException;

    @Override
    public ExampleSet read() throws OperatorException {
        return createExampleSet();
    }
}
