package com.volantis.mcs.eclipse.ab.editors.dom;

/**
 * A typesafe enumeration for specifying the configuration of an
 * ODOMObservableLabelProvider.
 */
public class ODOMLabelProviderConfiguration {

    /**
     * The OOLabelProviderConfiguration for just showing the individual
     * ODOMObservable i.e. none of its children if it has any.
     */
    public static final ODOMLabelProviderConfiguration JUST_OBSERVABLE = new ODOMLabelProviderConfiguration();

    /**
     * The OOLabelProviderConfiguration for showing the ODOMObservable
     * together with its children in the same label should there be any.
     */
    public static final ODOMLabelProviderConfiguration ELEMENT_AND_ATTRIBUTES = new ODOMLabelProviderConfiguration();

    /**
     * The OOLabelProviderConfiguration for showing only the children of
     * the ODOMObservable if there are any and all in the same label.
     */
    public static final ODOMLabelProviderConfiguration JUST_ATTRIBUTES = new ODOMLabelProviderConfiguration();

    /**
     * Construct a new OOLabelProviderConfiguration.
     */
    private ODOMLabelProviderConfiguration() {
    }
}
