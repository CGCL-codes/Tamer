package edu.ucdavis.genomics.metabolomics.util.transform.crosstable.object;

import org.xml.sax.Attributes;

public class BinFormat<Type> extends HeaderFormat<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public BinFormat(Type value, Attributes a) {
        super(value, a);
    }

    public BinFormat(Type value) {
        super(value);
    }
}
