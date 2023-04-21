package org.asoft.sapiente.component;

import java.lang.reflect.Method;

/**
 * Every component must define a 'processor method.' This is the method that
 * gets called by the framework and that perform the actual data processing
 * specific to each component.
 * 
 * A component processor method has very few limitations, as follows:
 * <ul>
 * <li>It must be annotated with the @Processor annotation;</li>
 * <li>It must return an object of type <code>ComponentStatus</code>;</li>
 * <li>It may accept zero, one, or two parameters. When accepting parameters,
 * these must be of type <code>DataRecord</code> or <code>Context</code>.</li>
 * </ul>
 * 
 * This class is a simple Java Bean style object that defines a Processor Method
 * instance.
 * 
 * Currently, a component may only define <b>one</b> processor method.
 * 
 * Created on Jul 24, 2009
 * 
 * @author Alex Silva
 * 
 */
public class ProcessorMethod {

    /**
	 * The actual method reference.
	 */
    private Method processorMethod;

    /**
	 * The parameter index that corresponds to the DataRecord object. It can be
	 * -1 (no DataRecord parameter; default), 0 or 1.
	 */
    private int dataRecordParamIndex = -1;

    /**
	 * The parameter index that corresponds to the Context object. It can be -1
	 * (no Context parameter; default), 0 or 1.
	 */
    private int contextParamIndex = -1;

    /**
	 * The component id to which this processor method is related to.
	 */
    private Object componentId;

    /**
	 * @return the processor method reference
	 */
    public Method getProcessorMethod() {
        return processorMethod;
    }

    public void setProcessorMethod(Method processorMethod) {
        this.processorMethod = processorMethod;
    }

    /**
	 * @return The parameter index that corresponds to the DataRecord object. It
	 *         can be -1 (no DataRecord parameter; default), 0 or 1.
	 */
    public int getDataRecordParamIndex() {
        return dataRecordParamIndex;
    }

    /**
	 * @param dataRecordParamIndex
	 *            the dataRecordParamIndex to set
	 */
    public void setDataRecordParamIndex(int dataRecordParamIndex) {
        this.dataRecordParamIndex = dataRecordParamIndex;
    }

    /**
	 * @return The parameter index that corresponds to the Context object. It
	 *         can be -1 (no Context parameter; default), 0 or 1.
	 */
    public int getContextParamIndex() {
        return contextParamIndex;
    }

    /**
	 * @param contextParamIndex
	 *            the contextParamIndex to set
	 */
    public void setContextParamIndex(int contextParamIndex) {
        this.contextParamIndex = contextParamIndex;
    }

    /**
	 * @return the component id.
	 */
    public Object getComponentId() {
        return componentId;
    }

    /**
	 * @param componentId
	 *            the component id to set
	 */
    public void setComponentId(Object componentId) {
        this.componentId = componentId;
    }
}
