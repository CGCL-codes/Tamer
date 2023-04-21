package it.dangelo.domjson;

import java.math.BigDecimal;

/**
 * Document object representation of a json number
 * @author Claudio D'Angelo
 *
 */
public interface JSONNumber extends JSONElement {

    /**
	 * Return the number value
	 * 
	 * @return the number value
	 * @throws DOMJsonException if an implementation error occurs
	 */
    BigDecimal getNumberValue() throws DOMJsonException;
}
