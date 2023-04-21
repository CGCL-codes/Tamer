package org.sempere.commons.xsl.transformer;

/**
 * Interface that defines methods to perform XSL transformation.
 * 
 * @author bsempere
 */
public interface XslTransformer {

    /**
	 * Performs a XSL transformation using the given transformable instance
	 * 
	 * @param transformable
	 * @return String
	 */
    String transform(Transformable transformable);
}
