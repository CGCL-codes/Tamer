package org.databene.model.data;

/**
 * Describes a group of components of which only one is present in an Entity.<br/><br/>
 * Created at 08.05.2008 19:17:59
 * @since 0.5.4
 * @author Volker Bergmann
 *
 */
public class AlternativeGroupDescriptor extends ComplexTypeDescriptor {

    public AlternativeGroupDescriptor(String name, DescriptorProvider provider) {
        super(name, provider);
    }
}
