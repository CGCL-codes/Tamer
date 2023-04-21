package org.iptc.ines.factory.type;

import org.iptc.ines.NewsMLContext;
import org.iptc.ines.catalog.exception.ControledVocabularyException;
import org.iptc.nar.interfaces.datatype.FlexPropTypeException;
import org.iptc.nar.interfaces.datatype.QCodeType;

public interface IQCodeFactory {

    public abstract QCodeType createQcode(NewsMLContext context, String curie) throws FlexPropTypeException, ControledVocabularyException;

    /**
	 * Define a Qcode without validation. alias must be controlled before with a
	 * Catalog
	 * 
	 * @param alias
	 * @param code
	 * @return
	 * @throws FlexPropTypeException
	 * @throws ControledVocabularyException
	 */
    public abstract QCodeType createQcode(NewsMLContext context, String alias, String code) throws FlexPropTypeException, ControledVocabularyException;
}
