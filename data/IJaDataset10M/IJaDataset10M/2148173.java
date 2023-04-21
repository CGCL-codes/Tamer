package org.stanwood.media.store.mp4;

import org.stanwood.media.extensions.ExtensionInfo;
import org.stanwood.media.extensions.ExtensionType;
import org.stanwood.media.extensions.ParameterType;

/**
 * Extension information about the store {@link MP4ITunesStore}
 */
public class MP4ITunesStoreInfo extends ExtensionInfo<MP4ITunesStore> {

    /** The parameter type information for the parameter specifying the location of the AtomicParsley command */
    public static final ParameterType PARAM_ATOMIC_PARSLEY_KEY = new ParameterType("AtomicParsley", String.class, false);

    /** The parameter type information for the parameter used to change the manager class */
    public static final ParameterType PARAM_MANAGER_KEY = new ParameterType("manager", String.class, false);

    private static final ParameterType PARAM_TYPES[] = new ParameterType[] { PARAM_ATOMIC_PARSLEY_KEY, PARAM_MANAGER_KEY };

    /**
	 * The constructor
	 */
    public MP4ITunesStoreInfo() {
        super(MP4ITunesStore.class.getName(), ExtensionType.SOURCE, PARAM_TYPES);
    }

    @Override
    protected MP4ITunesStore createExtension() {
        return new MP4ITunesStore(this);
    }
}
