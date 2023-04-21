package org.dbe.composer.wfengine.bpel.impl;

import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.ISdlVariable;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.impl.fastdom.SdlFastDocument;
import org.dbe.composer.wfengine.bpel.impl.fastdom.SdlFastElement;
import org.dbe.composer.wfengine.bpel.impl.fastdom.SdlFastText;
import org.dbe.composer.wfengine.bpel.impl.fastdom.SdlForeignNode;
import org.w3c.dom.Element;

/**
 * Serializes a variable to an <code>SdlFastElement</code> or
 * <code>AeFastDocument</code>.
 */
public class SdlVariableSerializer extends SdlMessageDataSerializer implements ISdlImplStateNames {

    private static final Logger logger = Logger.getLogger(SdlVariableSerializer.class.getName());

    /** The variable to serialize. */
    private ISdlVariable mVariable;

    /** The resulting serialization. */
    private SdlFastElement mVariableElement;

    /**
     * Constructor.
     *
     * @param aTypeMapping The type mapping for simple types.
     */
    public SdlVariableSerializer(SdlTypeMapping aTypeMapping) {
        super(aTypeMapping);
        logger.debug("SdlVariableSerializer()");
    }

    /**
     * Serializes the specified variable to an <code>SdlFastElement</code>.
     *
     * @param aVariable
     */
    protected SdlFastElement createVariableElement(ISdlVariable aVariable) throws SdlBusinessProcessException {
        SdlFastElement variableElement = new SdlFastElement(STATE_VAR);
        boolean hasData = aVariable.hasData();
        variableElement.setAttribute(STATE_NAME, aVariable.getName());
        variableElement.setAttribute(STATE_DATA, "yes");
        variableElement.setAttribute(STATE_HASDATA, "" + hasData);
        variableElement.setAttribute(STATE_VERSION, "" + aVariable.getVersionNumber());
        if (hasData) {
            variableElement = getData(variableElement, aVariable);
        }
        return variableElement;
    }

    private SdlFastElement getData(SdlFastElement variableElement, ISdlVariable aVariable) throws SdlBusinessProcessException {
        if (aVariable.getType() != null) {
            String data = getTypeMapping().serialize(aVariable.getTypeData());
            variableElement.appendChild(new SdlFastText(data));
        } else if (aVariable.getElement() != null) {
            Element data = aVariable.getElementData();
            variableElement.appendChild(new SdlForeignNode(data));
        } else if (aVariable.getMessageData() != null) {
            appendMessageDataParts(variableElement, aVariable.getMessageData());
        } else {
            logger.error("No data for variable that claims to have data!");
            throw new SdlBusinessProcessException("No data for variable that claims to have data!");
        }
        return variableElement;
    }

    /**
     * Returns an <code>AeFastDocument</code> representing the variable.
     */
    public SdlFastDocument getVariableDocument() throws SdlBusinessProcessException {
        return new SdlFastDocument(getVariableElement());
    }

    /**
     * Returns an <code>SdlFastElement</code> representing the variable.
     */
    public SdlFastElement getVariableElement() throws SdlBusinessProcessException {
        if (mVariableElement == null) {
            if (mVariable == null) {
                logger.error("No variable to serialize!");
                throw new SdlBusinessProcessException("No variable to serialize!");
            }
            mVariableElement = createVariableElement(mVariable);
        }
        return mVariableElement;
    }

    /**
     * Sets the variable to serialize.
     *
     * @param aVariable
     */
    public void setVariable(ISdlVariable aVariable) {
        mVariable = aVariable;
        mVariableElement = null;
    }
}
