package fr.inria.papyrus.uml4tst.emftext.alf.resource.alf;

/**
 * Implementors of this interface can provide a post-processor for text resources.
 */
public interface IAlfResourcePostProcessorProvider {

    /**
	 * Returns the processor that shall be called after text resource are successfully
	 * parsed.
	 */
    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfResourcePostProcessor getResourcePostProcessor();
}
