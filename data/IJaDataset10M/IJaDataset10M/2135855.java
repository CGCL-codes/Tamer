package org.jcryptool.crypto.flexiprovider.operations.xml.algorithms;

import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.xml.algorithms.paramspecs.AlgorithmParameterSpecElement;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jdom.Element;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

@SuppressWarnings("serial")
public class AlgorithmDescriptorElement extends Element {

    private String nameAttribute;

    private String typeAttribute;

    private AlgorithmParameterSpec algorithmParameterSpec;

    protected AlgorithmDescriptorElement() {
    }

    public AlgorithmDescriptorElement(AlgorithmDescriptor descriptor) {
        super("AlgorithmDescriptor");
        setAttribute("name", descriptor.getAlgorithmName());
        nameAttribute = getAttributeValue("name");
        setAttribute("type", descriptor.getType().getName());
        typeAttribute = getAttributeValue("type");
        if (descriptor.getAlgorithmParameterSpec() != null) {
            addContent(new AlgorithmParameterSpecElement(descriptor.getAlgorithmName(), descriptor.getAlgorithmParameterSpec()));
            algorithmParameterSpec = descriptor.getAlgorithmParameterSpec();
        }
    }

    public AlgorithmDescriptorElement(Element element) {
        super("AlgorithmDescriptor");
        setAttribute("name", element.getAttributeValue("name"));
        nameAttribute = getAttributeValue("name");
        setAttribute("type", element.getAttributeValue("type"));
        typeAttribute = getAttributeValue("type");
        if (element.getChild("AlgorithmParameterSpec") != null) {
            AlgorithmParameterSpecElement parameterSpecElement = new AlgorithmParameterSpecElement(element.getChild("AlgorithmParameterSpec"));
            algorithmParameterSpec = parameterSpecElement.getAlgorithmParameterSpec();
        }
    }

    public AlgorithmDescriptor getDescriptor() {
        return new AlgorithmDescriptor(getAlgorithmName(), getType(), getAlgorithmParameterSpec());
    }

    protected String getAlgorithmName() {
        return nameAttribute;
    }

    protected RegistryType getType() {
        return RegistryType.getTypeForName(typeAttribute);
    }

    protected AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return algorithmParameterSpec;
    }
}
