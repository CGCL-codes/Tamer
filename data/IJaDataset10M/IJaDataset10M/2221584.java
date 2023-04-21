package com.iver.cit.gvsig.fmap.rendering;

import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

/**
 * Information of the legend that allows to be represented graphically to the user
 * 
 */
public interface IClassifiedLegend extends ILegend {

    /**
	 * Obtains the descriptions of the classes that are in the classification
	 * @return DOCUMENT ME!
	 */
    String[] getDescriptions();

    /**
	 * Obtains the symbols of each class of the classification for the user.
	 *
	 * @return DOCUMENT ME!
	 */
    ISymbol[] getSymbols();

    /**
	 * Obtains the values in relation with each class of the classification
	 *
	 * @return DOCUMENT ME!
	 */
    Object[] getValues();
}
