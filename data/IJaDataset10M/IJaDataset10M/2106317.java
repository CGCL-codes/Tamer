package org.odlabs.wiquery.ui.effects;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.core.ui.ICoreUIJavaScriptResourceReference;

/**
 * $Id: TransferEffectJavaScriptResourceReference.java 1143 2011-07-29 11:51:49Z
 * hielke.hoeve@gmail.com $
 * <p>
 * References the JavaScript resource to import the Transfer jQuery UI effect.
 * </p>
 * 
 * @author Julien Roche
 * @since 1.0
 */
public class TransferEffectJavaScriptResourceReference extends WiQueryJavaScriptResourceReference implements ICoreUIJavaScriptResourceReference {

    /** Constant of serialization */
    private static final long serialVersionUID = -6460737051632029822L;

    /**
	 * Singleton instance.
	 */
    private static TransferEffectJavaScriptResourceReference instance = new TransferEffectJavaScriptResourceReference();

    /**
	 * Default constructor
	 */
    private TransferEffectJavaScriptResourceReference() {
        super(CoreEffectJavaScriptResourceReference.class, "jquery.effects.transfer.js");
    }

    /**
	 * Returns the {@link TransferEffectJavaScriptResourceReference} instance.
	 */
    public static TransferEffectJavaScriptResourceReference get() {
        return instance;
    }

    @Override
    public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
        AbstractResourceDependentResourceReference[] list = new AbstractResourceDependentResourceReference[1];
        list[0] = CoreEffectJavaScriptResourceReference.get();
        return list;
    }
}
