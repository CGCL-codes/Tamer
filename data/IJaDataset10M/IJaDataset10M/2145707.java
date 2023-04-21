package ar.com.rab.beancipher.spi;

import ar.com.rab.beancipher.spi.exceptions.TypeCipherException;

/**
 * This is the adapter known by the bean cipher to be applied on the
 * configured type for a given property.
 * Each used cipher / decipher implementations mut implement this adapter interface.
 * 
 * @author Bajales Raul
 */
public interface ITypeCipherAdapter {

    /**
     * Perform the cipher operation on the input object
     * 
     * @param input
     * @return Object
     * @throws TypeCipherException
     */
    public Object cipher(Object input) throws TypeCipherException;

    /**
     * Perform the decipher operation on the input object
     * 
     * @param input
     * @return Object
     * @throws TypeCipherException
     */
    public Object deCipher(Object input) throws TypeCipherException;
}
