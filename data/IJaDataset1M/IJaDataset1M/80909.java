package com.volantis.mcs.eclipse.ab.editors.devices.types;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

/**
 * Designates a policy type that has a structure composition i.e. consists
 * of a series of name/value pairs represented by field elements.
 */
final class StructureComposition extends PolicyTypeComposition {

    /**
     * Constant for the supported types.
     */
    private static final PolicyType SUPPORTED_TYPES[] = new PolicyType[] { PolicyType.EMULATE_EMPHASIS_TAG };

    /**
     * Construct a new StructureComposition.
     */
    StructureComposition() {
        super("structure");
    }

    public PolicyValueModifier createPolicyValueModifier(Composite parent, int style, String policyName, DeviceRepositoryAccessorManager dram) {
        Element policyTypeElement = dram.getTypeDefinitionElement(policyName);
        PolicyType policyType = PolicyType.getType(policyTypeElement);
        return policyType.createPolicyValueModifier(parent, style, policyName, dram);
    }

    void addTypeElementImpl(Element parent, PolicyType type, JDOMFactory factory) {
        type.addPolicyTypeElement(parent, factory);
    }

    public void addDefaultPolicyValue(Element element, String policyName, PolicyType type, JDOMFactory factory, DeviceRepositoryAccessorManager dram) {
        type.addDefaultPolicyValue(element, policyName, factory, dram);
    }

    boolean canHandleType(String policyTypeName) {
        return policyTypeName.equals(DeviceRepositorySchemaConstants.POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME);
    }

    boolean canHandleType(Element type) {
        String typeName = type.getAttributeValue(DeviceRepositorySchemaConstants.POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME);
        return DeviceRepositorySchemaConstants.POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME.equals(typeName);
    }

    public PolicyType[] getSupportedPolicyTypes() {
        return SUPPORTED_TYPES;
    }

    PolicyType getPolicyTypeImpl(Element type) {
        String typeName = type.getAttributeValue(DeviceRepositorySchemaConstants.POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME);
        return PolicyType.getType(typeName);
    }
}
