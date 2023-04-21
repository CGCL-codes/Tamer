package com.volantis.devrep.repository.api.devices.policy.values;

import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.mcs.devices.policy.values.PolicyValue;

/**
 * Internal interface for {@link com.volantis.mcs.devices.policy.values.PolicyValue}.
 */
public interface InternalPolicyValue extends PolicyValue {

    /**
     * Get an immutable meta data representation of this value.
     *
     * @return The {@link ImmutableMetaDataValue}.
     */
    public ImmutableMetaDataValue createMetaDataValue();
}
