package com.rapidminer.operator.ports.metadata;

import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ports.OutputPort;

/** Assigns a predefined meta data object to an output port. Useful if operators
 *  newly generate IOObjects. If the meta data changes
 *  dynamically, can be modified by overriding {@link #modifyMetaData(MetaData)}. 
 *  
 *  @author Simon Fischer */
public class GenerateNewMDRule implements MDTransformationRule {

    private OutputPort outputPort;

    private MetaData unmodifiedMetaData;

    public GenerateNewMDRule(OutputPort outputPort, Class<? extends IOObject> clazz) {
        this(outputPort, new MetaData(clazz));
    }

    public GenerateNewMDRule(OutputPort outputPort, MetaData unmodifiedMetaData) {
        this.outputPort = outputPort;
        this.unmodifiedMetaData = unmodifiedMetaData;
    }

    @Override
    public void transformMD() {
        MetaData clone = this.unmodifiedMetaData.clone();
        clone.addToHistory(outputPort);
        outputPort.deliverMD(modifyMetaData(clone));
    }

    /** Modifies the standard meta data before it is passed to the output. Can be used
	 *  if the transformation depends on parameters etc. The default implementation
	 *  just returns the original. Subclasses may safely modify the meta data, since
	 *  a copy is used for this method. */
    public MetaData modifyMetaData(MetaData unmodifiedMetaData) {
        return unmodifiedMetaData;
    }
}
