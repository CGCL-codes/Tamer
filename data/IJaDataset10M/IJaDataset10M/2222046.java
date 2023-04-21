package org.databene.model.data;

/**
 * Serves as a placeholder for type descriptors of a type that is not yet resolved to simple or complex type.<br/><br/>
 * Created at 11.05.2008 23:13:51
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class UnresolvedTypeDescriptor extends TypeDescriptor {

    public UnresolvedTypeDescriptor(String name, DescriptorProvider provider) {
        this(name, provider, null);
    }

    public UnresolvedTypeDescriptor(String name, DescriptorProvider provider, String parentName) {
        super(name, provider, parentName);
    }
}
