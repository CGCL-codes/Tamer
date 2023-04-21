package com.volantis.mcs.protocols;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import java.util.HashMap;

/**
 * A factory to provide emulated tags. This factoryMock should have its release
 * method called at the end of any page that creates the factory and maintains
 * a reference to it.
 */
public final class TagEmulationFactory implements DevicePolicyConstants {

    /**
     * A cache of previously created emulation tags.
     */
    private final HashMap emulatedTags = new HashMap(10);

    /**
     * The InternalDevice in use.
     */
    private final InternalDevice device;

    /**
     * Construct a new TagEmulationFactory for the provided MarinerPageContext
     *
     * @param device the InternalDevice to use
     */
    public TagEmulationFactory(InternalDevice device) {
        this.device = device;
    }

    /**
     * Get an emulated emphasis tag appropriate for the current device and
     * protocol. The key the for tag is one of the EMULATE contants defined
     * in DevicePolicyConstants.
     *
     * @return the EmulateEmphasisTag specified by the tagKey or null if
     *         no such emphasis emulation could be created.
     */
    public EmulateEmphasisTag getTagEmphasisEmulation(String tagKey) {
        EmulateEmphasisTag tag = (EmulateEmphasisTag) emulatedTags.get(tagKey);
        if (tag != null) {
            return tag;
        }
        if (emulatedTags.containsKey(tagKey)) {
            return null;
        }
        tag = EmulateEmphasisTag.create(device, tagKey);
        emulatedTags.put(tagKey, tag);
        return tag;
    }
}
