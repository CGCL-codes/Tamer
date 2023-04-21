package com.iver.cit.gvsig.fmap.rendering;

import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

/**
 * Interface that offers the methods to classify the legend.
 */
public interface IClassifiedVectorLegend extends IClassifiedLegend, IVectorLegend {

    /**
	 * Deletes all the information of classification: 
	 * intervals, values, or other classifying elements
	 */
    public abstract void clear();

    /**
	 * Returns the name of the field
	 * 
	 * @return Name of the field.
	 * 
	 * TODO refactor to allow more than one field name
	 */
    public abstract String[] getClassifyingFieldNames();

    /**
	 * Inserts the name of the field
	 *
	 * @param fieldNames Names of the fields.
	 * TODO refactor to allow more than one field name
	 */
    public abstract void setClassifyingFieldNames(String[] fieldNames);

    /**
     * Inserts a symbol.
     *
     * @param key.
     * @param symbol.
     */
    public abstract void addSymbol(Object key, ISymbol symbol);

    /**
     * Deletes a symbol using for that its key which is the parameter of the
     * method.
     *
     * @param key clave.
     */
    public abstract void delSymbol(Object key);

    /**
	 * Removes <b>oldSymbol</b> from the Legend and substitutes it with the <b>newSymbol</b> 
	 * @param oldSymbol
	 * @param newSymbol
	 */
    public abstract void replace(ISymbol oldSymbol, ISymbol newSymbol);

    public int[] getClassifyingFieldTypes();

    public void setClassifyingFieldTypes(int[] fieldTypes);
}
