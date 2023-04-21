package org.corrib.jonto.beans.impl;

import org.corrib.jonto.DecimalTaxonomyEntry;
import org.corrib.jonto.JOnto;
import org.corrib.jonto.NonDecimalTaxonomyException;
import org.corrib.jonto.TaxonomyContext;
import org.corrib.jonto.db.rdf.TaxonomyStorage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This is context implementation for Decimal classifications
 * 
 * @author skruk
 *
 */
public class DecimalTaxonomyContext extends TaxonomyContext<DecimalTaxonomyEntry> {

    /**
	 * Holds context wide code creation pattern
	 */
    protected String codepattern = null;

    /**
	 * @param _label
	 * @param _description
	 * @param _namespaceURI
	 * @param _namespaceAbbr
	 * @param _isDecimal
	 */
    public DecimalTaxonomyContext(String _label, String _description, String _namespaceURI, String _namespaceAbbr, String _codepattern, TaxonomyStorage _storage) {
        super(_label, _description, _namespaceURI, _namespaceAbbr, true, _storage);
        this.codepattern = _codepattern;
    }

    @Override
    public DecimalTaxonomyEntry createEntry(String uri) {
        String _label = this.getStorage().getLiteral(uri, JOnto.JONTO_DC_TITLE);
        String _description = this.getStorage().getLiteral(uri, JOnto.JONTO_DC_DESCRIPTION);
        String code = this.getStorage().getLiteral(uri, JOnto.JONTO_DTAX_NUMBER);
        DecimalTaxonomyEntry entry = new DecimalTaxonomyEntryImpl(this, code, this.codepattern, _label, _description);
        return entry;
    }

    public DecimalTaxonomyEntry createEntry(Integer[] code, String _label) {
        DecimalTaxonomyEntryImpl result = new DecimalTaxonomyEntryImpl(this, code, this.codepattern);
        result.setLabel(_label);
        return result;
    }

    public DecimalTaxonomyEntry[] listMatchingByNumber(String _number, boolean ignoreCase) throws NonDecimalTaxonomyException {
        throw new NotImplementedException();
    }

    public DecimalTaxonomyEntry[] listMatchingByNumber(Integer[] _number) throws NonDecimalTaxonomyException {
        throw new NotImplementedException();
    }
}
