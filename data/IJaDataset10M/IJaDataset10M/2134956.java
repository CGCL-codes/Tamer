package org.stanwood.media.store;

import org.stanwood.media.extensions.ExtensionInfo;
import org.stanwood.media.extensions.ExtensionType;
import org.stanwood.media.extensions.ParameterType;

/**
 * Extension information about the store {@link SapphireStore}
 */
public class SapphireStoreInfo extends ExtensionInfo<SapphireStore> {

    /**
	 * The constructor
	 */
    public SapphireStoreInfo() {
        super(SapphireStore.class.getName(), ExtensionType.SOURCE, new ParameterType[0]);
    }

    @Override
    protected SapphireStore createExtension() {
        return new SapphireStore();
    }
}
