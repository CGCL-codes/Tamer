package fulmine.protocol.wire;

import fulmine.model.field.containerdefinition.DescriptorField;

/**
 * Provides the {@link IWireIdentity} for {@link DescriptorField} objects. These
 * are always sent as SWF.
 * 
 * @author Ramon Servadei
 */
public class SWFWireIdentityRegistry implements IWireIdentityRegistry {

    public String getIdentityFor(IWireIdentity wireId) {
        return wireId.getAsString();
    }

    public IWireIdentity getWireIdentityFor(String identity) {
        return WireIdentity.get(identity);
    }
}
